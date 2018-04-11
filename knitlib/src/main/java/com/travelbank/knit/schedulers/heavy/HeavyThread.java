package com.travelbank.knit.schedulers.heavy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * Base class for all threads in {@link HeavyTaskScheduler} pool.
 *
 * @author Omer Ozer
 */

public abstract class HeavyThread extends IntentService {

    /**
     * Key for the task type inside the start {@link Intent}
     */
    private static final String TASK_TYPE_KEY = "TASK_TYPE_KEY";

    /**
     * Runnable task type.
     */
    public static final int RUNNABLE = 1;

    /**
     * Callable task type.
     */
    public static final int CALLABLE = 2;


    /**
     * {@link Map} that maps thread Ids to their own {@link ConcurrentLinkedQueue}s of {@link TaskPackage}.
     */
    private static Map<String,ConcurrentLinkedQueue<TaskPackage>> taskMap;

    /**
     * This methods starts {@link HeavyThread}s.
     * @param threadId Unique identifier for the {@link HeavyThread}
     * @param taskPackage Package that contains the task to be handled.
     * @param context Android {@link Context} to help start the {@link HeavyThread}s
     * @param taskThread The exact thread type to be used for the thread pool.({@code HThread1, HThread2... etc})
     */
    public static void handleTask(String threadId, TaskPackage taskPackage,Context context,Class<? extends HeavyThread> taskThread){
        getTaskQueueForThread(threadId).add(taskPackage);
        Intent intent = new Intent(context,taskThread);
        intent.putExtra(TASK_TYPE_KEY,taskPackage.getRunnable()==null? CALLABLE:RUNNABLE);
        context.startService(intent);
    }

    /**
     * Static initialization of the taskMap}
     */
    static {
        taskMap = new ConcurrentHashMap<>();
    }


    /**
     * Polls the next task from the queue for the given thread id.
     * @param threadId Given thread id .
     * @return Next task wrapper around {@link TaskPackage}
     */
    private static TaskPackage getNextTask(String threadId){
        return taskMap.get(threadId).poll();
    }


    /**
     * Returns the task queue for the given thread id.
     * @param threadId Given thread id .
     * @return Task queue for the given thread id.
     */
    private static Queue<TaskPackage> getTaskQueueForThread(String threadId){
        if(!taskMap.containsKey(threadId)) {
            taskMap.put(threadId, new ConcurrentLinkedQueue<TaskPackage>());
        }
        return taskMap.get(threadId);
    }

    /**
     * Returns the priority of the active {@link HeavyThread} with the given id.
     * Priority is the number of current active tasks. A priority of 0 means, the thread is the most
     * available to receive an incoming task.
     * @param threadId
     * @return
     */
    public static int getPriority(String threadId){
        return getTaskQueueForThread(threadId).size();
    }

    /**
     * Unique thread id for the current thread.
     */
    private String threadId;

    public HeavyThread(String name) {
        super(name);
        this.threadId = name;
    }

    /**
     * @see IntentService
     * @param intent intent that's sent to start the thread.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final TaskPackage currentTask = getNextTask(threadId);
        switch (intent.getIntExtra(TASK_TYPE_KEY,RUNNABLE)){
            case RUNNABLE:
                currentTask.getRunnable().run();
                currentTask.getCurrent().shutDown();
                break;
            case CALLABLE:
                try {
                    final Object data = currentTask.getCallable().call();
                    currentTask.getTarget().start();
                    currentTask.getTarget().submit(new Runnable() {
                        @Override
                        public void run() {
                            currentTask.getConsumer().consume(data);
                            currentTask.getCurrent().shutDown();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;

        }
    }


}
