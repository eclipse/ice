/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.tests.data;

import java.io.File;
import java.nio.file.Path;

/**
 * Utility methods for retrieving data from the test data directory.
 * @author Daniel Bluhm
 */
public class TestData {
	/**
	 * Path of the default test data directory.
	 */
	public static final Path DEFAULT_TEST_DATA_DIR =
		Path.of(System.getProperty("user.home")).resolve("ICETests");
	
	/**
	 * Environment variable for test data directory location.
	 */
	public static final String TEST_DATA_DIR_ENV_VAR = "TEST_DATA_DIR";

	/**
	 * Return a path to the specified test data file.
	 * @param filename for which a path will be returned.
	 * @return the path to the file within the ICE test data directory.
	 */
	public static Path path(String filename) {
		Path returnValue = null;
		Path testDataDir = null;

		String alternateTestDataDir = System.getenv(TEST_DATA_DIR_ENV_VAR);
		if (alternateTestDataDir == null) {
			testDataDir = DEFAULT_TEST_DATA_DIR;
		} else {
			testDataDir = Path.of(alternateTestDataDir);
		}

		File testDataDirFile = testDataDir.toFile();
		if (testDataDirFile.exists() && testDataDirFile.isDirectory()) {
			returnValue = testDataDir.resolve(filename);
		}
		return returnValue;
	}

}