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
package org.eclipse.ice.viz.service.geometry.reactor;

import java.util.List;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;

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
		return (PipeController) entities.get("Secondary Pipe").get(0);
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
		if (!secondary.isEmpty()) {
			removeEntity(secondary.get(0));
		}

		// Add the pipe under the Primary Pipe category
		addEntityByCategory(pipe, "Secondary Pipe");
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
