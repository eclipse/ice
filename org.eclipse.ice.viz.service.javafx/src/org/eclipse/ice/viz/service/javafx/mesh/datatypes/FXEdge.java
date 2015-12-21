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
package org.eclipse.ice.viz.service.javafx.mesh.datatypes;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Edge;
import org.eclipse.ice.viz.service.modeling.EdgeComponent;

/**
 * An extension of edge that manages its vertices' states as being selected and
 * under construction.
 * 
 * @author Robert Smith
 *
 */
public class FXEdge extends Edge {

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The edge's model
	 * @param view
	 *            The edge's view
	 */
	public FXEdge(EdgeComponent model, AbstractView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractController#setProperty(java.
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

}
