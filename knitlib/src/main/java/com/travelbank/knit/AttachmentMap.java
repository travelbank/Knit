package com.travelbank.knit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This object handles all live attachment mappings for a particular view object. {@link ViewEvents}
 * first check to see if carrier object
 * is an attachment if so, it'll find the view that the attachment is attached to and will send the
 * view events to it's {@link KnitPresenter}.
 *
 * @author Omer Ozer
 */

public class AttachmentMap {

    /**
     * Class that handles stubbing for attachment addition for a view through it's parent {@link
     * AttachmentMap}.
     */
    public class ViewAttachmentStubber {

        /**
         * Parent {@link AttachmentMap} object.
         */
        private AttachmentMap attachmentMap;

        /**
         * Current view object that's being stubbed through.
         */
        private Object viewObject;

        /**
         * {@link Set} that holds all attachments.
         */
        private Set<Object> attachments;

        public ViewAttachmentStubber(AttachmentMap attachmentMap) {
            this.attachmentMap = attachmentMap;
            this.attachments = new HashSet<>();
        }

        /**
         * Stub method that sets the view object.
         *
         * @param viewObject view object that will receive the attachments.
         * @return itself.
         */
        public ViewAttachmentStubber toView(Object viewObject) {
            this.viewObject = viewObject;
            return this;
        }

        /**
         * Stub method that sets the view object.
         *
         * @param attachment to be attached to a view.
         * @return itself.
         */
        public ViewAttachmentStubber add(Object attachment) {
            this.attachments.add(attachment);
            return this;
        }

        /**
         * Method that ends stubbing and adds it all to it's parent {@link AttachmentMap}.
         */
        public void done() {
            this.attachmentMap.addAttachmentsToView(viewObject, attachments);
        }

    }


    /**
     * {@link Map} that maps views to their associated attachments objects.
     */
    private Map<Object, Set<Object>> map;

    AttachmentMap() {
        this.map = new HashMap<>();
    }

    /**
     * Method that starts stubbing by returning the first stubber object for {@link
     * ViewAttachmentStubber}.
     *
     * @return stubber object.
     */
    public ViewAttachmentStubber attach() {
        return new ViewAttachmentStubber(this);
    }

    /**
     * Adds an attachment to a view.
     * @param view that is receiving the attachment.
     * @param attachment that is being attached to the view.
     */
    private void addAttachmentToView(Object view, Object attachment) {
        if (!map.containsKey(view)) {
            map.put(view, new HashSet<Object>());
        }
        map.get(view).add(attachment);
    }

    /**
     * Adds multiple attachments to a view.
     * @param view that is receiving the attachment.
     * @param attachments that are being attached to the view.
     */
    private void addAttachmentsToView(Object view, Set<Object> attachments) {
        if (!map.containsKey(view)) {
            map.put(view, new HashSet<Object>());
        }
        map.get(view).addAll(attachments);
    }

    /**
     * Removes an attachment from a view.
     * @param view that is releasing the attachment.
     * @param attachment that is being removed.
     */
     void removeAttachmentFromView(Object view, Object attachment) {
        if (!map.containsKey(view)) {
            return;
        }
        map.get(view).remove(attachment);
        if (map.get(view).isEmpty()) {
            map.remove(view);
        }
    }

    /**
     * Finds the view object that contains a particular attachment.
     * @param attachment to be searched.
     * @return the view that contains the given attachment.
     */
     Object getViewForAttachment(Object attachment) {
        for (Object view : map.keySet()) {
            if (map.get(view).contains(attachment)) {
                return view;
            }
        }
        return null;
    }

    /**
     * Releases all attachments of a view.
     * @param view that is releasing all attachments.
     */
    public void releaseAttachments(Object view) {
        this.map.remove(view);
    }

}
