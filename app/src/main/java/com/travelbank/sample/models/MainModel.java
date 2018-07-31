package com.travelbank.sample.models;

import android.util.Log;

import com.travelbank.knit.Generates;
import com.travelbank.knit.InstanceType;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.Model;
import com.travelbank.knit.generators.Generator0;

/**
 * Created by omerozer on 2/3/18.
 */

@Model(value = InstanceType.SINGLETON,tag = "testTag")
public class MainModel extends KnitModel {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","MAIN MODEL CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","MAIN MODEL DESTROYED");
    }

    @Generates("test")
    public Generator0<String> generateTestString = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","TEST CALL");
            return new KnitResponse<>("TEEEESST STRING");
        }
    };






}
