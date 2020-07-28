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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * Test TestData utility class.
 * @author Daniel Bluhm
 */
class TestDataTest {
	
	public FileSystem inMemFSWithDirs(String... dirs) throws IOException {
		FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
		for (String dir : dirs) {
			Path toMake = fs.getPath(dir);
			Files.createDirectories(toMake);
		}
		return fs;
	}

	@Test
	void testDefaultTestDataDir() throws IOException {
		TestData data = new TestData(inMemFSWithDirs(
			TestData.DEFAULT_TEST_DATA_DIR.toString()
		), Collections.emptyMap());
		assertEquals(
			TestData.DEFAULT_TEST_DATA_DIR.resolve("test").toString(),
			data.resolve("test").toString()
		);
	}

	@Test
	void testOverriddenTestDataDir() throws IOException {
		final String alt = "/home/test/test_data";
		TestData data = new TestData(inMemFSWithDirs(
			TestData.DEFAULT_TEST_DATA_DIR.toString(),
			alt
		), Map.of(TestData.TEST_DATA_DIR_ENV_VAR, alt));
		assertEquals(
			alt + "/test",
			data.resolve("test").toString()
		);
	}

	@Test
	void testNonExistentReturnsNull() throws IOException {
		final String unlikelyFile = "a_file_that_could_not_possibly_exist";
		TestData data = new TestData();
		assertNull(data.resolve(unlikelyFile));
	}
}
