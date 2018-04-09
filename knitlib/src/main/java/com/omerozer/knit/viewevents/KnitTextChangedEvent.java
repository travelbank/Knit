package com.omerozer.knit.viewevents;

import android.text.Editable;
import android.view.View;

/**
 * {@link ViewEventEnv} for simple {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)} events.
 *
 * @see ViewEventEnv
 * @author Omer Ozer
 */

public class KnitTextChangedEvent extends ViewEventEnv {

    /**
     * @see android.text.TextWatcher#beforeTextChanged(CharSequence, int, int, int) .
     * @see android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)  .
     * @see android.text.TextWatcher#afterTextChanged(Editable)  .
     */
    public enum State {
        BEFORE,
        ON,
        AFTER;
    }

    /**
     * Key for retreiving the I value out of {@link this#dataBundle}.
     * @see android.text.TextWatcher .
     */
    private static final String I = "i";

    /**
     * Key for retreiving the I value out of {@link this#dataBundle}.
     * @see android.text.TextWatcher .
     */
    private static final String I1 = "i1";

    /**
     * Key for retreiving the I value out of {@link this#dataBundle}.
     * @see android.text.TextWatcher .
     */
    private static final String I2 = "i2";

    /**
     * State for the current {@link KnitTextChangedEvent}.
     */
    private State state;

    /**
     * @see android.text.TextWatcher .
     */
    private CharSequence charSequence;

    /**
     * @see android.text.TextWatcher#afterTextChanged(Editable)
     */
    private Editable afterEditable;

    public KnitTextChangedEvent(String tag, View view, State state, CharSequence charSequence ,Editable editable) {
        super(tag, view);
        this.state = state;
        this.afterEditable = editable;
        this.charSequence = charSequence;
    }

    public KnitTextChangedEvent() {
        super();
    }

    /**
     * Setter for {@link this#state}
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Setter for {@link this#charSequence} .
     * @see android.text.TextWatcher .
     */
    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    /**
     * Setter for {@link this#afterEditable} .
     * @see android.text.TextWatcher .
     */
    public void setAfterEditable(Editable afterEditable) {
        this.afterEditable = afterEditable;
    }

    /**
     * Getter for {@link this#charSequence}
     * @return
     */
    public CharSequence getCharSequence() {
        return charSequence;
    }

    /**
     * Getter for {@link this#afterEditable}
     * @see android.text.TextWatcher#afterTextChanged(Editable)
     */
    public Editable getAfterEditable() {
        return afterEditable;
    }

    /**
     * Getter for {@link this#state}
     * @return current state of the {@link KnitTextChangedEvent}
     */
    public State getState() {
        return state;
    }

    /**
     * Getter for i value from {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)}.
     * @return
     */
    public int getI(){
        return getDataBundle().getInt(I);
    }

    /**
     * Getter for i1 value from {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)}.
     * @return
     */
    public int getI1(){
        return getDataBundle().getInt(I1);
    }

    /**
     * Getter for i2 value from {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)}.
     * @return
     */
    public int getI2(){
        return getDataBundle().getInt(I2);
    }

    /**
     * Setter for i value from {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)}.
     * @param i int value for where the string change starts
     */
    public void setI(int i){
        getDataBundle().putInt(I,i);
    }


    /**
     * Setter for i value from {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)}.
     * @param i1 int value for how long the change is.
     */
    public void setI1(int i1){
        getDataBundle().putInt(I1,i1);
    }

    /**
     * Setter for i value from {@link android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)}.
     * @param i2 int value for where the string change ends
     */
    public void setI2(int i2){
        getDataBundle().putInt(I2,i2);
    }


}
