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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.eclipse.ice.datastructures.form.mesh.PolynomialEdge;

import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the PolynomialEdge class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@Ignore
public class PolynomialEdgeTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the construction of the PolynomialEdge class and the
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
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks PolynomialEdge to ensure that it can be correctly
	 * visited by a realization of the IMeshPartVisitor interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkVisitation() {
		// begin-user-code

		IMeshPart part = new PolynomialEdge();

		// ---- Check visiting with an IMeshPartVisitor. ---- //
		// Create a new TestMeshVisitor that only does anything useful when
		// visiting a PolynomialEdge.
		TestMeshVisitor meshVisitor = new TestMeshVisitor() {
			@Override
			public void visit(PolynomialEdge edge) {
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
}