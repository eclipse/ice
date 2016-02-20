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
package org.eclipse.eavp.viz.service.mesh.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.EdgeAndVertexFaceMesh;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * <p>
 * Performs integration tests of the Mesh data structures.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class MeshTester {
	/**
	 * <p>
	 * This operation performs an integration test on the Mesh data structures.
	 * </p>
	 * 
	 */
	@Test
	public void checkMesh() {

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

		AbstractController mesh = new AbstractController(new AbstractMesh(),
				new AbstractView());
		NekPolygonController polygon;
		ArrayList<VertexController> vertices;
		VertexController vertex;
		ArrayList<EdgeController> edges;
		int counter = 1;

		ArrayList<VertexController> allVertices = new ArrayList<VertexController>();
		ArrayList<EdgeController> allEdges = new ArrayList<EdgeController>();
		ArrayList<NekPolygonController> allPolygons = new ArrayList<NekPolygonController>();

		/* ---- Add a new polygon. ---- */
		// Create the vertices.
		vertices = new ArrayList<VertexController>();
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 0f),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 0f),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 1f),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 1f),
				new AbstractView()));
		for (VertexController v : vertices) {
			v.setProperty("Id", Integer.toString(counter++));
		}
		// Create the edges.
		edges = new ArrayList<EdgeController>();
		for (int i = 0; i < 4; i++) {
			edges.add(new TestEdge(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % 4)),
					new AbstractView()));
			edges.get(i).setProperty("Id", Integer.toString(counter++));
		}
		// Create the polygon.
		polygon = new NekPolygonController(new EdgeAndVertexFaceMesh(),
				new AbstractView());
		for (EdgeController e : edges) {
			polygon.addEntityByCategory(e, "Edges");
		}
		for (VertexController v : vertices) {
			polygon.addEntityByCategory(v, "Vertices");
		}
		polygon.setProperty("Id", Integer.toString(counter++));

		// Update the current lists of all edges, vertices, and polygons.
		allVertices.addAll(vertices);
		allEdges.addAll(edges);
		allPolygons.add(polygon);

		// Add the polygon to the component.
		mesh.addEntity(polygon);

		// Check the mesh's contents.
		checkContents(mesh, allVertices, allEdges, allPolygons);
		/* ---------------------------- */

		/* ---- Add another polygon. ---- */
		// Create the vertices.
		vertices = new ArrayList<VertexController>();
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 1f),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(2f, 0f, 1f),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(2f, 0f, 2f),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 2f),
				new AbstractView()));
		vertices.get(0).setProperty("Id", "3");
		for (int i = 1; i < 4; i++) {
			vertices.get(i).setProperty("Id", Integer.toString(counter++));
		}
		// Create the edges.
		edges = new ArrayList<EdgeController>();
		for (int i = 0; i < 4; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 4)));
			edges.get(i).setProperty("Id", Integer.toString(counter++));
		}
		// Create the polygon.
		polygon = new NekPolygonController(new EdgeAndVertexFaceMesh(),
				new AbstractView());
		for (EdgeController e : edges) {
			polygon.addEntityByCategory(e, "Edges");
		}
		for (VertexController v : vertices) {
			polygon.addEntityByCategory(v, "Vertices");
		}
		polygon.setProperty("Id", Integer.toString(counter++));

		// Update the current lists of all edges, vertices, and polygons.
		for (int i = 0; i < 4; i++) {
			allVertices.add(vertices.get(i));
		}
		allEdges.addAll(edges);
		allPolygons.add(polygon);

		// Add the polygon to the component.
		mesh.addEntity(polygon);

		// Check the mesh's contents.
		checkContents(mesh, allVertices, allEdges, allPolygons);
		/* ------------------------------ */

		/* ---- Remove a polygon. ---- */
		// Update the current lists of all edges, vertices, and polygons.
		for (int i = 7; i >= 4; i--) {
			allVertices.remove(i);
		}
		for (int i = 7; i >= 4; i--) {
			allEdges.remove(i);
		}
		allPolygons.remove(1);

		// Remove the polygon.
		mesh.removeEntity(polygon);

		// Check the mesh's contents.
		checkContents(mesh, allVertices, allEdges, allPolygons);
		/* --------------------------- */

		/* ---- Add another polygon. ---- */
		// Create the vertices.
		vertices = new ArrayList<VertexController>();
		vertices.add(new VertexController(new VertexMesh(1d, 0d, 0d),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(2d, 0d, 1d),
				new AbstractView()));
		vertices.add(new VertexController(new VertexMesh(1d, 0d, 1d),
				new AbstractView()));
		vertices.get(0).setProperty("Id", "2");
		vertices.get(1).setProperty("Id", Integer.toString(counter++));
		vertices.get(2).setProperty("Id", "3");
		// Create the edges.
		edges = new ArrayList<EdgeController>();
		for (int i = 0; i < 3; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 3)));
			edges.get(i).setProperty("Id", Integer.toString(counter++));
		}
		edges.get(2).setProperty("Id", "6");

		// Create the polygon.
		polygon = new NekPolygonController(new EdgeAndVertexFaceMesh(),
				new AbstractView());
		for (EdgeController e : edges) {
			polygon.addEntityByCategory(e, "Edges");
		}
		for (VertexController v : vertices) {
			polygon.addEntityByCategory(v, "Vertices");
		}
		polygon.setProperty("Id", Integer.toString(counter++));

		// Update the current lists of all edges, vertices, and polygons.
		allVertices.addAll(vertices);
		allEdges.addAll(edges);
		allPolygons.add(polygon);

		// Add the polygon to the component.
		mesh.addEntity(polygon);

		// Check the mesh's contents.
		checkContents(mesh, allVertices, allEdges, allPolygons);
		/* ------------------------------ */

		// Reset all of the updated flags. Normally this is not necessary with
		// TestEdges, but we want to make absolutely sure the flag is reset.
		for (EdgeController edge : edges) {
			((TestEdge) edge).reset();
		}
		/* ---- Move a vertex. ---- */
		// Check the lengths of the to-be-affected edges.
		assertEquals(1.414214d, edges.get(0).getLength(), 1e-5f);
		assertEquals(1d, edges.get(2).getLength(), 1e-5f);

		// Move the vertex.
		vertex = (VertexController) edges.get(0)
				.getEntitiesByCategory("Vertices").get(0);
		vertex.updateLocation(0d, 0d, -1d);

		// Check the length of the first edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(0)).wasUpdated());
		assertEquals(1.41421, edges.get(0).getLength(), 1e-5f);

		// Check the length of the fourth edge. Make sure it's been updated.
		assertTrue(((TestEdge) edges.get(2)).wasUpdated());
		assertEquals(1d, edges.get(2).getLength(), 1e-5f);
		/* ------------------------ */

		return;
	}

	/**
	 * Checks that the contents of the given AbstractController's entity map are
	 * exactly equal to the union of the provided three lists of mesh parts.
	 * 
	 * @param mesh
	 *            The controller being checked for equality
	 * @param allVertices
	 *            The list of vertices which should be in mesh
	 * @param allEdges
	 *            The list of edges which should be in mesh
	 * @param allPolygons
	 *            The list of faces which should bei n mesh
	 */
	public void checkContents(AbstractController mesh,
			ArrayList<VertexController> allVertices,
			ArrayList<EdgeController> allEdges,
			ArrayList<NekPolygonController> allPolygons) {

		// The list of all child entities in the mesh, sorted by type
		List<AbstractController> meshPolygons = mesh.getEntities();
		List<AbstractController> meshEdges = new ArrayList<AbstractController>();
		List<AbstractController> meshVertices = new ArrayList<AbstractController>();

		// Extract the children from each mesh's polygons
		for (AbstractController face : meshPolygons) {

			// Get the edges
			for (AbstractController edge : face
					.getEntitiesByCategory("Edges")) {
				if (!meshEdges.contains(edge)) {
					meshEdges.add(edge);
				}
			}

			// Get the vertices
			for (AbstractController vertex : face
					.getEntitiesByCategory("Vertices")) {
				if (!meshVertices.contains(vertex)) {
					meshVertices.add(vertex);
				}
			}
		}

		// Check that every object in the lists is also in the mesh
		assertTrue(meshVertices.containsAll(allVertices));
		assertTrue(allVertices.containsAll(meshVertices));
		assertTrue(meshEdges.containsAll(allEdges));
		assertTrue(allEdges.containsAll(meshEdges));
		assertTrue(meshPolygons.containsAll(allPolygons));
		assertTrue(allPolygons.containsAll(meshPolygons));
	}
}