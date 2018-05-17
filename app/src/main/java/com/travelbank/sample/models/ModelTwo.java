package com.travelbank.sample.models;

import android.util.Log;

import com.travelbank.knit.Generates;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.Model;
import com.travelbank.knit.generators.Generator1;

/**
 * Created by omerozer on 2/6/18.
 */

@Model
public class ModelTwo extends KnitModel {

    int testVal;

    String testString;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","MODELTWO CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","MODELTWO DESTROYED");
    }

    @Generates("testN")
    Generator1<String,String> generateTestTwoParams = new Generator1<String, String>() {
        @Override
        public KnitResponse<String> generate(String generate) {
            Log.d("KNIT_TEST","TEST_N CALL");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new KnitResponse<>(generate+"YAAAH"+Integer.toString(123654));
        }
    };
}
