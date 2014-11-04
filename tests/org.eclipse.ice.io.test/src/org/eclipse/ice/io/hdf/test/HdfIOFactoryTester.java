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
package org.eclipse.ice.io.hdf.test;

import static org.junit.Assert.fail;
import org.eclipse.ice.io.hdf.HdfIOFactory;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the out-of-the-box writing and reading ability of {@link HdfIOFactory}.
 * 
 * @author Jordan H. Deyton
 * 
 */
@Ignore
public class HdfIOFactoryTester {

	/**
	 * This test ensures the factory's reference to the {@link IHdfIORegistry}
	 * is set properly and can be retreived.
	 */
	@Test
	public void checkRegistry() {
		fail("Not implemented.");
	}

	/**
	 * This method checks that, by default, the factory can write to an HDF5
	 * file. If object types are not supported (which should be the case since
	 * OSGi is not running), then the HDF5 file will be empty.
	 */
	@Test
	public void checkWriting() {
		fail("Not implemented.");
	}

	/**
	 * This method checks that, by default, the factory can read from an HDF5
	 * file. If the tag Attributes do not match any other factory in the
	 * registry or are non-existent, then the HDF5 Group cannot be read.
	 */
	@Test
	public void checkReading() {
		fail("Not implemented.");
	}
}
