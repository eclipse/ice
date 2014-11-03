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
package org.eclipse.ice.client.widgets.mesh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.eclipse.ice.client.widgets.mesh.AbstractMeshController;
import org.eclipse.ice.client.widgets.mesh.StateType;
import org.eclipse.ice.client.widgets.mesh.VertexController;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Checks the VertexController for changes to its fields and for connections
 * with its corresponding model (a Vertex).
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VertexControllerTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the ability to get and set the location of the Vertex associated
	 * with this controller.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLocation() {
		// begin-user-code

		/*
		 * Create a VertexController. Try setting its location to different
		 * values and make sure the model was updated properly. This should also
		 * put the controller into the update queue.
		 */

		// FIXME - jME3 import problem

		// // Create the parameters for input. It needs an IUpdateable and a
		// // ConcurrentLinkedQueue.
		// Vertex vertex = new Vertex(0f, 0f, 0f);
		// ConcurrentLinkedQueue<AbstractMeshController> queue = new
		// ConcurrentLinkedQueue<AbstractMeshController>();
		//
		// // The AbstractMeshController to test. (TestMeshController adds
		// nothing
		// // to the real implementation.)
		// VertexController controller = new VertexController(vertex, queue);
		//
		// /* ---- Check the initial state. ---- */
		// // Vector3f v;
		// // assertEquals(new Vector3f(0f, 0f, 0f), controller.getLocation());
		// /* ---------------------------------- */
		//
		// /* ---- Set the state to a null value. ---- */
		// controller.setState(null);
		//
		// // Check the new state of the controller.
		// assertEquals(StateType.None, controller.getState());
		// // The queue should be empty (no change).
		// assertTrue(queue.isEmpty());
		// /* ---------------------------------------- */
		//
		// /* ---- Set the state to a new value. ---- */
		// controller.setState(StateType.Selected);
		//
		// // Check the new state of the controller.
		// assertEquals(StateType.Selected, controller.getState());
		// // The controller should now be on the queue.
		// assertTrue(queue.peek() == controller);
		//
		// // Reset the queue.
		// queue.clear();
		// /* --------------------------------------- */
		//
		// /* ---- Set the state to the same value. ---- */
		// controller.setState(StateType.Selected);
		//
		// // Check the new state of the controller.
		// assertEquals(StateType.Selected, controller.getState());
		// // The queue should be empty (no change).
		// assertTrue(queue.isEmpty());
		// /* ------------------------------------------ */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the equals and hashCode methods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	@Test
	public void checkEquals() {
		// begin-user-code

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		Vertex model1 = new Vertex(0f, 0f, 0f);
		Vertex model2 = new Vertex(0f, 0f, 0.001f); // Different! Used with
													// unequalObject below.
		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// // Initialize objects for testing.
		// VertexController object = new VertexController(model1, queue);
		// VertexController equalObject = new VertexController(model1, queue);
		// VertexController unequalObject = new VertexController(model2, queue);
		//
		// // Make sure the references are different.
		// assertFalse(object == equalObject);
		// assertFalse(object == unequalObject);
		// assertFalse(equalObject == unequalObject);
		//
		// // Check that equality is reflexive and symmetric.
		// assertTrue(object.equals(object));
		// assertTrue(object.equals(equalObject));
		// assertTrue(equalObject.equals(object));
		//
		// // Check that equals will fail when it should.
		// assertFalse(object.equals==null);
		// assertFalse(object.equals(42));
		// assertFalse("just a string".equals(object));
		// assertFalse(object.equals(unequalObject));
		// assertFalse(unequalObject.equals(object));
		//
		// // Check the hash codes.
		// assertTrue(object.hashCode() == object.hashCode());
		// assertTrue(object.hashCode() == equalObject.hashCode());
		// assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the copy and clone methods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	@Test
	public void checkCopy() {
		// begin-user-code

		// Create the parameters for input. It needs an IUpdateable and a
		// ConcurrentLinkedQueue.
		Vertex model1 = new Vertex(0f, 0f, 0f);
		Material material1 = new Material();
		Vertex model2 = new Vertex(1f, 2f, 3f);
		Material material2 = new Material();

		ConcurrentLinkedQueue<AbstractMeshController> queue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// Initialize objects for testing.
		VertexController object = new VertexController(model1, queue, material1);
		VertexController copy = new VertexController(model2, queue, material2);
		VertexController clone = null;

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
		clone = (VertexController) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
		// end-user-code
	}
}