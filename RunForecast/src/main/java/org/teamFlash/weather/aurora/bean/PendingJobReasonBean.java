package org.teamFlash.weather.aurora.bean;

import org.teamFlash.weather.aurora.client.sdk.PendingReason;

import java.util.Set;

/**
 * Created by girish on 12/10/16.
 */
public class PendingJobReasonBean  extends ResponseBean  {
    /**
     * Instantiates a new pending job response bean.
     *
     * @param responseBean the response bean
     */
    private Set<PendingReason> reasons;

    public PendingJobReasonBean(ResponseBean responseBean) {
        this.setResponseCode(responseBean.getResponseCode());
        this.setServerInfo(responseBean.getServerInfo());
    }

    /**
     * Gets the reasons.
     *
     * @return the reasons
     */
    public Set<PendingReason> getReasons() {
        return reasons;
    }

    /**
     * Sets the reasons.
     *
     * @param reasons the new reasons
     */
    public void setReasons(Set<PendingReason> reasons) {
        this.reasons = reasons;
    }

    /* (non-Javadoc)
     * @see org.apache.airavata.cloud.aurora.client.bean.ResponseBean#toString()
     */
    @Override
    public String toString() {
        return "PendingJobResponseBean [reasons=" + reasons + "]";
    }
}
