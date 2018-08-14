package com.travelbank.knit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

/**
 *  Utility class that extracts certain Android native components out of view objects and starts fragments.
 *
 *  @author Omer Ozer
 */

public class AndroidViewUtility {

    /**
     * Extract {@link FragmentManager} from the given view object.
     * @param view given view object.
     * @return {@link FragmentManager} that is extracted.
     */
    static FragmentManager extractFragmentManager(Object view){
        if(view instanceof AppCompatActivity){
            return ((AppCompatActivity)view).getFragmentManager();
        }
        if(view instanceof Activity){
            return ((Activity)view).getFragmentManager();
        }
        if(view instanceof Fragment){
            return extractFragmentManager(((Fragment)view).getActivity());
        }
        return null;
    }

    /**
     * Extract {@link android.support.v4.app.FragmentManager} from the given view object.
     * @param view given view object.
     * @return {@link android.support.v4.app.FragmentManager} that is extracted.
     */
    static android.support.v4.app.FragmentManager extractSupportFragmentManager(Object view){
        if(view instanceof FragmentActivity){
            return ((FragmentActivity)view).getSupportFragmentManager();
        }
        if(view instanceof android.support.v4.app.Fragment){
            return ((android.support.v4.app.Fragment)view).getFragmentManager();
        }
        if(view instanceof Fragment){
            extractSupportFragmentManager(((Fragment)view).getActivity());
        }
        return null;
    }

    /**
     * Returns an instance of the given {@link Fragment} class.
     * @param clazz given {@link Fragment} class.
     * @return Initialized {@link Fragment}.
     */
    static Fragment initFragment(Class<? extends Fragment> clazz){
        try {
            return clazz.getConstructor().newInstance();
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

    /**
     * Returns an instance of the given {@link android.support.v4.app.Fragment} class.
     * @param clazz given {@link android.support.v4.app.Fragment} class.
     * @return Initialized {@link android.support.v4.app.Fragment}.
     */
    static android.support.v4.app.Fragment initSupportFragment(Class<? extends android.support.v4.app.Fragment> clazz){
        try {
            return clazz.getConstructor().newInstance();
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

    /**
     * Extracts an {@link Activity} from an Android ui component such as {@link Fragment} or {@link android.view.View}.
     * @param viewObject given view object.
     * @return extracted {@link Activity}
     */
    static Activity extractActivity(Object viewObject){
        if (viewObject instanceof Activity){
            return (Activity) viewObject;
        }

        if(viewObject instanceof Fragment){
            return ((Fragment)viewObject).getActivity();
        }

        if(viewObject instanceof android.support.v4.app.Fragment){
            return ((android.support.v4.app.Fragment)viewObject).getActivity();
        }

        if(viewObject instanceof View){
            return (Activity)((View)viewObject).getContext();
        }
        throw new RuntimeException("Knit: View object must be an Android UI/View such as Activity,Fragment,View.");
    }


    /**
     * Extracts an {@link View} from an Android ui component such as {@link Fragment} or {@link android.view.View}.
     * @param viewObject given view object.
     * @return extracted {@link View}
     */
    public static View extractRootView(Object viewObject){
        if (viewObject instanceof Activity){
            return ((Activity) viewObject).getWindow().getDecorView().getRootView();
        }

        if(viewObject instanceof Fragment){
            return ((Fragment)viewObject).getView();
        }

        if(viewObject instanceof android.support.v4.app.Fragment){
            return ((android.support.v4.app.Fragment)viewObject).getView();
        }

        if(viewObject instanceof View){
            return (View)viewObject;
        }
        throw new RuntimeException("Knit: View object must be an Android UI/View such as Activity,Fragment,View.");
    }

}
