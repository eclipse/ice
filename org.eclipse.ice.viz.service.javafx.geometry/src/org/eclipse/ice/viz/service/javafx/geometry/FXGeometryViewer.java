/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.geometry;

import org.eclipse.ice.viz.service.javafx.canvas.FXViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * An extension of FX viewer for use with the geometry editor
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXGeometryViewer extends FXViewer {

	public FXGeometryViewer(Composite parent) {
		super(parent);

		// Register the geometry attachment class with an attachment manager
		renderer.register(FXGeometryAttachment.class,
				new FXGeometryAttachmentManager());
	}
}
