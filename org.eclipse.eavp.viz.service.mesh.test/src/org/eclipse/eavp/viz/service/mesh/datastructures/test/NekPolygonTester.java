/*******************************************************************************
 * Copyright (c) 2014-2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.mesh.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryConditionType;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonMesh;
import org.eclipse.eavp.viz.service.mesh.datastructures.PolygonProperties;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * <p>
 * Tests the NekPolygon class.
 * </p>
 *
 * @author Jordan H. Deyton
 * @author Robert Smith
 */
public class NekPolygonTester {

	/**
	 * <p>
	 * This operation tests the construction of the Polygon class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 *
	 */
	@Test
	public void checkCreation() {

		VertexController vertex;
		EdgeController edge;

		// We need edges and vertices to supply to created Polygons.
		ArrayList<EdgeController> edges = new ArrayList<EdgeController>();
		ArrayList<VertexController> vertices = new ArrayList<VertexController>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(5f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 5f),
				new BasicView()));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setProperty(MeshProperty.ID,
					Integer.toString(i + 1));
		}

		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new EdgeController(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % size)),
					new BasicView());
			edge.setProperty(MeshProperty.ID, Integer.toString(i + 1));

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Create a Polygon to test.
		NekPolygonController polygon;

		/* ---- Test the nullary constructor. ---- */
		// Initialize the polygon.
		polygon = new NekPolygonController(new FaceMesh(), new BasicView());

		// Check the edges.
		assertNotNull(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
		assertTrue(polygon.getEntitiesFromCategory(MeshCategory.EDGES).isEmpty());

		// Check the vertices.
		assertNotNull(polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		assertTrue(
				polygon.getEntitiesFromCategory(MeshCategory.VERTICES).isEmpty());
				/* --------------------------------------- */

		/* ---- Try a valid polygon. ---- */
		for (EdgeController e : edges) {
			polygon.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			polygon.addEntityToCategory(v, MeshCategory.VERTICES);
		}

		// Check the contents of the polygon's Edge List.
		List<IController> polygonEdges = polygon
				.getEntitiesFromCategory(MeshCategory.EDGES);
		assertNotNull(polygonEdges);
		assertEquals(size, polygonEdges.size());
		assertEquals(edges, polygonEdges);

		// Check the contents of the polygon's Vertex List.
		List<IController> polygonVertices = polygon
				.getEntitiesFromCategory(MeshCategory.VERTICES);
		assertNotNull(polygonVertices);
		assertEquals(size, polygonVertices.size());
		assertEquals(vertices, polygonVertices);
		/* ------------------------------ */

		/* ---- Make sure we cannot modify the Edge/Vertex Lists. ---- */
		// Try to remove an edge.
		polygon.getEntitiesFromCategory(MeshCategory.EDGES).remove(0);
		polygonEdges = polygon.getEntitiesFromCategory(MeshCategory.EDGES);
		assertEquals(size, polygonEdges.size());
		assertEquals(edges, polygonEdges);

		// Try to remove a vertex.
		polygon.getEntitiesFromCategory(MeshCategory.VERTICES).clear();
		assertEquals(size, polygonVertices.size());
		assertEquals(vertices, polygonVertices);
		/* ----------------------------------------------------------- */

		/* ---- Check invalid arguments. ---- */
		try {
			// Try passing in nulls.
			polygon = new NekPolygonController(null, null);

			// If we've reached this point, then there was no exception thrown!
			fail();

		} catch (IllegalArgumentException e) {

		}
		/* ----------------------------------------- */

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
		EdgeController edge;

		// We need edges and vertices to supply to created Polygons.
		ArrayList<EdgeController> edges = new ArrayList<EdgeController>();
		ArrayList<VertexController> vertices = new ArrayList<VertexController>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new VertexController(new VertexMesh(0d, 0d, 0d),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(5d, 0d, 5d),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(0d, 0d, 5d),
				new BasicView()));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setProperty(MeshProperty.ID,
					Integer.toString(i + 1));
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new EdgeController(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % size)),
					new BasicView());
			edge.setProperty(MeshProperty.ID, Integer.toString(i + 1));

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		NekPolygonController polygon = new NekPolygonController(new FaceMesh(),
				new BasicView());
		for (EdgeController e : edges) {
			polygon.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			polygon.addEntityToCategory(v, MeshCategory.VERTICES);
		}
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
			assertEquals(defaultCondition,
					polygon.getFluidBoundaryCondition(i));
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
		NekPolygonController polygon = new NekPolygonController(new FaceMesh(),
				new BasicView());
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
		EdgeController edge;

		// We need edges and vertices to supply to created Polygons.
		ArrayList<EdgeController> edges = new ArrayList<EdgeController>();
		ArrayList<VertexController> vertices = new ArrayList<VertexController>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new VertexController(new VertexMesh(0d, 0d, 0d),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(5d, 0d, 0d),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(0d, 0d, 5d),
				new BasicView()));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setProperty(MeshProperty.ID,
					Integer.toString(i + 1));
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			edge = new EdgeController(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % size)),
					new BasicView());
			edge.setProperty(MeshProperty.ID, Integer.toString(i + 1));

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		NekPolygonController polygon = new NekPolygonController(new FaceMesh(),
				new BasicView());
		for (EdgeController e : edges) {
			polygon.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			polygon.addEntityToCategory(v, MeshCategory.VERTICES);
		}
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

	// TODO Reimplement this test if JAXB xml persistence is ever implemented
	// for the Mesh Editor.
	// /**
	// * This operation checks the ability of the Polygon to persist itself to
	// XML
	// * and to load itself from an XML input stream.
	// *
	// * @throws IOException
	// * @throws JAXBException
	// * @throws NullPointerException
	// */
	// @Test
	// public void checkLoadingFromXML()
	// throws NullPointerException, JAXBException, IOException {
	//
	// // We need edges and vertices to supply to created Polygons.
	// ArrayList<EdgeController> edges = new ArrayList<EdgeController>();
	// ArrayList<VertexController> vertices = new ArrayList<VertexController>();
	// // Local Declarations
	// VizJAXBHandler xmlHandler = new VizJAXBHandler();
	// ArrayList<Class> classList = new ArrayList<Class>();
	// classList.add(NekPolygonController.class);
	//
	// // For our test, we'll just make a triangle with one point at the origin
	// // and the other two on the x and z axes at a distance of 5.
	// vertices.add(new VertexController(new VertexMesh(0d, 0d, 0d),
	// new AbstractView()));
	// vertices.add(new VertexController(new VertexMesh(5d, 0d, 0d),
	// new AbstractView()));
	// vertices.add(new VertexController(new VertexMesh(0d, 0d, 5d),
	// new AbstractView()));
	//
	// // Set the IDs for the vertices.
	// for (int i = 0; i < vertices.size(); i++) {
	// vertices.get(i).setProperty("Id", Integer.toString(i + 1));
	// }
	// // Get the edges.
	// int size = vertices.size();
	// for (int i = 0; i < size; i++) {
	// // Create a new ArrayList holding the vertices that will describe
	// // the current edge.
	// EdgeController edge = new EdgeController(new EdgeMesh(vertices.get(i),
	// vertices.get((i + 1) % size)), new AbstractView());
	// edge.setProperty("Id", Integer.toString(i + 1));
	//
	// // Add the edge to the List of Edges.
	// edges.add(edge);
	// }
	//
	// // Create a Polygon to test.
	// NekPolygonController polygon = new NekPolygonController(new FaceMesh(),
	// new AbstractView());
	// for (EdgeController e : edges) {
	// polygon.addEntity(e);
	// }
	// for (VertexController v : vertices) {
	// polygon.addEntity(v);
	// }
	//
	// // Add a fluid and passive scalar boundary condition.
	// polygon.setFluidBoundaryCondition(1,
	// new BoundaryCondition(BoundaryConditionType.Flux));
	// polygon.setOtherBoundaryCondition(1, 1,
	// new BoundaryCondition(BoundaryConditionType.Wall));
	//
	// // Add a PolygonProperties.
	// polygon.setPolygonProperties("leet", 1337);
	//
	// // Load it into XML.
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// xmlHandler.write(polygon, classList, outputStream);
	//
	// // Convert the output stream data to an input stream.
	// ByteArrayInputStream inputStream = new ByteArrayInputStream(
	// outputStream.toByteArray());
	//
	// // Load the input stream's contents into a new component.
	// NekPolygonController loadedPolygon = new NekPolygonController();
	// loadedPolygon = (NekPolygonController) xmlHandler.read(classList,
	// inputStream);
	//
	// // Make sure the two components match.
	// assertTrue(polygon.equals(loadedPolygon));
	//
	// return;
	// }

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
		ArrayList<EdgeController> edges = new ArrayList<EdgeController>();
		ArrayList<VertexController> vertices = new ArrayList<VertexController>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(5f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 5f),
				new BasicView()));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setProperty(MeshProperty.ID,
					Integer.toString(i + 1));
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			EdgeController edge = new EdgeController(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % size)),
					new BasicView());
			edge.setProperty(MeshProperty.ID, Integer.toString(i + 1));

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Initialize objects for testing.
		NekPolygonController object = new NekPolygonController(new FaceMesh(),
				new BasicView());
		for (EdgeController e : edges) {
			object.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			object.addEntityToCategory(v, MeshCategory.VERTICES);
		}
		NekPolygonController equalObject = new NekPolygonController(
				new FaceMesh(), new BasicView());
		for (EdgeController e : edges) {
			equalObject.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			equalObject.addEntityToCategory(v, MeshCategory.VERTICES);
		}

		// Change one of the vertices.
		vertices.set(2, new VertexController(new VertexMesh(0d, 0d, 6d),
				new BasicView()));
		vertices.get(2).setProperty(MeshProperty.ID, "3");
		// Update the corresponding edges.
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			EdgeController edge = new EdgeController(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % size)),
					new BasicView());
			edge.setProperty(MeshProperty.ID, Integer.toString(i + 1));

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Initialize the unequalObject.
		NekPolygonController unequalObject = new NekPolygonController(
				new FaceMesh(), new BasicView());
		for (EdgeController e : edges) {
			unequalObject.addEntityToCategory(e, MeshCategory.EDGES);
		}
		for (VertexController v : vertices) {
			unequalObject.addEntityToCategory(v, MeshCategory.VERTICES);
		}

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
		ArrayList<EdgeController> edges = new ArrayList<EdgeController>();
		ArrayList<VertexController> vertices = new ArrayList<VertexController>();

		// For our test, we'll just make a triangle with one point at the origin
		// and the other two on the x and z axes at a distance of 5.
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(5f, 0f, 0f),
				new BasicView()));
		vertices.add(new VertexController(new VertexMesh(0f, 0f, 5f),
				new BasicView()));

		// Set the IDs for the vertices.
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setProperty(MeshProperty.ID,
					Integer.toString(i + 1));
		}
		// Get the edges.
		int size = vertices.size();
		for (int i = 0; i < size; i++) {
			// Create a new ArrayList holding the vertices that will describe
			// the current edge.
			EdgeController edge = new EdgeController(
					new EdgeMesh(vertices.get(i), vertices.get((i + 1) % size)),
					new BasicView());
			edge.setProperty(MeshProperty.ID, Integer.toString(i + 1));

			// Add the edge to the List of Edges.
			edges.add(edge);
		}

		// Create a Polygon to test.
		NekPolygonController object = new NekPolygonController(
				new NekPolygonMesh(), new BasicView());
		for (EdgeController e : edges) {
			object.addEntityToCategory(e, MeshCategory.EDGES);
		}

		NekPolygonController copy = new NekPolygonController(
				new NekPolygonMesh(), new BasicView());
		for (EdgeController e : edges) {
			EdgeController temp = (EdgeController) e.clone();
			temp.setProperty(MeshProperty.ID, "2");
			copy.addEntity(temp);
		}

		NekPolygonController clone = new NekPolygonController(
				new NekPolygonMesh(), new BasicView());
		for (EdgeController e : edges) {
			EdgeController temp = (EdgeController) e.clone();
			temp.setProperty(MeshProperty.ID, "3");
			copy.addEntity(temp);
		}

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
		clone = (NekPolygonController) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}