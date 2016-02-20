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
package org.eclipse.eavp.viz.service.javafx.canvas;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.viewer.IAttachmentManager;

/**
 * <p>
 * Abstract implementation of an IAttachmentManager.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
abstract public class AbstractAttachmentManager implements IAttachmentManager {

	/** The active list of attachments. */
	protected List<IAttachment> active;

	/** The list of attachments queued for removal. */
	private List<IAttachment> removalQueue;

	/**
	 * @see IAttachmentManager#allocate()
	 */
	@Override
	public abstract IAttachment allocate();

	/**
	 * @see IAttachmentManager#destroy(IAttachment)
	 */
	@Override
	public void destroy(IAttachment attach) {

		if (removalQueue == null) {
			removalQueue = new ArrayList<>();
		}

		active.remove(attach);
		removalQueue.add(attach);
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

		for (IAttachment attachment : removalQueue) {
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
	public List<IAttachment> getAttachments() {
		return active;
	}

}
