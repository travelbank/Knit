package com.travelbank.sample.models;

import android.util.Log;

import com.travelbank.knit.Collects;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.Model;
import com.travelbank.knit.generators.Generator0;

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
