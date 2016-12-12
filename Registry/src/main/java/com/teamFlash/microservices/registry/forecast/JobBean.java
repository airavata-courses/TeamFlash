package com.teamFlash.microservices.registry.forecast;

public class JobBean {

    String user;
    int jobid;
    String taskid;
    String taskStatus;
    String taskServer;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskServer() {
        return taskServer;
    }

    public void setTaskServer(String taskServer) {
        this.taskServer = taskServer;
    }
}
