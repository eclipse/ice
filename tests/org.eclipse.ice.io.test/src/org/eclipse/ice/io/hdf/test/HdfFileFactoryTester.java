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
package org.eclipse.ice.io.hdf.test;

import static org.junit.Assert.*;

import org.eclipse.ice.io.hdf.HdfFileFactory;

import java.io.File;
import java.io.IOException;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * This class tests the HdfFileFactory class.
 * </p>
 * 
 * @author Eric J. Lingerfelt
 */
public class HdfFileFactoryTester {

	/**
	 * The file handle for the test
	 */
	File dataFile = null;

	/**
	 * <p>
	 * This operation checks the openH5File, createH5File, and closeH5File
	 * operations.
	 * </p>
	 */
	@Test
	public void checkFileOperations() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String testFileName = "test1.h5";
		H5File h5File;
		int fileHandle = -1;
		int newFileHandle = -1;
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests";

		// Creating a file:

		// Try to create a file on null
		assertNull(HdfFileFactory.createH5File(null));

		// Create a normal file
		File dataFile = new File(userDir + separator + testFileName);
		// Create a file
		System.out.println(dataFile.getAbsolutePath());
		h5File = HdfFileFactory.createH5File(dataFile.toURI());
		// Make sure it is not null
		assertNotNull(h5File);

		// Make sure it exists and can be written and read from
		assertTrue(h5File.exists());
		assertTrue(h5File.canWrite());
		assertTrue(h5File.canRead());
		try {
			// Get the first file handle
			fileHandle = h5File.open();
			// Make sure the file is open and exists
			assertTrue(fileHandle >= 0);

			h5File.createGroup("Bob",
					(H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
							.getRootNode()).getUserObject());

			// Close the file
			h5File.close();

			// create a file ontop of a file
			h5File = HdfFileFactory.createH5File(dataFile.toURI());
			// Open it
			fileHandle = h5File.open();
			// Make sure the file is open and exists
			assertTrue(fileHandle >= 0);

			// Check to see if bob exists
			H5Group group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();
			assertEquals(0, group.getMemberList().size()); // Bob, or any other
															// group, does not
															// exist

			// Now, add Bob back into the group
			h5File.createGroup("Bob",
					(H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
							.getRootNode()).getUserObject());

			// Close the file with the correct operation
			HdfFileFactory.closeH5File(h5File);

			// Now, try to open that file again with open
			h5File = HdfFileFactory.openH5File(dataFile.toURI());

			assertNotNull(h5File);

			// Show that it exists
			assertTrue(h5File.exists());

			// Delete the dataFile (close the file first)
			h5File.close();
			dataFile.delete();
			// Now, try to grab it. This should return null
			assertNull(HdfFileFactory.openH5File(dataFile.toURI()));

		} catch (Exception e) {
			// Fail if any exceptions are hit
			e.printStackTrace();
			fail();
		}

		// Delete the file
		if (dataFile.exists()) {
			dataFile.delete();
		}

	}
}