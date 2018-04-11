package com.travelbank.knitprocessor.vp;

import static com.travelbank.knitprocessor.KnitFileStrings.TYPE_NAME_CLASS;
import static com.travelbank.knitprocessor.KnitFileStrings.TYPE_NAME_LIST;
import static com.travelbank.knitprocessor.KnitFileStrings.TYPE_NAME_STRING;

import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.PackageStringExtractor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by omerozer on 2/6/18.
 */

public class ViewToPresenterMapWriter extends KnitClassWriter {

    void write(Filer filer, Map<KnitPresenterMirror, KnitViewMirror> map) {
        TypeSpec.Builder viewToPresenterMapBuilder = TypeSpec
                .classBuilder(KnitFileStrings.KNIT_VIEW_PRESENTER)
                .addSuperinterface(
                        ClassName.bestGuess(KnitFileStrings.KNIT_VIEW_PRESENTER_INTERFACE))
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(viewToPresenterMapBuilder);

        createFindPresenterForClassMethod(viewToPresenterMapBuilder,map);
        createGetAllViewsMethod(viewToPresenterMapBuilder,map);
        createGetAllUpdatingValuesMethod(viewToPresenterMapBuilder,map);
        createGetPresenterForParentMethod(viewToPresenterMapBuilder,map);

       writeToFile(filer,KnitFileStrings.KNIT_PACKAGE,viewToPresenterMapBuilder);
    }

    private void createFindPresenterForClassMethod(TypeSpec.Builder builder,Map<KnitPresenterMirror, KnitViewMirror> map){
        MethodSpec.Builder findPresenterForClassMethodBuilder = MethodSpec
                .methodBuilder("getPresenterClassForView")
                .addParameter(TYPE_NAME_CLASS, "viewClass")
                .addModifiers(Modifier.PUBLIC)
                .returns(TYPE_NAME_CLASS)
                .addAnnotation(Override.class);


        for (KnitPresenterMirror knitPresenterMirror : map.keySet()) {
            String packageString = PackageStringExtractor.extract(
                    map.get(knitPresenterMirror).enclosingClass.asType());
            findPresenterForClassMethodBuilder.beginControlFlow("if(viewClass.equals($L.class))",
                    map.get(knitPresenterMirror).enclosingClass.getQualifiedName());
            findPresenterForClassMethodBuilder.addStatement("return $L.$L_Presenter.class",
                    packageString, knitPresenterMirror.enclosingClass.getSimpleName());
            findPresenterForClassMethodBuilder.endControlFlow();
        }
        findPresenterForClassMethodBuilder.addStatement("return null");

        builder.addMethod(findPresenterForClassMethodBuilder.build());
    }

    private void createGetAllViewsMethod(TypeSpec.Builder builder,Map<KnitPresenterMirror, KnitViewMirror> map){
        WildcardTypeName wildcardTypeName = WildcardTypeName.subtypeOf(TypeName.OBJECT);

        ParameterizedTypeName viewClassName = ParameterizedTypeName.get(ClassName.bestGuess(Class.class.getCanonicalName()),wildcardTypeName);

        ParameterizedTypeName returnTypeForGetAll = ParameterizedTypeName.get(TYPE_NAME_LIST, viewClassName);

        MethodSpec.Builder getAllViewsMethodBuilder = MethodSpec
                .methodBuilder("getAllViews")
                .addAnnotation(Override.class)
                .returns(returnTypeForGetAll)
                .addModifiers(Modifier.PUBLIC);

        int c = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java.util.Arrays.asList(");
        for (KnitPresenterMirror modelMirror : map.keySet()) {
            stringBuilder.append(map.get(modelMirror).enclosingClass.getQualifiedName());
            stringBuilder.append(".class");
            if (c < map.size() - 1) {
                stringBuilder.append(",");
            }
            c++;
        }
        stringBuilder.append(",Object.class");
        stringBuilder.append(")");

        getAllViewsMethodBuilder.addStatement("return $L",stringBuilder.toString());
        builder.addMethod(getAllViewsMethodBuilder.build());
    }

    private void createGetAllUpdatingValuesMethod(TypeSpec.Builder builder,Map<KnitPresenterMirror, KnitViewMirror> map){
        ParameterizedTypeName returnTypeForGeneratedVals = ParameterizedTypeName.get(TYPE_NAME_LIST, TYPE_NAME_STRING);

        int c = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java.util.Arrays.asList(");

        MethodSpec.Builder getUpdatingValuesMethodBuilder = MethodSpec
                .methodBuilder("getUpdatingValues")
                .addParameter(TYPE_NAME_CLASS,"clazz")
                .addAnnotation(Override.class)
                .returns(returnTypeForGeneratedVals)
                .addModifiers(Modifier.PUBLIC);

        for(KnitPresenterMirror presenterMirror: map.keySet()) {
            c = 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append("java.util.Arrays.asList(");
            String packageString = PackageStringExtractor.extract(map.get(presenterMirror).enclosingClass.asType());

            for (String field : presenterMirror.needs) {
                stringBuilder.append("\"");
                stringBuilder.append(field);
                stringBuilder.append("\"");
                if (c < presenterMirror.needs.size() - 1) {
                    stringBuilder.append(",");
                }
                c++;
            }
            stringBuilder.append(")");

            getUpdatingValuesMethodBuilder.beginControlFlow("if(clazz.equals($L.$L$L.class))",packageString, presenterMirror.enclosingClass.getSimpleName(),"_Presenter");
            getUpdatingValuesMethodBuilder.addStatement("return $L",stringBuilder.toString());
            getUpdatingValuesMethodBuilder.endControlFlow();
        }

        getUpdatingValuesMethodBuilder.addStatement("return null");
        builder.addMethod(getUpdatingValuesMethodBuilder.build());

    }

    private void createGetPresenterForParentMethod(TypeSpec.Builder builder,Map<KnitPresenterMirror, KnitViewMirror> map){
        MethodSpec.Builder getPresenterForParentMethodBuilder = MethodSpec
                .methodBuilder("getPresenterClassForPresenter")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TYPE_NAME_CLASS,"parentClass")
                .returns(TYPE_NAME_CLASS);

        for(KnitPresenterMirror presenterMirror : map.keySet()){
            String packageString = PackageStringExtractor.extract(
                    map.get(presenterMirror).enclosingClass.asType());
            getPresenterForParentMethodBuilder.beginControlFlow("if(parentClass.equals($L.class))",presenterMirror.enclosingClass.getQualifiedName());
            getPresenterForParentMethodBuilder.addStatement("return $L.$L$L.class",packageString,presenterMirror.enclosingClass.getSimpleName(),"_Presenter");
            getPresenterForParentMethodBuilder.endControlFlow();
        }
        getPresenterForParentMethodBuilder.addStatement("return null");
        builder.addMethod(getPresenterForParentMethodBuilder.build());
    }
}
