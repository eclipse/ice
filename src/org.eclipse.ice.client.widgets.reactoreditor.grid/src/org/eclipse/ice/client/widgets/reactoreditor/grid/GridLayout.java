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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class provides an alternative grid-based layout to draw2d's SWT-inspired
 * GridLayout. With this GridLayout, all child IFigures are equally-sized.<br>
 * <br>
 * There is an accompanying {@link GridData} feature, as well. All child
 * IFigures are expected to have a {@link GridData} as a constraint.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class GridLayout extends AbstractHintLayout {

	/**
	 * The number of rows in this layout.
	 */
	protected final int rows;

	/**
	 * The number of columns in this layout.
	 */
	protected final int columns;

	/**
	 * How many pixels to put between cells horizontally.
	 */
	protected int horizontalSpacing = -1;

	/**
	 * How many pixels to put between cells vertically.
	 */
	protected int verticalSpacing = -1;

	/**
	 * A HashMap containing the constraints for each IFigure affected by the
	 * layout.
	 */
	protected final Map<IFigure, GridData> constraints = new HashMap<IFigure, GridData>();

	/**
	 * The default constructor. Generates a 1x1 GridLayout.
	 */
	public GridLayout() {
		super();

		// The rows and columns default to 1.
		this.rows = 1;
		this.columns = 1;
	}

	/**
	 * A non-default constructor. This generates a rows by columns GridLayout.
	 * If rows or columns are less than 1, they are set to 1.
	 * 
	 * @param rows
	 *            The number of rows in the layout.
	 * @param columns
	 *            The number of columns in the layout.
	 */
	public GridLayout(int rows, int columns) {
		super();

		// Make sure the number of rows/columns is at least 1.
		this.rows = (rows > 1 ? rows : 1);
		this.columns = (columns > 1 ? columns : 1);
	}

	/**
	 * Gets how many pixels are put between child IFigures horizontally.
	 * 
	 * @return The horizontal spacing for this layout.
	 */
	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	/**
	 * Gets how many pixels are put between child IFigures vertically.
	 * 
	 * @return The vertical spacing for this layout.
	 */
	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	/**
	 * Sets how many pixels are put between child IFigures horizontally.
	 * 
	 * @param horizontalSpacing
	 *            The horizontal spacing for this layout. This can be positive
	 *            or negative.
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}

	/**
	 * Sets how many pixels are put between child IFigures vertically.
	 * 
	 * @param verticalSpacing
	 *            The vertically spacing for this layout. This can be positive
	 *            or negative.
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.
	 * draw2d.IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint,
			int hHint) {

		// This layout is greedy, so return the maximum possible size (the size
		// of the container).
		return container.getClientArea().getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.LayoutManager#getConstraint(org.eclipse.draw2d.IFigure
	 * )
	 */
	@Override
	public GridData getConstraint(IFigure child) {
		return constraints.get(child);
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only
	 * be of type {@link GridData}.
	 * 
	 * @see LayoutManager#setConstraint(IFigure, Object)
	 */
	@Override
	public void setConstraint(IFigure figure, Object newConstraint) {
		// super needs to perform whatever it does with the constraint.
		super.setConstraint(figure, newConstraint);

		// Store the constraint in the HashMap if it is not null.
		if (newConstraint != null) {
			constraints.put(figure, (GridData) newConstraint);
		}
		return;
	}

	/**
	 * Applies a rectangle-based layout to the container.
	 */
	@Override
	public void layout(IFigure container) {

		// Get the maximum bounding box we can use for layout out sub-figures.
		Rectangle limit = container.getClientArea();

		// Compute the maximum width/height of each cell. We'll make everything
		// square.
		int maxWidth = (limit.width - (columns - 1) * horizontalSpacing)
				/ columns;
		int maxHeight = (limit.height - (rows - 1) * verticalSpacing) / rows;

		// Get the size and any vertical/horizontal padding necessary.
		int size, paddingX = 0, paddingY = 0;
		if (maxWidth > maxHeight) { // Height-restricted.
			size = maxHeight;
			paddingX = (limit.width - size * columns - horizontalSpacing
					* (columns - 1)) / 2;
		} else { // Width-restricted.
			size = maxWidth;
			paddingY = (limit.height - size * rows - verticalSpacing
					* (rows - 1)) / 2;
		}

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
			x = paddingX + column * (size + horizontalSpacing) + offsets.x;
			y = paddingY + row * (size + verticalSpacing) + offsets.y;
			w = size + offsets.width;
			h = size + offsets.height;

			// Set the bounds for the child IFigure.
			child.setBounds(new Rectangle(x, y, w, h));
		}

		return;
	}

}
