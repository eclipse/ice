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
 * A controller for a Vertex model part.
 * 
 * @author Robert Smith
 *
 */
public class VertexController extends PointController {

	/**
	 * The nullary constructor
	 */
	public VertexController() {
		super();
	}

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public VertexController(VertexMesh model, AbstractView view) {
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
		VertexController clone = new VertexController();
		clone.copy(this);

		return clone;
	}

	/**
	 * Deep copy the given object's data into this one.
	 * 
	 * @param otherObject
	 *            The object to copy into this one.
	 */
	@Override
	public void copy(AbstractController otherObject) {

		// Create the model and give it a reference to this
		model = new VertexMesh();
		model.setController(this);

		// Copy the other object's data members
		model.copy(otherObject.model);
		view = (AbstractView) otherObject.view.clone();

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}
}
