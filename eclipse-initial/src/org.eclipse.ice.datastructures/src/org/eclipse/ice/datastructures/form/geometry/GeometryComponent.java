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
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Composite container for ComplexShapes and PrimitiveShapes along with any
 * additional information required to interpret the geometry data
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@Entity()
@Table(name = "GeometryComponent")
@XmlRootElement(name = "GeometryComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeometryComponent extends ICEObject implements Component,
		IUpdateableListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The set of ComponentListeners observing the shape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	@Transient
	private ArrayList<IUpdateableListener> listeners;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of shapes referenced by the GeometryComponent container
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, targetEntity = AbstractShape.class)
	@XmlAnyElement()
	@XmlElementRefs(value = {
			@XmlElementRef(name = "ComplexShape", type = ComplexShape.class),
			@XmlElementRef(name = "PrimitiveShape", type = PrimitiveShape.class) })
	private ArrayList<IShape> shapes;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the ICEObject.setName() operation and provides
	 * an update notification in addition to setting the name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The new ICEObject ID
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setName(String name) {
		// begin-user-code

		// Call ICEObject::setName
		super.setName(name);

		// Notify listeners
		notifyListeners();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the ICEObject.setId() operation and provides an
	 * update notification in addition to setting the id.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            <p>
	 *            The new ICEObject ID
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setId(int id) {
		// begin-user-code

		// Call ICEObject::setId
		super.setId(id);

		// Notify listeners
		notifyListeners();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates an empty list of IShapes and ComponentListeners
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GeometryComponent() {
		// begin-user-code

		// Create a new shapes list
		shapes = new ArrayList<IShape>();

		// Create a new listeners list
		listeners = new ArrayList<IUpdateableListener>();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds an IShape to the shape list
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param shape
	 *            <p>
	 *            The new shape to be added to the existing list
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addShape(IShape shape) {
		// begin-user-code

		// Ignore null
		if (shape == null) {
			return;
		}
		// Register this GeometryComponent as a listener
		shape.register(this);

		// Add the shape to the shapes list
		shapes.add(shape);

		// Celebrate the addition of the new shape!
		notifyListeners();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the given IShape if it exists in the shape list
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param shape
	 *            <p>
	 *            The IShape reference to be removed from the shapes list
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeShape(IShape shape) {
		// begin-user-code

		// Ignore null
		if (shape == null) {
			return;
		}
		// Remove the shape from the shapes list if it exists
		if (shapes.remove(shape)) {

			// Notify listeners if a change was made
			notifyListeners();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a list of all IShapes stored in the shapes list
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of shapes contained in this GeometryComponent container
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IShape> getShapes() {
		// begin-user-code

		// Simply return a reference to the shapes list
		return this.shapes;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param shapes
	 *            <p>
	 *            The shapes list to replace the existing shapes list
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setShapes(ArrayList<IShape> shapes) {
		// begin-user-code

		// If null, quietly fail
		if (shapes == null) {
			return;
		}
		// Replace the reference to the shapes list
		this.shapes = shapes;

		for (IShape shape : shapes) {
			shape.register(this);
		}

		// Notify listeners
		notifyListeners();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the GeometryComponent from persistent storage as
	 * XML. This operation will throw an IOException if it fails.
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
			this.copy((GeometryComponent) dataObject);
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
	 * This operation returns the hashcode value of the GeometryComponent.
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

		// Start with the ICEObject's hashcode
		int hash = super.hashCode();

		// Hash the list
		for (IShape shape : shapes) {
			hash = 31 * hash + shape.hashCode();
		}

		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to check equality between this GeometryComponent
	 * and another GeometryComponent. It returns true if the GeometryComponents
	 * are equal and false if they are not.
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
		if (this.shapes.size() != geometryComponent.shapes.size()) {
			return false;
		}
		// Check for equal elements in shapes list
		// The list must be ordered similarly for this test to pass.
		int numShapes = shapes.size();
		for (int index = 0; index < numShapes; index++) {

			// Check for equal shapes in the current index
			if (!shapes.get(index).equals(geometryComponent.shapes.get(index))) {
				return false;
			}
		}

		// The two shapes are equal
		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation copies the contents of a GeometryComponent into the
	 * current object using a deep copy.
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
	public void copy(GeometryComponent iceObject) {
		// begin-user-code

		// Return if object is null
		if (iceObject == null) {
			return;
		}
		// Copy the ICEObject data
		super.copy(iceObject);

		// Copy shapes list
		this.shapes.clear();

		for (IShape shape : iceObject.shapes) {
			this.shapes.add((IShape) shape.clone());
		}

		this.notifyListeners();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a clone of the GeometryComponent using a deep
	 * copy.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The new clone
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Instantiate GeometryComponent
		GeometryComponent geometryComponent = new GeometryComponent();

		// Return the copied GeometryComponent
		geometryComponent.copy(this);
		return geometryComponent;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Notifies all IUpdateableListeners in the listener list that an event has
	 * occurred which has changed the state of this GeometryComponent
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void notifyListeners() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#register(IUpdateableListener listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void register(IUpdateableListener listener) {
		// begin-user-code

		// Fail silently if listener is null
		if (listener == null) {
			return;
		}
		// Add listener to listeners list
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor visitor)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(IComponentVisitor visitor) {
		// begin-user-code

		// Call the visitor's visit operation

		visitor.visit(this);

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateableListener#update(Component component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(IUpdateable component) {
		// begin-user-code

		// If the component is an IShape, we're receiving an event from one of
		// our children.

		if (component instanceof IShape) {
			notifyListeners();
		}
		// end-user-code
	}
}