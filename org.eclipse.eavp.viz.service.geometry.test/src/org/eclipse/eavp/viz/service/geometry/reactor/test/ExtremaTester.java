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
package org.eclipse.eavp.viz.service.geometry.reactor.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.junit.Test;

/**
 * A class to test the functionality of Extrema.
 * 
 * @author Robert Smith
 *
 */
public class ExtremaTester {

	/**
	 * Check that an Extrema is constructed correctly.
	 */
	@Test
	public void checkConstruction() {

		// Create some extrema and check the initial values.
		Extrema first = new Extrema(-1d, 0d, -0.5d, 0.5d, 0d, 1d);
		assertTrue(first.getMinX() == -1d);
		assertTrue(first.getMinY() == -0.5d);
		assertTrue(first.getMaxY() == 0.5d);
		assertTrue(first.getMaxZ() == 1d);

		Extrema second = new Extrema(0d, 1d, -1d, 0d, -0.5d, 0.5d);
		assertTrue(second.getMinY() == -1d);
		assertTrue(second.getMinZ() == -0.5d);
		assertTrue(second.getMaxZ() == 0.5d);
		assertTrue(second.getMaxX() == 1d);

		Extrema third = new Extrema(-0.5d, 0.5d, 0d, 1d, -1d, 0d);
		assertTrue(third.getMinZ() == -1d);
		assertTrue(third.getMinX() == -0.5d);
		assertTrue(third.getMaxX() == 0.5d);
		assertTrue(third.getMaxY() == 1d);

		// Create an extrema by combining the regions above
		ArrayList<Extrema> extrema = new ArrayList<Extrema>();
		extrema.add(first);
		extrema.add(second);
		extrema.add(third);
		Extrema all = new Extrema(extrema);

		// Check that the new Extrema covers all the sub-regions it was made
		// from
		assertTrue(all.getMinX() == -1d);
		assertTrue(all.getMaxX() == 1d);
		assertTrue(all.getMinY() == -1d);
		assertTrue(all.getMaxY() == 1d);
		assertTrue(all.getMinZ() == -1d);
		assertTrue(all.getMaxZ() == 1d);

		// Create an extrema from an empty list and check that it creates a
		// region with 0 for all its values
		Extrema empty = new Extrema(new ArrayList<Extrema>());
		assertTrue(empty.getMinX() == 0d);
		assertTrue(empty.getMaxX() == 0d);
		assertTrue(empty.getMinY() == 0d);
		assertTrue(empty.getMaxY() == 0d);
		assertTrue(empty.getMinZ() == 0d);
		assertTrue(empty.getMaxZ() == 0d);
	}

}