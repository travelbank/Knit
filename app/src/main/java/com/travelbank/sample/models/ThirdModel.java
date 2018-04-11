package com.travelbank.sample.models;

import android.util.Log;

import com.travelbank.knit.Generates;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.Model;
import com.travelbank.knit.generators.Generator0;
import com.travelbank.sample.datatype.StringWrapper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by omerozer on 3/1/18.
 */

@Model
public class ThirdModel extends KnitModel {


    @Generates("Ttest")
    public Generator0<List<StringWrapper>> generateTestString = new Generator0<List<StringWrapper>>() {

        @Override
        public KnitResponse<List<StringWrapper>> generate() {
            Log.d("KNIT_TEST","TEST CALL");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new KnitResponse<>(Arrays.asList(new StringWrapper("TEEEEST")));
        }
    };

}
