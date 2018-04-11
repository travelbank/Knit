package com.travelbank.knitprocessor.model;

import com.travelbank.knit.InstanceType;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import static com.travelbank.knitprocessor.KnitFileStrings.TYPE_NAME_CLASS;
import static com.travelbank.knitprocessor.KnitFileStrings.TYPE_NAME_LIST;
import static com.travelbank.knitprocessor.KnitFileStrings.TYPE_NAME_STRING;

/**
 * Created by omerozer on 2/6/18.
 */

public class  ModelMapWriter extends KnitClassWriter  {
    void write(Filer filer, Set<KnitModelMirror> modelMirrors) {

        TypeSpec.Builder modelMapBuilder = TypeSpec
                .classBuilder("ModelMap")
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_MAP_INTERFACE))
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(modelMapBuilder);

        createGetAllMethod(modelMapBuilder,modelMirrors);
        createGetHandledValuesMethod(modelMapBuilder,modelMirrors);
        createGetRequiredValuesMethod(modelMapBuilder,modelMirrors);
        createGetInternalModelForModelMethod(modelMapBuilder,modelMirrors);
        createIsModelSingletonMethod(modelMapBuilder,modelMirrors);

        writeToFile(filer,KnitFileStrings.KNIT_PACKAGE,modelMapBuilder);
    }

    private void createGetAllMethod(TypeSpec.Builder builder,Set<KnitModelMirror> modelMirrors){
        WildcardTypeName wildcardTypeName = WildcardTypeName.subtypeOf(
                ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        ParameterizedTypeName classTypeName = ParameterizedTypeName.get(TYPE_NAME_CLASS, wildcardTypeName);

        ParameterizedTypeName returnTypeForGetAll = ParameterizedTypeName.get(TYPE_NAME_LIST, classTypeName);

        int c = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java.util.Arrays.asList(");
        for (KnitModelMirror modelMirror : modelMirrors) {
            stringBuilder.append(modelMirror.enclosingClass.getQualifiedName());
            stringBuilder.append("_Model");
            stringBuilder.append(".class");
            if (c < modelMirrors.size() - 1) {
                stringBuilder.append(",");
            }
            c++;
        }
        stringBuilder.append(")");

        builder.addMethod(MethodSpec
                .methodBuilder("getAll")
                .addAnnotation(Override.class)
                .returns(returnTypeForGetAll)
                .addStatement("return " + stringBuilder.toString())
                .addModifiers(Modifier.PUBLIC)
                .build());
    }

    private void createGetHandledValuesMethod(TypeSpec.Builder builder,Set<KnitModelMirror> modelMirrors){
        int c;
        StringBuilder stringBuilder;

        ParameterizedTypeName returnTypeForGeneratedVals = ParameterizedTypeName.get(TYPE_NAME_LIST, TYPE_NAME_STRING);

        MethodSpec.Builder getGeneratedValuesMethodBuilder = MethodSpec
                .methodBuilder("getGeneratedValues")
                .addParameter(TYPE_NAME_CLASS,"clazz")
                .addAnnotation(Override.class)
                .returns(returnTypeForGeneratedVals)
                .addModifiers(Modifier.PUBLIC);

        for(KnitModelMirror knitModelMirror: modelMirrors) {
            c = 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append("java.util.Arrays.asList(");
            for (String field : knitModelMirror.vals) {
                stringBuilder.append("\"");
                stringBuilder.append(field);
                stringBuilder.append("\"");
                if (c < knitModelMirror.vals.size() - 1) {
                    stringBuilder.append(",");
                }
                c++;
            }
            stringBuilder.append(")");

            getGeneratedValuesMethodBuilder.beginControlFlow("if(clazz.equals($L$L.class))",knitModelMirror.enclosingClass.getQualifiedName(),"_Model");
            getGeneratedValuesMethodBuilder.addStatement("return $L",stringBuilder.toString());
            getGeneratedValuesMethodBuilder.endControlFlow();
        }

        getGeneratedValuesMethodBuilder.addStatement("return null");

        builder.addMethod(getGeneratedValuesMethodBuilder.build());
    }

    private void createGetRequiredValuesMethod(TypeSpec.Builder builder,Set<KnitModelMirror> modelMirrors){
        int c;
        StringBuilder stringBuilder;
        ParameterizedTypeName returnTypeForGeneratedVals = ParameterizedTypeName.get(TYPE_NAME_LIST, TYPE_NAME_STRING);

        MethodSpec.Builder getRequiredValuesMethodBuilder = MethodSpec
                .methodBuilder("getRequiredValues")
                .addParameter(TYPE_NAME_CLASS,"clazz")
                .addAnnotation(Override.class)
                .returns(returnTypeForGeneratedVals)
                .addModifiers(Modifier.PUBLIC);

        for(KnitModelMirror knitModelMirror: modelMirrors) {
            c = 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append("java.util.Arrays.asList(");
            for (String field : knitModelMirror.reqs) {
                stringBuilder.append("\"");
                stringBuilder.append(field);
                stringBuilder.append("\"");
                if (c < knitModelMirror.reqs.size() - 1) {
                    stringBuilder.append(",");
                }
                c++;
            }
            stringBuilder.append(")");

            getRequiredValuesMethodBuilder.beginControlFlow("if(clazz.equals($L$L.class))",knitModelMirror.enclosingClass.getQualifiedName(),"_Model");
            getRequiredValuesMethodBuilder.addStatement("return $L",stringBuilder.toString());
            getRequiredValuesMethodBuilder.endControlFlow();
        }

        getRequiredValuesMethodBuilder.addStatement("return null");

        builder.addMethod(getRequiredValuesMethodBuilder.build());
    }

    private void createIsModelSingletonMethod(TypeSpec.Builder builder,Set<KnitModelMirror> modelMirrors){

        WildcardTypeName knitModelSubType = WildcardTypeName.subtypeOf(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        ParameterizedTypeName knitModelClassName = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_CLASS,knitModelSubType);

        MethodSpec.Builder isModelSingletonMethodBuilder = MethodSpec
                        .methodBuilder("isModelSingleton")
                        .addAnnotation(Override.class)
                        .returns(TypeName.BOOLEAN)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(knitModelClassName,"modelClazz");

        for(KnitModelMirror modelMirror : modelMirrors){
            isModelSingletonMethodBuilder.beginControlFlow("if(modelClazz.equals($L_Model.class))",modelMirror.enclosingClass.getQualifiedName());
            isModelSingletonMethodBuilder.addStatement("return $L",Boolean.toString(modelMirror.instanceType.equals(InstanceType.SINGLETON)));
            isModelSingletonMethodBuilder.endControlFlow();
        }

        isModelSingletonMethodBuilder.addStatement("return false");

        builder.addMethod(isModelSingletonMethodBuilder.build());

    }

    private void createGetInternalModelForModelMethod(TypeSpec.Builder builder,Set<KnitModelMirror> modelMirrors){

        WildcardTypeName internalModelSubTypeName = WildcardTypeName.subtypeOf(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        ParameterizedTypeName internalModelClassName = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_CLASS,internalModelSubTypeName);

        WildcardTypeName knitModelSubType = WildcardTypeName.subtypeOf(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_EXT));

        ParameterizedTypeName knitModelClassName = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_CLASS,knitModelSubType);

        MethodSpec.Builder getModelClassForModelMethodBuilder = MethodSpec
                .methodBuilder("getModelClassForModel")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(internalModelClassName)
                .addParameter(knitModelClassName,"modelClazz");

        for(KnitModelMirror modelMirror : modelMirrors){
            getModelClassForModelMethodBuilder.beginControlFlow("if($L.class.equals(modelClazz))",modelMirror.enclosingClass.getQualifiedName());
            getModelClassForModelMethodBuilder.addStatement("return $L_Model.class",modelMirror.enclosingClass.getQualifiedName());
            getModelClassForModelMethodBuilder.endControlFlow();
        }

        getModelClassForModelMethodBuilder.addStatement("return null");

        builder.addMethod(getModelClassForModelMethodBuilder.build());
    }
}
