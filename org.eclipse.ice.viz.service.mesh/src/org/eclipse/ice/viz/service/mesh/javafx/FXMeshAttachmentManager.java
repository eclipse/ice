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
package org.eclipse.ice.viz.service.mesh.javafx;

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
public class FXMeshAttachmentManager implements IAttachmentManager {

	/** The active list of attachments. */
	private List<FXMeshAttachment> active;

	/** The list of attachments queued for removal. */
	private List<FXMeshAttachment> removalQueue;

	/**
	 * @see IAttachmentManager#allocate()
	 */
	@Override
	public IAttachment allocate() {
		if (active == null) {
			active = new ArrayList<>();
		}

		FXMeshAttachment attach = new FXMeshAttachment(this);
		active.add(attach);

		return attach;
	}

	/**
	 * @see IAttachmentManager#destroy(IAttachment)
	 */
	@Override
	public void destroy(IAttachment attach) {
		if (!(attach instanceof FXMeshAttachment)) {
			return;
		}

		if (removalQueue == null) {
			removalQueue = new ArrayList<>();
		}

		active.remove(attach);
		removalQueue.add((FXMeshAttachment) attach);
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

		for (FXMeshAttachment attachment : removalQueue) {
			attachment.detach(attachment.getOwner());
		}

		removalQueue.clear();
	}

	/**
	 * @see IAttachmentManager#update()
	 */
	@Override
	public void update() {
		processDeletions();

	}

	/**
	 * Getter for the list of active attachments.
	 * 
	 * @return All attachments currently managed by this object which are not
	 *         scheduled for deletion.
	 */
	public List<FXMeshAttachment> getAttachments() {
		return active;
	}

}
