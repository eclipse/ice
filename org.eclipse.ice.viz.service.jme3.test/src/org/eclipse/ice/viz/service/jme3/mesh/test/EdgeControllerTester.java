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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.ice.viz.service.jme3.mesh.AbstractMeshController;
import org.eclipse.ice.viz.service.jme3.mesh.EdgeController;
import org.eclipse.ice.viz.service.jme3.mesh.test.EdgeControllerTester.TestEdgeController.TestPropertyHandler;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.junit.Test;

import com.jme3.material.Material;

/**
 * <p>
 * Checks the EdgeController for changes to its fields and for connections with
 * its corresponding model (an Edge).
 * </p>
 * 
 * @author Jordan H. Deyton
 */

public class EdgeControllerTester {
	/**
	 * <p>
	 * This test ensures that the EdgeController registers, updates, and
	 * unregisters properly with its corresponding model (an IUpdateable).
	 * </p>
	 * 
	 */
	@Test
	public void checkUpdate() {
		// Create the endpoints
		Vertex start = new Vertex(1.0f, 1.0f, 1.0f);
		start.setId(0);
		Vertex end = new Vertex(2.0f, 2.0f, 2.0f);
		end.setId(1);

		// Create the edge
		Edge edge = new Edge(start, end);

		// Get a TestPropertyHandler. This must be done through a dummy
		// EdgeController because PropertyHandler is a private class.
		TestEdgeController dummy = new TestEdgeController(edge,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material(), null);
		TestPropertyHandler handler = dummy.getHandler();

		// Create the controller
		TestEdgeController controller = new TestEdgeController(edge,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material(), handler);

		// Change one of the endpoints and see if the handler received an
		// update.
		start.setLocation(3.0f, 3.0f, 3.0f);
		
		//Give the update thread time to notify the PropertyHandler
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			fail("Test thread could not wait.");
		}
		
		//Have the controller update any dirty values.
		controller.syncView();
		
		//Check that the property handler was updated.
		assertTrue(handler.wasUpdated());

	}

	/**
	 * <p>
	 * Tests the equals and hashCode methods.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquals() {
		// Create the endpoints
		Vertex start = new Vertex(1.0f, 1.0f, 1.0f);
		start.setId(0);
		Vertex end = new Vertex(2.0f, 2.0f, 2.0f);
		end.setId(1);

		// Create the edge
		Edge edge = new Edge(start, end);

		// Create the controller.
		EdgeController controller = new EdgeController(edge,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material());

		// Check that a controller is equal to itself.
		assertTrue(controller.equals(controller));
		assertTrue(controller.hashCode() == controller.hashCode());

		// Create a controller from the same edge.
		EdgeController controllerCopy = new EdgeController(edge,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material());
		assertTrue(controller.equals(controllerCopy));
		assertTrue(controller.hashCode() == controllerCopy.hashCode());

		// Create a different controller.
		Vertex start2 = new Vertex(1.0f, 1.0f, 1.0f);
		start2.setId(2);
		Vertex end2 = new Vertex(2.0f, 2.0f, 2.0f);
		end2.setId(3);
		Edge edge2 = new Edge(start2, end2);
		EdgeController controllerDifferent = new EdgeController(edge2,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material());

		// Check that the two controllers are not equal.
		assertFalse(controller.equals(controllerDifferent));
		assertFalse(controller.hashCode() == controllerDifferent.hashCode());
	}

	/**
	 * <p>
	 * Tests the copy and clone methods.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopy() {
		// Create the endpoints
		Vertex start = new Vertex(1.0f, 1.0f, 1.0f);
		start.setId(0);
		Vertex end = new Vertex(2.0f, 2.0f, 2.0f);
		end.setId(1);

		// Create the edge
		Edge edge = new Edge(start, end);

		// Create the controller.
		EdgeController controller = new EdgeController(edge,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material());

		// Create a different controller and copy the contents of the first into
		// it..
		Vertex start2 = new Vertex(1.0f, 1.0f, 1.0f);
		start2.setId(2);
		Vertex end2 = new Vertex(2.0f, 2.0f, 2.0f);
		end2.setId(3);
		Edge edge2 = new Edge(start2, end2);
		EdgeController controllerCopy = new EdgeController(edge2,
				new ConcurrentLinkedQueue<AbstractMeshController>(),
				new Material());
		controllerCopy.copy(controller);
		assertTrue(controller.equals(controllerCopy));

		// Clone the controller.
		EdgeController controllerClone = (EdgeController) controller.clone();
		assertTrue(controller.equals(controllerClone));

	}

	/**
	 * An extension of EdgeController exposing the class's PropertyHandler for
	 * testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	public class TestEdgeController extends EdgeController {

		/*
		 * The default constructor. Adds the given PropertyHandler to the
		 * controller.
		 */
		public TestEdgeController(Edge edge,
				ConcurrentLinkedQueue<AbstractMeshController> queue,
				Material material, PropertyHandler handler) {
			super(edge, queue, material);
			properties.put("location", handler);
		}

		/*
		 * Getter method for the TestPropertyHandler.
		 */
		public TestPropertyHandler getHandler() {
			return new TestPropertyHandler();
		}

		/*
		 * A simple implementation of PropertyHandler that only tracks whether
		 * or not it has received an update notification.
		 */
		public class TestPropertyHandler extends PropertyHandler {
			
			//Whether the handler has received an update
			boolean updated = false;

			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ice.viz.service.jme3.mesh.AbstractMeshController.PropertyHandler#syncView()
			 */
			@Override
			public void syncView() {
				updated = true;
			}

			/*
			 * Getter for the updated variable.
			 */
			public boolean wasUpdated() {
				return updated;
			}
		}
	}
}