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

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;

import javafx.scene.Group;

/**
 * A JavaFX specific extension of Vertex which associates this controller with
 * the graphical representation's JavaFX node properties, allowing the user to
 * select it through the FXCanvas.
 * 
 * @author Robert Smith
 *
 */
public class FXVertexController extends VertexController {

	/**
	 * The nullary cosntructor
	 */
	public FXVertexController() {
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
	public FXVertexController(VertexMesh model, AbstractView view) {
		super(model, view);

		// Add a reference to this controller to the view's JavaFX node
		// properties
		((Group) view.getRepresentation()).getProperties()
				.put(AbstractController.class, this);
	}

	/**
	 * Get the scale of the application the view is drawn in.
	 * 
	 * @return The conversion rate between internal units and JavaFX units
	 */
	public int getApplicationScale() {
		return ((FXVertexView) view).getApplicationScale();
	}

	/**
	 * Sets the scale of the application this vertex will be displayed in. The
	 * vertex will now be drawn with all the coordinates in the VertexMesh
	 * multiplied by the scale.
	 * 
	 * @param scale
	 *            The conversion factor between JavaFX units and the logical
	 *            units used by the application.
	 */
	public void setApplicationScale(int scale) {
		((FXVertexView) view).setApplicationScale(scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#update(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IManagedVizUpdateable,
	 * org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] types) {

		// Queue any messages from the view refresh
		updateManager.enqueue();

		// If the update came from the component, send it to the view so that it
		// can refresh.
		if (component == model) {

			// Only property or selection changes will change the view
			for (SubscriptionType type : types) {
				if (type == SubscriptionType.PROPERTY
						|| type == SubscriptionType.SELECTION) {
					view.refresh(model);
					break;
				}
			}
		}

		// Notify own listeners of the change.
		updateManager.notifyListeners(types);
		updateManager.flushQueue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a copy of the model
		FXVertexController clone = new FXVertexController();
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
		model.copy(otherObject.getModel());
		view = (AbstractView) otherObject.getView().clone();

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}

}
