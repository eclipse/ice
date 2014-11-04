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

import org.eclipse.ice.datastructures.ICEObject.ICEObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This interface is for Junctions that {@link IJunctionListener}s can register
 * with and listen to for updates to their set of attached
 * {@link PlantComponent}s.
 * </p>
 * <p>
 * Normally, components that extend {@link ICEObject} will use its methods to
 * notify listeners of changes. However, its methods are "blanket" notifications
 * that just mean <i>something</i> has changed, but not what.
 * 
 * In the interest of speeding up reactions to changes in Junction's pipes, this
 * interface provides methods to add and remove individual pipes as well as a
 * notify method that should call the appropriate notification method for all
 * registered IJunctionListeners.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IJunction {
	// TODO Add IJunction to the model.
	
	/**
	 * Registers an IJunctionListener to listen to the Junction for pipe add and
	 * remove events, as well as any other specialized events that may be too
	 * complex for a regular IUpdateableListener to interpret.
	 * 
	 * @param listener
	 *            The listener to register. <b>Duplicate listeners are not
	 *            accepted.</b>
	 */
	public void registerJunctionListener(IJunctionListener listener);

	/**
	 * Unregisters an IJunctionListener from the Junction. It will no longer
	 * receive pipe add and remove events from this Junction.
	 * 
	 * @param listener
	 *            The listener to unregister.
	 */
	public void unregisterJunctionListener(IJunctionListener listener);

	/**
	 * Notifies all registered {@link IJunctionListener}s of any added or
	 * removed pipes <i>in a separate notifier thread</i>.
	 * 
	 * @param components
	 *            The PlantComponents that have been added or removed from the
	 *            Junction.
	 * @param added
	 *            Whether the components were added or removed.
	 */
	public void notifyJunctionListeners(List<PlantComponent> components,
			boolean added);

	/**
	 * This method is used to determine if a component is connected to the
	 * Junction as input or output.
	 * 
	 * @param component
	 *            The PlantComponent that is either input or output.
	 * @return True if the component is an input component for the Junction,
	 *         false otherwise.
	 */
	public boolean isInput(PlantComponent component);

	/**
	 * Gets the PlantComponents, usually Pipes or HeatExchangers, that are input
	 * for the Junction.
	 * 
	 * @return A List of the Junction's input PlantComponents.
	 */
	public ArrayList<PlantComponent> getInputs();

	/**
	 * Gets the PlantComponents, usually Pipes or HeatExchangers, that are
	 * output for the Junction.
	 * 
	 * @return A List of the Junction's output PlantComponents.
	 */
	public ArrayList<PlantComponent> getOutputs();

	/**
	 * Adds a PlantComponent as input to the Junction.
	 * 
	 * @param input
	 *            The PlantComponent that will be input for the Junction.
	 */
	public void addInput(PlantComponent input);

	/**
	 * Removes an input PlantComponent from the Junction.
	 * 
	 * @param input
	 *            The PlantComponent that should be removed from the Junction.
	 */
	public void removeInput(PlantComponent input);

	/**
	 * Adds an output PlantComponent to the Junction.
	 * 
	 * @param output
	 *            The PlantComponent that should be removed from the Junction.
	 */
	public void addOutput(PlantComponent output);

	/**
	 * Removes an output PlantComponent from the Junction.
	 * 
	 * @param output
	 *            The PlantComponent that should be removed from the Junction.
	 */
	public void removeOutput(PlantComponent output);

}
