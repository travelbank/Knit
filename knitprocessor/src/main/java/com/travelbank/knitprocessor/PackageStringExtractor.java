package com.travelbank.knitprocessor;

import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

/**
 * Created by omerozer on 2/4/18.
 */

public class PackageStringExtractor {
    public static String extract(TypeMirror mirror){
        String name = TypeName.get(mirror).toString();
        int i = name.lastIndexOf('.');
        return name.substring(0,i);
    }
}
