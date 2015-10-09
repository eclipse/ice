/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.internal.model.geometry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.geometry.scene.model.IAttachment;
import org.eclipse.ice.viz.service.geometry.viewer.IAttachmentManager;

/**
 * <p>
 * Manages FXGeometryAttachment allocations.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXGeometryAttachmentManager implements IAttachmentManager {

    /** The active list of attachments. */
    private List<FXGeometryAttachment> active;

    /** The list of attachments queued for removal. */
    private List<FXGeometryAttachment> removalQueue;

    /**
     * @see IAttachmentManager#allocate()
     */
    public IAttachment allocate() {
        if (active == null) {
            active = new ArrayList<>();
        }

        FXGeometryAttachment attach = new FXGeometryAttachment(this);
        active.add(attach);

        return attach;
    }

    /**
     * @see IAttachmentManager#destroy(IAttachment)
     */
    public void destroy(IAttachment attach) {
        if (!(attach instanceof FXGeometryAttachment)) {
            return;
        }

        if (removalQueue == null) {
            removalQueue = new ArrayList<>();
        }

        active.remove(attach);
        removalQueue.add((FXGeometryAttachment) attach);
    }

    /**
     * <p>
     * Batch deletes the attachments in the removal queue.
     * </p>
     */
    private void processDeletions() {
        if (removalQueue == null || removalQueue.isEmpty()) {
            return;
        }

        for (FXGeometryAttachment attachment : removalQueue) {
            attachment.detach(attachment.getOwner());
        }

        removalQueue.clear();
    }

    /**
     * @see IAttachmentManager#update()
     */
    public void update() {
        processDeletions();

    }

}
