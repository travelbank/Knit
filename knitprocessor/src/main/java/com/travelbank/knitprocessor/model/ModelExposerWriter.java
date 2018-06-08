package com.travelbank.knitprocessor.model;

import com.travelbank.knitprocessor.InterfaceMethodsCreatorForExposers;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.KnitMethodsFilter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.travelbank.knitprocessor.StringUtil;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/4/18.
 */

class ModelExposerWriter extends KnitClassWriter {
    void write(Filer filer, KnitModelMirror modelMirror) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(modelMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(clazzBuilder);

        FieldSpec parentField = FieldSpec
                .builder(TypeName.get(modelMirror.enclosingClass.asType()), "parent",
                        Modifier.PRIVATE).build();

        createConstructor(clazzBuilder, modelMirror);
        createExposedElements(clazzBuilder, modelMirror);
        createGetParentMethod(clazzBuilder, modelMirror);
        createNonAsycnGettersAndSetters(clazzBuilder, modelMirror);
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnCreateMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnLoadMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnMemoryLow());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnDestroyMethod());

        clazzBuilder.addField(parentField);

        PackageElement enclosingPackage =
                (PackageElement) modelMirror.enclosingClass.getEnclosingElement();

        writeToFile(filer, enclosingPackage.getQualifiedName().toString(), clazzBuilder);

    }

    private void createConstructor(TypeSpec.Builder builder, KnitModelMirror modelMirror) {
        builder.addMethod(MethodSpec
                .constructorBuilder()
                .addParameter(TypeName.get(modelMirror.enclosingClass.asType()), "parent")
                .addStatement("this.parent = parent").build());
    }

    private void createGetParentMethod(TypeSpec.Builder builder, KnitModelMirror modelMirror) {
        builder.addMethod(MethodSpec
                .methodBuilder("getParent")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_EXT))
                .addStatement("return this.parent")
                .build());
    }

    private void createNonAsycnGettersAndSetters(TypeSpec.Builder builder,
            KnitModelMirror modelMirror) {
        for (VariableElement variableElement : modelMirror.fields) {
            builder.addMethod(createGetter(variableElement));
            builder.addMethod(createSetter(variableElement));
        }
    }

    private MethodSpec createGetter(VariableElement variableElement) {
        return MethodSpec.methodBuilder(
                "get" + StringUtil.firstLetterToCaps(variableElement.getSimpleName().toString()))
                .returns(TypeName.get(variableElement.asType()))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return parent.$L", variableElement.getSimpleName())
                .build();
    }

    private MethodSpec createSetter(VariableElement variableElement) {
        return MethodSpec.methodBuilder(
                "set" + StringUtil.firstLetterToCaps(variableElement.getSimpleName().toString()))
                .addParameter(TypeName.get(variableElement.asType()), "param")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.$L = param", variableElement.getSimpleName())
                .build();
    }

    private void createExposedElements(TypeSpec.Builder builder, KnitModelMirror modelMirror) {
        for (Element element : modelMirror.enclosingClass.getEnclosedElements()) {
            if (element.getModifiers().contains(Modifier.PRIVATE)
                    || element.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            if (element.getKind().isField()) {
                MethodSpec getter = MethodSpec
                        .methodBuilder("get_" + element.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.get(element.asType()))
                        .addStatement("return parent." + element.getSimpleName())
                        .build();

                builder.addMethod(getter);
            } else if (element.getKind().equals(ElementKind.METHOD) && KnitMethodsFilter.filter(
                    element)) {
                ExecutableElement methodElement = (ExecutableElement) element;
                MethodSpec.Builder userMethodBuilder = MethodSpec
                        .methodBuilder("use_" + element.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC);

                int c = 0;
                StringBuilder paramsText = new StringBuilder();
                for (VariableElement param : methodElement.getParameters()) {
                    paramsText.append("v");
                    paramsText.append(Integer.toString(c));
                    userMethodBuilder.addParameter(TypeName.get(param.asType()),
                            "v" + Integer.toString(c));
                    if (c < methodElement.getParameters().size() - 1) {
                        paramsText.append(",");
                    }
                    c++;
                }

                String returnTypeString = "";

                if (!methodElement.getReturnType().toString().contains("void")) {
                    userMethodBuilder.returns(TypeName.get(methodElement.getReturnType()));
                    returnTypeString = "return ";
                }

                userMethodBuilder.addStatement("$Lparent.$L($L)", returnTypeString,methodElement.getSimpleName(),
                        paramsText.toString());
                builder.addMethod(userMethodBuilder.build());
            }
        }
    }
}
