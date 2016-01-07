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
package org.eclipse.ice.viz.service.jme3.geometry;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.IControllerFactory;
import org.eclipse.ice.viz.service.modeling.ShapeMesh;

/**
 * A factory for creating JME3Shapes from MeshComponents.
 * 
 * @author Robert Smith
 *
 */
public class JME3ControllerFactory implements IControllerFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.IControllerFactory#createController(
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public AbstractController createController(AbstractMesh model) {

		// If the model is not a shape mesh, fail silently
		if (model instanceof ShapeMesh) {

			// Create a JME3 view
			AbstractView view = new JME3ShapeView((ShapeMesh) model);

			// Create a JME3 controller
			return new JME3Shape((ShapeMesh) model, view);

		} else {
			return null;
		}

	}

}
