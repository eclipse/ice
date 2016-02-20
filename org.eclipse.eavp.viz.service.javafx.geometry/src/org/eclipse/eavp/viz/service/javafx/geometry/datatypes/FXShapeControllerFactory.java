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
package org.eclipse.eavp.viz.service.javafx.geometry.datatypes;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractControllerFactory;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.eclipse.eavp.viz.service.modeling.TubeMesh;

/**
 * A factory for creating instances of Shape with FXShapeViews for use with
 * JavaFX.
 * 
 * @author Robert Smith
 *
 */
public class FXShapeControllerFactory extends AbstractControllerFactory {

	/**
	 * The default cosntructor.
	 */
	public FXShapeControllerFactory() {
		super();

		// Set the ShapeMesh provider
		typeMap.put(ShapeMesh.class, new IControllerProvider() {
			@Override
			public AbstractController createController(AbstractMesh model) {

				// Create an FXShapeView for the model, then wrap them both in a
				// shape controller
				FXShapeView view = new FXShapeView((ShapeMesh) model);
				return new FXShapeController((ShapeMesh) model, view);
			}
		});

		// Set the TubeMesh provider
		typeMap.put(TubeMesh.class, new IControllerProvider() {
			@Override
			public AbstractController createController(AbstractMesh model) {

				// Create an FXShapeView for the model, then wrap them both in a
				// shape controller
				FXShapeView view = new FXShapeView((TubeMesh) model);
				return new FXShapeController((ShapeMesh) model, view);
			}
		});
	}

}
