package com.omerozer.knit.schedulers.heavy;

import com.omerozer.knit.schedulers.Consumer;
import com.omerozer.knit.schedulers.SchedulerInterface;

import java.util.concurrent.Callable;

/**
 *
 * This class contains everything that a {@link HeavyThread} needs to perform a task.
 *
 * @author Omer Ozer
 */

public class TaskPackage {

    /**
     * Constant {@link TaskPackage} for empty tasks.
     */
    public static final TaskPackage EMPTY = new TaskPackage();

    /**
     * {@link SchedulerInterface} that is sending this package
     */
    private SchedulerInterface current;

    /**
     * {@link SchedulerInterface} that is going to handle consumer task once the current task is complete.
     */
    private SchedulerInterface target;

    /**
     * {@link Consumer} class that will handle the result of the task.
     */
    private Consumer consumer;

    /**
     * {@link Runnable} that contains the task.
     */
    private Runnable runnable;

    /**
     * {@link Callable} that contains the task and generates a result.
     */
    private Callable callable;

    /**
     * Returns the scheduler instance that this task is coming from.
     * @return Scheduler instance that this task is coming from
     */
    public SchedulerInterface getCurrent() {
        return current;
    }

    /**
     * Set the scheduler instance that this task is coming from.
     * @param current Scheduler instance that this task is coming from
     */
    public void setCurrent(SchedulerInterface current) {
        this.current = current;
    }

    /**
     * Returns the target {@link SchedulerInterface} that will run the consume part of the task.
     * @return Target {@link SchedulerInterface} that will run the consume part of the task.
     */
    public SchedulerInterface getTarget() {
        return target;
    }

    /**
     * Set the target {@link SchedulerInterface} that will run the consume part of the task.
     * @param  target Target {@link SchedulerInterface} that will run the consume part of the task.
     */
    public void setTarget(SchedulerInterface target) {
        this.target = target;
    }

    /**
     * Returns the Consumer object that will handle the consume flow.
     * @return Consumer object that will handle the consume flow.
     */
    public Consumer getConsumer() {
        return consumer;
    }

    /**
     * Set the Consumer object that will handle the consume flow.
     * @param  consumer Consumer object that will handle the consume flow.
     */
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Returns the runnable task that is being sent.
     * @return The runnable task that is being sent.
     */
    public Runnable getRunnable() {
        return runnable;
    }

    /**
     * Set the runnable task that is being sent.
     * @param runnable The runnable task that is being sent.
     */
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Return the callable task that generates the data for the {@link Consumer} .
     * @return the callable task that generates the data for the {@link Consumer} .
     */
    public Callable getCallable() {
        return callable;
    }

    /**
     * Set the callable task that generates the data for the {@link Consumer} .
     * @param callable Set the callable task that generates the data for the {@link Consumer} .
     */
    public void setCallable(Callable callable) {
        this.callable = callable;
    }
}
