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
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.IControllerFactory;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;

/**
 * A factory for creating JME3Shapes from MeshComponents.
 * 
 * @author r8s
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
	public AbstractController createController(AbstractMeshComponent model) {

		// If the model is not a shapecomponent, fail silently
		if (model instanceof ShapeComponent) {

			// Create a JME3 view
			AbstractView view = new JME3ShapeView((ShapeComponent) model);

			// Create a JME3 controller
			return new JME3Shape((ShapeComponent) model, view);

		} else {
			return null;
		}

	}

}
