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

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.Representation;
import org.eclipse.eavp.viz.service.modeling.Transformation;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestController;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestMesh;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestView;
import org.junit.Before;
import org.junit.Test;

import javafx.scene.Group;

/**
 * A test class for the AbstractController
 * 
 * @author Robert Smith
 *
 */
public class BasicControllerTester {

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
	 * Check that the AbstractController is in the correct state after
	 * construction.
	 */
	@Test
	public void testConstruction() {

		TestMesh mesh = new TestMesh(new ArrayList<IController>());
		TestView view = new TestView();
		TestController controller = new TestController(mesh, view);

		// Check that the controller contains the mesh and view
		assertTrue(mesh == controller.getModel());
		assertTrue(view == controller.getView());

		// Check that the model has a reference to the controller
		assertTrue(controller == mesh.getController());
	}

	/**
	 * Tests the state of the entities list as objects are added and removed, as
	 * well as whether objects in it are sending proper notifications.
	 */
	@Test
	public void testEntities() {
		// Check that the map of entities is empty
		assertEquals(0, controller.getEntities().size());

		// Create a new VizObject with id 2
		BasicController object = new BasicController(new BasicMesh(),
				new BasicView());
		object.setProperty(MeshProperty.ID, "2");

		// Add the object as a child
		controller.addEntity(object);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the controller now has a map with one entity with id 2.
		assertEquals(1, controller.getEntities().size());
		assertTrue("2".equals(
				controller.getEntities().get(0).getProperty(MeshProperty.ID)));

		// Create a new part with id 3
		BasicController secondObject = new BasicController(
				new BasicMesh(), new BasicView());
		secondObject.setProperty(MeshProperty.ID, "3");

		// Add a second entity
		controller.addEntity(secondObject);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that there are two entities
		assertEquals(2, controller.getEntities().size());

		// Remove one of the entities
		controller.removeEntity(object);

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		List<IController> temp = controller.getEntities();

		// Check that there is only one entity
		assertEquals(1, controller.getEntities().size());

		// Check that empty categories return empty lists
		assertNotNull(controller.getEntitiesFromCategory(MeshCategory.FACES));
		assertEquals(0,
				controller.getEntitiesFromCategory(MeshCategory.FACES).size());
	}

	/**
	 * Tests the disposal of the controller
	 */
	@Test
	public void testDispose() {

		// The controller should start off disposed
		assertFalse(controller.getDisposed().get());

		// Dispose the controller and check that it is set as disposed.
		controller.dispose();
		assertTrue(controller.getDisposed().get());

	}

	/**
	 * Tests the category functionality of the entities map
	 */
	@Test
	public void testEntityCategories() {

		// Create an edge entity
		BasicController edge = new BasicController(new BasicMesh(),
				new BasicView());
		edge.setProperty(MeshProperty.NAME, "edge");
		controller.addEntityToCategory(edge, MeshCategory.EDGES);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Create a vertex entity
		BasicController vertex1 = new BasicController(new BasicMesh(),
				new BasicView());
		vertex1.setProperty(MeshProperty.NAME, "vertex1");
		controller.addEntityToCategory(vertex1, MeshCategory.VERTICES);

		// Create another vertex entity
		BasicController vertex2 = new BasicController(new BasicMesh(),
				new BasicView());
		vertex2.setProperty(MeshProperty.NAME, "vertex2");
		controller.addEntityToCategory(vertex2, MeshCategory.VERTICES);

		// Check that there are three entities, 1 edge, and 2 vertices
		assertEquals(3, controller.getEntities().size());
		assertEquals(1,
				controller.getEntitiesFromCategory(MeshCategory.EDGES).size());
		assertEquals(2,
				controller.getEntitiesFromCategory(MeshCategory.VERTICES).size());

		// Check that the edge is in the Edges category
		assertTrue("edge"
				.equals(controller.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(0).getProperty(MeshProperty.NAME)));

		// Create a list of all the names in the Vertices category
		ArrayList<String> vertexNames = new ArrayList<String>();
		for (IController object : controller
				.getEntitiesFromCategory(MeshCategory.VERTICES)) {
			vertexNames.add(object.getProperty(MeshProperty.NAME));
		}

		// Check that the two vertices were in the right category
		assertTrue(vertexNames.contains("vertex1"));
		assertTrue(vertexNames.contains("vertex2"));
	}

	/**
	 * Tests the functionality of the properties map
	 */
	@Test
	public void testGetProperty() {
		// An empty property should return null
		assertNull(controller.getProperty(MeshProperty.DESCRIPTION));

		// Set a property and test that its value is correct
		controller.setProperty(MeshProperty.DESCRIPTION, "test value");
		assertTrue("test value"
				.equals(controller.getProperty(MeshProperty.DESCRIPTION)));

		// Set a new value to the previous property and test that the value is
		// changed
		controller.setProperty(MeshProperty.DESCRIPTION, "new value");
		assertTrue("new value"
				.equals(controller.getProperty(MeshProperty.DESCRIPTION)));
	}

	/**
	 * Tests that the controller properly delegates the getRepresentation()
	 * function to the view
	 */
	@Test
	public void testGetRepresentation() {

		// Get the representation from the TestView
		Representation<VizObject> representation = controller.getRepresentation();

		// The representation should be a TestObject
		assertTrue(representation.getData() instanceof VizObject);
	}

	/**
	 * Tests the controller's operations on the transformation's rotation
	 */
	@Test
	public void testGetRotation() {

		// Get the controller's initial rotation
		double[] rotation = controller.getRotation();

		// Check that the rotation is initially 0
		assertEquals(0, Double.compare(rotation[0], 0));
		assertEquals(0, Double.compare(rotation[1], 0));
		assertEquals(0, Double.compare(rotation[2], 0));

		// Set the rotation to a new value
		controller.setRotation(1, 2, 3);
		rotation = controller.getRotation();

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the rotation is set correctly
		assertEquals(0, Double.compare(rotation[0], 1));
		assertEquals(0, Double.compare(rotation[1], 2));
		assertEquals(0, Double.compare(rotation[2], 3));
	}

	/**
	 * Tests the controller's operations on the transformation's scale
	 */
	@Test
	public void testGetScale() {

		// Get the controller's initial scale
		double[] scale = controller.getScale();

		// Check that the scale is initially 0
		assertEquals(0, Double.compare(scale[0], 1.0));
		assertEquals(0, Double.compare(scale[1], 1.0));
		assertEquals(0, Double.compare(scale[2], 1.0));

		// Set the scale to a new value
		controller.setScale(1, 2, 3);
		scale = controller.getScale();

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the scale is set correctly
		assertEquals(0, Double.compare(scale[0], 1));
		assertEquals(0, Double.compare(scale[1], 2));
		assertEquals(0, Double.compare(scale[2], 3));
	}

	/**
	 * Tests the controller's operations on the transformation's size
	 */
	@Test
	public void testGetSize() {

		// Check that the size is initially 1
		assertEquals(0, Double.compare(controller.getSize(), 1));

		// Set the size and make sure the size is set correctly
		controller.setSize(2);
		assertEquals(0, Double.compare(controller.getSize(), 2));

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());
	}

	/**
	 * Tests the controller's operations on the transformation's skew
	 */
	@Test
	public void testGetSkew() {

		// Get the controller's initial skew
		double[] skew = controller.getSkew();

		// Check that the skew is initially 0
		assertEquals(0, Double.compare(skew[0], 0));
		assertEquals(0, Double.compare(skew[1], 0));
		assertEquals(0, Double.compare(skew[2], 0));

		// Set the skew to a new value
		controller.setSkew(1, 2, 3);
		skew = controller.getSkew();

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the skew is set correctly
		assertEquals(0, Double.compare(skew[0], 1));
		assertEquals(0, Double.compare(skew[1], 2));
		assertEquals(0, Double.compare(skew[2], 3));
	}

	/**
	 * Tests the controller's operations on the transformation's translation
	 */
	@Test
	public void testGetTranslation() {

		// Get the controller's initial translation
		double[] translation = controller.getTranslation();

		// Check that the translation is initially 0
		assertEquals(0, Double.compare(translation[0], 0));
		assertEquals(0, Double.compare(translation[1], 0));
		assertEquals(0, Double.compare(translation[2], 0));

		// Set the skew to a new value
		controller.setTranslation(1, 2, 3);
		translation = controller.getTranslation();

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the skew is set correctly
		assertEquals(0, Double.compare(translation[0], 1));
		assertEquals(0, Double.compare(translation[1], 2));
		assertEquals(0, Double.compare(translation[2], 3));
	}

	/**
	 * Check that AbstractController's equality testing is correct
	 */
	@Test
	public void testEquality() {

		// Create a controller
		BasicMesh mesh = new TestMesh(new ArrayList<IController>());
		mesh.setProperty(MeshProperty.DESCRIPTION, "True");
		IController object = new TestController(mesh, new TestView());

		// Create a controller equal to the first
		BasicMesh equalMesh = new TestMesh(new ArrayList<IController>());
		equalMesh.setProperty(MeshProperty.DESCRIPTION, "True");
		IController equalObject = new TestController(equalMesh, new TestView());

		// Create a controller which is not equal to the first
		BasicMesh inequalMesh = new TestMesh(new ArrayList<IController>());
		inequalMesh.setProperty(MeshProperty.DESCRIPTION, "False");
		IController inequalObject = new TestController(inequalMesh,
				new TestView());

		// A controller should equal itself
		assertTrue(object.equals(object));

		// A controller should equal a controller with an equal mesh and view
		assertTrue(object.equals(equalObject));

		// A controller should not equal a controller with an inequal mesh
		assertFalse(object.equals(inequalObject));

		// Set the meshes to be equal
		inequalObject.setProperty(MeshProperty.DESCRIPTION, "True");

		// The two objects should now be equal
		assertTrue(object.equals(inequalObject));

		// Set the views to be inequal
		((TestView) inequalObject.getView()).setData(1);

		// The objects should be inequal again
		assertFalse(object.equals(inequalObject));

		// Check that a cloned controller is equal to the original
		IController clone = (IController) ((BasicController) object).clone();
		assertTrue(object.equals(clone));
	}

	/**
	 * Check that updates are handled correctly
	 */
	@Test
	public void checkUpdates() {

		// Create a controller
		TestMesh mesh = new TestMesh(new ArrayList<IController>());
		TestController object = new TestController(mesh, new TestView());

		// Create a parent object to receive updates
		TestMesh parentMesh = new TestMesh(new ArrayList<IController>());
		TestController parent = new TestController(parentMesh, new TestView());
		parent.addEntity(object);

		// Create an update and check that the parent was updated
		mesh.setProperty(MeshProperty.DESCRIPTION, "Value");
		assertTrue(parent.isUpdated());

		// Check that the view was refreshed
		assertTrue(((TestView) object.getView()).isRefreshed());
	}
}
