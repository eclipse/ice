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
package org.eclipse.eavp.viz.service.modeling;

/**
 * A controller for a Face part.
 * 
 * @author Robert Smith
 *
 */
public class FaceController extends BasicController {

	/**
	 * The nullary constructor.
	 */
	public FaceController() {

	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model representing this Face internally
	 * @param view
	 *            The view representing this Face in the graphics engine
	 */
	public FaceController(FaceMesh model, BasicView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a copy of the model
		FaceController clone = new FaceController();
		clone.copy(this);

		// Refresh the view to be in sync with the model
		clone.refresh();

		return clone;
	}

}
