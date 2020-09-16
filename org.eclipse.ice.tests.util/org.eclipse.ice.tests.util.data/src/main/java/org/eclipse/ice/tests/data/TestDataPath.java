/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.tests.util.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for retrieving data from the test data directory.
 * @author Daniel Bluhm
 */
public class TestDataPath {
	/**
	 * Logger.
	 */
	public final Logger logger = LoggerFactory.getLogger(TestConfig.class);

	/**
	 * Path of the default test data directory.
	 */
	static final Path DEFAULT_TEST_DATA_DIR =
		Path.of(System.getProperty("user.home")).resolve("ICETests");

	/**
	 * Environment variable for test data directory location.
	 */
	static final String TEST_DATA_PATH_ENV_VAR = "TEST_DATA_PATH";

	/**
	 * Test data directory.
	 */
	private Path testDataDir;

	/**
	 * FileSystem on which paths are retrieved.
	 */
	private FileSystem fs;

	/**
	 * No args constructor.
	 */
	public TestDataPath() {
		fs = FileSystems.getDefault();
		this.testDataDir = determineTestDataDirectory(System.getenv());
	}

	/**
	 * Constructor for use with a different FS and environment. Primarily for
	 * testing.
	 * @param fs alternate FileSystem
	 * @param env alternate environment
	 */
	public TestDataPath(FileSystem fs, Map<String, String> env) {
		this.fs = fs;
		this.testDataDir = determineTestDataDirectory(env);
	}

	/**
	 * Determine test data directory from environment.
	 * @param env environment from which an alternate test data directory may be
	 *        loaded.
	 */
	private Path determineTestDataDirectory(Map<String, String> env) {
		String alternateTestDataDir = env.get(TEST_DATA_PATH_ENV_VAR);
		if (alternateTestDataDir == null) {
			return fs.getPath(DEFAULT_TEST_DATA_DIR.toString());
		} else {
			return fs.getPath(alternateTestDataDir);
		}
	}

	/**
	 * Create the test data directory if it doesn't already exist.
	 * @throws IOException on file errors
	 */
	public void create() throws IOException {
		Files.createDirectories(testDataDir);
	}

	/**
	 * Create a directory inside of test data directory if it doesn't already
	 * exist. All parent directories are created if necessary.
	 * @param dirname of directory to create
	 * @throws IOException on file errors
	 */
	public void createDir(String dirname) throws IOException {
		Files.createDirectories(testDataDir.resolve(dirname));
	}

	/**
	 * Create a directory inside of test data directory if it doesn't already
	 * exist. All parent directories are created if necessary. Generally
	 * intended for use by paths resolved from the test data directory but all
	 * other paths will work as well.
	 * @param dirname of directory to create
	 * @throws IOException on file errors
	 */
	public void createDir(Path dir) throws IOException {
		Files.createDirectories(dir);
	}

	/**
	 * Return whether the test data directory exists.
	 * @return true if test data directory exists
	 */
	public boolean exists() {
		return Files.exists(testDataDir);
	}

	/**
	 * Return whether the file exists in the test data directory.
	 * @param filename to test
	 * @return true if filename exists in test data directory.
	 */
	public boolean exists(String filename) {
		return Files.exists(testDataDir.resolve(filename));
	}

	/**
	 * Return whether the path exists. Generally intended for paths resolved
	 * from the testDataDirectory but will work for any path.
	 * @param path to test
	 * @return true if path exists.
	 */
	public boolean exists(Path path) {
		return Files.exists(path);
	}

	/**
	 * Return a path to the specified test data file.
	 * @param filename for which a path will be returned.
	 * @return the path to the file within the ICE test data directory or null
	 *         if the test data directory does not exist.
	 */
	public Path resolve(String filename) {
		return testDataDir.resolve(filename);
	}

	/**
	 * Return a new input stream for the file inside of the test data directory.
	 * @param filename file for which an input stream will be opened.
	 * @return InputStream for file in test data directory.
	 * @throws IOException on file not fouind
	 */
	public InputStream input(String filename) throws IOException {
		return Files.newInputStream(testDataDir.resolve(filename));
	}

	/**
	 * Return a new input stream for the file inside of the test data directory.
	 * Generally intended for paths resolved from test data directory but will
	 * work for any Path.
	 * @param path for which an input stream will be opened.
	 * @return InputStream for file in test data directory.
	 * @throws IOException on file not fouind
	 */
	public InputStream input(Path path) throws IOException {
		return Files.newInputStream(path);
	}

	/**
	 * Return a new output stream for a file inside of the test data directory.
	 * If the file does not exist, a new file will be created.
	 * @param filename file for which an output stream will be opened.
	 * @return OutputStream for file in test data Directory.
	 * @throws IOException on file open errors
	 */
	public OutputStream output(String filename) throws IOException {
		return Files.newOutputStream(testDataDir.resolve(filename));
	}

	/**
	 * Return a new output stream for a file inside of the test data directory.
	 * If the file does not exist, a new file will be created.
	 * Generally intended for paths resolved from test data directory but will
	 * work for any Path.
	 * @param path for which an output stream will be opened.
	 * @return OutputStream for file in test data Directory.
	 * @throws IOException on file open errors
	 */
	public OutputStream output(Path path) throws IOException {
		return Files.newOutputStream(path);
	}

	/**
	 * Return a new reader for a file inside of the test data directory.
	 * @param filename to open for reading.
	 * @return Reader for file in test data directory.
	 * @throws IOException on file not found
	 */
	public Reader reader(String filename) throws IOException {
		return Files.newBufferedReader(testDataDir.resolve(filename));
	}

	/**
	 * Return a new reader for a file inside of the test data directory.
	 * @param path to open for reading.
	 * @return Reader for file in test data directory.
	 * @throws IOException on file not found
	 */
	public Reader reader(Path path) throws IOException {
		return Files.newBufferedReader(path);
	}

	/**
	 * Return a new writer for a file inside of the test data directory.
	 * A new file will be created if it does not already exist.
	 * @param filename file to open for writing.
	 * @return Writer for file in test data directory.
	 * @throws IOException on file open errors
	 */
	public Writer writer(String filename) throws IOException {
		return Files.newBufferedWriter(testDataDir.resolve(filename));
	}

	/**
	 * Return a new writer for a file inside of the test data directory.
	 * A new file will be created if it does not already exist.
	 * @param path to open for writing.
	 * @return Writer for file in test data directory.
	 * @throws IOException on file open errors
	 */
	public Writer writer(Path path) throws IOException {
		return Files.newBufferedWriter(path);
	}
}
