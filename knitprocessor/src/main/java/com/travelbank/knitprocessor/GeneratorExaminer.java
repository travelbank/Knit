package com.travelbank.knitprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/13/18.
 */

public class GeneratorExaminer {


    public static List<String> getGenerateTypes(VariableElement variableElement){
        return genGenerateTypes(variableElement.asType().toString());
    }

    public static List<String> genGenerateTypes(String type){

        if(isParameterized(type)){
            String sub = type.substring(type.indexOf('<')+1,type.lastIndexOf(">"));
            return Arrays.asList(sub.split(",(?=(?:[^<]*<[^>]*>)*[^>]*$)"));
        }

        return Collections.singletonList(type);
    }

    public static TypeName getName(String string){
        if(!isParameterized(string)){
            return ClassName.bestGuess(string);
        }

        ClassName base = ClassName.bestGuess(string.substring(0,string.indexOf("<")));

        List<String> types = genGenerateTypes(string);
        TypeName[] names = new ClassName[types.size()];
        for(int i = 0; i< types.size();i++){
            names[i] = getName(types.get(i));
        }

        return ParameterizedTypeName.get(base,names);
    }

    public static boolean isParameterized(String type){
        return type.contains("<")&&type.contains(">");
    }


}
