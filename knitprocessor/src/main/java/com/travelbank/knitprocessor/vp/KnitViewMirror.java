package com.travelbank.knitprocessor.vp;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by omerozer on 2/2/18.
 */

class KnitViewMirror {

    public TypeElement enclosingClass;

    public List<ExecutableElement> methods = new ArrayList<>();

}
