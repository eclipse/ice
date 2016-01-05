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
package org.eclipse.ice.viz.service.javafx.geometry.plant;

import java.nio.channels.Pipe;

import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;
import org.eclipse.ice.viz.service.geometry.reactor.ReactorController;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;

/**
 * A controller that manages all the parts present in a Reactor Analyzer. This
 * part is not intended to be displayed in JavaFX or to represent an given part
 * of the reactor. Rather, it serves merely to collect all parts under a single
 * root node to ease managing the data structures.
 * 
 * @author Robert Smith
 *
 */
public class FXReactorAnalyzerRoot extends AbstractController
		implements WireFramePart {

	/**
	 * The default constructor
	 * 
	 * @param model
	 *            The mesh component which will contain and manage the reactor's
	 *            parts
	 * @param view
	 *            A dummy view which will be unused by the root
	 */
	public FXReactorAnalyzerRoot(AbstractMeshComponent model,
			AbstractView view) {
		super(model, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractController#addEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void addEntity(AbstractController entity) {

		// If the entity is a core channel, add it to all reactors
		if (entity instanceof Pipe) {
			if (((Pipe) entity).getPipeType() == PipeType.CORE_CHANNEL) {

				// Queue updates from adding children
				updateManager.enqueue();

				// Add the entity to this, then to all reactors
				model.addEntityByCategory(entity, "Core Channels");

				for (AbstractController reactor : model
						.getEntitiesByCategory("Reactors")) {
					reactor.addEntity(entity);
				}

				// Fire update for the added child
				UpdateableSubscriptionType[] eventTypes = new UpdateableSubscriptionType[] {
						UpdateableSubscriptionType.Child };
				updateManager.notifyListeners(eventTypes);
			}

			// If the entity is a reactor, add all core channels to it
		} else if (entity instanceof ReactorController) {

			// Queue updates from adding children
			updateManager.enqueue();

			// Add the entity to this, then put all the core channels in it
			model.addEntityByCategory(entity, "Reactors");

			for (AbstractController channel : model
					.getEntitiesByCategory("Core Channels")) {
				entity.addEntity(channel);
			}

			// Fire update for the added child
			UpdateableSubscriptionType[] eventTypes = new UpdateableSubscriptionType[] {
					UpdateableSubscriptionType.Child };
			updateManager.notifyListeners(eventTypes);
		}

		// For other types of part, add it normally
		else {
			model.addEntity(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.reactor.javafx.datatypes.WireFramePart#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {
		for (AbstractController child : model.getEntities()) {
			((WireFramePart) child).setWireFrameMode(on);
		}
	}

}
