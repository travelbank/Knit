package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 2/26/18.
 */

public class KnitTaskFlow {

    public static<T> ScheduleFlower<T> create(Callable<T> callable){
        return new ScheduleFlower<T>().task(callable);
    }

    public static class ScheduleFlower<T>{

        private Callable<T> task;
        private SchedulerInterface on;
        private SchedulerInterface target;


        public ScheduleFlower task(Callable<T> callable){
            this.task = callable;
            return this;
        }

        public ScheduleFlower runOn(SchedulerInterface scheduler){
            this.on = scheduler;
            return this;
        }

        public ScheduleFlower consumeOn(SchedulerInterface scheduler){
            this.target = scheduler;
            return this;
        }

        public void start(Consumer<T> consumer){
            this.on.setTargetAndConsumer(target,consumer);
            this.on.start();
            this.on.submit(task);
        }

    }

}
