package com.omerozer.knitprocessor.vp;

import com.omerozer.knitprocessor.KnitClassWriter;
import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.ReturnTypeExaminer;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.ref.WeakReference;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

/**
 * Created by omerozer on 2/14/18.
 */

public class ContractWriter extends KnitClassWriter {

     public void write(Filer filer, KnitViewMirror viewMirror) {

        TypeSpec.Builder contractClassBuilder = TypeSpec
                .classBuilder(viewMirror.enclosingClass.getSimpleName()+ KnitFileStrings.KNIT_CONTRACT_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(contractClassBuilder);

       createParentField(contractClassBuilder,viewMirror);
       createConstructor(contractClassBuilder,viewMirror);
       //createNullcheckMethod(contractClassBuilder,viewMirror);
       createNonAndroidMethods(contractClassBuilder,viewMirror);

       PackageElement packageElement = (PackageElement)viewMirror.enclosingClass.getEnclosingElement();
       writeToFile(filer,packageElement.getQualifiedName().toString(),contractClassBuilder);

    }

    private void createParentField(TypeSpec.Builder builder,KnitViewMirror viewMirror){
        ParameterizedTypeName parentWeakName = ParameterizedTypeName.get(
                ClassName.bestGuess(WeakReference.class.getCanonicalName()),
                TypeName.get(viewMirror.enclosingClass.asType()));

        FieldSpec parentWeakField = FieldSpec
                .builder(parentWeakName,"parent")
                .addModifiers(Modifier.PRIVATE)
                .build();

        builder.addField(parentWeakField);
    }

    private void createConstructor(TypeSpec.Builder builder,KnitViewMirror viewMirror){
        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addParameter(TypeName.get(viewMirror.enclosingClass.asType()),"parent")
                .addStatement("this.parent = new WeakReference<>(parent)")
                .build();
        builder.addMethod(constructor);
    }

    private void createNullcheckMethod(TypeSpec.Builder builder, KnitViewMirror viewMirror){
        MethodSpec nullCheckMethod = MethodSpec
                .methodBuilder("nullCheck")
                .returns(TypeName.BOOLEAN)
                .addStatement("return this.parent==null || this.parent.get()==null")
                .build();

        builder.addMethod(nullCheckMethod);
    }

    private void createNonAndroidMethods(TypeSpec.Builder builder,KnitViewMirror viewMirror){
        for(ExecutableElement executableElement : viewMirror.methods){
            if(executableElement.getModifiers().contains(Modifier.PRIVATE)){
                continue;
            }
            MethodSpec.Builder methodBuilder = MethodSpec
                    .methodBuilder(executableElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC);

            int c = 0;
            StringBuilder paramBlock = new StringBuilder();
            for(Element param : executableElement.getParameters()){
                paramBlock.append(param.getSimpleName());
                methodBuilder.addParameter(TypeName.get(param.asType()),param.getSimpleName().toString());
                if(c < executableElement.getParameters().size()-1){paramBlock.append(",");}
                c++;
            }

            if(executableElement.getReturnType().toString().contains("void")){
//                methodBuilder.beginControlFlow("if(nullCheck())");
//                methodBuilder.addStatement("return");
//                methodBuilder.endControlFlow();
                methodBuilder.addStatement("this.parent.get().$L($L)",executableElement.getSimpleName(),paramBlock);
            }else{
                TypeName returnType = TypeName.get(executableElement.getReturnType());
                methodBuilder.returns(returnType);

//                methodBuilder.beginControlFlow("if(nullCheck())");
//                methodBuilder.addStatement("return $L", ReturnTypeExaminer.getDefaultReturnValueInString(returnType));
//                methodBuilder.endControlFlow();

                methodBuilder.addStatement("return this.parent.get().$L($L)",executableElement.getSimpleName(),paramBlock);
            }

            builder.addMethod(methodBuilder.build());
        }
    }





}
