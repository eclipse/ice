/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.geometry;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A class which manages a collection of shapes for its parent
 * GeometryComponent. It is registered as a listener for each IShape and the
 * parent GeometryComponent is registered as a listener of this class.
 * 
 * @author Robert Smith
 *
 */

@XmlRootElement(name = "Geometry")
@XmlAccessorType(XmlAccessType.FIELD)
public class Geometry implements IVizUpdateable, IVizUpdateableListener {

	/**
	 * <p>
	 * The list of shapes referenced by the GeometryComponent container
	 * </p>
	 * 
	 */
	@XmlAnyElement()
	@XmlElementRefs(value = {
			@XmlElementRef(name = "ComplexShape", type = ComplexShape.class),
			@XmlElementRef(name = "PrimitiveShape", type = PrimitiveShape.class) })
	private ArrayList<IShape> shapes;

	/**
	 * The list of objects registered as listeners to this one.
	 */
	@XmlTransient
	private ArrayList<IVizUpdateableListener> listeners;

	/**
	 * The IUpdateable description
	 */
	String description;

	/**
	 * The IUpdateable ID
	 */
	int id;

	/**
	 * The IUpdateable name
	 */
	String name;

	/**
	 * <p>
	 * Creates empty lists of IShapes and IUpdateableListeners
	 * </p>
	 * 
	 */
	public Geometry() {
		// Create new shapes and listeners lists
		shapes = new ArrayList<IShape>();

		listeners = new ArrayList<IVizUpdateableListener>();
	}

	/**
	 * <p>
	 * Adds an IShape to the shape list
	 * </p>
	 * 
	 * @param shape
	 *            <p>
	 *            The new shape to be added to the existing list
	 *            </p>
	 */
	public void addShape(IShape shape) {

		// Ignore null
		if (shape == null) {
			return;
		}
		// Register the parent GeometryComponent as a listener
		shape.register(this);

		// Add the shape to the shapes list
		shapes.add(shape);

		// Notify the listeners
		update(shape);

	}

	/**
	 * <p>
	 * Removes the given IShape if it exists in the shape list
	 * </p>
	 * 
	 * @param shape
	 *            <p>
	 *            The IShape reference to be removed from the shapes list
	 *            </p>
	 */
	public void removeShape(IShape shape) {

		// Ignore null
		if (shape == null) {
			return;
		}
		// Remove the shape from the shapes list if it exists
		if (shapes.remove(shape)) {

			// Notify listeners if a change was made
			update(shape);
		}

	}

	/**
	 * <p>
	 * Returns a list of all IShapes stored in the shapes list
	 * </p>
	 * 
	 * @return <p>
	 *         The list of shapes contained in this GeometryComponent container
	 *         </p>
	 */
	public ArrayList<IShape> getShapes() {

		// Simply return a reference to the shapes list
		return this.shapes;

	}

	/**
	 * 
	 * @param shapes
	 *            <p>
	 *            The shapes list to replace the existing shapes list
	 *            </p>
	 */
	public void setShapes(ArrayList<IShape> shapes) {

		// If null, quietly fail
		if (shapes == null) {
			return;
		}

		// Tracks whether the parent component needs to be alerted to a change
		boolean updateComponent = false;

		// If shapes were already present, there has been a change
		if (shapes.size() > 0) {
			updateComponent = true;
		}

		// Replace the reference to the shapes list
		this.shapes = shapes;

		for (IShape shape : shapes) {
			shape.register(this);

			// At least one shape has been added
			updateComponent = true;
		}

		// A change has been made, notify the listeners
		if (updateComponent) {
			update(new PrimitiveShape());
		}

	}

	/**
	 * <p>
	 * This operation returns the hashcode value of the GeometryComponent.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the ICEObject.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Start with the ICEObject's hashcode
		int hash = super.hashCode();

		// Hash the list
		for (IShape shape : shapes) {
			hash = 31 * hash + shape.hashCode();
		}

		return hash;

	}

	/**
	 * <p>
	 * This operation is used to check equality between this GeometryComponent
	 * and another GeometryComponent. It returns true if the GeometryComponents
	 * are equal and false if they are not.
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
		// GeometryComponent
		if (otherObject == null || !(otherObject instanceof Geometry)) {
			return false;
		}

		// At this point, other object must be a PrimitiveShape, so cast it
		Geometry geometry = (Geometry) otherObject;

		// Check for equal number of shapes in shapes list
		if (this.shapes.size() != geometry.shapes.size()) {
			return false;
		}
		// Check for equal elements in shapes list
		// The list must be ordered similarly for this test to pass.
		int numShapes = shapes.size();
		for (int index = 0; index < numShapes; index++) {

			// Check for equal shapes in the current index
			if (!shapes.get(index).equals(geometry.shapes.get(index))) {
				return false;
			}
		}

		// The two shapes are equal
		return true;

	}

	/**
	 * <p>
	 * This operation copies the contents of a GeometryComponent into the
	 * current object using a deep copy.
	 * </p>
	 * 
	 * @param iceObject
	 *            <p>
	 *            The ICEObject from which the values should be copied
	 *            </p>
	 */
	public void copy(Geometry iceObject) {

		// Return if object is null
		if (iceObject == null) {
			return;
		}

		// Copy listeners list
		this.listeners.clear();

		for (IVizUpdateableListener listener : iceObject.listeners) {
			this.listeners.add(listener);
		}

		// Tracks whether the parent component needs to be alerted to a change
		boolean updateComponent = false;

		// If there were shapes that will be overwritten, a change has been made
		if (shapes.size() > 0) {
			updateComponent = true;
		}

		// Copy shapes list
		this.shapes.clear();

		for (IShape shape : iceObject.shapes) {
			this.shapes.add((IShape) shape.clone());

			// At least one shape has been added
			updateComponent = true;
		}

		// A change has been made, notify the listeners
		if (updateComponent) {
			update(new PrimitiveShape());
		}

	}

	/**
	 * <p>
	 * This operation returns a clone of the GeometryComponent using a deep
	 * copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Instantiate GeometryComponent
		Geometry geometry = new Geometry();

		// Return the copied GeometryComponent
		geometry.copy(this);
		return geometry;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {
		final Geometry geometry = this;

		// If the listeners are empty, return
		if (this.listeners == null || this.listeners.isEmpty()) {
			return;
		}
		// Create a thread object that notifies all listeners

		Thread notifyThread = new Thread() {

			@Override
			public void run() {
				// Loop over all listeners and update them
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).update(geometry);
				}
			}
		};

		// Start the thread
		notifyThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setId(int)
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#setName(java.lang
	 * .String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#setDescription(
	 * java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateable#update(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// Not implemented

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateable#register(org.eclipse
	 * .ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void register(IVizUpdateableListener listener) {
		listeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateable#unregister(org.eclipse
	 * .ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void unregister(IVizUpdateableListener listener) {
		listeners.remove(listener);

	}

}
