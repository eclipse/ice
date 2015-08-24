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
package org.eclipse.ice.viz.service.mesh.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.viz.service.mesh.datastructures.Custom2DShape;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.junit.Test;

/**
 * <p>
 * Tests the Custom2DShape class.
 * </p>
 * 
 * @authors Jordan H. Deyton, Taylor Patterson
 */
public class Custom2DShapeTester {

	// TODO Currently, Custom2DShape is just a collection of Polygon instances
	// with no restrictions on the polygons. Thus, while the polygons may be
	// invalid for a MeshComponent, they are valid here. If we want to change
	// this, the following tests (particular checkAddPolygons) will need to be
	// updated to use unique vertex IDs, edge IDs, and polygon IDs.

	/**
	 * <p>
	 * This operation tests the construction of the Custom2DShape class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		// Instantiate Custom2DShape
		Custom2DShape shape = new Custom2DShape();

		// Make sure the polygons List was initialized but empty.
		assertNotNull(shape.getPolygons());
		assertTrue(shape.getPolygons().isEmpty());

		return;

	}

	/**
	 * <p>
	 * Tests the ability to add Polygons to the Custom2DShape.
	 * </p>
	 * 
	 */
	@Test
	public void checkAddPolygons() {

		// Useful variables.
		Edge edge;
		ArrayList<Vertex> vertices;
		ArrayList<Edge> edges;
		Polygon polygon;
		int size;

		// Create a MeshComponent to test.
		Custom2DShape shape = new Custom2DShape();

		// This list will keep track of the current polygons.
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();

		// Make sure the list of polygons is empty.
		assertEquals(polygons, shape.getPolygons());

		/* ---- Add a polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The first polygon will be a triangle with one point at the origin and
		// the other two on the x and z axes at a distance of 5.

		// Create the vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);

		// Add the polygon.
		shape.addPolygon(polygon);
		polygons.add(polygon);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* ------------------------ */

		/* ---- Add another polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The second polygon will be a triangle that forms a square with the
		// first polygon.

		// Create the vertices.
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 5f));
		vertices.add(new Vertex(0f, 0f, 5f));
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);

		// Add the polygon.
		shape.addPolygon(polygon);
		polygons.add(polygon);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* ------------------------------ */

		/* ---- Remove a polygon. ---- */
		// Remove the last polygon.
		shape.removePolygon(polygon);
		polygons.remove(1);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* --------------------------- */

		/* ---- Remove an invalid polygon. ---- */
		// Try to remove the last polygon again.
		shape.removePolygon(polygon);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* ------------------------------------ */

		/* ---- Set a list of polygons. ---- */
		// Restore the recently removed polygon.
		polygons.add(polygon);

		// Now set the polygon list.
		shape.setPolygons(polygons);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* --------------------------------- */

		/* ---- Add a null value. ---- */
		shape.addPolygon(null);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* --------------------------- */

		/* ---- Set list to a null value. ---- */
		shape.setPolygons(null);

		// Check the shape's polygons.
		assertEquals(polygons, shape.getPolygons());
		/* ----------------------------------- */

		/* ---- Check that we cannot modify the list directly. ---- */
		// Get the shape's polygons.
		ArrayList<Polygon> shapePolygons = shape.getPolygons();

		// Try to modify it.
		shapePolygons.clear();

		// Check the shape's polygons.
		assertEquals(2, polygons.size());
		assertEquals(polygons, shape.getPolygons());
		/* -------------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * This operation checks the Custom2DShape to insure that it can be
	 * correctly visited by a realization of the IShapeVisitor interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		// TODO This method will require Custom2DShape to be added to the
		// IShapesVisitor class or the IMeshVisitor class if one is created.

		return;
	}

	/**
	 * <p>
	 * This operation tests the Custom2DShape to ensure that it can properly
	 * dispatch notifications when it receives an update that changes its state.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {

		// Useful variables.
		Edge edge;
		ArrayList<Vertex> vertices;
		ArrayList<Edge> edges;
		Polygon polygon;
		int size;

		// Create a MeshComponent to test.
		Custom2DShape shape = new Custom2DShape();

		// Create a listener and register it with the shape.
		TestComponentListener listener = new TestComponentListener();
		shape.register(listener);

		// This list will keep track of the current polygons.
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();

		/* ---- Create a polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The first polygon will be a triangle with one point at the origin and
		// the other two on the x and z axes at a distance of 5.

		// Create the vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);

		// Add the polygon to our list for bookkeeping.
		polygons.add(polygon);
		/* ------------------------ */

		/* ---- Add a polygon. ---- */
		// Valid input. Notifies.
		shape.addPolygon(polygon);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Invalid input. Doesn't notify.
		shape.addPolygon(null);
		assertFalse(listener.wasNotified());
		listener.reset();
		/* ------------------------ */

		/* ---- Remove a polygon. ---- */
		// Valid input. Notifies.
		shape.removePolygon(polygon);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Invalid input. Doesn't notify.
		shape.removePolygon(null);
		assertFalse(listener.wasNotified());
		listener.reset();
		/* --------------------------- */

		/* ---- Set the polygon list. ---- */
		// Valid input. Notifies.
		shape.setPolygons(polygons);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Invalid input. Doesn't notify.
		shape.setPolygons(null);
		assertFalse(listener.wasNotified());
		listener.reset();
		/* ------------------------------- */

		return;
	}

	/**
	 * <p>
	 * This operation checks the ability of the Custom2DShape to persist itself
	 * to XML and to load itself from an XML input stream.
	 * </p>
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 * 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// Useful variables.
		Edge edge;
		ArrayList<Vertex> vertices;
		ArrayList<Edge> edges;
		Polygon polygon;
		int size;
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Custom2DShape.class);

		// Create a MeshComponent to test.
		Custom2DShape shape = new Custom2DShape();

		// This list will keep track of the current polygons.
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();

		// Make sure the list of polygons is empty.
		assertEquals(polygons, shape.getPolygons());

		/* ---- Add a polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The first polygon will be a triangle with one point at the origin and
		// the other two on the x and z axes at a distance of 5.

		// Create the vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);

		// Add the polygon.
		shape.addPolygon(polygon);
		polygons.add(polygon);
		/* ------------------------ */

		/* ---- Perform the XML test. ---- */
		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(shape, classList, outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		Custom2DShape loadedShape = new Custom2DShape();
		loadedShape = (Custom2DShape) xmlHandler.read(classList, inputStream);

		// Make sure the two components match.
		assertTrue(shape.equals(loadedShape));

		return;
	}

	/**
	 * <p>
	 * This operation checks the Custom2DShape to insure that its equals() and
	 * hashCode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Useful variables.
		Edge edge;
		ArrayList<Vertex> vertices;
		ArrayList<Edge> edges;
		Polygon polygon;
		int size;

		/* ---- Create a polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The first polygon will be a triangle with one point at the origin and
		// the other two on the x and z axes at a distance of 5.

		// Create the vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);
		/* ------------------------ */

		// Initialize objects for testing.
		Custom2DShape object = new Custom2DShape();
		Custom2DShape equalObject = new Custom2DShape();
		Custom2DShape unequalObject = new Custom2DShape();

		// Set up the object and equalObject.
		object.setName("YAMC");
		object.addPolygon(polygon);

		equalObject.setName("YAMC");
		equalObject.addPolygon((Polygon) polygon.clone());

		// Set up the unequalObject.
		unequalObject.setName("YAMC");
		/* ---- Create a polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The first polygon will be a triangle with one point at the origin and
		// the other two on the x and z axes at a distance of 5.

		// Create the vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 6f)); // Different!
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);
		/* ------------------------ */
		unequalObject.addPolygon(polygon);

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
	 * This operation checks the Custom2DShape to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Useful variables.
		Edge edge;
		ArrayList<Vertex> vertices;
		ArrayList<Edge> edges;
		Polygon polygon;
		int size;

		/* ---- Create a polygon. ---- */
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// The first polygon will be a triangle with one point at the origin and
		// the other two on the x and z axes at a distance of 5.

		// Create the vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));
		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Create the edges.
		size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);
		/* ------------------------ */

		// Initialize objects for testing.
		Custom2DShape object = new Custom2DShape();
		Custom2DShape copy = new Custom2DShape();
		Custom2DShape clone = null;

		// Set up the object.
		object.setName("YAMC");
		object.addPolygon(polygon);

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
		clone = (Custom2DShape) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}