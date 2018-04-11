package com.travelbank.knitprocessor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;

/**
 * Created by bo_om on 3/12/2018.
 */

public abstract class KnitClassWriter {

    public void addKnitWarning(TypeSpec.Builder builder){
        builder.addJavadoc(KnitFileStrings.KNIT_CLASS_COMMENT);
    }

    protected void writeToFile(Filer filer, String packageName, TypeSpec.Builder builder){
        JavaFile javaFile = JavaFile.builder(packageName,builder.build()).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
