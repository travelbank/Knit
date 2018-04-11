package com.travelbank.knitprocessor;

import com.travelbank.knit.*;
import com.travelbank.knit.Presenter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitAnnotations {
    public static Set<String> getStageOne() {
        return new HashSet<String>(Arrays.asList(
                KnitView.class.getCanonicalName(),
                Presenter.class.getCanonicalName()
                ));
    }

    public static Set<String> getStageTwo() {
        return new HashSet<String>(Arrays.asList(
                Use.class.getCanonicalName()
        ));
    }

    public static Set<String> getStageThree(){
        return new HashSet<String>(Arrays.asList(
                Use.class.getCanonicalName(),
                Model.class.getCanonicalName()
        ));
    }
}
