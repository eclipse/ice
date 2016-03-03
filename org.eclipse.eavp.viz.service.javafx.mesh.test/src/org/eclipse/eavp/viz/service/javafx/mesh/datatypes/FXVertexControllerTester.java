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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IMesh;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FXVertexView
 * 
 * @author Robert Smith
 *
 */
public class FXVertexControllerTester {

	/**
	 * Check that FXEdgeControllers can be properly cloned.
	 */
	@Test
	public void checkClone() {

		// Create a cloned FXVertex and check that it is identical to the
		// original
		VertexMesh mesh = new VertexMesh();
		FXVertexController vertex = new FXVertexController(mesh,
				new FXVertexView(mesh));
		vertex.setProperty(MeshProperty.INNER_RADIUS, "Property");
		FXVertexController clone = (FXVertexController) vertex.clone();
		assertTrue(vertex.equals(clone));
	}

	/**
	 * Checks that the controller correctly triggers refreshes in its view
	 */
	@Test
	public void checkRefresh() {

		// Create a vertex
		VertexMesh mesh = new VertexMesh();
		TestVertexView view = new TestVertexView(mesh);
		FXVertexController vertex = new FXVertexController(mesh, view);

		// Reset the view's refreshed state
		view.wasRefreshed();

		// The view should be refreshed when a property is changed
		mesh.setProperty(MeshProperty.INNER_RADIUS, "Property");
		assertTrue(view.wasRefreshed());

		// The view should be refreshed when the part is selected
		mesh.setProperty(MeshProperty.SELECTED, "True");
		assertTrue(view.wasRefreshed());

		// The view should not be refreshed when a child is added
		mesh.addEntity(
				new BasicController(new BasicMesh(), new BasicView()));
		assertFalse(view.wasRefreshed());
	}

	/**
	 * An extension of FXVertexView which keeps track of whether it has been
	 * refreshed for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestVertexView extends FXVertexView {

		/**
		 * Whether the view has been refreshed since the last time it was
		 * checked.
		 */
		boolean refreshed = false;

		/**
		 * The default constructor.
		 * 
		 * @param model
		 *            The model that this view will represent.
		 */
		public TestVertexView(VertexMesh model) {
			super(model);
		}

		/**
		 * Check if the view was refreshed, then return the view to its original
		 * unrefreshed state.
		 * 
		 * @return True if refresh() was called since the last time this
		 *         function was invoked. Otherwise, false.
		 */
		public boolean wasRefreshed() {
			boolean temp = refreshed;
			refreshed = false;
			return temp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.javafx.mesh.datatypes.FXVertexView#
		 * refresh(org.eclipse.eavp.viz.service.modeling.AbstractMesh)
		 */
		@Override
		public void refresh(IMesh model) {
			refreshed = true;
		}

	}
}
