/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.nek5000.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.nek5000.ProblemProperties;
import org.junit.Test;

/**
 * Tests the methods of the Nek ProblemProperties class.
 * 
 * @author w5q
 * 
 */
public class ProblemPropertiesTester {

	/*
	 * Tests the construction of the ProblemProperties class.
	 */
	@Test
	public void checkConstruction() {

		ProblemProperties properties = null;

		// Try to construct with invalid values
		properties = new ProblemProperties(0, -1, -2, -3);

		// Check it's not null
		assertNotNull(properties);

		// Check the values weren't set
		assertEquals(0, properties.getNumDimensions());
		assertEquals(0, properties.getNumThermalElements());
		assertEquals(0, properties.getNumFluidElements());
		assertEquals(0, properties.getNumPassiveScalars());
		assertEquals("2.610000", properties.getNekVersion());

		// Try to construct with valid values
		properties = new ProblemProperties(2, 64, 32, 0);

		// Check the values were set
		assertEquals(2, properties.getNumDimensions());
		assertEquals(64, properties.getNumThermalElements());
		assertEquals(32, properties.getNumFluidElements());
		assertEquals(0, properties.getNumPassiveScalars());
		assertEquals("2.610000", properties.getNekVersion());
	}

}
