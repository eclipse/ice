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
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Intermediate class for all geometrical components (i.e. components that have
 * position, direction, etc. in space; they generate a mesh)
 * </p>
 * 
 * @author w5q
 */
@XmlRootElement(name = "GeometricalComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeometricalComponent extends PlantComponent {
	/**
	 * <p>
	 * Origin (start) of the pipe. Array should contain 3 elements, representing
	 * (x,y,z) coordinates.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Position")
	protected double[] position;

	/**
	 * <p>
	 * Orientation vector of the pipe. Array should contain 3 elements,
	 * representing (x,y,z) coordinates.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Orientation")
	protected double[] orientation;

	/**
	 * <p>
	 * Rotation of the component (in degrees).
	 * </p>
	 * 
	 */
	@XmlElement(name = "Rotation")
	protected double rotation;

	/**
	 * <p>
	 * Number of elements in the component.
	 * </p>
	 * 
	 */
	@XmlElement(name = "NumberElements")
	protected int numElements;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public GeometricalComponent() {

		// Set the name, description and ID.
		setName("Geometrical Component 1");
		setDescription("A geometrical plant component for reactors");
		setId(1);

		// Set the default position, orientation and rotation.
		position = new double[3];
		for (int i = 0; i < 3; i++) {
			position[i] = 0.0;
		}

		orientation = new double[3];
		for (int i = 0; i < 3; i++) {
			orientation[i] = 0.0;
		}
		rotation = 0.0;
		numElements = 1;

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * 
	 * @param pos
	 *            <p>
	 *            Position (origin) of the component.
	 *            </p>
	 * @param orient
	 *            <p>
	 *            Orientation vector of the pipe.
	 *            </p>
	 * @param rot
	 *            <p>
	 *            Rotation of the component (in degrees).
	 *            </p>
	 */
	public GeometricalComponent(double[] pos, double[] orient, double rot) {

		// Call the nullary constructor.
		this();

		// Set the position, orientation and rotation.
		setPosition(pos);
		setOrientation(orient);
		setRotation(rot);

		return;
	}

	/**
	 * @return the position
	 */
	public double[] getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(double[] position) {

		// Check for a valid parameter (non-null 3D vector).
		if (position != null && position.length == 3) {
			// If the position changes, set it and notify listeners.
			if (position[0] != this.position[0]
					|| position[1] != this.position[1]
					|| position[2] != this.position[2]) {

				this.position[0] = position[0];
				this.position[1] = position[1];
				this.position[2] = position[2];

				// Notify listeners of the change.
				notifyListeners();
			}
		} else {
			throw new InvalidParameterException("GeometricalComponent error:"
					+ "The position array must be non-null and 3 elements in "
					+ "size.");
		}

		return;
	}

	/**
	 * @return the orientation
	 */
	public double[] getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation
	 *            the orientation to set
	 */
	public void setOrientation(double[] orientation) {

		// Check the parameter is valid (non-null 3D vector).
		if (orientation != null && orientation.length == 3) {
			// If the orientation changes, set it and notify listeners.
			if (orientation[0] != this.orientation[0]
					|| orientation[1] != this.orientation[1]
					|| orientation[2] != this.orientation[2]) {

				this.orientation[0] = orientation[0];
				this.orientation[1] = orientation[1];
				this.orientation[2] = orientation[2];

				// Notify listeners of the change.
				notifyListeners();
			}
		} else {
			throw new InvalidParameterException(
					"GeometricalComponent error:"
							+ "The orientation array must be non-null and 3 elements in"
							+ "size.");
		}
		return;
	}

	/**
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(double rotation) {

		// If the rotation has changed, set it and notify listeners.
		if (rotation != this.rotation) {
			this.rotation = rotation;

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * @return the numElements
	 */
	public int getNumElements() {
		return numElements;
	}

	/**
	 * @param numElements
	 *            the numElements to set
	 */
	public void setNumElements(int numElements) {

		// Check the number of elements is valid.
		if (numElements >= 1) {
			// If the value changes, set it and notify listeners.
			if (numElements != this.numElements) {
				this.numElements = numElements;

				// Notify listeners of the change.
				notifyListeners();
			}
		} else {
			throw new InvalidParameterException("GeometricalComponent error: "
					+ "The number of elements must positive.");
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
		else if (otherObject != null
				&& otherObject instanceof GeometricalComponent) {

			// Cast the other object.
			GeometricalComponent component = (GeometricalComponent) otherObject;

			// Compare all the variables. (Save the biggest for last; Java
			// should
			// short-circuit the logical operators as soon as a mismatch is
			// found)
			equals = (super.equals(component)
					&& Arrays.equals(position, component.position)
					&& Arrays.equals(orientation, component.orientation)
					&& rotation == component.rotation && numElements == component.numElements);
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
		GeometricalComponent object = new GeometricalComponent();

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
	public void copy(GeometricalComponent otherObject) {

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Copy the other object.
		super.copy(otherObject);
		position = otherObject.position;
		orientation = otherObject.orientation;
		rotation = otherObject.rotation;
		numElements = otherObject.numElements;

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
		hash = 31 * hash + Arrays.hashCode(position);
		hash = 31 * hash + Arrays.hashCode(orientation);
		hash = 31 * hash + new Double(rotation).hashCode();
		hash = 31 * hash + numElements;

		return hash;
	}

	/**
	 * <p>
	 * Accepts IPlantComponentVisitors to reveal the type of a PlantComponent.
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