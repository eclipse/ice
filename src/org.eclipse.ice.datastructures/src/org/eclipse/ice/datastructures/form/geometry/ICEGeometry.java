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
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/

package org.eclipse.ice.datastructures.form.geometry;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.viz.service.geometry.shapes.Geometry;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;

/**
 * This is a wrapper class around a Geometry object. It delegates almost all
 * work to the underlying Geometry, providing the Geometry's capabilities to
 * ICE. Like a Geometry, it contains a number of IShapes, here similarly wrapped
 * in ICEShape objects.
 * 
 * @author Robert Smith
 * @author Jay Jay Billings
 *
 */

@XmlRootElement(name = "Geometry")
@XmlAccessorType(XmlAccessType.FIELD)
public class ICEGeometry implements IUpdateable, IUpdateableListener {

	/**
	 * The held Geometry object.
	 */
	@XmlAnyElement()
	@XmlElementRef(name = "Geometry", type = Geometry.class)
	Geometry geometry;

	/**
	 * The list of ICEShapes which hold the IShapes corresponding to this object's Geometry's children.
	 */
	@XmlAnyElement()
	@XmlElementRef(name = "ICEShape", type = ICEShape.class)
	ArrayList<ICEShape> shapes;

	/**
	 * The listeners registered to listen to this object.
	 */
	@XmlTransient
	ArrayList<IUpdateableListener> listeners;

	/**
	 * Instantiates each class variable to be empty.
	 */
	public ICEGeometry() {
		//Create empty geometry, list of child shapes, and list of listeners.
		geometry = new Geometry();

		shapes = new ArrayList<ICEShape>();

		listeners = new ArrayList<IUpdateableListener>();
	}

	/**
	 * Accessor method for Geometry.
	 * 
	 * @return This class's held geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Add an ICEShape as a child to this object, and also add that ICEShape's wrapped IShape as a child to the wrapped Geometry.
	 * 
	 * @param shape The shape to be added as a child.
	 */
	public void addShape(ICEShape shape) {
		
		//Fail silently if passed a null reference
		if (shape != null) {
			geometry.addShape(shape.getShape());
			shapes.add(shape);
			shape.register(this);
			
			//A change has been made, notify listeners
			notifyListeners();
		}
	}

	/**
	 * Remove an ICEShape as a child to this object, and also remove that ICEShape's wrapped IShape as a child to the wrapped Geometry. 
	 * 
	 * @param shape The shape to be removed from the list of children.
	 */
	public void removeShape(ICEShape shape) {
		
		//Fail silently if passed a null reference
		if (shape != null) {
			geometry.removeShape(shape.getShape());
			shapes.remove(shape);
			
			//Unregister as the shape's listener
			shape.unregister(this);
			
			//A change has been made, notify listeners
			notifyListeners();
		}
	}

	/**
	 * Accessor method for the list of child shapes.
	 * 
	 * @return The list of child shapes
	 */
	public ArrayList<ICEShape> getShapes() {
		return shapes;
	}

	/**
	 * Set the list of child shapes to the list passed in. Also, set the wrapped Geometry's list of children to the list of IShapes held by the new list of ICEShapes. 
	 * 
	 * @param newShapes The new list of child shapes
	 */
	public void setShapes(ArrayList<ICEShape> newShapes) {

		//Unregister from old shapes
		for (ICEShape iceShape : shapes){
			iceShape.unregister(this);
		}
		
		//Register as a listener for all the new ICEShapes and create a list of all their wrapped IShapes
		ArrayList<IShape> ishapes = new ArrayList<IShape>();
		for (ICEShape iceShape : newShapes) {
			iceShape.register(this);
			ishapes.add(iceShape.getShape());
		}
		
		//Set the held geometry's list of child shapes.
		geometry.setShapes(ishapes);
		
		//Replace the list of child shapes
		shapes = newShapes;
		
		//A change has been made, notify listeners
		notifyListeners();
	}

	/**
	 * Calculate a hashcode for this object
	 */
	public int hashCode() {
		//Sum the has for all held objects.
		int hash = geometry.hashCode();
		for (ICEShape shape : shapes) {
			hash += shape.hashCode();
		}
		return hash;
	}

	/**
	 * Test if this object equals another
	 */
	public boolean equals(Object otherObject) {
		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof ICEGeometry)) {
			return false;
		}

		// At this point, other object must be a PrimitiveShape, so cast it
		ICEGeometry iceGeometry = (ICEGeometry) otherObject;

		//Check if the held Geometries are equal. 
		if (!geometry.equals(iceGeometry.getGeometry())) {
			return false;
		}

		return true;
	}

	/**
	 * Copy the contents of the given ICEGeometry into this object.
	 * 
	 * @param otherGeometry The ICEGeometry to be copied into this object
	 */
	public void copy(ICEGeometry otherGeometry) {
		//Copy the other Geometry object.
		geometry.copy(otherGeometry.getGeometry());

		//Unregister old shapes
		for (ICEShape shape : shapes){
			shape.unregister(this);
		}
		
		//Copy the list of child shapes. 
		shapes.clear();
		
		for (ICEShape shape : otherGeometry.getShapes()) {
			shapes.add((ICEShape) shape.clone());
		}

		//Register as a listener for all the new child shapes.
		for (ICEShape shape : shapes) {
			shape.register(this);
		}

		//A change has been made, notify listeners
		notifyListeners();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setId(int)
	 */
	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getId()
	 */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#register(org.eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void register(IUpdateableListener listener) {
		listeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#unregister(org.eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void unregister(IUpdateableListener listener) {
		listeners.remove(listener);

	}

	/**
	 * Return a copy of this object
	 */
	public Object clone() {
		ICEGeometry newGeometry = new ICEGeometry();
		newGeometry.copy(this);
		return newGeometry;
	}

	/**
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of this GeometryComponent
	 * </p>
	 * 
	 */
	protected void notifyListeners() {

		final ICEGeometry geometry = this;

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
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org.eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {
		//Notify listeners only if updated by ICEShape
		if (component instanceof ICEShape) {
			notifyListeners();
		}

	}

}
