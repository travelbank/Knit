package com.travelbank.knit;

import com.travelbank.knit.schedulers.ImmediateScheduler;
import com.travelbank.knit.schedulers.KnitSchedulers;
import com.travelbank.knit.schedulers.SchedulerInterface;
import com.travelbank.knit.schedulers.SchedulerProvider;

/**
 * Created by omerozer on 2/26/18.
 */

public class TestSchedulers implements SchedulerProvider {
    @Override
    public SchedulerInterface io() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface main() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface immediate() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface heavy() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface forType(KnitSchedulers type) {
        return immediate();
    }
}
