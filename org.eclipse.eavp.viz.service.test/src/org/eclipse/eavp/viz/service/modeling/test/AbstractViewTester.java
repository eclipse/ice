/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.Transformation;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestController;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestMesh;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestView;
import org.junit.Before;
import org.junit.Test;

/**
 * A class for testing the functionality of the AbstractView.
 * 
 * @author Robert Smith
 *
 */
public class AbstractViewTester {

	/**
	 * The entities for the component
	 */
	List<AbstractController> entities;

	/**
	 * The controller's model
	 */
	TestMesh component;

	/**
	 * The controller's view
	 */
	TestView view;

	/**
	 * The controller to test
	 */
	TestController controller;

	/**
	 * Initialize the controller before each test.
	 */
	@Before
	public void beforeEachTest() {
		entities = new ArrayList<AbstractController>();
		component = new TestMesh(entities);
		view = new TestView();
		controller = new TestController(component, view);
	}

	/**
	 * Tests the view's ability to keep track of the previous and current
	 * transformations.
	 */
	@Test
	public void testSynched() {
		// Get the previous transformation
		Transformation prev = view.getPreviousTransformation();

		// Initially, the previous and current transformations should be
		// identical
		assertTrue(view.getTransformation().equals(prev));

		// Change the transformation
		view.getTransformation().setSize(5);

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(view.isUpdated());

		// Makes sure the previous transformation is different from the current
		// one
		assertFalse(view.getTransformation()
				.equals(view.getPreviousTransformation()));

		// Change the transformation a second time
		view.getTransformation().setSize(10);

		// Make sure the previous transformation is different from the current
		// one
		assertFalse(view.getTransformation()
				.equals(view.getPreviousTransformation()));

		// Make sure that the previous transformation hasn't changed
		assertTrue(prev.equals(view.getPreviousTransformation()));

		// Create a new transformation and set it as the controller's
		// transformation
		Transformation transform = new Transformation();
		transform.setSize(20);
		view.setTransformation(transform);

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(view.isUpdated());

		// Make sure that the previous transformation hasn't changed
		assertFalse(view.getTransformation()
				.equals(view.getPreviousTransformation()));

		// Make sure that the previous transformation hasn't changed
		assertTrue(prev.equals(view.getPreviousTransformation()));

		// Synchronize the controller
		view.setSynched();

		// Make sure the previous transformation has been updated.
		assertTrue(view.getTransformation()
				.equals(view.getPreviousTransformation()));

		// Assert the previous transformation has the correct size
		assertEquals(0,
				Double.compare(20, view.getPreviousTransformation().getSize()));
	}

	/**
	 * Test the view's ability to notify observers of changes.
	 */
	@Test
	public void testUpdateNotification() {

		// Update the transformation
		view.getTransformation().setSize(2);

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the view was notified
		assertTrue(view.isUpdated());

		// Set a new transformation
		view.setTransformation(new Transformation());

		// Check that the view sent a notification
		assertTrue(controller.isUpdated());

	}

	/**
	 * Check the AbstractView's equality testing.
	 */
	@Test
	public void checkEquality() {

		// Create objects for testing
		AbstractView object = new AbstractView();
		AbstractView equalObject = new AbstractView();
		AbstractView unequalObject = new AbstractView();

		// Give one view a different transformation
		Transformation transformation = new Transformation();
		transformation.setSize(2);
		unequalObject.setTransformation(transformation);

		// An object should equal itself
		assertTrue(object.equals(object));

		// Check that a comparison of two equal objects returns true
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that a comparison of two unequal objects returns false
		assertFalse(object.equals(unequalObject));
	}
}