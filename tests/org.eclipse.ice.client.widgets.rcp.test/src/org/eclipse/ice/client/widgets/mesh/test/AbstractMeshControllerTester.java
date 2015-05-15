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
package org.eclipse.ice.client.widgets.mesh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.eclipse.ice.client.widgets.mesh.AbstractMeshController;
import org.eclipse.ice.client.widgets.mesh.StateType;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.jme3.scene.Node;

/**
 * <p>
 * Tests that the AbstractMeshController hooks up with its model appropriately.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class AbstractMeshControllerTester {

	/**
	 * <p>
	 * This operation tests the construction of the AbstractMeshController
	 * class.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		ICEObject object = new ICEObject();
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// The AbstractMeshController to test. (TestMeshController adds nothing
		// to the real implementation.)
		TestMeshController controller;

		/* ---- Valid input. ---- */
		controller = new TestMeshController(object, queue);

		assertEquals(StateType.None, controller.getState());
		assertTrue((Object) controller.getParentNode() == null);
		/* ---------------------- */

		/* ---- Invalid input. ---- */
		// Null queue.
		try {
			controller = new TestMeshController(object, null);

			// If we've made it this far, an exception was not thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Null model.
		try {
			controller = new TestMeshController(null, queue);

			// If we've made it this far, an exception was not thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Both null.
		try {
			controller = new TestMeshController(null, null);

			// If we've made it this far, an exception was not thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ------------------------ */

		return;
	}

	/**
	 * <p>
	 * This test ensures that the AbstractMeshController registers, updates, and
	 * unregisters properly with its corresponding model (an IUpdateable).
	 * </p>
	 * 
	 */
	@Test
	public void checkUpdate() {

		/*
		 * This test creates a controller pointing to an ICEObject. It checks
		 * the controller correctly registers (via construction), updates (via
		 * changes to the ICEObject), and unregisters (via disposal).
		 */

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		ICEObject object = new ICEObject();
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// The AbstractMeshController to test. (TestMeshController adds nothing
		// to the real implementation.)
		TestMeshController controller;

		/* ---- Register with the object. ---- */
		controller = new TestMeshController(object, queue);
		/* ----------------------------------- */

		/* ---- Test updating. ---- */
		object.setName("Pew Pew Pew!");

		assertTrue(controller.wasUpdated());
		/* ------------------------ */

		/* ---- Unregister with the object. ---- */
		controller.dispose();
		/* ------------------------------------- */

		/* ---- Test updating. ---- */
		object.setDescription("Lightning never strikes the same place twice, but it can strike freaking everywhere!!");

		assertFalse(controller.wasUpdated());
		/* ------------------------ */

		return;
	}

	/**
	 * <p>
	 * Tests the ability to get and set the state of the view associated with
	 * this controller.
	 * </p>
	 * 
	 */
	@Test
	public void checkState() {

		/*
		 * Create a TestMeshController. Try setting its state to different
		 * values and make sure either the value changed and the queue was
		 * updated, or that the value did not change and the queue was not
		 * updated.
		 */

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		ICEObject object = new ICEObject();
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// The AbstractMeshController to test. (TestMeshController adds nothing
		// to the real implementation.)
		TestMeshController controller = new TestMeshController(object, queue);

		/* ---- Check the initial state. ---- */
		assertEquals(StateType.None, controller.getState());
		/* ---------------------------------- */

		/* ---- Set the state to a null value. ---- */
		controller.setState(null);

		// Check the new state of the controller.
		assertEquals(StateType.None, controller.getState());
		// The queue should be empty (no change).
		assertTrue(queue.isEmpty());
		/* ---------------------------------------- */

		/* ---- Set the state to a new value. ---- */
		controller.setState(StateType.Selected);

		// Check the new state of the controller.
		assertEquals(StateType.Selected, controller.getState());
		// The controller should now be on the queue.
		assertTrue(queue.peek() == controller);

		// Reset the queue.
		queue.clear();
		/* --------------------------------------- */

		/* ---- Set the state to the same value. ---- */
		controller.setState(StateType.Selected);

		// Check the new state of the controller.
		assertEquals(StateType.Selected, controller.getState());
		// The queue should be empty (no change).
		assertTrue(queue.isEmpty());
		/* ------------------------------------------ */

		return;
	}

	/**
	 * <p>
	 * Tests the ability to get and set the parent jME3 Node associated with
	 * this controller.
	 * </p>
	 * 
	 */
	@Test
	public void checkParentNode() {

		/*
		 * Create a TestMeshController. Try setting its parent to different
		 * values and make sure either the value changed and the queue was
		 * updated, or that the value did not change and the queue was not
		 * updated.
		 */

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		ICEObject object = new ICEObject();
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// The AbstractMeshController to test. (TestMeshController adds nothing
		// to the real implementation.)
		TestMeshController controller = new TestMeshController(object, queue);

		Node node = new Node();

		/* ---- Check the initial parent node. ---- */
		assertTrue(controller.getParentNode() == null);
		/* ---------------------------------------- */

		/* ---- Set the node to a null value. ---- */
		controller.setParentNode(null);

		// Check the new state of the controller.
		assertTrue(controller.getParentNode() == null);
		// The queue should be empty (no change).
		assertTrue(queue.isEmpty());
		/* --------------------------------------- */

		/* ---- Set the node to a new value. ---- */
		controller.setParentNode(node);

		// Check the new state of the controller.
		assertEquals(node, controller.getParentNode());
		// The controller should now be on the queue.
		assertTrue(queue.peek() == controller);

		// Reset the queue.
		queue.clear();
		/* -------------------------------------- */

		/* ---- Set the node to the same value. ---- */
		controller.setParentNode(node);

		// Check the new state of the controller.
		assertEquals(node, controller.getParentNode());
		// The queue should be empty (no change).
		assertTrue(queue.isEmpty());
		/* ----------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests that the controller tries to updates its view when necessary.
	 * </p>
	 * 
	 */
	@Test
	public void checkSyncView() {

		/*
		 * For this test, we create a simple thread to call syncView for each
		 * controller in a concurrent queue.
		 * 
		 * Controllers will put themselves on the queue when their models have
		 * been updated. We use the TestMeshController to tell when the syncView
		 * method was called. This makes sure the AbstractMeshController's
		 * implementation's correctly load them into the queue.
		 */

		// Set up a concurrent queue.
		final ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// Use this flag in the update thread to tell the thread to terminate
		// when we are done testing.
		final AtomicBoolean running = new AtomicBoolean(true);

		// Create a thread to process updates to the controller's view.
		Thread updateThread = new Thread() {
			@Override
			public void run() {

				// Keep processing events.
				while (running.get()) {
					// Process all available events in the queue.
					while (!queue.isEmpty()) {
						queue.poll().syncView();
					}
				}
				return;
			}
		};
		// Start the thread.
		updateThread.start();

		// Create a controller.
		ICEObject model = new ICEObject();
		TestMeshController controller = new TestMeshController(model, queue);

		// No syncs yet.
		assertFalse(controller.wasSynced());

		// Set a name.
		model.setName("010011110100110101000111");
		assertTrue(controller.wasSynced());
		// Set a description.
		model.setDescription("Pew Pew Pew!");
		assertTrue(controller.wasSynced());

		// Stop the update thread.
		running.set(false);
		try {
			updateThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * <p>
	 * Tests the equals and hashCode methods.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquals() {

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		ICEObject model1 = new ICEObject();
		ICEObject model2 = new ICEObject();
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// Initialize objects for testing.
		AbstractMeshController object = new TestMeshController(model1, queue);
		AbstractMeshController equalObject = new TestMeshController(model1,
				queue);
		AbstractMeshController unequalObject = new TestMeshController(model2,
				queue);

		// Set up the object and equalObject.
		model1.setId(2);

		// Set up the unequalObject.
		model2.setId(3);

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object == null);
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());
		assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
	}

	/**
	 * <p>
	 * Tests the copy and clone methods.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopy() {

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		ICEObject model1 = new ICEObject();
		ICEObject model2 = new ICEObject();
		model2.setId(2);
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// Initialize objects for testing.
		AbstractMeshController object = new TestMeshController(model1, queue);
		AbstractMeshController copy = new TestMeshController(model2, queue);
		AbstractMeshController clone = null;

		// Make sure the objects are not equal before copying.
		assertFalse(object == copy);
		assertFalse(object.equals(copy));

		// Copy the object.
		copy.copy(object);

		// Make sure the references are different but contents the same.
		assertFalse(object == copy);
		assertTrue(object.equals(copy));

		// Do the same for the clone operation.

		// Make sure the objects are not equal before copying.
		assertFalse(object == clone);
		assertFalse(object.equals(clone));

		// Copy the object.
		clone = (AbstractMeshController) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}

}