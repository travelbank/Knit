package com.omerozer.knit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/13/18.
 */

public class KnitNavigator {

    private KnitInterface knitInterface;

    public KnitNavigator(KnitInterface knitInterface) {
        this.knitInterface = knitInterface;
    }

    public ActivityNavigator toActivity() {
        return new ActivityNavigator();
    }

    public abstract class Navigator {
        public abstract Class<?> getTarget();
    }

    public class ActivityNavigator extends Navigator {

        private WeakReference<Activity> from;
        private Class<? extends Activity> target;
        private KnitMessage knitMessage;

        public ActivityNavigator from(Object viewObject){
            this.from = new WeakReference<Activity> ((Activity)viewObject);
            return this;
        }

        public ActivityNavigator target(Class<? extends Activity> target) {
            this.target = target;
            return this;
        }

        public ActivityNavigator setMessage(KnitMessage message) {
            this.knitMessage = message;
            return this;
        }

        public void go() {
            if (knitMessage != null) {
                knitInterface.getMessageTrain().putMessageForView(knitInterface.getViewToPresenterMap().getPresenterClassForView(target), knitMessage);
            }
            Intent intent = new Intent(from.get(), target);
            from.get().startActivity(intent);
        }

        @Override
        public Class<?> getTarget() {
            return target;
        }
    }


}
