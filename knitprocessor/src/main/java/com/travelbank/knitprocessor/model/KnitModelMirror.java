package com.travelbank.knitprocessor.model;


import com.travelbank.knit.InstanceType;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/4/18.
 */

class KnitModelMirror {

    public TypeElement enclosingClass;

    public InstanceType instanceType;

    public Map<String[],VariableElement> generatesParamsMap = new LinkedHashMap<>();

    public Map<String[],VariableElement> inputterField = new LinkedHashMap<>();

    public Set<String> vals = new LinkedHashSet<>();

    public Set<String> reqs = new LinkedHashSet<>();

}
