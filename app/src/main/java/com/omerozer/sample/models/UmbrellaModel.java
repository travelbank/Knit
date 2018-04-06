package com.omerozer.sample.models;

import android.util.Log;

import com.omerozer.knit.Collects;
import com.omerozer.knit.Inputs;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;
import com.omerozer.knit.inputters.Inputter1;

/**
 * Created by omerozer on 2/14/18.
 */

public class UmbrellaModel extends KnitModel {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","UMBRELLA CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","UMBRELLA DESTROYED");
    }

    @Collects(value = "umbrella" , needs = {"testN","test"})
    Generator0<String> collector = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","UMBRELLA CALL");
            KnitResponse<String> t1 = requestImmediately("testN","YAHH");
            KnitResponse<String> t2 = requestImmediately("test");
            String result = t1.getBody() + "=/=" + t2.getBody();
            return new KnitResponse<String>(result);
        }
    };

    @Inputs("input")
    Inputter1<String> stringInputter = new Inputter1<String>() {
        @Override
        public void input(String param1) {

        }
    };

}
