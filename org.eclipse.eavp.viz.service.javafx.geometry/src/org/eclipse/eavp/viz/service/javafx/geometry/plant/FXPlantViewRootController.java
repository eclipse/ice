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
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMeshCategory;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMeshProperty;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IMeshCategory;
import org.eclipse.eavp.viz.service.modeling.IWireframeController;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;

/**
 * A controller that manages all the parts present in a Reactor Analyzer. This
 * part is not intended to be displayed in JavaFX or to represent an given part
 * of the reactor. Rather, it serves merely to collect all parts under a single
 * root node to ease managing the data structures.
 * 
 * @author Robert Smith
 *
 */
public class FXPlantViewRootController extends BasicController
		implements IWireframeController {

	/**
	 * Whether or not the scene is in wireframe mode
	 */
	private boolean wireframe;

	/**
	 * The nullary constructor
	 */
	public FXPlantViewRootController() {
		super();
		wireframe = false;
	}

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The mesh component which will contain and manage the reactor's
	 *            parts
	 * @param view
	 *            A dummy view which will be unused by the root
	 */
	public FXPlantViewRootController(BasicMesh model, BasicView view) {
		super(model, view);
		wireframe = false;

		// Identify this object as the root of the tree
		model.setProperty(MeshProperty.ROOT, "True");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#addEntity(org.
	 * eclipse.ice.viz.service.modeling.IController)
	 */
	@Override
	public void addEntity(IController entity) {

		// If the entity is a core channel, add it to all reactors
		if ("True"
				.equals(entity.getProperty(ReactorMeshProperty.CORE_CHANNEL))) {

			// Queue updates from adding children
			updateManager.enqueue();

			// Add the entity to this, then to all reactors
			model.addEntityToCategory(entity,
					ReactorMeshCategory.CORE_CHANNELS);

			for (IController reactor : model
					.getEntitiesFromCategory(ReactorMeshCategory.REACTORS)) {
				reactor.addEntity(entity);
			}

			// Fire update for the added child
			SubscriptionType[] eventTypes = new SubscriptionType[] {
					SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}

		// For other types of part, add it normally
		else {
			model.addEntity(entity);
		}

		// Set the entity to the correct wireframe mode
		((IWireframeController) entity).setWireFrameMode(wireframe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling. IController,
	 * java.lang.String)
	 */
	@Override
	public void addEntityToCategory(IController entity,
			IMeshCategory category) {

		// If the entity is a reactor, add all core channels to it
		if (ReactorMeshCategory.REACTORS.equals(category)) {

			// Queue updates from adding children
			updateManager.enqueue();

			// Add the entity to this, then put all the core channels in it
			model.addEntityToCategory(entity, ReactorMeshCategory.REACTORS);

			for (IController channel : model
					.getEntitiesFromCategory(ReactorMeshCategory.CORE_CHANNELS)) {
				entity.addEntityToCategory(channel,
						ReactorMeshCategory.CORE_CHANNELS);
			}

			// Fire update for the added child
			SubscriptionType[] eventTypes = new SubscriptionType[] {
					SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}

		// Otherwise, add it normally
		else {
			model.addEntityToCategory(entity, category);
		}

		// Set the entity to the correct wireframe mode
		((IWireframeController) entity).setWireFrameMode(wireframe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.reactor.javafx.datatypes.WireFramePart#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {

		// Save the wireframe state
		wireframe = on;

		// Set all the children to the proper wireframe mode
		for (IController child : model.getEntities()) {
			((IWireframeController) child).setWireFrameMode(on);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#clone()
	 */
	@Override
	public Object clone() {

		// Create a new shape from clones of the model and view
		FXPlantViewRootController clone = new FXPlantViewRootController();

		// Copy any other data into the clone
		clone.copy(this);

		return clone;
	}

}
