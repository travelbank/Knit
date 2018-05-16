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

    public static Set<String> getAll(){
        return new HashSet<String>(Arrays.asList(
                KnitView.class.getCanonicalName(),
                Presenter.class.getCanonicalName(),
                Model.class.getCanonicalName()
        ));
    }
}
