package com.travelbank.knitprocessor.vp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * Created by omerozer on 2/14/18.
 */

public class AndroidViewMethodsFilter {

    static final Set<String> androidSpecificMethods = new HashSet<>();

    static {
        androidSpecificMethods.addAll(
                Arrays.asList("onCreate","onResume","onPause","onActivityResult","onCreateView","onDraw","onDestroy","onNewIntent","onAttach","onStart","onStop","onBackPressed")
        );
    }


    static boolean filter(Element element){
        return !androidSpecificMethods.contains(element.getSimpleName().toString());
    }
}
