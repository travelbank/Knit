package com.travelbank.knit.viewevents;

import android.support.design.widget.TabLayout;

import java.lang.ref.WeakReference;

/**
 *
 * Tab selection events from {@link android.support.design.widget.TabLayout}.
 *

 * @author Omer Ozer
 */

public class TabSelectedEvent extends ViewEventEnv {

    /**
     *
     * Type of callbacks fired by {@link TabLayout.OnTabSelectedListener}.
     *
     * @see {@link TabLayout.OnTabSelectedListener#onTabSelected(TabLayout.Tab)}
     * @see {@link TabLayout.OnTabSelectedListener#onTabReselected(TabLayout.Tab)}
     * @see {@link TabLayout.OnTabSelectedListener#onTabUnselected(TabLayout.Tab)}
     */
    public enum State{
        SELECTED,
        RESELECTED,
        UNSELECTED;
    }

    /**
     * Tab that is selected.
     */
    private WeakReference<TabLayout.Tab> tab;

    /**
     * Type of callback state
     * @see State
     */

    private State state;


    public TabSelectedEvent(){}


    /**
     * Setter for {@link this#tab}.
     * @param tab {@link TabLayout.Tab} to be set as {@link this#tab}.
     */
    public void setTab(TabLayout.Tab tab){
        this.tab = new WeakReference<TabLayout.Tab>(tab);
    }

    /**
     * Getter for {@link this#tab}.
     * @return {@link TabLayout.Tab} that was selected.
     */
    public TabLayout.Tab getTab(){
        return tab.get();
    }


    /**
     * Setter for {@link this#state}.
     * @param state {@link State} that is being set as {@link this#state}.
     */
    public void setState(State state){
        this.state = state;
    }

    /**
     * Getter for {@link this#state}
     * @return {@link State} that was set.
     */
    public State getState(){
        return this.state;
    }

}
