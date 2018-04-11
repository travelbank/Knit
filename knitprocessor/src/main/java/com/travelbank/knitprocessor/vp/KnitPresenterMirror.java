package com.travelbank.knitprocessor.vp;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitPresenterMirror {

    public TypeMirror targetView;

    public TypeElement enclosingClass;

    public Map<String,ExecutableElement> updatingMethodsMap = new LinkedHashMap<>();

    public Map<String,ExecutableElement> viewEventMethods = new LinkedHashMap<>();

    public Set<String> needs = new LinkedHashSet<>();

}
