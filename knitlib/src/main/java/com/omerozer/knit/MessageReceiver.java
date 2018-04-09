package com.omerozer.knit;

/**
 *
 * Knit components that receive messages in the form of {@link KnitMessage} implement this.
 * Used when a presenter passes objects to another via {@link KnitNavigator}.
 *
 * @see KnitPresenter
 * @author Omer Ozer
 */

public interface MessageReceiver {

    /**
     * Callback that receives the sent {@link KnitMessage}.
     * @param message message that is sent.
     */
    void receiveMessage(KnitMessage message);
}
