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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshPart;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.junit.Test;

/**
 * <p>
 * Tests the Edge class.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class EdgeTester {

	/**
	 * <p>
	 * This operation tests the construction of the Edge class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		// The expected behavior is that if the supplied ArrayList of Vertices
		// is invalid, then the edge's default vertices become an empty list and
		// its length becomes -1.

		// The vertices are valid if:
		// they are non-null Vertex instances
		// they are not the same reference
		// there are exactly 2 Vertex instances

		ArrayList<Vertex> vertices;
		int[] vertexIds = { 1, 1 };
		Vertex vertex;

		Edge edge;

		/* ---- Try the nullary constructor. ---- */
		// Initialize the edge.
		edge = new Edge();

		// Verify the edge's vertices.
		assertEquals(0f, edge.getLength(), 1e-7f);
		System.out.println(Arrays.toString(edge.getVertexIds()));
		assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));
		/* -------------------------------------- */

		/* ---- Try valid vertices. ---- */
		// Set up the input vertices.
		vertices = new ArrayList<Vertex>();
		vertex = new Vertex(1f, 2f, 4f);
		vertex.setId(1);
		vertices.add(vertex);
		vertex = new Vertex(4f, 5f, 6f);
		vertex.setId(2);
		vertices.add(vertex);
		vertexIds = new int[] { 1, 2 };

		// Initialize the edge.
		edge = new Edge(vertices);

		// Verify the edge's vertices.
		assertFalse(Math.abs(-1f - edge.getLength()) < 1e-7f);
		assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

		// Try the other constructor.
		edge = new Edge(vertices.get(0), vertices.get(1));

		// Verify the edge's vertices.
		assertFalse(Math.abs(-1f - edge.getLength()) < 1e-7f);
		assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));
		/* ----------------------------- */

		/* ---- Try invalid vertex lists. ---- */
		vertexIds = new int[] { -1, -1 };

		// Pass in a null list.
		try {
			edge = new Edge(null);
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		// Try the other constructor.
		try {
			edge = new Edge(null, null);
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Pass in an empty list.
		try {
			edge = new Edge(new ArrayList<Vertex>());
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Pass in a list with three vertices.
		vertices.add(new Vertex(7f, 8f, 9f));
		try {
			edge = new Edge(vertices);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Pass in a list with one vertex.
		vertices.remove(2);
		vertices.remove(1);
		try {
			edge = new Edge(vertices);
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		// Try passing only one valid vertex to the other constructor.
		try {
			edge = new Edge(vertices.get(0), null);
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		try {
			edge = new Edge(null, vertices.get(0));
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Pass in a list with one vertex and one null.
		vertices.add(null);
		try {
			edge = new Edge(vertices);
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Pass in a list with the same reference twice.
		vertices.set(1, vertices.get(0));
		try {
			edge = new Edge(vertices);
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		// Try the other constructor.
		try {
			edge = new Edge(vertices.get(0), vertices.get(0));
			assertEquals(-1f, edge.getLength(), 1e-7f);
			assertTrue(Arrays.equals(vertexIds, edge.getVertexIds()));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ----------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Checks that the length of the Edge is calculated properly from the Edge's
	 * vertices.
	 * </p>
	 * 
	 */
	@Test
	public void checkLength() {

		// If the edge was correctly initialized, its length should be valid.
		// If the edge was incorrectly initialized, its length should be -1.

		ArrayList<Vertex> vertices = null;
		float[] v1, v2;
		float distance, d1, d2, d3;

		Edge edge;

		/* ---- Try an invalid edge. ---- */
		try {
			// Initialize the edge.
			edge = new Edge(vertices);

			// Check the length.
			assertEquals(0f, edge.getLength(), 1e-7f);

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ------------------------------ */

		/* ---- Try a valid edge. ---- */
		// Set up the vertices.
		vertices = new ArrayList<Vertex>();
		vertices.add(new Vertex(5f, 12.34f, 69.999389f));
		vertices.add(new Vertex(57.3f, 0.4441f, 847.1f));
		vertices.get(0).setId(1);
		vertices.get(1).setId(2);

		// Compute the distance between the two vertices.
		v1 = vertices.get(0).getLocation();
		v2 = vertices.get(1).getLocation();
		d1 = v2[0] - v1[0];
		d2 = v2[1] - v1[1];
		d3 = v2[2] - v1[2];
		distance = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);

		// Initialize the edge.
		edge = new Edge(vertices);

		// Check the length.
		assertEquals(distance, edge.getLength(), 1e-7f);
		/* --------------------------- */

		// Before updating the vertices, add listeners to the edges that will be
		// notified of the vertex changes.
		TestComponentListener listener = new TestComponentListener();
		edge.register(listener);

		/* ---- Try updating the vertices. ---- */
		// Set a vertex's location.
		v2[0] = 42f;
		vertices.get(0).setLocation(v2);

		// Compute the distance between the two vertices.
		v1 = vertices.get(0).getLocation();
		v2 = vertices.get(1).getLocation();
		d1 = v2[0] - v1[0];
		d2 = v2[1] - v1[1];
		d3 = v2[2] - v1[2];
		distance = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);

		// Check the length. Make sure the edge was notified before
		// checking its length.
		assertTrue(listener.wasNotified());
		listener.reset();
		assertEquals(distance, edge.getLength(), 1e-7f);

		// Try zero-ing out the length. Make sure the edge was notified before
		// checking its length.
		vertices.get(0).setLocation(vertices.get(1).getLocation());
		assertTrue(listener.wasNotified());
		listener.reset();
		assertEquals(0, edge.getLength(), 1e-7f);
		/* ------------------------------------ */

		return;
	}

	/**
	 * <p>
	 * This operation checks Edge to ensure that it can be correctly visited by
	 * a realization of the IMeshPartVisitor interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		IMeshPart part = new Edge();

		// ---- Check visiting with an IMeshPartVisitor. ---- //
		// Create a new TestMeshVisitor that only does anything useful when
		// visiting a Edge.
		TestMeshVisitor meshVisitor = new TestMeshVisitor() {
			@Override
			public void visit(Edge edge) {
				visited = true;
			}
		};
		assertFalse(meshVisitor.wasVisited());

		// Now try to visit the MeshComponent with the TestMeshVisitor.
		part.acceptMeshVisitor(meshVisitor);
		assertTrue(meshVisitor.wasVisited());
		// -------------------------------------------------- //

		return;
	}

	/**
	 * <p>
	 * Checks the ability of the Edge to update when a Vertex has been updated.
	 * </p>
	 * 
	 */
	@Test
	public void checkUpdate() {

		// TODO - If there is functionality added to the Edge that changes
		// some property of the edge when one of its vertices is updated, this
		// test method will need to be updated.

		// Make sure the edge registers with its vertices properly.
		Vertex start = new Vertex(0f, 1f, 2f);
		start.setId(1);
		Vertex end = new Vertex(1f, 2f, 3f);
		end.setId(2);
		float[] location = { 0f, 0f, 0f };

		// Test one constructor.
		TestEdge edge = new TestEdge(start, end);

		// Update the start vertex.
		start.setLocation(location);
		// Verify that the edge was updated.
		assertTrue(edge.wasUpdated());

		// Check the location reported by the edge.
		assertTrue(Arrays.equals(location, edge.getStartLocation()));

		// Update the end vertex.
		end.setLocation(location);
		// Verify that the edge was updated.
		assertTrue(edge.wasUpdated());

		// Check the location reported by the edge.
		assertTrue(Arrays.equals(location, edge.getEndLocation()));

		// Test the other constructor.
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(start);
		vertices.add(end);

		// Because our new TestEdge will be equivalent to our old one, we need
		// to unregister from the vertex.
		start.unregister(edge);
		end.unregister(edge);
		edge = new TestEdge(vertices);

		// Update the start vertex.
		location[0] = 4217938f;
		start.setLocation(location);
		assertTrue(edge.wasUpdated());

		// Check the location reported by the edge.
		assertTrue(Arrays.equals(location, edge.getStartLocation()));

		// Update the end vertex.
		location[0] = 86.19f;
		location[1] = 489.31893f;
		end.setLocation(location);
		// Verify that the edge was updated.
		assertTrue(edge.wasUpdated());

		// Check the location reported by the edge.
		assertTrue(Arrays.equals(location, edge.getEndLocation()));

		// It should update if we assign a new vertex instance to the same ID.
		start = new Vertex(86.19f, 489.31893f, 0f);
		start.setId(1);
		edge.update(start);
		// Verify that the edge was updated.
		assertTrue(edge.wasUpdated());
		// Check the length for good measure.
		assertEquals(0f, edge.getLength(), 1e-7f);

		// Check the location reported by the edge.
		location = new float[] { 86.19f, 489.31893f, 0f };
		assertTrue(Arrays.equals(location, edge.getStartLocation()));

		return;
	}

	/**
	 * <p>
	 * This operation checks the ability of the Edge to persist itself to XML
	 * and to load itself from an XML input stream.
	 * </p>
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 * 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// We need vertices to supply to created edges.
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Edge.class);

		// For our test, we'll just make an edge along the x axis from the
		// origin of length 5.
		float[] location = { 0f, 0f, 0f };
		vertices.add(new Vertex(location));
		location[0] = 5f;
		vertices.add(new Vertex(location));

		// Set the vertices' IDs.
		vertices.get(0).setId(1);
		vertices.get(1).setId(2);

		// Create an Edge to test.
		Edge edge = new Edge(vertices);

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(edge, classList, outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		Edge loadedEdge = new Edge();
		loadedEdge = (Edge) xmlHandler.read(classList, inputStream);

		// Make sure the two components match.
		assertTrue(edge.equals(loadedEdge));

		return;
	}

	/**
	 * <p>
	 * This operation checks the Edge to insure that its equals() and hashCode()
	 * operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// We need vertices to supply to created edges.
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make an edge along the x axis from the
		// origin of length 5.
		float[] location = { 0f, 0f, 0f };
		vertices.add(new Vertex(location));
		location[0] = 5f;
		vertices.add(new Vertex(location));

		// Set the vertices' IDs.
		vertices.get(0).setId(1);
		vertices.get(1).setId(2);

		// Initialize objects for testing.
		Edge object = new Edge(vertices);
		Edge equalObject = new Edge(vertices.get(1), vertices.get(0));

		// Change the second Vertex.
		location[0] = 6f;
		vertices.set(1, new Vertex(location));
		vertices.get(1).setId(2);

		// Initialize the unequalObject.
		Edge unequalObject = new Edge(vertices);

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object==null);
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
	 * This operation checks the Edge to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// We need vertices to supply to created edges.
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make an edge along the x axis from the
		// origin of length 5.
		float[] location = { 0f, 0f, 0f };
		vertices.add(new Vertex(location));
		location[0] = 5f;
		vertices.add(new Vertex(location));

		// Set the vertices' IDs.
		vertices.get(0).setId(1);
		vertices.get(1).setId(2);

		// Initialize objects for testing.
		Edge object = new Edge(vertices);
		Edge copy = new Edge();
		Edge clone = null;

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
		clone = (Edge) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}