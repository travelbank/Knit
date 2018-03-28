package com.omerozer.knit;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.omerozer.knit.viewevents.GenericEvent;
import com.omerozer.knit.viewevents.GenericEventPool;
import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEvent;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEventPool;
import com.omerozer.knit.viewevents.KnitOnRefreshEvent;
import com.omerozer.knit.viewevents.KnitOnSwitchToggleEvent;
import com.omerozer.knit.viewevents.KnitOnSwitchToggleEventPool;
import com.omerozer.knit.viewevents.KnitOnTextChangedEventPool;
import com.omerozer.knit.viewevents.KnitSwipeRefreshLayoutEventPool;
import com.omerozer.knit.viewevents.KnitTextChangedEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by omerozer on 3/16/18.
 */

public class ViewEvents {

    private static Map<View,Set<Object>> viewToListenersMap = new LinkedHashMap<>();

    private KnitOnClickEventPool onClickEventPool;
    private KnitOnTextChangedEventPool onTextChangedEventPool;
    private KnitOnFocusChangedEventPool onFocusChangedEventPool;
    private KnitSwipeRefreshLayoutEventPool onSwipeRefreshEventPool;
    private KnitOnSwitchToggleEventPool onSwitchToggleEventPool;
    private GenericEventPool genericEventPool;

    private Knit knit;

    public ViewEvents(Knit knit){
        this.knit = knit;
        this.onClickEventPool = new KnitOnClickEventPool();
        this.onTextChangedEventPool = new KnitOnTextChangedEventPool();
        this.onFocusChangedEventPool = new KnitOnFocusChangedEventPool();
        this.onSwipeRefreshEventPool = new KnitSwipeRefreshLayoutEventPool();
        this.onSwitchToggleEventPool = new KnitOnSwitchToggleEventPool();
        this.genericEventPool = new GenericEventPool();
    }

    public void onClick(final String tag, final Object carrierObject, View view) {
        view.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                KnitOnClickEvent event = onClickEventPool.getObject();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knit.findPresenterForView(carrierObject).handle(onClickEventPool, event, knit.getModelManager());
            }
        });
    }

    public void onBeforeTextChanged(final String tag, final Object carrierObject,
            final EditText view) {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getObject();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.BEFORE);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knit.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knit.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        view.addTextChangedListener(watcher);
    }

    public void onTextChanged(final String tag, final Object carrierObject,
            final EditText view) {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getObject();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.ON);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knit.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knit.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        view.addTextChangedListener(watcher);
    }

    public void onAfterTextChanged(final String tag, final Object carrierObject,
            final EditText view) {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getObject();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.AFTER);
                event.setAfterEditable(editable);
                knit.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knit.getModelManager());
                view.addTextChangedListener(this);
            }
        };
        view.addTextChangedListener(watcher);
    }

    public void onFocusChanged(final String tag, final Object carrierObject,
            final View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                KnitOnFocusChangedEvent event = onFocusChangedEventPool.getObject();
                event.setTag(tag);
                event.setFocus(b);
                knit.findPresenterForView(carrierObject).handle(onFocusChangedEventPool, event, knit.getModelManager());
            }
        });
    }

    public void onSwipeRefresh(final String tag, final Object carrierObject,final SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KnitOnRefreshEvent event = onSwipeRefreshEventPool.getObject();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knit.findPresenterForView(carrierObject).handle(onSwipeRefreshEventPool, event, knit.getModelManager());
            }
        });
    }

    public void onSwitchToggled(final String tag, final Object carrierObject,final Switch view){
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KnitOnSwitchToggleEvent event = onSwitchToggleEventPool.getObject();
                event.setTag(tag);
                event.setToggle(isChecked);
                knit.findPresenterForView(carrierObject).handle(onSwitchToggleEventPool,event,knit.getModelManager());
            }
        });
    }

    public <T> void fireGenericEvent(String tag,Object carrierObject,Object... params){
        GenericEvent genericEvent = genericEventPool.getObject();
        genericEvent.setTag(tag);
        genericEvent.setParams(params);
        knit.findPresenterForView(carrierObject).handle(genericEventPool, genericEvent, knit.getModelManager());
    }


    public void onViewResult(Object carrierObject,int requestCode,int resultCode,Intent data){
        knit.findPresenterForView(carrierObject).onViewResult(requestCode,resultCode,data);
    }


}
