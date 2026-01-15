package com.gufli.hytale.toolbox.scheduler;

/**
 * A task that is scheduled.
 */
public interface SchedulerTask {

    /**
     * Cancels the task
     */
    void cancel();
}