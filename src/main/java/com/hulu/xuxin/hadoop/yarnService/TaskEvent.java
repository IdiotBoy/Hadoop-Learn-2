package com.hulu.xuxin.hadoop.yarnService;


import org.apache.hadoop.yarn.event.AbstractEvent;

public class TaskEvent extends AbstractEvent<TaskEventType>{
    private String taskID;

    public TaskEvent(String taskID, TaskEventType type) {
        super(type);
        this.taskID = taskID;
    }

    public String getTaskID() {
        return taskID;
    }
}
