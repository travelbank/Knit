package com.travelbank.knitprocessor;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/4/18.
 */

public class Boxer {
    public static TypeName boxIfNeeded(VariableElement variableElement){
        TypeName type = TypeName.get(variableElement.asType());
        if(variableElement.asType().getKind().isPrimitive()){
            if (type == TypeName.INT) return TypeName.get(Integer.class);
            if (type == TypeName.SHORT) return TypeName.get(Short.class);
            if (type == TypeName.LONG) return TypeName.get(Long.class);
            if (type == TypeName.BOOLEAN) return TypeName.get(Boolean.class);
            if (type == TypeName.DOUBLE) return TypeName.get(Double.class);
            if (type == TypeName.FLOAT) return TypeName.get(Float.class);
            if (type == TypeName.BYTE) return TypeName.get(Byte.class);
            return type;
        }
        return type;
    }
}
