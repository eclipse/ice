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
package org.eclipse.ice.reactorAnalyzer;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.Composite;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;

/**
 * This class is intended to contain {@link IReactorComponent}s by implementing
 * the {@link Composite} interface. However, it associates child components by
 * associating them with an ID, which is not necessarily the same as the
 * component IDs.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorComposite extends ICEObject implements IReactorComponent,
		Composite {

	// TODO Test this class.

	/**
	 * A List of IReactorComponents stored by this Composite.
	 */
	private final Map<Integer, IReactorComponent> reactorComponents;

	/**
	 * The default constructor. Initializes an empty list of IReactorComponents.
	 */
	public ReactorComposite() {
		reactorComponents = new TreeMap<Integer, IReactorComponent>();
	}

	// ---- Implements IReactorComponent ---- //
	/**
	 * Fires the default visit operation on the visitor. It should result in a
	 * call to {@link IComponentVisitor#visit(IReactorComponent)}.
	 * 
	 * @param visitor
	 *            The visitor.
	 */
	@Override
	public void accept(IComponentVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
	}

	/**
	 * Overrides {@link ICEObject}'s behavior to pass the update to all child
	 * components.
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		for (IReactorComponent component : reactorComponents.values()) {
			component.update(updatedKey, newValue);
		}
	}

	// All other methods from IReactorComponent (a sub-interface of Component)
	// have been implemented by ICEObject.
	// -------------------------------------- //

	// ---- Implements Composite ---- //
	/**
	 * If the child is an IReactorComponent, this method adds the child to this
	 * Composite. Any previously added IReactorComponent associated with the
	 * same ID as this child's ID will be removed.
	 * 
	 * @param child
	 *            The Component to add.
	 */
	@Override
	public void addComponent(Component child) {

		// If the child is not null, visit it with a visitor that adds
		// IReactorComponents to the map.
		if (child != null) {

			// Get the ID of the new component and get the previous component
			// associated with the ID.
			final int id = child.getId();
			Component oldComponent = reactorComponents.get(id);

			// Visit the child and put it into the map if it's an
			// IReactorComponent.
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(IReactorComponent component) {
					reactorComponents.put(id, component);
				}
			};
			child.accept(visitor);

			// Notify listeners of the change only if the value changed.
			if (child != oldComponent) {
				notifyListeners();
			}
		}

		return;
	}

	/**
	 * Adds an IReactorComponent to this Composite. If its ID is already taken,
	 * then the previous IReactorComponent associated with the ID is replaced
	 * and returned. Null values will not be added to this Composite.
	 * 
	 * @param id
	 *            The ID associated with the new IReactorComponent.
	 * @param child
	 *            The IReactorComponent to add.
	 * @return The previous IReactorComponent associated with the ID, or null if
	 *         one did not exist.
	 */
	public IReactorComponent setComponent(int id, IReactorComponent child) {

		IReactorComponent oldComponent = null;

		// Only add non-null reactor components.
		if (child != null) {
			oldComponent = reactorComponents.put(id, child);

			// Notify listeners of the change only if the value changed.
			if (child != oldComponent) {
				notifyListeners();
			}
		}

		return oldComponent;
	}

	/**
	 * Removes the IReactorComponent in this Composite that is associated with
	 * the specified ID.
	 */
	@Override
	public void removeComponent(int childId) {
		// Remove the component associated with the ID and notify listeners if
		// it was removed.
		if (reactorComponents.remove(childId) != null) {
			notifyListeners();
		}
	}

	/**
	 * Gets the IReactorComponent in this Composite that is associated with the
	 * specified ID.
	 */
	@Override
	public Component getComponent(int childId) {
		return reactorComponents.get(childId);
	}

	/**
	 * Convenience method that returns the same component as
	 * {@link #getComponent(int)} but already cast as an IReactorComponent.
	 * 
	 * @param childId
	 *            The ID associated with the desired IReactorComponent.
	 * @return The IReactorComponent associated with the specified ID, or null
	 *         if no such component exists.
	 */
	public IReactorComponent getReactorComponent(int childId) {
		return reactorComponents.get(childId);
	}

	/**
	 * Gets the number of IReactorComponents stored in this Composite.
	 * 
	 * @return The number of IReactorComponents stored in this Composite.
	 */
	@Override
	public int getNumberOfComponents() {
		return reactorComponents.size();
	}

	/**
	 * Gets the list of Components stored in this Composite.
	 * 
	 * @return The list of Components stored in this Composite.
	 */
	@Override
	public ArrayList<Component> getComponents() {
		return new ArrayList<Component>(reactorComponents.values());
	}

	/**
	 * Convenience method that returns the same list as {@link #getComponents()}
	 * but already cast as IReactorComponents.
	 * 
	 * @return The Composite's current list of IReactorComponents.
	 */
	public ArrayList<IReactorComponent> getReactorComponents() {
		return new ArrayList<IReactorComponent>(reactorComponents.values());
	}

	// ------------------------------ //

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
	@Override
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof ReactorComposite) {

			// We can now cast the other object.
			ReactorComposite composite = (ReactorComposite) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && reactorComponents
					.equals(composite.reactorComponents));
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
	@Override
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * hash + reactorComponents.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Copies the contents of the object from another object. Components from
	 * the other Composite are not deep copied.
	 * </p>
	 * 
	 * @param otherObject
	 *            The object to be copied from.
	 */
	public void copy(ReactorComposite otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}

		// Copy the local values.
		reactorComponents.clear();
		reactorComponents.putAll(otherObject.reactorComponents);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated cloned object.
	 */
	@Override
	public Object clone() {

		// Initialize a new object.
		ReactorComposite object = new ReactorComposite();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

}
