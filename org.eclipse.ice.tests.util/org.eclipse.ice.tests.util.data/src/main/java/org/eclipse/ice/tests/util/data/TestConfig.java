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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testing utility for loading configuration files from test data directory.
 * @author Daniel Bluhm
 */
public class TestConfig extends Properties {
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -4388914400856890552L;

	/**
	 * Logger.
	 */
	public static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

	/**
	 * Test data directory path resolver.
	 */
	private TestDataPath dataPath;

	public TestConfig(TestDataPath dataPath) {
		this.dataPath = dataPath;
	}

	/**
	 * Retrieve a test configuration from the test data directory and load the
	 * properties contained therein. If the file was not found, an empty
	 * TestConfig is returned. This is intended to enable usage of getProperty
	 * with a default value to statically provide defaults from code. If file
	 * non-existence is an error condition, verify the existence of the file
	 * with {@link TestDataPath#exists(String)}.
	 * @param propertyFilename name of the property file to load.
	 * @return loaded Properties, empty if any errors occur.
	 */
	public void load(String propertyFilename) {
		Path propPath = dataPath.resolve(propertyFilename);
		try(InputStream propStream = dataPath.input(propPath)) {
			super.load(propStream);
		} catch (IOException e) {
			logger.warn(
				"Could not load properties from {}:",
				propPath,
				e
			);
		}
	}

	/**
	 * Load a TestConfig from a property file in the default test data
	 * directory. See {@link TestConfig#load(String)}.
	 * @param propertyFilename filename of property file from which properties
	 *        will be loaded.
	 * @return new TestConfig
	 */
	public static TestConfig from(String propertyFilename) {
		TestConfig config = new TestConfig(new TestDataPath());
		config.load(propertyFilename);
		return config;
	}
}
