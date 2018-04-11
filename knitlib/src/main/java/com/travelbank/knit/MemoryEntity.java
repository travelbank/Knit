package com.travelbank.knit;

/**
 * All internal components of Knit such as {@link InternalModel} & {@link InternalPresenter} extend this class.
 * Provides {@link com.travelbank.knit.components.graph.UsageGraph} and the user with lifecycle callbacks.
 *
 * @see com.travelbank.knit.components.graph.UsageGraph
 * @author Omer Ozer
 */

public interface MemoryEntity {

    /**
     * Called when entity is first created. Do all resource/dependency initialization or registration type of tasks here.
     */
    void onCreate();

    /**
     * Called when there is enough memory to load back resources that were released inside {@link this#onMemoryLow()}
     */
    void onLoad();

    /**
     * Called when Android OS runs low on memory.
     * @see com.travelbank.knit.components.KnitMemoryManager
     */
    void onMemoryLow();

    /**
     * Called when entity is destroyed. Do all resource/dependency nullification and unregistration type of tasks here.
     */
    void onDestroy();

    /**
     * Returns whether this entity recently released it's resources via {@link this#onMemoryLow()} or not. If {@link this#onMemoryLow()} is called,
     * this will return {@code true}. Otherwise will always return {@code false}.
     * @return Whether this entity recently released it's resources via {@link this#onMemoryLow()} or not.
     */
    boolean shouldLoad();
}
