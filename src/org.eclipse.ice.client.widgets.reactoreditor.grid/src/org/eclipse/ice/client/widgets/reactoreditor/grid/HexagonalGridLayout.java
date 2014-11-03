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
package org.eclipse.ice.client.widgets.reactoreditor.grid;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class extends the GridLayout to an evenly-sized, hexagon-based grid
 * layout. This layout computes the largest possible hexagon for each cell and
 * sets the bounds of each IFigure based on this hexagon.<br>
 * <br>
 * The hexagons comprising this layout can be oriented with the flat sides up or
 * with the point up. To change the orientation, use the method
 * {@link #setRotated(boolean)}.<br>
 * <br>
 * If you want the child IFigures affected by this layout to be hexagons, they
 * need to be PolygonShapes with a PointList of size 6. When initializing the
 * child hexagon, call <code>setPoints()</code> with the PointList returned by
 * this LayoutManager's {@link #getPoints()} method. This LayoutManager does not
 * call <code>setPoints()</code>, a method that causes an invalidation, on the
 * child IFigures. Instead, the LayoutManager directly modifies the PointList.
 * 
 * @author djg
 * 
 */
public class HexagonalGridLayout extends GridLayout {

	/**
	 * The set of points used by each hexagon in this layout.
	 */
	private final PointList points;

	/**
	 * Whether or not the layout is rotated 90 degrees. The default value is
	 * false. If not rotated, the flat sides of each hexagonal location are on
	 * top and bottom. If rotated, the flat sides are on the left and right.
	 */
	private boolean rotated = false;

	/**
	 * The default constructor. Generates a 1x1 HexagonalGridLayout.
	 */
	public HexagonalGridLayout() {
		super();

		// This constructor creates an underlying array of 0s for 6 points.
		points = new PointList(new int[12]);
	}

	/**
	 * A non-default constructor. This generates a rows by columns
	 * HexagonalGridLayout. If rows or columns are less than 1, they are set to
	 * 1.
	 * 
	 * @param rows
	 *            The number of rows in the layout.
	 * @param columns
	 *            The number of columns in the layout.
	 */
	public HexagonalGridLayout(int rows, int columns) {
		super(rows, columns);

		// This constructor creates an underlying array of 0s for 6 points.
		points = new PointList(new int[12]);
	}

	/**
	 * Gets the PointList that should be used by child hexagon Figures
	 * (PolygonShapes). Be sure to call the hexagon's
	 * <code>setPoints(PointList)</code> with the value returned by this method.
	 * 
	 * @return A PointList of 6 points.
	 */
	public PointList getPoints() {
		return points;
	}

	/**
	 * Sets whether or not the layout is rotated 90 degrees. The default value
	 * is false. If not rotated, the flat sides of each hexagonal location are
	 * on top and bottom. If rotated, the flat sides are on the left and right.
	 * 
	 * @param rotated
	 *            Whether or not to rotate the layout.
	 */
	public void setRotated(boolean rotated) {
		this.rotated = rotated;
	}

	/**
	 * Applies a hexagon-based layout to the container.
	 */
	@Override
	public void layout(IFigure container) {

		// Get the maximum bounding box we can use for layout out sub-figures.
		Rectangle limit = container.getClientArea();

		// Math.cos() may vary across platforms, so pre-compute it.
		double cosPiDivSix = Math.sqrt(3) / 2.0;

		if (!rotated) {

			// Get the hexagon side lengths supported by the current
			// width/height of
			// the client area.
			double lx = 2.0
					* (double) (limit.width - horizontalSpacing * (columns - 1))
					/ (double) (3 * columns + 1);
			double ly = (double) (limit.height - verticalSpacing * (rows - 1))
					/ (cosPiDivSix * (double) (rows * 2 + 1));

			// Determine which length to use and flag the direction that needs
			// to be
			// padded (x or y).
			double l;
			int paddingX = 0, paddingY = 0;
			if (ly >= lx) { // Size restricted by width.
				l = lx;
			} else { // Size restricted by height.
				l = ly;
				paddingX++;
			}

			// Compute the half-height and half-width of each hexagon.
			lx = l / 2; // shortcut: sin(PI/6) = 1/2!
			ly = l * cosPiDivSix;

			// Compute the necessary padding for whichever dimension needs it.
			if (paddingX != 0) { // Size restricted by height.
				paddingX = (int) (((double) limit.width - horizontalSpacing
						* (rows - 1) - (lx * (double) (3 * columns + 1))) / 2.0);
			} else { // Size restricted by width.
				paddingY = (int) (((double) limit.height - verticalSpacing
						* (rows - 1) - (ly * (double) (2 * rows + 1))) / 2.0);
			}

			// Update the PointList used by each hexagon.
			// Note: The hexagons store references to the PointList, so we do
			// not need to call hexagon.setPoints(), which, in fact,
			// refreshes/repaints the Shape, in the quadratic loop below.
			points.setPoint(new Point((int) (lx), 0), 0);
			points.setPoint(new Point((int) (lx + l), 0), 1);
			points.setPoint(new Point((int) (l + l), (int) ly), 2);
			points.setPoint(new Point((int) (lx + l), (int) (ly + ly)), 3);
			points.setPoint(new Point((int) (lx), (int) (ly + ly)), 4);
			points.setPoint(new Point(0, (int) ly), 5);

			// We want to limit math ops, so compute the factors used in the
			// loop.
			double xFactor = l + lx + (double) horizontalSpacing;
			double yFactor = 2 * ly + (double) verticalSpacing;

			// Compute the width and height of each hexagon.
			int width = (int) Math.ceil(2 * l);
			int height = (int) Math.ceil(2 * ly);

			// Variables used throughout the below loop.
			int i, row, column, x, y, w, h;

			// Loop over the IFigures in the container with this layout.
			for (Object childObject : container.getChildren()) {
				IFigure child = (IFigure) childObject;

				// Get the constraints (and the x, y, w, h offsets from it).
				GridData constraint = getConstraint(child);
				Rectangle offsets = constraint.getOffsets();

				// Get the index and compute the row and column for the index.
				i = constraint.getIndex();
				row = i / columns;
				column = i % columns;

				// Compute the bounds of the cell in the row, column position.
				x = paddingX + (int) (column * xFactor) + offsets.x;
				y = paddingY
						+ (int) (row * yFactor + ly * (double) (column % 2))
						+ offsets.y;
				w = width + offsets.width;
				h = height + offsets.height;

				// Set the bounds for the child IFigure.
				child.setBounds(new Rectangle(x, y, w, h));
			}
		} else { // All hexagons are rotated 90 degrees.

			// Get the hexagon side lengths supported by the current
			// width/height of
			// the client area.
			double lx = (double) (limit.width - horizontalSpacing
					* (columns - 1))
					/ (cosPiDivSix * (double) (columns * 2 + 1));
			double ly = 2.0
					* (double) (limit.height - verticalSpacing * (rows - 1))
					/ (double) (3 * rows + 1);

			// Determine which length to use and flag the direction that needs
			// to be
			// padded (x or y).
			double l;
			int paddingX = 0, paddingY = 0;
			if (ly >= lx) { // Size restricted by width.
				l = lx;
			} else { // Size restricted by height.
				l = ly;
				paddingX++;
			}

			// Compute the half-height and half-width of each hexagon.
			lx = l * cosPiDivSix;
			ly = l / 2; // shortcut: sin(PI/6) = 1/2!

			// Compute the necessary padding for whichever dimension needs it.
			if (paddingX != 0) { // Size restricted by height.
				paddingX = (int) (((double) limit.width - horizontalSpacing
						* (columns - 1) - (lx * (double) (2 * columns + 1))) / 2.0);
			} else { // Size restricted by width.
				paddingY = (int) (((double) limit.height - verticalSpacing
						* (rows - 1) - (ly * (double) (3 * rows + 1))) / 2.0);
			}

			// Update the PointList used by each hexagon.
			// Note: The hexagons store references to the PointList, so we do
			// not need to call hexagon.setPoints(), which, in fact,
			// refreshes/repaints the Shape, in the quadratic loop below.
			points.setPoint(new Point((int) lx, 0), 0);
			points.setPoint(new Point((int) (lx + lx), (int) ly), 1);
			points.setPoint(new Point((int) (lx + lx), (int) (ly + l)), 2);
			points.setPoint(new Point((int) lx, (int) (l + l)), 3);
			points.setPoint(new Point(0, (int) (ly + l)), 4);
			points.setPoint(new Point(0, (int) ly), 5);

			// We want to limit math ops, so compute the factors used in the
			// loop.
			double xFactor = lx + lx + (double) horizontalSpacing;
			double yFactor = l + ly + (double) verticalSpacing;

			// Compute the width and height of each hexagon.
			int width = (int) Math.ceil(lx + lx);
			int height = (int) Math.ceil(l + l);

			// Variables used throughout the below loop.
			int i, row, column, x, y, w, h;

			// Loop over the IFigures in the container with this layout.
			for (Object childObject : container.getChildren()) {
				IFigure child = (IFigure) childObject;

				// Get the constraints (and the x, y, w, h offsets from it).
				GridData constraint = getConstraint(child);
				Rectangle offsets = constraint.getOffsets();

				// Get the index and compute the row and column for the index.
				i = constraint.getIndex();
				row = i / columns;
				column = i % columns;

				// Compute the bounds of the cell in the row, column position.
				x = paddingX
						+ (int) (column * xFactor + lx * (double) (row % 2))
						+ offsets.x;
				y = paddingY + (int) (row * yFactor) + offsets.y;
				w = width + offsets.width;
				h = height + offsets.height;

				// Set the bounds for the child IFigure.
				child.setBounds(new Rectangle(x, y, w, h));
			}
		}

		return;
	}

}
