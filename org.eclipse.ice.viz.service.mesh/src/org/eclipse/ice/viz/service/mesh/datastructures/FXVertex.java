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
import org.eclipse.ice.viz.service.modeling.Vertex;
import org.eclipse.ice.viz.service.modeling.VertexComponent;

import javafx.scene.Group;

/**
 * A JavaFX specific extension of Vertex which associates this controller with
 * the graphical representation's JavaFX node properties, allowing the user to
 * select it through the FXCanvas.
 * 
 * @author Robert Smith
 *
 */
public class FXVertex extends Vertex {

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public FXVertex(VertexComponent model, AbstractView view) {
		super(model, view);

		// Add a reference to this controller to the view's JavaFX node
		// properties
		((Group) view.getRepresentation()).getProperties()
				.put(AbstractController.class, this);
	}

}
