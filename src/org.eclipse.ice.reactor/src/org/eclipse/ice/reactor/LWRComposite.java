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
package org.eclipse.ice.reactor;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.Composite;

import java.util.Hashtable;
import java.util.ArrayList;

import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The LWRComposite class represents all reactor components that can store and
 * manage LWRComponents. This class implements the ICE Composite interface. This
 * class was designed as a "branch" within the Reactor package to hold
 * references to other LWRComponents. Although this class implements the
 * Composite interface, classes that extend LWRComposite should consider if they
 * will need to override the Composite Interface's operations to provide
 * specific utility as needed.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRComposite extends LWRComponent implements Composite {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A Hashtable keyed on LWRComponent name storing unique LWRComponents.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected Hashtable<String, LWRComponent> lWRComponents;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRComposite() {
		// begin-user-code

		// Setup the composite
		this.name = "Composite 1";
		this.id = 1;
		this.description = "Composite 1's Description";

		// Setup hashTable
		this.lWRComponents = new Hashtable<String, LWRComponent>();

		// Setup the LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRCOMPOSITE;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the LWRComponent corresponding to the provided name or null if
	 * the name is not found.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The provided LWRComponent's name.
	 *            </p>
	 * @return <p>
	 *         The LWRComponent corresponding to the provided name or null if
	 *         the name is not found.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRComponent getComponent(String name) {
		// begin-user-code

		// if the name is not null, return the name
		if (name != null) {
			return this.lWRComponents.get(name);
		}

		// Name was null, return
		return null;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an ArrayList of Strings containing the names of all LWRComponents
	 * contained in this LWRComposite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         An ArrayList of Strings containing the names of all LWRComponents
	 *         contained in this LWRComposite
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getComponentNames() {
		// begin-user-code
		// Local Declarations
		ArrayList<String> keys = new ArrayList<String>();
		Iterator<String> iter;
		String keyName;

		// Setup iterator
		iter = this.lWRComponents.keySet().iterator();

		// Iterate over the list and get the component
		// Only grab the first in the list
		while (iter.hasNext()) {

			// Iterate the next piece
			keyName = iter.next();
			// Add each string key to the list
			keys.add(keyName);
		}

		// Return the names
		return keys;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes a LWRComponent with the provided name from this LWRComposite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name of the LWRComponent to remove.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(String name) {
		// begin-user-code
		// Local Declarations

		// IF the name is not null and it is contained in the list (trimmed),
		// then remove it
		if (name != null && this.lWRComponents.containsKey(name)
				&& !name.trim().isEmpty()) {
			this.lWRComponents.remove(name);
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Local declarations
		LWRComposite composite;
		boolean retVal = false;

		// If this object is an instance of the LWRComponent, cast it.
		// Make sure it is also not null
		if (otherObject != null && otherObject instanceof LWRComposite) {
			composite = (LWRComposite) otherObject;

			// If they are on the same heap, return true
			if (this == otherObject) {
				return true;
			}
			// Check values
			retVal = (super.equals(otherObject) && this.lWRComponents
					.equals(composite.lWRComponents));

		}

		// Return retVal
		return retVal;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Get the super's hashCode
		int hash = super.hashCode();

		// Hash the hashTable
		hash += 31 * this.lWRComponents.hashCode();

		// Return the hashCode
		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(LWRComposite otherObject) {
		// begin-user-code

		// Local declarations
		Iterator<String> iter;

		// If the object is null, return
		if (otherObject == null) {
			return;
		}
		// Copy super contents
		super.copy(otherObject);

		// Copy the HashTable's contents
		this.lWRComponents.clear();
		iter = otherObject.lWRComponents.keySet().iterator();

		while (iter.hasNext()) {
			String key = iter.next();
			this.lWRComponents.put(key,
					(LWRComponent) otherObject.lWRComponents.get(key));

		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		LWRComposite composite = new LWRComposite();

		// Copy contents
		composite.copy(this);

		// Return the newly instantiated object
		return composite;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren() {
		// begin-user-code

		// Get the children in super
		ArrayList<IHdfWriteable> children = super.getWriteableChildren();

		// If super had no children
		if (children == null) {

			// Initialize to new array list
			children = new ArrayList<IHdfWriteable>();
		}

		// Add all of these children
		children.addAll(lWRComponents.values());

		return children;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iHdfReadable
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readChild(IHdfReadable iHdfReadable) {
		// begin-user-code

		// If the child is null or not an instance of LWRComponent, then return
		// false.
		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// Remove any child with the new child's name from this LWRComposite
		this.removeComponent(childComponent.getName());

		// Add the new child LWRComponent
		this.addComponent(childComponent);

		// Return true for success
		return true;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#addComponent(Component child)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addComponent(Component child) {
		// begin-user-code

		// If the child is null or if the name is already in the table, return
		if (child == null || this.lWRComponents.containsKey(child.getName())
				|| !(child instanceof LWRComponent)) {
			return;
		}

		// Otherwise, add component
		this.lWRComponents.put(child.getName(), (LWRComponent) child);

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#removeComponent(int childId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(int childId) {
		// begin-user-code
		// Local declarations
		Component component = null;
		Iterator<LWRComponent> iter;

		// Setup iterator
		iter = this.lWRComponents.values().iterator();

		// Iterate over the list and get the component
		// Only grab the first in the list
		while (iter.hasNext()) {

			// Iterate the next piece
			component = iter.next();

			// If the component's id matches the required child's id, remove the
			// component
			if (component.getId() == childId) {
				this.removeComponent(component.getName());
			}
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#getComponent(int childId)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Component getComponent(int childId) {
		// begin-user-code
		// Local declarations
		Component component = null;
		Iterator<LWRComponent> iter;

		// Setup iterator
		iter = this.lWRComponents.values().iterator();

		// Iterate over the list and get the component
		// Only grab the first in the list
		while (iter.hasNext()) {

			// Iterate the next piece
			component = iter.next();

			// If the component's id matches the required child's id, return the
			// component
			if (component.getId() == childId) {
				return component;
			}
		}

		// Nothing was found!
		return null;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#getNumberOfComponents()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfComponents() {
		// begin-user-code

		// Return the size
		return this.lWRComponents.size();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#getComponents()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Component> getComponents() {
		// begin-user-code
		// Local Declarations
		ArrayList<Component> components = new ArrayList<Component>();
		Component component;
		Iterator<LWRComponent> iter;

		// Setup iterator
		iter = this.lWRComponents.values().iterator();

		// Iterate over the list and get the component
		// Only grab the first in the list
		while (iter.hasNext()) {

			// Iterate the next piece
			component = iter.next();
			// Add the component to the list
			components.add((Component) component);
		}

		// Return the list
		return components;

		// end-user-code
	}
}