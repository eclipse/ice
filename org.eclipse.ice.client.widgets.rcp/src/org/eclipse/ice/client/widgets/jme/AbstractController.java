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
package org.eclipse.ice.client.widgets.jme;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.viz.service.jme3.application.IRenderQueue;
import org.eclipse.ice.viz.service.jme3.widgets.AbstractView;

import com.jme3.scene.Node;

/**
 * This class provides a base implementation for a controller that manages an
 * {@link IUpdateable} model and an {@link AbstractView} associated with that
 * model. It ensures that changes to the model or features provided by the
 * controller itself are synchronized with the view through the parent
 * SimpleApplication's simpleUpdate() thread.
 * 
 * @author Jordan Deyton
 * 
 */
public abstract class AbstractController implements IUpdateableListener {
	/*
	 * Note: We previously had an IControllerListener interface whereby objects
	 * could listen for changes to an AbstractController. If this interface is
	 * required for future work, this package (widgets.jme) and the plant
	 * package (widgets.reactoreditor.plant) should be selectively reverted to
	 * restore IControllerListener, AbstractController, and PipeController to
	 * their state before 20140903-1600. Also note that this change was
	 * committed to the branch.
	 */

	/**
	 * The model for which this controller provides a view.
	 */
	protected final IUpdateable model;
	/**
	 * The {@link AbstractView} associated with this controller.
	 */
	private final AbstractView view;

	/**
	 * The queue responsible for tasks that need to be performed on the jME
	 * rendering thread.
	 */
	protected final IRenderQueue renderQueue;
	/**
	 * Whether or not the controller and its view have been disposed.
	 */
	protected final AtomicBoolean disposed;

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
	 * @param renderQueue
	 *            The queue responsible for tasks that need to be performed on
	 *            the jME rendering thread.
	 */
	public AbstractController(IUpdateable model, AbstractView view,
			IRenderQueue renderQueue) {

		// Set the model and register with it. It should not be null.
		this.model = (model != null ? model : new ICEObject());
		this.model.register(this);

		// Set the view. If it is null, create a new, basic view.
		this.view = (view != null ? view : new AbstractView("Invalid View") {
		});

		// Set the update queue. If it is null, create a new queue.
		this.renderQueue = (renderQueue != null ? renderQueue
				: new IRenderQueue() {
					@Override
					public <T> Future<T> enqueue(Callable<T> callable) {
						return null;
					}
				});

		// Initialize the disposed boolean.
		disposed = new AtomicBoolean(false);

		// ---- Initialize any base features. ---- //
		parentNode = null;
		// --------------------------------------- //

		return;
	}

	/**
	 * @return The model for which this controller provides a view.
	 */
	public IUpdateable getModel() {
		return model;
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
				renderQueue.enqueue(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						view.setParentNode(parentNode);
						return true;
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
			renderQueue.enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					view.dispose();
					return true;
				}
			});
		}

		return;
	}

	/**
	 * Updates the controller and/or view if the {@link #model} has changed.
	 */
	@Override
	public abstract void update(IUpdateable component);

}
