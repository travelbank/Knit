package com.travelbank.knit.components.graph;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.travelbank.knit.AndroidViewUtility;

/**
 * Created by Omer Ozer on 8/13/2018.
 */

public class ViewObserver {

    public static void observeView(Object viewObject,Listener listener){
        View root = AndroidViewUtility.extractRootView(viewObject);
        root.getViewTreeObserver().addOnGlobalLayoutListener(getLayoutListener(root,listener));
    }

    private static ViewTreeObserver.OnGlobalLayoutListener getLayoutListener(final View view,final Listener listener){
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                listener.onViewInflated();
            }
        };
    }


    public interface Listener{
        void onViewInflated();
    }



}
