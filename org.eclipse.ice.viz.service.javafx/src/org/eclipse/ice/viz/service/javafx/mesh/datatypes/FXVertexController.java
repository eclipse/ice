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
package org.eclipse.ice.viz.service.javafx.mesh.datatypes;

import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.VertexController;
import org.eclipse.ice.viz.service.modeling.VertexMesh;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractController#update(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IManagedVizUpdateable,
	 * org.eclipse.ice.viz.service.datastructures.VizObject.
	 * UpdateableSubscriptionType[])
	 */
	@Override
	public void update(IManagedVizUpdateable component,
			UpdateableSubscriptionType[] types) {

		// Queue any messages from the view refresh
		updateManager.enqueue();

		// If the update came from the component, send it to the view so that it
		// can refresh.
		if (component == model) {

			// Only property or selection changes will change the view
			for (UpdateableSubscriptionType type : types) {
				if (type == UpdateableSubscriptionType.Property
						|| type == UpdateableSubscriptionType.Selection) {
					view.refresh(model);
					break;
				}
			}
		}

		// Notify own listeners of the change.
		updateManager.notifyListeners(types);
		updateManager.flushQueue();

	}

}
