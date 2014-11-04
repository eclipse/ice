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

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Implements a number of operations shared between the components in the Shape
 * composite pattern
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlSeeAlso({ PrimitiveShape.class, ComplexShape.class })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractShape extends ICEObject implements IShape {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Stores the list of keys for the property list
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Keys")
	private ArrayList<String> keys;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Stores the list of values for the property list
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Values")
	private ArrayList<String> values;
	/**
	 * <!-- begin-UML-doc -->
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
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "Transformation")
	protected Transformation transformation;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * If applicable, the parent of the shape in the CSG tree
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	private IShape parent;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the transformation matrix, creates the array containing the
	 * key/value property pairs, and creates a listeners list
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AbstractShape() {
		// begin-user-code

		// Initialize transformation
		transformation = new Transformation();

		// Create properties lists
		keys = new ArrayList<String>();
		values = new ArrayList<String>();

		// Create listeners list
		listeners = new ArrayList<IUpdateableListener>();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a copy of the transformation matrix associated with this shape
	 * node
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The transformation matrix applied to this node in the CSG tree
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Transformation getTransformation() {
		// begin-user-code
		return this.transformation;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Replaces the transformation matrix with a copy of the given Matrix4x4
	 * </p>
	 * <p>
	 * Returns whether the setting was successful
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param transformation
	 *            <p>
	 *            The transformation matrix to be applied to the shape
	 *            </p>
	 * @return <p>
	 *         True if setting the transformation was successful, false
	 *         otherwise
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setTransformation(Transformation transformation) {
		// begin-user-code

		// Fail if null and return false
		if (transformation == null) {
			return false;
		}

		// Otherwise set and return true

		this.transformation = transformation;

		// Notify listeners and return success
		notifyListeners();
		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the value associated with the property key
	 * </p>
	 * <p>
	 * If the key does not exist, this operation returns null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param key
	 *            <p>
	 *            The key corresponding to the desired value
	 *            </p>
	 * @return <p>
	 *         The value associated with the property key
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the property value associated with the key string
	 * </p>
	 * <p>
	 * If the key does not yet exist, append the key/value pair to the end of
	 * the property list. If it exists, find and replace the property value with
	 * the new one.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setProperty(String key, String value) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the value associated with the key in the properties list
	 * </p>
	 * <p>
	 * This operation returns whether the key was found and removed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param key
	 *            <p>
	 *            The key associated with the value to remove
	 *            </p>
	 * @return <p>
	 *         True if the value was found and removed, false otherwise
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeProperty(String key) {
		// begin-user-code
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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hashcode value of the shape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode of the object
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		// Get initial hash code
		int hash = super.hashCode();

		// Hash the transformation
		hash = 31 * hash + transformation.hashCode();

		// Hash the properties
		hash = 31 * hash + keys.hashCode();
		hash = 31 * hash + values.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this shape and another
	 * shape. It returns true if the shape are equal and false if they are not.
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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Copies the contents of a shape into the current object using a deep copy
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
	public void copy(AbstractShape iceObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the Component to call back to an IComponentVisitor
	 * so that the visitor can perform its required actions for the exact type
	 * of the Component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor querying the type of the object
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IComponentVisitor visitor) {
		// begin-user-code

		// Call the visitor's visit operation
		// This is okay to do so in the AbstractClass because it is only
		// required that the visit() call reveals its type as an IShape

		visitor.visit(this);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of the shape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	protected void notifyListeners() {
		// begin-user-code

		// Let the base class handle most of the notifications
		super.notifyListeners();

		// Notify the parent's listeners
		if (parent != null) {
			((AbstractShape) parent).notifyListeners();
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the parent associated with this shape, or null if the shape does
	 * not have a parent
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The parent of the shape
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IShape getParent() {
		// begin-user-code
		return parent;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the parent of the IShape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param parent
	 *            <p>
	 *            The parent of the IShape
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setParent(IShape parent) {
		// begin-user-code
		this.parent = parent;
		// end-user-code
	}
}