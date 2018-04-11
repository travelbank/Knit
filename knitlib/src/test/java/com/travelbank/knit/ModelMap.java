package com.travelbank.knit;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by omerozer on 3/8/18.
 */

public class ModelMap implements ModelMapInterface {

    private static ModelMap mockModelMap;

    public static ModelMap getMock() {

        mockModelMap = Mockito.mock(ModelMap.class);
        when(mockModelMap.getAll()).thenReturn(
                Arrays.asList(TestModel_Model.class, TestModel2_Model.class,
                        TestSingleton_Model.class, UmbrellaModel_Model.class));

        when(mockModelMap.getGeneratedValues(TestModel_Model.class)).thenReturn(
                Arrays.asList("v1"));
        when(mockModelMap.getGeneratedValues(TestModel2_Model.class)).thenReturn(
                Arrays.asList("v2"));
        when(mockModelMap.getGeneratedValues(UmbrellaModel_Model.class)).thenReturn(
                Arrays.asList("umb"));
        when(mockModelMap.getGeneratedValues(TestSingleton_Model.class)).thenReturn(
                Arrays.asList("singltn"));

        when(mockModelMap.getRequiredValues(TestModel_Model.class)).thenReturn(
                Arrays.<String>asList());
        when(mockModelMap.getRequiredValues(TestModel2_Model.class)).thenReturn(
                Arrays.<String>asList());
        when(mockModelMap.getRequiredValues(UmbrellaModel_Model.class)).thenReturn(
                Arrays.asList("v1", "v2", "singltn"));
        when(mockModelMap.getRequiredValues(TestSingleton_Model.class)).thenReturn(
                Arrays.<String>asList());

        when(mockModelMap.isModelSingleton(TestModel_Model.class)).thenReturn(false);
        when(mockModelMap.isModelSingleton(TestModel2_Model.class)).thenReturn(false);
        when(mockModelMap.isModelSingleton(UmbrellaModel_Model.class)).thenReturn(false);
        when(mockModelMap.isModelSingleton(TestSingleton_Model.class)).thenReturn(true);

        return mockModelMap;
    }

    @Override
    public List<Class<? extends InternalModel>> getAll() {
        return Arrays.asList(TestModel_Model.class, TestModel2_Model.class,
                TestSingleton_Model.class, UmbrellaModel_Model.class);
    }

    @Override
    public List<String> getGeneratedValues(Class<?> clazz) {

        if (clazz.equals(TestModel_Model.class)) {
            return Arrays.asList("v1");
        }

        if (clazz.equals(TestModel2_Model.class)) {
            return Arrays.asList("v2");
        }

        if (clazz.equals(UmbrellaModel_Model.class)) {
            return Arrays.asList("umb");
        }

        if (clazz.equals(TestSingleton_Model.class)) {
            return Arrays.asList("singltn");
        }


        return null;
    }

    @Override
    public List<String> getRequiredValues(Class<?> clazz) {

        if (clazz.equals(TestModel_Model.class) || clazz.equals(TestModel2_Model.class)) {
            return Arrays.asList();
        }

        if (clazz.equals(TestSingleton_Model.class)) {
            return Arrays.asList();
        }

        if (clazz.equals(UmbrellaModel_Model.class)) {
            return Arrays.asList("v1", "v2", "singltn");
        }

        return null;
    }

    @Override
    public boolean isModelSingleton(Class<? extends InternalModel> clazz) {
        if (clazz.equals(TestSingleton_Model.class)) {
            return true;
        }
        if (clazz.equals(TestModel_Model.class)) {
            return false;
        }
        if (clazz.equals(TestModel2_Model.class)) {
            return false;
        }
        if (clazz.equals(UmbrellaModel_Model.class)) {
            return false;
        }
        return false;
    }

    @Override
    public Class<? extends InternalModel> getModelClassForModel(Class<? extends KnitModel> target) {
        if (target.equals(TestModel.class)) {
            return TestModel_Model.class;
        }
        if (target.equals(TestModel2.class)) {
            return TestModel2_Model.class;
        }
        if (target.equals(TestSingletonModel.class)) {
            return TestSingleton_Model.class;
        }
        if (target.equals(UmbrellaModel.class)) {
            return UmbrellaModel_Model.class;
        }
        return null;
    }
}
