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
 * <!-- begin-UML-doc -->
 * <p>
 * This is the primary design of a countercurrent exchanger. It has two loops, a
 * primary and secondary loop, and therefore four in/outlets.
 * </p>
 * <p>
 * It also contains a solid wall part which is between these two loops, acting
 * as a heat transfer medium.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "HeatExchanger")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatExchanger extends GeometricalComponent {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The radius of the inner pipe.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	private double innerRadius;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Length of the heat exchanger.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	private double length;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Primary pipe of heat exchanger.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "PrimaryPipe")
	private final Pipe primaryPipe;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The secondary pipe of the heat exchanger.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "SecondaryPipe")
	private final Pipe secondaryPipe;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public HeatExchanger() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * @return the innerRadius
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getInnerRadius() {
		// begin-user-code
		return innerRadius;
		// end-user-code
	}

	/**
	 * @param innerRadius
	 *            the innerRadius to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInnerRadius(double innerRadius) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * @return the length
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getLength() {
		// begin-user-code
		return length;
		// end-user-code
	}

	/**
	 * @param length
	 *            the length to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLength(double length) {
		// begin-user-code

		// The length must be positive and must be different.
		if (length > 0 && length != this.length) {
			this.length = length;

			// Set the lengths of the pipes.
			primaryPipe.setLength(length);
			secondaryPipe.setLength(length);

			// Notify listeners of any change.
			notifyListeners();
		}

		// end-user-code
	}

	/**
	 * @return the primaryPipe
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Pipe getPrimaryPipe() {
		// begin-user-code
		return primaryPipe;
		// end-user-code
	}

	/**
	 * @return the secondaryPipe
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Pipe getSecondaryPipe() {
		// begin-user-code
		return secondaryPipe;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a list of the components (primary pipe, secondary pipe -- in that
	 * order) contained in the heat exchanger.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A list of the components contained, in the order: primary pipe,
	 *         secondary pipe, wall.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<PlantComponent> getComponents() {
		// begin-user-code

		// Create the list
		ArrayList<PlantComponent> list = new ArrayList<PlantComponent>();

		if (primaryPipe != null && secondaryPipe != null) {
			list.add(primaryPipe);
			list.add(secondaryPipe);
		}

		return list;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		HeatExchanger clone = new HeatExchanger();
		clone.copy(this);
		return clone;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(HeatExchanger otherObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		int hash = super.hashCode();
		hash = 31 * hash + (int) innerRadius;
		hash = 31 * hash + (int) length;
		hash = 31 * hash + ((primaryPipe != null) ? primaryPipe.hashCode() : 0);
		hash = 31 * hash
				+ ((secondaryPipe != null) ? secondaryPipe.hashCode() : 0);

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Accepts PlantComponentVisitors to reveal the type of a PlantComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IPlantComponentVisitor visitor) {
		// begin-user-code

		if (visitor != null) {
			visitor.visit(this);
		}

		// end-user-code
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