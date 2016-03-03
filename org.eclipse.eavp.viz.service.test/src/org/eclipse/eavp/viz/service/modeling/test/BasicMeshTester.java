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
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IMesh;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
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
public class BasicMeshTester {

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
	 * Tests the functionality of the properties map.
	 */
	@Test
	public void testGetProperty() {
		// An empty property should return null
		assertNull(component.getProperty(MeshProperty.DESCRIPTION));

		// Set a property and test that its value is correct
		component.setProperty(MeshProperty.DESCRIPTION, "test value");
		assertTrue("test value"
				.equals(component.getProperty(MeshProperty.DESCRIPTION)));

		// Set a new value to the previous property and test that the value is
		// changed
		component.setProperty(MeshProperty.DESCRIPTION, "new value");
		assertTrue("new value"
				.equals(component.getProperty(MeshProperty.DESCRIPTION)));
	}

	/**
	 * Test the functionality of the entities map.
	 */
	@Test
	public void testEntities() {
		// Check that the map of entities is empty
		assertEquals(0, component.getEntities().size());

		// Create a new VizObject with id 2
		BasicController object = new BasicController(new BasicMesh(),
				new BasicView());
		object.setProperty(MeshProperty.ID, "2");

		// Add the object as a child
		component.addEntity(object);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the controller now has a map with one entity with id 2.
		assertEquals(1, component.getEntities().size());
		assertTrue("2".equals(
				component.getEntities().get(0).getProperty(MeshProperty.ID)));

		// Create a new part with id 3
		BasicController secondObject = new BasicController(
				new BasicMesh(), new BasicView());
		object.setProperty(MeshProperty.ID, "3");

		// Add a second entity
		component.addEntity(secondObject);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that there are two entities
		assertEquals(2,
				component.getEntitiesFromCategory(MeshCategory.DEFAULT).size());

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
		BasicController edge = new BasicController(new BasicMesh(),
				new BasicView());
		edge.setProperty(MeshProperty.NAME, "edge");
		component.addEntityToCategory(edge, MeshCategory.EDGES);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Create a vertex entity
		BasicController vertex1 = new BasicController(new BasicMesh(),
				new BasicView());
		vertex1.setProperty(MeshProperty.NAME, "vertex1");
		component.addEntityToCategory(vertex1, MeshCategory.VERTICES);

		// Create another vertex entity
		BasicController vertex2 = new BasicController(new BasicMesh(),
				new BasicView());
		vertex2.setProperty(MeshProperty.NAME, "vertex2");
		component.addEntityToCategory(vertex2, MeshCategory.VERTICES);

		// Check that there are three entities, 1 edge, and 2 vertices
		assertEquals(1,
				component.getEntitiesFromCategory(MeshCategory.EDGES).size());
		assertEquals(2,
				component.getEntitiesFromCategory(MeshCategory.VERTICES).size());

		// Check that the edge is in the Edges category
		assertTrue("edge"
				.equals(component.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(0).getProperty(MeshProperty.NAME)));

		// Create a list of all the names in the Vertices category
		ArrayList<String> vertexNames = new ArrayList<String>();
		for (IController object : component
				.getEntitiesFromCategory(MeshCategory.VERTICES)) {
			vertexNames.add(object.getProperty(MeshProperty.NAME));
		}

		// Check that the two vertices were in the right category
		assertTrue(vertexNames.contains("vertex1"));
		assertTrue(vertexNames.contains("vertex2"));

		// Check that empty categories return empty lists
		assertNotNull(component.getEntitiesFromCategory(MeshCategory.FACES));
		assertEquals(0,
				component.getEntitiesFromCategory(MeshCategory.FACES).size());
	}

	/**
	 * Tests the observer pattern registration.
	 */
	@Test
	public void testRegisteration() {

		// Create and register an object
		BasicController object = new BasicController(new BasicMesh(),
				new BasicView());
		object.register(component);

		// Set the object's id. This should trigger an update.
		object.setProperty(MeshProperty.ID, "2");

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
		object.setProperty(MeshProperty.ID, "3");

		// The component should not have received the update.
		assertFalse(component.isUpdated());
	}

	/**
	 * Check that AbstractController's equality testing is correct
	 */
	@Test
	public void testEquality() {

		// Create a mesh
		IMesh mesh = new TestMesh(new ArrayList<IController>());
		mesh.setProperty(MeshProperty.DESCRIPTION, "True");

		// Create a mesh equal to the first
		BasicMesh equalMesh = new TestMesh(new ArrayList<IController>());
		equalMesh.setProperty(MeshProperty.DESCRIPTION, "True");

		// Create a mesh which is not equal to the first
		IMesh inequalMesh = new TestMesh(new ArrayList<IController>());
		inequalMesh.setProperty(MeshProperty.DESCRIPTION, "False");

		// A mesh should equal itself
		assertTrue(mesh.equals(mesh));

		// A mesh should equal a controller with a equal properties and entities
		// maps
		assertTrue(mesh.equals(equalMesh));

		// A mesh should not equal a mesh with different properties
		assertFalse(mesh.equals(inequalMesh));

		// Set the properties to be equal
		inequalMesh.setProperty(MeshProperty.DESCRIPTION, "True");

		// The two objects should now be equal
		assertTrue(mesh.equals(inequalMesh));

		// Set the entities to be inequal
		BasicController child = new BasicController(equalMesh,
				new BasicView());
		inequalMesh.addEntityToCategory(child, MeshCategory.FACES);

		// The objects should be inequal again
		assertFalse(mesh.equals(inequalMesh));

		// Add the same object under a different category
		mesh.addEntityToCategory(child, MeshCategory.EDGES);
		assertFalse(mesh.equals(inequalMesh));

		// Remove the child and put it in the same category
		mesh.removeEntity(child);
		mesh.addEntityToCategory(child, MeshCategory.FACES);

		// The meshes should be equal again
		assertTrue(mesh.equals(inequalMesh));

		// Check that a cloned controller is equal to the original
		IMesh clone = (IMesh) ((BasicMesh) mesh).clone();
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
		component.setProperty(MeshProperty.DESCRIPTION, "Value");
		assertTrue(listener.gotProperty());
		assertFalse(listener.gotSelection());

		// Changing the Selected property should instead fire a SELECTION update
		component.setProperty(MeshProperty.SELECTED, "True");
		assertFalse(listener.gotProperty());
		assertTrue(listener.gotSelection());

		// Create a second object, add it to the mesh, and check that a CHILD
		// update was fired
		BasicController child = new BasicController(new BasicMesh(),
				new BasicView());
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
		component.copy(new BasicMesh());
		assertTrue(listener.gotChild());
		assertTrue(listener.gotProperty());
		assertTrue(listener.gotSelection());
		assertTrue(listener.gotWireframe());
		assertTrue(listener.gotTransformation());
	}
}
