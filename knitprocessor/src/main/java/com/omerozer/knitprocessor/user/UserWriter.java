package com.omerozer.knitprocessor.user;

import com.omerozer.knitprocessor.KnitClassWriter;
import com.omerozer.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/5/18.
 */

public class UserWriter extends KnitClassWriter {

    ParameterizedTypeName entityInstanceName = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_ENTITY,ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER));

    void write(Filer filer, UserMirror userMirror) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(
                        userMirror.enclosingClass.getSimpleName() + KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX)
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_USER))
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(clazzBuilder);

        FieldSpec parentField = FieldSpec
                .builder(entityInstanceName, "parent")
                .addModifiers(Modifier.PRIVATE)
                .build();

        createConstructor(clazzBuilder,userMirror);
        createExposedMethodsMethod(clazzBuilder,userMirror);
        createGetterMethods(clazzBuilder,userMirror);
        createCastParentMethod(clazzBuilder,userMirror);

        clazzBuilder.addField(parentField);

        PackageElement packageElement = (PackageElement)userMirror.enclosingClass.getEnclosingElement() ;

        writeToFile(filer,packageElement.getQualifiedName().toString(),clazzBuilder);

    }

    private void createGetterMethods(TypeSpec.Builder builder, UserMirror userMirror){
        for (String string : userMirror.getterMap.keySet()) {
            MethodSpec getterMethod = MethodSpec
                    .methodBuilder("get"+string)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(userMirror.getterMap.get(string).getReturnType()))
                    .addStatement("return this.parent.get"+string+"()")
                    .build();
            builder.addMethod(getterMethod);
        }

    }

    private void createExposedMethodsMethod(TypeSpec.Builder builder, UserMirror userMirror){
        for (ExecutableElement methodElement : userMirror.method) {
            if(methodElement.getModifiers().contains(Modifier.PRIVATE)){
                continue;
            }
            MethodSpec.Builder userBuilder = MethodSpec
                    .methodBuilder("use_" + methodElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC);
            userBuilder.beginControlFlow("if(parent.isAvailable())");
            int c = 0;
            for (VariableElement variableElement : methodElement.getParameters()) {
                userBuilder.addParameter(TypeName.get(variableElement.asType()), "v" + c++);
            }
            StringBuilder paramsBlock = new StringBuilder();
            paramsBlock.append("castParent().");
            paramsBlock.append(methodElement.getSimpleName());
            paramsBlock.append("(");
            for (int i = 0; i < c; i++) {
                paramsBlock.append("v");
                paramsBlock.append(i);
                if (i < c - 1) {
                    paramsBlock.append(",");
                }
            }
            paramsBlock.append(")");
            userBuilder.addStatement(paramsBlock.toString());
            userBuilder.endControlFlow();
            builder.addMethod(userBuilder.build());
        }
    }

    private void createCastParentMethod(TypeSpec.Builder builder, UserMirror userMirror){
        MethodSpec getterMethod = MethodSpec
                .methodBuilder("castParent")
                .addModifiers(Modifier.PRIVATE)
                .returns(ClassName.bestGuess(userMirror.enclosingClass.getQualifiedName().toString()))
                .addStatement("return ($L)this.parent.get()",userMirror.enclosingClass.getQualifiedName())
                .build();
        builder.addMethod(getterMethod);
    }

    private void createConstructor(TypeSpec.Builder builder, UserMirror userMirror){
        builder.addMethod(MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entityInstanceName, "parent")
                .addStatement("this.parent = parent").build());
    }
}
