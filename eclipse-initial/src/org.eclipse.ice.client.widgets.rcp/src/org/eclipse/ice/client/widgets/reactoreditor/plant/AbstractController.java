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

import org.eclipse.ice.client.widgets.mesh.ISyncAction;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.jme3.scene.Node;

/**
 * This class provides a base implementation for a controller that manages an
 * {@link IUpdateable} model and an {@link AbstractView} associated with that
 * model. It ensures that changes to the model or features provided by the
 * controller itself are synchronized with the view through the parent
 * SimpleApplication's simpleUpdate() thread.
 * 
 * @author djg
 * 
 */
public abstract class AbstractController implements IUpdateableListener {

	/**
	 * The model for which this controller provides a view.
	 */
	protected final IUpdateable model;
	/**
	 * The {@link AbstractView} associated with this controller.
	 */
	private final AbstractView view;

	/**
	 * The queue (a ConcurrentLinkedQueue of {@link ISyncAction}s) that is
	 * processed in the SimpleApplication's simpleUpdate() thread. Any changes
	 * to the {@link #view} should be done by adding a new action to this queue.
	 */
	protected final Queue<ISyncAction> updateQueue;
	/**
	 * Whether or not the controller and its view have been disposed.
	 */
	protected final AtomicBoolean disposed;

	/**
	 * A list of listeners that need to be informed when either the controller
	 * or its view is updated.
	 */
	private final List<IControllerListener> listeners;
	/**
	 * A read/write lock for concurrent access to {@link #listeners}.
	 */
	private final ReadWriteLock listenerLock;

	// ---- Base Features ---- //
	/**
	 * The current parent node for the {@link #view}.
	 */
	private Node parentNode;

	// ----------------------- //

	/**
	 * The default constructor. If any arguments are invalid (null), an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param model
	 *            The model for which this controller provides a view.
	 * @param view
	 *            The view associated with this controller. This needs to be
	 *            instantiated by the sub-class.
	 * @param updateQueue
	 *            The queue (a ConcurrentLinkedQueue of {@link ISyncAction}s)
	 *            that is processed in the SimpleApplication's simpleUpdate()
	 *            thread. Any changes to the {@link #view} are performed by
	 *            adding a new action to this queue.
	 */
	public AbstractController(IUpdateable model, AbstractView view,
			ConcurrentLinkedQueue<ISyncAction> updateQueue) {

		// Set the model and register with it. It should not be null.
		this.model = (model != null ? model : new ICEObject());
		this.model.register(this);

		// Set the view. If it is null, create a new, basic view.
		this.view = (view != null ? view : new AbstractView("Invalid View") {
		});

		// Set the update queue. If it is null, create a new queue.
		this.updateQueue = (updateQueue != null ? updateQueue
				: new ConcurrentLinkedQueue<ISyncAction>());

		// Initialize the disposed boolean.
		disposed = new AtomicBoolean(false);

		// Initialize the list of listeners and its lock for concurrent access.
		listeners = new ArrayList<IControllerListener>();
		listenerLock = new ReentrantReadWriteLock(true);

		// ---- Initialize any base features. ---- //
		parentNode = null;
		// --------------------------------------- //

		return;
	}

	/**
	 * Sets the Node to which the associated {@link AbstractView} is attached.
	 * 
	 * @param node
	 *            The new parent node for the associated {@link AbstractView}.
	 */
	public void setParentNode(Node node) {

		// If the parent node is not null and is different, we can attach the
		// view to the new parent.
		if (node != null && node != parentNode) {
			this.parentNode = node;

			// If the controller is not disposed, we should try to attach the
			// view to the new parent.
			if (!disposed.get()) {
				updateQueue.add(new ISyncAction() {
					public void simpleUpdate(float tpf) {
						view.setParentNode(parentNode);
					}
				});
			}
		}

		return;
	}

	/**
	 * Disposes of the AbstractController and its associated
	 * {@link AbstractView}.
	 */
	public void dispose() {

		// If we have not already disposed of the controller, we need to
		// unregister from the IUpdateable model and, if possible, dispose of
		// the AbstractView managed by this controller.
		if (disposed.compareAndSet(false, true)) {
			// Unregister from the model.
			model.unregister(this);

			// Add a new update action to remove the view.
			updateQueue.add(new ISyncAction() {
				public void simpleUpdate(float tpf) {
					view.dispose();
				}
			});
		}

		return;
	}

	/**
	 * Registers a new {@link IControllerListener} to listen for updates to this
	 * controller or its view when its
	 * {@link #notifyControllerListeners(String)} method is called.
	 * 
	 * @param listener
	 *            The object that wants to listen to the controller.
	 * @return True if the listener was added successfully, false otherwise.
	 *         Duplicates are not allowed.
	 */
	public final boolean addControllerListener(IControllerListener listener) {

		boolean success = false;

		// We cannot add a null listener!
		if (listener != null) {
			// Acquire the write lock since we will be modifying the list of
			// listeners.
			listenerLock.writeLock().lock();
			try {
				// Since we do not allow duplicates, we need to check that the
				// listener does not already exist. We also want to base it on
				// actual references instead of objects that pass .equals().
				boolean exists = false;
				for (IControllerListener existingListener : listeners) {
					if (existingListener == listener) {
						exists = true;
						break;
					}
				}

				// Add the listener if it's not already in the list.
				if (!exists) {
					success = listeners.add(listener);
				}
			} finally {
				listenerLock.writeLock().unlock();
			}
		}

		return success;
	}

	/**
	 * Unregisters a {@link IControllerListener} from listening for updates to
	 * this controller or its view when its
	 * {@link #notifyControllerListeners(String)} method is called.
	 * 
	 * @param listener
	 *            The object that no longer wants to listen to the controller.
	 * @return True if the listener was removed successfully, false otherwise.
	 *         Duplicates are not allowed, so a listener cannot be removed twice
	 *         in a row.
	 */
	public final boolean removeControllerListener(IControllerListener listener) {

		boolean success = false;

		// We do not allow null listeners!
		if (listener != null) {
			// Acquire the write lock since we will be modifying the list of
			// listeners.
			listenerLock.writeLock().lock();
			try {
				// Loop over the current listeners and remove it if it is the
				// same reference as the argument. Since we do not allow
				// duplicate listeners, we can break on the first match.
				Iterator<IControllerListener> iterator;
				for (iterator = listeners.iterator(); iterator.hasNext();) {
					if (iterator.next() == listener) {
						iterator.remove();
						success = true;
						break;
					}
				}
			} finally {
				listenerLock.writeLock().unlock();
			}
		}

		return success;
	}

	/**
	 * Notifies all {@link IControllerListener}s that this controller was
	 * updated.
	 * 
	 * @param key
	 *            A key specifying the type of update that was performed.
	 */
	protected final void notifyControllerListeners(final String key) {

		// The update operations for listeners may be time consuming. We also
		// want to make the list of listeners safer for a multi-threaded
		// environment. To get around this problem, we acquire the list's read
		// lock and then copy its contents to a local, final list. The notifier
		// thread uses this local copy of the list.

		// Create an empty list to store the current list of listeners.
		final List<IControllerListener> currentListeners;
		currentListeners = new ArrayList<IControllerListener>();

		// Get the current list of listeners. Use the read lock to prevent
		// concurrent modification exceptions!
		listenerLock.readLock().lock();
		try {
			currentListeners.addAll(listeners);
		} finally {
			listenerLock.readLock().unlock();
		}

		// If there are listeners, then we should notify them.
		if (!currentListeners.isEmpty()) {
			// Create a new thread to notify all the listeners of the change
			// with the specified key.
			Thread notifierThread = new Thread() {
				@Override
				public void run() {
					for (IControllerListener listener : currentListeners) {
						listener.update(key, AbstractController.this);
					}
				}
			};
			// Start the notification thread.
			notifierThread.start();
		}

		return;
	}

	/**
	 * Updates the controller and/or view if the {@link #model} has changed.
	 */
	public abstract void update(IUpdateable component);

}
