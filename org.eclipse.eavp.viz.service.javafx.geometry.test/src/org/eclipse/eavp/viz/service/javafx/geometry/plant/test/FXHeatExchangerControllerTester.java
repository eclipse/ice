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
package org.eclipse.eavp.viz.service.javafx.geometry.plant.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerView;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.junit.Test;

/**
 * A class for testing the functionality of the FXHeatExchangerController
 * 
 * @author Robert Smith
 *
 */
public class FXHeatExchangerControllerTester {

	/**
	 * Test that the FXHeatExchangerController is cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Create a cloned FXHeatExchangerController and check that it is
		// identical to the
		// original
		HeatExchangerMesh mesh = new HeatExchangerMesh();
		FXHeatExchangerController exchanger = new FXHeatExchangerController(
				mesh, new FXHeatExchangerView(mesh));
		exchanger.setProperty("Test", "Property");
		FXHeatExchangerController clone = (FXHeatExchangerController) exchanger
				.clone();
		assertTrue(exchanger.equals(clone));
	}

	/**
	 * Check that the exchanger will ignore Wireframe type messages except when
	 * they are sent by the controller's view.
	 */
	@Test
	public void checkUpdate() {

		// Create an exchanger
		HeatExchangerMesh mesh = new HeatExchangerMesh();
		TestHeatExchangerView view = new TestHeatExchangerView(mesh);
		FXHeatExchangerController exchanger = new FXHeatExchangerController(
				mesh, view);

		// Reset the view's refreshed state
		view.wasRefreshed();

		// Set the view to wireframe mode
		view.setWireFrameMode(true);

		// This should have signaled the controller to perform a refresh
		assertTrue(view.wasRefreshed());

		// Create a second exchanger
		HeatExchangerMesh mesh2 = new HeatExchangerMesh();
		TestHeatExchangerView view2 = new TestHeatExchangerView(mesh2);
		FXHeatExchangerController exchanger2 = new FXHeatExchangerController(
				mesh2, view2);

		// Add it as a child to the current one
		exchanger.addEntity(exchanger2);

		// Reset the view's state
		view.wasRefreshed();

		// Set the child's view to wireframe mode
		view2.setWireFrameMode(true);

		// Since the wireframe message was not received from the controller's
		// view, it should not cause the controller to refresh the view.
		assertFalse(view.wasRefreshed());

	}

	private class TestHeatExchangerView extends FXHeatExchangerView {

		/**
		 * Whether the view has been refreshed since the last time it was
		 * checked.
		 */
		boolean refreshed = false;

		/**
		 * The default constructor.
		 * 
		 * @param model
		 *            The internal model to be dispalyed.
		 */
		public TestHeatExchangerView(AbstractMesh model) {
			super(model);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.javafx.geometry.plant.
		 * FXHeatExchangerView
		 * #refresh(org.eclipse.eavp.viz.service.modeling.AbstractMesh)
		 */
		@Override
		public void refresh(AbstractMesh model) {
			super.refresh(model);
			refreshed = true;
		}

		/**
		 * Determines if the view has been refreshed since the last time it was
		 * checked and returns it to its initial, un-refreshed state.
		 * 
		 * @return True if the view has been refreshed since the last time
		 *         wasRefreshed() was invoked. False otherwise.
		 */
		public boolean wasRefreshed() {
			boolean temp = refreshed;
			refreshed = false;
			return temp;
		}
	}
}
