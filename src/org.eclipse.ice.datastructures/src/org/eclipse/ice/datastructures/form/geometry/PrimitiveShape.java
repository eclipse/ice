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
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Represents a mathematically simple solid with no child shapes
 * </p>
 * <p>
 * This is the leaf node of the composite pattern consisting of the ComplexShape
 * and PrimitiveShape
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "PrimitiveShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrimitiveShape extends AbstractShape {
	/**
	 * <p>
	 * The type of shape of this PrimitiveShape
	 * </p>
	 * 
	 */
	@XmlAttribute
	private ShapeType shapeType;

	/**
	 * <p>
	 * Calls AbstractShape's constructor and initializes the ShapeType
	 * </p>
	 * <p>
	 * Upon creation, the associated ShapeType is set to None and must be reset
	 * appropriately in order for the PrimitiveShape to have an effect on a
	 * generated mesh.
	 * </p>
	 * 
	 */
	public PrimitiveShape() {

		// Call AbstractShape's constructor

		super();

		// Set the ShapeType to None

		shapeType = ShapeType.None;

	}

	/**
	 * <p>
	 * Calls AbstractShape's constructor and initializes the ShapeType
	 * </p>
	 * <p>
	 * When this constructor is called, the ShapeType enumerator is set to the
	 * given value.
	 * </p>
	 * 
	 * @param shapeType
	 *            <p>
	 *            The type of shape to be set in this new PrimitiveShape
	 *            </p>
	 */
	public PrimitiveShape(ShapeType shapeType) {

		// Call nullery constructor first
		this();

		// Set the type to the given value
		if (shapeType == null) {
			shapeType = ShapeType.None;
		}
		this.shapeType = shapeType;

	}

	/**
	 * <p>
	 * Sets the type of shape of the PrimitiveShape
	 * </p>
	 * <p>
	 * If the shape type has previously been set, this operation ignores
	 * additional calls to this function. That is, the shape type is permitted
	 * to change only if the current shape type is None.
	 * </p>
	 * 
	 * @param shapeType
	 *            <p>
	 *            The type of shape to set on this PrimitiveShape
	 *            </p>
	 */
	public void setType(ShapeType shapeType) {

		if (shapeType == null) {
			return;
		}
		// Set the type only if the current ShapeType is None
		if (this.shapeType == ShapeType.None) {
			this.shapeType = shapeType;

			// Notify listeners
			notifyListeners();
		}

	}

	/**
	 * <p>
	 * Gets the ShapeType of the PrimitiveShape
	 * </p>
	 * 
	 * @return <p>
	 *         The ShapeType corresponding to this PrimitiveShape
	 *         </p>
	 */
	public ShapeType getType() {
		return shapeType;
	}

	/**
	 * <p>
	 * This operation is used to check equality between the PrimitiveShape and
	 * another PrimitiveShape. It returns true if the PrimitiveShapes are equal
	 * and false if they are not.
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
	public boolean equals(Object otherObject) {

		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof PrimitiveShape)) {
			return false;
		}
		// Check that these objects have the same ICEObject data
		if (!super.equals(otherObject)) {
			return false;
		}
		// At this point, other object must be a PrimitiveShape, so cast it
		PrimitiveShape otherPrimitiveShape = (PrimitiveShape) otherObject;

		// Check for unequal types
		if (shapeType != otherPrimitiveShape.shapeType) {
			return false;
		}
		// The two objects are equal
		return true;

	}

	/**
	 * <p>
	 * This operation copies the contents of a PrimitiveShape into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param iceObject
	 *            <p>
	 *            The ICEObject from which the values should be copied.
	 *            </p>
	 */
	public void copy(PrimitiveShape iceObject) {

		// Return if object is null
		if (iceObject == null) {
			return;
		}
		// Copy the ICEObject data
		super.copy(iceObject);

		// Copy shapeType
		this.shapeType = iceObject.shapeType;

	}

	/**
	 * <p>
	 * This operation returns a clone of the PrimitiveShape using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	public Object clone() {

		// Create a new PrimitiveShape
		PrimitiveShape primitiveShape = new PrimitiveShape();

		// Copy `this` into primitiveShape
		primitiveShape.copy(this);

		return primitiveShape;

	}

	/**
	 * <p>
	 * This operation returns the hashcode value of the PrimitiveShape.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the ICEObject.
	 *         </p>
	 */
	public int hashCode() {
		// Get initial hash code
		int hash = super.hashCode();

		// Hash the ShapeType
		hash = 31 * hash + shapeType.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * This operation returns a clone of the PrimitiveShape using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	public void update(String updatedKey, String newValue) {
		// Not implemented
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#acceptShapeVisitor(IShapeVisitor visitor)
	 */
	public void acceptShapeVisitor(IShapeVisitor visitor) {

		// Only visit if it is not null
		if (visitor != null) {
			visitor.visit(this);
		}
	}
}