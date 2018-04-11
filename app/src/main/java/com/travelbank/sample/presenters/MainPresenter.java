package com.travelbank.sample.presenters;

import static com.travelbank.sample.views.MainActivity.BUTTON_CLICK;

import android.util.Log;

import com.travelbank.knit.KnitPresenter;
import com.travelbank.knit.Presenter;
import com.travelbank.knit.ViewEvent;
import com.travelbank.knit.viewevents.ViewEventEnv;
import com.travelbank.sample.views.MainActivity;
import com.travelbank.sample.views.MainActivityContract;
import com.travelbank.sample.views.SecondActivity;


/**
 * Created by omerozer on 2/2/18.
 */

@Presenter(MainActivity.class)
public class MainPresenter extends KnitPresenter<MainActivityContract> {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","MAIN PRESENTER MAIN CREATED");
    }

    @Override
    public void onViewStart() {
        super.onViewStart();
        Log.d("KNIT_TEST","MAIN PRESENTER VIEW STARTED");
    }

    @Override
    public void onViewResume() {
        super.onViewResume();
        Log.d("KNIT_TEST","MAIN PRESENTER VIEW RESUMED");
    }

    @Override
    public void onViewPause() {
        super.onViewPause();
        Log.d("KNIT_TEST","MAIN PRESENTER VIEW PAUSED");
    }

    @Override
    public void onViewStop() {
        super.onViewStop();
        Log.d("KNIT_TEST","MAIN PRESENTER VIEW STOPPED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","MAIN PRESENTER MAIN DESTROYED");
    }

    @ViewEvent(BUTTON_CLICK)
    public void handle(ViewEventEnv eventEnv) {
            getNavigator()
                    .toActivity()
                    .from(getView())
                    .setMessage(newMessage().putData("txt",getContract().get()))
                    .target(SecondActivity.class)
                    .go();
    }



}
