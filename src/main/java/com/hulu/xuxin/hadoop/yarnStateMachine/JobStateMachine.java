package com.hulu.xuxin.hadoop.yarnStateMachine;

import java.util.EnumSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.InvalidStateTransitonException;
import org.apache.hadoop.yarn.state.MultipleArcTransition;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;

import com.hulu.xuxin.hadoop.yarnService.JobEvent;
import com.hulu.xuxin.hadoop.yarnService.JobEventType;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class JobStateMachine implements EventHandler<JobEvent> {
    private final String jobID;
    private EventHandler eventHandler;
    private final Lock writeLock;
    private final Lock readLock;


    protected static final StateMachineFactory<JobStateMachine, JobStateInternal, JobEventType, JobEvent>
        stateMachineFactory
            = new StateMachineFactory<JobStateMachine, JobStateMachine.JobStateInternal, JobEventType, JobEvent>(JobStateInternal.NEW)
                  .addTransition(JobStateInternal.NEW, JobStateInternal.INITED, JobEventType.JOB_INIT, new InitTransition())
                  .addTransition(JobStateInternal.INITED, JobStateInternal.SETUP, JobEventType.JOB_START, new StartTransition())
                  .addTransition(JobStateInternal.SETUP, JobStateInternal.RUNNING, JobEventType.JOB_SETUP_COMPLETED, new SetupCompletedTransition())
                  .addTransition(JobStateInternal.RUNNING, EnumSet.of(JobStateInternal.KILLED, JobStateInternal.SUCCEEDED),
                      JobEventType.JOB_COMPLETED, new JobTasksCompletedTransition())
                  .installTopology();

    private final StateMachine<JobStateInternal, JobEventType, JobEvent> stateMachine;

    public JobStateMachine(String jobID, EventHandler eventHandler) {
        this.jobID = jobID;
        this.eventHandler = eventHandler;
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        stateMachine = stateMachineFactory.make(this);
    }

    protected StateMachine<JobStateInternal, JobEventType, JobEvent> getStateMachine() {
        return stateMachine;
    }

    public static class InitTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
        @Override
        public void transition(JobStateMachine job, JobEvent event) {
            System.out.println("Receiving event " + event);
            job.eventHandler.handle(new JobEvent(job.jobID, JobEventType.JOB_START));
        }
    }

    public static class StartTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
        @Override
        public void transition(JobStateMachine job, JobEvent event) {
            System.out.println("Receiving event " + event);
            job.eventHandler.handle(new JobEvent(job.jobID, JobEventType.JOB_SETUP_COMPLETED));
        }
    }

    public static class SetupCompletedTransition implements SingleArcTransition<JobStateMachine, JobEvent> {
        @Override
        public void transition(JobStateMachine job, JobEvent event) {
            System.out.println("Receiving event " + event);
            job.eventHandler.handle(new JobEvent(job.jobID, JobEventType.JOB_COMPLETED));
        }
    }

    public static class JobTasksCompletedTransition implements MultipleArcTransition<JobStateMachine, JobEvent, JobStateInternal> {
        @Override
        public JobStateInternal transition(JobStateMachine job, JobEvent event) {
            System.out.println("Receiving event " + event);
            return JobStateInternal.SUCCEEDED;
        }
    }

    @Override
    public void handle(JobEvent event) {
        try {
            writeLock.lock();
            JobStateInternal oldState = getInternalState();
            try {
                getStateMachine().doTransition(event.getType(), event);
            } catch (InvalidStateTransitonException e) {
                System.out.println("Can't handle this event at current state");
            }
            if (oldState != getInternalState()) {
                System.out.println("Job Transitioned from " + oldState + " to " + getInternalState());
            }
        } finally {
            writeLock.unlock();
        }
    }

    public JobStateInternal getInternalState() {
        readLock.lock();
        try {
            return getStateMachine().getCurrentState();
        } finally {
            readLock.unlock();
        }
    }

    public enum JobStateInternal {
        NEW,
        SETUP,
        INITED,
        RUNNING,
        SUCCEEDED,
        KILLED,
    }


}
