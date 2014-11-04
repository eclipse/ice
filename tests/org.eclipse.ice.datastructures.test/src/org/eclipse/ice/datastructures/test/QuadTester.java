/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.Quad;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the Quad class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class QuadTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the construction of the Quad class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCreation() {
		// begin-user-code

		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		Quad quad;

		/* ---- Test the nullary constructor. ---- */
		// Initialize the quad.
		quad = new Quad();

		// Check the edges.
		assertNotNull(quad.getEdges());
		assertTrue(quad.getEdges().isEmpty());

		// Check the vertices.
		assertNotNull(quad.getVertices());
		assertTrue(quad.getVertices().isEmpty());
		/* --------------------------------------- */

		/* ---- Test valid input. ---- */
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(1f, 0f, 3f));
		vertices.add(new Vertex(2f, 0f, 3f));
		vertices.add(new Vertex(3f, 0f, 0f));
		for (int i = 0; i < 4; i++) {
			vertices.get(i).setId(i + 1);
		}
		for (int i = 0; i < 4; i++) {
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % 4);
			edges.add(new Edge(start, end));
			edges.get(i).setId(i + 1);
		}

		// Initialize the quad.
		quad = new Quad(edges, vertices);

		// Check the edges.
		assertEquals(edges, quad.getEdges());

		// Check the vertices.
		assertEquals(vertices, quad.getVertices());
		/* --------------------------- */

		/* ---- Test invalid input. ---- */
		// Try having too many edges/vertices.
		vertices.add(new Vertex(2f, 0f, -1f));
		vertices.get(4).setId(5);

		edges.remove(3);
		edges.add(new Edge(vertices.get(3), vertices.get(4)));
		edges.get(3).setId(4);
		edges.add(new Edge(vertices.get(4), vertices.get(0)));
		edges.get(4).setId(5);

		// Make sure the constructor throws the proper exception.
		try {
			quad = new Quad(edges, vertices);

			// Check the edges.
			assertNotNull(quad.getEdges());
			assertTrue(quad.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(quad.getVertices());
			assertTrue(quad.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Try having too few edges/vertices.
		vertices.remove(4);
		vertices.remove(3);
		edges.remove(4);
		edges.remove(3);
		edges.remove(2);
		edges.add(new Edge(vertices.get(2), vertices.get(0)));
		edges.get(2).setId(3);

		// Make sure the constructor throws the proper exception.
		try {
			quad = new Quad(edges, vertices);

			// Check the edges.
			assertNotNull(quad.getEdges());
			assertTrue(quad.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(quad.getVertices());
			assertTrue(quad.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}

		// Restore the original quad.
		vertices.add(new Vertex(3f, 0f, 0f));
		vertices.get(3).setId(4);
		edges.remove(2);
		edges.add(new Edge(vertices.get(2), vertices.get(3)));
		edges.get(2).setId(3);
		edges.add(new Edge(vertices.get(3), vertices.get(0)));
		edges.get(3).setId(4);

		// Initialize the quad.
		quad = new Quad(edges, vertices);

		// Check the edges.
		assertEquals(edges, quad.getEdges());

		// Check the vertices.
		assertEquals(vertices, quad.getVertices());

		// Try an invalid vertex.
		vertices.set(3, new Vertex());

		// Make sure the constructor throws the proper exception.
		try {
			quad = new Quad(edges, vertices);

			// Check the edges.
			assertNotNull(quad.getEdges());
			assertTrue(quad.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(quad.getVertices());
			assertTrue(quad.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ----------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the copying and cloning methods of Quad. Note that
	 * since the Quad.copy(Quad) method just refers to the Polygon copy method,
	 * it doesn't need to be tested here.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {

		// ---- Check Copying Polygons into Quads. ---- //

		// Create a default Quad to use for testing
		Quad quad = new Quad();

		// Check that it's edges and vertices are empty
		assertTrue(quad.getEdges().isEmpty());
		assertTrue(quad.getVertices().isEmpty());

		// Create a 3-sided polygon (invalid for copying to a Quad)
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(1f, 0f, 3f));
		vertices.add(new Vertex(2f, 0f, 3f));
		for (int i = 0; i < 3; i++) {
			vertices.get(i).setId(i + 1);
		}
		for (int i = 0; i < 3; i++) {
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % 3);
			edges.add(new Edge(start, end));
			edges.get(i).setId(i + 1);
		}

		// Initialize the Polygon
		Polygon polygon = new Polygon(edges, vertices);

		// Try to copy the polygon into the quad (shouldn't work)
		quad.copy(polygon);

		// Check the quad remains unchanged (empty edges and vertices)
		assertTrue(quad.getEdges().isEmpty());
		assertTrue(quad.getVertices().isEmpty());

		// Create a 4-sided Polygon now (valid for copying to a Quad)
		edges.clear();
		vertices.clear();

		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(1f, 0f, 3f));
		vertices.add(new Vertex(2f, 0f, 3f));
		vertices.add(new Vertex(2f, 3f, 7f));
		for (int i = 0; i < 4; i++) {
			vertices.get(i).setId(i + 1);
		}
		for (int i = 0; i < 4; i++) {
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % 4);
			edges.add(new Edge(start, end));
			edges.get(i).setId(i + 1);
		}

		// Initialize the new Polygon
		polygon = new Polygon(edges, vertices);

		// Copy the Polygon into the Quad
		quad.copy(polygon);

		// Check that they're equal
		assertTrue(quad.equals(polygon));

		// ---- Check cloning. ---- //

		// Make a new quad for cloning
		Quad cloneQuad = new Quad();

		// Check that it doesn't equal clone yet
		assertFalse(cloneQuad.equals(quad));

		// Clone the quad now
		cloneQuad = (Quad) quad.clone();

		// Check that they're the same
		assertTrue(cloneQuad.equals(quad));

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks Quad to ensure that it can be correctly visited by
	 * a realization of the IMeshPartVisitor interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVisitation() {
		// begin-user-code

		IMeshPart part = new Quad();

		// ---- Check visiting with an IMeshPartVisitor. ---- //
		// Create a new TestMeshVisitor that only does anything useful when
		// visiting a Quad.
		TestMeshVisitor meshVisitor = new TestMeshVisitor() {
			@Override
			public void visit(Quad quad) {
				visited = true;
			}
		};
		assertFalse(meshVisitor.wasVisited());

		// Now try to visit the MeshComponent with the TestMeshVisitor.
		part.acceptMeshVisitor(meshVisitor);
		assertTrue(meshVisitor.wasVisited());
		// -------------------------------------------------- //

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the Quad to persist itself to XML
	 * and to load itself from an XML input stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoadingFromXML() {
		// begin-user-code

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// Construct the quad's edges and vertices.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(1f, 0f, 3f));
		vertices.add(new Vertex(2f, 0f, 3f));
		vertices.add(new Vertex(3f, 0f, 0f));
		for (int i = 0; i < 4; i++) {
			vertices.get(i).setId(i + 1);
		}
		for (int i = 0; i < 4; i++) {
			Vertex start = vertices.get(i);
			Vertex end = vertices.get((i + 1) % 4);
			edges.add(new Edge(start, end));
			edges.get(i).setId(i + 1);
		}

		// Create a Polygon to test.
		Quad quad = new Quad(edges, vertices);

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		quad.persistToXML(outputStream);
		assertNotNull(outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		Quad loadedQuad = new Quad();
		loadedQuad.loadFromXML(inputStream);

		// Make sure the two components match.
		assertTrue(quad.equals(loadedQuad));

		// Check invalid parameters.

		// Try passing null and make sure the components match.
		inputStream = null;
		loadedQuad.loadFromXML(inputStream);
		assertTrue(quad.equals(loadedQuad));

		// Try passing a bad input stream and make sure the components match.
		inputStream = new ByteArrayInputStream("jkl;2invalidstream".getBytes());
		loadedQuad.loadFromXML(inputStream);
		assertTrue(quad.equals(loadedQuad));

		return;
		// end-user-code
	}
}