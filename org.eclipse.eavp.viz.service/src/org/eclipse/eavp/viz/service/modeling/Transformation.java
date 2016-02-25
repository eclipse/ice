/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;

/**
 * <p>
 * Stores the information needed to generate a 4D affine transformation matrix
 * given certain transformation variables
 * </p>
 * <p>
 * The matrix transformations are applied in the following order: skew, size,
 * scale, rotation, and translation
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "Transformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transformation extends VizObject implements IManagedUpdateable {
	/**
	 * <p>
	 * The amount of skew for each of the three axes: x, y, and z
	 * </p>
	 * 
	 */
	@XmlElement(name = "Skew")
	@XmlList
	private double[] skew = new double[3];
	/**
	 * <p>
	 * Defines the amount of uniform scale for all three dimensions
	 * </p>
	 * 
	 */
	@XmlElement(name = "Size")
	private double size;
	/**
	 * <p>
	 * Defines the scaling factors in each of the three dimensions: x, y, and z
	 * </p>
	 * 
	 */
	@XmlElement(name = "Scale")
	@XmlList
	private double[] scale = new double[3];
	/**
	 * <p>
	 * Defines the rotation in radians along the x, y, and z axes passing
	 * through the origin
	 * </p>
	 * 
	 */
	@XmlElement(name = "Rotation")
	@XmlList
	private double[] rotation = new double[3];
	/**
	 * <p>
	 * Defines the translation along each of the three dimensions: x, y, and z
	 * </p>
	 * 
	 */
	@XmlElement(name = "Translation")
	@XmlList
	private double[] translation = new double[3];

	/**
	 * The manager for filtering event notifications from this object to
	 * subscribed listeners.
	 */
	private UpdateableSubscriptionManager updateManager = new UpdateableSubscriptionManager(
			this);

	/**
	 * <p>
	 * Upon creation, the Transformation should set its skew values to 0, sizes
	 * to 1, scale to 1, rotations to 0, and translation to 0. The resultant
	 * transformation matrix should be the 4x4 identity matrix.
	 * </p>
	 * 
	 */
	public Transformation() {

		// Set initial transformation variables

		size = 1.0;
		scale[0] = 1.0;
		scale[1] = 1.0;
		scale[2] = 1.0;

	}

	/**
	 * Combines the Transformation with another, summing each individual value
	 * from the other Transformation with its own data members.
	 * 
	 * @param other
	 *            The Transformation to add to this one
	 */
	public void add(Transformation other) {

		// Add each value from the other transformation to this one
		size = size + other.size;
		rotation[0] = rotation[0] + other.rotation[0];
		scale[0] = scale[0] + other.scale[0];
		skew[0] = skew[0] + other.skew[0];
		translation[0] = translation[0] + other.translation[0];
		rotation[1] = rotation[1] + other.rotation[1];
		scale[1] = scale[1] + other.scale[1];
		skew[1] = skew[1] + other.skew[1];
		translation[1] = translation[1] + other.translation[1];
		rotation[2] = rotation[2] + other.rotation[2];
		scale[2] = scale[2] + other.scale[2];
		skew[2] = skew[2] + other.skew[2];
		translation[2] = translation[2] + other.translation[2];

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * <p>
	 * Returns an array of the 3 skew values
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The skew values
	 *         </p>
	 */
	public double[] getSkew() {
		return skew;
	}

	/**
	 * <p>
	 * Returns the size factor
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The size value
	 *         </p>
	 */
	public double getSize() {
		return size;
	}

	/**
	 * <p>
	 * Returns an array of the 3 scale values
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The scale values
	 *         </p>
	 */
	public double[] getScale() {
		return scale;
	}

	/**
	 * <p>
	 * Returns an array of the 3 rotation values
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The rotation values
	 *         </p>
	 */
	public double[] getRotation() {
		return rotation;
	}

	/**
	 * <p>
	 * Returns an array of the 3 translation values
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The translation values
	 *         </p>
	 */
	public double[] getTranslation() {
		return translation;
	}

	/**
	 * <p>
	 * Sets the skew values to the three given parameters
	 * </p>
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
	 */
	public void setSkew(double x, double y, double z) {
		skew[0] = x;
		skew[1] = y;
		skew[2] = z;

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * <p>
	 * Sets the size value to the given parameter
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The size scaling factor
	 *            </p>
	 */
	public void setSize(double size) {
		this.size = size;

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * <p>
	 * Sets the scale values to the three given parameters
	 * </p>
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
	 */
	public void setScale(double x, double y, double z) {
		scale[0] = x;
		scale[1] = y;
		scale[2] = z;

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * <p>
	 * Sets the rotation values to the three given parameters
	 * </p>
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
	 */
	public void setRotation(double xAxis, double yAxis, double zAxis) {
		rotation[0] = xAxis;
		rotation[1] = yAxis;
		rotation[2] = zAxis;

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * <p>
	 * Sets the translation values to the three given parameters
	 * </p>
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
	 */
	public void setTranslation(double x, double y, double z) {
		translation[0] = x;
		translation[1] = y;
		translation[2] = z;

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * <p>
	 * Applies an additional translation to the existing transformation
	 * variables
	 * </p>
	 * 
	 * @param translation
	 *            <p>
	 *            An array of three doubles to add to the existing translation
	 *            values
	 *            </p>
	 */
	public void translate(double[] translation) {

		// Don't do anything if the translation array has less or greater
		// than 3 elements

		if (translation.length != 3) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			this.translation[i] += translation[i];
		}

		// Notify listeners of the change
		SubscriptionType[] eventTypes = new SubscriptionType[1];
		eventTypes[0] = SubscriptionType.TRANSFORMATION;
		updateManager.notifyListeners(eventTypes);

	}

	/**
	 * <p>
	 * Returns the flat array of elements in the matrix
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         Flat list of elements in the array
	 *         </p>
	 *         <p>
	 *         Each row from the top to the bottom is stacked end-to-end in a
	 *         one-dimensional array. The ith row and the jth column can be
	 *         accessed as index i * 4 + j in the flat array.
	 *         </p>
	 */
	public double[] getMatrixArray() {

		// TODO Implement transformation-to-matrix conversion

		return null;

	}

	/**
	 * <p>
	 * This operation returns the hashcode value of the Transformation.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The hashcode of the ICEObject.
	 *         </p>
	 */
	@Override
	public int hashCode() {

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

	}

	/**
	 * <p>
	 * This operation is used to check equality between this Transformation and
	 * another Transformation. It returns true if the Transformations are equal
	 * and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other ICEObject that should be compared with this one.
	 *            </p>
	 * @return
	 * 		<p>
	 *         True if the ICEObjects are equal, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

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
				&& Arrays.equals(this.rotation, other.rotation)
				&& Arrays.equals(this.translation, other.translation));

	}

	/**
	 * <p>
	 * Copies the contents of a Transformation into the current object using a
	 * deep copy
	 * </p>
	 * 
	 * @param iceObject
	 *            <p>
	 *            The ICEObject from which the values should be copied
	 *            </p>
	 */
	public void copy(Transformation iceObject) {

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

	}

	/**
	 * <p>
	 * Returns a clone of the Transformation using a deep copy.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The new clone
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Create a new Transformation and copy the current object's data

		Transformation transformation = new Transformation();
		transformation.copy(this);
		return transformation;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateable#register(org.eclipse.eavp.viz.service.datastructures
	 * .VizObject.IManagedVizUpdateableListener)
	 */
	@Override
	public void register(IManagedUpdateableListener listener) {
		updateManager.register(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateable#unregister(org.eclipse.eavp.viz.service.
	 * datastructures.VizObject.IManagedVizUpdateableListener)
	 */
	@Override
	public void unregister(IManagedUpdateableListener listener) {
		updateManager.unregister(listener);

	}

}