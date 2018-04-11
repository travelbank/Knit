package com.travelbank.knitprocessor.user;

import com.travelbank.knit.Use;
import com.travelbank.knit.UseMethod;
import com.travelbank.knitprocessor.KnitAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by omerozer on 2/4/18.
 */

public class KnitUserProcessor extends AbstractProcessor {

    List<UserMirror> userMirrors;
    UserWriter userWriter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.userMirrors = new ArrayList<>();
        this.userWriter = new UserWriter();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        processUsers(roundEnvironment.getElementsAnnotatedWith(Use.class));
        createUsers(userMirrors);

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return KnitAnnotations.getStageTwo();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void processUsers(Set<? extends Element> users) {
        for (Element user : users) {
            UserMirror userMirror = new UserMirror();
            userMirror.enclosingClass = (TypeElement) user;
            for (Element element : user.getEnclosedElements()) {
                if (element.getKind().equals(ElementKind.METHOD) && element.getAnnotation(
                        UseMethod.class) != null) {
                    userMirror.method.add((ExecutableElement) element);
                    userMirror.requiredValues.add(element.getAnnotation(UseMethod.class).value());
                }
            }
            this.userMirrors.add(userMirror);
        }
    }

    private void createUsers(List<UserMirror> users) {
        for (UserMirror userMirror : users) {
            userWriter.write(processingEnv.getFiler(), userMirror);
        }
    }


}
