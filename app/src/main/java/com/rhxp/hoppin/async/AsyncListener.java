package com.rhxp.hoppin.async;

/**
 * Created by Ricky on 11/17/14.
 */
public interface AsyncListener {
    /**
     * Gets called when task is complete. If success is false, result will be null
     * @param taskCode
     * @param success
     * @param result
     */
    public void onTaskComplete(TaskCode taskCode, boolean success, Object result);
}
