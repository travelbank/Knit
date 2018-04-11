package com.travelbank.knitprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/13/18.
 */

public class GeneratorExaminer {


    public static List<String> getGenerateTypes(VariableElement variableElement){

        String typeString = variableElement.asType().toString();
        typeString = typeString.substring(typeString.indexOf('<')+1,typeString.lastIndexOf(">"));
        List<String> params = new ArrayList<>();

        if(!typeString.contains(",")){
            return Arrays.asList(typeString);
        }

        typeString+=",";

        while (typeString.contains(",")){
            params.add(typeString.substring(0,typeString.indexOf(',')));
            typeString = typeString.substring(typeString.indexOf(',')+1,typeString.length());
        }

        return params;
    }

    public static List<String> genGenerateTypes(String type){
        String typeString = type;
        typeString = typeString.substring(typeString.indexOf('<')+1,typeString.lastIndexOf(">"));
        List<String> params = new ArrayList<>();

        if(!typeString.contains(",")){
            return Arrays.asList(typeString);
        }

        typeString+=",";

        while (typeString.contains(",")){
            params.add(typeString.substring(0,typeString.indexOf(',')));
            typeString = typeString.substring(typeString.indexOf(',')+1,typeString.length());
        }

        return params;
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
