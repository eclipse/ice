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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.ice.client.widgets.jme.IRenderQueue;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.Reactor;

import com.jme3.bounding.BoundingBox;

/**
 * This class provides a controller for Reactors and links the {@link Reactor}
 * model with the {@link ReactorView}. Any updates to the view should be
 * coordinated through this class.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorController extends AbstractPlantController implements
		IPlantControllerManagerListener {

	/**
	 * The {@link Reactor} model for which this controller provides a view.
	 */
	private final Reactor model;
	/**
	 * The {@link ReactorView} associated with this controller.
	 */
	private final ReactorView view;

	// ---- Additional Features ---- //
	// TODO Any features that are not a part of the model but are configurable
	// in Peacock should go here.
	// ----------------------------- //

	// ---- Model/View synchronization ---- //
	// This class listens to the Junction model as an IUpdateableListener (see
	// AbstractController). The update method from the interface only notifies
	// that the Junction has somehow changed, so we need a way to update the
	// view based on the current pipes without just recomputing all of the
	// bounding boxes. We use these two maps below to contain the current
	// input and output pipes that are displayed in the view.

	/**
	 * The manager of all controllers for the applications. When the pipes
	 * contained by this reactor change, we will need to get jME3 bounds for the
	 * pipes from their controllers. We should query the controller manager to
	 * get references to the pipe controllers.
	 */
	private final PlantControllerManager controllers;
	/**
	 * The control channel pipes whose bounding boxes are contained within the
	 * {@link #view}.
	 */
	private final HashMap<Integer, Pipe> coreChannels;

	// ------------------------------------ //

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model (a {@link Reactor}) for which this controller
	 *            provides a view.
	 * @param view
	 *            The view (a {@link ReactorView}) associated with this
	 *            controller.
	 * @param renderQueue
	 *            The queue responsible for tasks that need to be performed on
	 *            the jME rendering thread.
	 * @param manager
	 *            A {@link PlantControllerManager} used for looking up
	 *            {@link PipeController}s for the current {@link Pipe}s
	 *            contained by the Reactor.
	 */
	public ReactorController(Reactor model, ReactorView view,
			IRenderQueue renderQueue,
			PlantControllerManager manager) {
		super(model, view, renderQueue);

		// Set the model. If it is null, create a new, default model.
		this.model = (model != null ? model : new Reactor());

		// Set the view. If it is null, create a new, default view.
		this.view = (view != null ? view
				: new ReactorView("Invalid View", null));

		// Set the controller manager used to look up PipeControllers.
		this.controllers = (manager != null ? manager
				: new PlantControllerManager());

		// ---- Initialize any additional features. ---- //
		coreChannels = new HashMap<Integer, Pipe>();
		// --------------------------------------------- //

		// If any of the arguments were invalid, we should throw an exception
		// now after all class variables have been initialized.
		if (model == null) {
			throw new IllegalArgumentException(
					"ReactorController error: Model is null!");
		} else if (view == null) {
			throw new IllegalArgumentException(
					"ReactorController error: View is null!");
		} else if (renderQueue == null) {
			throw new IllegalArgumentException(
					"ReactorController error: Update queue is null!");
		} else if (controllers == null) {
			throw new IllegalArgumentException(
					"ReactorController error: Controller manager is null!");
		}
		
		// Register with the controller manager as an
		// IPlantControllerManagerListener.
		this.controllers.registerListener(this);

		// The view should not be attached to the scene yet, so we should be
		// able to synchronize it with the current model.
		
		// ---- Synchronize the initial view with the model. ---- //
		List<Integer> ids = new ArrayList<Integer>();
		List<BoundingBox> boxes = new ArrayList<BoundingBox>();
		
		for (Pipe pipe : model.getCoreChannels()) {
			int id = pipe.getId();
			
			// Add the ID and BoundingBox to the list that will be used to 
			// update the view.
			
			PipeController controller = (PipeController) controllers
					.getController(pipe);
			if (controller != null) {
				ids.add(id);
				boxes.add(controller.getBounds());
			}
			
			// Register with the pipe and add it to the map of displayed pipes.
			coreChannels.put(id, pipe);
			pipe.register(this);
		}
		
		// Update the view.
		view.putPipes(ids, boxes);
		// ------------------------------------------------------ //

		return;
	}

	/**
	 * Updates the {@link #view} depending on the changes in the {@link model}.
	 * It also updates the view based on changes to pipes contained in this
	 * reactor.
	 */
	public void update(IUpdateable component) {

		// If the updated component is the reactor, then pipes may have been
		// removed or added. We should update the view based on these pipes.
		if (component == model) {
			// Get the current control channel pipes from the reactor model.
			List<CoreChannel> pipes = (List<CoreChannel>) model
					.getCoreChannels();

			// Create the lists that will need to be passed to the view.
			final List<Integer> removedIds = new ArrayList<Integer>();
			final List<Pipe> addedPipes = new ArrayList<Pipe>();

			Set<Integer> unusedPipes = new HashSet<Integer>(
					coreChannels.keySet());

			// Determine all of the added and removed pipes.
			for (Pipe pipe : pipes) {
				int id = pipe.getId();
				if (coreChannels.containsKey(id)) {
					unusedPipes.remove(id);
				} else {
					// Add the ID and controller to the list that will be used
					// to update the view.
					addedPipes.add(pipe);

					// Register with the pipe and add it to the map of input
					// pipes.
					coreChannels.put(id, pipe);
					pipe.register(this);
				}
			}
			// Add unused pipe IDs to the list of removed IDs.
			for (int id : unusedPipes) {
				// Add the ID to the list that will be used to update the view.
				removedIds.add(id);

				// Unregister from the pipe and remove it from the map of 
				// control channel pipes.
				coreChannels.remove(id).unregister(this);
			}

			// If pipes have been added or removed, refresh the view. This needs
			// to be done in an ISyncAction because the current bounding boxes
			// for the pipes may not have been updated in the simple update
			// thread yet.
			if (!removedIds.isEmpty() || !addedPipes.isEmpty()) {
				renderQueue.enqueue(new Callable<Boolean>() {
					public Boolean call() {
						// Remove all old pipes from the view.
						view.removePipes(removedIds);
						
						// Initialize the lists of IDs and BoundingBoxes that need
						// to be added to the view.
						List<Integer> ids = new ArrayList<Integer>();
						List<BoundingBox> boxes = new ArrayList<BoundingBox>();

						// Add the IDs and boxes for all new input and output pipes.
						// We need to get the current bounding boxes from the
						// controllers.
						PipeController controller;
						for (Pipe pipe : addedPipes) {
							controller = (PipeController) controllers
									.getController(pipe);
							if (controller != null) {
								ids.add(pipe.getId());
								boxes.add(controller.getBounds());
							}
						}
						// Add all the new pipes to the view.
						view.putPipes(ids, boxes);

						return true;
					}
				});
			}
		}
		// If the updated component is a pipe that this reactor is listening to,
		// then we should send the pipe's current position to the view.
		else {
			// Get the changed pipe and its ID.
			Pipe pipe = (Pipe) component;
			int id = pipe.getId();
			// Initialize the two lists that need to be sent to the view. Since
			// we are only updating a single pipe, we only need the single
			// pipe's ID and bounding box.
			final List<Integer> ids = new ArrayList<Integer>(1);
			final List<BoundingBox> boxes = new ArrayList<BoundingBox>(1);
			// Get the PipeController for the new pipe.
			final PipeController controller = (PipeController) controllers
					.getController(pipe);
			
			if (controller != null) {
				ids.add(id);
	
				// Update the view with the pipe's current bounds synchronized
				// with the simple update thread.
				renderQueue.enqueue(new Callable<Boolean>() {
					public Boolean call() {
						boxes.add(controller.getBounds());
						view.putPipes(ids, boxes);
						return true;
					}
				});
			}
		}

		return;
	}

	// ---- Implements IPlantControllerManagerListener ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.reactoreditor.plant.
	 * IPlantControllerManagerListener
	 * #addedController(org.eclipse.ice.reactor.plant.PlantComponent,
	 * org.eclipse.ice
	 * .client.eclipse.widgets.reactoreditor.plant.AbstractPlantController)
	 */
	public void addedController(PlantComponent component,
			AbstractPlantController controller) {
		
		if (component != null && controller != null) {
			// Get the changed pipe and its ID.
			int id = component.getId();
			Pipe pipe = coreChannels.get(id);
			
			// If it's actually a core channel, we need to update the view.
			if (component == pipe) {
				// Initialize the two lists that need to be sent to the view.
				// Since we are only updating a single pipe, we only need the
				// single pipe's ID and bounding box.
				final List<Integer> ids = new ArrayList<Integer>(1);
				final List<BoundingBox> boxes = new ArrayList<BoundingBox>(1);
				final PipeController pipeController = (PipeController) controller;

				ids.add(id);

				// Update the view with the pipe's current bounds synchronized
				// with the simple update thread.
				renderQueue.enqueue(new Callable<Boolean>() {
					public Boolean call() {
						boxes.add(pipeController.getBounds());
						view.putPipes(ids, boxes);
						return true;
					}
				});
			}
		}
		
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.reactoreditor.plant.
	 * IPlantControllerManagerListener
	 * #removedController(org.eclipse.ice.reactor.plant.PlantComponent)
	 */
	public void removedController(PlantComponent component) {

		if (component != null) {
			// Get the changed pipe and its ID.
			int id = component.getId();
			Pipe pipe = coreChannels.get(id);

			// If it's actually a core channel, we need to update the view.
			if (component == pipe) {

				final List<Integer> ids = new ArrayList<Integer>(1);
				ids.add(id);

				// Remove the pipe from the view.
				renderQueue.enqueue(new Callable<Boolean>() {
					public Boolean call() {
						view.removePipes(ids);
						return true;
					}
				});
			}
		}

		return;
	}
	// ---------------------------------------------------- //

}