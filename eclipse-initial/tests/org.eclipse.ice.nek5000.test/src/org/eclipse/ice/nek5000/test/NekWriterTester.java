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
package org.eclipse.ice.nek5000.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.nek5000.NekReader;
import org.eclipse.ice.nek5000.NekWriter;
import org.eclipse.ice.nek5000.ProblemProperties;

/**
 * Tests the methods of the NekWriter class. Tests are broken down by Nek5000
 * examples.
 * 
 * @author w5q
 *
 */
public class NekWriterTester {
		
	@Test
	public void checkConj_ht() {
		
		// Local declarations
		NekWriter writer = null;
		NekReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String outputFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "conj_ht_WriterTest.rea";
		String exampleFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "conj_ht.rea";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: conj_ht_WriterTester.rea");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: conj_ht_WriterTester.rea");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: conj_ht_WriterTester.rea");
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
			fail("Failed to create FileReader for output file: conj_ht_WriterTester.rea");
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
		
		// Check the writer constructs
		writer = new NekWriter();
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
		ProblemProperties properties = null;
		try {
			writer.writeReaFile(components, fakeFile, properties);
		} catch (IOException e2) {
			e2.printStackTrace();
			fail("Failed to write conj_ht_WriterTester.rea with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components and ProblemProperties to test with
		reader = new NekReader();
		try {
			components = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find conj_ht.rea file for reading");
		} catch (IOException e2) {
			fail("Failed to read from conj_ht.rea file");
			e2.printStackTrace();
		}
		properties = new ProblemProperties(2, 64, 32, 0);
		
		// Try to write with all valid parameters
		try {
			writer.writeReaFile(components, outputFile, properties);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Failed to write conj_ht_WriterTester.rea with valid parameters");
		}
			
		// Check that the file now is not empty, and contains 745 lines
		int numLines = 0;
		try {
			while (buffer.read() != -1) {
				assertTrue(buffer.readLine() != null);
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		assertEquals(745, numLines);
				
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file just generated and creating a
		// second set of Components from that
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find conj_ht_WriterTester.rea file for reading");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from conj_ht_WriterTester.rea file");
		}
		
		// Compare the second set of Components with the first one,
		// they should be identical
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).equals(testComponents.get(i)));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}
	
	@Test
	public void checkEddy_uv() {
		// Local declarations
		NekWriter writer = null;
		NekReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String outputFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "eddy_uv_WriterTest.rea";
		String exampleFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "eddy_uv.rea";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: eddy_uv_WriterTester.rea");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: eddy_uv_WriterTester.rea");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: eddy_uv_WriterTester.rea");
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
			fail("Failed create FileReader for output file: eddy_uv_WriterTester.rea");
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
		
		// Check the writer constructs
		writer = new NekWriter();
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
		ProblemProperties properties = null;
		try {
			writer.writeReaFile(components, fakeFile, properties);
		} catch (IOException e2) {
			e2.printStackTrace();
			fail("Failed to write eddy_uv_WriterTester.rea with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components and ProblemProperties to test with
		reader = new NekReader();
		try {
			components = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find eddy_uv.rea file for reading");
		} catch (IOException e2) {
			fail("Failed to read from eddy_uv.rea file");
			e2.printStackTrace();
		}
		properties = new ProblemProperties(2, 256, 256, 0);
		
		// Try to write with all valid parameters
		try {
			writer.writeReaFile(components, outputFile, properties);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Failed to write eddy_uv_WriterTester.rea with valid parameters");
		}
			
		// Check that the file now is not empty, and contains 1961 lines
		int numLines = 0;
		try {
			while (buffer.read() != -1) {
				assertTrue(buffer.readLine() != null);
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		assertEquals(1961, numLines);
				
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file just generated and creating a
		// second set of Components from that
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find eddy_uv_WriterTester.rea file for reading");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from eddy_uv_WriterTester.rea file");
		}
		
		// Compare the second set of Components with the first one,
		// they should be identical
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).equals(testComponents.get(i)));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}
	
	@Test
	public void checkKov() {
		
		// Local declarations
		NekWriter writer = null;
		NekReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String outputFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "kov_WriterTest.rea";
		String exampleFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "kov.rea";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: kov_WriterTester.rea");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: kov_WriterTester.rea");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: kov_WriterTester.rea");
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
			fail("Failed create FileReader for output file: kov_WriterTester.rea");
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
		
		// Check the writer constructs
		writer = new NekWriter();
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
		ProblemProperties properties = null;
		try {
			writer.writeReaFile(components, fakeFile, properties);
		} catch (IOException e2) {
			e2.printStackTrace();
			fail("Failed to write kov_WriterTester.rea with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components and ProblemProperties to test with
		reader = new NekReader();
		try {
			components = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find kov.rea file for reading");
		} catch (IOException e2) {
			fail("Failed to read from kov.rea file");
			e2.printStackTrace();
		}
		properties = new ProblemProperties(2, 8, 8, 0);
		
		// Try to write with all valid parameters
		try {
			writer.writeReaFile(components, outputFile, properties);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Failed to write kov_WriterTester.rea with valid parameters");
		}
			
		// Check that the file now is not empty, and contains 241 lines
		int numLines = 0;
		try {
			while (buffer.read() != -1) {
				assertTrue(buffer.readLine() != null);
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		assertEquals(241, numLines);
				
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file just generated and creating a
		// second set of Components from that
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find kov_WriterTester.rea file for reading");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from kov_WriterTester.rea file");
		}
		
		// Compare the second set of Components with the first one,
		// they should be identical
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).equals(testComponents.get(i)));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}
	
	@Test
	public void checkRay_dd() {
		
		// Local declarations
		NekWriter writer = null;
		NekReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String outputFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "ray_dd_WriterTest.rea";
		String exampleFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "ray_dd.rea";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: ray_dd_WriterTest.rea");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: ray_dd_WriterTest.rea");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: ray_dd_WriterTest.rea");
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
			fail("Failed create FileReader for output file: ray_dd_WriterTest.rea");
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
		
		// Check the writer constructs
		writer = new NekWriter();
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
		ProblemProperties properties = null;
		try {
			writer.writeReaFile(components, fakeFile, properties);
		} catch (IOException e2) {
			e2.printStackTrace();
			fail("Failed to write ray_dd_WriterTest.rea with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components and ProblemProperties to test with
		reader = new NekReader();
		try {
			components = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find ray_dd.rea file for reading");
		} catch (IOException e2) {
			fail("Failed to read from ray_dd.rea file");
			e2.printStackTrace();
		}
		properties = new ProblemProperties(2, 3, 3, 0);
		
		// Try to write with all valid parameters
		try {
			writer.writeReaFile(components, outputFile, properties);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Failed to write ray_dd_WriterTest.rea with valid parameters");
		}
			
		// Check that the file now is not empty, and contains 203 lines
		int numLines = 0;
		try {
			while (buffer.read() != -1) {
				assertTrue(buffer.readLine() != null);
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		assertEquals(203, numLines);
				
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file just generated and creating a
		// second set of Components from that
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find ray_dd_WriterTester.rea file for reading");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from ray_dd_WriterTester.rea file");
		}
		
		// Compare the second set of Components with the first one,
		// they should be identical
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).equals(testComponents.get(i)));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}
	
	@Test
	public void checkRay_nn() {
		
		// Local declarations
		NekWriter writer = null;
		NekReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String outputFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "ray_nn_WriterTest.rea";
		String exampleFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "ray_nn.rea";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: ray_nn_WriterTest.rea");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: ray_nn_WriterTest.rea");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: ray_nn_WriterTest.rea");
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
			fail("Failed create FileReader for output file: ray_nn_WriterTest.rea");
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
		
		// Check the writer constructs
		writer = new NekWriter();
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
		ProblemProperties properties = null;
		try {
			writer.writeReaFile(components, fakeFile, properties);
		} catch (IOException e2) {
			e2.printStackTrace();
			fail("Failed to write ray_nn_WriterTest.rea with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components and ProblemProperties to test with
		reader = new NekReader();
		try {
			components = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find ray_nn.rea file for reading");
		} catch (IOException e2) {
			fail("Failed to read from ray_nn.rea file");
			e2.printStackTrace();
		}
		properties = new ProblemProperties(2, 3, 3, 0);
		
		// Try to write with all valid parameters
		try {
			writer.writeReaFile(components, outputFile, properties);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Failed to write ray_nn_WriterTest.rea with valid parameters");
		}
			
		// Check that the file now is not empty, and contains 203 lines
		int numLines = 0;
		try {
			while (buffer.read() != -1) {
				assertTrue(buffer.readLine() != null);
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		assertEquals(203, numLines);
				
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file just generated and creating a
		// second set of Components from that
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find ray_nn_WriterTester.rea file for reading");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from ray_nn_WriterTester.rea file");
		}
		
		// Compare the second set of Components with the first one,
		// they should be identical
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).equals(testComponents.get(i)));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}
	
	@Test
	public void checkV2d() {
		
		// Local declarations
		NekWriter writer = null;
		NekReader reader = null;
		
		FileReader fileReader = null;
		BufferedReader buffer = null;
		String separator = System.getProperty("file.separator");
		String outputFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "v2d_WriterTest.rea";
		String exampleFilePath = System.getProperty("user.dir") + separator 
				+ "examples" + separator + "v2d.rea";
		File outputFile = new File(outputFilePath);
		File exampleFile = new File(exampleFilePath);
		
		// If the tester output file doesn't exist, create it
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to create output file: v2d_WriterTest.rea");
			}
		}
		
		// Otherwise, if it already exists, clear its contents
		else {
			FileOutputStream emptyFileStream = null;
			try {
				emptyFileStream = new FileOutputStream(outputFile);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Failed to set FileOutputStream to output file: v2d_WriterTest.rea");
			}
			try {
				emptyFileStream.write("".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				fail("Failed to clear contents of output file: v2d_WriterTest.rea");
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
			fail("Failed create FileReader for output file: v2d_WriterTest.rea");
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
		
		// Check the writer constructs
		writer = new NekWriter();
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
		ProblemProperties properties = null;
		try {
			writer.writeReaFile(components, fakeFile, properties);
		} catch (IOException e2) {
			e2.printStackTrace();
			fail("Failed to write v2d_WriterTest.rea with invalid parameters");
		}

		// Check that the file is still empty
		try {									
			assertTrue(buffer.read() == -1);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		
		// Generate valid Components and ProblemProperties to test with
		reader = new NekReader();
		try {
			components = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			fail("Failed to find v2d.rea file for reading");
		} catch (IOException e2) {
			fail("Failed to read from v2d.rea file");
			e2.printStackTrace();
		}
		properties = new ProblemProperties(2, 20, 20, 0);
		
		// Try to write with all valid parameters
		try {
			writer.writeReaFile(components, outputFile, properties);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Failed to write v2d_WriterTest.rea with valid parameters");
		}
			
		// Check that the file now is not empty, and contains 388 lines
		int numLines = 0;
		try {
			while (buffer.read() != -1) {
				assertTrue(buffer.readLine() != null);
				numLines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read BufferedReader");
		}
		assertEquals(388, numLines);
				
		// Close the buffered reader
		try {
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to close BufferedReader");
		}
		
		// Now try reading from the test file just generated and creating a
		// second set of Components from that
		ArrayList<Component> testComponents = new ArrayList<Component>();
		try {
			testComponents = reader.loadREAFile(exampleFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Failed to find v2d_WriterTester.rea file for reading");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read from v2d_WriterTester.rea file");
		}
		
		// Compare the second set of Components with the first one,
		// they should be identical
		assertEquals(components.size(), testComponents.size());
		for ( int i = 0; i < components.size(); i++ ) {
			assertTrue(components.get(i).equals(testComponents.get(i)));
		}
		
		// Delete the test output file
		outputFile.delete();
		
		return;
	}

}
