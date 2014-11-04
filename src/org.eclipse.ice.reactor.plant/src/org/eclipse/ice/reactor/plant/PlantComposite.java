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
package org.eclipse.ice.reactor.plant;

import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.Composite;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A PlantComposite is a container for {@link PlantComponent}s. Duplicate
 * components and components with duplicate IDs are not allowed.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantComposite extends PlantComponent implements Composite {

	/**
	 * A map of all PlantComponents contained by this PlantComposite, keyed on
	 * the components' IDs.
	 */
	private final TreeMap<Integer, PlantComponent> components;

	/**
	 * A list of IPlantCompositeListeners that are notified when PlantComponents
	 * are added or removed from this PlantComposite.
	 */
	private final List<IPlantCompositeListener> listeners;

	/**
	 * The default, nullary constructor. Initializes all default values.
	 */
	public PlantComposite() {
		// begin-user-code

		// Set the name, ID, and description.
		this.objectName = "Plant Composite 1";
		this.uniqueId = 1;
		this.objectDescription = "Container for plant-level reactor components";

		// Initialize the map of PlantComponents.
		components = new TreeMap<Integer, PlantComponent>();

		// Initialize the list of listeners.
		listeners = new ArrayList<IPlantCompositeListener>();

		return;
		// end-user-code
	}

	/**
	 * Regular {@link Component}s cannot be added to this {@link Composite}. Use
	 * {@link #addPlantComponent(PlantComponent)}.
	 */
	public void addComponent(Component child) {
		// Regular components cannot be added.
	}

	/**
	 * Adds a PlantComponent to the PlantComposite. Duplicate components and
	 * components with non-unique IDs are not allowed.
	 * 
	 * @param component
	 *            The PlantComponent to add.
	 */
	public void addPlantComponent(PlantComponent component) {
		// begin-user-code

		if (component != null) {
			int id = component.getId();

			// If the ID is availalble, add the component and notify listeners
			// that a component has been added.
			if (!components.containsKey(id)) {
				components.put(id, component);

				// Notify IPlantCompositeListeners.
				List<PlantComponent> components = new ArrayList<PlantComponent>(
						1);
				components.add(component);
				notifyPlantCompositeListeners(components, true);

				// Notify IUpdateableListeners.
				notifyListeners();
			}
		}

		return;
		// end-user-code
	}

	/**
	 * Removes the {@link PlantComponent} with the specified ID.
	 */
	public void removeComponent(int childId) {
		// begin-user-code

		// Try to remove the component with the ID.
		PlantComponent component = components.remove(childId);

		// If necessary, notify listeners that a component has been removed.
		if (component != null) {
			// Notify IPlantCompositeListeners.
			List<PlantComponent> components = new ArrayList<PlantComponent>(1);
			components.add(component);
			notifyPlantCompositeListeners(components, false);

			// Notify IUpdateableListeners.
			notifyListeners();
		}

		return;
		// end-user-code
	}

	/**
	 * Gets the Component with the specified ID. To avoid having to cast the
	 * return value, use {@link #getPlantComponent(int)}.
	 */
	public Component getComponent(int childId) {
		// begin-user-code
		return components.get(childId);
		// end-user-code
	}

	/**
	 * Gets the PlantComponent with the specified ID if one exists.
	 * 
	 * @param childId
	 *            The ID of the PlantComponent to get.
	 * @return The PlantComponent that is stored in the PlantComposite, or null
	 *         if one does not exist with the specified ID.
	 */
	public PlantComponent getPlantComponent(int childId) {
		// begin-user-code
		return components.get(childId);
		// end-user-code
	}

	/**
	 * Gets the number of {@link PlantComponent}s stored in this PlantComposite.
	 */
	public int getNumberOfComponents() {
		// begin-user-code
		return components.size();
		// end-user-code
	}

	/**
	 * Gets a list of the Components stored in this PlantComposite. To avoid
	 * having to cast the return values, use {@link #getPlantComponents()}.
	 */
	public ArrayList<Component> getComponents() {
		// begin-user-code
		return new ArrayList<Component>(components.values());
		// end-user-code
	}

	/**
	 * Gets a list of the PlantComponents stored in this PlantComposite.
	 * 
	 * @return A list of PlantComponents stored in this PlantComposite.
	 */
	public List<PlantComponent> getPlantComponents() {
		// begin-user-code
		return new ArrayList<PlantComponent>(components.values());
		// end-user-code
	}

	/**
	 * Registers an IPlantCompositeListener to listen to the Composite for
	 * Component add and remove events, as well as any other specialized events
	 * that may be too complex for a regular IUpdateableListener to interpret.
	 * 
	 * @param listener
	 *            The listener to register. <b>Duplicate listeners are not
	 *            accepted.</b>
	 */
	public void registerPlantCompositeListener(IPlantCompositeListener listener) {
		// begin-user-code

		if (listener != null) {

			boolean found = false;

			// The list of listeners is usually small, so use a linear search.
			int size = listeners.size();
			for (int i = 0; !found && i < size; i++) {
				found = (listener == listeners.get(i));
			}
			// If the listener is not already in the list, add it.
			if (!found) {
				listeners.add(listener);
			}
		}
		return;
		// end-user-code
	}

	/**
	 * Unregisters an IPlantCompositeListener from the PlantComposite. It will
	 * no longer receive Component add and remove events from this Composite.
	 * 
	 * @param listener
	 *            The listener to unregister.
	 */
	public void unregisterPlantCompositeListener(
			IPlantCompositeListener listener) {
		// begin-user-code

		boolean found = false;

		// Loop over the list of listeners and remove the first matching
		// listener reference.
		int i, size = listeners.size();
		for (i = 0; !found && i < size; i++) {
			found = (listener == listeners.get(i));
		}
		// If the listener was found, remove it.
		if (found) {
			listeners.remove(i - 1);
		}
		return;
		// end-user-code
	}

	/**
	 * Notifies all registered {@link IPlantCompositeListener}s of any added or
	 * removed PlantComponents <i>in a separate notifier thread</i>.
	 * 
	 * @param components
	 *            The PlantComponents that have been added or removed from the
	 *            Composite.
	 * @param added
	 *            Whether the components were added or removed.
	 */
	public void notifyPlantCompositeListeners(
			final List<PlantComponent> components, final boolean added) {
		// begin-user-code

		if (components != null && !components.isEmpty()) {
			// Create a thread to notify IPlantCompositeListeners that
			// PlantComponents were either added or removed.
			Thread notifierThread = new Thread() {
				@Override
				public void run() {
					if (added) {
						for (IPlantCompositeListener listener : listeners) {
							listener.addedComponents(PlantComposite.this,
									components);
						}
					} else {
						for (IPlantCompositeListener listener : listeners) {
							listener.removedComponents(PlantComposite.this,
									components);
						}
					}
				}
			};
			notifierThread.start();
		}

		return;
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.PlantComponent#accept(org.eclipse.ice.reactor
	 * .plant.IPlantComponentVisitor)
	 */
	public void accept(IPlantComponentVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.PlantComponent#equals(java.lang.Object)
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// By default, assume the objects are not equivalent.
		boolean equals = false;

		// If the references are the same, then the objects are equivalent.
		if (this == otherObject) {
			equals = true;
		}
		// If the other object is not null and the same type, then check all of
		// the class variables.
		else if (otherObject != null && otherObject instanceof PlantComposite) {
			PlantComposite otherComposite = (PlantComposite) otherObject;

			equals = super.equals(otherComposite)
					&& components.equals(otherComposite.components);
		}

		return equals;
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.reactor.plant.PlantComponent#hashCode()
	 */
	public int hashCode() {
		// begin-user-code
		int hash = super.hashCode();
		hash = 31 * hash + components.hashCode();
		return hash;
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.reactor.plant.PlantComponent#clone()
	 */
	public Object clone() {
		// begin-user-code
		PlantComposite clone = new PlantComposite();
		clone.copy(this);
		return clone;
		// end-user-code
	}

	/**
	 * Copies the contents of the other PlantComposite. The components stored
	 * are not deep copied.
	 * 
	 * @param otherObject
	 *            The other object to copy the contents from.
	 */
	public void copy(PlantComposite otherObject) {
		// begin-user-code

		if (otherObject != null) {

			// Copy the PlantComponent and ICEObject data.
			super.copy(otherObject);

			// Clear all components and notify IPlantCompositeListeners.
			List<PlantComponent> list = new ArrayList<PlantComponent>(
					components.values());
			notifyPlantCompositeListeners(list, false);
			components.clear();

			// Copy all of the local class data.
			components.putAll(otherObject.components);

			// Notify IPlantComposite listeners of the added components.
			list = new ArrayList<PlantComponent>(components.values());
			notifyPlantCompositeListeners(list, true);

			// Notify IUpdateableListeners of the change.
			notifyListeners();
		}

		return;
		// end-user-code
	}

}
