/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.widgets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.Trigger;

/**
 * Input controls for jME typically require three components:
 * <ul>
 * <li>an <code>InputListener</code> (e.g., an {@link ActionListener} or an
 * {@link AnalogListener})</li>
 * <li>one or more mapping names (e.g., "leftClick", "rightClick")</li>
 * <li>one or more {@link Trigger}s per mapping name</li>
 * </ul>
 * <p>
 * This class provides a wrapper for these components and handles registration
 * with an <code>Application</code>'s {@link InputManager}.
 * </p>
 * <p>
 * <b>Note:</b> A single <code>InputControl</code> cannot be registered with
 * more than one <code>InputManager</code> at a time.
 * </p>
 * 
 * @author Jordan Deyton
 * 
 */
public class InputControl {

	// TODO We might need to add getters at a later time. Currently, the primary
	// convenience of this class is that it's easy to unregister and you only
	// need to keep track of a single instance rather than a separate listener,
	// strings, and triggers.

	/**
	 * The <code>InputListener</code> that will be called when one of the
	 * {@link #mappings} is triggered. This is typically an
	 * <code>ActionListener</code> or <code>AnalogListener</code>.
	 */
	private final InputListener listener;
	/**
	 * The mapping names and <code>Trigger</code>s for those mapping names.
	 */
	private final Map<String, Trigger[]> mappings;

	/**
	 * The current <code>InputManager</code> with which this control is
	 * registered.
	 */
	private InputManager input;

	/**
	 * The default constructor.
	 * 
	 * @param listener
	 *            The <code>InputListener</code> that will be called when one of
	 *            the {@link #mappings} is triggered. This is typically an
	 *            <code>ActionListener</code> or <code>AnalogListener</code>.
	 * @param mappingNames
	 *            The expected mapping names for the listener.
	 */
	public InputControl(InputListener listener, String... mappingNames) {
		this.listener = listener;

		if (mappingNames != null) {
			mappings = new HashMap<String, Trigger[]>(mappingNames.length);
			for (String mappingName : mappingNames) {
				mappings.put(mappingName, null);
			}
		} else {
			mappings = new HashMap<String, Trigger[]>(1);
		}

		return;
	}

	/**
	 * Adds <code>Trigger</code>s for the specified mapping names. Does nothing
	 * if either argument is <code>null</code>.
	 * 
	 * @param mappingName
	 *            The name of the mapping that will be triggered.
	 * @param triggers
	 *            The <code>Trigger</code>s that will cause an event with the
	 *            mapping name to be passed to the <code>InputControl</code>'s
	 *            <code>InputListener</code>.
	 */
	public void addTriggers(String mappingName, Trigger... triggers) {
		if (mappingName != null && triggers != null) {
			mappings.put(mappingName, triggers);
		}
	}

	/**
	 * Registers the control with the specified <code>InputManager</code>. The
	 * <code>InputControl</code> can only be registered with one
	 * <code>InputManager</code> at a time.
	 * 
	 * @param input
	 *            The <code>InputManager</code> to register with.
	 */
	public void registerWithInput(InputManager input) {

		if (input != null && this.input == null) {

			// Set the InputManager so it can be unregistered later.
			this.input = input;

			// Convert the collection of mapping names to a String array.
			String[] mappingNames = new String[mappings.size()];
			mappings.keySet().toArray(mappingNames);

			// Add the action or analog listener to the InputManager.
			input.addListener(listener, mappingNames);

			// Add all of the Triggers for the mapping name.
			for (String mappingName : mappingNames) {
				input.addMapping(mappingName, mappings.get(mappingName));
			}
		}

		return;
	}

	/**
	 * Unregisters the control from its current <code>InputManager</code>.
	 */
	public void unregisterFromInput() {

		if (input != null) {

			// Remove the action or analog listener from the InputManager.
			input.removeListener(listener);

			// Delete all triggers registered with the InputManager.
			for (Entry<String, Trigger[]> entry : mappings.entrySet()) {
				String mappingName = entry.getKey();
				for (Trigger trigger : entry.getValue()) {
					input.deleteTrigger(mappingName, trigger);
				}
			}

			// Unset the InputManager so it can be reset later.
			input = null;
		}

		return;
	}
}
