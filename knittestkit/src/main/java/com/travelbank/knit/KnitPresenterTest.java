package com.travelbank.knit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by omerozer on 2/20/18.
 */

@RunWith(JUnit4.class)
public abstract class KnitPresenterTest<T extends KnitPresenter,K,I> implements BaseTest {

    private T testSubject;

    private InternalPresenter internalObject;

    @Before
    public void init(){
        testSubject = null;
        setup();
    }

    public void setup(){

    }

    protected abstract K getViewWrapper();

    protected abstract I getAccesor();

    protected abstract KnitNavigator getNavigator();

    protected abstract Class<T> getPresenterClass();

    protected T getPresenter(){
        if(testSubject==null){
            internalObject = KnitTestKit
                    .presenterBuilder(getPresenterClass())
                    .setKnit(getKnitInstance())
                    .setModelManager(getModelManager())
                    .setNavigator(getNavigator())
                    .usingContract(getViewWrapper())
                    .usingAccessor(getAccesor())
                    .build();
            testSubject = (T)internalObject.getParent();
        }
        return testSubject;
    }

    InternalPresenter getInternalObject(){
        return internalObject;
    }

}
