package com.travelbank.knitprocessor.interactor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.travelbank.knitprocessor.GeneratorExaminer;
import com.travelbank.knitprocessor.KnitClassWriter;
import com.travelbank.knitprocessor.KnitFileStrings;
import com.travelbank.knitprocessor.KnitMethodsFilter;
import com.travelbank.knitprocessor.StringUtil;
import com.travelbank.knitprocessor.Tuple2;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 5/16/18.
 */

public class InteractorWriter extends KnitClassWriter {

    public void write(Filer filer, Element presenter, Set<Tuple2<String, Element>> models) {

        TypeSpec.Builder interactorClassBuilder = TypeSpec
                .classBuilder(presenter.getSimpleName()
                        + KnitFileStrings.KNIT_INTERACTOR_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        addKnitWarning(interactorClassBuilder);

        createConstructor(interactorClassBuilder, presenter, models);
        createFields(interactorClassBuilder, presenter, models);
        createMethods(interactorClassBuilder, models);

        PackageElement packageElement =
                (PackageElement) presenter.getEnclosingElement();
        writeToFile(filer, packageElement.getQualifiedName().toString(), interactorClassBuilder);

    }

    private void createFields(TypeSpec.Builder clazzBuilder,
            Element presenter, Set<Tuple2<String, Element>> models) {

        int v = 0;
        for (Tuple2<String, Element> pair : models) {
            TypeElement type = (TypeElement) pair.getB();
            clazzBuilder.addField(
                    FieldSpec.builder(ClassName.bestGuess(type.getQualifiedName().toString()
                                    + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX),
                            "exposer" + v++).addModifiers(Modifier.PRIVATE).build());
        }
    }

    private void createConstructor(TypeSpec.Builder clazzBuilder,
            Element presenter, Set<Tuple2<String, Element>> models) {

        MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(KnitFileStrings.TYPE_NAME_KNIT, "knit");

        int v = 0;
        for (Tuple2<String, Element> pair : models) {
            TypeElement type = (TypeElement) pair.getB();

            constructorBuilder.addStatement(
                    "this.$L$L = ($L$L)(($L$L)knit.getModelManager().getModelThatGeneratesData"
                            + "($S)).getParentExposer()",
                    "exposer", Integer.toString(v++), pair.getB().getSimpleName(),
                    KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX, type.getQualifiedName(),
                    KnitFileStrings.KNIT_MODEL_POSTFIX, pair.getA());
        }

        clazzBuilder.addMethod(constructorBuilder.build());
    }

    private void createMethods(TypeSpec.Builder clazzBuilder, Set<Tuple2<String, Element>> models) {
        int v = 0;
        for (Tuple2<String, Element> pair : models) {
            TypeElement type = (TypeElement) pair.getB();
            for (Element enclosed : type.getEnclosedElements()) {
                if (enclosed.getKind().equals(ElementKind.FIELD)
                        && !enclosed.getModifiers().contains(Modifier.PRIVATE)
                        && !enclosed.getModifiers().contains(Modifier.STATIC) && GeneratorExaminer.filter(enclosed)) {
                    clazzBuilder.addMethod(createGetter((VariableElement) enclosed, v));
                    clazzBuilder.addMethod(createSetter((VariableElement) enclosed, v));
                }
            }
            v++;
        }

    }

    private MethodSpec createGetter(VariableElement variableElement, int v) {
        return MethodSpec.methodBuilder(
                "get" + StringUtil.firstLetterToCaps(variableElement.getSimpleName().toString()))
                .returns(TypeName.get(variableElement.asType()))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L$L.$L$L()", "exposer", Integer.toString(v), "get",
                        StringUtil.firstLetterToCaps(variableElement.getSimpleName().toString()))
                .build();
    }

    private MethodSpec createSetter(VariableElement variableElement, int v) {
        return MethodSpec.methodBuilder(
                "set" + StringUtil.firstLetterToCaps(variableElement.getSimpleName().toString()))
                .addParameter(TypeName.get(variableElement.asType()), "param")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$L$L.$L$L(param)", "exposer", Integer.toString(v), "set",
                        StringUtil.firstLetterToCaps(variableElement.getSimpleName().toString()))
                .build();
    }


}
