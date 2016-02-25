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
 * A controller for an Edge part which maintains a list of polygons it is an
 * edge for.
 * 
 * @author Robert Smith
 *
 */
public class FaceEdgeController extends EdgeController {

	/**
	 * The default constructor
	 */
	public FaceEdgeController(FaceEdgeMesh model, AbstractView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Clone the model and view
		FaceEdgeMesh modelClone = (FaceEdgeMesh) model.clone();
		AbstractView viewClone = (AbstractView) view.clone();

		// Create a new controller for the clones and return it
		FaceEdgeController clone = new FaceEdgeController(modelClone,
				viewClone);
		return clone;
	}
}
