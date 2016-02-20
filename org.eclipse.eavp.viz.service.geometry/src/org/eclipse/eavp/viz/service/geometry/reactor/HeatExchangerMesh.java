/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.geometry.reactor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;

/**
 * The internal data representation for a Heat Exchanger part.
 * 
 * @author Robert Smith
 *
 */
public class HeatExchangerMesh extends AbstractMesh {

	/**
	 * The nullary constructor.
	 */
	public HeatExchangerMesh() {
		super();
	}

	/**
	 * Convenience getter method for the primary pipe.
	 * 
	 * @return The Heat Exchanger's primary pipe, or null if it does not have
	 *         one.
	 */
	public PipeController getPrimaryPipe() {

		List<AbstractController> category = getEntitiesByCategory(
				"Primary Pipe");

		return !category.isEmpty() ? (PipeController) category.get(0) : null;
	}

	/**
	 * Convenience getter method for the secondary pipe.
	 * 
	 * @return The Heat Exchanger's secondary pipe, or null if it does not have
	 *         one.
	 */
	public PipeController getSecondaryPipe() {
		List<AbstractController> category = getEntitiesByCategory(
				"Secondary Pipe");

		return !category.isEmpty() ? (PipeController) category.get(0) : null;
	}

	/**
	 * Set the heat exchanger's primary pipe, removing any other primary pipe as
	 * necessary.
	 * 
	 * @param pipe
	 *            The Heat Exchanger's new primary pipe.
	 */
	public void setPrimaryPipe(PipeController pipe) {

		// Set this exchanger's transformation based on the pipe's, as the heat
		// exchanger should be drawn around the pipe
		controller.setTransformation(pipe.getTransformation());

		// Get the current primary pipe, if any
		List<AbstractController> primary = entities.get("Primary Pipe");

		// If there is already one, remove it.
		if (primary != null) {
			if (!primary.isEmpty()) {
				removeEntity(primary.get(0));
			}
		}

		// Add the pipe under the Primary Pipe category
		addEntityByCategory(pipe, "Primary Pipe");
	}

	/**
	 * Set the heat exchanger's secondary pipe, removing any other secondary
	 * pipe as necessary.
	 * 
	 * @param pipe
	 *            The Heat Exchanger's new secondary pipe.
	 */
	public void setSecondaryPipe(PipeController pipe) {

		// Get the current secondary pipe, if any
		List<AbstractController> secondary = entities.get("Secondary Pipe");

		// If there is already one, remove it.
		if (secondary != null) {
			if (!secondary.isEmpty()) {
				removeEntity(secondary.get(0));
			}
		}

		// Add the pipe under the Primary Pipe category
		addEntityByCategory(pipe, "Secondary Pipe");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMesh#addEntityByCategory(org
	 * .eclipse.ice.viz.service.modeling.AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController newEntity,
			String category) {

		// Don't listen to junctions, to avoid circular listening
		if ("Secondary Input".equals(category)
				|| "Secondary Output".equals(category)) {

			// Get the entities for the given category
			List<AbstractController> catList = entities.get(category);

			// If the list is null, make an empty one
			if (catList == null) {
				catList = new ArrayList<AbstractController>();
			}

			// Prevent a part from being added multiple times
			else if (catList.contains(newEntity)) {
				return;
			}

			// If the entity is already present in this category, don't add a
			// second
			// entry for it
			else
				for (AbstractController entity : catList) {
					if (entity == newEntity) {
						return;
					}
				}

			// Add the entity to the list and put it in the map
			catList.add(newEntity);
			entities.put(category, catList);

			SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}

		// Otherwise, add the entity normally
		else {
			super.addEntityByCategory(newEntity, category);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		HeatExchangerMesh clone = new HeatExchangerMesh();
		clone.copy(this);
		return clone;
	}
}
