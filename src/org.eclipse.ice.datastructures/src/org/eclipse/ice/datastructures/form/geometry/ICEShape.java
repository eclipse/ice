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
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.viz.service.jme3.shapes.AbstractShape;
import org.eclipse.ice.viz.service.jme3.shapes.ComplexShape;
import org.eclipse.ice.viz.service.jme3.shapes.IShape;
import org.eclipse.ice.viz.service.jme3.shapes.OperatorType;
import org.eclipse.ice.viz.service.jme3.shapes.PrimitiveShape;
import org.eclipse.ice.viz.service.jme3.shapes.ShapeType;
import org.eclipse.ice.viz.service.jme3.shapes.Transformation;

/**
 * This is a wrapper class around an IShape object. It delegates almost all work
 * to the underlying IShape, providing the IShape's capabilities to ICE. It
 * provides a way to interact with both PrimitiveShapes and ComplexShapes, and,
 * like a ComplexShape, can maintain a list of child IShapes.
 * 
 * @author Robert Smith
 *
 */
@XmlRootElement(name = "ICEShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class ICEShape extends ICEObject implements Component, IUpdateable,
		IUpdateableListener {

	/**
	 * The wrapped IShape.
	 */
	@XmlAnyElement()
	@XmlElementRefs(value = {
			@XmlElementRef(name = "ComplexShape", type = ComplexShape.class),
			@XmlElementRef(name = "PrimitiveShape", type = PrimitiveShape.class) })
	private IShape shape;

	/**
	 * The ICEShape which holds the IShape that is the parent to this object's
	 * wrapped IShape.
	 */
	@XmlTransient
	private ICEShape parent;

	/**
	 * The list of ICEShapes containing the IShapes which are the wrapped
	 * IShape's chidlren.
	 */
	@XmlAnyElement()
	@XmlElementRef(name = "ICEShape", type = ICEShape.class)
	private ArrayList<ICEShape> children;

	/**
	 * Accessor method for the wrapped IShape.
	 * 
	 * @return The wrapped IShape
	 */
	public IShape getShape() {
		return shape;
	}

	/**
	 * Mutator method for the wrapped IShape.
	 * 
	 * @param newShape
	 */
	public void setShape(IShape newShape) {
		shape = newShape;

		// A change has been made, notify listeners.
		notifyListeners();
	}

	/**
	 * Nullary constructor. Instantiates the object with empty data.
	 */
	public ICEShape() {
		shape = null;
		parent = null;
		children = new ArrayList<ICEShape>();
	}

	/**
	 * A constructor which initializes the object to hold a PrimitiveShape.
	 * 
	 * @param shapeType
	 *            The ShapeType for the PrimitiveShape this object will wrap.
	 */
	public ICEShape(ShapeType shapeType) {
		if (shapeType == null) {
			shapeType = ShapeType.None;
		}
		shape = new PrimitiveShape(ShapeType.values()[shapeType.ordinal()]);
		parent = null;
		children = new ArrayList<ICEShape>();
	}

	/**
	 * A constructor which initializes the object to hold a ComplexShape.
	 * 
	 * @param operatorType
	 *            The OperatorType for the ComplexShape this object will wrap.
	 */
	public ICEShape(OperatorType operatorType) {
		if (operatorType == null) {
			operatorType = OperatorType.None;
		}
		shape = new ComplexShape(OperatorType.values()[operatorType.ordinal()]);
		parent = null;
		children = new ArrayList<ICEShape>();

	}

	/**
	 * Accessor method for the wrapped shape's transformation.
	 * 
	 * @return The Transformation of the wrapped shape.
	 */
	public Transformation getTransformation() {
		return shape.getTransformation();
	}

	/**
	 * Mutator method for the wrapped shape's transformation.
	 * 
	 * @param newTransformation
	 *            The new Transformation for the wrapped shape.
	 * @return Whether or not the Transformation was succesfully set.
	 */
	public boolean setTransformation(Transformation newTransformation) {

		if (newTransformation != null) {
			// A change is being made, notify listeners
			boolean success = shape.setTransformation(newTransformation);
			notifyListeners();
			return success;
		}

		// If passed a null reference, return false.
		return false;
	}

	/**
	 * Accessor method for the wrapped shape's properties.
	 * 
	 * @param key
	 *            The key for the property to access.
	 * @return The property associated with the given key.
	 */
	public String getProperty(String key) {
		return shape.getProperty(key);
	}

	/**
	 * Mutator method for the wrapped shape's properties.
	 * 
	 * @param key
	 *            The key for the property to set
	 * @param value
	 *            The value to set the specified property to
	 * @return Whether or not the property was succesfully set
	 */
	public boolean setProperty(String key, String value) {

		// A change is being made, notify listeners.
		notifyListeners();
		return shape.setProperty(key, value);
	}

	/**
	 * Remove a property from the wrapped shape's list of properties.
	 * 
	 * @param key
	 *            The key of the property to remove
	 * @return Whether or not the property was succesfully removed.
	 */
	public boolean removeProperty(String key) {
		return shape.removeProperty(key);
	}

	/**
	 * Calculate a has code for this object.
	 */
	public int hashCode() {
		int hash = 31 * shape.hashCode();
		return hash;
	}

	/**
	 * Determine if this object is equal to another.
	 */
	public boolean equals(Object otherObject) {
		// Check if a similar reference
		if (this == otherObject) {
			return true;
		}
		// Check that the other object is not null and an instance of the
		// PrimitiveShape
		if (otherObject == null || !(otherObject instanceof ICEShape)) {
			return false;
		}

		// At this point, other object must be a PrimitiveShape, so cast it
		ICEShape iceShape = (ICEShape) otherObject;

		// Check if the wrapped IShapes are equal.
		if (!shape.equals(iceShape.getShape())) {
			return false;
		}

		return true;
	}

	/**
	 * Copy the contents of the given ICEShape into this object.
	 * 
	 * @param otherShape
	 *            The ICEShape to be copied into this object
	 */
	public void copy(ICEShape otherShape) {
		super.copy(otherShape);
		if (otherShape != null) {

			// Invoke the correct copy method
			if (!otherShape.isComplex()) {

				shape = new PrimitiveShape(ShapeType.None);

				PrimitiveShape abstractShape = (PrimitiveShape) otherShape
						.getShape();
				((PrimitiveShape) shape).copy(abstractShape);
			} else {

				shape = new ComplexShape(OperatorType.None);

				ComplexShape abstractShape = (ComplexShape) otherShape
						.getShape();
				((ComplexShape) shape).copy(abstractShape);

				// Copy the list of children
				children.clear();
				ArrayList<ICEShape> tempChildren = otherShape.getChildren();
				if (!tempChildren.isEmpty()) {
					for (ICEShape tempChild : tempChildren) {
						addShape((ICEShape) tempChild.clone());
					}
				}

				parent = otherShape.getShapeParent();

			}
			notifyListeners();
		}

	}

	public ArrayList<IUpdateableListener> getListeners() {
		return listeners;
	}

	/**
	 * Accessor method for the list of child shapes.
	 * 
	 * @return The list of ICEShapes which contain the IShapes which are the
	 *         children of this object's wrapped IShapes
	 */
	public ArrayList<ICEShape> getChildren() {
		return children;
	}

	/**
	 * Accesor method for the parent ICEShape
	 * 
	 * @return The ICEShape wrapping the IShape which is this object's wrapped
	 *         IShape's parent
	 */
	public ICEShape getShapeParent() {
		return parent;
	}

	/**
	 * Mutator method for the parent ICEShape
	 * 
	 * @param newParent
	 *            The ICEShape that wraps the IShape to be this object's wrapped
	 *            IShape's new parent.
	 */
	protected void setParent(ICEShape newParent) {

		if (newParent != null) {
			((AbstractShape) shape).setParent(newParent.getShape());
		} else {
			((AbstractShape) shape).setParent(null);
		}
		parent = newParent;

		// A change has been made, notify listeners
		notifyListeners();
	}

	/**
	 * Return a copy of this object.
	 */
	public Object clone() {
		ICEShape newShape = new ICEShape();
		newShape.copy(this);
		return newShape;
	}

	/**
	 * Add a new IShape to the wrapped IShape's list of children.
	 * 
	 * @param newShape
	 *            The ICEShape wrapping the new child IShape
	 */
	public void addShape(ICEShape newShape) {

		// Only ComplexShapes can have children
		if (isComplex() && newShape != this) {

			// Add the IShape to the wrapped IShape
			((ComplexShape) shape).addShape(newShape.getShape());

			// Add the ICEShape to this.
			newShape.setParent(this);
			newShape.register(this);
			children.add(newShape);

			// A change has been made, notify listeners
			notifyListeners();
		}
	}

	/**
	 * Checks if the wrapped shape is a ComplexShape.
	 * 
	 * @return Whether or not the wrapped shape is a ComplexShape
	 */
	public boolean isComplex() {
		if (!(shape instanceof ComplexShape)) {
			return false;
		}

		return true;
	}

	/**
	 * Remove a child shape from the wrapped IShape
	 * 
	 * @param oldShape
	 *            The ICEShape wrapping the IShape to be removed.
	 */
	public void removeShape(ICEShape oldShape) {

		// Only ComplexShapes can have children
		if (isComplex()) {

			// Remove the child IShape from the wrapped IShape
			((ComplexShape) shape).removeShape(oldShape.getShape());

			// Remove the child ICEShape from this.
			children.remove(oldShape);
			oldShape.unregister(this);
			oldShape.setParent(null);

			// A change has been made, notify the listeners.
			notifyListeners();
		}

	}

	/**
	 * Accessor method for the list of child shapes.
	 * 
	 * @return The list of ICEShapes containing the wrapped IShape's children.
	 */
	public ArrayList<ICEShape> getShapes() {
		return children;
	}

	/**
	 * Mutator method for the list of child shapes.
	 * 
	 * @param newChildren
	 *            The list of ICEShapes containing the new children
	 */
	public void setShapes(ArrayList<ICEShape> newChildren) {

		// Only ComplexShapes can have children
		if (isComplex()) {
			ArrayList<IShape> shapes = new ArrayList<IShape>();
			for (ICEShape iceShape : newChildren) {
				shapes.add(iceShape.getShape());
				iceShape.register(this);
			}
			((ComplexShape) shape).setShapes(shapes);

			for (ICEShape child : children) {
				child.unregister(this);
			}
			children = newChildren;

			// A change has been made, notify listeners
		}
		notifyListeners();
	}

	/**
	 * Accessor method for the wrapped shape's ShapeType
	 * 
	 * @return The wrapped PrimitiveShape's ShapeType or null if the wrapped
	 *         shape is a ComplexShape
	 */
	public ShapeType getShapeType() {
		if (!isComplex() && shape != null) {
			return ((PrimitiveShape) shape).getType();
		} else
			return null;
	}

	/**
	 * Accessor method for the wrapped shape's OperatorType
	 * 
	 * @return The wrapped ComplexShape's OperatorType or null if the wrapped
	 *         shape is a PrimitiveShape
	 */
	public OperatorType getOperatorType() {
		if (isComplex() && shape != null) {
			return ((ComplexShape) shape).getType();
		} else
			return null;
	}

	/**
	 * Mutator method for the wrapped shape's ShapeType
	 * 
	 * @param type
	 *            The ShapeType to change the wrapped PrimitiveShape to
	 */
	public void setShapeType(ShapeType type) {
		// Fail silently for ComplexShapes, which don't have ShapeTypes.
		if (!isComplex()) {
			((PrimitiveShape) shape).setType(type);

			// A change has been made, notify listeners
			notifyListeners();
		}
	}

	/**
	 * Mutator method for the wrapped shape's OperatorType
	 * 
	 * @param type
	 *            The OperatorType to change the wrapped ComplexShape to
	 */
	public void setOperatorType(OperatorType type) {
		// Fail silently for PrimitiveShapes, which don't have OperatorTypes.
		if (isComplex()) {
			((ComplexShape) shape).setType(type);

			// A change has been made, notify listeners
			notifyListeners();
		}
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

		final ICEShape iceShape = this;

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
					listeners.get(i).update(iceShape);
				}
			}
		};

		// Start the thread
		notifyThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Component#accept(org.eclipse
	 * .ice.datastructures.componentVisitor.IComponentVisitor)
	 */
	@Override
	public void accept(IComponentVisitor visitor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(IUpdateable component) {
		// TODO Auto-generated method stub
		notifyListeners();

	}

}
