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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.datastructures.VizObject.test.TestManagedListener;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestController;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestMesh;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestView;
import org.junit.Before;
import org.junit.Test;

/**
 * A class to test the functionality of the AbstractMeshComponent.
 * 
 * @author Robert Smith
 *
 */
public class AbstractMeshTester {

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
	 * Tests the functionality of the properties map.
	 */
	@Test
	public void testGetProperty() {
		// An empty property should return null
		assertNull(component.getProperty("empty"));

		// Set a property and test that its value is correct
		component.setProperty("test property", "test value");
		assertTrue("test value".equals(component.getProperty("test property")));

		// Set a new value to the previous property and test that the value is
		// changed
		component.setProperty("test property", "new value");
		assertTrue("new value".equals(component.getProperty("test property")));
	}

	/**
	 * Test the functionality of the entities map.
	 */
	@Test
	public void testEntities() {
		// Check that the map of entities is empty
		assertEquals(0, component.getEntities().size());

		// Create a new VizObject with id 2
		AbstractController object = new AbstractController(new AbstractMesh(),
				new AbstractView());
		object.setProperty("Id", "2");

		// Add the object as a child
		component.addEntity(object);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the controller now has a map with one entity with id 2.
		assertEquals(1, component.getEntities().size());
		assertTrue(
				"2".equals(component.getEntities().get(0).getProperty("Id")));

		// Create a new part with id 3
		AbstractController secondObject = new AbstractController(
				new AbstractMesh(), new AbstractView());
		object.setProperty("Id", "3");

		// Add a second entity
		component.addEntity(secondObject);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that there are two entities
		assertEquals(2, component.getEntitiesByCategory("Default").size());

		// Remove one of the entities
		component.removeEntity(object);

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that there is only one entity
		assertEquals(1, component.getEntities().size());
	}

	/**
	 * Tests the category functionality of the entities map.
	 */
	@Test
	public void testEntityCategories() {

		// Create an edge entity
		AbstractController edge = new AbstractController(new AbstractMesh(),
				new AbstractView());
		edge.setProperty("Name", "edge");
		component.addEntityByCategory(edge, "Edges");

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Create a vertex entity
		AbstractController vertex1 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		vertex1.setProperty("Name", "vertex1");
		component.addEntityByCategory(vertex1, "Vertices");

		// Create another vertex entity
		AbstractController vertex2 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		vertex2.setProperty("Name", "vertex2");
		component.addEntityByCategory(vertex2, "Vertices");

		// Check that there are three entities, 1 edge, and 2 vertices
		assertEquals(1, component.getEntitiesByCategory("Edges").size());
		assertEquals(2, component.getEntitiesByCategory("Vertices").size());

		// Check that the edge is in the Edges category
		assertTrue("edge".equals(component.getEntitiesByCategory("Edges").get(0)
				.getProperty("Name")));

		// Create a list of all the names in the Vertices category
		ArrayList<String> vertexNames = new ArrayList<String>();
		for (AbstractController object : component
				.getEntitiesByCategory("Vertices")) {
			vertexNames.add(object.getProperty("Name"));
		}

		// Check that the two vertices were in the right category
		assertTrue(vertexNames.contains("vertex1"));
		assertTrue(vertexNames.contains("vertex2"));

		// Check that empty categories return empty lists
		assertNotNull(component.getEntitiesByCategory("empty category"));
		assertEquals(0,
				component.getEntitiesByCategory("empty category").size());
	}

	/**
	 * Tests the observer pattern registration.
	 */
	@Test
	public void testRegisteration() {

		// Create and register an object
		AbstractController object = new AbstractController(new AbstractMesh(),
				new AbstractView());
		object.register(component);

		// Set the object's id. This should trigger an update.
		object.setProperty("Id", "2");

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the component got the update.
		assertTrue(component.isUpdated());

		// Unregister the object
		object.unregister(component);

		// Unregistering causes a notification, so clear the updated flag
		component.isUpdated();

		// Set the object's id, triggering an update.
		object.setProperty("Id", "3");

		// The component should not have received the update.
		assertFalse(component.isUpdated());
	}

	/**
	 * Check that AbstractController's equality testing is correct
	 */
	@Test
	public void testEquality() {

		// Create a mesh
		AbstractMesh mesh = new TestMesh(new ArrayList<AbstractController>());
		mesh.setProperty("Equal", "True");

		// Create a mesh equal to the first
		AbstractMesh equalMesh = new TestMesh(
				new ArrayList<AbstractController>());
		equalMesh.setProperty("Equal", "True");

		// Create a mesh which is not equal to the first
		AbstractMesh inequalMesh = new TestMesh(
				new ArrayList<AbstractController>());
		inequalMesh.setProperty("Equal", "False");

		// A mesh should equal itself
		assertTrue(mesh.equals(mesh));

		// A mesh should equal a controller with a equal properties and entities
		// maps
		assertTrue(mesh.equals(equalMesh));

		// A mesh should not equal a mesh with different properties
		assertFalse(mesh.equals(inequalMesh));

		// Set the properties to be equal
		inequalMesh.setProperty("Equal", "True");

		// The two objects should now be equal
		assertTrue(mesh.equals(inequalMesh));

		// Set the entities to be inequal
		AbstractController child = new AbstractController(equalMesh,
				new AbstractView());
		inequalMesh.addEntityByCategory(child, "Test");

		// The objects should be inequal again
		assertFalse(mesh.equals(inequalMesh));

		// Add the same object under a different category
		mesh.addEntityByCategory(child, "Wrong");
		assertFalse(mesh.equals(inequalMesh));

		// Remove the child and put it in the same category
		mesh.removeEntity(child);
		mesh.addEntityByCategory(child, "Test");

		// The meshes should be equal again
		assertTrue(mesh.equals(inequalMesh));

		// Check that a cloned controller is equal to the original
		AbstractMesh clone = (AbstractMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}

	/**
	 * Check that the mesh sends the proper updates
	 */
	@Test
	public void checkUpdates() {

		// Create a listener for the object
		ArrayList<SubscriptionType> allList = new ArrayList<SubscriptionType>();
		allList.add(SubscriptionType.ALL);
		TestManagedListener listener = new TestManagedListener(allList);
		component.register(listener);

		// Changing a mesh's properties should fire a PROPERTY update
		component.setProperty("Test", "Value");
		assertTrue(listener.gotProperty());
		assertFalse(listener.gotSelection());

		// Changing the Selected property should instead fire a SELECTION update
		component.setProperty("Selected", "True");
		assertFalse(listener.gotProperty());
		assertTrue(listener.gotSelection());

		// Create a second object, add it to the mesh, and check that a CHILD
		// update was fired
		AbstractController child = new AbstractController(new AbstractMesh(),
				new AbstractView());
		component.addEntity(child);
		assertTrue(listener.gotChild());

		// Remove the object and check that this also fires a CHILD update
		component.removeEntity(child);
		assertTrue(listener.gotChild());

		// Remove an object that isn't there. This should not fire an update.
		component.removeEntity(child);
		assertFalse(listener.gotChild());

		// Make the component a copy of something else. This should fire every
		// type of update.
		component.copy(new AbstractMesh());
		assertTrue(listener.gotChild());
		assertTrue(listener.gotProperty());
		assertTrue(listener.gotSelection());
		assertTrue(listener.gotWireframe());
		assertTrue(listener.gotTransformation());
	}
}
