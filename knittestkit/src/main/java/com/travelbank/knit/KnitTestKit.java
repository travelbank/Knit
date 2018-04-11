package com.travelbank.knit;

import com.travelbank.knit.classloaders.KnitPresenterLoader;
import com.travelbank.knit.classloaders.KnitUtilsLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by omerozer on 2/18/18.
 */

public final class KnitTestKit {

    public  static<T extends KnitPresenter> PresenterBuilder<T> presenterBuilder(Class<T> presenterClass){
        return new PresenterBuilder<T>().from(presenterClass);
    }
    public  static<T extends KnitModel> ModelBuilder<T> modelBuilder(Class<T> modelClass){
        return new ModelBuilder<T>().from(modelClass);
    }

    public static class PresenterBuilder<T extends KnitPresenter>{

        private Class<T> clazz;
        private Knit knit;
        private InternalModel modelManager;
        private KnitNavigator navigator;
        private Object contract;

        PresenterBuilder<T> from(Class<T> clazz){
            this.clazz = clazz;
            return this;
        }

        public PresenterBuilder<T> setKnit(Knit knit){
            this.knit = knit;
            return this;
        }

        public PresenterBuilder<T> setModelManager(InternalModel internalModel){
            this.modelManager = internalModel;
            return this;
        }

        public PresenterBuilder<T> setNavigator(KnitNavigator navigator){
            this.navigator = navigator;
            return this;
        }

        public PresenterBuilder<T> usingContract(Object contract){
            this.contract = contract;
            return this;
        }

        public InternalPresenter build(){
                KnitPresenterLoader knitPresenterLoader = new KnitPresenterLoader(knit);
                ViewToPresenterMapInterface viewToPresenterMap = new KnitUtilsLoader().getViewToPresenterMap(Knit.class);
                InternalPresenter internalPresenter = knitPresenterLoader.loadPresenter(viewToPresenterMap.getPresenterClassForPresenter(clazz));
                T presenter = (T)internalPresenter.getParent();
                presenter.setKnit(knit);
                presenter.setContract(contract);
                presenter.setModelManager(modelManager);
                presenter.setNavigator(navigator);
                return internalPresenter;
        }

    }

    public static class ModelBuilder<T extends KnitModel>{
        private Class<T> clazz;
        private InternalModel modelManager;

        ModelBuilder<T> from(Class<T> clazz){
            this.clazz = clazz;
            return this;
        }

        public ModelBuilder<T> setModelManager(InternalModel internalModel){
            this.modelManager = internalModel;
            return this;
        }

        public T build(){
            try {
                T model = clazz.getConstructor().newInstance();
                model.setModelManager(modelManager);
                return model;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
