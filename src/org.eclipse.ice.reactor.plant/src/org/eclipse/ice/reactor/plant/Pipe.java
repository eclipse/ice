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
 * <!-- begin-UML-doc -->
 * <p>
 * A simple pipe component.
 * </p>
 * <p>
 * A pipe is defined by its position, direction, length and area.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "Pipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pipe extends GeometricalComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Length of the pipe.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Length")
	protected double length;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Inner radius of the pipe.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Radius")
	protected double radius;

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
	public Pipe() {
		// begin-user-code

		// Set the name, description and ID.
		setName("Pipe 1");
		setDescription("Pipe plant component for reactors");
		setId(1);

		// Set the length, number of elements and radius.
		setLength(1.0);
		setRadius(0.1);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param length
	 *            <p>
	 *            Length of the pipe.
	 *            </p>
	 * @param radius
	 *            <p>
	 *            Radius of the pipe.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Pipe(double length, double radius) {
		// begin-user-code

		// Call the nullary constructor.
		this();

		// Set the length and radius.
		setLength(length);
		setRadius(radius);

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
		// end-user-code
	}

	/**
	 * @return the radius
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getRadius() {
		// begin-user-code
		return radius;
		// end-user-code
	}

	/**
	 * @param radius
	 *            the radius to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRadius(double radius) {
		// begin-user-code

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
		Pipe object = new Pipe();

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
	public void copy(Pipe otherObject) {
		// begin-user-code

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Copy the other object.
		super.copy(otherObject);
		length = otherObject.length;
		radius = otherObject.radius;

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
		hash = 31 * hash + new Double(length).hashCode();
		hash = 31 * hash + new Double(radius).hashCode();

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
}