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
package org.eclipse.ice.client.widgets.grid;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

public class GridEditorTools {

	/**
	 * Creates a basic GEF viewer in the specified container and with the
	 * specified EditPartFactory.
	 * 
	 * @param container
	 *            The Composite that will contain the viewer.
	 * @param factory
	 *            The factory used to create the viewer's EditParts.
	 */
	public static GraphicalViewer createViewer(Composite container,
			EditPartFactory factory) {
		// Create the GraphicalViewer. This is fairly standard procedure for
		// GEF.

		// Use this to test re-sizing of window (this viewer does not create
		// scroll bars).
		GraphicalViewer viewer = new GraphicalViewerImpl();
		viewer.setRootEditPart(new SimpleRootEditPart());
		viewer.createControl(container);

		// Pass the custom EditPartFactory and the Grid model to the
		// GraphicalViewer.
		viewer.setEditPartFactory(factory);

		// Set the GraphicalViewer's EditDomain to a default.
		viewer.setEditDomain(new DefaultEditDomain(null));

		return viewer;
	}

	/**
	 * Updates the GEF viewer's contents based on the model.
	 * 
	 * @param viewer
	 *            The GraphicalViewer to update.
	 * @param model
	 *            The model to use for the Graphical Viewer.
	 */
	public static void setViewerContents(GraphicalViewer viewer, Grid model) {

		// Sets the actual contents of the viewer as described in the GEF API.
		viewer.setContents(model);

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

		return;
	}

	/**
	 * Given a square grid of a certain size and an octant index, this method
	 * computes a list of (x, y) -> (column, row) values for all positions in
	 * the grid in the octant. The column coordinates increase from left to
	 * right, and the row coordinates increase from top to bottom.
	 * 
	 * @param size
	 *            The size of the grid (length and width are the same).
	 * @param octantIndex
	 *            The index of the octant. 0 starts in the top-right octant, and
	 *            the indices circle counter-clockwise to the last octant in the
	 *            bottom right just below the x-axis.
	 * @return A List of Points containing the (column, row) coordinates for all
	 *          grid locations in the quadrant.
	 */
	public static List<Point> getOctant(int size, int octantIndex) {

		// A List of points to contain the 2D-array-friendly (column, row)
		// coordinates for cells in the octant.
		List<Point> octant = new ArrayList<Point>();

		// Determine the "radius" or half the width of the quadrant.
		int radius = (int) Math.ceil(size / 2.0);

		// Determine theta from the octant index.
		double theta = (Math.PI * octantIndex + Math.PI) / 4.0;

		// Default start and end values for x and y (when they are positive).
		int xStart = 0, xEnd = radius - 1;
		int yStart = 0, yEnd = radius - 1;

		// Determine the order of the octant within its quadrant. For instance,
		// octants 0 and 1 correspond to 0 and 1, 4 and 5 correspond to 0 and 1,
		// and so on.
		boolean xFromY = (octantIndex % 2 == 0);

		// f is the factor determining if the subdivision of the quadrant is
		// based on y=x or y=-x. Of course, if x and y share the same sign,
		// the two octants in the quadrant use y=x. Otherwise, they use y=-x.
		int f = 1;

		// If x is negative, we loop over x from -radius to 0.
		if (Math.cos(theta) < 0.0) {
			xStart = 1 - radius;
			xEnd = 0;
			f *= -1;
		}

		// If y is positive, just loop over the x values from start to finish.
		if (Math.sin(theta) > 0.0) {
			for (int y = yStart; y <= yEnd; y++) {
				// Because we are dealing with octants, we must set whether the
				// x loops from y to the end or from the start to y.
				if (xFromY) {
					xStart = y * f;
				} else {
					xEnd = y * f;
				}
				for (int x = xStart; x <= xEnd; x++) {
					octant.add(new Point(x + radius - 1, radius - y - 1));
				}
			}
		}
		// If y is negative, switch the order for determining start and end from
		// the boolean xFromY.
		else {
			// If y is negative, we loop over
			yStart = 1 - radius;
			yEnd = 0;
			f *= -1;

			for (int y = yStart; y <= yEnd; y++) {
				// Because we are dealing with octants, we must set whether the
				// x loops from y to the end or from the start to y. This is
				// reversed from the above condition for positive y's.
				if (xFromY) {
					xEnd = y * f;
				} else {
					xStart = y * f;
				}
				for (int x = xStart; x <= xEnd; x++) {
					octant.add(new Point(x + radius - 1, radius - y - 1));
				}
			}
		}

		return octant;
	}

	/**
	 * Given a square grid of a certain size and a quadrant index, this method
	 * computes a list of (x, y) -> (column, row) values for all positions in
	 * the grid in the quadrant. The column coordinates increase from left to
	 * right, and the row coordinates increase from top to bottom.
	 * 
	 * @param size
	 *            The size of the grid (length and width are the same).
	 * @param quadrantIndex
	 *            The index of the octant. 0 starts in the top-right quadrant,
	 *            and the indices circle counter-clockwise to the last quadrant
	 *            in the bottom right just below the x-axis.
	 * @return A List of Points containing the (column, row) coordinates for all
	 *         grid locations in the quadrant.
	 */
	public static List<Point> getQuadrant(int size, int quadrantIndex) {
		// For the computation, we consider the grid to have a center at
		// (radius, radius). We determine the sign of x and y depending on the
		// angle theta corresponding to the quadrant index, and then we loop
		// over x and y *increasing* using different values if x and/or y are
		// negative. For each (x, y) coordinate computed, we translate it back
		// to (0, 0) in the top-left corner with y increasing downward.

		// A List of points to contain the 2D-array-friendly (column, row)
		// coordinates for cells in the quadrant.
		List<Point> quadrant = new ArrayList<Point>();

		// Determine the "radius" or half the width of the quadrant.
		int radius = (int) Math.ceil(size / 2.0);

		// Determine theta from the quadrant index.
		double theta = (Math.PI * 2 * quadrantIndex + Math.PI) / 4.0;

		// Default start and end values for x and y. So we don't have to change
		// the structure of our loops, x and y should always increase!
		int xStart = 0, xEnd = radius - 1;
		int yStart = 0, yEnd = radius - 1;

		// See if the quadrant corresponds to negative x values.
		if (Math.cos(theta) < 0.0) {
			xStart = 1 - radius;
			xEnd = 0;
		}
		// See if the quadrant corresponds to negative y values.
		if (Math.sin(theta) < 0.0) {
			yStart = 1 - radius;
			yEnd = 0;
		}

		// Loop over the x and y values in the quadrant. For each one, determine
		// its (column, row) coordinates.
		for (int y = yStart; y <= yEnd; y++) {
			for (int x = xStart; x <= xEnd; x++) {
				quadrant.add(new Point(x + radius - 1, radius - y - 1));
			}
		}

		return quadrant;
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
	public static List<Point> getSextant(int radius, int sextantIndex,
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
					double R2 = i2 + r2 - 2 * i * r
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
					double R2 = i2 + r2 - 2 * i * r
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

}
