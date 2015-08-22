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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;

/**
 * <p>
 * This is a class for custom 2D shapes that can be manually constructed via the
 * Mesh Editor. A shape comprises several polygons that together form a single
 * mesh.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@XmlRootElement(name = "Custom2DShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class Custom2DShape extends ICEObject {
	/**
	 * <p>
	 * The collection of polygons composing the Custom2DShape.
	 * </p>
	 * 
	 */
	@XmlElementRefs(value = { @XmlElementRef(name = "Polygon", type = Polygon.class) })
	private ArrayList<Polygon> polygons;

	/**
	 * <p>
	 * The default constructor.
	 * </p>
	 * 
	 */
	public Custom2DShape() {

		// Call AbstractShape's constructor
		super();

		// Initialize the array.
		polygons = new ArrayList<Polygon>();

		return;
	}

	/**
	 * <p>
	 * Gets the collection of polygons composing the Custom2DShape.
	 * </p>
	 * 
	 * @return <p>
	 *         The collection of Polygon2Ds composing a Custom2DShape.
	 *         </p>
	 */
	public ArrayList<Polygon> getPolygons() {

		// Return a copy of the list.
		return new ArrayList<Polygon>(polygons);
	}

	/**
	 * <p>
	 * Sets the collection of polygons composing the Custom2DShape.
	 * </p>
	 * 
	 * @param polygons
	 *            <p>
	 *            The collection of Polygon2Ds that will compose the
	 *            Custom2DShape.
	 *            </p>
	 */
	public void setPolygons(ArrayList<Polygon> polygons) {

		// Add all polygons if the list is not null.
		if (polygons != null) {
			for (Polygon polygon : polygons) {
				addPolygon(polygon);
			}
		}

		// Notifying listeners is handled by addPolygon().

		return;
	}

	/**
	 * <p>
	 * Adds a Polygon to the Custom2DShape.
	 * </p>
	 * 
	 * @param polygon
	 *            <p>
	 *            The polygon that will be added to the Custom2DShape.
	 *            </p>
	 */
	public void addPolygon(Polygon polygon) {

		// Store the polygon if it was not already in the shape.
		if (polygon != null && !polygons.contains(polygon)) {
			polygons.add(polygon);

			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * Removes a Polygon from the Custom2DShape.
	 * </p>
	 * 
	 * @param polygon
	 *            <p>
	 *            The polygon that will be removed from the Custom2DShape.
	 *            </p>
	 */
	public void removePolygon(Polygon polygon) {

		// Remove the polygon from the list.
		if (polygons.remove(polygon)) {
			// Notify listeners of the change.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the Custom2DShape.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the Object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Get initial hash code
		int hash = super.hashCode();

		// Hash each polygon in the polygons list
		for (Polygon polygon : polygons) {
			hash = 31 * hash + polygon.hashCode();
		}

		return hash;

	}

	/**
	 * <p>
	 * This operation is used to check equality between this Custom2DShape and
	 * another Custom2DShape. It returns true if the Custom2DShapes are equal
	 * and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the Objects are equal, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

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

	}

	/**
	 * <p>
	 * This operation copies the contents of a Custom2DShape into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The Object from which the values should be copied.
	 *            </p>
	 */
	public void copy(Custom2DShape otherObject) {

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
	}

	/**
	 * <p>
	 * This operation returns a clone of the Custom2DShape using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Create a new Custom2DShape
		Custom2DShape custom2DShape = new Custom2DShape();

		// Copy `this` into custom2DShape
		custom2DShape.copy(this);

		return custom2DShape;

	}

	/*
	 * Overrides a super class method.
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// Not implemented
		return;

	}

}