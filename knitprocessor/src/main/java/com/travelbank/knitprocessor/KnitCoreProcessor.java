package com.travelbank.knitprocessor;

import com.travelbank.knitprocessor.model.KnitModelProcessor;
import com.travelbank.knitprocessor.user.KnitUserProcessor;
import com.travelbank.knitprocessor.user.UserMirror;
import com.travelbank.knitprocessor.vp.KnitPresenterProcessor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Created by omerozer on 5/16/18.
 */

public class KnitCoreProcessor extends AbstractProcessor {

    private KnitPresenterProcessor knitPresenterProcessor;
    private KnitModelProcessor knitModelProcessor;
    private KnitUserProcessor knitUserProcessor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.knitPresenterProcessor = new KnitPresenterProcessor(processingEnvironment);
        this.knitModelProcessor = new KnitModelProcessor(processingEnvironment);
        this.knitUserProcessor = new KnitUserProcessor(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            Set<UserMirror> userMirrors = new HashSet<>();
            knitPresenterProcessor.process(set, roundEnvironment, userMirrors);
            knitUserProcessor.process(userMirrors);
            knitModelProcessor.process(set, roundEnvironment, userMirrors);
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return KnitAnnotations.getAll();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
