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

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class provides a constraint for the custom GridLayout in this same
 * package. These GridData objects can keep track of the index and x, y, width,
 * and height offsets for the associated Figure.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class GridData {

	/**
	 * The index of the Figure.
	 */
	private final int index;

	/**
	 * A draw2d Rectangle representing the x, y, width, and height offsets of
	 * this Figure.
	 */
	private final Rectangle offsets;

	/**
	 * This creates a GridData with a specified index and no offsets.
	 * 
	 * @param index
	 *            The index of the Figure.
	 */
	public GridData(int index) {
		this.index = index;
		offsets = new Rectangle(0, 0, 0, 0);
	}

	/**
	 * This creates a GridData with a specified index and a rectangle that
	 * specifies the offsets of the Figure (x, y, width, and height).
	 * 
	 * @param index
	 *            The index of the Figure.
	 * @param offsets
	 *            A draw2d Rectangle representing the x, y, width, and height
	 *            offsets of this Figure.
	 */
	public GridData(int index, Rectangle offsets) {
		this.index = index;
		this.offsets = offsets;
	}

	/**
	 * Gets the index of the Figure with this GridData.
	 * 
	 * @return An integer representing the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets a copy of the offsets of this GridData object.
	 * 
	 * @return A draw2d Rectangle containing pixel offsets.
	 */
	public Rectangle getOffsets() {
		return offsets.getCopy();
	}
}
