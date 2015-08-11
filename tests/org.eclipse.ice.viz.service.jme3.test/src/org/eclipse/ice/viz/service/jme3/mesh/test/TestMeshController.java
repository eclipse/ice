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
package org.eclipse.ice.viz.service.jme3.mesh.test;

import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.viz.service.datastructures.IVizUpdateable;
import org.eclipse.ice.viz.service.jme3.mesh.AbstractMeshController;

/**
 * <p>
 * This class provides a subclass of AbstractMeshController to test its basic
 * functions that are not required to be implemented by subclasses. For update()
 * and syncView() calls, it marks a flag that can be requested to check that the
 * AbstractMeshController properly registers with the provided IUpdateable.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class TestMeshController extends AbstractMeshController {

	/** 
	 * <p>A stored reference to the model managed by the controller. Required for the implementation of clone.</p>
	 */
	private IVizUpdateable model;
	
	/** 
	 * <p>Whether or not the controller's update method was called.</p>
	 */
	private AtomicBoolean wasUpdated = new AtomicBoolean(false);
	
	/** 
	 * <p>Whether or not the controller's syncView method was called.</p>
	 */
	private AtomicBoolean wasSynced = new AtomicBoolean(false);
	
	/**
	 * <p>The default constructor. Does nothing but call the AbstractMeshController constructor.</p>
	 * @param model The model. For testing purposes, this can be an ICEObject.
	 * @param queue The queue used to push updates to the views.
	 */
	public TestMeshController(IVizUpdateable model,
			ConcurrentLinkedQueue<AbstractMeshController> queue) {
		super(model, queue);
		
		// Store a reference to the model.
		this.model = model;

		return;
	}

	/** 
	 * <p>Implements the syncView method of AbstractMeshController to mark a flag when the view is synced.</p>
	 */
	@Override
	public void syncView() {
		
		// Update the wasSynced flag.
		wasSynced.set(true);
		
		return;
	}
	
	/** 
	 * <p>Overrides the update method of AbstractMeshController to mark a flag when the controller is notified.</p>
	 * @param component <p>The component that has been updated.</p>
	 */
	@Override
	public void update(IVizUpdateable component) {
				
		// Update the wasUpdated flag.
		wasUpdated.set(true);
		// Proceed with the normal update process.
		super.update(component);
		
		return;
	}

	/** 
	 * <p>Implements the clone method of AbstractMeshController.</p>
	 */
	@Override
	public Object clone() {
		// Initialize a new object.
		TestMeshController object = new TestMeshController((IVizUpdateable) model.clone(), updateQueue);

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}
	
	/** 
	 * <p>Gets whether or not the controller's update method was called. This also resets the wasUpdated value to false after the call.</p>
	 * @return <p>True if the controller's update method was called, false otherwise.</p>
	 */
	public boolean wasUpdated() {
		
		// Wait a half second so that the thread can work.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// Complain and fail
			e.printStackTrace();
			fail();
		}
		
		// Reset wasUpdated and return its previous value.
		return wasUpdated.getAndSet(false);
	}
	
	/** 
	 * <p>Gets whether or not the controller's syncView method was called. This also resets the wasSynced value to false after the call.</p>
	 * @return <p>True if the controller's syncView method was called, false otherwise.</p>
	 */
	public boolean wasSynced() {
		
		// Wait a half second so that the thread can work.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// Complain and fail
			e.printStackTrace();
			fail();
		}
		
		// Reset wasUpdated and return its previous value.
		return wasSynced.getAndSet(false);
	}
	
}
