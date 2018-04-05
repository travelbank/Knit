package com.omerozer.knit.classloaders;

import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.ViewToPresenterMapInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * Initializes {@link ModelMapInterface} and {@link ViewToPresenterMapInterface} classes
 * with the data created by the KnitProcessor. The loaded classes will be {@code ModelMap} and {@code ViewToPresenterMap}
 * that implement {@link ModelMapInterface} and {@link ViewToPresenterMapInterface} respectively
 *
 * @author Omer Ozer
 */

public class KnitUtilsLoader {

    private static final String KNIT_VIEW_PRESENTER_MAP = "com.omerozer.knit.ViewToPresenterMap";
    private static final String KNIT_MODEL_MAP = "com.omerozer.knit.ModelMap";

    private Constructor<?> getConstructorForModelMap(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        try {
            Class<?> presenter = classLoader.loadClass(KNIT_MODEL_MAP);
            Constructor<?> constructor = presenter.getConstructor();

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Initializes the {@link ModelMapInterface} required by the current environment
     * @param clazz base class required by the {@link ClassLoader}. Knit passes a {@code Knit.class} to it.
     * @return This is the {@code ModelMap implements ModelMapInterface} class.
     */
    public ModelMapInterface getModelMap(Class<?> clazz){
        try {
            return (ModelMapInterface)getConstructorForModelMap(clazz).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<?> getViewToPresenterConstructor(Class<?> clazz) {

        ClassLoader classLoader = clazz.getClassLoader();

        try {
            Class<?> presenterMapClazz = classLoader.loadClass(KNIT_VIEW_PRESENTER_MAP);
            Constructor<?> constructor = presenterMapClazz.getConstructor();

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Initializes the {@link ViewToPresenterMapInterface} required by the current environment
     * @param clazz base class required by the {@link ClassLoader}. Knit passes a {@code Knit.class} to it.
     * @return This is the {@code ViewToPresenterMap implements ViewToPresenterMapInterface} class.
     */
    public ViewToPresenterMapInterface getViewToPresenterMap(Class<?> clazz){
        try {
            return (ViewToPresenterMapInterface)getViewToPresenterConstructor(clazz).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
