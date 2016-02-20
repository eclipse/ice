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
package org.eclipse.eavp.viz.service.javafx.mesh.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.javafx.canvas.AbstractViewer;
import org.eclipse.eavp.viz.service.javafx.mesh.FXMeshCanvas;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import javafx.scene.shape.Box;

/**
 * A class to test the functionality of the FXMeshCanvas
 * 
 * @author Robert Smith
 *
 */
public class FXMeshCanvasTester {

	/**
	 * Check that the canvas's axes can be turned on and off.
	 */
	@Test
	public void checkAxes() {

		// The canvas for testing
		FXMeshCanvas canvas = new TestCanvas(
				new AbstractController(new AbstractMesh(), new AbstractView()));

		// Initialize the canvas
		try {
			canvas.draw(
					new Composite(new Shell(Display.getDefault()), SWT.NONE));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// check that the viewer's axes are visible by default
		ArrayList<Box> axes = ((TestGizmo) ((TestViewer) canvas.getViewer())
				.getGizmo()).getAxes();
		for (Box axis : axes) {
			assertTrue(axis.isVisible());
		}

		// Set the canvas's axes off and check that they are invisible
		canvas.setVisibleAxis(false);
		for (Box axis : axes) {
			assertFalse(axis.isVisible());
		}

		// Set the canvas's axes on and check that they are visible
		canvas.setVisibleAxis(true);
		for (Box axis : axes) {
			assertTrue(axis.isVisible());
		}
	}

	/**
	 * Check the canvas's ability to set and delete its selection.
	 */
	public void checkSelection() {

		// The root for the tree of shapes in the canvas
		AbstractController root = new AbstractController(new AbstractMesh(),
				new AbstractView());

		// The canvas for testing
		FXMeshCanvas canvas = new TestCanvas(
				new AbstractController(new AbstractMesh(), new AbstractView()));

		// Create the polygons for the test
		AbstractController neighbor1 = new AbstractController(
				new AbstractMesh(), new AbstractView());
		neighbor1.setProperty("Name", "neighbor1");
		AbstractController neighbor2 = new AbstractController(
				new AbstractMesh(), new AbstractView());
		neighbor2.setProperty("Name", "neighbor2");
		AbstractController independent = new AbstractController(
				new AbstractMesh(), new AbstractView());
		independent.setProperty("Name", "independent");

		// Create the points used for the test. These points will be used by
		// exactly one polygon apiece
		AbstractController point1 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		point1.setProperty("Name", "point1");
		AbstractController point2 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		point2.setProperty("Name", "point2");
		AbstractController point3 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		point3.setProperty("Name", "point3");
		AbstractController point4 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		point4.setProperty("Name", "point4");
		AbstractController point5 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		point5.setProperty("Name", "point5");
		AbstractController point6 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		point6.setProperty("Name", "point6");

		// Create more points. These will be shared by the two triangles
		AbstractController shared1 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		shared1.setProperty("Name", "shared1");
		AbstractController shared2 = new AbstractController(new AbstractMesh(),
				new AbstractView());
		shared2.setProperty("Name", "shared2");

		// Add the vertices to the polygons. The polygons should be two
		// triangles which share an edge and a rectangle that is not connected
		// to either of them.
		neighbor1.addEntityByCategory(point1, "Vertices");
		neighbor1.addEntityByCategory(shared1, "Vertices");
		neighbor1.addEntityByCategory(shared2, "Vertices");

		neighbor2.addEntityByCategory(point2, "Vertices");
		neighbor2.addEntityByCategory(shared1, "Vertices");
		neighbor2.addEntityByCategory(shared2, "Vertices");

		independent.addEntityByCategory(point3, "Vertices");
		independent.addEntityByCategory(point4, "Vertices");
		independent.addEntityByCategory(point5, "Vertices");
		independent.addEntityByCategory(point6, "Vertices");

		// Try deleting with nothing selected and no polygons. Nothing should
		// happen.
		canvas.deleteSelection();

		// Add the polygons to the canvas
		root.addEntity(neighbor1);
		root.addEntity(neighbor2);
		root.addEntity(independent);

		// Delete with nothing selected. Nothing should be deleted.
		canvas.deleteSelection();
		assertTrue(root.getEntities().contains(neighbor1));
		assertTrue(root.getEntities().contains(neighbor2));
		assertTrue(root.getEntities().contains(independent));

		// Create a selection for the rectangle
		Object[] selectionIndependent = new Object[4];
		selectionIndependent[0] = point3;
		selectionIndependent[1] = point4;
		selectionIndependent[2] = point5;
		selectionIndependent[3] = point6;

		// Select the rectangle
		canvas.setSelection(selectionIndependent);

		// The four points of the rectangle should be selected. Other points
		// should not be
		assertFalse("True".equals(point1.getProperty("Selected")));
		assertTrue("True".equals(point3.getProperty("Selected")));
		assertTrue("True".equals(point4.getProperty("Selected")));
		assertTrue("True".equals(point5.getProperty("Selected")));
		assertTrue("True".equals(point6.getProperty("Selected")));

		// Create a selection for the first triangle
		Object[] selectionShared1 = new Object[3];
		selectionShared1[0] = point1;
		selectionShared1[1] = shared1;
		selectionShared1[2] = shared2;

		// Select the triangle. The three vertices of the triangle should be
		// selected, while the vertices of the rectangle should be deselected.
		canvas.setSelection(selectionShared1);
		assertTrue("True".equals(point1.getProperty("Selected")));
		assertTrue("True".equals(shared1.getProperty("Selected")));
		assertTrue("True".equals(shared2.getProperty("Selected")));
		assertFalse("True".equals(point3.getProperty("Selected")));
		assertFalse("True".equals(point4.getProperty("Selected")));
		assertFalse("True".equals(point5.getProperty("Selected")));
		assertFalse("True".equals(point6.getProperty("Selected")));

		// Delete the first triangle and check that it, and nothing else, is
		// gone
		canvas.deleteSelection();
		assertFalse(root.getEntities().contains(neighbor1));
		assertTrue(root.getEntities().contains(neighbor2));
		assertTrue(root.getEntities().contains(independent));

		// Delete the triangle again. Nothing should change.
		canvas.deleteSelection();
		assertFalse(root.getEntities().contains(neighbor1));
		assertTrue(root.getEntities().contains(neighbor2));
		assertTrue(root.getEntities().contains(independent));

		// Delete the rectangle and check that only the second triangle remains.
		canvas.setSelection(selectionIndependent);
		canvas.deleteSelection();
		assertFalse(root.getEntities().contains(neighbor1));
		assertTrue(root.getEntities().contains(neighbor2));
		assertFalse(root.getEntities().contains(independent));

	}

	/**
	 * An extension of FXMeshCanvas that replaces the FXMeshViewer with a
	 * TestViewer.
	 * 
	 * @author Robert Smith
	 *
	 */
	public class TestCanvas extends FXMeshCanvas {

		/**
		 * The default constructor.
		 * 
		 * @param mesh
		 *            The root node that will contain all the modeling parts to
		 *            be rendered.
		 */
		public TestCanvas(AbstractController mesh) {
			super(mesh);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.javafx.mesh.FXMeshCanvas#
		 * materializeViewer(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected AbstractViewer materializeViewer(Composite viewerParent)
				throws Exception {
			try {
				return new TestViewer(viewerParent);

			} catch (Exception e) {
				throw new Exception("", e); //$NON-NLS-1$
			}
		}
	}

}
