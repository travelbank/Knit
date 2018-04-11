package com.travelbank.knit;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * This class holds {@link KnitMessage}s for a target View. Once message is fetched. it is removed from the train.
 *
 * @author Omer Ozer
 */

public class MessageTrain {

    /**
     * {@link Map} container for all messages. Maps view class types to the associated message to be received.
     */
    private Map<Class<?>,KnitMessage> messageMap;

    MessageTrain(){
        this.messageMap = new HashMap<>();
    }

    /**
     * Sets the {@link KnitMessage} object for a given view.
     *
     * @param view given view.
     * @param message message to be sent to that view.
     */
    public void putMessageForView(Class<?> view,KnitMessage message){
        this.messageMap.put(view,message);
    }

    /**
     * Whether or not a given view has a {@link KnitMessage} to be delivered.
     * @param view Given view.
     * @return Whether or not a given view has a {@link KnitMessage} to be delivered.
     */
    public boolean hasMessage(Class<?> view){
        return this.messageMap.containsKey(view);
    }

    /**
     * Returns {@link KnitMessage} for a given view class , removing it from {@link this#messageMap}.
     * @param view given view class.
     * @return {@link KnitMessage} that the train contains for the view.
     */
    public KnitMessage getMessageForView(Class<?> view){
        KnitMessage message = this.messageMap.get(view);
        this.messageMap.remove(view);
        return message;
    }
}
