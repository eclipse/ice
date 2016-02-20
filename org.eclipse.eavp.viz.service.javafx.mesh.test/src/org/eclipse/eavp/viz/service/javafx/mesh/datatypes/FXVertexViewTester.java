/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.mesh.datatypes;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.mesh.datatypes.FXVertexView;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FXVertexView.
 * 
 * @author Robert Smith
 *
 */
public class FXVertexViewTester {

	/**
	 * Test that FXLinearViews are cloned correctly
	 */
	@Test
	public void checkClone() {

		// Create a cloned view and check that it is identical to the original
		VertexMesh mesh = new VertexMesh();
		FXVertexView view = new FXVertexView(mesh);
		FXVertexView clone = (FXVertexView) view.clone();
		assertTrue(view.equals(clone));
	}
}
