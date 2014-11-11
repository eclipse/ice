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
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the Vertex class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton, Taylor Patterson
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VertexTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the construction of the Vertex class and the
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

		/* ---- Test array input ---- */
		// Initialize a vertex at (1,1,1)
		float[] location = { 1f, 1f, 1f };
		Vertex vertex = new Vertex(location);

		// Check the contents of the vertex's location list
		float[] vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(location, vertexLocation));

		// Ensure that changing the input location does not change the vertex
		location[0] = 0f;
		vertexLocation = vertex.getLocation();
		assertFalse(Arrays.equals(location, vertexLocation));
		/* -------------------------- */

		/* ---- Test three float input ---- */
		vertex = new Vertex(location[0], location[1], location[2]);

		// Check the contents of the vertex's location list
		vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(new float[] { location[0], location[1],
				location[2] }, vertexLocation));
		/* -------------------------------- */

		/* ---- Test null input ---- */
		// Initialize the vertex
		try {
			vertex = new Vertex(null);

			// Check the contents of the vertex's location list
			vertexLocation = vertex.getLocation();
			assertNotNull(vertexLocation);
			assertEquals(3, vertexLocation.length);
			assertTrue(Arrays
					.equals(new float[] { 0f, 0f, 0f }, vertexLocation));

			// If we've reached this point, then there was no exception thrown!
			fail();
		} catch (IllegalArgumentException e) {

		}
		/* ------------------------- */

		/* ---- Test the nullary constructor. ---- */
		// Initialize the vertex
		vertex = new Vertex();

		// Check the contents of the vertex's location list
		vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(new float[] { 0f, 0f, 0f }, vertexLocation));
		/* --------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations checks the getter and setter for the Vertex's location.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLocation() {
		// begin-user-code

		/* ---- Test location getter ---- */
		// Create a vertex at the origin
		float[] location = { 0f, 0f, 0f };
		Vertex vertex = new Vertex(location);

		// Check the contents of the vertex's location list
		float[] vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(location, vertexLocation));

		// Make sure the location cannot be directly modified
		vertexLocation[0] = 5f;
		assertEquals(3, vertex.getLocation().length);
		assertTrue(Arrays.equals(location, vertex.getLocation()));
		/* ------------------------------ */

		/* ---- Test location setters ---- */
		// Check set location with null
		vertex.setLocation(null);
		vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(location, vertexLocation));

		// Check set location with array
		location[0] = 5f;
		vertex.setLocation(location);
		vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(location, vertexLocation));

		// Check set location with 3 floats
		location[0] = 0f;
		vertex.setLocation(location[0], location[1], location[2]);
		vertexLocation = vertex.getLocation();
		assertNotNull(vertexLocation);
		assertEquals(3, vertexLocation.length);
		assertTrue(Arrays.equals(location, vertexLocation));
		/* ------------------------------- */

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks Vertex to ensure that it can be correctly visited
	 * by a realization of the IMeshPartVisitor interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVisitation() {
		// begin-user-code

		IMeshPart part = new Vertex();

		// ---- Check visiting with an IMeshPartVisitor. ---- //
		// Create a new TestMeshVisitor that only does anything useful when
		// visiting a Vertex.
		TestMeshVisitor meshVisitor = new TestMeshVisitor() {
			@Override
			public void visit(Vertex vertex) {
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
	 * This operation tests the Vertex to ensure that it can properly dispatch
	 * notifications when it changes its state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkNotification() {
		// begin-user-code

		// Create a vertex at the origin
		float[] location = { 0f, 0f, 0f };

		// Create a Vertex to test
		Vertex start = new Vertex(location);
		start.setId(1);
		Vertex end = new Vertex(location);
		end.setId(2);

		// Alter the location for a second vertex.
		location[0] = 5f;

		// Create an Edge to test.
		TestEdge testEdge = new TestEdge(start, end);

		// Make sure no notification has been made
		assertFalse(testEdge.wasUpdated());

		// Alter the vertex location.
		start.setLocation(location);

		// Test if the edge was updated.
		assertTrue(testEdge.wasUpdated());

		// Set the same location.
		start.setLocation(location[0], location[1], location[2]);

		// Make sure no notification has been made.
		assertFalse(testEdge.wasUpdated());

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the Vertex to persist itself to XML
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

		// Create a vertex at the origin
		float[] location = { 0f, 0f, 0f };

		// Create a Vertex to test
		Vertex vertex = new Vertex(location);

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		vertex.persistToXML(outputStream);
		assertNotNull(outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		Vertex loadedVertex = new Vertex();
		loadedVertex.loadFromXML(inputStream);

		// Make sure the two components match.
		assertTrue(vertex.equals(loadedVertex));

		// Check invalid parameters.

		// Try passing null and make sure the components match.
		inputStream = null;
		loadedVertex.loadFromXML(inputStream);
		assertTrue(vertex.equals(loadedVertex));

		// Try passing a bad input stream and make sure the components match.
		inputStream = new ByteArrayInputStream("jkl;2invalidstream".getBytes());
		loadedVertex.loadFromXML(inputStream);
		assertTrue(vertex.equals(loadedVertex));

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Vertex to insure that its equals() and
	 * hashCode() operations work.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Initialize objects for testing.
		Vertex object = new Vertex(10f, 14f, 12.1f);
		Vertex equalObject = new Vertex(new float[] { 10f, 14f, 12.1f });
		Vertex unequalObject = new Vertex(10f, 14f, 12f);

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Vertex to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Initialize objects for testing.
		Vertex object = new Vertex(10f, 14f, 12.1f);
		Vertex copy = new Vertex(1f, 2f, 3f);
		Vertex clone = null;

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
		clone = (Vertex) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;

		// end-user-code
	}
}