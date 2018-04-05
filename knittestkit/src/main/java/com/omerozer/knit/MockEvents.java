package com.omerozer.knit;

import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEventPool;
import com.omerozer.knit.viewevents.KnitOnTextChangedEventPool;

/**
 * Created by omerozer on 2/20/18.
 */

public final class MockEvents {

    private static final KnitOnClickEventPool onClickEventPool = new KnitOnClickEventPool();
    private static final KnitOnTextChangedEventPool onTextChangedEventPool = new KnitOnTextChangedEventPool();
    private static final KnitOnFocusChangedEventPool onFocusChangedEventPool = new KnitOnFocusChangedEventPool();

    public static void fireMockClickEvent(String tag,KnitPresenterTest presenterTest){
        KnitOnClickEvent event = onClickEventPool.getObject();
        event.setTag(tag);
        presenterTest.getInternalObject().handle(onClickEventPool,event,NullValues.NULL_MODEL.get());
    }

}
