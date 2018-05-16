package com.travelbank.knitprocessor.model;

import com.travelbank.knitprocessor.GeneratorExaminer;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.user.UserMirror;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;

import static com.travelbank.knitprocessor.KnitFileStrings.*;

/**
 * Created by omerozer on 2/4/18.
 */

class KnitModelWriter extends KnitClassWriter {

     String[] defaultMethods = new String[]{KNIT_ME_ONCREATE_METHOD,KNIT_ME_DESTROY_METHOD,KNIT_ME_LOAD_METHOD,KNIT_ME_MEMORY_LOW_METHOD};

    ParameterizedTypeName entityInstanceName = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_ENTITY,ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER));

     void write(Filer filer, KnitModelMirror modelMirror,
            Map<KnitModelMirror, Set<UserMirror>> map) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(modelMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_MODEL_POSTFIX)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        addKnitWarning(clazzBuilder);

        createGetParentMethod(clazzBuilder,modelMirror);
        createDependencyFields(clazzBuilder,modelMirror);
        createDefaultMethods(clazzBuilder,modelMirror);
        createGetHandledValuesMethod(clazzBuilder,modelMirror);
        createRequestMethodForPresenter(clazzBuilder, modelMirror, map);
        createRequestImmediateMethod(clazzBuilder,modelMirror);
        createGeneratingFields(clazzBuilder, modelMirror);
        createConstructor(clazzBuilder, modelMirror);
        createInputMethod(clazzBuilder, modelMirror);



        PackageElement packageElement =
                (PackageElement) modelMirror.enclosingClass.getEnclosingElement();


       writeToFile(filer,packageElement.getQualifiedName().toString(),clazzBuilder);

    }

    private void createDependencyFields(TypeSpec.Builder builder,
                                        KnitModelMirror modelMirror){
        FieldSpec parentExposerField = FieldSpec
                .builder(ClassName.bestGuess(
                        modelMirror.enclosingClass.getQualifiedName().toString()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX), "parentExposer")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec schedulerProviderField = FieldSpec
                .builder(KnitFileStrings.TYPE_NAME_SCHEDULER_PROVIDER, "schedulerProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();

        FieldSpec instanceMapField = FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class),
                        entityInstanceName,
                        ClassName.bestGuess(KnitFileStrings.KNIT_USER)), "userMap")
                .addModifiers(Modifier.PRIVATE)
                .build();
        builder.addField(parentExposerField);
        builder.addField(schedulerProviderField);
        builder.addField(instanceMapField);
    }

    private void createGeneratingFields(TypeSpec.Builder builder,
            KnitModelMirror modelMirror) {

        Set<VariableElement> fields = new HashSet<>();
        fields.addAll(modelMirror.generatesParamsMap.values());
        fields.addAll(modelMirror.inputterField.values());

        for (VariableElement field : fields) {
            FieldSpec generatorField = FieldSpec
                    .builder(TypeName.get(field.asType()), field.getSimpleName().toString(),
                            Modifier.PRIVATE)
                    .build();

            FieldSpec generatorLock = FieldSpec
                    .builder(TypeName.OBJECT, field.getSimpleName().toString()+"Lock",
                            Modifier.PRIVATE,Modifier.FINAL)
                    .build();
            builder.addField(generatorLock);
            builder.addField(generatorField);
        }
    }

    private void createConstructor(TypeSpec.Builder builder,
            KnitModelMirror modelMirror) {

        MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("java.lang.Object parent = new $L()",
                        modelMirror.enclosingClass.getQualifiedName())
                .addParameter(KnitFileStrings.TYPE_NAME_SCHEDULER_PROVIDER, "schedulerProvider")
                .addStatement("this.parentExposer = new "
                                + modelMirror.enclosingClass.getQualifiedName().toString()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX + "(($L)parent)",
                        modelMirror.enclosingClass.getQualifiedName().toString())
                .addStatement("this.schedulerProvider = schedulerProvider")
                .addStatement("this.userMap = new java.util.concurrent.ConcurrentHashMap<>()");

        Set<VariableElement> fields = new HashSet<>();
        fields.addAll(modelMirror.generatesParamsMap.values());
        fields.addAll(modelMirror.inputterField.values());

        for (VariableElement generator : fields) {
            constructorBuilder.addStatement("this." + generator.getSimpleName().toString() + " = this.parentExposer.get_" + generator.getSimpleName() + "()");
            constructorBuilder.addStatement("this.$L$L = new $L()",generator.getSimpleName().toString(),"Lock",TypeName.OBJECT);
        }

        builder.addMethod(constructorBuilder.build());
    }

    private void createGetHandledValuesMethod(TypeSpec.Builder builder,
                                              KnitModelMirror modelMirror){
        Set<String> handledVals = new HashSet<>();

        for (String[] params : modelMirror.generatesParamsMap.keySet()) {
            handledVals.addAll(Arrays.asList(params));
        }

        for (String[] params : modelMirror.inputterField.keySet()) {
            handledVals.addAll(Arrays.asList(params));
        }

        String handledValsArray = KnitFileStrings.createStringArrayField(handledVals);

        MethodSpec getHandledValsMethod = MethodSpec
                .methodBuilder(KNIT_MODEL_GETHANDLEDVALUES)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class)
                .addStatement("return " + handledValsArray)
                .build();

        builder.addMethod(getHandledValsMethod);
    }

    private void createGetParentMethod(TypeSpec.Builder builder,
                                       KnitModelMirror modelMirror){

        MethodSpec getParentMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_GET_PARENT_METHOD)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_EXT))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.parentExposer.getParent()")
                .build();

        builder.addMethod(getParentMethod);
    }

    private void createDefaultMethods(TypeSpec.Builder builder,
                                      KnitModelMirror modelMirror){

        for(String methodName: defaultMethods){
            MethodSpec methodSpec = MethodSpec
                    .methodBuilder(methodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.parentExposer.use_$L()",
                            methodName)
                    .build();
            builder.addMethod(methodSpec);
        }
    }

    private void createInputMethod(TypeSpec.Builder builder,
            KnitModelMirror modelMirror) {

        MethodSpec.Builder inputMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_INPUT_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "data")
                .addParameter(Object[].class, "params", Modifier.FINAL);

        for (String[] generatedVals : modelMirror.inputterField.keySet()) {
            List<String> types = GeneratorExaminer.getGenerateTypes(
                    modelMirror.inputterField.get(generatedVals));
            String condition = createConditionBlock(generatedVals);
            inputMethodBuilder.beginControlFlow("if($L)", condition);
            inputMethodBuilder.addStatement("$L.input($L)",
                    modelMirror.inputterField.get(generatedVals).getSimpleName(),
                    createParamBlock(types, 0));
            inputMethodBuilder.endControlFlow();
        }

        builder.addMethod(inputMethodBuilder.build());
    }

    private void createRequestMethodForPresenter(TypeSpec.Builder builder,
            KnitModelMirror modelMirror, Map<KnitModelMirror, Set<UserMirror>> map) {

        MethodSpec.Builder requestMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_REQUEST_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "data")
                .addParameter(KnitFileStrings.TYPE_NAME_SCHEDULER_ENUM, "runOn")
                .addParameter(KnitFileStrings.TYPE_NAME_SCHEDULER_ENUM, "consumeOn")
                .addParameter(entityInstanceName, "instance")
                .addParameter(Object[].class, "params", Modifier.FINAL);


        for (String[] generatedVals : modelMirror.generatesParamsMap.keySet()) {
            if (map.containsKey(modelMirror)) {
                String condition = createConditionBlock(generatedVals);
                requestMethodBuilder.beginControlFlow("if($L)", condition);
                Set<UserMirror> users = map.get(modelMirror);
                for (UserMirror userMirror : users) {
                    if (methodExists(generatedVals, userMirror)) {
                        requestMethodBuilder.beginControlFlow("if(instance.instanceOf($L.class))",
                                userMirror.qualifiedName);
                        requestMethodBuilder.beginControlFlow("if(!userMap.containsKey($L))",
                                "instance");
                        requestMethodBuilder.addStatement("userMap.put(instance,new $L$L($L))",
                                userMirror.qualifiedName,
                                KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX, "instance");
                        requestMethodBuilder.endControlFlow();
                        String userText = userMirror.qualifiedName+
                                KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX;
                        requestMethodBuilder.addStatement(
                                "final $L user = ($L)userMap.get(instance)",
                                userText,
                                userText);

                        List<String> types = GeneratorExaminer.getGenerateTypes(
                                modelMirror.generatesParamsMap.get(generatedVals));


                        String knitTaskFlow = KnitFileStrings.KNIT_TASK_FLOW;

                        TypeSpec callable = createCallableTypeSpec(generatedVals, types,
                                modelMirror);
                        TypeSpec consumer = createConsumerTypeSpec(generatedVals, types, userMirror);

                        requestMethodBuilder.addStatement(
                                "$L.create($L).runOn(schedulerProvider.forType(runOn)).consumeOn"
                                        + "(schedulerProvider.forType(consumeOn)).start($L)",
                                knitTaskFlow, callable, consumer);

                        requestMethodBuilder.endControlFlow();
                    }
                }
                requestMethodBuilder.endControlFlow();
            }

        }


        builder.addMethod(requestMethodBuilder.build());
    }

    private void createRequestImmediateMethod(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror) {
        MethodSpec.Builder requestImmediateMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_REQUEST_IMMEDIATE_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE))
                .addParameter(String.class, "data")
                .addParameter(Object[].class, "params", Modifier.FINAL);

        for (String[] generatedVals : modelMirror.generatesParamsMap.keySet()) {
                String condition = createConditionBlock(generatedVals);
                requestImmediateMethodBuilder.beginControlFlow("if($L)", condition);
                List<String> types = GeneratorExaminer.getGenerateTypes(modelMirror.generatesParamsMap.get(generatedVals));
                requestImmediateMethodBuilder.beginControlFlow("synchronized($L$L)",modelMirror.generatesParamsMap.get(generatedVals).getSimpleName(),"Lock");
                requestImmediateMethodBuilder.addStatement("return $L.generate($L)", modelMirror.generatesParamsMap.get(generatedVals).getSimpleName(),createParamBlock(types,1));
                requestImmediateMethodBuilder.endControlFlow();

            requestImmediateMethodBuilder.endControlFlow();

        }

        requestImmediateMethodBuilder.addStatement("return null");
        clazzBuilder.addMethod(requestImmediateMethodBuilder.build());

    }


    private String createParamBlock(List<String> params, int padding) {
        StringBuilder paramBuilder = new StringBuilder();

        for (int i = padding; i < params.size(); i++) {
            paramBuilder.append("(");
            paramBuilder.append(params.get(i));
            paramBuilder.append(")");
            paramBuilder.append("params[");
            paramBuilder.append(i - padding);
            paramBuilder.append("]");

            if (i < params.size() - 1) {
                paramBuilder.append(",");
            }
        }
        return paramBuilder.toString();
    }


    private String createConditionBlock(String[] params) {
        StringBuilder conditionBuilder = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            conditionBuilder.append("\"");
            conditionBuilder.append(params[i]);
            conditionBuilder.append("\"");
            conditionBuilder.append(".equals(data)");

            if (i < params.length - 1) {
                conditionBuilder.append("||");
            }
        }
        return conditionBuilder.toString();
    }


    private String findMethod(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (String data : userMirror.methodMap.keySet()) {
                if (data.equals(param)) {
                    return "use_" + userMirror.userMethodNames.get(data);
                }
            }
        }
        return "";
    }

    private boolean methodExists(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (String data : userMirror.methodMap.keySet()) {
                if (data.equals(param)) {
                    return true;
                }
            }
        }
        return false;
    }


    private TypeSpec createCallableTypeSpec(String[] params, List<String> types,
            KnitModelMirror modelMirror) {

        ClassName knitResponse = ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE);
        TypeName responseType = GeneratorExaminer.getName(types.get(0));

        ParameterizedTypeName parameterizedKnitResponse = ParameterizedTypeName.get(knitResponse, responseType);


        ParameterizedTypeName parameterizedCallable = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_CALLABLE, parameterizedKnitResponse);

        TypeSpec callable = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(parameterizedCallable)
                .addMethod(MethodSpec
                        .methodBuilder("call")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(parameterizedKnitResponse)
                        .beginControlFlow("synchronized($L$L)",modelMirror.generatesParamsMap.get(params).getSimpleName(),"Lock")
                        .addStatement("return $L.generate($L)",
                                modelMirror.generatesParamsMap.get(
                                        params).getSimpleName(),
                                createParamBlock(types, 1))
                        .endControlFlow()
                        .build())
                .build();

        return callable;
    }

    private TypeSpec createConsumerTypeSpec(String[] params, List<String> types, UserMirror userMirror) {
        ClassName knitResponse = ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE);
        TypeName responseType = GeneratorExaminer.getName(types.get(0));
        ParameterizedTypeName parameterizedKnitResponse = ParameterizedTypeName.get(knitResponse,
                responseType);
        ParameterizedTypeName parameterizedConsumerName = ParameterizedTypeName.get(
                KnitFileStrings.TYPE_NAME_CONSUMER, parameterizedKnitResponse);
        ClassName throwableName = ClassName.bestGuess("java.lang.Throwable");

        TypeSpec consumer = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(parameterizedConsumerName)
                .addMethod(MethodSpec
                        .methodBuilder("consume")
                        .addAnnotation(Override.class)
                        .addParameter(parameterizedKnitResponse, "t")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("user.$L(t)",
                                findMethod(params, userMirror))
                        .build())
                .addMethod(MethodSpec
                        .methodBuilder("error")
                        .addAnnotation(Override.class)
                        .addParameter(throwableName, "error")
                        .addModifiers(Modifier.PUBLIC).build())
                .build();

        return consumer;

    }


}
