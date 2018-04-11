package com.travelbank.knitprocessor;

import com.squareup.javapoet.TypeName;

/**
 * Created by omerozer on 3/5/18.
 */

public class ReturnTypeExaminer {


    public static String getDefaultReturnValueInString(TypeName typeName){
        if(typeName.isPrimitive() || typeName.isBoxedPrimitive()){
            String name = typeName.toString().toLowerCase();
            if(name.contains("int")||name.contains("long")||name.contains("short")||name.contains("float")||name.contains("double")){
                return "0";
            }
            if(name.contains("string")){
                return "\"\"";
            }
            if(name.contains("char")){
                return "''";
            }
            if(name.contains("bool")){
                return "false";
            }
        }
        return "null";
    }


}
