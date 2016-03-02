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

import org.eclipse.eavp.viz.service.mesh.datastructures.MeshEditorMeshProperty;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IMeshProperty;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;

/**
 * An extension of edge that manages its vertices' states as being selected and
 * under construction.
 * 
 * @author Robert Smith
 *
 */
public class FXEdgeController extends EdgeController {

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The edge's model
	 * @param view
	 *            The edge's view
	 */
	public FXEdgeController(EdgeMesh model, BasicView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractController#setProperty(
	 * java. lang.String, java.lang.String)
	 */
	@Override
	public void setProperty(IMeshProperty property, String value) {

		// If the Edge's constructing or selected properties are being changed,
		// propagate that change to its vertices
		if (MeshEditorMeshProperty.UNDER_CONSTRUCTION.equals(property)
				|| MeshProperty.SELECTED.equals(property)) {

			// Lock notifications from changing own vertices
			updateManager.enqueue();
			for (IController vertex : model
					.getEntitiesFromCategory(MeshCategory.VERTICES)) {
				vertex.setProperty(property, value);
			}
		}

		super.setProperty(property, value);

		// Empty the queue
		updateManager.flushQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Clone the model and view
		EdgeMesh modelClone = (EdgeMesh) model.clone();
		BasicView viewClone = (BasicView) view.clone();

		// Create a new controller for the clones and return it
		FXEdgeController clone = new FXEdgeController(modelClone, viewClone);
		return clone;
	}
}
