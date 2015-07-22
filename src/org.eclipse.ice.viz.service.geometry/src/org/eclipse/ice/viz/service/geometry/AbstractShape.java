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
package org.eclipse.ice.viz.service.geometry;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * Implements a number of operations shared between the components in the Shape
 * composite pattern
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlSeeAlso({ PrimitiveShape.class, ComplexShape.class })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractShape extends VizObject implements IShape {
	/**
	 * <p>
	 * Stores the list of keys for the property list
	 * </p>
	 * 
	 */
	@XmlElement(name = "Keys")
	private ArrayList<String> keys;
	/**
	 * <p>
	 * Stores the list of values for the property list
	 * </p>
	 * 
	 */
	@XmlElement(name = "Values")
	private ArrayList<String> values;
	/**
	 * <p>
	 * The matrix transformation applied to the shape
	 * </p>
	 * <p>
	 * In the case of a ComplexShape, each transformation affects its child
	 * shapes, so in order to calculate the final transformation of an
	 * individual PrimitiveShape, you must take the matrix product of all of the
	 * ancestors' transformations.
	 * </p>
	 * <p>
	 * The transformation matrix applied to this node in the CSG tree
	 * </p>
	 * 
	 */
	@XmlElement(name = "Transformation")
	protected Transformation transformation;

	/**
	 * <p>
	 * If applicable, the parent of the shape in the CSG tree
	 * </p>
	 * 
	 */
	@XmlTransient
	private IShape parent;

	/**
	 * <p>
	 * Initializes the transformation matrix, creates the array containing the
	 * key/value property pairs, and creates a listeners list
	 * </p>
	 * 
	 */
	public AbstractShape() {

		// Initialize transformation
		transformation = new Transformation();

		// Create properties lists
		keys = new ArrayList<String>();
		values = new ArrayList<String>();

		// Create listeners list
		listeners = new ArrayList<IVizUpdateableListener>();

	}

	/**
	 * <p>
	 * Returns a copy of the transformation matrix associated with this shape
	 * node
	 * </p>
	 * 
	 * @return <p>
	 *         The transformation matrix applied to this node in the CSG tree
	 *         </p>
	 */
	@Override
	public Transformation getTransformation() {
		return this.transformation;
	}

	/**
	 * <p>
	 * Replaces the transformation matrix with a copy of the given Matrix4x4
	 * </p>
	 * <p>
	 * Returns whether the setting was successful
	 * </p>
	 * 
	 * @param transformation
	 *            <p>
	 *            The transformation matrix to be applied to the shape
	 *            </p>
	 * @return <p>
	 *         True if setting the transformation was successful, false
	 *         otherwise
	 *         </p>
	 */
	@Override
	public boolean setTransformation(Transformation transformation) {

		// Fail if null and return false
		if (transformation == null) {
			return false;
		}

		// Otherwise set and return true

		this.transformation = transformation;

		// Notify listeners and return success
		notifyListeners();
		return true;

	}

	/**
	 * <p>
	 * Returns the value associated with the property key
	 * </p>
	 * <p>
	 * If the key does not exist, this operation returns null.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key corresponding to the desired value
	 *            </p>
	 * @return <p>
	 *         The value associated with the property key
	 *         </p>
	 */
	@Override
	public String getProperty(String key) {

		if (key == null || "".equals(key)) {
			return null;
		}

		// Get index of key
		int index = keys.indexOf(key);

		if (index < 0) {
			return null;
		}

		// Return the value
		try {
			return values.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

	}

	/**
	 * <p>
	 * Sets the property value associated with the key string
	 * </p>
	 * <p>
	 * If the key does not yet exist, append the key/value pair to the end of
	 * the property list. If it exists, find and replace the property value with
	 * the new one.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The new key
	 *            </p>
	 * @param value
	 *            <p>
	 *            The new value
	 *            </p>
	 * @return <p>
	 *         True if the property setting is valid, false otherwise
	 *         </p>
	 */
	@Override
	public boolean setProperty(String key, String value) {

		// Validate parameters
		if (key == null || "".equals(key) || value == null) {
			return false;
		}
		// Find index of key
		int index = keys.indexOf(key);

		// Update

		if (index < 0) {
			// Insert new value

			keys.add(key);
			values.add(value);
		} else {
			// Modify the value

			values.set(index, value);
		}

		// Notify listeners and return success
		notifyListeners();
		return true;

	}

	/**
	 * <p>
	 * Removes the value associated with the key in the properties list
	 * </p>
	 * <p>
	 * This operation returns whether the key was found and removed.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key associated with the value to remove
	 *            </p>
	 * @return <p>
	 *         True if the value was found and removed, false otherwise
	 *         </p>
	 */
	@Override
	public boolean removeProperty(String key) {
		// Validate parameters

		if (key == null) {
			return false;
		}
		// Get index of key

		int index = keys.indexOf(key);

		if (index < 0) {
			return false;
		}
		keys.remove(index);
		values.remove(index);

		// Notify listeners and return success
		notifyListeners();
		return true;
	}

	/**
	 * <p>
	 * This operation returns the hashcode value of the shape.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the object
	 *         </p>
	 */
	@Override
	public int hashCode() {
		// Get initial hash code
		int hash = super.hashCode();

		// Hash the transformation
		hash = 31 * hash + transformation.hashCode();

		// Hash the properties
		hash = 31 * hash + keys.hashCode();
		hash = 31 * hash + values.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this shape and another
	 * shape. It returns true if the shape are equal and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other ICEObject that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the ICEObjects are equal, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof AbstractShape)) {
			return false;
		}
		// Check that these objects have the same ICEObject data
		if (!super.equals(otherObject)) {
			return false;
		}

		// At this point, other object must be a PrimitiveShape, so cast it
		AbstractShape abstractShape = (AbstractShape) otherObject;

		// Check for unequal transformation
		if (!transformation.equals(abstractShape.transformation)) {
			return false;
		}
		// Check for unequal properties
		if (!keys.equals(abstractShape.keys)
				|| !values.equals(abstractShape.values)) {
			return false;
		}
		// The two objects are equal
		return true;

	}

	/**
	 * <p>
	 * Copies the contents of a shape into the current object using a deep copy
	 * </p>
	 * 
	 * @param iceObject
	 *            <p>
	 *            The ICEObject from which the values should be copied
	 *            </p>
	 */
	public void copy(AbstractShape iceObject) {

		// Return if object is null
		if (iceObject == null) {
			return;
		}
		// Copy the ICEObject data
		super.copy(iceObject);

		// Copy transformation
		this.transformation = (Transformation) iceObject.transformation.clone();

		// Copy properties

		this.keys.clear();
		this.values.clear();

		for (String key : iceObject.keys) {
			this.keys.add(key);
		}

		for (String value : iceObject.values) {
			this.values.add(value);
		}

		this.notifyListeners();

	}

	/**
	 * <p>
	 * This operation directs the Component to call back to an IComponentVisitor
	 * so that the visitor can perform its required actions for the exact type
	 * of the Component.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor querying the type of the object
	 *            </p>
	 */
	public void accept(IShapeVisitor visitor) {
		
	}

	/**
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of the shape
	 * </p>
	 * 
	 */
	@Override
	protected void notifyListeners() {

		// Let the base class handle most of the notifications
		super.notifyListeners();

		// Notify the parent's listeners
		if (parent != null) {
			((AbstractShape) parent).notifyListeners();
		}
	}

	/**
	 * <p>
	 * Returns the parent associated with this shape, or null if the shape does
	 * not have a parent
	 * </p>
	 * 
	 * @return <p>
	 *         The parent of the shape
	 *         </p>
	 */
	@Override
	public IShape getParent() {
		return parent;
	}

	/**
	 * <p>
	 * Sets the parent of the IShape
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The parent of the IShape
	 *            </p>
	 */
	public void setParent(IShape parent) {
		this.parent = parent;
	}
}