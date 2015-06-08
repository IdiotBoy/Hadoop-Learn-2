package com.hulu.xuxin.hadoop.yarnService;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

@SuppressWarnings("unchecked")
public class SimpleMRAppMasterTest {
    public static void main(String[] args) throws Exception {
        String jobID = "job_20131215_12";
        SimpleMRAppMaster appMaster = new SimpleMRAppMaster("Simple MRAppMaster", jobID, 5);
        YarnConfiguration conf = new YarnConfiguration(new Configuration());
        appMaster.serviceInit(conf);
        appMaster.serviceStart();
        appMaster.getDispatcher().getEventHandler().handle(
            new JobEvent(jobID, JobEventType.JOB_KILL));
        appMaster.getDispatcher().getEventHandler().handle(
            new JobEvent(jobID, JobEventType.JOB_INIT));

        Thread.sleep(3000);
        appMaster.close();
    }
}
