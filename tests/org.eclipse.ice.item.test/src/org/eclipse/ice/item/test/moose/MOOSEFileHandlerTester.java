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
package org.eclipse.ice.item.test.moose;

import static org.junit.Assert.*;

import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;
import org.eclipse.ice.item.utilities.moose.Parameter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.junit.Test;

/**
 * This class tests the MOOSEFileHandler.
 * 
 * @author Jay Jay Billings
 */
public class MOOSEFileHandlerTester {

	/*
	 * FIXME Some of the tests here do byte comparisons between a reference file
	 * and an output file. This will cause failures (due to line endings adding
	 * or removing bytes) if the reference file was created on a different
	 * operating system! So either the test methods need to be redesigned, or we
	 * need to be extra careful about passing around the test data to developers
	 * on other operating systems.
	 */

	/**
	 * This operation makes sure that MOOSE data can be loaded a gold-standard
	 * YAML file.
	 */
	@Test
	public void checkLoadingFromYAML() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		DataComponent parameters = null;
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";
		String shortFilePath = userDir + separator + "bison_short.yaml";
		String mediumFilePath = userDir + separator + "bison_medium.yaml";
		String largeFilePath = userDir + separator + "bison.yaml";
		MOOSEFileHandler handler = new MOOSEFileHandler();

		System.out.println("MOOSEFileHandlerTester Message: "
				+ "Checking small sized file.");

		// Load the blocks for a file with only a single parent block
		ArrayList<TreeComposite> blocks = null;
		try {
			blocks = handler.loadYAML(shortFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check the blocks. This file should only have one block.
		assertNotNull(blocks);
		assertEquals(3, blocks.size());
		assertEquals("Adaptivity", blocks.get(0).getName());
		// But that block should have several parameters
		parameters = (DataComponent) blocks.get(0).getComponent(1);
		assertNotNull(parameters);
		assertEquals(3, parameters.retrieveAllEntries().size());
		// And it should also have a couple of exemplar children
		assertTrue(blocks.get(0).hasChildExemplars());
		assertEquals(2, blocks.get(0).getChildExemplars().size());

		System.out.println("MOOSEFileHandlerTester Message: "
				+ "Checking medium sized file.");

		// Load the blocks for a file with multiple parent blocks
		try {
			blocks = handler.loadYAML(mediumFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (TreeComposite block : blocks) {
			System.out.println("Block name = " + block.getName());
		}

		// Check the blocks
		assertNotNull(blocks);
		assertEquals(4, blocks.size());

		System.out.println("MOOSEFileHandlerTester Message: "
				+ "Checking large sized file.");

		// Load the blocks for a file with multiple parent blocks
		try {
			blocks = handler.loadYAML(largeFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (TreeComposite block : blocks) {
			System.out.println("Block name = " + block.getName());
		}

		// Check the blocks
		assertNotNull(blocks);
		assertEquals(34, blocks.size());

		// Verify blocks 17 and 23 are actually AdaptiveTreeComposites
		assertTrue(blocks.get(17) instanceof AdaptiveTreeComposite);
		assertTrue(blocks.get(23) instanceof AdaptiveTreeComposite);

		return;

	}

	/**
	 * This method is responsible for checking that action syntax file is
	 * correctly loaded.
	 * 
	 * @throws IOException
	 */
	@Test
	public void checkLoadingActionSyntax() throws IOException {

		// Local declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";
		String pathName = userDir + separator + "bison.syntax";

		// Create a new MOOSEFileHandler
		MOOSEFileHandler handler = new MOOSEFileHandler();

		// Load the action syntax
		ArrayList<String> actionSyntax = handler.loadActionSyntax(pathName);

		// Verify it loaded an ArrayList correctly
		assertNotNull(actionSyntax);
		assertEquals(21, actionSyntax.size());

		// Verify a couple of the entries
		assertEquals("Adaptivity", actionSyntax.get(0));
		assertEquals("AuxVariables/*/InitialCondition", actionSyntax.get(1));
		assertEquals("BCs/PlenumPressure", actionSyntax.get(4));
		assertEquals("Executioner/Adaptivity", actionSyntax.get(10));
		assertEquals("Executioner/TimeStepper", actionSyntax.get(13));
		assertEquals("GlobalParams", actionSyntax.get(14));
		assertEquals("Problem", actionSyntax.get(19));
		assertEquals("Variables/*/InitialCondition", actionSyntax.get(20));

		// Try loading with an invalid pathname
		actionSyntax = handler.loadActionSyntax(null);

		// Check that it didn't work
		assertNull(actionSyntax);

		return;
	}

	/**
	 * This operation ensures that the MOOSEFileHandler can create a MOOSE input
	 * file from a set of incoming TreeComposites.
	 */
	@Test
	public void checkWritingInputFileFromTree() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";
		String filePath = userDir + separator + "bison_short.yaml";
		String inputFilePath = userDir + separator + "bison_short.input";
		String outputFilePath = userDir + separator + "bison_short.output";
		String refFilePath = userDir + separator + "bison_short.input.ref";
		File outputFile = null;
		MOOSEFileHandler handler = new MOOSEFileHandler();
		TreeComposite adaptivity = null, indicators = null;
		TreeComposite analyticalIndicator = null, fluxJumpIndicator = null;
		TreeComposite function = null, variable = null, dispX = null, powerHistory = null;

		// Load the blocks
		ArrayList<TreeComposite> blocks = null;
		try {
			blocks = handler.loadYAML(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Create an analytical indicator
		adaptivity = blocks.get(0);
		indicators = (TreeComposite) adaptivity.getChildExemplars().get(0)
				.clone();
		analyticalIndicator = (TreeComposite) indicators.getChildExemplars()
				.get(1).clone();
		fluxJumpIndicator = (TreeComposite) indicators.getChildExemplars()
				.get(2).clone();
		// Add these to the block. It *MUST* be done in this order.
		adaptivity.setNextChild(indicators);
		indicators.setNextChild(analyticalIndicator);
		analyticalIndicator.setActive(true);
		indicators.setNextChild(fluxJumpIndicator);
		// Make the blocks active
		adaptivity.setActive(true);
		indicators.setActive(true);
		analyticalIndicator.setActive(true);
		fluxJumpIndicator.setActive(true);

		// Get and mark the enabled entries for the Adaptivity tree.
		DataComponent adaptivityData = (DataComponent) adaptivity
				.getDataNodes().get(0);
		adaptivity.setActiveDataNode(adaptivityData);
		adaptivityData.retrieveEntry("initial_steps").setTag("true");
		adaptivityData.retrieveEntry("marker").setTag("true");
		adaptivityData.retrieveEntry("steps").setTag("true");

		// Get and set entries for the Indicators subblocks
		DataComponent analyticalIndicatorsComponent = (DataComponent) analyticalIndicator
				.getDataNodes().get(0);
		analyticalIndicatorsComponent.retrieveEntry("block").setTag("false");
		analyticalIndicatorsComponent.retrieveEntry("type").setTag("true");
		// "function" and "variable" are marked required by the YAML file
		DataComponent fluxJumpIndicatorComponent = (DataComponent) fluxJumpIndicator
				.getDataNodes().get(0);
		fluxJumpIndicatorComponent.retrieveEntry("block").setTag("false");
		fluxJumpIndicatorComponent.retrieveEntry("scale_by_flux_faces").setTag(
				"false");
		fluxJumpIndicatorComponent.retrieveEntry("type").setTag("true");
		// "property" and "variable" are marked required by the YAML file

		// Create a variable. Variable does not have any exemplars in this
		// example, so we can just create a tree for this test.
		variable = (TreeComposite) blocks.get(2);
		dispX = new TreeComposite();
		dispX.setName("disp_x");
		variable.setNextChild(dispX);
		// Activate both nodes
		dispX.setActive(true);
		variable.setActive(true);

		// Create a function
		function = (TreeComposite) blocks.get(1);
		powerHistory = (TreeComposite) function.getChildExemplars().get(0)
				.clone();
		// Add the exemplar clone back into the function. Again it *MUST* be
		// done this way.
		function.setNextChild(powerHistory);
		// Configure the power history
		powerHistory.setName("power_history");
		// Activate both nodes
		function.setActive(true);
		powerHistory.setActive(true);

		// Configure the power history tree's data
		DataComponent powerData = (DataComponent) powerHistory.getDataNodes()
				.get(0);
		powerData.retrieveEntry("type").setTag("true");
		powerData.retrieveEntry("scale_factor").setTag("true");
		// Add a parameter for the data file
		Parameter dataFileParam = new Parameter();
		dataFileParam.setName("data_file");
		dataFileParam.setDefault("powerhistory.csv");
		dataFileParam.setEnabled(true);
		powerData.addEntry(dataFileParam.toEntry());

		// Dump the input file
		handler.dumpInputFile(outputFilePath, blocks);

		// Check to see if the file(s) exists
		outputFile = new File(outputFilePath);
		assertTrue(outputFile.exists());

		// Compare the input file to the reference file
		int firstHash, lastHash;
		try {
			// Load the input file into a byte array
			RandomAccessFile inputFileRAF = new RandomAccessFile(inputFilePath,
					"r");
			byte[] inputBytes = new byte[(int) inputFileRAF.length()];
			inputFileRAF.read(inputBytes);
			// Convert to a string
			String inputString = new String(inputBytes);

			// Chop off the comments at the end of each line. The parameter
			// descriptions from the YAML file are appended as comments via the
			// process loading YAML through the MOOSEFileHandler. But this is
			// an additional feature, and these comments aren't found in the
			// original reference file, so we must remove them for testing
			String[] inputArray = inputString.split("\\n+");
			inputString = "";
			boolean hasComment = false;
			for (String line : inputArray) {
				hasComment = !(line.lastIndexOf(" # ") == -1);
				if (hasComment) {

					// Figure out if it's the whole line commented out, or just
					// an in-line comment (only remove the inline)
					firstHash = line.trim().indexOf("#");
					lastHash = line.trim().lastIndexOf("#");

					// If the whole line is commented out, and has no in-line
					// comment
					if (firstHash == 0 && firstHash == lastHash) {
						// do nothing
					} else {
						// Lop off the in-line comment. Unfortunately we must
						// do this, as loading up a YAML file adds descriptions
						// as comments, so the files won't be exactly the same.
						line = line.substring(0, line.lastIndexOf(" # "));
						line = line.replaceAll("\\s+$", "");
					}

					if (line.endsWith("=")) {
						line += " ";
					}
				}

				inputString += line + "\n";
			}

			// Load the reference file into a byte array
			RandomAccessFile refFileRAF = new RandomAccessFile(refFilePath, "r");
			byte[] refBytes = new byte[(int) refFileRAF.length()];
			refFileRAF.read(refBytes);
			// Convert to a string
			String refString = new String(refBytes);

			// Compare the strings
			System.out.println(inputString);
			assertEquals(refString, inputString);
			// Close everything
			inputFileRAF.close();
			refFileRAF.close();
			outputFile.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		return;
	}

	/**
	 * This operation checks the MOOSEFileHandler to make sure that it can load
	 * a MOOSE GetPot file into a TreeComposite.
	 */
	@Test
	public void checkLoadingFromGetPot() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";
		String refFilePath = userDir + separator + "bison_short.input.ref";
		String outFilePath = userDir + separator + "bison_short.input.out";
		// Turn debugging on
		System.setProperty("DebugICE", "on");
		// Create the handler
		MOOSEFileHandler handler = new MOOSEFileHandler();

		// Load the file into a TreeComposite with the Handler
		ArrayList<TreeComposite> potTree = handler.loadFromGetPot(refFilePath);

		// Hehe... "potTree."

		// Write an output file based on the tree that was loaded
		handler.dumpInputFile(outFilePath, potTree);

		// Compare the input file to the reference file
		try {
			// Load the reference file into a byte array
			RandomAccessFile refFileRAF = new RandomAccessFile(refFilePath, "r");
			byte[] refBytes = new byte[(int) refFileRAF.length()];
			refFileRAF.read(refBytes);
			// Load the input file into a byte array
			RandomAccessFile outputFileRAF = new RandomAccessFile(outFilePath,
					"r");
			byte[] outputBytes = new byte[(int) outputFileRAF.length()];
			outputFileRAF.read(outputBytes);
			// Compare the strings
			assertEquals(new String(refBytes), new String(outputBytes));
			// Close everything
			outputFileRAF.close();
			refFileRAF.close();
			// Delete the output file
			File inputFile = new File(outFilePath);
			inputFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			// One reason that this might happen is because the tree wasn't
			// loaded from the pot file correctly.
			fail();
		}

		return;
	}
}
