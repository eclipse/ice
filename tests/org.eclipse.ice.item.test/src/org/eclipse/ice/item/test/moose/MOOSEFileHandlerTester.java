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
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the MOOSEFileHandler.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MOOSEFileHandlerTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation makes sure that MOOSE data can be loaded a gold-standard
	 * YAML file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoadingFromYAML() {
		// begin-user-code

		// Local Declarations
		String separator = System.getProperty("file.separator");
		DataComponent parameters = null;
		String shortFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison_short.yaml";
		String mediumFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison_medium.yaml";
		String largeFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison.yaml";
		MOOSEFileHandler handler = new MOOSEFileHandler();

		System.out.println("MOOSEFileHandlerTester Message: "
				+ "Checking small sized file.");

		// Load the blocks for a file with only a single parent block
		ArrayList<TreeComposite> blocks = handler.loadYAML(shortFilePath);

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
		blocks = handler.loadYAML(mediumFilePath);

		for (TreeComposite block : blocks) {
			System.out.println("Block name = " + block.getName());
		}

		// Check the blocks
		assertNotNull(blocks);
		assertEquals(4, blocks.size());

		System.out.println("MOOSEFileHandlerTester Message: "
				+ "Checking large sized file.");

		// Load the blocks for a file with multiple parent blocks
		blocks = handler.loadYAML(largeFilePath);

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

		// end-user-code
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
		String pathName = System.getProperty("user.dir") + separator + "data"
				+ separator + "bison.syntax";

		// Create a new MOOSEFileHandler
		MOOSEFileHandler handler = new MOOSEFileHandler();

		// Load the action syntax
		ArrayList<String> actionSyntax = handler.loadActionSyntax(pathName);

		// Verify it loaded an ArrayList correctly
		assertNotNull(actionSyntax);
		assertEquals(21, actionSyntax.size());

		// Verify a couple of the entries
		assertEquals("Adaptivity\r", actionSyntax.get(0));
		assertEquals("AuxVariables/*/InitialCondition\r", actionSyntax.get(1));
		assertEquals("BCs/PlenumPressure\r", actionSyntax.get(4));
		assertEquals("Executioner/Adaptivity\r", actionSyntax.get(10));
		assertEquals("Executioner/TimeStepper\r", actionSyntax.get(13));
		assertEquals("GlobalParams\r", actionSyntax.get(14));
		assertEquals("Problem\r", actionSyntax.get(19));
		assertEquals("Variables/*/InitialCondition\r", actionSyntax.get(20));

		// Try loading with an invalid pathname
		actionSyntax = handler.loadActionSyntax(null);

		// Check that it didn't work
		assertNull(actionSyntax);

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation insures that the MOOSEFileHandler can create a MOOSE input
	 * file from a set of incoming TreeComposites.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkWritingInputFileFromTree() {
		// begin-user-code

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String filePath = System.getProperty("user.dir") + separator + "data"
				+ separator + "bison_short.yaml";
		String inputFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison_short.input";
		String refFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison_short.input.ref";
		File inputFile = null;
		MOOSEFileHandler handler = new MOOSEFileHandler();
		TreeComposite adaptivity = null, indicators = null;
		TreeComposite analyticalIndicator = null, fluxJumpIndicator = null;
		TreeComposite function = null, variable = null, dispX = null, powerHistory = null;

		// Load the blocks
		ArrayList<TreeComposite> blocks = handler.loadYAML(filePath);

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
		// Make the blocks active, except for fluxJumpIndicator.
		adaptivity.setActive(true);
		indicators.setActive(true);
		analyticalIndicator.setActive(true);
		fluxJumpIndicator.setActive(true);

		// Get and mark the required entries for the Adaptivity tree.
		DataComponent adaptivityData = (DataComponent) adaptivity.getDataNodes().get(0);
		adaptivity.setActiveDataNode(adaptivityData);
		adaptivityData.retrieveEntry("initial_steps").setRequired(true);
		adaptivityData.retrieveEntry("marker").setRequired(true);
		adaptivityData.retrieveEntry("steps").setRequired(true);

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
		DataComponent powerData = (DataComponent) powerHistory.getDataNodes().get(0);
		powerData.retrieveEntry("type").setRequired(true);
		powerData.retrieveEntry("scale_factor").setRequired(true);
		// Add a parameter for the data file
		Parameter dataFileParam = new Parameter();
		dataFileParam.setName("data_file");
		dataFileParam.setDefault("powerhistory.csv");
		dataFileParam.setRequired(true);
		powerData.addEntry(dataFileParam.toEntry());

		// Dump the input file
		handler.dumpInputFile(inputFilePath, blocks);

		// Check to see if the file exists
		inputFile = new File(inputFilePath);
		assertTrue(inputFile.exists());

		// Compare the input file to the reference file
		try {
			// Load the input file into a byte array
			RandomAccessFile inputFileRAF = new RandomAccessFile(inputFilePath,
					"r");
			byte[] inputBytes = new byte[(int) inputFileRAF.length()];
			inputFileRAF.read(inputBytes);
			// Convert to a string
			String inputString = new String(inputBytes);
			// Load the reference file into a byte array
			RandomAccessFile refFileRAF = new RandomAccessFile(refFilePath, "r");
			byte[] refBytes = new byte[(int) refFileRAF.length()];
			refFileRAF.read(refBytes);
			// Convert to a string
			String refString = new String(refBytes);
			// Compare the arrays
			System.out.println(inputString);
			assertEquals(refString, inputString);
			// Close everything
			inputFileRAF.close();
			refFileRAF.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		return;
		// end-user-code
	}

	/**
	 * This operation checks the MOOSEFileHandler to make sure that it can load
	 * a MOOSE GetPot file into a TreeComposite.
	 */
	@Test
	public void checkLoadingFromGetPot() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String refFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison_short.input.ref";
		String outFilePath = System.getProperty("user.dir") + separator
				+ "data" + separator + "bison_short.input.out";
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