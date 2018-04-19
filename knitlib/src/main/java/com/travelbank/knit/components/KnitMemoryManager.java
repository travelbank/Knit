package com.travelbank.knit.components;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.travelbank.knit.EntityInstance;
import com.travelbank.knit.MemoryEntity;
import com.travelbank.knit.components.graph.UsageGraph;

/**
 * This class is used to subscribe to Android OS's {@link ComponentCallbacks2} to listen to memory
 * updates. When memory gets low , {@link MemoryEntity#onMemoryLow()} call back on all active {@link
 * MemoryEntity}s gets called
 *
 * @author Omer Ozer
 */

public class KnitMemoryManager implements ComponentCallbacks2 {

    /**
     * {@link UsageGraph} instance to fetch active components.
     */
    private UsageGraph usageGraph;

    public KnitMemoryManager(UsageGraph usageGraph) {
        this.usageGraph = usageGraph;
    }

    /**
     * @param i Memory level
     * @see ComponentCallbacks2
     */
    @Override
    public void onTrimMemory(int i) {
        handleMemoryLevel(i);
    }


    /**
     * @param configuration Configuration update
     * @see ComponentCallbacks2
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
     * Simply goes over all active {@link MemoryEntity}s and calls the {@link ComponentCallbacks2}
     * method
     *
     * @param i Memory level
     */
    private void handleMemoryLevel(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            for (EntityInstance entity : usageGraph.activeEntities()) {
                if (entity.isAvailable()) {
                    entity.get().onMemoryLow();
                }
            }
        }
    }
}
