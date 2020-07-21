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

package org.eclipse.ice.tests.dev.pojofromjson;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.ice.dev.pojofromjson.PojoFromJson;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * Test PojoFromJson.
 */
class PojoFromJsonTest {

	/**
	 * Source JSON.
	 */
	private static final String TEST_ELEMENT_JSON = "TestElement.json";

	/**
	 * Interface destination file name.
	 */
	private static final String TEST_ELEMENT_INTERFACE = "TestElement.java";

	/**
	 * Implementation destination file name.
	 */
	private static final String TEST_ELEMENT_IMPLEMENTATION = "TestElementImplementation.java";

	/**
	 * @return InputStream for JSON
	 * @throws FileNotFoundException
	 */
	public InputStream getTestElementJsonStream() throws FileNotFoundException {
		return new FileInputStream(
			getClass().getClassLoader().getResource(TEST_ELEMENT_JSON).getPath()
		);
	}

	/**
	 * @return Path to in-memory destination.
	 * @throws IOException
	 */
	public Path inMemoryDestination() throws IOException {
		FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
		Path dest = fs.getPath("/dest");
		Files.createDirectory(dest);
		return dest;
	}

	/**
	 * Test that handleInputJson generates an interface and implementation as
	 * expected.
	 *
	 * There is no need to test the contents of the generated files as that is
	 * tested in
	 * {@link org.eclipse.ice.tests.dev.annotations.processors.DataElementProcessorTest}.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testInterfaceAndImplementationGenerated()
		throws JsonParseException, JsonMappingException, FileNotFoundException,
		IOException
	{
		Path destination = inMemoryDestination();
		PojoFromJson.handleInputJson(
			getTestElementJsonStream(), destination
		);
		assertTrue(Files.exists(destination.resolve(TEST_ELEMENT_INTERFACE)));
		assertTrue(Files.exists(destination.resolve(TEST_ELEMENT_IMPLEMENTATION)));
	}
}
