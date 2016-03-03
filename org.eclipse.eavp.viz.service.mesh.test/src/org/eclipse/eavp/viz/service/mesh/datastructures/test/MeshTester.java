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
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.DetailedFaceMesh;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
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

		IController mesh = new BasicController(new BasicMesh(),
				new BasicView());
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
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 1f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 1f),
				new BasicView()));
		for (VertexController v : vertices) {
			v.setProperty(MeshProperty.ID, Integer.toString(counter++));
		}
		// Create the edges.
		edges = new ArrayList<EdgeController>();
		for (int i = 0; i < 4; i++) {
			edges.add(new TestEdge(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % 4)),
					new BasicView()));
			edges.get(i).setProperty(MeshProperty.ID,
					Integer.toString(counter++));
		}
		// Create the polygon.
		polygon = new NekPolygonController(new DetailedFaceMesh(),
				new BasicView());
		for (EdgeController e : edges) {
			polygon.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			polygon.addEntityToCategory(v, MeshCategory.VERTICES);
		}
		polygon.setProperty(MeshProperty.ID, Integer.toString(counter++));

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
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(2f, 0f, 1f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(2f, 0f, 2f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(1f, 0f, 2f),
				new BasicView()));
		vertices.get(0).setProperty(MeshProperty.ID, "3");
		for (int i = 1; i < 4; i++) {
			vertices.get(i).setProperty(MeshProperty.ID,
					Integer.toString(counter++));
		}
		// Create the edges.
		edges = new ArrayList<EdgeController>();
		for (int i = 0; i < 4; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 4)));
			edges.get(i).setProperty(MeshProperty.ID,
					Integer.toString(counter++));
		}
		// Create the polygon.
		polygon = new NekPolygonController(new DetailedFaceMesh(),
				new BasicView());
		for (EdgeController e : edges) {
			polygon.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			polygon.addEntityToCategory(v, MeshCategory.VERTICES);
		}
		polygon.setProperty(MeshProperty.ID, Integer.toString(counter++));

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
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(2d, 0d, 1d),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(1d, 0d, 1d),
				new BasicView()));
		vertices.get(0).setProperty(MeshProperty.ID, "2");
		vertices.get(1).setProperty(MeshProperty.ID,
				Integer.toString(counter++));
		vertices.get(2).setProperty(MeshProperty.ID, "3");
		// Create the edges.
		edges = new ArrayList<EdgeController>();
		for (int i = 0; i < 3; i++) {
			edges.add(new TestEdge(vertices.get(i), vertices.get((i + 1) % 3)));
			edges.get(i).setProperty(MeshProperty.ID,
					Integer.toString(counter++));
		}
		edges.get(2).setProperty(MeshProperty.ID, "6");

		// Create the polygon.
		polygon = new NekPolygonController(new DetailedFaceMesh(),
				new BasicView());
		for (EdgeController e : edges) {
			polygon.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			polygon.addEntityToCategory(v, MeshCategory.VERTICES);
		}
		polygon.setProperty(MeshProperty.ID, Integer.toString(counter++));

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
		vertex = edges.get(0)
				.getEntitiesFromCategory(MeshCategory.VERTICES, VertexController.class).get(0);
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
	 * Checks that the contents of the given IController's entity map are
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
	public void checkContents(IController mesh,
			ArrayList<VertexController> allVertices,
			ArrayList<EdgeController> allEdges,
			ArrayList<NekPolygonController> allPolygons) {

		// The list of all child entities in the mesh, sorted by type
		List<IController> meshPolygons = mesh.getEntities();
		List<IController> meshEdges = new ArrayList<IController>();
		List<IController> meshVertices = new ArrayList<IController>();

		// Extract the children from each mesh's polygons
		for (IController face : meshPolygons) {

			// Get the edges
			for (IController edge : face
					.getEntitiesFromCategory(MeshCategory.EDGES)) {
				if (!meshEdges.contains(edge)) {
					meshEdges.add(edge);
				}
			}

			// Get the vertices
			for (IController vertex : face
					.getEntitiesFromCategory(MeshCategory.VERTICES)) {
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