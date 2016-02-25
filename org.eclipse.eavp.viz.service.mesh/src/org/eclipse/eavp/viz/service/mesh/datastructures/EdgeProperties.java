/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.eavp.viz.service.mesh.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This class contains properties of an edge. These properties can be maintained
 * by either a Polygon or the Edge itself. All properties are private fields but
 * can be accessed via getters and setters.
 * </p>
 * <p>
 * </p>
 * <p>
 * Currently, the properties only contain the boundary conditions of edges
 * (faces). The intent is to let the Polygon (element) manage these properties
 * since in Nek the elements have boundary conditions for all their faces.
 * Shared faces can have two fluid, thermal, or nth passive scalar boundary
 * conditions.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@XmlRootElement(name = "EdgeProperties")
@XmlAccessorType(XmlAccessType.FIELD)
public class EdgeProperties {

	/**
	 * <p>
	 * The fluid boundary condition for this edge in a polygon.
	 * </p>
	 * 
	 */
	@XmlElement(name = "fluidCondition")
	private BoundaryCondition fluidBoundaryCondition;
	/**
	 * <p>
	 * The thermal boundary condition for this edge in a polygon.
	 * </p>
	 * 
	 */
	@XmlElement(name = "thermalCondition")
	private BoundaryCondition thermalBoundaryCondition;
	/**
	 * <p>
	 * A list of passive scalar boundary condition for this edge in a polygon.
	 * </p>
	 * 
	 */
	@XmlElement(name = "otherCondition")
	private ArrayList<BoundaryCondition> otherBoundaryConditions;

	/**
	 * <p>
	 * The default constructor for properties of an Edge. This initializes every
	 * property it can to non-null values.
	 * </p>
	 * 
	 */
	public EdgeProperties() {

		// Initialize the boundary condition fields.
		fluidBoundaryCondition = new BoundaryCondition();
		thermalBoundaryCondition = new BoundaryCondition();
		otherBoundaryConditions = new ArrayList<BoundaryCondition>();

		return;
	}

	/**
	 * <p>
	 * Sets the fluid boundary condition for the edge.
	 * </p>
	 * 
	 * @param condition
	 *            The new BoundaryCondition.
	 * @return True if the new condition was successfully set, false otherwise.
	 */
	public boolean setFluidBoundaryCondition(BoundaryCondition condition) {

		boolean changed = false;

		// Only set the boundary condition if:
		// The new condition is not null
		// The new condition is not the same one as the old one
		if (condition != null && condition != fluidBoundaryCondition) {
			fluidBoundaryCondition = condition;
			changed = true;
		}

		return changed;
	}

	/**
	 * <p>
	 * Gets the fluid boundary condition for the edge.
	 * </p>
	 * 
	 * @return The edge's BoundaryCondition. This should never be null.
	 */
	public BoundaryCondition getFluidBoundaryCondition() {
		return fluidBoundaryCondition;
	}

	/**
	 * <p>
	 * Sets the thermal boundary condition for the edge.
	 * </p>
	 * 
	 * @param condition
	 *            The new BoundaryCondition.
	 * @return True if the new condition was successfully set, false otherwise.
	 */
	public boolean setThermalBoundaryCondition(BoundaryCondition condition) {

		boolean changed = false;

		// Only set the boundary condition if:
		// The new condition is not null
		// The new condition is not the same one as the old one
		if (condition != null && condition != thermalBoundaryCondition) {
			thermalBoundaryCondition = condition;
			changed = true;
		}

		return changed;
	}

	/**
	 * <p>
	 * Gets the thermal boundary condition for the edge.
	 * </p>
	 * 
	 * @return The edge's BoundaryCondition. This should never be null.
	 */
	public BoundaryCondition getThermalBoundaryCondition() {
		return thermalBoundaryCondition;
	}

	/**
	 * <p>
	 * Sets a passive scalar boundary condition for the edge.
	 * </p>
	 * 
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @param condition
	 *            The new BoundaryCondition.
	 * @return True if the new condition was successfully set, false otherwise.
	 */
	public boolean setOtherBoundaryCondition(int otherId,
			BoundaryCondition condition) {

		boolean changed = false;

		// The new boundary condition will only be added if:
		// The new condition is not null
		// The otherId (index of the passive scalar set) is valid (non-negative)
		if (condition != null && otherId >= 0) {
			// Get the size of the list of passive scalar conditions.
			int size = otherBoundaryConditions.size();

			// We only need to make any changes if:
			// The new passive scalar index is too big (we need to add more
			// default conditions to the list)
			// The new condition is not the same one as the old one

			// If necessary, add new, default BoundaryConditions to the list,
			// then add the new condition to the end of the list.
			if (otherId >= size) {
				for (int i = size; i < otherId; i++) {
					otherBoundaryConditions.add(new BoundaryCondition());
				}
				otherBoundaryConditions.add(condition);
				changed = true;
			}
			// If the index is already valid, compare the new condition with the
			// one in the list and update it if necessary.
			else if (condition != otherBoundaryConditions.get(otherId)) {
				otherBoundaryConditions.set(otherId, condition);
				changed = true;
			}
		}

		return changed;
	}

	/**
	 * <p>
	 * Gets a passive scalar boundary condition from the edge.
	 * </p>
	 * 
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @return The edge's BoundaryCondition, or null if the passive scalar index
	 *         is invalid.
	 */
	public BoundaryCondition getOtherBoundaryCondition(int otherId) {

		// The default return value
		BoundaryCondition condition = null;

		// Check the index before trying to retrieve the boundary condition.
		if (otherId >= 0 && otherId < otherBoundaryConditions.size()) {
			condition = otherBoundaryConditions.get(otherId);
		}

		return condition;
	}

	/**
	 * <p>
	 * Gets all passive scalar boundary conditions for the edge.
	 * </p>
	 * 
	 * @return An ArrayList of all passive scalar boundary conditions for the
	 *         edge. If no passive scalar boundary conditions exist, this will
	 *         be an empty list.
	 */
	public ArrayList<BoundaryCondition> getOtherBoundaryConditions() {
		return new ArrayList<BoundaryCondition>(otherBoundaryConditions);
	}

	/**
	 * <p>
	 * This operation returns the hash value of the EdgeProperties.
	 * </p>
	 * 
	 * @return The hash of the Object.
	 */
	@Override
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = 31;

		// Add local hashes.
		hash = 31 * hash + fluidBoundaryCondition.hashCode();
		hash = 31 * hash + thermalBoundaryCondition.hashCode();
		hash = 31 * hash + otherBoundaryConditions.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this EdgeProperties and
	 * another. It returns true if they are equal and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other Object that should be compared with this one.
	 * @return True if the Objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof EdgeProperties) {

			// We can now cast the other object.
			EdgeProperties conditions = (EdgeProperties) otherObject;

			// Compare the values between the two objects.
			equals = (fluidBoundaryCondition
					.equals(conditions.fluidBoundaryCondition)
					&& thermalBoundaryCondition
							.equals(conditions.thermalBoundaryCondition) && otherBoundaryConditions
					.equals(conditions.otherBoundaryConditions));
		}

		return equals;
	}

	/**
	 * <p>
	 * This operation copies the contents of a EdgeProperties into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param properties
	 *            The Object from which the values should be copied.
	 */
	public void copy(EdgeProperties properties) {

		// Check the parameters.
		if (properties == null) {
			return;
		}

		/* ---- Deep copy the boundary conditions. ---- */
		// Clone the fluid boundary condition.
		fluidBoundaryCondition = (BoundaryCondition) properties.fluidBoundaryCondition
				.clone();

		// Clone the thermal boundary condition.
		thermalBoundaryCondition = (BoundaryCondition) properties.thermalBoundaryCondition
				.clone();

		// Clone and add each of the passive scalar boundary conditions.
		otherBoundaryConditions.clear();
		for (BoundaryCondition condition : properties.otherBoundaryConditions) {
			// Clone non-null boundary conditions.
			if (condition != null) {
				condition = (BoundaryCondition) condition.clone();
			}
			otherBoundaryConditions.add(condition);
		}
		/* -------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the EdgeProperties using a deep copy.
	 * </p>
	 * 
	 * @return The new clone.
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		EdgeProperties object = new EdgeProperties();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}
	
}
