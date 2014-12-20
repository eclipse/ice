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
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.io.ips.IPSReader;
import org.eclipse.ice.io.ips.IPSWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	@Test
	public void checkIPSWriter() throws MalformedURLException, IOException {

		// Local declarations
		IPSWriter writer = null;
		IPSReader reader = null;

		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "caebatTesterWorkspace" + separator
				+ "Caebat_Model";
		String outputFilePath = userDir + separator + "ips_WriterTest.conf";	
		String exampleFilePath = userDir + separator + "example_ini.conf";
		File outFile = new File(outputFilePath);
		
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		
		
		URI outputURI = null;
		URI inputURI = null;
		try {
			outputURI = new URI("file:" + outputFilePath);
			inputURI = new URI("file:" + exampleFilePath);
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		BufferedReader exampleReader = null;
		try {
			exampleReader = new BufferedReader(new FileReader(new File(
					exampleFilePath)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Create a buffered reader to access the contents of the output file
		buffer = new BufferedReader(new InputStreamReader(outputURI.toURL().openStream()));

		// Test that the output file is valid but empty
		assertNotNull(buffer);
		try {
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}

		/* --- Testing CONSTRUCTION --- */
		writer = new IPSWriter();
		assertNotNull(writer);

		/* --- Testing WRITING --- */
		// Try to write with invalid parameters
		Form fakeForm = null;
		URI fakeFile = null;

		writer.write(fakeForm, fakeFile);

		// Check that the file is still empty
		try {
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}

		// Generate valid Components to test with
		Form inputForm = null;
		reader = new IPSReader();
		inputForm = reader.read(inputURI);
		assertNotNull(inputForm);

		// Try to write with valid parameters
		writer.write(inputForm, outputURI);

		// Load up a buffered reader so we can check what came out
		try {
			buffer = new BufferedReader(new InputStreamReader(outputURI.toURL().openStream()));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Check that the file is not empty and has the correct number of lines
		int numLines = 0;
		try {
			while (buffer.readLine() != null) {
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}

		// TODO: FIX THE ASSERT TO MATCH
		assertEquals(113, numLines);

	
		// Now try reading from the test file and creating another set of
		// Components to compare to the ones read from the example file
		Form outputForm = reader.read(outputURI);

		// Compare the two sets
		ArrayList<Component> components = outputForm.getComponents();
		ArrayList<Component> testComponents = inputForm.getComponents();
		assertEquals(components.size(), testComponents.size());
		for (int i = 0; i < components.size(); i++) {
			assertTrue(components.get(i).getName()
					.equals(testComponents.get(i).getName()));
		}

		// Delete the test output file
		// Close the buffered reader
		try {
			buffer.close();
			exampleReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Remove the output file
		if (outFile.exists()) {
			outFile.delete();
		}

		return;
	}
}
