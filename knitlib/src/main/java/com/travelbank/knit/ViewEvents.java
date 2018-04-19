package com.travelbank.knit;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.travelbank.knit.viewevents.AdapterItemSelectedEvent;
import com.travelbank.knit.viewevents.AdapterItemSelectedEventPool;
import com.travelbank.knit.viewevents.GenericEvent;
import com.travelbank.knit.viewevents.GenericEventPool;
import com.travelbank.knit.viewevents.KnitOnClickEvent;
import com.travelbank.knit.viewevents.KnitOnClickEventPool;
import com.travelbank.knit.viewevents.KnitOnFocusChangedEvent;
import com.travelbank.knit.viewevents.KnitOnFocusChangedEventPool;
import com.travelbank.knit.viewevents.KnitOnRefreshEvent;
import com.travelbank.knit.viewevents.KnitOnSwitchToggleEvent;
import com.travelbank.knit.viewevents.KnitOnSwitchToggleEventPool;
import com.travelbank.knit.viewevents.KnitOnTextChangedEventPool;
import com.travelbank.knit.viewevents.KnitSwipeRefreshLayoutEventPool;
import com.travelbank.knit.viewevents.KnitTextChangedEvent;
import com.travelbank.knit.viewevents.TabSelectedEvent;
import com.travelbank.knit.viewevents.TabSelectionEventPool;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A hub for all view events. All view events are fired through this class.
 * Instance is contained inside {@link Knit}. Often accessed inside classes annotated with {@link
 * KnitView},
 * Events fired will be received by the {@link KnitPresenter} inside the method that is tagged with
 * the associated tag of the {@link com.travelbank.knit.viewevents.ViewEventEnv}
 * that is being fired.
 *
 * @author Omer Ozer
 */

public class ViewEvents {

    private static Map<View, Set<Object>> viewToListenersMap = new LinkedHashMap<>();

    private KnitOnClickEventPool onClickEventPool;
    private KnitOnTextChangedEventPool onTextChangedEventPool;
    private KnitOnFocusChangedEventPool onFocusChangedEventPool;
    private KnitSwipeRefreshLayoutEventPool onSwipeRefreshEventPool;
    private KnitOnSwitchToggleEventPool onSwitchToggleEventPool;
    private TabSelectionEventPool onTabSelectedEventPool;
    private AdapterItemSelectedEventPool onAdapterItemSelectedEventPool;
    private GenericEventPool genericEventPool;

    private Knit knit;

    public ViewEvents(Knit knit) {
        this.knit = knit;
    }

    private KnitOnClickEventPool getOnClickEventPool() {
        if (onClickEventPool == null) {
            this.onClickEventPool = new KnitOnClickEventPool();
        }
        return onClickEventPool;
    }

    private KnitOnTextChangedEventPool getOnTextChangedEventPool() {
        if (onTextChangedEventPool == null) {
            this.onTextChangedEventPool = new KnitOnTextChangedEventPool();
        }
        return onTextChangedEventPool;
    }

    private KnitOnFocusChangedEventPool getOnFocusChangedEventPool() {
        if (onFocusChangedEventPool == null) {
            this.onFocusChangedEventPool = new KnitOnFocusChangedEventPool();
        }

        return onFocusChangedEventPool;
    }

    private KnitSwipeRefreshLayoutEventPool getOnSwipeRefreshEventPool() {
        if (onSwipeRefreshEventPool == null) {
            this.onSwipeRefreshEventPool = new KnitSwipeRefreshLayoutEventPool();
        }
        return onSwipeRefreshEventPool;
    }

    private KnitOnSwitchToggleEventPool getOnSwitchToggleEventPool() {
        if (onSwitchToggleEventPool == null) {
            this.onSwitchToggleEventPool = new KnitOnSwitchToggleEventPool();
        }
        return onSwitchToggleEventPool;
    }

    private TabSelectionEventPool getOnTabSelectedEventPool() {
        if (onTabSelectedEventPool == null) {
            onTabSelectedEventPool = new TabSelectionEventPool();
        }
        return onTabSelectedEventPool;
    }

    private AdapterItemSelectedEventPool getOnAdapterItemSelectedEventPool() {
        if (onAdapterItemSelectedEventPool == null) {
            onAdapterItemSelectedEventPool = new AdapterItemSelectedEventPool();
        }
        return onAdapterItemSelectedEventPool;
    }

    private GenericEventPool getGenericEventPool() {
        if (genericEventPool == null) {
            this.genericEventPool = new GenericEventPool();
        }
        return genericEventPool;
    }

    public void onClick(final String tag, final Object carrierObject, View view) {
        view.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                KnitOnClickEvent event = getOnClickEventPool().getObject();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knit.findPresenterForView(carrierObject).get().handle(getOnClickEventPool(), event,
                        knit.getModelManager());
            }
        });
    }

    public void onBeforeTextChanged(final String tag, final Object carrierObject,
            final EditText view) {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = getOnTextChangedEventPool().getObject();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.BEFORE);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knit.findPresenterForView(carrierObject).get().handle(getOnTextChangedEventPool(),
                        event,
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
                KnitTextChangedEvent event = getOnTextChangedEventPool().getObject();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.ON);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knit.findPresenterForView(carrierObject).get().handle(getOnTextChangedEventPool(),
                        event,
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
                KnitTextChangedEvent event = getOnTextChangedEventPool().getObject();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.AFTER);
                event.setAfterEditable(editable);
                knit.findPresenterForView(carrierObject).get().handle(getOnTextChangedEventPool(),
                        event,
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
                KnitOnFocusChangedEvent event = getOnFocusChangedEventPool().getObject();
                event.setTag(tag);
                event.setFocus(b);
                knit.findPresenterForView(carrierObject).get().handle(getOnFocusChangedEventPool(),
                        event, knit.getModelManager());
            }
        });
    }

    public void onSwipeRefresh(final String tag, final Object carrierObject,
            final SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KnitOnRefreshEvent event = getOnSwipeRefreshEventPool().getObject();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knit.findPresenterForView(carrierObject).get().handle(getOnSwipeRefreshEventPool(),
                        event, knit.getModelManager());
            }
        });
    }

    public void onSwitchToggled(final String tag, final Object carrierObject, final Switch view) {
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KnitOnSwitchToggleEvent event = getOnSwitchToggleEventPool().getObject();
                event.setTag(tag);
                event.setToggle(isChecked);
                knit.findPresenterForView(carrierObject).get().handle(getOnSwitchToggleEventPool(),
                        event, knit.getModelManager());
            }
        });
    }

    public void onTabSelected(final String tag, final Object carrierObject, final TabLayout view) {
        view.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabSelectedEvent event = getOnTabSelectedEventPool().getObject();
                event.setTag(tag);
                event.setTab(tab);
                event.setState(TabSelectedEvent.State.SELECTED);
                knit.findPresenterForView(carrierObject).get().handle(getOnTabSelectedEventPool(),
                        event, knit.getModelManager());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onTabReselected(final String tag, final Object carrierObject,
            final TabLayout view) {
        view.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabSelectedEvent event = getOnTabSelectedEventPool().getObject();
                event.setTag(tag);
                event.setTab(tab);
                event.setState(TabSelectedEvent.State.RESELECTED);
                knit.findPresenterForView(carrierObject).get().handle(getOnTabSelectedEventPool(),
                        event, knit.getModelManager());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onTabUnselected(final String tag, final Object carrierObject,
            final TabLayout view) {
        view.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabSelectedEvent event = getOnTabSelectedEventPool().getObject();
                event.setTag(tag);
                event.setTab(tab);
                event.setState(TabSelectedEvent.State.UNSELECTED);
                knit.findPresenterForView(carrierObject).get().handle(getOnTabSelectedEventPool(),
                        event, knit.getModelManager());
            }
        });
    }

    public void onAdapterItemSelected(final String tag, final Object carrierObject,
            final AdapterView view) {
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterItemSelectedEvent event = getOnAdapterItemSelectedEventPool().getObject();
                event.setTag(tag);
                event.setIndex(position);
                knit.findPresenterForView(carrierObject).get().handle(getOnAdapterItemSelectedEventPool(),
                        event, knit.getModelManager());
            }
        });

    }


    public <T> void fireGenericEvent(String tag, Object carrierObject, Object... params) {
        GenericEvent genericEvent = getGenericEventPool().getObject();
        genericEvent.setTag(tag);
        genericEvent.setParams(params);
        knit.findPresenterForView(carrierObject).get().handle(getGenericEventPool(), genericEvent,
                knit.getModelManager());
    }


    public void onViewResult(Object carrierObject, int requestCode, int resultCode, Intent data) {
        knit.findPresenterForView(carrierObject).get().onViewResult(requestCode, resultCode, data);
    }


}
