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
package org.eclipse.ice.client.widgets.reactoreditor.plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.ice.client.widgets.jme.IRenderQueue;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.Reactor;

import com.jme3.material.Material;

/**
 * This class manages a collection of {@link AbstractPlantController}s and also
 * serves as a sort of factory for creating them based on the type of
 * {@link PlantComponent} model that needs a view and controller. Note that
 * views are managed strictly by the controllers, so this class does not provide
 * access to the created {@link AbstractPlantView}s.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantControllerManager {

	/**
	 * The queue responsible for performing tasks on the jME rendering thread.
	 */
	private final IRenderQueue renderQueue;

	/**
	 * A map of all managed PipeControllers keyed on their models' IDs.
	 */
	private final Map<Integer, PipeController> pipeControllers;
	/**
	 * A map of all managed JunctionControllers keyed on their models' IDs.
	 */
	private final Map<Integer, JunctionController> junctionControllers;
	/**
	 * A map of all managed ReactorControllers keyed on their models' IDs.
	 */
	private final Map<Integer, ReactorController> reactorControllers;

	/**
	 * A list of IPlantControllerManagerListeners that are notified when
	 * controllers are created or destroyed.
	 */
	private final List<IPlantControllerManagerListener> listeners;

	/**
	 * A read lock for reading from the list of listeners.
	 */
	private final Lock listenerReadLock;
	/**
	 * A write lock for updating the list of listeners.
	 */
	private final Lock listenerWriteLock;

	/**
	 * A read lock that should be used when pulling values from the maps.
	 */
	private final Lock readLock;
	/**
	 * A write lock that should be used when updating the maps.
	 */
	private final Lock writeLock;

	// FIXME We may need to create a lock for modifying the listeners.

	/**
	 * A nullary constructor. This constructor should not be used except to
	 * create an invalid PlantControllerManager, as its associated jME3
	 * application is not set.
	 */
	public PlantControllerManager() {
		this(null);
	}

	/**
	 * The default constructor. Neither the application nor the update queue
	 * should be null.
	 * 
	 * @param app
	 *            The jME3 PlantAppState for which this class manages
	 *            controllers (and views).
	 * @param updateQueue
	 *            The PlantAppState's thread-safe queue of actions. This is
	 *            required for the managed controllers to synchronize their
	 *            views with the jME3 application's simpleUpdate() thread.
	 */
	public PlantControllerManager(PlantAppState app) {

		// Set the references to the jME3 app and its update queue if possible.
		this.renderQueue = (app != null ? app : new IRenderQueue() {
			public <T> Future<T> enqueue(Callable<T> callable) {
				return null;
			}
		});

		// Create the maps of controllers.
		pipeControllers = new HashMap<Integer, PipeController>();
		junctionControllers = new HashMap<Integer, JunctionController>();
		reactorControllers = new HashMap<Integer, ReactorController>();

		// Initialize the list of listeners.
		listeners = new ArrayList<IPlantControllerManagerListener>();

		// Create a read/write lock and set the read/write lock references.
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
		readLock = lock.readLock();
		writeLock = lock.writeLock();

		// Create a read/write lock for the listeners.
		lock = new ReentrantReadWriteLock(true);
		listenerReadLock = lock.readLock();
		listenerWriteLock = lock.writeLock();

		return;
	}

	/**
	 * Creates an AbstractPlantController for the specified PlantComponent. The
	 * controller is stored inside this manager for later retrieval. Multiple
	 * controllers for the same component cannot be created by and stored in a
	 * single manager.
	 * 
	 * @param component
	 *            The PlantComponent that needs a view and controller.
	 * @param material
	 *            The Material that should be used to render the view.
	 * @return If the component is supported and does not already have a
	 *         controller in this manager, then the created controller is
	 *         returned. Otherwise, null is returned.
	 */
	public AbstractPlantController createController(PlantComponent component,
			final Material material) {

		// Create a wrapper for an AbstractPlantController. This avoids having
		// to use a class variable (which would require synchronization in
		// multi-threaded scenarios).
		final ControllerWrapper wrapper = new ControllerWrapper();

		// Only proceed if the component and material are not null.
		if (component != null && material != null) {

			// Get the components ID. This will be used in the visitor.
			final int id = component.getId();

			// Use an IPlantComponentVisitor to create the appropriate
			// controller based on the component. The controller is set only if
			// the component is supported and it does not already have a
			// controller.
			component.accept(new PlantControllerVisitor() {
				public void visit(Junction plantComp) {

					// Only create a new view/controller if there's not already
					// a controller associated with the ID.
					if (!junctionControllers.containsKey(id)) {
						// Create a view and controller.
						JunctionView view = new JunctionView(plantComp
								.getName(), material);
						JunctionController junctionController = new JunctionController(
								plantComp, view, renderQueue,
								PlantControllerManager.this);

						// Store the controller in its map, and set the
						// AbstractPlantController reference to point to the new
						// controller.
						writeLock.lock();
						try {
							junctionControllers.put(id, junctionController);
						} finally {
							writeLock.unlock();
						}

						wrapper.controller = junctionController;
					}

					return;
				}

				public void visit(Reactor plantComp) {

					// Only create a new view/controller if there's not already
					// a controller associated with the ID.
					if (!reactorControllers.containsKey(id)) {
						// Create a view and controller.
						ReactorView view = new ReactorView(plantComp.getName(),
								material);
						ReactorController reactorController = new ReactorController(
								plantComp, view, renderQueue,
								PlantControllerManager.this);

						// Store the controller in its map, and set the
						// AbstractPlantController reference to point to the new
						// controller.
						writeLock.lock();
						try {
							reactorControllers.put(id, reactorController);
						} finally {
							writeLock.unlock();
						}

						wrapper.controller = reactorController;
					}

					return;
				}

				public void visit(HeatExchanger plantComp) {

					// Only create a new view/controller if there's not already
					// a controller associated with the ID.
					if (!pipeControllers.containsKey(id)) {
						// Create a view and controller.
						HeatExchangerView view = new HeatExchangerView(
								plantComp.getName(), material);
						HeatExchangerController pipeController = new HeatExchangerController(
								plantComp, view, renderQueue);

						// Store the controller in its map, and set the
						// AbstractPlantController reference to point to the new
						// controller.
						writeLock.lock();
						try {
							pipeControllers.put(id, pipeController);
						} finally {
							writeLock.unlock();
						}

						wrapper.controller = pipeController;
					}

					return;
				}

				public void visit(Pipe plantComp) {

					// Only create a new view/controller if there's not already
					// a controller associated with the ID.
					if (!pipeControllers.containsKey(id)) {
						// Create a view and controller.
						PipeView view = new PipeView(plantComp.getName(),
								material);
						PipeController pipeController = new PipeController(
								plantComp, view, renderQueue);

						// Store the controller in its map, and set the
						// AbstractPlantController reference to point to the new
						// controller.
						writeLock.lock();
						try {
							pipeControllers.put(id, pipeController);
						} finally {
							writeLock.unlock();
						}

						wrapper.controller = pipeController;
					}

					return;
				}
			});

			// Notify IPlantControllerManagerListeners of the add.
			if (wrapper.controller != null) {
				notifyListeners(component, wrapper.controller, true);
			}
		}
		return wrapper.controller;
	}

	/**
	 * Gets an AbstractPlantController for a PlantComponent from this manager if
	 * one exists.
	 * 
	 * @param component
	 *            The PlantComponent whose controller should be retrieved.
	 * @return The component's associated AbstractPlantController, or null if
	 *         none exists.
	 */
	public AbstractPlantController getController(PlantComponent component) {

		// Create a wrapper for an AbstractPlantController. This avoids having
		// to use a class variable (which would require synchronization in
		// multi-threaded scenarios).
		final ControllerWrapper wrapper = new ControllerWrapper();

		if (component != null) {
			// Use a visitor that just gets a controller from one of the maps
			// based on the component type.

			readLock.lock();
			try {
				component.accept(new PlantControllerVisitor() {
					public void visit(Junction plantComp) {
						wrapper.controller = junctionControllers.get(plantComp
								.getId());
					}

					public void visit(Reactor plantComp) {
						wrapper.controller = reactorControllers.get(plantComp
								.getId());
					}

					public void visit(HeatExchanger plantComp) {
						wrapper.controller = pipeControllers.get(plantComp
								.getId());
					}

					public void visit(Pipe plantComp) {
						wrapper.controller = pipeControllers.get(plantComp
								.getId());
					}
				});
			} finally {
				readLock.unlock();
			}

			// FIXME This won't work with the current way HeatExchangers are
			// handled. The heat exchanger and its primary pipe share the same
			// controller, but they are referenced with unique PlantComponents
			// (because HeatExchangers wrap two Pipes, a primary and secondary).
			// Make sure the controller's model matches the component.
			// if (controller != null && component != controller.model) {
			// controller = null;
			// }
		}

		return wrapper.controller;
	}

	/**
	 * Removes and disposes the AbstractPlantController for a PlantComponent.
	 * 
	 * @param component
	 *            The PlantComponent whose controller should be removed from the
	 *            manager and disposed.
	 * @return The AbstractPlantController that was removed and disposed, or
	 *         null if there was not one.
	 */
	public AbstractPlantController removeController(PlantComponent component) {

		// Create a wrapper for an AbstractPlantController. This avoids having
		// to use a class variable (which would require synchronization in
		// multi-threaded scenarios).
		final ControllerWrapper wrapper = new ControllerWrapper();

		if (component != null) {
			final int id = component.getId();

			// Use a visitor that removes the controller from the appropriate
			// map if its model matches the component. The controller should be
			// null if it was not removed.
			writeLock.lock();
			try {
				component.accept(new PlantControllerVisitor() {
					public void visit(Junction plantComp) {
						wrapper.controller = junctionControllers.get(id);
						if (wrapper.controller != null
								&& plantComp == wrapper.controller.getModel()) {
							junctionControllers.remove(id);
						} else {
							wrapper.controller = null;
						}
					}

					public void visit(Reactor plantComp) {
						wrapper.controller = reactorControllers.get(id);
						if (wrapper.controller != null
								&& plantComp == wrapper.controller.getModel()) {
							reactorControllers.remove(id);
						} else {
							wrapper.controller = null;
						}
					}

					public void visit(HeatExchanger plantComp) {
						wrapper.controller = pipeControllers.get(id);
						if (wrapper.controller != null
								&& plantComp == wrapper.controller.getModel()) {
							pipeControllers.remove(id);
						} else {
							wrapper.controller = null;
						}
					}

					public void visit(Pipe plantComp) {
						wrapper.controller = pipeControllers.get(id);
						if (wrapper.controller != null
								&& plantComp == wrapper.controller.getModel()) {
							pipeControllers.remove(id);
						} else {
							wrapper.controller = null;
						}
					}
				});
			} finally {
				writeLock.unlock();
			}

			// If the controller was removed, dispose it.
			if (wrapper.controller != null) {
				wrapper.controller.dispose();

				// Notify IPlantControllerManagerListeners of the removal.
				if (wrapper.controller != null) {
					notifyListeners(component, wrapper.controller, false);
				}
			}
		}

		return wrapper.controller;
	}

	/**
	 * Removes all {@link AbstractPlantController}s from this manager and
	 * disposes of them.
	 */
	public void clearControllers() {

		// Dispose of and clear the pipe controllers.
		writeLock.lock();
		try {
			for (AbstractPlantController c : pipeControllers.values()) {
				c.dispose();
			}
			pipeControllers.clear();

			// Dispose of and clear the junction controllers.
			for (AbstractPlantController c : junctionControllers.values()) {
				c.dispose();
			}
			junctionControllers.clear();

			// Dispose of and clear the pipe controllers.
			for (AbstractPlantController c : reactorControllers.values()) {
				c.dispose();
			}
			reactorControllers.clear();
		} finally {
			writeLock.unlock();
		}

		return;
	}

	/**
	 * Registers a listener to listen for controller creation and deletion
	 * events.
	 * 
	 * @param listener
	 *            The listener to register. <b>Duplicate listeners are not
	 *            supported.</b>
	 */
	public void registerListener(IPlantControllerManagerListener listener) {

		if (listener != null) {

			boolean found = false;

			// The list of listeners is usually small, so use a linear search.
			listenerWriteLock.lock();
			try {
				int size = listeners.size();
				for (int i = 0; !found && i < size; i++) {
					found = (listener == listeners.get(i));
				}

				// If the listener is not already in the list, add it.
				if (!found) {
					listeners.add(listener);
				}
			} finally {
				listenerWriteLock.unlock();
			}
		}

		return;
	}

	/**
	 * Unregisters a listener from listening for controller creation and
	 * deletion events.
	 * 
	 * @param listener
	 *            The listener to unregister.
	 */
	public void unregisterListener(IPlantControllerManagerListener listener) {

		boolean found = false;

		// Loop over the list of listeners and remove the first matching
		// listener reference.
		listenerWriteLock.lock();
		try {
			int i, size = listeners.size();
			for (i = 0; !found && i < size; i++) {
				found = (listener == listeners.get(i));
			}
			// If the listener was found, remove it.
			if (found) {
				listeners.remove(i - 1);
			}
		} finally {
			listenerWriteLock.unlock();
		}

		return;
	}

	/**
	 * Notifies {@link IPlantControllerManagerListener}s that a controller has
	 * been created or deleted for a PlantComponent.
	 * 
	 * @param component
	 *            The component whose controller was added or removed.
	 * @param controller
	 *            The controller that was added or removed.
	 * @param added
	 *            Whether the controller was added or removed.
	 */
	public void notifyListeners(final PlantComponent component,
			final AbstractPlantController controller, final boolean added) {

		// Create a thread to notify IJunctionListeners that pipes were
		// either added or removed.
		Thread notifierThread = new Thread() {
			@Override
			public void run() {
				AbstractPlantController controller = getController(component);
				listenerReadLock.lock();
				try {
					if (added) {
						for (IPlantControllerManagerListener l : listeners) {
							l.addedController(component, controller);
						}
					} else {
						for (IPlantControllerManagerListener l : listeners) {
							l.removedController(component);
						}
					}
				} finally {
					listenerReadLock.unlock();
				}
			}
		};
		notifierThread.start();

		return;
	}

	/**
	 * A wrapper for an AbstractPlantController. This avoids having to use a
	 * class variable for visit operations (which would require synchronization
	 * in multi-threaded environments).
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class ControllerWrapper {
		public AbstractPlantController controller = null;
	}
}
