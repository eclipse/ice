/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.sfr.base;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.Composite;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * <p>
 * The SFRComposite class represents all reactor components that can store and
 * manage SFRComponents. This class implements the ICE Composite interface. This
 * class was designed as a "branch" within the Reactor package to hold
 * references to other SFRComponents.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFRComposite extends SFRComponent implements Composite {
	/**
	 * <!-- begin-UML-doc --> Hashtable of all SFRComponents contained in the
	 * SFRComposite, keyed by name. <!-- end-UML-doc -->
	 * 
	 */
	private Hashtable<String, SFRComponent> sFRComponents;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public SFRComposite() {

		// Call the super constructor.
		super();

		// Set up the default values.
		setName("Composite 1");
		setId(1);
		setDescription("Composite 1's Description");

		// Initialize the list of child sFRComponents.
		sFRComponents = new Hashtable<String, SFRComponent>();

		return;
	}

	/**
	 * <p>
	 * Returns the SFRComponent of the specified name
	 * </p>
	 * 
	 * @param name
	 *            Name of the SFRComponent. Cannot be null.
	 * @return The SFRComponent matching the specified name. Will return a null
	 *         component if no match to name was found.
	 */
	public SFRComponent getComponent(String name) {

		// Initialize the default return value.
		SFRComponent component = null;

		// Try to look up the component based on the name.
		if (name != null) {
			component = sFRComponents.get(name);
		}
		// Return the component.
		return component;
	}

	/**
	 * <p>
	 * Returns a string ArrayList of all SFRComponent names contained in the
	 * SFRComposite.
	 * </p>
	 * 
	 * @return ArrayList of string names representing all SFRComponents in the
	 *         SFRComposite.
	 */
	public ArrayList<String> getComponentNames() {

		// Initialize the List of component names.
		ArrayList<String> componentNames = new ArrayList<String>();

		// Loop over the keys in the component Hashtable and add them to the
		// list.
		for (String componentName : sFRComponents.keySet()) {
			componentNames.add(componentName);
		}

		// Return the names.
		return componentNames;
	}

	/**
	 * <p>
	 * Removes the component with the specified name.
	 * </p>
	 * 
	 * @param name
	 *            Name of the SFRComponent to be removed.
	 */
	public void removeComponent(String name) {

		// Hashtables do not accept null keys, so remove the component if name
		// is not null. We do not need to check if the key is in the table.
		if (name != null) {
			sFRComponents.remove(name);
		}
		return;
	}

	/**
	 * <!-- begin-UML-doc --> Adds the specified Component to the SFRComposite.
	 * 
	 * @param child
	 *            The child component to add to the SFRComposite. Cannot be
	 *            null.
	 * @see Composite#addComponent(Component child)
	 */
	public void addComponent(Component child) {

		// We only want to add the child if it is a non-null SFRComponent and a
		// component with the same name is not in the Hashtable.

		// Make sure the child is a non-null SFRComponent.
		if (child != null && child instanceof SFRComponent) {

			// Get the name of the child.
			String key = child.getName();

			// If the name is not already a key in the table, add the child.
			if (!sFRComponents.containsKey(key)) {
				sFRComponents.put(key, (SFRComponent) child);
			}
		}

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Removes the component specified by the ID, from
	 * the SFRComposite. <!-- end-UML-doc -->
	 * 
	 * @param childId
	 *            The ID of the component to the removed.
	 * @see Composite#removeComponent(int childId)
	 */
	public void removeComponent(int childId) {

		// FIXME - Currently, it is possible to have multiple Components with
		// the same ID in the Hashtable which is keyed on the components' names.
		// This means we have to loop through the entire list of components!

		// Declare the component for iteration in the loop below.
		SFRComponent component;

		// Set up the iterator for looping over the child component values.
		Iterator<SFRComponent> iter = sFRComponents.values().iterator();

		// Iterate over the child components.
		while (iter.hasNext()) {

			// Increment the iterator.
			component = iter.next();

			// Remove components with a matching ID.
			if (childId == component.getId()) {
				removeComponent(component.getName());
			}
		}

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the SFRComponent of the specified ID, from
	 * the SFRComposite.
	 * 
	 * @param childId
	 *            The ID of the SFRComponent to be returned.
	 * @return The SFRComponent matching the specified ID. If no match is found,
	 *         returns a null SFRComponent.
	 * @see Composite#getComponent(int childId)
	 */
	public Component getComponent(int childId) {

		// Declare the component for iteration in the loop below.
		SFRComponent component;

		// Set up the iterator for looping over the child component values.
		Iterator<SFRComponent> iter = sFRComponents.values().iterator();

		// Iterate over the child components.
		while (iter.hasNext()) {

			// Increment the iterator.
			component = iter.next();

			// Return the first component with a matching ID.
			if (childId == component.getId()) {
				return component;
			}
		}

		// If the code reaches this point, then no matching component was found.
		return null;
	}

	/**
	 * <!-- begin-UML-doc --> Returns the number of SFRComponents contained in
	 * the SFRComposite, as an integer. <!-- end-UML-doc -->
	 * 
	 * @return The number of components in the composite.
	 * @see Composite#getNumberOfComponents()
	 */
	public int getNumberOfComponents() {

		// Return the size of the component table.
		return sFRComponents.size();
	}

	/**
	 * <!-- begin-UML-doc --> Returns an ArrayList of SFRComponents contained in
	 * the SFRComposite. <!-- end-UML-doc -->
	 * 
	 * @return An ArrayList of components contained in the composite. Returns an
	 *         empty ArrayList if the composite was empty.
	 * @see Composite#getComponents()
	 */
	public ArrayList<Component> getComponents() {

		// Initialize the return value, an ArrayList of components.
		ArrayList<Component> components = new ArrayList<Component>();

		// Add all of the child components to the List.
		for (SFRComponent component : sFRComponents.values()) {
			components.add(component);
		}

		// Return the ArrayList.
		return components;
	}

	/**
	 * <p>
	 * Compares the contents of objects and returns true if they are identical,
	 * otherwise returns false.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to compare against.
	 * @return Returns true if the two objects are equal, otherwise false.
	 */
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof SFRComposite) {

			// We can now cast the other object.
			SFRComposite composite = (SFRComposite) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && sFRComponents
					.equals(composite.sFRComponents));
		}

		return equals;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return The hash of the object.
	 */
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * sFRComponents.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Deep copies the contents of the object from another object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The object to be copied from.
	 */
	public void copy(SFRComposite otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		// Copy the super's values.
		super.copy(otherObject);

		// Copy the local values.
		sFRComponents.clear();
		sFRComponents.putAll(otherObject.sFRComponents);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated cloned object.
	 */
	public Object clone() {

		// Initialize a new object.
		SFRComposite object = new SFRComposite();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}
}