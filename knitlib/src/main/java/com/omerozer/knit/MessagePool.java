package com.omerozer.knit;

/**
 * {@link MemoryPool} for messages that get sent from one {@link KnitPresenter} to another.
 * @see MemoryPool
 *
 * @author Omer Ozer
 */

public class MessagePool extends MemoryPool<KnitMessage> {

    /**
     * Creates new instance of {@link KnitMessage}
     * @return new {@link KnitMessage} instance
     */
    @Override
    protected KnitMessage createNewInstance() {
        return new KnitMessage();
    }
}
