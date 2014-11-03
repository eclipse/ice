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

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.utilities.moose.Block;
import org.eclipse.ice.item.utilities.moose.Parameter;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This operation checks the MOOSE Block class. The loadFromMap() operation is
 * checked by the MOOSEFileHandlerTester class because it requires integration
 * with SnakeYAML.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class BlockTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the accessors for the Block class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAccessors() {
		// begin-user-code

		// Local Declarations
		String name = "Snow", description = "Snow always mirrors";
		String type = "std::string";
		Parameter param1 = new Parameter(), param2 = new Parameter();
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		Block block = new Block(), subBlock = new Block();
		ArrayList<Block> blockList = new ArrayList<Block>();

		// Setup the block
		block.setName(name);
		block.setType(type);
		block.setDescription(description);
		param1.setName("Test Param 1");
		param1.setDefault("Test Param 1");
		param1.setCpp_type("Test Param 1");
		param1.setDescription("Test Param 1");
		param1.setRequired(true);
		paramList.add(param1);
		param2.setName("Test Param 2");
		param2.setDefault("Test Param 2");
		param2.setCpp_type("Test Param 2");
		param2.setDescription("Test Param 2");
		paramList.add(param2);
		block.setParameters(paramList);
		blockList.add(subBlock);
		block.setSubblocks(blockList);
		block.setActive(true);

		// Check the block
		assertEquals(name, block.getName());
		assertEquals(description, block.getDescription());
		assertEquals(type, block.getType());
		assertEquals(paramList.size(), block.getParameters().size());
		assertEquals(blockList.size(), block.getSubblocks().size());
		assertEquals(true, block.isActive());

		// Check switching the required mode off
		block.setActive(false);
		assertEquals(false, block.isActive());

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the Block to write itself to an ICE
	 * TreeComposite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkToTreeComposite() {
		// begin-user-code

		// Local Declarations
		String name = "Snow", description = "Snow always mirrors";
		String type = "std::string";
		Parameter param1 = new Parameter(), param2 = new Parameter();
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		Block block = new Block(), subBlock = new Block();
		ArrayList<Block> blockList = new ArrayList<Block>();

		// Setup the block
		block.setName(name);
		block.setType(type);
		block.setDescription(description);
		param1.setName("Test Param 1");
		param1.setDefault("Test Param 1");
		param1.setCpp_type("Test Param 1");
		param1.setDescription("Test Param 1");
		param1.setRequired(true);
		paramList.add(param1);
		param2.setName("Test Param 2");
		param2.setDefault("Test Param 2");
		param2.setCpp_type("Test Param 2");
		param2.setDescription("Test Param 2");
		paramList.add(param2);
		block.setParameters(paramList);
		blockList.add(subBlock);
		block.setSubblocks(blockList);
		block.setActive(true);

		// Create a TreeComposite from the Block
		TreeComposite tree = block.toTreeComposite();

		// Check the tree
		assertEquals(name, tree.getName());
		assertEquals(description, tree.getDescription());
		assertEquals(true, tree.isActive());
		// Check the parameters
		assertEquals(1, tree.getNumberOfDataNodes());
		assertTrue(tree.getComponent(1) instanceof DataComponent);
		DataComponent parameters = (DataComponent) tree.getComponent(1);
		assertEquals(paramList.size(), parameters.retrieveAllEntries().size());
		// Check the subblocks
		assertEquals(blockList.size(), tree.getNumberOfChildren());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the block to write itself in a
	 * GetPot compatible string.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkToGetPot() {
		// begin-user-code

		// Local Declarations
		String name = "Snow", description = "Snow always mirrors";
		String type = "std::string";
		Parameter param1 = new Parameter(), param2 = new Parameter();
		Parameter param3 = new Parameter();
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		ArrayList<Parameter> subBlockParamList = new ArrayList<Parameter>();
		Block block = new Block(), subBlock = new Block();
		ArrayList<Block> blockList = new ArrayList<Block>();

		// Setup the block
		block.setName(name);
		block.setType(type);
		block.setDescription(description);
		param1.setName("Test Param 1");
		param1.setDefault("Test Param 1");
		param1.setCpp_type("Test Param 1");
		param1.setDescription("Test Param 1");
		param1.setRequired(true);
		paramList.add(param1);
		param2.setName("Test Param 2");
		param2.setDefault("Test Param 2");
		param2.setCpp_type("Test Param 2");
		param2.setDescription("Test Param 2");
		paramList.add(param2);
		block.setParameters(paramList);
		blockList.add(subBlock);
		block.setSubblocks(blockList);
		block.setActive(true);

		// Setup the subblock
		param3.setName("Test Param 3");
		param3.setDefault("Test Param 3");
		param3.setCpp_type("Test Param 3");
		param3.setDescription("Test Param 3");
		param3.setRequired(true);
		subBlockParamList.add(param3);
		subBlock.setParameters(subBlockParamList);
		subBlock.setActive(true);

		// Create a GetPot string from the Block and check it
		// System.out.println(block.toGetPot(null));
		String testString = "[Snow]\n" + "  Test Param 1 = Test Param 1\n"
				+ "  [./]\n" + "    Test Param 3 = Test Param 3\n"
				+ "  [../]\n" + "[]\n";
		// System.out.println(testString);
		assertEquals(testString, block.toGetPot(null));
		// Make sure that toString() works as described.
		// System.out.println(testString);
		assertEquals(testString, block.toString());

		return;
		// end-user-code
	}

	/**
	 * This operation checks the Block to make sure that it can be loaded from a
	 * GetPot-compatible string.
	 */
	@Test
	public void checkFromGetPot() {

		// Local Declarations
		String name = "Snow", description = "Snow always mirrors";
		String type = "std::string";
		Parameter param1 = new Parameter(), param2 = new Parameter();
		Parameter param3 = new Parameter();
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		ArrayList<Parameter> subBlockParamList = new ArrayList<Parameter>();
		Block block = new Block(), subBlock = new Block();
		ArrayList<Block> blockList = new ArrayList<Block>();

		// Setup the block
		block.setName(name);
		block.setType(type);
		block.setDescription(description);
		param1.setName("Test Param 1");
		param1.setDefault("Test Param 1");
		param1.setCpp_type("Test Param 1");
		param1.setDescription("Test Param 1");
		param1.setRequired(true);
		paramList.add(param1);
		param2.setName("Test Param 2");
		param2.setDefault("Test Param 2");
		param2.setCpp_type("Test Param 2");
		param2.setDescription("Test Param 2");
		param2.setRequired(true);
		paramList.add(param2);
		block.setParameters(paramList);
		blockList.add(subBlock);
		block.setSubblocks(blockList);
		block.setActive(true);

		// Setup the subblock
		param3.setName("Test Param 3");
		param3.setDefault("Test Param 3");
		param3.setCpp_type("Test Param 3");
		param3.setDescription("Test Param 3");
		param3.setRequired(true);
		subBlockParamList.add(param3);
		subBlock.setParameters(subBlockParamList);
		subBlock.setActive(true);

		// Create a block to test
		Block testBlock = new Block();
		testBlock.fromGetPot(block.toGetPot(null));
		System.out.println(block.toGetPot(null));

		// Check the block information
		assertEquals(block.getName(), testBlock.getName());

		// Grab the parameters from the test block
		ArrayList<Parameter> testParameters = testBlock.getParameters();
		assertEquals(block.getParameters().size(), testParameters.size());
		// Check the first one
		Parameter testParam = testParameters.get(0);
		assertEquals(param1.getName(), testParam.getName());
		assertEquals(param1.getDefault(), testParam.getDefault());
		// Check the second one
		testParam = testParameters.get(1);
		assertEquals(param2.getName(), testParam.getName());
		assertEquals(param2.getDefault(), testParam.getDefault());

		// Grab the subblock and check it
		ArrayList<Block> testSubBlocks = testBlock.getSubblocks();
		assertEquals(block.getSubblocks().size(), testSubBlocks.size());
		Block testSubBlock = testSubBlocks.get(0);
		assertEquals(subBlock.getName(), testSubBlock.getName());
		// Check the parameter list of the subblock
		testParameters = testSubBlock.getParameters();
		assertEquals(subBlock.getParameters().size(), testParameters.size());
		// Check the parameter
		testParam = testParameters.get(0);
		assertEquals(param3.getName(), testParam.getName());
		assertEquals(param3.getDefault(), testParam.getDefault());

		return;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Block to make sure that it can be loaded from a
	 * TreeComposite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFromTreeComposite() {
		// begin-user-code

		// Local Declarations
		String name = "Snow", description = "Snow always mirrors";
		String type = "std::string";
		Parameter param1 = new Parameter(), param2 = new Parameter();
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		Block block = new Block(), subBlock = new Block(), testBlock = null;
		ArrayList<Block> blockList = new ArrayList<Block>();

		// Setup the block
		block.setName(name);
		block.setType(type);
		block.setDescription(description);
		param1.setName("Test Param 1");
		param1.setDefault("Test Param 1");
		param1.setCpp_type(type);
		param1.setDescription("Test Param 1");
		param1.setRequired(true);
		paramList.add(param1);
		param2.setName("Test Param 2");
		param2.setDefault("Test Param 2");
		param2.setCpp_type(type);
		param2.setDescription("Test Param 2");
		paramList.add(param2);
		block.setParameters(paramList);
		blockList.add(subBlock);
		block.setSubblocks(blockList);
		block.setActive(true);

		// Create a TreeComposite from the Block
		TreeComposite tree = block.toTreeComposite();
		assertNotNull(tree);

		// Load the test block
		testBlock = new Block();
		testBlock.fromTreeComposite(tree);

		// Check the Block
		assertEquals(block.getName(), testBlock.getName());
		assertEquals(block.getDescription(), testBlock.getDescription());
		assertEquals(block.getParameters().size(), testBlock.getParameters()
				.size());
		assertEquals(block.getSubblocks().size(), testBlock.getSubblocks()
				.size());
		assertEquals(block.isActive(), testBlock.isActive());
		// System.out.println(block.toGetPot(null));

		// There's no need to check the children thoroughly. At least, not yet.

		return;
		// end-user-code
	}
}