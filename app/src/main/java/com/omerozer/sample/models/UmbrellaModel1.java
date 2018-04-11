package com.omerozer.sample.models;

import android.util.Log;

import com.omerozer.knit.Collects;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;

/**
 * Created by omerozer on 4/11/18.
 */

@Model
public class UmbrellaModel1 extends KnitModel {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","UMBRELLA1 CREATED");
    }

    @Collects(value = "umbrella1" , needs = {"umbrella"})
    Generator0<String> collector = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","UMBRELLA CALL");
            KnitResponse<String> t1 = requestImmediately("umbrella");
            String result = t1.getBody() + "=/=" + t1.getBody();
            return new KnitResponse<String>(result);
        }
    };

}
