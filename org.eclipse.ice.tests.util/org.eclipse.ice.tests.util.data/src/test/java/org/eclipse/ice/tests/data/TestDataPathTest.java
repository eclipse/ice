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

package org.eclipse.ice.tests.util.data;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * Test TestDataPath utility class.
 * @author Daniel Bluhm
 */
class TestDataPathTest {

	public static TestDataPath inMemTestDataPath(
		Map<String, String> env, String... dirs
	) throws IOException {
		FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
		for (String dir : dirs) {
			Path toMake = fs.getPath(dir);
			Files.createDirectories(toMake);
		}
		if (env == null) {
			env = Collections.emptyMap();
		}
		return new TestDataPath(fs, env);
	}

	@Test
	void testPlatformIndependence() {
		FileSystem fs = Jimfs.newFileSystem(Configuration.windows());
		TestDataPath data = new TestDataPath(fs, Map.of(
			TestDataPath.TEST_DATA_PATH_ENV_VAR, "test"
		));
		assertEquals("test\\test\\test.txt", data.resolve("test/test.txt").toString());
	}

	@Test
	void testResolve() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertEquals(
			TestDataPath.DEFAULT_TEST_DATA_DIR.resolve("test").toString(),
			data.resolve("test").toString()
		);
	}

	@Test
	void testResolveWithOverridenDefault() throws IOException {
		final Path alt = Path.of("/home/test/test_data");
		TestDataPath data = inMemTestDataPath(
			Map.of(TestDataPath.TEST_DATA_PATH_ENV_VAR, alt.toString())
		);
		assertEquals(
			alt.resolve("test").toString(),
			data.resolve("test").toString()
		);
	}

	@Test
	void testCreate() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		data.create();
		assertTrue(Files.exists(data.resolve("")));
	}

	@Test
	void testCreateDirString() throws IOException {
		final String test = "test";
		TestDataPath data = inMemTestDataPath(null);
		data.createDir(test);
		assertTrue(Files.exists(data.resolve(test)));
	}

	@Test
	void testCreateDirPath() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		final Path test = data.resolve("test");
		data.createDir(test);
		assertTrue(Files.exists(test));
	}

	@Test
	void testExists() throws IOException {
		TestDataPath data = inMemTestDataPath(
			null, TestDataPath.DEFAULT_TEST_DATA_DIR.toString()
		);
		assertTrue(data.exists());
	}

	@Test
	void testExistsString() throws IOException {
		TestDataPath data = inMemTestDataPath(
			null,
			TestDataPath.DEFAULT_TEST_DATA_DIR.resolve("test").toString()
		);
		assertTrue(data.exists("test"));
	}

	@Test
	void testExistsPath() throws IOException {
		TestDataPath data = inMemTestDataPath(
			null,
			TestDataPath.DEFAULT_TEST_DATA_DIR.resolve("test").toString()
		);
		final Path test = data.resolve("test");
		assertTrue(data.exists(test));
	}

	@Test
	void testInputString() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {data.input("test");});
		data.create();
		Files.createFile(data.resolve("test"));
		InputStream input = data.input("test");
		assertEquals(0, input.available());
	}

	@Test
	void testInputPath() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {
			data.input(data.resolve("test"));
		});
		data.create();
		Files.createFile(data.resolve("test"));
		InputStream input = data.input(data.resolve("test"));
		assertEquals(0, input.available());
	}

	@Test
	void testOutputString() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {
			data.output("test");
		});
		data.create();
		data.output("test");
		Files.exists(data.resolve("test"));
	}

	@Test
	void testOutputPath() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {
			data.output(data.resolve("test"));
		});
		data.create();
		data.output(data.resolve("test"));
		Files.exists(data.resolve("test"));
	}

	@Test
	void testReaderString() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {data.reader("test");});
		data.create();
		Files.createFile(data.resolve("test"));
		Reader reader = data.reader("test");
		assertEquals(-1, reader.read());
	}

	@Test
	void testReaderPath() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {
			data.reader(data.resolve("test"));
		});
		data.create();
		Files.createFile(data.resolve("test"));
		Reader reader = data.reader(data.resolve("test"));
		assertEquals(-1, reader.read());
	}

	@Test
	void testWriterString() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {
			data.writer("test");
		});
		data.create();
		data.writer("test");
		Files.exists(data.resolve("test"));
	}

	@Test
	void testWriterPath() throws IOException {
		TestDataPath data = inMemTestDataPath(null);
		assertThrows(IOException.class, () -> {
			data.writer(data.resolve("test"));
		});
		data.create();
		data.writer(data.resolve("test"));
		Files.exists(data.resolve("test"));
	}
}
