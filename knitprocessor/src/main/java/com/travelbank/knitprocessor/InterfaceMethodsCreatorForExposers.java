package com.travelbank.knitprocessor;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by omerozer on 2/15/18.
 */

public class InterfaceMethodsCreatorForExposers {

    public static MethodSpec getOnCreateMethod(){
        return MethodSpec
                .methodBuilder("use_onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onCreate()")
                .build();
    }

    public static MethodSpec getOnDestroyMethod(){
        return MethodSpec
                .methodBuilder("use_onDestroy")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onDestroy()")
                .build();
    }

    public static MethodSpec getOnLoadMethod(){
        return MethodSpec
                .methodBuilder("use_onLoad")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onLoad()")
                .build();
    }

    public static MethodSpec getOnMemoryLow(){
        return MethodSpec
                .methodBuilder("use_onMemoryLow")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onMemoryLow()")
                .build();
    }

    public static MethodSpec getOnViewAppliedMethod(){
        return MethodSpec
                .methodBuilder("use_onViewApplied")
                .addParameter(Object.class,"o")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onViewApplied(o)")
                .build();
    }

    public static MethodSpec getOnCurrentViewReleased(){
        return MethodSpec
                .methodBuilder("use_onCurrentViewReleased")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onCurrentViewReleased()")
                .build();
    }

}
