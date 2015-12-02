/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.mesh.datastructures;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Face;
import org.eclipse.ice.viz.service.modeling.FaceComponent;

/**
 * An extension of Face for JavaFX that manages its edges' status as under
 * construction and selected.
 * 
 * @author Robert Smith
 *
 */
public class FXFace extends Face {

	/**
	 * The nullary constructor.
	 */
	public FXFace() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The part's internal model
	 * @param view
	 *            The part's graphical view
	 */
	public FXFace(FaceComponent model, AbstractView view) {
		super(model, view);
	}

	@Override
	public void setProperty(String property, String value) {

		// If the Edge's constructing or selected properties are being changed,
		// propogate that change to its vertices
		if ("Constructing".equals(property) || "Selected".equals(property)) {

			// Lock notifications from changing own edges
			notifyLock.set(true);
			for (AbstractController vertex : model
					.getEntitiesByCategory("Edges")) {
				vertex.setProperty(property, value);
			}
			notifyLock.set(false);
		}

		super.setProperty(property, value);
	}
}
