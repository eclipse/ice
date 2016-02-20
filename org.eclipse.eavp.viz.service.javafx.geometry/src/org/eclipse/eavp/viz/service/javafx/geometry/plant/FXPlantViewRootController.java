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
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.IWireFramePart;

/**
 * A controller that manages all the parts present in a Reactor Analyzer. This
 * part is not intended to be displayed in JavaFX or to represent an given part
 * of the reactor. Rather, it serves merely to collect all parts under a single
 * root node to ease managing the data structures.
 * 
 * @author Robert Smith
 *
 */
public class FXPlantViewRootController extends AbstractController
		implements IWireFramePart {

	/**
	 * Whether or not the scene is in wireframe mode
	 */
	boolean wireframe;

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
	public FXPlantViewRootController(AbstractMesh model, AbstractView view) {
		super(model, view);
		wireframe = false;

		// Identify this object as the root of the tree
		model.setProperty("Root", "True");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractController#addEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void addEntity(AbstractController entity) {

		// If the entity is a core channel, add it to all reactors
		if ("True".equals(entity.getProperty("Core Channel"))) {

			// Queue updates from adding children
			updateManager.enqueue();

			// Add the entity to this, then to all reactors
			model.addEntityByCategory(entity, "Core Channels");

			for (AbstractController reactor : model
					.getEntitiesByCategory("Reactors")) {
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
		((IWireFramePart) entity).setWireFrameMode(wireframe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController entity,
			String category) {

		// If the entity is a reactor, add all core channels to it
		if ("Reactors".equals(category)) {

			// Queue updates from adding children
			updateManager.enqueue();

			// Add the entity to this, then put all the core channels in it
			model.addEntityByCategory(entity, "Reactors");

			for (AbstractController channel : model
					.getEntitiesByCategory("Core Channels")) {
				entity.addEntity(channel);
			}

			// Fire update for the added child
			SubscriptionType[] eventTypes = new SubscriptionType[] {
					SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}

		// Otherwise, add it normally
		else {
			model.addEntityByCategory(entity, category);
		}

		// Set the entity to the correct wireframe mode
		((IWireFramePart) entity).setWireFrameMode(wireframe);
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
		for (AbstractController child : model.getEntities()) {
			((IWireFramePart) child).setWireFrameMode(on);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
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
