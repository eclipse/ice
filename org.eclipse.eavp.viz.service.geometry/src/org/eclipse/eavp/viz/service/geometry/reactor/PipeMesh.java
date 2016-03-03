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
package org.eclipse.eavp.viz.service.geometry.reactor;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IMeshCategory;
import org.eclipse.eavp.viz.service.modeling.IMeshProperty;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.TubeMesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The internal representation of a Pipe part.
 * 
 * @author Robert Smith
 *
 */
public class PipeMesh extends TubeMesh {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PipeMesh.class);

	/**
	 * The default constructor.
	 */
	public PipeMesh() {
		super();
	}

	/**
	 * A constructor which initializes a pipe's length and radius.
	 * 
	 * @param length
	 *            The pipe's length
	 * @param radius
	 *            The pipe's radius
	 */
	public PipeMesh(double length, double radius) {
		super(length, radius);
	}

	/**
	 * Convenience getter method for the number of rods.
	 * 
	 * @return The number of rods in a SubChannel pipe
	 */
	public int getNumRods() {
		return Integer.parseInt(properties.get(ReactorMeshProperty.NUM_RODS));
	}

	/**
	 * Convenience getter method for the pitch
	 * 
	 * @return The pipe's pitch
	 */
	public double getPitch() {
		return Double.parseDouble(properties.get(ReactorMeshProperty.PITCH));
	}

	/**
	 * Convenience getter method for the rod diameter
	 * 
	 * @return The pipe's rod diameter, under the assumption that all rods are
	 *         of uniform size.
	 */
	public double getRodDiameter() {
		return Double
				.parseDouble(properties.get(ReactorMeshProperty.ROD_DIAMETER));
	}

	/**
	 * Convenience setter method for a SubChannel Pipe's number of rods. Does
	 * nothing for non-SubChannel pipes.
	 * 
	 * @param numRods
	 *            The number of rods in the SubChannel
	 */
	public void setNumRods(int numRods) {
		setProperty(ReactorMeshProperty.NUM_RODS, Integer.toString(numRods));
	}

	/**
	 * Convenience setter method for the pipe's pitch
	 * 
	 * @param pitch
	 */
	public void setPitch(double pitch) {
		setProperty(ReactorMeshProperty.PITCH, Double.toString(pitch));
	}

	/**
	 * Convenience setter method for the pipe's rod diameter
	 * 
	 * @param rodDiameter
	 *            The pipe's rod diameter
	 */
	public void setRodDiameter(double rodDiameter) {
		setProperty(ReactorMeshProperty.ROD_DIAMETER,
				Double.toString(rodDiameter));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.ShapeMesh#addEntityByCategory(org.
	 * eclipse.ice.viz.service.modeling.AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityToCategory(IController entity,
			IMeshCategory category) {

		// If adding an input or output, add it without registering
		// as a listener, to avoid circular updates
		if (ReactorMeshCategory.INPUT.equals(category)
				|| ReactorMeshCategory.OUTPUT.equals(category)) {

			// Get the entities for the given category
			ArrayList<IController> catList = entities.get(category);

			// If the list is null, make an empty one
			if (catList == null) {
				catList = new ArrayList<IController>();
			}

			// Prevent a part from being added multiple times
			else if (catList.contains(entity)) {
				return;
			}

			// If the entity is already present in this category, don't add a
			// second entry for it
			else
				for (IController currentEntity : catList) {
					if (entity == currentEntity) {
						return;
					}
				}

			// Add the entity to the list and put it in the map
			catList.add(entity);
			entities.put(category, catList);

			// Notify listeners of the new child
			SubscriptionType[] eventTypes = { SubscriptionType.CHILD };
			updateManager.notifyListeners(eventTypes);
		}

		// Otherwise, add it normally
		else {
			super.addEntityToCategory(entity, category);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.ShapeMesh#setProperty(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void setProperty(IMeshProperty property, String value) {

		// Validate input
		if (MeshProperty.INNER_RADIUS.equals(property)) {
			logger.error(
					"Pipes are specified as always having an inner radius equal to their outer radius. Inner radius cannot be set.");
			return;
		}

		super.setProperty(property, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.TubeComponent#getInnerRadius()
	 */
	@Override
	public double getInnerRadius() {

		// Pipes are always drawn with infinite thinness, so their inner and
		// outer radii are identical
		return getRadius();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Make a new shape component and copy the data into it
		PipeMesh clone = new PipeMesh();
		clone.copy(this);

		return clone;
	}
}
