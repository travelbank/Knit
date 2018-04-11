package com.travelbank.knit;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by Omer Ozer on 3/8/18.
 */

public class ViewToPresenterMap implements ViewToPresenterMapInterface {


    public static ViewToPresenterMap getMock() {

        ViewToPresenterMap mockViewToPresenterMap = Mockito.mock(ViewToPresenterMap.class);

        when(mockViewToPresenterMap.getAllViews()).thenReturn(
                Arrays.asList(View1.class, View2.class));

        when(mockViewToPresenterMap.getUpdatingValues(TestPresenter_Presenter.class)).thenReturn(
                Arrays.asList("umb"));
        when(mockViewToPresenterMap.getUpdatingValues(TestPresenter2_Presenter.class)).thenReturn(
                Arrays.<String>asList());

        when(mockViewToPresenterMap.getPresenterClassForPresenter(TestPresenter.class)).thenReturn(
                TestPresenter_Presenter.class);
        when(mockViewToPresenterMap.getPresenterClassForPresenter(TestPresenter2.class)).thenReturn(
                TestPresenter2_Presenter.class);

        when(mockViewToPresenterMap.getPresenterClassForView(View1.class)).thenReturn(
                TestPresenter_Presenter.class);
        when(mockViewToPresenterMap.getPresenterClassForView(View2.class)).thenReturn(
                TestPresenter2_Presenter.class);

        return mockViewToPresenterMap;
    }

    @Override
    public Class getPresenterClassForView(Class viewClass) {
        if (viewClass.equals(View1.class)) {
            return TestPresenter_Presenter.class;
        }
        if (viewClass.equals(View2.class)) {
            return TestPresenter2_Presenter.class;
        }
        return null;
    }

    @Override
    public Class getPresenterClassForPresenter(Class parentClass) {
        if (parentClass.equals(TestPresenter.class)) {
            return TestPresenter_Presenter.class;
        }
        if (parentClass.equals(TestPresenter2.class)) {
            return TestPresenter2_Presenter.class;
        }
        return null;
    }

    @Override
    public List<Class<?>> getAllViews() {
        return Arrays.asList(View1.class, View2.class);
    }

    @Override
    public List<String> getUpdatingValues(Class clazz) {
        if (clazz.equals(TestPresenter_Presenter.class)) {
            return Arrays.asList("umb");
        }

        if (clazz.equals(TestPresenter2_Presenter.class)) {
            return Arrays.asList();
        }

        return null;
    }
}
