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
package org.eclipse.ice.client.widgets.mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;

/**
 * This class provides a base class for any custom modes of handling user input.
 * 
 * By default, the mouse left and right clicks are mapped to
 * {@link #leftClick(boolean)} and {@link #rightClick(boolean)}, respectively.
 * This behavior can be overridden by modifying the {@link #listeners} map.
 * 
 * Only some keys are allowed to be bound to custom actions. For a list of
 * bindable keys, see the {@link Key} enum.
 * 
 * @author djg
 * 
 */
public abstract class MeshApplicationMode {

	/**
	 * This enum provides all keys that are bindable by MeshApplicationMode
	 * subclasses. Each literal has a {@link #name} to which
	 * {@link ActionListener}s can be registered in the MeshApplication. It also
	 * has a group of {@link Trigger}s that the MeshApplication uses to fire
	 * these keys.
	 * 
	 * @author djg
	 * 
	 */
	public enum Key {
		/**
		 * A mouse left-click. Triggered only by a mouse left-click.
		 */
		LeftClick(new MouseButtonTrigger(MouseInput.BUTTON_LEFT)),
		/**
		 * A mouse right-click. Triggered only by a mouse right-click.
		 */
		RightClick(new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)),
		/**
		 * The Shift keys. Triggered by both the left and right shift keys.
		 */
		Shift(new KeyTrigger(KeyInput.KEY_LSHIFT), new KeyTrigger(
				KeyInput.KEY_RSHIFT)),
		/**
		 * The Control (Ctrl) keys. Triggered by both the left and right control
		 * keys.
		 */
		Control(new KeyTrigger(KeyInput.KEY_LCONTROL), new KeyTrigger(
				KeyInput.KEY_RCONTROL)),
		/**
		 * The Enter key.
		 */
		Enter(new KeyTrigger(KeyInput.KEY_RETURN)),
		/**
		 * The Delete key.
		 */
		Delete(new KeyTrigger(KeyInput.KEY_DELETE)),
		/**
		 * The Backspace key.
		 */
		Backspace(new KeyTrigger(KeyInput.KEY_BACK)),
		/**
		 * The Escape key.
		 */
		Escape(new KeyTrigger(KeyInput.KEY_ESCAPE)),
		/**
		 * This event occurs only when the mouse has moved.
		 */
		MouseMove(new MouseAxisTrigger(MouseInput.AXIS_X, false),
				new MouseAxisTrigger(MouseInput.AXIS_X, true),
				new MouseAxisTrigger(MouseInput.AXIS_Y, true),
				new MouseAxisTrigger(MouseInput.AXIS_Y, false));

		/**
		 * The name of the key event. By default, this is the same as
		 * {@link #toString()}.
		 */
		public final String name;
		/**
		 * The {@link Trigger}s that should fire this key event.
		 */
		public final Trigger[] triggers;

		/**
		 * The private enum constructor. Initializes the name and key triggers.
		 * 
		 * @param triggers
		 *            The {@link Trigger}s that should fire this key event.
		 */
		private Key(Trigger... triggers) {
			this.name = this.toString();
			this.triggers = triggers;
		}
	}

	/**
	 * A Map of {@link ActionListener}s. This map contains any listener that
	 * should be linked to a {@link Key}.
	 */
	protected final Map<Key, InputListener> listeners;

	/**
	 * An {@link ActionListener} that calls {@link #leftClick(boolean)} or
	 * {@link #rightClick(boolean)} when it is fired. By default, it is mapped
	 * to {@link Key#LeftClick} and {@link Key#RightClick}.
	 */
	protected final ActionListener mouseClickListener;

	/**
	 * The default constructor. Initializes the Map of {@link ActionListener}s.
	 * It also initializes a mouse-click listener that calls the
	 * {@link #leftClick(boolean)} and {@link #rightClick(boolean)} events.
	 */
	public MeshApplicationMode() {
		// Initialize the Map of ActionListeners.
		listeners = new HashMap<Key, InputListener>();

		// Create a listener to listen for mouse clicks.
		mouseClickListener = new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				if (Key.LeftClick.name.equals(name)) {
					leftClick(isPressed);
				} else {
					rightClick(isPressed);
				}
			}
		};
		// Map the listener to the left and right click keys.
		listeners.put(Key.LeftClick, mouseClickListener);
		listeners.put(Key.RightClick, mouseClickListener);

		return;
	}

	/**
	 * Gets all {@link ActionListener}s for this MeshApplicationMode.
	 * 
	 * @return An ArrayList of ActionListeners. By default, this at least
	 *         contains the mouse left-/right-click listener.
	 */
	public List<InputListener> getInputListeners() {

		List<InputListener> listeners = new ArrayList<InputListener>();
		listeners.add(mouseClickListener);

		return listeners;
	}

	/**
	 * Gets all {@link Key}s and the {@link ActionListener}s that should fire
	 * when those keys are clicked.
	 * 
	 * @return A Map whose key-value pairs are Keys and ActionListeners.
	 */
	public Map<Key, InputListener> getInputListenerMappings() {
		return new HashMap<Key, InputListener>(listeners);
	}

	/**
	 * Loads any relevant data necessary for the MeshApplicationMode to run.
	 * 
	 * @param application
	 *            The {@link MeshApplication} that is using this
	 *            MeshApplicationMode.
	 */
	public abstract void load(MeshApplication application);

	/**
	 * Clears any data that was necessary for the MeshApplicationMode to run.
	 */
	public abstract void clear();

	/**
	 * Performs simpleUpdate operations specific to this application mode.
	 * 
	 * @param tpf
	 *            Time per frame. Can be used to limit movement to make it
	 *            consistent across machines.
	 */
	public void simpleUpdate(float tpf) {
		// Do nothing. Subclasses can override this.
	}

	/**
	 * By default, this method is called when the left mouse button is clicked.
	 * 
	 * @param isPressed
	 *            True on mouse down, false on mouse up.
	 */
	protected abstract void leftClick(boolean isPressed);

	/**
	 * By default, this method is called when the right mouse button is clicked.
	 * 
	 * @param isPressed
	 *            True on mouse down, false on mouse up.
	 */
	protected abstract void rightClick(boolean isPressed);

	/**
	 * Gets the name of this MeshApplicationMode.
	 * 
	 * @return A String. It should not be null.
	 */
	public abstract String getName();

	/**
	 * Gets a description of this MeshApplicationMode.
	 * 
	 * @return A String. It should not be null.
	 */
	public abstract String getDescription();

}
