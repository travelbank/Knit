package com.travelbank.sample.presenters;

import android.util.Log;

import com.travelbank.knit.KnitMessage;
import com.travelbank.knit.KnitPresenter;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.ModelEvent;
import com.travelbank.knit.Presenter;
import com.travelbank.knit.schedulers.KnitSchedulers;
import com.travelbank.sample.datatype.StringWrapper;
import com.travelbank.sample.views.SecondActivity;
import com.travelbank.sample.views.SecondActivityViewWrapper;

import java.util.List;

/**
 * Created by omerozer on 2/6/18.
 */

@Presenter(value = SecondActivity.class)
public class SecondPresenter extends KnitPresenter<SecondActivityViewWrapper,SecondPresenterModelAccessor> {

    private String string;

    @Override
    public void onViewApplied(Object viewObject){
        super.onViewApplied(viewObject);
        //request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
        request("umbrella1", KnitSchedulers.IO,KnitSchedulers.MAIN);
        Log.d("KNIT_TEST","PRESENTER TWO VIEW APPLIED");

    }

    @Override
    public void onCurrentViewReleased() {
        super.onCurrentViewReleased();
        Log.d("KNIT_TEST","PRESENTER TWO VIEW RELEASED");
    }

    @Override
    public void receiveMessage(KnitMessage message) {
        super.receiveMessage(message);
        this.string = message.<String>getData("txt");

    }

    @Override
    public void onViewStart() {
        super.onViewStart();
        getViewWrapper().recMes(getAccessor().getTestString());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","PRESENTER TWO CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","PRESENTER TWO DESTROYED");
    }

    @ModelEvent("umbrella1")
    void updateData2(KnitResponse<String> data){
        getViewWrapper().recMes(data.getBody());
    }

    @ModelEvent("Ttest")
    void updateDatat2(KnitResponse<List<StringWrapper>> data){
        getViewWrapper().recMes(data.getBody().get(0).string);
    }
}
