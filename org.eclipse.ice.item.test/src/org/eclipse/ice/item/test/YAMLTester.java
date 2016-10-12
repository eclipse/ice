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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 * This class tests the SnakeYAML parser to make sure that it is integrated into
 * ICE IO correctly.
 * 
 * @author Jay Jay Billings
 * 
 */
public class YAMLTester {

	/**
	 * This operation runs a simple SnakeYAML example from their website and
	 * checks the results.
	 */
	@Test
	public void checkSnakeYAML() {

		// Create a YAML tree
		Yaml yaml = new Yaml();
		String document = "hello: 25";
		// Load it from the string
		Map map = (Map) yaml.load(document);
		// Check the map
		assertEquals("{hello=25}", map.toString());
		assertEquals(new Integer(25), map.get("hello"));

		return;
	}

	/**
	 * This operation loads input from INL's Moose framework
	 * to make sure that the file can be parsed. It is also based on an example
	 * from the SnakeYAML website.
	 */
	@Test
	public void checkSnakeYAMLWithMoose() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		InputStream input = null;
		File mooseFile = null;
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";

		// Load the moose file
		try {
			mooseFile = new File(userDir + separator + "moose_test.yaml");
			input = new FileInputStream(mooseFile);
		} catch (FileNotFoundException e) {
			// Complain if the file is not found
			e.printStackTrace();
			// Fail, cry and go home ;)
			fail();
		}

		// Load the YAML tree
		Yaml yaml = new Yaml();
		ArrayList data = (ArrayList) yaml.load(input);
		Map dataMap = (Map) data.get(0);

		// Close the input stream reading from the file.
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Dump the file for review
		// String output = yaml.dump(data);
		// System.out.println(output);

		// Check the input. The short input file only has the /Adaptivity block
		// from the input, so size = 1.
		assertEquals(42, data.size());
		// Check the name, type and description of the /Adaptivity block
		assertEquals("/Adaptivity", dataMap.get("name"));
		assertEquals("", dataMap.get("description"));
		assertEquals(null, dataMap.get("type"));

		return;
	}
}
