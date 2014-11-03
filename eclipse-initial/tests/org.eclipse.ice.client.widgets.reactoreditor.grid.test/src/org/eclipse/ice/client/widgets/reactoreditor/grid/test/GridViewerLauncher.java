/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.grid.test;

import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CellEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CircularGridEditPartFactory;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Grid;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditPartFactory;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;
import org.eclipse.ice.client.widgets.reactoreditor.grid.HexagonalGridEditPartFactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

/**
 * This class was used to test the GridEditor. It is not meant to be run as a
 * JUnit test. If you want to see how well the grid viewers work, set the bundle
 * to "Activate when one of its classes is loaded" in the MANIFEST.MF "Overview"
 * tab. It will then call the Activator class.
 * 
 * @author djg
 * 
 */
public class GridViewerLauncher extends Thread {
	private volatile boolean active = true;

	public void run() {
		// Create a display
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Grid Editor");

		// The lone ReactorEditor needs to fill this window.
		shell.setLayout(new FillLayout());

		// Create Viewers for three different views of LW/SFRs.
		createLWRCore(shell);
		createSFRCore(shell);
		createSFRAssembly(shell);

		// Pack and open everything
		shell.setSize(1200, 900);
		shell.open();

		// Wait until the shell is closed
		while (!shell.isDisposed() && active) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Dispose the display
		display.dispose();

		final BundleContext bundleContext = FrameworkUtil.getBundle(
				this.getClass()).getBundleContext();
		if (bundleContext != null) {
			try {
				bundleContext.getBundle(0).stop();
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}

		return;
	}

	/**
	 * Creates a GEF Viewer with the specified model and factory.
	 * 
	 * @param container
	 *            The Composite that will contain the viewer.
	 * @param model
	 *            The model for the viewer.
	 * @param factory
	 *            The factory used to create the viewer's EditParts.
	 */
	public static GraphicalViewer createViewer(Composite container, Grid model,
			EditPartFactory factory) {
		// Create the GraphicalViewer. This is fairly standard procedure for
		// GEF.
		// GraphicalViewer viewer = new ScrollingGraphicalViewer();
		// Use this to test re-sizing of window (this viewer does not create
		// scroll bars).
		GraphicalViewer viewer = new GraphicalViewerImpl();
		viewer.setRootEditPart(new SimpleRootEditPart());
		viewer.createControl(container);
		// Pass the custom EditPartFactory and the Grid model to the
		// GraphicalViewer.
		viewer.setEditPartFactory(factory);
		viewer.setContents(model);
		// Set the GraphicalViewer's EditDomain to a default.
		viewer.setEditDomain(new DefaultEditDomain(null));

		/* -- Set the viewer's current selection of cells. -- */
		// Here, we need to set the viewer's current selection of EditParts to
		// the currently-selected Cells in the Grid model. This allows us to
		// show the user the Cells that were previously selected. To do this, we
		// need to add all of the EditParts for selected Cells to the viewer's
		// StructuredSelection.

		// Create the object array needed to create the StructuredSelection.
		List<Object> selectionList = new ArrayList<Object>();

		// Add the EditParts for all selected Cells to the ArrayList.
		for (Cell cell : model.getCells()) {
			if (cell.getSelected()) {
				// Get the Cell's EditPart.
				CellEditPart editPart = (CellEditPart) viewer
						.getEditPartRegistry().get(cell);

				// Add the CellEditPart to the array.
				selectionList.add(editPart);
			}
		}

		// Create the StructuredSelection from the array and pass it to viewer.
		viewer.setSelection(new StructuredSelection(selectionList.toArray()));
		/* -------------------------------------------------- */

		return viewer;
	}

	/**
	 * Creates a GEF Viewer that shows an example LWR core.
	 * 
	 * @param container
	 *            The Composite that will contain the viewer.
	 */
	private void createLWRCore(Composite container) {

		// This will be a 15x15 core.
		int rows = 15;
		int columns = rows;
		int size = rows * columns;

		// Configure some invalid/disabled/selected cells.
		List<Cell.State> states = new ArrayList<Cell.State>(size);
		for (int i = 0; i < size; i++) {
			states.add(Cell.State.UNSELECTED);
		}

		// Select the middle cell.
		states.set(7 * columns + 7, Cell.State.SELECTED);

		// Some valid states, just for fun.
		// states.put( 17, Cell.State.DISABLED);
		states.set(18, Cell.State.UNSELECTED);

		selectQuadrant(rows, columns, 0);
		selectOctant(rows, columns, 4);

		/* ----- Get invalid cell indexes for a 15x15 reactor core. ----- */
		// FIXME - This can probably be done better.
		// Top-left corner.
		states.set(0, Cell.State.INVALID);
		states.set(1, Cell.State.INVALID);
		states.set(2, Cell.State.INVALID);
		states.set(3, Cell.State.INVALID);
		states.set(15, Cell.State.INVALID);
		states.set(30, Cell.State.INVALID);
		states.set(45, Cell.State.INVALID);
		states.set(16, Cell.State.INVALID);
		// Top-right corner.
		states.set(14, Cell.State.INVALID);
		states.set(13, Cell.State.INVALID);
		states.set(12, Cell.State.INVALID);
		states.set(11, Cell.State.INVALID);
		states.set(29, Cell.State.INVALID);
		states.set(44, Cell.State.INVALID);
		states.set(59, Cell.State.INVALID);
		states.set(28, Cell.State.INVALID);
		// Bottom-left corner.
		states.set(210, Cell.State.INVALID);
		states.set(211, Cell.State.INVALID);
		states.set(212, Cell.State.INVALID);
		states.set(213, Cell.State.INVALID);
		states.set(195, Cell.State.INVALID);
		states.set(180, Cell.State.INVALID);
		states.set(165, Cell.State.INVALID);
		states.set(196, Cell.State.INVALID);
		// Bottom-right corner.
		states.set(208, Cell.State.INVALID);
		states.set(179, Cell.State.INVALID);
		states.set(194, Cell.State.INVALID);
		states.set(209, Cell.State.INVALID);
		states.set(221, Cell.State.INVALID);
		states.set(222, Cell.State.INVALID);
		states.set(223, Cell.State.INVALID);
		states.set(224, Cell.State.INVALID);
		/* -------------------------------------------------------------- */

		// Make a new GridEditorInput for our model.
		GridEditorInput input = new GridEditorInput(rows, columns);

		// Set the states we just configured.
		input.setStates(states);

		// Create a new model from the input.
		Grid grid = new Grid(input);

		// Create the GEF Viewer.
		Composite rectangularGrid = new Composite(container, SWT.NONE);
		rectangularGrid.setLayout(new FillLayout());
		createViewer(rectangularGrid, grid, new GridEditPartFactory());
	}

	/**
	 * Creates a GEF Viewer that shows an example SFR core.
	 * 
	 * @param container
	 *            The Composite that will contain the viewer.
	 */
	private void createSFRCore(Composite container) {

		// This will be a core with radius 8.
		int radius = 8;
		int rows = 2 * radius - 1;
		int columns = rows;
		int size = rows * columns;

		// Configure some invalid/disabled/selected cells.
		List<Cell.State> states = new ArrayList<Cell.State>(size);
		for (int i = 0; i < size; i++) {
			states.add(Cell.State.INVALID);
		}

		/* ----- Set the enabled positions (all of the sextants. ----- */
		// Get all valid indexes (by fetching each index from each sextant).
		for (int i = 0; i < 6; i++) {
			for (Point cell : selectSextant(radius, i, false)) {
				states.set(cell.y * columns + cell.x, Cell.State.UNSELECTED);
			}
		}
		/* ----------------------------------------------------------- */

		/* ----- Select the top-right sextant. ----- */
		for (Point cell : selectSextant(radius, 0, false)) {
			states.set(cell.y * columns + cell.x, Cell.State.SELECTED);
		}
		/* ----------------------------------------- */

		// Make a new GridEditorInput for our model.
		GridEditorInput input = new GridEditorInput(rows, columns);

		// Set the states we just configured.
		input.setStates(states);

		// Create a new model from the input.
		Grid grid = new Grid(input);

		// Create the Composite that will contain the GEF Viewer.
		Composite circularGrid = new Composite(container, SWT.NONE);
		circularGrid.setBackground(ColorConstants.darkGreen);
		circularGrid.setLayout(new FillLayout());

		// Create the GEF Viewer.
		createViewer(circularGrid, grid, new HexagonalGridEditPartFactory());
	}

	/**
	 * Creates a GEF Viewer that shows an example SFR assembly.
	 * 
	 * @param container
	 *            The Composite that will contain the viewer.
	 */
	private void createSFRAssembly(Composite container) {

		// This will be a core with radius 8.
		int radius = 7;
		int rows = 2 * radius - 1;
		int columns = rows;
		int size = rows * columns;

		// Configure some invalid/disabled/selected cells.
		List<Cell.State> states = new ArrayList<Cell.State>(size);
		for (int i = 0; i < size; i++) {
			states.add(Cell.State.INVALID);
		}

		/* ----- Set the enabled positions (all of the sextants. ----- */
		// Get all valid indexes (by fetching each index from each sextant).
		for (int i = 0; i < 6; i++) {
			for (Point cell : selectSextant(radius, i, true)) {
				states.set(cell.y * columns + cell.x, Cell.State.UNSELECTED);
			}
		}
		/* ----------------------------------------------------------- */

		/* ----- Select the top-right sextant. ----- */
		for (Point cell : selectSextant(radius, 0, true)) {
			states.set(cell.y * columns + cell.x, Cell.State.SELECTED);
		}
		/* ----------------------------------------- */

		// Make a new GridEditorInput for our model.
		GridEditorInput input = new GridEditorInput(rows, columns);

		// Set the states we just configured.
		input.setStates(states);

		// Create a new model from the input.
		Grid grid = new Grid(input);

		// Create the Composite that will contain the GEF Viewer.
		Composite circularGrid = new Composite(container, SWT.NONE);
		circularGrid.setBackground(ColorConstants.darkGreen);
		circularGrid.setLayout(new FillLayout());

		// Create the GEF Viewer.
		createViewer(circularGrid, grid, new CircularGridEditPartFactory());
	}

	/**
	 * Selects a sextant of a hexagon-based grid.
	 * 
	 * @param radius
	 *            The radius (including the center) of the grid of hexagons.
	 * @param sextantIndex
	 *            The index of the sextant we would like to examine.
	 * @param rotated
	 *            Whether or not the hexagon-based grid is oriented such that
	 *            the flat sides of the hexagons are vertical.
	 * @return An ArrayList of Points defining the indexes of the hexagonal
	 *         cells in the sextant.
	 */
	public static List<Point> selectSextant(int radius, int sextantIndex,
			boolean rotated) {
		// It is important to note that a grid of hexagons with any such
		// "radius" forms a lattice in the shape of a hexagon.

		/* ------------------------------------------------------------------ */
		// Determine the (x,y) coordinates of hexagons within the sextant.

		// XXX An explanation as to how this code works follows:
		// The hexagonal lattice with a certain radius actually forms rings of
		// concentric hexagons. To compute the hexagons in a sextant, I loop
		// over the sextant's portion of each ring from the inside out.
		// The center of each hexagon on the ring forms a triangle with the
		// sextant's first hexagon in the ring and the origin. I use the law
		// of cosines to determine the distance of the center of the hexagon
		// from the origin and to determine the angle it forms with the origin.
		// Then, I can convert the real "radius" of the hexagon and its angle
		// theta (polar) into a real x and y (Cartesian). From these real x and
		// y, I can deduce the relative x and y that the hexagon occupies within
		// the rectangular grid of hexagons.

		// A list of (x,y) indexes for hexagons in the interesting sextant.
		List<Point> sextant = new ArrayList<Point>();

		if (!rotated) {
			// Determine the start and end angles for the selected sextant.
			double start = Math.PI * ((2 * sextantIndex - 1) / 6.0);
			// double end = Math.PI * ((2 * sextantIndex + 1) / 6.0);

			// maxX is a factor used to determine the x index of hexagons later.
			// This is the value of cos(PI/6 = 30 degrees).
			double maxX = Math.sqrt(3) / 2.0;

			// Pre-compute this value.
			double cosPiDivThree = Math.cos(Math.PI / 3);

			// The center hex is always in a sextant. Add (0,0) to the List.
			sextant.add(new Point(radius - 1, radius - 1));

			// For the sextant, loop through all inner hexagon rings.
			for (int r = 1; r < radius; r++) {
				// Compute the square of the current radius.
				double r2 = r * r;

				// For the hexagon ring, loop through all hexagons between the
				// sextant's start and end angles.
				for (int i = 0; i <= r; i++) {
					// Compute the square of the segment between the start of
					// the
					// sextant and the center of the hexagon.
					double i2 = i * i;

					// Use the law of cosines to determine the distance of the
					// hexagon's center from the origin.
					double R2 = (double) i2 + (double) r2 - 2 * i * r
							* cosPiDivThree;
					double R = Math.sqrt(R2);

					// Use the law of cosines to determine the angle theta for
					// the
					// hexagon's center. We have to add on the starting angle
					// for
					// this sextant.
					double theta = start
							+ Math.acos((R2 + r2 - i2) / (2.0 * R * r));

					// Compute the cartesian coordinates for the center of the
					// hexagon. (The distance left and up, respectively, of the
					// center.)
					double x = R * Math.cos(theta);
					double y = R * Math.sin(theta);

					// Compute the x index (column) for the hex cell from x, the
					// relative x position.
					int xIndex = (int) Math.round(x / maxX) + radius - 1;

					// Compute the y index (row) for the hex cell from y, the
					// relative y position. If both the column and the radius
					// are odd, we need to shift the y value up by 0.5 units. If
					// both the column and radius are even, we need to
					// shift the y value down by 0.5 units. Otherwise, don't
					// shift the y value.
					int yIndex = radius
							- 1
							- (int) Math.round(y + (xIndex % 2 * 0.5)
									- ((radius + 1) % 2 * 0.5));

					// Add the computed x and y indexes to the list of Points.
					sextant.add(new Point(xIndex, yIndex));
				}
			}
		} else { // All hexagons are rotated 90 degrees.
			// Determine the start and end angles for the selected sextant.
			double start = Math.PI * (sextantIndex / 3.0);
			// double end = Math.PI * ((sextantIndex + 1) / 3.0);

			// maxX is a factor used to determine the x index of hexagons later.
			// This is the value of cos(PI/6 = 30 degrees).
			double maxY = Math.sqrt(3) / 2.0;

			// Pre-compute this value.
			double cosPiDivThree = Math.cos(Math.PI / 3);

			// The center hex is always in a sextant. Add (0,0) to the List.
			sextant.add(new Point(radius - 1, radius - 1));

			// For the sextant, loop through all inner hexagon rings.
			for (int r = 1; r < radius; r++) {
				// Compute the square of the current radius.
				double r2 = r * r;

				// For the hexagon ring, loop through all hexagons between the
				// sextant's start and end angles.
				for (int i = 0; i <= r; i++) {
					// Compute the square of the segment between the start of
					// the
					// sextant and the center of the hexagon.
					double i2 = i * i;

					// Use the law of cosines to determine the distance of the
					// hexagon's center from the origin.
					double R2 = (double) i2 + (double) r2 - 2 * i * r
							* cosPiDivThree;
					double R = Math.sqrt(R2);

					// Use the law of cosines to determine the angle theta for
					// the
					// hexagon's center. We have to add on the starting angle
					// for
					// this sextant.
					double theta = start
							+ Math.acos((R2 + r2 - i2) / (2.0 * R * r));

					// Compute the cartesian coordinates for the center of the
					// hexagon. (The distance left and up, respectively, of the
					// center.)
					double x = R * Math.cos(theta);
					double y = R * Math.sin(theta);

					// Compute the y index (row) for the hex cell from y, the
					// relative y position.
					int yIndex = radius - 1 - (int) Math.round(y / maxY);

					// Compute the x index (column) for the hex cell from x, the
					// relative x position. If both the row and the radius
					// are odd, we need to shift the x value left by 0.5 units.
					// If
					// both the row and radius are even, we need to
					// shift the x value right by 0.5 units. Otherwise, don't
					// shift the x value.
					int xIndex = radius
							- 1
							+ (int) Math.round(x - (yIndex % 2 * 0.5)
									+ ((radius + 1) % 2 * 0.5));

					// Add the computed x and y indexes to the list of Points.
					sextant.add(new Point(xIndex, yIndex));
				}
			}
		}
		/* ------------------------------------------------------------------ */

		return sextant;
	}

	public static List<Point> selectOctant(int rows, int columns,
			int octantIndex) {
		List<Point> octant = new ArrayList<Point>((int) Math.ceil(rows
				* columns / 8.0));

		return octant;
	}

	public static List<Point> selectQuadrant(int rows, int columns,
			int quadrantIndex) {
		List<Point> quadrant = new ArrayList<Point>((int) Math.ceil(rows
				* columns / 4.0));

		for (int x = 0; x < columns / 2; x++) {
			for (int y = 0; y < rows / 2; y++) {

			}
		}

		return quadrant;
	}

	public void stopThread() {
		active = false;
	}
}
