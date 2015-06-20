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

import org.eclipse.ice.datastructures.form.mesh.BezierEdge;
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * Tests the BezierEdge class.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@Ignore
public class BezierEdgeTester {
	/**
	 * <p>
	 * This operation tests the construction of the BezierEdge class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * This operation checks BezierEdge to ensure that it can be correctly
	 * visited by a realization of the IMeshPartVisitor interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		IMeshPart part = new BezierEdge();

		// ---- Check visiting with an IMeshPartVisitor. ---- //
		// Create a new TestMeshVisitor that only does anything useful when
		// visiting a BezierEdge.
		TestMeshVisitor meshVisitor = new TestMeshVisitor() {
			@Override
			public void visit(BezierEdge edge) {
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
}