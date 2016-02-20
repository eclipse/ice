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
package org.eclipse.eavp.viz.service.javafx.mesh.datatypes;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;

/**
 * An extension of edge that manages its vertices' states as being selected and
 * under construction.
 * 
 * @author Robert Smith
 *
 */
public class FXEdgeController extends EdgeController {

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The edge's model
	 * @param view
	 *            The edge's view
	 */
	public FXEdgeController(EdgeMesh model, AbstractView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractController#setProperty(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public void setProperty(String property, String value) {

		// If the Edge's constructing or selected properties are being changed,
		// propagate that change to its vertices
		if ("Constructing".equals(property) || "Selected".equals(property)) {

			// Lock notifications from changing own vertices
			updateManager.enqueue();
			for (AbstractController vertex : model
					.getEntitiesByCategory("Vertices")) {
				vertex.setProperty(property, value);
			}
		}

		super.setProperty(property, value);

		// Empty the queue
		updateManager.flushQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Clone the model and view
		EdgeMesh modelClone = (EdgeMesh) model.clone();
		AbstractView viewClone = (AbstractView) view.clone();

		// Create a new controller for the clones and return it
		FXEdgeController clone = new FXEdgeController(modelClone, viewClone);
		return clone;
	}
}
