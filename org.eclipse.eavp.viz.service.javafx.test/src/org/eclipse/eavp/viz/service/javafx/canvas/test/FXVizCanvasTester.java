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
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.eavp.viz.service.javafx.canvas.FXSelection;
import org.eclipse.eavp.viz.service.javafx.canvas.FXViewer;
import org.eclipse.eavp.viz.service.javafx.canvas.FXVizCanvas;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

/**
 * A class to test the functionality of the FXVizCanvas
 * 
 * @author Robert Smith
 *
 */
public class FXVizCanvasTester {

	/**
	 * Test that the canvas is drawn correctly.
	 */
	@Test
	public void checkDraw() {

		BasicController root = new BasicController(new BasicMesh(),
				new BasicView());
		TestVizFXCanvas canvas = new TestVizFXCanvas(root);

		try {
			canvas.draw(new Composite(new Shell(new Display()), SWT.NONE));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Check that the canvas's viewer is the correct type
		assertTrue(canvas.getViewer() instanceof FXViewer);

		// Change the viewer's selection and check that an update was received
		canvas.getViewer().setSelection(new FXSelection(root));
		assertTrue(canvas.updated);
	}

	/**
	 * An extension of FXCanvas that keeps track of when its viewer's selection
	 * has been changed.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestVizFXCanvas extends FXVizCanvas {

		public boolean updated = false;

		/**
		 * The default constructor.
		 * 
		 * @param geometry
		 *            The root node.
		 */
		public TestVizFXCanvas(BasicController geometry) {
			super(geometry);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.javafx.canvas.FXVizCanvas#
		 * handleSelectionChanged(org.eclipse.jface.viewers.
		 * SelectionChangedEvent)
		 */
		@Override
		protected void handleSelectionChanged(SelectionChangedEvent event) {
			updated = true;
		}

	}
}
