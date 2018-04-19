package com.travelbank.knit;


import android.app.Activity;

import java.lang.ref.WeakReference;
//Todo(Omer): work on it later
public class AndroidView {

    public static AndroidView get(Object object){
        return new AndroidView(object);
    }

    private WeakReference<Object> viewObject;

    private AndroidView(Object object){
        this.viewObject = new WeakReference<Object>(object);
    }

    public void startActivity(Class<? extends Activity> clazz){

    }

}
