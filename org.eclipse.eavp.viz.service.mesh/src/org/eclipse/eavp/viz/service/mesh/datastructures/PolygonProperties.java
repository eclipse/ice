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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * A container for the material ID and group number of every polygon, as defined
 * in the MESH DATA section of a Nek5000 reafile. Example:
 * </p>
 * <code>
 *             ELEMENT      44 [    1R]  GROUP  0
 * </code>
 * <p>
 * For this Polygon, materialId = "1R", groupNum = 0.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
@XmlRootElement(name = "PolygonProperties")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolygonProperties {

	/**
	 * <p>
	 * Material ID of the polygon. Must be between 1-4 chars long.
	 * </p>
	 * 
	 */
	@XmlAttribute
	private String materialId;

	/**
	 * <p>
	 * Group number of the polygon. Must be no more than 5 digits.
	 * </p>
	 * 
	 */
	@XmlAttribute
	private int groupNum;

	/**
	 * <p>
	 * A nullary constructor that sets default values.
	 * </p>
	 * 
	 */
	public PolygonProperties() {
		materialId = "nul1";
		groupNum = 0;
	}

	/**
	 * <p>
	 * The default, parameterized constructor.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            Material ID of the polygon. Must be between 1-4 chars long.
	 *            </p>
	 * @param group
	 *            <p>
	 *            Group number of the polygon. Must be no more than 5 digits.
	 *            </p>
	 */
	public PolygonProperties(String id, int group) {

		this();

		// Check for valid values
		if (id != null && id.length() <= 4 && group <= 99999) {
			materialId = id;
			groupNum = group;
		}

		return;
	}

	/**
	 * <p>
	 * Returns the material ID as a string.
	 * </p>
	 * 
	 * @return <p>
	 *         The material ID of the polygon.
	 *         </p>
	 */
	public String getMaterialId() {
		return materialId;
	}

	/**
	 * <p>
	 * Returns the group number as an int.
	 * </p>
	 * 
	 * @return <p>
	 *         The group number of the polygon.
	 *         </p>
	 */
	public int getGroupNum() {
		return groupNum;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this PolygonProperties
	 * and another PolygonProperties. It returns true if they are equal and
	 * false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the Objects are equal, false otherwise.
	 *         </p>
	 * 
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
		else if (otherObject != null
				&& otherObject instanceof PolygonProperties) {

			// We can now cast the other object.
			PolygonProperties properties = (PolygonProperties) otherObject;

			// Compare the values between the two objects.
			equals = (materialId.equals(properties.materialId) && groupNum == properties.groupNum);
		}

		return equals;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the PolygonProperties.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the Object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Add local hashes.
		int hash = materialId.hashCode();
		hash = 31 * hash + groupNum;

		return hash;
	}

	/**
	 * <p>
	 * This operation returns a clone of the PolygonProperties using a deep
	 * copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		PolygonProperties object = new PolygonProperties();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * <p>
	 * This operation copies the contents of a PolygonProperties into the
	 * current object using a deep copy.
	 * </p>
	 * 
	 * @param properties
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 */
	public void copy(PolygonProperties properties) {

		// Check the parameters.
		if (properties == null) {
			return;
		}

		materialId = properties.materialId;
		groupNum = properties.groupNum;

		return;
	}
}
