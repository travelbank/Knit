package com.travelbank.knitprocessor.interactor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.model.KnitModelMirror;
import com.travelbank.knitprocessor.vp.KnitPresenterMirror;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

/**
 * Created by omerozer on 5/16/18.
 */

public class InteractorWriter extends KnitClassWriter {

    public void write(Filer filer, KnitPresenterMirror presenterMirror,Set<KnitModelMirror> modelMirrorSet) {

        TypeSpec.Builder interactorClassBuilder = TypeSpec
                .classBuilder(presenterMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_INTERACTOR_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(interactorClassBuilder);

        createConstructor(interactorClassBuilder, presenterMirror);


        PackageElement packageElement =
                (PackageElement) presenterMirror.enclosingClass.getEnclosingElement();
        writeToFile(filer, packageElement.getQualifiedName().toString(), interactorClassBuilder);

    }

    private void createFields(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror){

        //FieldSpec

    }

    private void createConstructor(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        clazzBuilder.addMethod(
                MethodSpec
                        .constructorBuilder()
                        .addParameter(KnitFileStrings.TYPE_NAME_KNIT, "knit")
                        .addParameter(KnitFileStrings.TYPE_NAME_KNIT_PRESENTER, "presenter")
                        .addStatement("this.modelManager = knit.getModelManager()")
                        .addStatement("this.values = knit.getViewToPresenterMap().getUpdatingValues(presenter.getClass())")
                        .build()
        );
    }



}
