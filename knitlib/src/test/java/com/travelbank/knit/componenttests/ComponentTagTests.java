package com.travelbank.knit.componenttests;

import com.travelbank.knit.components.ComponentTag;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by bo_om on 3/12/2018.
 */

public class ComponentTagTests {

    @Test
    public void componentIncrementTest(){
        ComponentTag componentTag1 = ComponentTag.getNewTag();
        ComponentTag componentTag2 = ComponentTag.getNewTag();
        Short tag1 = componentTag1.getTag();
        Short tag2 = componentTag2.getTag();
        tag1++;
        assertEquals(tag1 , tag2);
    }

}
