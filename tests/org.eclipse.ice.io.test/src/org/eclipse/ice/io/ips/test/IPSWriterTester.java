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
package org.eclipse.ice.io.ips.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.io.ips.IPSReader;
import org.eclipse.ice.io.ips.IPSWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


/**
 * Tests the methods of the IPSWriter class.
 * 
 * @author bzq
 *
 */
public class IPSWriterTester {

	/**
	 * Tests the IPSWriter
	 */
	@Test
	public void checkIPSWriter() {
		
		// Local declarations
		IPSWriter writer = null;
		IPSReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "caebatTesterWorkspace" 
				+ separator + "Caebat_Model";
		String outputFilePath = userDir + separator + "ips_WriterTest.conf";
		String exampleFilePath = userDir + separator + "example_ini.conf";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: ips_WriterTester.conf");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: ips_WriterTester.conf");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: ips_WriterTester.conf");
			}
			try {
				emptyFileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to close FileOutputStream");
			}
		}
		
		// Create a buffered reader to access the contents of the output file
		try {
			fileReader = new FileReader(outputFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			fail("Failed to create FileReader for output file: ips_WriterTester.conf");
		}
		buffer = new BufferedReader(fileReader);
		
		// Test that the output file is valid but empty
		assertNotNull(outputFile);
		assertTrue(outputFile.isFile());
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		/* --- Testing CONSTRUCTION --- */
		writer = new IPSWriter();
		assertNotNull(writer);
		
		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		/* --- Testing WRITING --- */
		// Try to write with invalid parameters
		ArrayList<Component> components = null;
		File fakeFile = null;

		try {
			writer.writeINIFile(components, fakeFile);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to write with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components to test with
		reader = new IPSReader();
		try {
			components = reader.loadINIFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find example_ini.conf file for reading");
		} catch (IOException e2) {
			fail("Failed to read from example_ini.conf file");
			e2.printStackTrace();
		}
		assertNotNull(components);
		
		// Try to write with valid parameters
		try {
			writer.writeINIFile(components, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to write ips_WriterTest.conf with valid parameters");
		}
		
		// Check that the file is not empty and has the correct number of lines
		try {
			fileReader = new FileReader(outputFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			fail("Failed to create FileReader for output file: ips_WriterTester.conf");
		}
		buffer = new BufferedReader(fileReader);
		int numLines = 0;
		try {
			while (buffer.readLine() != null) {
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		//TODO: FIX THE ASSERT TO MATCH
		assertEquals(113, numLines);
		
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file and creating another set of 
		// Components to compare to the ones read from the example file
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadINIFile(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find ips_WriterTest.conf file for reading.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from ips_WriterTest.conf file.");
		}
		
		// Compare the two sets
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).getName().equals(testComponents.get(i).getName()));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}
}
