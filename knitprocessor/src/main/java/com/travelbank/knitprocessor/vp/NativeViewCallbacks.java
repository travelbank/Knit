package com.travelbank.knitprocessor.vp;

import static com.travelbank.knitprocessor.KnitFileStrings.ANDROID_INTENT;

import com.travelbank.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by omerozer on 3/9/18.
 */

public class NativeViewCallbacks {
    public static List<String> getAll(){
        return Arrays.asList("onViewStart","onViewResume","onViewPause","onViewStop","onViewResult","onReturnToView","receiveMessage");
    }

    private static boolean isOnViewResult(String result){
        return "onViewResult".equals(result);
    }

    private static boolean isReceiveMessage(String result){ return "receiveMessage".equals(result); }

    public static void createNativeCallbackMethodsForExposer(TypeSpec.Builder builder){
        for (String callback : getAll()) {
            if(isOnViewResult(callback)){
                builder.addMethod(MethodSpec
                        .methodBuilder("use_" + callback)
                        .addParameter(TypeName.INT,"req")
                        .addParameter(TypeName.INT,"res")
                        .addParameter(ANDROID_INTENT,"data")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.parent.$L(req,res,data)", callback)
                        .build());
                continue;
            }

            if(isReceiveMessage(callback)){
                builder.addMethod(MethodSpec
                        .methodBuilder("use_" + callback)
                        .addParameter(KnitFileStrings.TYPE_NAME_KNIT_MESSAGE,"msg")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.parent.$L(msg)", callback)
                        .build());
                continue;
            }

            builder.addMethod(MethodSpec
                    .methodBuilder("use_" + callback)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.parent.$L()", callback)
                    .build());
        }
    }

    public static void createNativeCallbackMethodsForPresenter(TypeSpec.Builder builder){
        for(String callback : getAll()){
            if(isOnViewResult(callback)){
                builder.addMethod(MethodSpec
                        .methodBuilder(callback)
                        .addParameter(TypeName.INT,"req")
                        .addParameter(TypeName.INT,"res")
                        .addParameter(ANDROID_INTENT,"data")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.parent.use_$L(req,res,data)", callback)
                        .build());
                continue;
            }

            if(isReceiveMessage(callback)){
                builder.addMethod(MethodSpec
                        .methodBuilder(callback)
                        .addParameter(KnitFileStrings.TYPE_NAME_KNIT_MESSAGE,"msg")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.parent.use_$L(msg)", callback)
                        .build());
                continue;
            }

            builder.addMethod(MethodSpec
                    .methodBuilder(callback)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.parent.use_$L()",callback)
                    .build());
        }
    }
}
