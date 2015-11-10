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
package org.eclipse.ice.viz.service.modeling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.VizObject;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Transformation;
import org.junit.Before;
import org.junit.Test;

/**
 * A test class for the AbstractController
 * 
 * @author Robert Smith
 *
 */
public class AbstractControllerTester {

	/**
	 * The entities for the component
	 */
	List<AbstractController> entities;

	/**
	 * The controller's model
	 */
	TestMeshComponent component;

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
		component = new TestMeshComponent(entities);
		view = new TestView();
		controller = new TestController(component, view);
	}

	/**
	 * Tests the state of the entities list as objects are added and removed, as
	 * well as whether objects in it are sending propert notifications.
	 */
	@Test
	public void testEntities() {
		// Check that the map of entities is empty
		assertEquals(0, controller.getEntities().size());

		// Create a new VizObject with id 2
		AbstractController object = new AbstractController(
				new AbstractMeshComponent(), new AbstractView());
		object.setProperty("Id", "2");

		// Add the object as a child
		controller.addEntity(object);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Check that the controller now has a map with one entity with id 2.
		assertEquals(1, controller.getEntities().size());
		assertTrue(
				"2".equals(controller.getEntities().get(0).getProperty("Id")));

		// Add a second entity
		controller.addEntity((AbstractController) object.clone());

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

		List<AbstractController> temp = controller.getEntities();

		// Check that there is only one entity
		assertEquals(1, controller.getEntities().size());

		// Check that empty categories return empty lists
		assertNotNull(controller.getEntitiesByCategory("empty category"));
		assertEquals(0,
				controller.getEntitiesByCategory("empty category").size());
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
		AbstractController edge = new AbstractController(
				new AbstractMeshComponent(), new AbstractView());
		edge.setProperty("Name", "edge");
		controller.addEntityByCategory(edge, "Edges");

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Create a vertex entity
		AbstractController vertex1 = new AbstractController(
				new AbstractMeshComponent(), new AbstractView());
		vertex1.setProperty("Name", "vertex1");
		controller.addEntityByCategory(vertex1, "Vertices");

		// Create another vertex entity
		AbstractController vertex2 = new AbstractController(
				new AbstractMeshComponent(), new AbstractView());
		vertex2.setProperty("Name", "vertex2");
		controller.addEntityByCategory(vertex2, "Vertices");

		// Check that there are three entities, 1 edge, and 2 vertices
		assertEquals(3, controller.getEntities().size());
		assertEquals(1, controller.getEntitiesByCategory("Edges").size());
		assertEquals(2, controller.getEntitiesByCategory("Vertices").size());

		// Check that the edge is in the Edges category
		assertTrue("edge".equals(controller.getEntitiesByCategory("Edges")
				.get(0).getProperty("Name")));

		// Create a list of all the names in the Vertices category
		ArrayList<String> vertexNames = new ArrayList<String>();
		for (AbstractController object : controller
				.getEntitiesByCategory("Vertices")) {
			vertexNames.add(object.getProperty("Name"));
		}

		// Check that the two vertices were in the right category
		assertTrue(vertexNames.contains("vertex1"));
		assertTrue(vertexNames.contains("vertex2"));
	}

	/**
	 * Tests that the controlelr properly maintains the previous transformation
	 * through various operations.
	 */
	@Test
	public void testPreviousTransformation() {
		// Get the previous transformation
		Transformation prev = controller.getPreviousTransformation();

		// Initially, the previous and current transformations should be
		// identical
		assertTrue(controller.getTransformation().equals(prev));

		// Change the transformation
		controller.getTransformation().setSize(5);

		// Wait for the notification thread
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Makes sure the previous transformation is different from the current
		// one
		assertFalse(controller.getTransformation()
				.equals(controller.getPreviousTransformation()));

		// Change the transformation a second time
		controller.getTransformation().setSize(10);

		// Make sure the previous transformation is different from the current
		// one
		assertFalse(controller.getTransformation()
				.equals(controller.getPreviousTransformation()));

		// Make sure that the previous transformation hasn't changed
		assertTrue(prev.equals(controller.getPreviousTransformation()));

		// Create a new transformation and set it as the controller's
		// transformation
		Transformation transform = new Transformation();
		transform.setSize(20);
		controller.setTransformation(transform);

		// Check that the controller was notified
		assertTrue(controller.isUpdated());

		// Make sure that the previous transformation hasn't changed
		assertFalse(controller.getTransformation()
				.equals(controller.getPreviousTransformation()));

		// Make sure that the previous transformation hasn't changed
		assertTrue(prev.equals(controller.getPreviousTransformation()));

		// Synchronize the controller
		controller.synched();

		// Make sure the previous transformation has been updated.
		assertTrue(controller.getTransformation()
				.equals(controller.getPreviousTransformation()));

		// Assert the previous transformation has the correct size
		assertEquals(0, Double.compare(20,
				controller.getPreviousTransformation().getSize()));
	}

	/**
	 * Tests the functionality of the properties map
	 */
	@Test
	public void testGetProperty() {
		// An empty property should return null
		assertNull(controller.getProperty("empty"));

		// Set a property and test that its value is correct
		controller.setProperty("test property", "test value");
		assertTrue(
				"test value".equals(controller.getProperty("test property")));

		// Set a new value to the previous property and test that the value is
		// changed
		controller.setProperty("test property", "new value");
		assertTrue("new value".equals(controller.getProperty("test property")));
	}

	/**
	 * Tests that the controller properly delegates the getRepresentation()
	 * function to the view
	 */
	@Test
	public void testGetRepresentation() {

		// Get the representation from the TestView
		Object representation = controller.getRepresentation();

		// The representation should be a TestObject
		assertTrue(representation instanceof TestObject);
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
}

class TestController extends AbstractController {

	boolean updated = false;

	public TestController(AbstractMeshComponent model, AbstractView view) {
		super(model, view);
	}

	@Override
	public void update(IVizUpdateable notification) {
		updated = true;
	}

	public boolean isUpdated() {
		boolean temp = updated;
		updated = false;
		return temp;
	}

}

class TestMeshComponent extends AbstractMeshComponent {

	boolean updated = false;

	public TestMeshComponent(List<AbstractController> entities) {
		super(entities);
	}

	@Override
	public void update(IVizUpdateable notification) {
		updated = true;
		super.update(notification);
	}

	public boolean isUpdated() {
		boolean temp = updated;
		updated = false;
		return temp;
	}
}

class TestView extends AbstractView {

	boolean updated = false;

	public TestView() {
		super();
	}

	@Override
	public Object getRepresentation() {
		return new TestObject();
	}

	@Override
	public void update(IVizUpdateable notification) {
		updated = true;
		super.update(notification);
	}

	public boolean isUpdated() {
		boolean temp = updated;
		updated = false;
		return temp;
	}
}

class TestObject extends VizObject {
	public TestObject() {
		super();
	}
}