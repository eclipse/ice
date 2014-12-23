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
 * <!-- begin-UML-doc -->
 * <p>
 * Intermediate class for all geometrical components (i.e. components that have
 * position, direction, etc. in space; they generate a mesh)
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "GeometricalComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeometricalComponent extends PlantComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Origin (start) of the pipe. Array should contain 3 elements, representing
	 * (x,y,z) coordinates.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Position")
	protected double[] position;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Orientation vector of the pipe. Array should contain 3 elements,
	 * representing (x,y,z) coordinates.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Orientation")
	protected double[] orientation;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Rotation of the component (in degrees).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Rotation")
	protected double rotation;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Number of elements in the component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "NumberElements")
	protected int numElements;

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
	public GeometricalComponent() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GeometricalComponent(double[] pos, double[] orient, double rot) {
		// begin-user-code

		// Call the nullary constructor.
		this();

		// Set the position, orientation and rotation.
		setPosition(pos);
		setOrientation(orient);
		setRotation(rot);

		return;
		// end-user-code
	}

	/**
	 * @return the position
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getPosition() {
		// begin-user-code
		return position;
		// end-user-code
	}

	/**
	 * @param position
	 *            the position to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPosition(double[] position) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * @return the orientation
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getOrientation() {
		// begin-user-code
		return orientation;
		// end-user-code
	}

	/**
	 * @param orientation
	 *            the orientation to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setOrientation(double[] orientation) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * @return the rotation
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getRotation() {
		// begin-user-code
		return rotation;
		// end-user-code
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRotation(double rotation) {
		// begin-user-code

		// If the rotation has changed, set it and notify listeners.
		if (rotation != this.rotation) {
			this.rotation = rotation;

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
		// end-user-code
	}

	/**
	 * @return the numElements
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumElements() {
		// begin-user-code
		return numElements;
		// end-user-code
	}

	/**
	 * @param numElements
	 *            the numElements to set
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setNumElements(int numElements) {
		// begin-user-code

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
		GeometricalComponent object = new GeometricalComponent();

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
	public void copy(GeometricalComponent otherObject) {
		// begin-user-code

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
		hash = 31 * hash + Arrays.hashCode(position);
		hash = 31 * hash + Arrays.hashCode(orientation);
		hash = 31 * hash + new Double(rotation).hashCode();
		hash = 31 * hash + numElements;

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Accepts IPlantComponentVisitors to reveal the type of a PlantComponent.
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