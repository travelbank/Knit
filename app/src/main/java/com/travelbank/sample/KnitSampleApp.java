package com.travelbank.sample;

import android.app.Application;

import com.travelbank.knit.Knit;

/**
 * Created by omerozer on 2/6/18.
 */

public class KnitSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Knit.init(this);
    }
}
