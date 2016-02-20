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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.Composite;

/**
 * A PlantComposite is a container for {@link PlantComponent}s. Duplicate
 * components and components with duplicate IDs are not allowed.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantComposite extends PlantComponent
		implements Composite, IVizUpdateable {

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
	 * A list of IVizUpdateableListeners who are to be updated when changes
	 * occur to this object.
	 */
	private List<IVizUpdateableListener> basicListeners;

	/**
	 * The default, nullary constructor. Initializes all default values.
	 */
	public PlantComposite() {

		// Set the name, ID, and description.
		this.objectName = "Plant Composite 1";
		this.uniqueId = 1;
		this.objectDescription = "Container for plant-level reactor components";

		// Initialize the map of PlantComponents.
		components = new TreeMap<Integer, PlantComponent>();

		// Initialize the lists of listeners.
		listeners = new ArrayList<IPlantCompositeListener>();
		basicListeners = new ArrayList<IVizUpdateableListener>();

		return;
	}

	/**
	 * Regular {@link Component}s cannot be added to this {@link Composite}. Use
	 * {@link #addPlantComponent(PlantComponent)}.
	 */
	@Override
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

		if (component != null) {
			int id = component.getId();

			// If the ID is available, add the component and notify listeners
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
	}

	/**
	 * Removes the {@link PlantComponent} with the specified ID.
	 */
	@Override
	public void removeComponent(int childId) {

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
	}

	/**
	 * Gets the Component with the specified ID. To avoid having to cast the
	 * return value, use {@link #getPlantComponent(int)}.
	 */
	@Override
	public Component getComponent(int childId) {
		return components.get(childId);
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
		return components.get(childId);
	}

	/**
	 * Gets the number of {@link PlantComponent}s stored in this PlantComposite.
	 */
	@Override
	public int getNumberOfComponents() {
		return components.size();
	}

	/**
	 * Gets a list of the Components stored in this PlantComposite. To avoid
	 * having to cast the return values, use {@link #getPlantComponents()}.
	 */
	@Override
	public ArrayList<Component> getComponents() {
		return new ArrayList<Component>(components.values());
	}

	/**
	 * Gets a list of the PlantComponents stored in this PlantComposite.
	 * 
	 * @return A list of PlantComponents stored in this PlantComposite.
	 */
	public List<PlantComponent> getPlantComponents() {
		return new ArrayList<PlantComponent>(components.values());
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
	public void registerPlantCompositeListener(
			IPlantCompositeListener listener) {

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

					for (IVizUpdateableListener listener : basicListeners) {
						listener.update(PlantComposite.this);
					}
				}
			};
			notifierThread.start();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.reactor.plant.PlantComponent#accept(org.eclipse.ice.
	 * reactor .plant.IPlantComponentVisitor)
	 */
	@Override
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
	@Override
	public boolean equals(Object otherObject) {

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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.reactor.plant.PlantComponent#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 31 * hash + components.hashCode();
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.reactor.plant.PlantComponent#clone()
	 */
	@Override
	public Object clone() {
		PlantComposite clone = new PlantComposite();
		clone.copy(this);
		return clone;
	}

	/**
	 * Copies the contents of the other PlantComposite. The components stored
	 * are not deep copied.
	 * 
	 * @param otherObject
	 *            The other object to copy the contents from.
	 */
	public void copy(PlantComposite otherObject) {

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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable#
	 * register(org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void register(IVizUpdateableListener listener) {
		basicListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable#
	 * unregister(org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void unregister(IVizUpdateableListener listener) {
		basicListeners.remove(listener);
	}

}
