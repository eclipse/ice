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
package org.eclipse.ice.reactor.plant;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * A simple pipe component.
 * </p>
 * <p>
 * A pipe is defined by its position, direction, length and area.
 * </p>
 * 
 * @author w5q
 */
@XmlRootElement(name = "Pipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pipe extends GeometricalComponent {
	/**
	 * <p>
	 * Length of the pipe.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Length")
	protected double length;

	/**
	 * <p>
	 * Inner radius of the pipe.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Radius")
	protected double radius;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public Pipe() {

		// Set the name, description and ID.
		setName("Pipe 1");
		setDescription("Pipe plant component for reactors");
		setId(1);

		// Set the length, number of elements and radius.
		setLength(1.0);
		setRadius(0.1);

		return;
	}

	/**
	 * 
	 * @param length
	 *            <p>
	 *            Length of the pipe.
	 *            </p>
	 * @param radius
	 *            <p>
	 *            Radius of the pipe.
	 *            </p>
	 */
	public Pipe(double length, double radius) {

		// Call the nullary constructor.
		this();

		// Set the length and radius.
		setLength(length);
		setRadius(radius);

		return;
	}

	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(double length) {

		// Check that the input is valid.
		if (length > 0) {
			// Don't update the length unless the value is different!
			if (length != this.length) {
				this.length = length;

				// Notify listeners of the change.
				notifyListeners();
			}
		} else {
			throw new InvalidParameterException("Pipe error: "
					+ "Length must be non-negative.");
		}
		return;
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(double radius) {

		// Check the radius is valid.
		if (radius > 0) {
			// Don't update the radius unless the value is different!
			if (radius != this.radius) {
				this.radius = radius;

				// Notify listeners of the change.
				notifyListeners();
			}
		} else {
			throw new InvalidParameterException("Pipe error: "
					+ "The radius must be non-negative.");
		}
		return;
	}

	/**
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof Pipe) {

			// Cast the other object.
			Pipe component = (Pipe) otherObject;

			// Compare all the variables. (Save the biggest for last; Java
			// should
			// short-circuit the logical operators as soon as a mismatch is
			// found)
			equals = (super.equals(component) && length == component.length && radius == component.radius);
		}

		return equals;
	}

	/**
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 */
	public Object clone() {

		// Initialize a new object.
		Pipe object = new Pipe();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 */
	public void copy(Pipe otherObject) {

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Copy the other object.
		super.copy(otherObject);
		length = otherObject.length;
		radius = otherObject.radius;

		return;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 */
	public int hashCode() {

		// Call the super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash = 31 * hash + new Double(length).hashCode();
		hash = 31 * hash + new Double(radius).hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Accepts PlantComponentVisitors to reveal the type of a PlantComponent.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 */
	public void accept(IPlantComponentVisitor visitor) {

		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}