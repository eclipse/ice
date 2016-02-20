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
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import org.eclipse.eavp.viz.service.geometry.reactor.PipeController;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;

import javafx.scene.paint.PhongMaterial;

/**
 * A JavaFX implementation of PipeController, providing extra functionality from
 * the FXPipeView.
 * 
 * @author Robert Smith
 *
 */
public class FXPipeController extends PipeController {

	/**
	 * The nullary constructor.
	 */
	public FXPipeController() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The internal representation of the part.
	 * @param view
	 *            The graphical representation of the part.
	 */
	public FXPipeController(PipeMesh model, FXPipeView view) {
		super(model, view);
	}

	/**
	 * Set the shape's default material. The shape is not guaranteed to actually
	 * display in this material after the function returns, as other materials
	 * may be taking precedence over the default one, such as the
	 * selectedMaterial when the shape is selected.
	 * 
	 * @param material
	 */
	public void setMaterial(PhongMaterial material) {
		((FXPipeView) view).setMaterial(material);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Create a new shape from clones of the model and view
		FXPipeController clone = new FXPipeController();

		// Copy any other data into the clone
		clone.copy(this);

		return clone;
	}

}
