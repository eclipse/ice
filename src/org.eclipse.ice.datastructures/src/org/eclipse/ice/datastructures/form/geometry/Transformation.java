/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.datastructures.form.geometry;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Stores the information needed to generate a 4D affine transformation matrix
 * given certain transformation variables
 * </p>
 * <p>
 * The matrix transformations are applied in the following order: skew, size,
 * scale, rotation, and translation
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "Transformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transformation extends ICEObject {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The amount of skew for each of the three axes: x, y, and z
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Skew")
	@XmlList
	private double[] skew = new double[3];
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Defines the amount of uniform scale for all three dimensions
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Size")
	private double size;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Defines the scaling factors in each of the three dimensions: x, y, and z
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Scale")
	@XmlList
	private double[] scale = new double[3];
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Defines the rotation in radians along the x, y, and z axes passing
	 * through the origin
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Rotation")
	@XmlList
	private double[] rotation = new double[3];
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Defines the translation along each of the three dimensions: x, y, and z
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Translation")
	@XmlList
	private double[] translation = new double[3];

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Upon creation, the Transformation should set its skew values to 0, sizes
	 * to 1, scale to 1, rotations to 0, and translation to 0. The resultant
	 * transformation matrix should be the 4x4 identity matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Transformation() {
		// begin-user-code

		// Set initial transformation variables

		size = 1.0;
		scale[0] = 1.0;
		scale[1] = 1.0;
		scale[2] = 1.0;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an array of the 3 skew values
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The skew values
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getSkew() {
		// begin-user-code
		return skew;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the size factor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The size value
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getSize() {
		// begin-user-code
		return size;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an array of the 3 scale values
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The scale values
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getScale() {
		// begin-user-code
		return scale;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an array of the 3 rotation values
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The rotation values
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getRotation() {
		// begin-user-code
		return rotation;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an array of the 3 translation values
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The translation values
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getTranslation() {
		// begin-user-code
		return translation;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the skew values to the three given parameters
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param x
	 *            <p>
	 *            The skew on the x-axis
	 *            </p>
	 * @param y
	 *            <p>
	 *            The skew on the y-axis
	 *            </p>
	 * @param z
	 *            <p>
	 *            The skew on the z-axis
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSkew(double x, double y, double z) {
		// begin-user-code
		skew[0] = x;
		skew[1] = y;
		skew[2] = z;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the size value to the given parameter
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            <p>
	 *            The size scaling factor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSize(double size) {
		// begin-user-code
		this.size = size;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the scale values to the three given parameters
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param x
	 *            <p>
	 *            The scale on the x-axis
	 *            </p>
	 * @param y
	 *            <p>
	 *            The scale on the y-axis
	 *            </p>
	 * @param z
	 *            <p>
	 *            The scale on the z-axis
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setScale(double x, double y, double z) {
		// begin-user-code
		scale[0] = x;
		scale[1] = y;
		scale[2] = z;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the rotation values to the three given parameters
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param xAxis
	 *            <p>
	 *            The rotation on the x-axis through the origin
	 *            </p>
	 * @param yAxis
	 *            <p>
	 *            The rotation on the y-axis through the origin
	 *            </p>
	 * @param zAxis
	 *            <p>
	 *            The rotation on the z-axis through the origin
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRotation(double xAxis, double yAxis, double zAxis) {
		// begin-user-code
		rotation[0] = xAxis;
		rotation[1] = yAxis;
		rotation[2] = zAxis;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the translation values to the three given parameters
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param x
	 *            <p>
	 *            The translation on the x-axis
	 *            </p>
	 * @param y
	 *            <p>
	 *            The translation on the y-axis
	 *            </p>
	 * @param z
	 *            <p>
	 *            The translation on the z-axis
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTranslation(double x, double y, double z) {
		// begin-user-code
		translation[0] = x;
		translation[1] = y;
		translation[2] = z;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Applies an additional translation to the existing transformation
	 * variables
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param translation
	 *            <p>
	 *            An array of three doubles to add to the existing translation
	 *            values
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void translate(double[] translation) {
		// begin-user-code

		// Don't do anything if the translation array has less or greater
		// than 3 elements

		if (translation.length != 3) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			this.translation[i] += translation[i];
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the flat array of elements in the matrix
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         Flat list of elements in the array
	 *         </p>
	 *         <p>
	 *         Each row from the top to the bottom is stacked end-to-end in a
	 *         one-dimensional array. The ith row and the jth column can be
	 *         accessed as index i * 4 + j in the flat array.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double[] getMatrixArray() {
		// begin-user-code

		// TODO Implement transformation-to-matrix conversion

		return null;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the Transformation from persistent storage as XML.
	 * This operation will throw an IOException if it fails.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            An input stream from which the ICEObject should be loaded.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code

		// Initialize JAXBManipulator
		jaxbManipulator = new ICEJAXBHandler();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream
		Object dataObject;
		try {
			dataObject = jaxbManipulator.read(this.getClass(), inputStream);
			// Copy contents of new object into current data structure
			this.copy((Transformation) dataObject);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Nullerize jaxbManipilator
		jaxbManipulator = null;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hashcode value of the Transformation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode of the ICEObject.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declaration
		int hash = 1;

		// Compute hash code from ICEObject data
		hash = 31 * hash + super.hashCode();

		// Hash the transformation variables
		hash = 31 * hash + Arrays.hashCode(skew);
		hash = 31 * hash + new Double(size).hashCode();
		hash = 31 * hash + Arrays.hashCode(scale);
		hash = 31 * hash + Arrays.hashCode(rotation);
		hash = 31 * hash + Arrays.hashCode(translation);

		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this Transformation and
	 * another Transformation. It returns true if the Transformations are equal
	 * and false if they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other ICEObject that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the ICEObjects are equal, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Check if they are the same reference in memory
		if (this == otherObject) {
			return true;
		}

		// Check that the object is not null, and that it is a Matrix4x4
		if (otherObject == null || !(otherObject instanceof Transformation)) {
			return false;
		}

		// Check that these objects have the same ICEObject data
		if (!super.equals(otherObject)) {
			return false;
		}

		// At this point, other object must be a Matrix4x4, so cast it
		Transformation other = (Transformation) otherObject;

		// Check elements
		return (Arrays.equals(this.skew, other.skew) && this.size == other.size
				&& Arrays.equals(this.scale, other.scale)
				&& Arrays.equals(this.rotation, other.rotation) && Arrays
					.equals(this.translation, other.translation));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Copies the contents of a Transformation into the current object using a
	 * deep copy
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iceObject
	 *            <p>
	 *            The ICEObject from which the values should be copied
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Transformation iceObject) {
		// begin-user-code

		// Return if object is null
		if (iceObject == null) {
			return;
		}
		// Copy contents of super
		super.copy(iceObject);

		// Copy contents of elements - Deep copy
		this.skew = iceObject.skew.clone();
		this.size = iceObject.size;
		this.scale = iceObject.scale.clone();
		this.rotation = iceObject.rotation.clone();
		this.translation = iceObject.translation.clone();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a clone of the Transformation using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Create a new Transformation and copy the current object's data

		Transformation transformation = new Transformation();
		transformation.copy(this);
		return transformation;

		// end-user-code
	}

	/**
	 * Returns the string representation of this Transformation including all of
	 * its transformation variables
	 * 
	 * @return The string representation
	 */
	@Override
	public String toString() {

		// When a new Transformation is initialized, the default string
		// representation is the following.

		// Skew: (0, 0, 0), Size: 1, Scale: (1, 1, 1), Rotation: (0, 0, 0),
		// Translation: (0, 0, 0)

		String output = "Skew: (" + skew[0] + ", ";
		output += skew[1] + ", " + skew[2] + "), ";
		output += "Size: " + size + ", ";
		output += "Scale: (" + scale[0] + ", ";
		output += scale[1] + ", " + scale[2] + "), ";
		output += "Rotation: (" + rotation[0] + ", ";
		output += rotation[1] + ", " + rotation[2] + "), ";
		output += "Translation: (" + translation[0] + ", ";
		output += translation[1] + ", " + translation[2] + ")";

		return output;
	}

}