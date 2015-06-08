package com.hulu.xuxin.hadoop.yarnService;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;

@SuppressWarnings("unchecked")
public class SimpleMRAppMaster extends CompositeService {
    private Dispatcher dispatcher;
    private String jobID;
    private int taskNumber;
    private String[] taskIDs;

    public SimpleMRAppMaster(String name, String jobID, int taskNumber) {
        super(name);
        this.jobID = jobID;
        this.taskNumber = taskNumber;
        taskIDs = new String[taskNumber];
        for (int i = 0; i < taskNumber; i++)
            taskIDs[i] = new String(jobID + "_task_" + i);
    }

    public void serviceInit(final Configuration conf) throws Exception {
        dispatcher = new AsyncDispatcher();
        dispatcher.register(JobEventType.class, new JobEventDispather());
        dispatcher.register(TaskEventType.class, new TaskEventDispather());
        addService((Service)dispatcher);
        super.serviceInit(conf);
    }

    public void serviceStart() throws Exception {
        super.serviceStart();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    private class JobEventDispather implements EventHandler<JobEvent> {
        @Override
        public void handle(JobEvent event) {
            if (event.getType() == JobEventType.JOB_KILL) {
                System.out.println("Receive JOB_KILL event, killing all the tasks");
                for (int i = 0; i < taskNumber; i++)
                    dispatcher.getEventHandler().handle(new TaskEvent(taskIDs[i], TaskEventType.T_KILL));
            } else if (event.getType() == JobEventType.JOB_INIT) {
                System.out.println("Receive JOB_INIT event, scheduling tasks");
                for (int i = 0; i < taskNumber; i++)
                    dispatcher.getEventHandler().handle(new TaskEvent(taskIDs[i], TaskEventType.T_SCHEDULE));
            }
        }
    }

    private class TaskEventDispather implements EventHandler<TaskEvent> {
        @Override
        public void handle(TaskEvent event) {
            if (event.getType() == TaskEventType.T_KILL) {
                System.out.println("Receive T_KILL event of task " + event.getTaskID());
            } else if (event.getType() == TaskEventType.T_SCHEDULE) {
                System.out.println("Receive T_SCHEDUE event of task " + event.getTaskID());
            }
        }
    }

}
