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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.ice.viz.service.mesh.datastructures.BoundaryConditionType;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshPart;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.PolygonProperties;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.junit.Test;

/**
 * <p>
 * Tests the Polygon class.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class PolygonTester {

	/**
	 * <p>
	 * This operation tests the construction of the Polygon class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		Vertex vertex;
		Edge edge;

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}

		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Create a Polygon to test.
		Polygon polygon;

		/* ---- Test the nullary constructor. ---- */
		// Initialize the polygon.
		polygon = new Polygon();

		// Check the edges.
		assertNotNull(polygon.getEdges());
		assertTrue(polygon.getEdges().isEmpty());

		// Check the vertices.
		assertNotNull(polygon.getVertices());
		assertTrue(polygon.getVertices().isEmpty());
		/* --------------------------------------- */

		/* ---- Try a valid polygon. ---- */
		polygon = new Polygon(edges, vertices);

		// Check the contents of the polygon's Edge List.
		ArrayList<Edge> polygonEdges = polygon.getEdges();
		assertNotNull(polygonEdges);
		assertEquals(size, polygonEdges.size());
		assertEquals(edges, polygonEdges);

		// Check the contents of the polygon's Vertex List.
		ArrayList<Vertex> polygonVertices = polygon.getVertices();
		assertNotNull(polygonVertices);
		assertEquals(size, polygonVertices.size());
		assertEquals(vertices, polygonVertices);
		/* ------------------------------ */

		/* ---- Make sure we cannot modify the Edge/Vertex Lists. ---- */
		// Try to remove an edge.
		polygon.getEdges().remove(0);
		assertEquals(size, polygonEdges.size());
		assertEquals(edges, polygonEdges);

		// Try to remove a vertex.
		polygon.getVertices().clear();
		assertEquals(size, polygonVertices.size());
		assertEquals(vertices, polygonVertices);
		/* ----------------------------------------------------------- */

		/* ---- Check invalid arguments. ---- */
		try {
			// Try passing in nulls.
			polygon = new Polygon(null, null);

			// Check the edges.
			assertNotNull(polygon.getEdges());
			assertTrue(polygon.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(polygon.getVertices());
			assertTrue(polygon.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ---------------------------------- */

		/* ---- Try passing in an invalid edge. ---- */
		edge = new Edge();
		edge.setId(100);
		edges.set(2, edge);

		try {
			polygon = new Polygon(edges, vertices);

			// Check the edges.
			assertNotNull(polygon.getEdges());
			assertTrue(polygon.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(polygon.getVertices());
			assertTrue(polygon.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ----------------------------------------- */

		/* ---- Try passing in an edge that uses a different Vertex. ---- */
		vertex = new Vertex(0f, 0f, 5f);
		vertex.setId(200);
		edge = new Edge(vertices.get(2), vertex);
		edge.setId(300);
		edges.set(2, edge);

		try {
			polygon = new Polygon(edges, vertices);

			// Check the edges.
			assertNotNull(polygon.getEdges());
			assertTrue(polygon.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(polygon.getVertices());
			assertTrue(polygon.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* -------------------------------------------------------------- */

		/* ---- Try passing in an edge with the same ID. ---- */
		edge = new Edge(vertices.get(2), vertices.get(0));
		edge.setId(1);
		edges.set(2, edge);

		try {
			polygon = new Polygon(edges, vertices);

			// Check the edges.
			assertNotNull(polygon.getEdges());
			assertTrue(polygon.getEdges().isEmpty());

			// Check the vertices.
			assertNotNull(polygon.getVertices());
			assertTrue(polygon.getVertices().isEmpty());

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* -------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * This operation tests the ability to set and get fluid, thermal, and
	 * passive scalar boundary conditions on edges of a polygon.
	 * </p>
	 * 
	 */
	@Test
	public void checkBoundaryConditions() {

		/**
		 * Boundary conditions can only be get or set for valid edges. The first
		 * step is to create a Polygon. Then, we should check the initial
		 * conditions for its edges. We also need to make sure we can set and
		 * retrieve the conditions.
		 */

		/* ---- Create a polygon. ---- */
		Edge edge;

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		Polygon polygon = new Polygon(edges, vertices);
		/* --------------------------- */

		// The number of passive scalar boundary conditions.
		int NPSCAL = 2;
		int edgeId;
		int scalarId;
		BoundaryCondition condition;
		BoundaryCondition defaultCondition = new BoundaryCondition();

		/* ---- Check the initial boundary conditions. ---- */
		// Loop over the edges. Getters for fluid, thermal, and other should
		// return null.
		for (int i = 1; i <= 3; i++) {
			assertEquals(defaultCondition, polygon.getFluidBoundaryCondition(i));
			assertEquals(defaultCondition,
					polygon.getThermalBoundaryCondition(i));
			for (int j = 0; j < NPSCAL; j++) {
				assertTrue(polygon.getOtherBoundaryCondition(i, j) == null);
			}
		}
		/* ------------------------------------------------ */

		/* ---- Try getting/setting boundary conditions. ---- */
		// Set/get a fluid boundary condition for edge 1.
		edgeId = 1;
		condition = new BoundaryCondition(
				BoundaryConditionType.AxisymmetricBoundary);
		polygon.setFluidBoundaryCondition(edgeId, condition);
		assertEquals(condition, polygon.getFluidBoundaryCondition(edgeId));

		// Update the fluid boundary condition for edge 1.
		condition = new BoundaryCondition(
				BoundaryConditionType.DirichletTemperatureScalar);
		polygon.setFluidBoundaryCondition(edgeId, condition);
		assertEquals(condition, polygon.getFluidBoundaryCondition(edgeId));

		// Set/get a thermal boundary condition for edge 1.
		condition = new BoundaryCondition(
				BoundaryConditionType.DirichletVelocity);
		polygon.setThermalBoundaryCondition(edgeId, condition);
		assertEquals(condition, polygon.getThermalBoundaryCondition(edgeId));

		// Set/get a passive scalar boundary condition for edge 2.
		edgeId = 2;
		scalarId = 1;
		condition = new BoundaryCondition(BoundaryConditionType.Flux);
		polygon.setOtherBoundaryCondition(edgeId, scalarId, condition);
		assertEquals(condition,
				polygon.getOtherBoundaryCondition(edgeId, scalarId));

		// The boundary condition for passive scalar index 0 should be default.
		scalarId = 0;
		assertEquals(defaultCondition,
				polygon.getOtherBoundaryCondition(edgeId, scalarId));

		// Set/get another passive scalar boundary condition edge 2.
		condition = new BoundaryCondition(BoundaryConditionType.Insulated);
		polygon.setOtherBoundaryCondition(edgeId, scalarId, condition);
		assertEquals(condition,
				polygon.getOtherBoundaryCondition(edgeId, scalarId));
		/* -------------------------------------------------- */

		/* ---- Try getting/setting invalid boundary conditions. ---- */
		// Try setting a null fluid condition.
		edgeId = 1;
		condition = polygon.getFluidBoundaryCondition(edgeId);
		polygon.setFluidBoundaryCondition(edgeId, null);
		assertEquals(condition, polygon.getFluidBoundaryCondition(edgeId));

		// Try an invalid edgeId for the fluid condition. Make sure the getter
		// returns null first. Then try to set it. After the set, the getter
		// should still return null.
		edgeId = 4;
		assertTrue(polygon.getFluidBoundaryCondition(edgeId) == null);
		polygon.setFluidBoundaryCondition(edgeId, condition);
		assertTrue(polygon.getFluidBoundaryCondition(edgeId) == null);

		// Now try the same things for the thermal condition.

		// Try setting a null thermal condition.
		edgeId = 1;
		condition = polygon.getThermalBoundaryCondition(edgeId);
		polygon.setThermalBoundaryCondition(edgeId, null);
		assertEquals(condition, polygon.getThermalBoundaryCondition(edgeId));

		// Try an invalid edgeId for the thermal condition. Make sure the getter
		// returns null first. Then try to set it. After the set, the getter
		// should still return null.
		edgeId = 0;
		assertTrue(polygon.getThermalBoundaryCondition(edgeId) == null);
		polygon.setThermalBoundaryCondition(edgeId, condition);
		assertTrue(polygon.getThermalBoundaryCondition(edgeId) == null);

		// Now try similar things for the passive scalar conditions.

		// Try an invalid set command with a bad condition.
		edgeId = 2;
		scalarId = 0;
		condition = polygon.getOtherBoundaryCondition(edgeId, scalarId);
		polygon.setOtherBoundaryCondition(edgeId, scalarId, null);
		assertEquals(condition,
				polygon.getOtherBoundaryCondition(edgeId, scalarId));

		// Try a set command with an invalid edgeId. Make sure the getter
		// returns null first. Then try to set it. After the set, the getter
		// should still return null.
		edgeId = 42;
		assertTrue(polygon.getOtherBoundaryCondition(edgeId, scalarId) == null);
		polygon.setThermalBoundaryCondition(edgeId, condition);
		assertTrue(polygon.getOtherBoundaryCondition(edgeId, scalarId) == null);

		// Try a set command with an invalid scalarId (negative). Make sure the
		// getter
		// returns null first. Then try to set it. After the set, the getter
		// should still return null.
		edgeId = 2;
		scalarId = -1;
		assertTrue(polygon.getOtherBoundaryCondition(edgeId, scalarId) == null);
		polygon.setOtherBoundaryCondition(edgeId, scalarId, condition);
		assertTrue(polygon.getOtherBoundaryCondition(edgeId, scalarId) == null);
		/* ---------------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Checks the PolygonProperties getter and setter.
	 * </p>
	 * 
	 */
	@Test
	public void checkPolygonProperites() {

		// Create a Polygon and PolygonProperties for testing
		Polygon polygon = new Polygon();
		PolygonProperties defaultProps = new PolygonProperties();
		PolygonProperties customProps = new PolygonProperties("54g", 9000);

		// Check the default PolygonProperties
		assertTrue(polygon.getPolygonProperties().equals(defaultProps));

		// Try to set new PolygonProeprties and check it
		polygon.setPolygonProperties("54g", 9000);
		assertTrue(polygon.getPolygonProperties().equals(customProps));

		return;
	}

	/**
	 * <p>
	 * This operation checks Polygon to ensure that it can be correctly visited
	 * by a realization of the IMeshPartVisitor interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		IMeshPart part = new Polygon();

		// ---- Check visiting with an IMeshPartVisitor. ---- //
		// Create a new TestMeshVisitor that only does anything useful when
		// visiting a Polygon.
		TestMeshVisitor meshVisitor = new TestMeshVisitor() {
			@Override
			public void visit(Polygon polygon) {
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
	 * This operation ensures that notifications are sent out to listeners if
	 * the polygon's properties have been changed.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {

		/**
		 * We only need to check the three boundary condition setters. First,
		 * create a polygon and add a listener to it. Then make sure the
		 * listener is notified when each setter is called.
		 * 
		 * We also should pass on alerts when the boundary conditions themselves
		 * have been modified.
		 */

		/* ---- Create a polygon. ---- */
		Edge edge;

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		Polygon polygon = new Polygon(edges, vertices);
		/* --------------------------- */

		// Create and register a listener.
		TestComponentListener listener = new TestComponentListener();
		polygon.register(listener);

		BoundaryCondition condition = new BoundaryCondition();
		int edgeId = 1;
		int scalarId = 0;

		/* ---- Test the setters. ---- */;
		// Test the fluid boundary condition.
		polygon.setFluidBoundaryCondition(edgeId, condition);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Test the thermal boundary condition.
		polygon.setThermalBoundaryCondition(edgeId, condition);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Test a passive scalar boundary condition.
		polygon.setOtherBoundaryCondition(edgeId, scalarId, condition);
		assertTrue(listener.wasNotified());
		listener.reset();
		/* --------------------------- */

		/* ---- Test that updates are relayed from the conditions. ---- */
		// This functionality has currently been disabled.
		condition.setType(BoundaryConditionType.Symmetry);
		assertTrue(listener.wasNotified());
		listener.reset();
		/* ------------------------------------------------------------ */

		return;
	}

	/**
	 * This operation checks the ability of the Polygon to persist itself to XML
	 * and to load itself from an XML input stream.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		// Local Declarations
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Polygon.class);

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			Edge edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Create a Polygon to test.
		Polygon polygon = new Polygon(edges, vertices);

		// Add a fluid and passive scalar boundary condition.
		polygon.setFluidBoundaryCondition(1, new BoundaryCondition(
				BoundaryConditionType.Flux));
		polygon.setOtherBoundaryCondition(1, 1, new BoundaryCondition(
				BoundaryConditionType.Wall));

		// Add a PolygonProperties.
		polygon.setPolygonProperties("leet", 1337);

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(polygon, classList, outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		Polygon loadedPolygon = new Polygon();
		loadedPolygon = (Polygon) xmlHandler.read(classList, inputStream);

		// Make sure the two components match.
		assertTrue(polygon.equals(loadedPolygon));

		return;
	}

	/**
	 * <p>
	 * This operation checks the Polygon to insure that its equals() and
	 * hashCode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			Edge edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Initialize objects for testing.
		Polygon object = new Polygon(edges, vertices);
		Polygon equalObject = new Polygon(edges, vertices);

		// Change one of the vertices.
		vertices.set(2, new Vertex(0f, 0f, 6f));
		vertices.get(2).setId(3);
		// Update the corresponding edges.
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			Edge edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Initialize the unequalObject.
		Polygon unequalObject = new Polygon(edges, vertices);

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
	 * This operation checks the Polygon to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// We need edges and vertices to supply to created Polygons.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new Vertex(0f, 0f, 0f));
		vertices.add(new Vertex(5f, 0f, 0f));
		vertices.add(new Vertex(0f, 0f, 5f));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setId(i + 1);
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			Edge edge = new Edge(vertices.get(i), vertices.get((i + 1) % size));
			edge.setId(i + 1);

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Create a Polygon to test.
		Polygon object = new Polygon(edges, vertices);
		Polygon copy = new Polygon();
		Polygon clone = null;

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
		clone = (Polygon) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}