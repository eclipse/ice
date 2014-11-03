/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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

import static org.junit.Assert.fail;

import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.Vertex;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class provides a simple generalization of an Edge that keeps track of
 * when one of its vertices was updated. Additional functionality may be added
 * at a later time.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestEdge extends Edge {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Whether or not the Edge's update method was called.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicBoolean wasUpdated = new AtomicBoolean(false);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Calls the super constructor with the same signature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param start
	 *            <p>
	 *            The first Vertex in this Edge.
	 *            </p>
	 * @param end
	 *            <p>
	 *            The second Vertex in this Edge.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestEdge(Vertex start, Vertex end) {
		// begin-user-code
		super(start, end);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Calls the super constructor with the same signature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param vertices
	 *            <p>
	 *            The two Vertices this Edge connects.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestEdge(ArrayList<Vertex> vertices) {
		// begin-user-code
		super(vertices);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the update method of Edge to mark a flag when the Edge is
	 * notified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param vertex
	 *            <p>
	 *            The Vertex that has been updated.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void update(IUpdateable component) {
		// begin-user-code

		// Call the super's update method and update the flag.
		super.update(component);
		wasUpdated.set(true);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets whether or not the Edge's update method was called. This also resets
	 * the value to false after the call.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the Edge's update method was called, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasUpdated() {
		// begin-user-code

		// Wait a couple of seconds so that the thread can work, but break early
		// if the thread has finished.
		double seconds = 2;
		double sleepTime = 0.0;
		while (!wasUpdated.get() && sleepTime < seconds) {
			try {
				Thread.sleep(50);
				sleepTime += 0.05;
			} catch (InterruptedException e) {
				// Complain and fail
				e.printStackTrace();
				fail();
			}
		}

		// Reset wasUpdated and return its previous value.
		return wasUpdated.getAndSet(false);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Resets the Test class' flag that notifies it was updated. This is
	 * normally not necessary but can be used in lieu of multiple calls to
	 * wasUpdated().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code
		wasUpdated.set(false);
		// end-user-code
	}
}