package com.hulu.xuxin.hadoop.yarnStateMachine;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.hulu.xuxin.hadoop.yarnService.JobEvent;
import com.hulu.xuxin.hadoop.yarnService.JobEventType;

@SuppressWarnings("unchecked")
public class SimpleMRAppMasterTest2 {
    public static void main(String[] args) throws Exception {
        String jobID = "job_20131215_12";
        SimpleMRAppMaster2 appMaster = new SimpleMRAppMaster2("Simple MRAppMaster", jobID);
        YarnConfiguration conf = new YarnConfiguration(new Configuration());
        appMaster.serviceInit(conf);
        appMaster.serviceStart();
        appMaster.getDispatcher().getEventHandler().handle(
            new JobEvent(jobID, JobEventType.JOB_INIT));

        Thread.sleep(3000);
        appMaster.close();
    }
}
