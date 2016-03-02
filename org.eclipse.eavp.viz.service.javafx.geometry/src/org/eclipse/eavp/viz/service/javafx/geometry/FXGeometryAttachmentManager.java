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
package org.eclipse.eavp.viz.service.javafx.geometry;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;

/**
 * <p>
 * Manages FXGeometryAttachment allocations.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXGeometryAttachmentManager extends BasicAttachmentManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.model.geometry.
	 * AbstractAttachmentManager#allocate()
	 */
	@Override
	public IAttachment allocate() {
		if (active == null) {
			active = new ArrayList<>();
		}

		// Create a geometry attachment and add it to the list of active
		// attachments
		FXAttachment attach = new FXGeometryAttachment(this);
		active.add(attach);

		return attach;
	}

}
