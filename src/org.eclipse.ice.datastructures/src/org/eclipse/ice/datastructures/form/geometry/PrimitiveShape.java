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

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Represents a mathematically simple solid with no child shapes
 * </p>
 * <p>
 * This is the leaf node of the composite pattern consisting of the ComplexShape
 * and PrimitiveShape
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "PrimitiveShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrimitiveShape extends AbstractShape {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The type of shape of this PrimitiveShape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	private ShapeType shapeType;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Calls AbstractShape's constructor and initializes the ShapeType
	 * </p>
	 * <p>
	 * Upon creation, the associated ShapeType is set to None and must be reset
	 * appropriately in order for the PrimitiveShape to have an effect on a
	 * generated mesh.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PrimitiveShape() {
		// begin-user-code

		// Call AbstractShape's constructor

		super();

		// Set the ShapeType to None

		shapeType = ShapeType.None;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Calls AbstractShape's constructor and initializes the ShapeType
	 * </p>
	 * <p>
	 * When this constructor is called, the ShapeType enumerator is set to the
	 * given value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param shapeType
	 *            <p>
	 *            The type of shape to be set in this new PrimitiveShape
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PrimitiveShape(ShapeType shapeType) {
		// begin-user-code

		// Call nullery constructor first
		this();

		// Set the type to the given value
		if (shapeType == null) {
			shapeType = ShapeType.None;
		}
		this.shapeType = shapeType;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the type of shape of the PrimitiveShape
	 * </p>
	 * <p>
	 * If the shape type has previously been set, this operation ignores
	 * additional calls to this function. That is, the shape type is permitted
	 * to change only if the current shape type is None.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param shapeType
	 *            <p>
	 *            The type of shape to set on this PrimitiveShape
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setType(ShapeType shapeType) {
		// begin-user-code

		if (shapeType == null) {
			return;
		}
		// Set the type only if the current ShapeType is None
		if (this.shapeType == ShapeType.None) {
			this.shapeType = shapeType;

			// Notify listeners
			notifyListeners();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the ShapeType of the PrimitiveShape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The ShapeType corresponding to this PrimitiveShape
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ShapeType getType() {
		// begin-user-code
		return shapeType;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the PrimitiveShape from persistent storage as XML.
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
		jaxbManipulator = new ICEJAXBManipulator();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream
		Object dataObject;
		try {
			dataObject = jaxbManipulator.read(this.getClass(), inputStream);
			// Copy contents of new object into current data structure
			this.copy((PrimitiveShape) dataObject);
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
	 * This operation is used to check equality between the PrimitiveShape and
	 * another PrimitiveShape. It returns true if the PrimitiveShapes are equal
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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a PrimitiveShape into the current
	 * object using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iceObject
	 *            <p>
	 *            The ICEObject from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(PrimitiveShape iceObject) {
		// begin-user-code

		// Return if object is null
		if (iceObject == null) {
			return;
		}
		// Copy the ICEObject data
		super.copy(iceObject);

		// Copy shapeType
		this.shapeType = iceObject.shapeType;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the PrimitiveShape using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Create a new PrimitiveShape
		PrimitiveShape primitiveShape = new PrimitiveShape();

		// Copy `this` into primitiveShape
		primitiveShape.copy(this);

		return primitiveShape;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hashcode value of the PrimitiveShape.
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
		// Get initial hash code
		int hash = super.hashCode();

		// Hash the ShapeType
		hash = 31 * hash + shapeType.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the PrimitiveShape using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(String updatedKey, String newValue) {
		// begin-user-code
		// Not implemented
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IShape#acceptShapeVisitor(IShapeVisitor visitor)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void acceptShapeVisitor(IShapeVisitor visitor) {
		// begin-user-code

		// Only visit if it is not null
		if (visitor != null) {
			visitor.visit(this);
		}
		// end-user-code
	}
}