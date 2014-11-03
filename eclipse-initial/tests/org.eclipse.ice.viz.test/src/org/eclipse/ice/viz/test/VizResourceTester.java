/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.viz.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.eclipse.ice.viz.VizResource;
import org.junit.Test;

/**
 * This class is responsible for checking the VizResource class
 * 
 * @author tnp
 * 
 */
public class VizResourceTester {

	/**
	 * The VizResource to use for testing.
	 */
	private VizResource vizResource;

	/**
	 * Check the setFileSet and getFileSet operations.
	 */
	@Test
	public void checkFileSet() {

		// Initialize the VizResource
		vizResource = new VizResource();

		// Ensure null initialization
		assertNull(vizResource.getFileSet());

		// Create a file set
		String[] fakeFilenames = { "Benjamin", "Buford", "Blue" };

		// Set the file set
		vizResource.setFileSet(fakeFilenames);

		// Check that the file set is set
		assertNotNull(vizResource.getFileSet());
		assertEquals(3, vizResource.getFileSet().length);
		assertTrue(fakeFilenames.equals(vizResource.getFileSet()));

	}

	/**
	 * Check the setFileSetTitle and getFileSetTitle operations.
	 */
	@Test
	public void checkFileSetTitle() {

		// Initialize the VizResource
		vizResource = new VizResource();

		// Ensure null initialization
		assertNull(vizResource.getFileSetTitle());

		// Set the file set title
		vizResource.setFileSetTitle("Bubba");

		// Check that the file set title is set
		assertNotNull(vizResource.getFileSetTitle());
		assertTrue("Bubba".equals(vizResource.getFileSetTitle()));

	}

	/**
	 * Check the setRemote and isRemote operations.
	 */
	@Test
	public void checkIsRemote() {

		// Initialize the VizResource
		vizResource = new VizResource();

		// Default value is false
		assertFalse(vizResource.isRemote());

		// Set to be remote
		vizResource.setRemote(true);

		// Check that the flag is now true
		assertTrue(vizResource.isRemote());

	}

}
