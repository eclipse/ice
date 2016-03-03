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

import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IMesh;
import org.eclipse.eavp.viz.service.modeling.Representation;

import javafx.scene.Group;

/**
 * A class which provides and manages a simple empty node for a JavaFX part,
 * under which the face's children that have graphical representations (such as
 * edges and vertices), can be grouped.
 * 
 * @author Robert Smith
 *
 */
public class FXFaceView extends BasicView {

	/**
	 * The node which will contain the polygon's children.
	 */
	private Group node;

	/**
	 * The nullary constructor.
	 */
	public FXFaceView() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 */
	public FXFaceView(IMesh model) {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Representation<Group> getRepresentation() {
		return new Representation<Group>(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new AbstractView and make it a copy of this
		FXFaceView clone = new FXFaceView();
		clone.copy(this);

		return clone;
	}
}
