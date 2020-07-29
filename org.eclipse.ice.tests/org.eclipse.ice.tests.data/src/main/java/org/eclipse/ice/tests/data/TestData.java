/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.tests.data;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * Utility methods for retrieving data from the test data directory.
 * @author Daniel Bluhm
 */
public class TestData {
	/**
	 * Path of the default test data directory.
	 */
	static final Path DEFAULT_TEST_DATA_DIR =
		Path.of(System.getProperty("user.home")).resolve("ICETests");

	/**
	 * Environment variable for test data directory location.
	 */
	static final String TEST_DATA_DIR_ENV_VAR = "TEST_DATA_DIR";

	/**
	 * Default test data directory.
	 */
	private Path defaultTestDataDir = DEFAULT_TEST_DATA_DIR;

	/**
	 * FileSystem on which paths are retrieved.
	 */
	private FileSystem fs = FileSystems.getDefault();

	/**
	 * Environment from which an alternate test data dir will be potentially
	 * pulled.
	 */
	private Map<String, String> env = System.getenv();

	/**
	 * No args constructor.
	 */
	public TestData() {
	}

	/**
	 * Constructor for use with a different FS and environment. Primarily for
	 * testing.
	 * @param fs alternate FileSystem
	 * @param env alternate environment
	 */
	public TestData(FileSystem fs, Map<String, String> env) {
		this.fs = fs;
		this.env = env;
		this.defaultTestDataDir = fs.getPath(DEFAULT_TEST_DATA_DIR.toString());
	}

	/**
	 * Return a path to the specified test data file.
	 * @param filename for which a path will be returned.
	 * @return the path to the file within the ICE test data directory or null
	 *         if the file does not exist.
	 */
	public Optional<Path> resolve(String filename) {
		Optional<Path> returnValue = Optional.empty();
		Path testDataDir = null;

		String alternateTestDataDir = env.get(TEST_DATA_DIR_ENV_VAR);
		if (alternateTestDataDir == null) {
			testDataDir = defaultTestDataDir;
		} else {
			testDataDir = fs.getPath(alternateTestDataDir);
		}

		if (Files.exists(testDataDir) && Files.isDirectory(testDataDir)) {
			returnValue = Optional.of(testDataDir.resolve(filename));
		}
		return returnValue;
	}

}