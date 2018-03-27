package com.omerozer.sample.presenters;

import android.util.Log;

import com.omerozer.knit.KnitMessage;
import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.ModelEvent;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.sample.datatype.StringWrapper;
import com.omerozer.sample.views.SecondActivity;
import com.omerozer.sample.views.SecondActivityContract;

import java.util.List;

/**
 * Created by omerozer on 2/6/18.
 */

@Presenter(value = SecondActivity.class,needs = "DENTS")
public class SecondPresenter extends KnitPresenter<SecondActivityContract> {

    private String string;

    @Override
    public void onViewApplied(Object viewObject){
        super.onViewApplied(viewObject);
//        request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
//        request("Ttest", KnitSchedulers.IO,KnitSchedulers.MAIN);
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
        getContract().recMes(string);
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

    @ModelEvent("umbrella")
    void updateData2(KnitResponse<String> data){
        getContract().recMes(data.getBody());
    }

    @ModelEvent("Ttest")
    void updateDatat2(KnitResponse<List<StringWrapper>> data){
        getContract().recMes(data.getBody().get(0).string);
    }
}
