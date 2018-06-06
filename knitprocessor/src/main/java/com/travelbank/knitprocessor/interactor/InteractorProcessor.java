package com.travelbank.knitprocessor.interactor;

import com.travelbank.knit.Collects;
import com.travelbank.knit.Generates;
import com.travelbank.knit.Inputs;
import com.travelbank.knit.Model;
import com.travelbank.knit.ModelEvent;
import com.travelbank.knit.Presenter;
import com.travelbank.knitprocessor.KnitBaseProcessor;
import com.travelbank.knitprocessor.Tuple2;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Created by omerozer on 5/16/18.
 */

public class InteractorProcessor extends KnitBaseProcessor {

    InteractorWriter interactorWriter;

    public InteractorProcessor(ProcessingEnvironment env) {
        super(env);
        interactorWriter = new InteractorWriter();
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        createInteractors(mapPresentersToModels(roundEnvironment));
        return true;
    }

    private Map<Element, Set<Tuple2<String,Element>>> mapPresentersToModels(RoundEnvironment roundEnvironment) {
        Map<Element, Set<String>> presenterToRequiredValueMap = processPresenters(
                roundEnvironment.getElementsAnnotatedWith(Presenter.class));

        Map<Element, Set<String>> modelToGeneratedMap = processModels(
                roundEnvironment.getElementsAnnotatedWith(Model.class));

        return createPresenterToModelsMap(
                presenterToRequiredValueMap, modelToGeneratedMap);

    }

    private Map<Element, Set<String>> processPresenters(Set<? extends Element> presenters) {
        Map<Element, Set<String>> presenterToRequiredValueMap = new LinkedHashMap<>();
        for (Element presenter : presenters) {
            presenterToRequiredValueMap.put(presenter, new LinkedHashSet<String>());
            TypeElement clazz = (TypeElement) presenter;

            String[] needs = presenter.getAnnotation(Presenter.class).needs();
            if (needs[0] != "") {
                presenterToRequiredValueMap.get(presenter).addAll(Arrays.<String>asList(needs));
            }
            for (Element element : clazz.getEnclosedElements()) {

                if (element.getKind().equals(ElementKind.METHOD)) {
                    if (element.getAnnotation(ModelEvent.class) != null) {
                        presenterToRequiredValueMap.get(presenter).addAll(
                                Collections.<String>singletonList(
                                        element.getAnnotation(ModelEvent.class).value()));
                    }
                }
            }
        }
        return presenterToRequiredValueMap;
    }

    private Map<Element, Set<String>> processModels(Set<? extends Element> models) {
        Map<Element, Set<String>> modelToGeneratedMap = new LinkedHashMap<>();
        for (Element model : models) {
            TypeElement clazz = (TypeElement) model;
            modelToGeneratedMap.put(model, new LinkedHashSet<String>());
            for (Element element : clazz.getEnclosedElements()) {
                if (element.getKind().isField()) {
                    if (element.getAnnotation(Generates.class) != null) {
                        String[] params = element.getAnnotation(Generates.class).value();
                        modelToGeneratedMap.get(model).addAll(Arrays.asList(params));
                    } else if (element.getAnnotation(Collects.class) != null) {
                        String[] params = element.getAnnotation(Collects.class).value();
                        modelToGeneratedMap.get(model).addAll(Arrays.asList(params));
                    } else if (element.getAnnotation(Inputs.class) != null) {
                        String[] params = element.getAnnotation(Inputs.class).value();
                        modelToGeneratedMap.get(model).addAll(Arrays.asList(params));
                    }
                }
            }
        }
        return modelToGeneratedMap;
    }

    private Map<Element, Set<Tuple2<String,Element>>> createPresenterToModelsMap(Map<Element, Set<String>> pm,
            Map<Element, Set<String>> mm) {
        Map<Element, Set<Tuple2<String,Element>>> presenterToModelMap = new LinkedHashMap<>();
        for (Element presenter : pm.keySet()) {
            presenterToModelMap.put(presenter, new LinkedHashSet<Tuple2<String,Element>>());
            for (String required : pm.get(presenter)) {
                for (Element model : mm.keySet()) {
                    if (mm.get(model).contains(required)) {
                        presenterToModelMap.get(presenter).add(new Tuple2<String, Element>(required,model));
                        break;
                    }
                }
            }

        }
        return presenterToModelMap;
    }

    private void createInteractors(Map<Element, Set<Tuple2<String,Element>>> m) {
        for (Element presenter : m.keySet()) {
            interactorWriter.write(getEnv().getFiler(), presenter, m.get(presenter));
        }
    }
}
