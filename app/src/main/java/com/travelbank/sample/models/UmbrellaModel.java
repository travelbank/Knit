package com.travelbank.sample.models;

import android.util.Log;

import com.travelbank.knit.Collects;
import com.travelbank.knit.Inputs;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.Model;
import com.travelbank.knit.generators.Generator0;
import com.travelbank.knit.inputters.Inputter1;

/**
 * Created by omerozer on 2/14/18.
 */

@Model
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
