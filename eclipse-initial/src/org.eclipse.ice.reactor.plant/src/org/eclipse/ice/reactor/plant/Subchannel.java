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

import java.io.InputStream;
import java.security.InvalidParameterException;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Represents a lattice of rods for housing fuel rods of a reactor. For the
 * purpose of drawing, this is just a specialized pipe.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Subchannel extends Pipe {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Number of fuel rods contained within.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int numRods;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Diameter of the subchannel fuel rods (this assumes uniform rod sizes).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double rodDiameter;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Pitch of the fuel rod bundle (distance between adjacent rod centers).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double pitch;

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
	public Subchannel() {
		// begin-user-code

		// Set the name, description and ID.
		setName("Subchannel 1");
		setDescription("A subchannel plant component for reactors");
		setId(1);

		// Set the default number of rods, rod diameter and pitch.
		setNumRods(1);
		setRodDiameter(1.0);
		setPitch(1.5);
		// Note: Pitch must always be set after diameter, as setPitch method
		// checks that pitch >= rodDiameter.

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param numRods
	 *            <p>
	 *            Number of rods contained.
	 *            </p>
	 * @param rodDiameter
	 *            <p>
	 *            Diameter of the (uniformly-sized) fuel rods.
	 *            </p>
	 * @param pitch
	 *            <p>
	 *            Pitch of the fuel rods.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Subchannel(int numRods, double rodDiameter, double pitch) {
		// begin-user-code

		// Set the name, description and ID.
		setName("Subchannel 1");
		setDescription("A subchannel plant component for reactors");
		setId(1);

		// Set the default number of rods, rod diameter and pitch.
		setNumRods(numRods);
		setRodDiameter(rodDiameter);
		setPitch(pitch);

		return;
		// end-user-code
	}

	/**
	 * @return the numRods
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumRods() {
		// begin-user-code
		return numRods;
		// end-user-code
	}

	/**
	 * @param numRods
	 *            the numRods to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setNumRods(int numRods) {
		// begin-user-code

		// Check the input is valid.
		if (numRods >= 1) {
			this.numRods = numRods;
		} else {
			throw new InvalidParameterException("Subchannel error: The number "
					+ "of rods must greater than or equal to 1.");
		}
		return;
		// end-user-code
	}

	/**
	 * @return the rodDiameter
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getRodDiameter() {
		// begin-user-code
		return rodDiameter;
		// end-user-code
	}

	/**
	 * @param rodDiameter
	 *            the rodDiameter to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRodDiameter(double rodDiameter) {
		// begin-user-code

		// Check the input is valid.
		if (rodDiameter > 0) {
			this.rodDiameter = rodDiameter;
		} else {
			throw new InvalidParameterException("Subchannel error: The rod "
					+ "diameter must be non-negative.");
		}
		return;
		// end-user-code
	}

	/**
	 * @return the pitch
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getPitch() {
		// begin-user-code
		return pitch;
		// end-user-code
	}

	/**
	 * @param pitch
	 *            the pitch to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPitch(double pitch) {
		// begin-user-code

		// Check that the input is valid.
		if (pitch >= rodDiameter) {
			this.pitch = pitch;
		} else {
			throw new InvalidParameterException("Subchannel error: The rod "
					+ "pitch cannot be smaller than the rod diameter.");
		}
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

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof Subchannel) {

			// Cast the other object.
			Subchannel component = (Subchannel) otherObject;

			// Compare all the variables. (Save the biggest for last; Java
			// should
			// short-circuit the logical operators as soon as a mismatch is
			// found)
			equals = (super.equals(component) && numRods == component.numRods
					&& rodDiameter == component.rodDiameter && pitch == component.pitch);
		}

		return equals;
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

		// Initialize a new object.
		Subchannel object = new Subchannel();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
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
	public void copy(Subchannel otherObject) {
		// begin-user-code

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Copy the other object.
		super.copy(otherObject);
		numRods = otherObject.numRods;
		rodDiameter = otherObject.rodDiameter;
		pitch = otherObject.pitch;

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

		// Call the super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash = 31 * hash + numRods;
		hash = 31 * hash + new Double(rodDiameter).hashCode();
		hash = 31 * hash + new Double(pitch).hashCode();

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

		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The operation loads the component from the XML stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            The stream containing the SML for this object.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}