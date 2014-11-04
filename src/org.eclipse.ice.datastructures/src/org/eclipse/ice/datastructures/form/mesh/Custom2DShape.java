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
package org.eclipse.ice.datastructures.form.mesh;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;
import org.eclipse.ice.datastructures.form.geometry.AbstractShape;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a class for custom 2D shapes that can be manually constructed via the
 * Mesh Editor. A shape comprises several polygons that together form a single
 * mesh.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "Custom2DShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class Custom2DShape extends AbstractShape {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The collection of polygons composing the Custom2DShape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElementRefs(value = { @XmlElementRef(name = "Polygon", type = Polygon.class) })
	private ArrayList<Polygon> polygons;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Custom2DShape() {
		// begin-user-code

		// Call AbstractShape's constructor
		super();

		// Initialize the array.
		polygons = new ArrayList<Polygon>();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets the collection of polygons composing the Custom2DShape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The collection of Polygon2Ds composing a Custom2DShape.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Polygon> getPolygons() {
		// begin-user-code

		// Return a copy of the list.
		return new ArrayList<Polygon>(polygons);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the collection of polygons composing the Custom2DShape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param polygons
	 *            <p>
	 *            The collection of Polygon2Ds that will compose the
	 *            Custom2DShape.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPolygons(ArrayList<Polygon> polygons) {
		// begin-user-code

		// Add all polygons if the list is not null.
		if (polygons != null) {
			for (Polygon polygon : polygons) {
				addPolygon(polygon);
			}
		}

		// Notifying listeners is handled by addPolygon().

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds a Polygon to the Custom2DShape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param polygon
	 *            <p>
	 *            The polygon that will be added to the Custom2DShape.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addPolygon(Polygon polygon) {
		// begin-user-code

		// Store the polygon if it was not already in the shape.
		if (polygon != null && !polygons.contains(polygon)) {
			polygons.add(polygon);

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes a Polygon from the Custom2DShape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param polygon
	 *            <p>
	 *            The polygon that will be removed from the Custom2DShape.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removePolygon(Polygon polygon) {
		// begin-user-code

		// Remove the polygon from the list.
		if (polygons.remove(polygon)) {
			// Notify listeners of the change.
			notifyListeners();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the Custom2DShape from persistent storage as XML.
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
			this.copy((Custom2DShape) dataObject);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hash value of the Custom2DShape.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the Object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Get initial hash code
		int hash = super.hashCode();

		// Hash each polygon in the polygons list
		for (Polygon polygon : polygons) {
			hash = 31 * hash + polygon.hashCode();
		}

		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this Custom2DShape and
	 * another Custom2DShape. It returns true if the Custom2DShapes are equal
	 * and false if they are not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the Objects are equal, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		boolean equal = false;

		// Check the reference.
		if (this == otherObject) {
			equal = true;
		}
		// Check that the other object is not null and an instance of the
		// Custom2DShape
		else if (otherObject != null && otherObject instanceof Custom2DShape) {

			// Cast to Custom2DShape
			Custom2DShape otherCustom2DShape = (Custom2DShape) otherObject;

			// Check that these objects have the same AbstractShape data
			// and equal polygon lists
			equal = (super.equals(otherObject) && polygons
					.equals(otherCustom2DShape.polygons));
		}

		return equal;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a Custom2DShape into the current
	 * object using a deep copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Custom2DShape otherObject) {
		// begin-user-code

		// Return if object is null
		if (otherObject == null) {
			return;
		}
		// Copy the AbstractShape data
		super.copy(otherObject);

		// Copy the polygons
		this.polygons.clear();
		for (Polygon polygon : otherObject.getPolygons()) {
			this.polygons.add((Polygon) polygon.clone());
		}

		// Notify listeners of the change.
		notifyListeners();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the Custom2DShape using a deep copy.
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

		// Create a new Custom2DShape
		Custom2DShape custom2DShape = new Custom2DShape();

		// Copy `this` into custom2DShape
		custom2DShape.copy(this);

		return custom2DShape;

		// end-user-code
	}

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
		return;

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

		return;

		// end-user-code
	}
}