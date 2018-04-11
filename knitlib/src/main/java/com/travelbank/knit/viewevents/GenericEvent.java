package com.travelbank.knit.viewevents;

/**
 * {@link ViewEventEnv} that can be tailored to fire various type of events.
 *
 * @author Omer Ozer
 */

public class GenericEvent extends ViewEventEnv {

    /**
     * Object array. Container for all params.
     */
    private Object[] params;

    public GenericEvent(String tag,Object... data) {
        this.params = data;
    }

    public GenericEvent(){}

    /**
     * Getter for {@link this#params}
     * @return
     */
    public Object[] getParams() {
        return params;
    }

    /**
     * Setter for {@link this#params}
     * @param params
     */
    public void setParams(Object... params) {
        this.params = params;
    }

    /**
     * Getter for {@link this#params}
     * @return
     */
    public Object[] getData(){
        return params;
    }

}
