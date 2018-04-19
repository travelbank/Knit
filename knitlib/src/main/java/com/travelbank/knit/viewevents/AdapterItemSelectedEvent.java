package com.travelbank.knit.viewevents;

/**
 *
 * {@link ViewEventEnv}s for {@link android.widget.AdapterView.OnItemClickListener}.
 *
 * @author Omer Ozer
 */

public class AdapterItemSelectedEvent extends ViewEventEnv {

    /**
     * {@link String} key used to extract the index from {@link this#getDataBundle()}.
     */
    private static final String INDEX_KEY = "ik";

    public AdapterItemSelectedEvent(){}

    /**
     * Setter for the index.
     * @param index index of the item selected.
     */
    public void setIndex(int index){
        getDataBundle().putInt(INDEX_KEY,index);
    }

    /**
     * Getter for the index.
     * @return index of the item selected.
     */
    public int getIndex(){
        return getDataBundle().getInt(INDEX_KEY);
    }

}
