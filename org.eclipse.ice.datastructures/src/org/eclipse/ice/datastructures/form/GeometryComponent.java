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
package org.eclipse.ice.datastructures.form;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.form.geometry.ICEGeometry;

/**
 * <p>
 * Composite container for a Geometry along with any additional information
 * required to interpret the geometry data
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "GeometryComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeometryComponent extends ICEObject implements Component,
		IUpdateableListener {
	/**
	 * <p>
	 * The set of ComponentListeners observing the GeometryComponent
	 * </p>
	 * 
	 */
	@XmlTransient
	private ArrayList<IUpdateableListener> listeners;

	/**
	 * The Geometry managed by the GeometryComponent
	 */
	@XmlAnyElement()
	@XmlElementRef(name = "ICEGeometry", type = ICEGeometry.class)
	private ICEGeometry geometry;

	/**
	 * <p>
	 * This operation overrides the ICEObject.setName() operation and provides
	 * an update notification in addition to setting the name.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The new ICEObject ID
	 *            </p>
	 */
	@Override
	public void setName(String name) {

		// Call ICEObject::setName
		super.setName(name);

		// Notify listeners
		notifyListeners();

	}

	/**
	 * <p>
	 * This operation overrides the ICEObject.setId() operation and provides an
	 * update notification in addition to setting the id.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The new ICEObject ID
	 *            </p>
	 */
	@Override
	public void setId(int id) {

		// Call ICEObject::setId
		super.setId(id);

		// Notify listeners
		notifyListeners();

	}

	/**
	 * <p>
	 * Creates an empty Geometry and list of IShapes and ComponentListeners
	 * </p>
	 * 
	 */
	public GeometryComponent() {

		// Create a new Geometry and register as its listener.
		geometry = new ICEGeometry();
		geometry.register(this);

		// Create a new listeners list
		listeners = new ArrayList<IUpdateableListener>();

	}

	/**
	 * Accessor method for the held Geometry.
	 * 
	 * @return The held Geometry
	 */
	public ICEGeometry getGeometry() {
		return geometry;
	}

	/**
	 * Mutator method for the held geometry
	 * 
	 * @param newGeometry
	 *            the new Geometry to hold
	 */
	public void setGeometry(ICEGeometry newGeometry) {
		geometry = newGeometry;

		// Register self as a listener for the geometry
		geometry.register(this);

		// Notify listeners
		notifyListeners();
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

		// Return the ICEObject's hashcode
		int hash = super.hashCode();

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
		if (otherObject == null || !(otherObject instanceof GeometryComponent)) {
			return false;
		}
		// Check that these objects have the same ICEObject data
		if (!super.equals(otherObject)) {
			return false;
		}
		// At this point, other object must be a PrimitiveShape, so cast it
		GeometryComponent geometryComponent = (GeometryComponent) otherObject;

		// Check for equal number of shapes in shapes list
		if (!this.geometry.equals(geometryComponent.geometry)) {
			return false;
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
	public void copy(GeometryComponent iceObject) {

		// Return if object is null
		if (iceObject == null) {
			return;
		}
		// Copy the ICEObject data
		super.copy(iceObject);

		// Copy shapes list
		this.setGeometry((ICEGeometry) iceObject.getGeometry().clone());
		// this.geometry.copy(iceObject.getGeometry());

		this.notifyListeners();

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
		GeometryComponent geometryComponent = new GeometryComponent();

		// Return the copied GeometryComponent
		geometryComponent.copy(this);
		return geometryComponent;

	}

	/**
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of this GeometryComponent
	 * </p>
	 * 
	 */
	@Override
	protected void notifyListeners() {

		final GeometryComponent geometryComponent = this;

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
					listeners.get(i).update(geometryComponent);
				}
			}
		};

		// Start the thread
		notifyThread.start();

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// Not implemented
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#register(IUpdateableListener listener)
	 */
	@Override
	public void register(IUpdateableListener listener) {

		// Fail silently if listener is null
		if (listener == null) {
			return;
		}
		// Add listener to listeners list
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor visitor)
	 */
	@Override
	public void accept(IComponentVisitor visitor) {

		// Call the visitor's visit operation
		visitor.visit(this);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org.eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		// If the component is an IShape, we're receiving an event from one of
		// our Geometry's children.
		if (component instanceof ICEGeometry) {
			notifyListeners();
		}
	}
}