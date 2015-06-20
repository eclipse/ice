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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.Composite;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

/**
 * <p>
 * The LWRComposite class represents all reactor components that can store and
 * manage LWRComponents. This class implements the ICE Composite interface. This
 * class was designed as a "branch" within the Reactor package to hold
 * references to other LWRComponents. Although this class implements the
 * Composite interface, classes that extend LWRComposite should consider if they
 * will need to override the Composite Interface's operations to provide
 * specific utility as needed.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRComposite extends LWRComponent implements Composite {
	/**
	 * <p>
	 * A Hashtable keyed on LWRComponent name storing unique LWRComponents.
	 * </p>
	 * 
	 */
	@XmlTransient
	protected Hashtable<String, LWRComponent> lWRComponents;

	/**
	 * <p>
	 * The Constructor.
	 * </p>
	 * 
	 */
	public LWRComposite() {

		// Setup the composite
		this.name = "Composite 1";
		this.id = 1;
		this.description = "Composite 1's Description";

		// Setup hashTable
		this.lWRComponents = new Hashtable<String, LWRComponent>();

		// Setup the LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRCOMPOSITE;

	}

	/**
	 * <p>
	 * Returns the LWRComponent corresponding to the provided name or null if
	 * the name is not found.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The provided LWRComponent's name.
	 *            </p>
	 * @return <p>
	 *         The LWRComponent corresponding to the provided name or null if
	 *         the name is not found.
	 *         </p>
	 */
	public LWRComponent getComponent(String name) {

		// if the name is not null, return the name
		if (name != null) {
			return this.lWRComponents.get(name);
		}

		// Name was null, return
		return null;
	}

	/**
	 * <p>
	 * Returns an ArrayList of Strings containing the names of all LWRComponents
	 * contained in this LWRComposite.
	 * </p>
	 * 
	 * @return <p>
	 *         An ArrayList of Strings containing the names of all LWRComponents
	 *         contained in this LWRComposite
	 *         </p>
	 */
	public ArrayList<String> getComponentNames() {
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
	}

	/**
	 * <p>
	 * Removes a LWRComponent with the provided name from this LWRComposite.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of the LWRComponent to remove.
	 *            </p>
	 */
	public void removeComponent(String name) {
		// Local Declarations

		// IF the name is not null and it is contained in the list (trimmed),
		// then remove it
		if (name != null && this.lWRComponents.containsKey(name)
				&& !name.trim().isEmpty()) {
			this.lWRComponents.remove(name);
		}
	}

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

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

	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Get the super's hashCode
		int hash = super.hashCode();

		// Hash the hashTable
		hash += 31 * this.lWRComponents.hashCode();

		// Return the hashCode
		return hash;

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 */
	public void copy(LWRComposite otherObject) {

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

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Local Declarations
		LWRComposite composite = new LWRComposite();

		// Copy contents
		composite.copy(this);

		// Return the newly instantiated object
		return composite;

	}

	/**
	 * Gets the children of this instance that can be written to the HDF format.
	 * 
	 * @return A list containing the writable children.
	 */
	@Override
	public ArrayList<IHdfWriteable> getWriteableChildren() {

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
	}

	/**
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * 
	 * @param iHdfReadable
	 *            The HDF child of this instance.
	 * @return True if the child could be read and added, false otherwise.
	 */
	@Override
	public boolean readChild(IHdfReadable iHdfReadable) {

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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#addComponent(Component child)
	 */
	@Override
	public void addComponent(Component child) {

		// If the child is null or if the name is already in the table, return
		if (child == null || this.lWRComponents.containsKey(child.getName())
				|| !(child instanceof LWRComponent)) {
			return;
		}

		// Otherwise, add component
		this.lWRComponents.put(child.getName(), (LWRComponent) child);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#removeComponent(int childId)
	 */
	@Override
	public void removeComponent(int childId) {
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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#getComponent(int childId)
	 */
	@Override
	public Component getComponent(int childId) {
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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#getNumberOfComponents()
	 */
	@Override
	public int getNumberOfComponents() {

		// Return the size
		return this.lWRComponents.size();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Composite#getComponents()
	 */
	@Override
	public ArrayList<Component> getComponents() {
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

	}
}