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

import static org.junit.Assert.assertEquals;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridData;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Test;

public class GridDataTester {

	@Test
	public void checkConstructors() {
		// Test the first constructor (only index provided).
		GridData gridData = new GridData(7);

		// Check the returned index.
		assertEquals(7, gridData.getIndex());

		// The offsets should all be zero.
		Rectangle offsets = gridData.getOffsets();
		assertEquals(0, offsets.x);
		assertEquals(0, offsets.y);
		assertEquals(0, offsets.width);
		assertEquals(0, offsets.height);

		// Test the second constructor (index and offsets provided).
		gridData = new GridData(42, new Rectangle(1, 3, 3, 7));

		// Check the returned index.
		assertEquals(42, gridData.getIndex());

		// The offsets should all be zero.
		offsets = gridData.getOffsets();
		assertEquals(1, offsets.x);
		assertEquals(3, offsets.y);
		assertEquals(3, offsets.width);
		assertEquals(7, offsets.height);

		// Make sure we can't modify the original offsets.
		offsets.x = 2;
		offsets = gridData.getOffsets();
		assertEquals(1, offsets.x);
		assertEquals(3, offsets.y);
		assertEquals(3, offsets.width);
		assertEquals(7, offsets.height);
	}
}
