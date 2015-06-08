package com.hulu.xuxin.hadoop.yarnStateMachine;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;

import com.hulu.xuxin.hadoop.yarnService.JobEventType;

public class SimpleMRAppMaster2 extends CompositeService {
    private Dispatcher dispatcher;
    private String jobID;

    public SimpleMRAppMaster2(String name, String jobID) {
        super(name);
        this.jobID = jobID;
    }

    public void serviceInit(final Configuration conf) throws Exception {
        dispatcher = new AsyncDispatcher();
        dispatcher.register(JobEventType.class, new JobStateMachine(jobID, dispatcher.getEventHandler()));
        addService((Service)dispatcher);
        super.serviceInit(conf);
    }

    public void serviceStart() throws Exception {
        super.serviceStart();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
