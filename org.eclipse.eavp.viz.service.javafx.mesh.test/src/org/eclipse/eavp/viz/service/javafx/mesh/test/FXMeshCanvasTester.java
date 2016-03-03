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

import org.eclipse.eavp.viz.service.javafx.canvas.BasicViewer;
import org.eclipse.eavp.viz.service.javafx.mesh.FXMeshCanvas;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
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
				new BasicController(new BasicMesh(), new BasicView()));

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
		IController root = new BasicController(new BasicMesh(),
				new BasicView());

		// The canvas for testing
		FXMeshCanvas canvas = new TestCanvas(
				new BasicController(new BasicMesh(), new BasicView()));

		// Create the polygons for the test
		BasicController neighbor1 = new BasicController(
				new BasicMesh(), new BasicView());
		neighbor1.setProperty(MeshProperty.NAME, "neighbor1");
		BasicController neighbor2 = new BasicController(
				new BasicMesh(), new BasicView());
		neighbor2.setProperty(MeshProperty.NAME, "neighbor2");
		BasicController independent = new BasicController(
				new BasicMesh(), new BasicView());
		independent.setProperty(MeshProperty.NAME, "independent");

		// Create the points used for the test. These points will be used by
		// exactly one polygon apiece
		BasicController point1 = new BasicController(new BasicMesh(),
				new BasicView());
		point1.setProperty(MeshProperty.NAME, "point1");
		BasicController point2 = new BasicController(new BasicMesh(),
				new BasicView());
		point2.setProperty(MeshProperty.NAME, "point2");
		BasicController point3 = new BasicController(new BasicMesh(),
				new BasicView());
		point3.setProperty(MeshProperty.NAME, "point3");
		BasicController point4 = new BasicController(new BasicMesh(),
				new BasicView());
		point4.setProperty(MeshProperty.NAME, "point4");
		BasicController point5 = new BasicController(new BasicMesh(),
				new BasicView());
		point5.setProperty(MeshProperty.NAME, "point5");
		BasicController point6 = new BasicController(new BasicMesh(),
				new BasicView());
		point6.setProperty(MeshProperty.NAME, "point6");

		// Create more points. These will be shared by the two triangles
		BasicController shared1 = new BasicController(new BasicMesh(),
				new BasicView());
		shared1.setProperty(MeshProperty.NAME, "shared1");
		BasicController shared2 = new BasicController(new BasicMesh(),
				new BasicView());
		shared2.setProperty(MeshProperty.NAME, "shared2");

		// Add the vertices to the polygons. The polygons should be two
		// triangles which share an edge and a rectangle that is not connected
		// to either of them.
		neighbor1.addEntityToCategory(point1, MeshCategory.VERTICES);
		neighbor1.addEntityToCategory(shared1, MeshCategory.VERTICES);
		neighbor1.addEntityToCategory(shared2, MeshCategory.VERTICES);

		neighbor2.addEntityToCategory(point2, MeshCategory.VERTICES);
		neighbor2.addEntityToCategory(shared1, MeshCategory.VERTICES);
		neighbor2.addEntityToCategory(shared2, MeshCategory.VERTICES);

		independent.addEntityToCategory(point3, MeshCategory.VERTICES);
		independent.addEntityToCategory(point4, MeshCategory.VERTICES);
		independent.addEntityToCategory(point5, MeshCategory.VERTICES);
		independent.addEntityToCategory(point6, MeshCategory.VERTICES);

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
		assertFalse("True".equals(point1.getProperty(MeshProperty.SELECTED)));
		assertTrue("True".equals(point3.getProperty(MeshProperty.SELECTED)));
		assertTrue("True".equals(point4.getProperty(MeshProperty.SELECTED)));
		assertTrue("True".equals(point5.getProperty(MeshProperty.SELECTED)));
		assertTrue("True".equals(point6.getProperty(MeshProperty.SELECTED)));

		// Create a selection for the first triangle
		Object[] selectionShared1 = new Object[3];
		selectionShared1[0] = point1;
		selectionShared1[1] = shared1;
		selectionShared1[2] = shared2;

		// Select the triangle. The three vertices of the triangle should be
		// selected, while the vertices of the rectangle should be deselected.
		canvas.setSelection(selectionShared1);
		assertTrue("True".equals(point1.getProperty(MeshProperty.SELECTED)));
		assertTrue("True".equals(shared1.getProperty(MeshProperty.SELECTED)));
		assertTrue("True".equals(shared2.getProperty(MeshProperty.SELECTED)));
		assertFalse("True".equals(point3.getProperty(MeshProperty.SELECTED)));
		assertFalse("True".equals(point4.getProperty(MeshProperty.SELECTED)));
		assertFalse("True".equals(point5.getProperty(MeshProperty.SELECTED)));
		assertFalse("True".equals(point6.getProperty(MeshProperty.SELECTED)));

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
		public TestCanvas(BasicController mesh) {
			super(mesh);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.javafx.mesh.FXMeshCanvas#
		 * materializeViewer(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected BasicViewer materializeViewer(Composite viewerParent)
				throws Exception {
			try {
				return new TestViewer(viewerParent);

			} catch (Exception e) {
				throw new Exception("", e); //$NON-NLS-1$
			}
		}
	}

}
