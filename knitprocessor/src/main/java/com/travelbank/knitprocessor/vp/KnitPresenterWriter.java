package com.travelbank.knitprocessor.vp;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.PackageStringExtractor;
import com.travelbank.knitprocessor.user.UserMirror;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/2/18.
 */

class KnitPresenterWriter extends KnitClassWriter {
    void write(Filer filer, KnitPresenterMirror presenterMirror,
            Map<KnitPresenterMirror, KnitViewMirror> map,Set<UserMirror> userMirrorSet) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(presenterMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_PRESENTER_POSTFIX)
                .superclass(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER))
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(clazzBuilder);

        UserMirror userMirror = new UserMirror();
        userMirror.enclosingClass = presenterMirror.enclosingClass.getSimpleName()
                + KnitFileStrings.KNIT_PRESENTER_POSTFIX;

        userMirror.qualifiedName = PackageStringExtractor.extract(presenterMirror.targetView)+"."+userMirror.enclosingClass;

        ClassName contractName = ClassName.bestGuess(presenterMirror.targetView.toString()+KnitFileStrings.KNIT_CONTRACT_POSTFIX);

        createFields(clazzBuilder,contractName,presenterMirror);

        createConstructor(clazzBuilder, presenterMirror);
        createApplyMethod(clazzBuilder, presenterMirror, contractName);
        createHandleMethod(clazzBuilder, presenterMirror, map);
        createRemoveMethod(clazzBuilder, presenterMirror);
        createLoadMethod(clazzBuilder, presenterMirror);
        createDestroyMethod(clazzBuilder, presenterMirror);
        createUpdatingMethods(clazzBuilder, presenterMirror, map,userMirror);
        createOnMemoryLowMethod(clazzBuilder,presenterMirror);
        createMethods(clazzBuilder,map,presenterMirror);
        createNativeViewCallbacks(clazzBuilder,presenterMirror);


        String packageName = PackageStringExtractor.extract(presenterMirror.targetView);

        userMirror.packageElement = packageName;
        userMirror.requiredValues.addAll(userMirror.methodMap.keySet());
        userMirrorSet.add(userMirror);

        writeToFile(filer,packageName,clazzBuilder);

    }

    private void createFields(TypeSpec.Builder builder,ClassName contractName ,KnitPresenterMirror presenterMirror){


        FieldSpec parentField = FieldSpec
                .builder(ClassName.bestGuess(presenterMirror.enclosingClass.getQualifiedName() +
                                KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX), "parent",
                        Modifier.PRIVATE)
                .build();

        FieldSpec loadedField = FieldSpec
                .builder(TypeName.BOOLEAN, "loaded", Modifier.PRIVATE)
                .build();

        FieldSpec modelManagerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL), "modelManager",
                        Modifier.PRIVATE)
                .build();

        FieldSpec updateablesField = FieldSpec
                .builder(String[].class,"updateables")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec navigatorField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.KNIT_NAVIGATOR),"navigator")
                .addModifiers(Modifier.PRIVATE)
                .build();



        FieldSpec activeViewWeakRefField = FieldSpec
                .builder(contractName, "activeViewContract")
                .addModifiers(Modifier.PRIVATE)
                .build();

        builder.addField(parentField);
        builder.addField(loadedField);
        builder.addField(modelManagerField);
        builder.addField(updateablesField);
        builder.addField(navigatorField);
        builder.addField(activeViewWeakRefField);
    }

    private void createConstructor(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT),"knitInstance")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_NAVIGATOR),"navigator")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL), "modelManager")
                .addStatement("$L parent = new $L()",presenterMirror.enclosingClass.getQualifiedName(),presenterMirror.enclosingClass.getQualifiedName())
                .addStatement("this.parent = new " + presenterMirror.enclosingClass.getQualifiedName() + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX + "(parent)")
                .addStatement("parent.setKnit(knitInstance)")
                .addStatement("this.modelManager = modelManager")
                .addStatement("this.updateables = $L",KnitFileStrings.createStringArrayField(presenterMirror.updatingMethodsMap.keySet()))
                .addStatement("this.navigator = navigator")
                .addModifiers(Modifier.PUBLIC);

        constructorBuilder.addStatement("this.loaded = false");

        clazzBuilder.addMethod(constructorBuilder.build());

    }

    private void createMethods(TypeSpec.Builder builder,Map<KnitPresenterMirror, KnitViewMirror> map,KnitPresenterMirror presenterMirror){

        MethodSpec shouldLoadMethod;
        MethodSpec.Builder shouldLoadMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_SHOULD_LOAD_METHOD)
                .returns(TypeName.BOOLEAN)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return !loaded");

        shouldLoadMethod = shouldLoadMethodBuilder.build();

        MethodSpec getModelManagerMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_GET_MODEL_MANAGER_METHOD)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return this.$L","modelManager")
                .build();

        MethodSpec getContractMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_GET_VIEW_METHOD)
                .returns(Object.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return this.activeViewContract")
                .build();

        MethodSpec onCreateMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_ONCREATE_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("this.parent.use_onCreate()")
                .build();

        MethodSpec getUpdatablesMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_GET_UPDATEABLES_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class)
                .addStatement("return updateables")
                .build();

        MethodSpec getNavigatorMEthod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_GET_NAVIGATOR_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_NAVIGATOR))
                .addStatement("return navigator")
                .build();

        MethodSpec getParentMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_GET_PARENT_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(presenterMirror.enclosingClass.asType()))
                .addStatement("return this.parent.getParent()")
                .build();

        builder.addMethod(shouldLoadMethod);
        builder.addMethod(getModelManagerMethod);
        builder.addMethod(getContractMethod);
        builder.addMethod(onCreateMethod);
        builder.addMethod(getUpdatablesMethod);
        builder.addMethod(getNavigatorMEthod);
        builder.addMethod(getParentMethod);
    }

    private void createNativeViewCallbacks(TypeSpec.Builder builder, KnitPresenterMirror knitPresenterMirror){
        NativeViewCallbacks.createNativeCallbackMethodsForPresenter(builder);
    }

    private void createHandleMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, Map<KnitPresenterMirror, KnitViewMirror> map) {

        MethodSpec.Builder handleMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_EVENT_HANDLE_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_EVENT_VIEW_EVENT_POOL),
                        "eventPool")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_EVENT_VIEW_EVENT_ENV),
                        "eventEnv")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL), "modelManager");

        handleMethodBuilder.addStatement("$L tag = eventEnv.getTag()", String.class.getCanonicalName());

        for(String viewEvent : presenterMirror.viewEventMethods.keySet()){
            handleMethodBuilder.beginControlFlow("if($S.equals(tag))",viewEvent);
            List<? extends VariableElement> paramsList = presenterMirror.viewEventMethods.get(viewEvent).getParameters();
            if(paramsList.isEmpty()){
                handleMethodBuilder.addStatement("this.parent.use_$L()",presenterMirror.viewEventMethods.get(viewEvent).getSimpleName());
            }else{
                handleMethodBuilder.addStatement("this.parent.use_$L(eventEnv)",presenterMirror.viewEventMethods.get(viewEvent).getSimpleName());
            }
            handleMethodBuilder.endControlFlow();
        }

        handleMethodBuilder.addStatement("eventPool.pool(eventEnv)");


        clazzBuilder.addMethod(handleMethodBuilder.build());
    }

    private void createApplyMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, ClassName name) {

        MethodSpec.Builder applyMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_APPLY_METHOD)
                .addParameter(TypeName.OBJECT, "viewObject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        applyMethodBuilder.addStatement("$L target = ($L)viewObject",
                presenterMirror.targetView.toString(), presenterMirror.targetView.toString());


        applyMethodBuilder.addStatement("this.activeViewContract = new $L(target)",name);
        applyMethodBuilder.addStatement("this.parent.use_onViewApplied(viewObject)");

        clazzBuilder.addMethod(applyMethodBuilder.build());
    }

    private void createRemoveMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        MethodSpec.Builder removeActiveView = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_RELEASE_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.activeViewContract = null")
                .addStatement("this.parent.use_onCurrentViewReleased()")
                .addAnnotation(Override.class);

        clazzBuilder.addMethod(removeActiveView.build());
    }

    private void createLoadMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        MethodSpec.Builder loadMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_LOAD_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        loadMethodBuilder.addStatement("this.parent.use_onLoad()");
        loadMethodBuilder.addStatement("this.loaded = true");
        clazzBuilder.addMethod(loadMethodBuilder.build());
    }

    private void createDestroyMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        MethodSpec.Builder destroyMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_DESTROY_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        destroyMethodBuilder.addStatement("this.parent.use_onDestroy()");
        clazzBuilder.addMethod(destroyMethodBuilder.build());
    }

    private void createOnMemoryLowMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror){
        MethodSpec.Builder onMemoryLowMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_MEMORY_LOW_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        onMemoryLowMethodBuilder.addStatement("this.parent.use_onMemoryLow()");
        onMemoryLowMethodBuilder.addStatement("this.loaded = false");
        clazzBuilder.addMethod(onMemoryLowMethodBuilder.build());
    }


    private void createUpdatingMethods(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, Map<KnitPresenterMirror, KnitViewMirror> map,UserMirror userMirror) {

        for(String string : presenterMirror.updatingMethodsMap.keySet()){
            MethodSpec.Builder updatingMethodBuilder = MethodSpec
                    .methodBuilder(string+KnitFileStrings.KNIT_PRESENTER_UPDATE_METHOD_POSTFIX)
                    .addModifiers(Modifier.PUBLIC);

            ExecutableElement methodElement = presenterMirror.updatingMethodsMap.get(string);
            int c = 0;
            StringBuilder paramsText = new StringBuilder();
            for(VariableElement param : methodElement.getParameters()){
                paramsText.append("v");
                paramsText.append(Integer.toString(c));
                updatingMethodBuilder.addParameter(TypeName.get(param.asType()),"v"+Integer.toString(c));
                if(c<methodElement.getParameters().size()-1){
                    paramsText.append(",");
                }
                c++;
            }
            userMirror.userMethodNames.put(string,string+KnitFileStrings.KNIT_PRESENTER_UPDATE_METHOD_POSTFIX);
            userMirror.methodMap.put(string,methodElement);
            updatingMethodBuilder.addStatement("parent.$L$L($L)","use_",methodElement.getSimpleName(),paramsText.toString());
            clazzBuilder.addMethod(updatingMethodBuilder.build());
        }

    }




}
