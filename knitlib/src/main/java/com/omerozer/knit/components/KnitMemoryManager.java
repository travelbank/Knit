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
 * Created by omerozer on 2/1/18.
 */

public class KnitMemoryManager implements ComponentCallbacks2 {

    private UsageGraph usageGraph;

    public KnitMemoryManager(UsageGraph usageGraph) {
        this.usageGraph = usageGraph;
    }

    @Override
    public void onTrimMemory(int i) {
        handleMemoryLevel(i);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
        handleMemoryLevel(ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW);
    }

    private void handleMemoryLevel(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            for (EntityInstance entity : usageGraph.activeEntities()) {
                entity.get().onMemoryLow();
            }
        }
    }
}
