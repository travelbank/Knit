package com.omerozer.knit.components;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import com.omerozer.knit.EntityInstance;
import com.omerozer.knit.MemoryEntity;
import com.omerozer.knit.components.graph.UsageGraph;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class is used to subscribe to Android OS's {@link ComponentCallbacks2} to listen to memory
 * updates. When memory gets low , {@code .onMemoryLow()} call back on all active {@link MemoryEntity}s gets called
 * @author Omer Ozer
 */

public class KnitMemoryManager implements ComponentCallbacks2 {

    private UsageGraph usageGraph;

    public KnitMemoryManager(UsageGraph usageGraph) {
        this.usageGraph = usageGraph;
    }

    /**
     * @see ComponentCallbacks2
     * @param i Memory level
     */
    @Override
    public void onTrimMemory(int i) {
        handleMemoryLevel(i);
    }


    /**
     * @see  ComponentCallbacks2
     * @param configuration Configuration update
     */
    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    /**
     * @see ComponentCallbacks2
     */
    @Override
    public void onLowMemory() {
        handleMemoryLevel(ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW);
    }


    /**
     * Simply goes over all active {@link MemoryEntity}s and calls the {@code .onMemoryLow()} method
     * @param i Memory level
     */
    private void handleMemoryLevel(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            for (EntityInstance entity : usageGraph.activeEntities()) {
                entity.get().onMemoryLow();
            }
        }
    }
}
