/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.canvas;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;

/**
 * An AttachmentManager for FXAttachments.
 * 
 * @author Robert Smith
 *
 */
public class FXAttachmentManager extends BasicAttachmentManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.canvas.AbstractAttachmentManager#
	 * allocate()
	 */
	@Override
	public IAttachment allocate() {
		return new FXAttachment(this);
	}

}
