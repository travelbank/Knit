package com.omerozer.knit;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Carrier object for inter-presenter objects.
 *
 * @author Omer Ozer
 */

public class KnitMessage implements Poolable {

    /**
     * {@link Map} that contains all objects inside the message.
     */
    private Map<String,Object> data;

    KnitMessage(){
        this.data = new HashMap<>();
    }

    /**
     * Put data inside {@link this#data} via stub pattern.
     * @param key key needed to access the given data.
     * @param value value of the given data.
     * @return itself for stubbing.
     */
    public KnitMessage putData(String key, Object value){
        this.data.put(key,value);
        return this;
    }

    /**
     * Access the value of the data for the given key. Throws exception if it doesn't exists.
     *
     * @param key Key for the value inside {@link this#data}
     * @param <T> Type of the value.
     * @return Instance of the value.
     */
    public<T> T getData(String key){
        if(!data.containsKey(key)){
            throw new RuntimeException("Knit: Data('" + key + "' ) could not be found in the message");
        }
        return (T)data.get(key);
    }

    /**
     * Clears {@link this#data}. Used inside {@link this#recycle()}.
     */
    private void clear(){
        this.data.clear();
    }


    /**
     * @see Poolable
     */
    @Override
    public void recycle() {
        clear();
    }
}
