package com.travelbank.knitprocessor.vp;

import com.travelbank.knitprocessor.InterfaceMethodsCreatorForExposers;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.KnitMethodsFilter;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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

public class PresenterExposerWriter extends KnitClassWriter {
    void write(Filer filer, KnitPresenterMirror presenterMirror) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(presenterMirror.enclosingClass.getSimpleName() + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(clazzBuilder);

        FieldSpec parentField = FieldSpec
                .builder(TypeName.get(presenterMirror.enclosingClass.asType()), "parent", Modifier.PRIVATE).build();
        clazzBuilder.addField(parentField);

        createConstructor(clazzBuilder, presenterMirror);
        createGetParentMethod(clazzBuilder, presenterMirror);
        createExposingMethods(clazzBuilder, presenterMirror);
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnCreateMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnViewAppliedMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnCurrentViewReleased());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnLoadMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnMemoryLow());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnDestroyMethod());
        createNativeCallbacks(clazzBuilder);

        PackageElement enclosingPackage = (PackageElement) presenterMirror.enclosingClass.getEnclosingElement();

        writeToFile(filer, enclosingPackage.getQualifiedName().toString(), clazzBuilder);

    }

    private void createConstructor(TypeSpec.Builder builder, KnitPresenterMirror presenterMirror) {
        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addParameter(TypeName.get(presenterMirror.enclosingClass.asType()), "parent")
                .addStatement("this.parent = parent")
                .addModifiers(Modifier.PUBLIC)
                .build();

        builder.addMethod(constructor);
    }

    private void createGetParentMethod(TypeSpec.Builder builder, KnitPresenterMirror presenterMirror) {
        MethodSpec getParentMethod = MethodSpec
                .methodBuilder("getParent")
                .returns(TypeName.get(presenterMirror.enclosingClass.asType()))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.parent")
                .build();

        builder.addMethod(getParentMethod);
    }


    private void createExposingMethods(TypeSpec.Builder builder, KnitPresenterMirror presenterMirror) {
        for (Element element : presenterMirror.enclosingClass.getEnclosedElements()) {
            if(element.getModifiers().contains(Modifier.PRIVATE) || element.getModifiers().contains(Modifier.STATIC)){
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
            } else if (element.getKind().equals(ElementKind.METHOD) && KnitMethodsFilter.filter(element)) {
                ExecutableElement methodElement = (ExecutableElement) element;
                MethodSpec.Builder userMethodBuilder = MethodSpec
                        .methodBuilder("use_" + element.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC);

                int c = 0;
                StringBuilder paramsText = new StringBuilder();
                for (VariableElement param : methodElement.getParameters()) {
                    paramsText.append("v");
                    paramsText.append(Integer.toString(c));
                    userMethodBuilder.addParameter(TypeName.get(param.asType()), "v" + Integer.toString(c));
                    if (c < methodElement.getParameters().size() - 1) {
                        paramsText.append(",");
                    }
                    c++;
                }
                userMethodBuilder.addStatement("parent.$L($L)", methodElement.getSimpleName(), paramsText.toString());
                builder.addMethod(userMethodBuilder.build());
            }
        }

    }

    private void createNativeCallbacks(TypeSpec.Builder builder) {
        NativeViewCallbacks.createNativeCallbackMethodsForExposer(builder);
    }

}
