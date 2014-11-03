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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.Vertex;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Performs integration tests of the Mesh data structures.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MeshTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation performs an integration test on the Mesh data structures.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkMesh() {
		// begin-user-code

		/*-
		 *  This test does the following:
		 * 1 Create a square of area 1 with its top-left corner at the origin.
		 * 2 Create a square of area 1 with its top-left corner at the 1st 
		 *   square's bottom-right corner.
		 * 3 Remove the 2nd square.
		 * 4 Add a triangle (half of square of area 1) that shares the right
		 *   edge of the 1st square. It also shares the 2 right vertices.
		 * 5 Move the top-left corner of the 1st square up by 1 unit (z-1).
		 * 6 Move the top-right corner of the 1st square right by 1 unit (x+1).
		 *   This also changes the triangle!
		 */

		MeshComponent mesh = new MeshComponent();
		Polygon polygon;
		ArrayList<Vertex> vertices;
		Vertex vertex;
		ArrayList<Edge> edges;
		int counter = 1;

		ArrayList<Vertex> allVertices = new ArrayList<Vertex>();
		ArrayList<Edge> allEdges = new ArrayList<Edge>();
		ArrayList<Polygon> allPolygons = new ArrayList<Polygon>();

		/* ---- Add a new polygon. ---- */
		// Create the vertices.
		vertices = new ArrayList<Vertex>();
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(1f, 0f, 0f));
		vertices.add(new Vertex(1f, 0f, 1f));
		vertices.add(new Vertex(0f, 0f, 1f));
		for (Vertex v : vertices) {
			v.setId(counter++);
		}
		// Create the edges.
		edges = new ArrayList<Edge>();
		for (int i = 0; i < 4; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 4)));
			edges.get(i).setId(counter++);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);
		polygon.setId(counter++);

		// Update the current lists of all edges, vertices, and polygons.
		allVertices.addAll(vertices);
		allEdges.addAll(edges);
		allPolygons.add(polygon);

		// Add the polygon to the component.
		mesh.addPolygon(polygon);

		// Check the mesh's contents.
		assertEquals(allVertices, mesh.getVertices());
		assertEquals(allEdges, mesh.getEdges());
		assertEquals(allPolygons, mesh.getPolygons());
		/* ---------------------------- */

		/* ---- Add another polygon. ---- */
		// Create the vertices.
		vertices = new ArrayList<Vertex>();
		vertices.add(new Vertex(1f, 0f, 1f));
		vertices.add(new Vertex(2f, 0f, 1f));
		vertices.add(new Vertex(2f, 0f, 2f));
		vertices.add(new Vertex(1f, 0f, 2f));
		vertices.get(0).setId(3);
		for (int i = 1; i < 4; i++) {
			vertices.get(i).setId(counter++);
		}
		// Create the edges.
		edges = new ArrayList<Edge>();
		for (int i = 0; i < 4; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 4)));
			edges.get(i).setId(counter++);
		}
		// Create the polygon.
		polygon = new Polygon(edges, vertices);
		polygon.setId(counter++);

		// Update the current lists of all edges, vertices, and polygons.
		for (int i = 1; i < 4; i++) {
			allVertices.add(vertices.get(i));
		}
		allEdges.addAll(edges);
		allPolygons.add(polygon);

		// Add the polygon to the component.
		mesh.addPolygon(polygon);

		// Check the mesh's contents.
		assertEquals(allVertices, mesh.getVertices());
		assertEquals(allEdges, mesh.getEdges());
		assertEquals(allPolygons, mesh.getPolygons());
		/* ------------------------------ */

		/* ---- Remove a polygon. ---- */
		// Update the current lists of all edges, vertices, and polygons.
		for (int i = 6; i >= 4; i--) {
			allVertices.remove(i);
		}
		for (int i = 7; i >= 4; i--) {
			allEdges.remove(i);
		}
		allPolygons.remove(1);

		// Remove the polygon.
		mesh.removePolygon(polygon.getId());

		// Check the mesh's contents.
		assertEquals(allVertices, mesh.getVertices());
		assertEquals(allEdges, mesh.getEdges());
		assertEquals(allPolygons, mesh.getPolygons());
		/* --------------------------- */

		/* ---- Add another polygon. ---- */
		// Create the vertices.
		vertices = new ArrayList<Vertex>();
		vertices.add(new Vertex(1f, 0f, 0f));
		vertices.add(new Vertex(2f, 0f, 1f));
		vertices.add(new Vertex(1f, 0f, 1f));
		vertices.get(0).setId(2);
		vertices.get(1).setId(counter++);
		vertices.get(2).setId(3);
		// Create the edges.
		edges = new ArrayList<Edge>();
		for (int i = 0; i < 3; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 3)));
			edges.get(i).setId(counter++);
		}
		edges.get(2).setId(6);

		// Create the polygon.
		polygon = new Polygon(edges, vertices);
		polygon.setId(counter++);

		// Update the current lists of all edges, vertices, and polygons.
		allVertices.add(vertices.get(1));
		allEdges.add(edges.get(0));
		allEdges.add(edges.get(1));
		allPolygons.add(polygon);

		// Add the polygon to the component.
		mesh.addPolygon(polygon);

		// Check the mesh's contents.
		assertEquals(allVertices, mesh.getVertices());
		assertEquals(allEdges, mesh.getEdges());
		assertEquals(allPolygons, mesh.getPolygons());
		/* ------------------------------ */

		vertices = mesh.getVertices();
		edges = mesh.getEdges();

		// Reset all of the updated flags. Normally this is not necessary with
		// TestEdges, but we want to make absolutely sure the flag is reset.
		for (Edge edge : edges) {
			((TestEdge) edge).reset();
		}
		/* ---- Move a vertex. ---- */
		// Check the lengths of the to-be-affected edges.
		assertEquals(1f, edges.get(0).getLength(), 1e-7f);
		assertEquals(1f, edges.get(3).getLength(), 1e-7f);

		// Move the vertex.
		vertex = allVertices.get(0);
		vertex.setLocation(0f, 0f, -1f);

		// Check the length of the first edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(0)).wasUpdated());
		assertEquals(Math.sqrt(2), edges.get(0).getLength(), 1e-7f);

		// Check the length of the fourth edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(3)).wasUpdated());
		assertEquals(2f, edges.get(3).getLength(), 1e-7f);
		/* ------------------------ */

		// Reset all of the updated flags. Normally this is not necessary with
		// TestEdges, but we want to make absolutely sure the flag is reset.
		for (Edge edge : edges) {
			((TestEdge) edge).reset();
		}
		/* ---- Move another vertex. ---- */
		// Check the lengths of the to-be-affected edges.
		assertEquals(Math.sqrt(2), edges.get(0).getLength(), 1e-7f);
		assertEquals(Math.sqrt(2), edges.get(4).getLength(), 1e-7f);
		assertEquals(1f, edges.get(1).getLength(), 1e-7f);

		// Move the vertex.
		vertex = allVertices.get(1);
		vertex.setLocation(2f, 0f, 0f);

		// Check the length of the first edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(0)).wasUpdated());
		assertEquals(2.23606798f, edges.get(0).getLength(), 1e-7f);
		// Check the length of the fifth edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(4)).wasUpdated());
		assertEquals(1f, edges.get(4).getLength(), 1e-7f);
		// Check the length of the second edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(1)).wasUpdated());
		assertEquals(Math.sqrt(2), edges.get(1).getLength(), 1e-7f);
		/* ------------------------------ */

		return;
		// end-user-code
	}
}