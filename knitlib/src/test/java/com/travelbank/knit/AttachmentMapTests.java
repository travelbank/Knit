package com.travelbank.knit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 5/14/18.
 */

public class AttachmentMapTests {

    AttachmentMap attachmentMap;

    @Mock
    Object viewMock1;

    @Mock
    Object viewMock2;

    @Mock
    Object attachment1;

    @Mock
    Object attachment2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        attachmentMap = new AttachmentMap();
        attachmentMap
                .attach()
                .toView(viewMock1)
                .add(attachment1)
                .add(attachment2)
                .done();
    }

    @Test
    public void addAttachmentsToViewTest(){
        assertEquals(viewMock1,attachmentMap.getViewForAttachment(attachment1));
        assertEquals(viewMock1,attachmentMap.getViewForAttachment(attachment2));
    }

    @Test
    public void removeAttachmentFromViewTest(){
        attachmentMap.removeAttachmentFromView(viewMock1,attachment1);
        assertNull(attachmentMap.getViewForAttachment(attachment1));
    }

    @Test
    public void releaseAttachmentsFromViewTest(){
        attachmentMap.releaseAttachments(viewMock1);

        assertNull(attachmentMap.getViewForAttachment(attachment1));
        assertNull(attachmentMap.getViewForAttachment(attachment2));
    }

    @Test
    public void multipleViewSupportTest(){
        attachmentMap.removeAttachmentFromView(viewMock1,attachment2);
        attachmentMap
                .attach()
                .toView(viewMock2)
                .add(attachment2)
                .done();

        assertEquals(viewMock1,attachmentMap.getViewForAttachment(attachment1));
        assertEquals(viewMock2,attachmentMap.getViewForAttachment(attachment2));
    }

}
