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
import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This is the primary design of a countercurrent exchanger. It has two loops, a
 * primary and secondary loop, and therefore four in/outlets.
 * </p>
 * <p>
 * It also contains a solid wall part which is between these two loops, acting
 * as a heat transfer medium.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
@XmlRootElement(name = "HeatExchanger")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatExchanger extends GeometricalComponent {

	/**
	 * <p>
	 * The radius of the inner pipe.
	 * </p>
	 * 
	 */
	@XmlAttribute()
	private double innerRadius;

	/**
	 * <p>
	 * Length of the heat exchanger.
	 * </p>
	 * 
	 */
	@XmlAttribute()
	private double length;

	/**
	 * <p>
	 * Primary pipe of heat exchanger.
	 * </p>
	 * 
	 */
	@XmlElement(name = "PrimaryPipe")
	private final Pipe primaryPipe;

	/**
	 * <p>
	 * The secondary pipe of the heat exchanger.
	 * </p>
	 * 
	 */
	@XmlElement(name = "SecondaryPipe")
	private final Pipe secondaryPipe;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public HeatExchanger() {

		// Call the superconstructor.
		super();

		// Initialize the pipes.
		primaryPipe = new Pipe();
		secondaryPipe = new Pipe();

		// Set the name, description and ID.
		setName("Heat Exchanger");
		setDescription("A heat exchanger for plants");
		setId(1);

		// Set the defaults
		innerRadius = 0.1;
		length = 1.0;

		return;
	}

	/**
	 * @return the innerRadius
	 */
	public double getInnerRadius() {
		return innerRadius;
	}

	/**
	 * @param innerRadius
	 *            the innerRadius to set
	 */
	public void setInnerRadius(double innerRadius) {

		// The radius must be positive and must be different.
		if (innerRadius > 0 && innerRadius != this.innerRadius) {
			this.innerRadius = innerRadius;

			// Update the wrapped pipes.
			primaryPipe.setRadius(innerRadius);
			secondaryPipe.setRadius(innerRadius);

			// Notify listeners of any change.
			notifyListeners();
		}

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

		// The length must be positive and must be different.
		if (length > 0 && length != this.length) {
			this.length = length;

			// Set the lengths of the pipes.
			primaryPipe.setLength(length);
			secondaryPipe.setLength(length);

			// Notify listeners of any change.
			notifyListeners();
		}

	}

	/**
	 * @return the primaryPipe
	 */
	public Pipe getPrimaryPipe() {
		return primaryPipe;
	}

	/**
	 * @return the secondaryPipe
	 */
	public Pipe getSecondaryPipe() {
		return secondaryPipe;
	}

	/**
	 * <p>
	 * Returns a list of the components (primary pipe, secondary pipe -- in that
	 * order) contained in the heat exchanger.
	 * </p>
	 * 
	 * @return <p>
	 *         A list of the components contained, in the order: primary pipe,
	 *         secondary pipe, wall.
	 *         </p>
	 */
	public ArrayList<PlantComponent> getComponents() {

		// Create the list
		ArrayList<PlantComponent> list = new ArrayList<PlantComponent>();

		if (primaryPipe != null && secondaryPipe != null) {
			list.add(primaryPipe);
			list.add(secondaryPipe);
		}

		return list;
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

		// Local Declarations
		boolean retVal = false;

		if (otherObject == this) {
			return true;
		} else if (otherObject != null && otherObject instanceof HeatExchanger
				&& super.equals(otherObject)) {
			HeatExchanger otherHeatExchanger = (HeatExchanger) otherObject;
			retVal = (innerRadius == otherHeatExchanger.innerRadius)
					&& (length == otherHeatExchanger.length)
					&& (primaryPipe.equals(otherHeatExchanger.primaryPipe))
					&& (secondaryPipe.equals(otherHeatExchanger.secondaryPipe));
			// && (solidWall.equals(otherHeatExchanger.solidWall));
		}

		return retVal;
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

		HeatExchanger clone = new HeatExchanger();
		clone.copy(this);
		return clone;

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
	public void copy(HeatExchanger otherObject) {

		if (otherObject != null) {

			// Copy all the base class stuff
			super.copy(otherObject);

			// Copy our stuff
			innerRadius = otherObject.innerRadius;
			length = otherObject.length;
			primaryPipe.copy(otherObject.primaryPipe);
			secondaryPipe.copy(otherObject.secondaryPipe);
		}

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

		int hash = super.hashCode();
		hash = 31 * hash + (int) innerRadius;
		hash = 31 * hash + (int) length;
		hash = 31 * hash + ((primaryPipe != null) ? primaryPipe.hashCode() : 0);
		hash = 31 * hash
				+ ((secondaryPipe != null) ? secondaryPipe.hashCode() : 0);

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

		if (visitor != null) {
			visitor.visit(this);
		}

	}

	/**
	 * Apply the geometric properties to the wrapped Pipes.
	 */
	@Override
	public void setPosition(double[] position) {
		// Set the positions of the Pipes.
		primaryPipe.setPosition(position);
		secondaryPipe.setPosition(position);
		// Set the position of this HeatExchanger.
		super.setPosition(position);
	}

	/**
	 * Apply the geometric properties to the wrapped Pipes.
	 */
	@Override
	public void setOrientation(double[] orientation) {
		// Set the orientations of the Pipes.
		primaryPipe.setOrientation(orientation);
		secondaryPipe.setOrientation(orientation);
		// Set the orientation of this HeatExchanger.
		super.setOrientation(orientation);
	}

	/**
	 * Apply the geometric properties to the wrapped Pipes.
	 */
	@Override
	public void setRotation(double rotation) {
		// Set the rotations of the Pipes.
		primaryPipe.setRotation(rotation);
		secondaryPipe.setRotation(rotation);
		// Set the rotation of this HeatExchanger.
		super.setRotation(rotation);
	}

	/**
	 * Apply the geometric properties to the wrapped Pipes.
	 */
	@Override
	public void setNumElements(int numElements) {
		// Set the number of elements for the Pipes.
		primaryPipe.setNumElements(numElements);
		secondaryPipe.setNumElements(numElements);
		// Set the number of elements for this HeatExchanger.
		super.setNumElements(numElements);
	}

	/**
	 * Overrides the default behavior so that the primary and secondary Pipes
	 * get the same ID as the HeatExchanger.
	 */
	public void setId(int id) {
		// Set the IDs for the Pipes.
		if (primaryPipe != null) {
			primaryPipe.setId(id);
		}
		if (secondaryPipe != null) {
			secondaryPipe.setId(id);
		}
		// Set the HeatExchanger's ID.
		super.setId(id);
	}
}