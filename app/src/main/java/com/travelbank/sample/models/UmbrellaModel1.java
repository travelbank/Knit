package com.travelbank.sample.models;

import android.util.Log;

import com.travelbank.knit.Collects;
import com.travelbank.knit.Generates;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.Model;
import com.travelbank.knit.generators.Generator0;
import com.travelbank.knit.generators.Generator1;
import com.travelbank.knit.generators.Generator2;
import com.travelbank.knit.generators.ValueGenerator;

import java.util.List;
import java.util.Map;

/**
 * Created by omerozer on 4/11/18.
 */

@Model
public class UmbrellaModel1 extends KnitModel {

    int testVal;

    String testString;

    List<String> asdasd;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","UMBRELLA1 CREATED");
    }

    @Collects(value = "umbrella1" , needs = {"umbrella"})
    Generator0<String> generator = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","UMBRELLA CALL");
            KnitResponse<String> t1 = requestImmediately("umbrella");
            String result = t1.getBody() + "=/=" + t1.getBody();
            return new KnitResponse<String>(result);
        }
    };

    @Generates("asdasdpoiuy")
    Generator2<List<String>,Map<String,String>,String[]> testGen = new Generator2<List<String>,Map<String,String>,String[]>() {

        @Override
        public KnitResponse<List<String>> generate(Map<String, String> param1, String[] param2) {
            return null;
        }
    };

}
