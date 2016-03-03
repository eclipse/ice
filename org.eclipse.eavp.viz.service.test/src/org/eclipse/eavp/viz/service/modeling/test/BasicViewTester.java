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

import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IView;
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
public class BasicViewTester {

	/**
	 * The entities for the component
	 */
	List<IController> entities;

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
		entities = new ArrayList<IController>();
		component = new TestMesh(entities);
		view = new TestView();
		controller = new TestController(component, view);
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
		IView object = new BasicView();
		IView equalObject = new BasicView();
		IView unequalObject = new BasicView();

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