package org.teamFlash.weather.aurora.bean;

import org.teamFlash.weather.aurora.client.sdk.ScheduledTask;

import java.util.List;

/**
 * Created by girish on 12/10/16.
 */
public class JobDetailsResponseBean  extends ResponseBean  {
    /**
     * Instantiates a new job details response bean.
     *
     * @param responseBean the response bean
     */
    public JobDetailsResponseBean(ResponseBean responseBean) {
        this.setResponseCode(responseBean.getResponseCode());
        this.setServerInfo(responseBean.getServerInfo());
    }

    /** The tasks. */
    private List<ScheduledTask> tasks;

    /**
     * Gets the tasks.
     *
     * @return the tasks
     */
    public List<ScheduledTask> getTasks() {
        return tasks;
    }

    /**
     * Sets the tasks.
     *
     * @param tasks the new tasks
     */
    public void setTasks(List<ScheduledTask> tasks) {
        this.tasks = tasks;
    }

    /* (non-Javadoc)
     * @see org.apache.airavata.cloud.aurora.client.bean.ResponseBean#toString()
     */
    @Override
    public String toString() {
        return "JobDetailsResponseBean [tasks=" + tasks + "]";
    }
}


