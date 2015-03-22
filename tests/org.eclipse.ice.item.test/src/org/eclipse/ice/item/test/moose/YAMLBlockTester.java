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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.item.utilities.moose.Block;
import org.eclipse.ice.item.utilities.moose.Parameter;
import org.eclipse.ice.item.utilities.moose.YAMLBlock;

import java.util.ArrayList;

import org.junit.Test;

/**
 * This class is responsible for testing the YAMLBlock class.
 * 
 * @author Jay Jay Billings
 */
public class YAMLBlockTester {
	/**
	 * This operation checks the ability of the YAMLBlock to write itself to a
	 * ICE TreeComposite.
	 */
	@Test
	public void checkToTreeComposite() {
		
		// Local Declarations
		String name = "Snow", description = "Snow always mirrors";
		String type = "std::string";
		Parameter param1 = new Parameter(), param2 = new Parameter();
		ArrayList<Parameter> paramList = new ArrayList<Parameter>();
		Block block = new YAMLBlock(), subBlock = new YAMLBlock();
		ArrayList<Block> blockList = new ArrayList<Block>();

		// Setup the block
		block.setName(name);
		block.setType(type);
		block.setDescription(description);
		param1.setName("Test Param 1");
		param1.setDefault("Test Param 1");
		param1.setCpp_type("Test Param 1");
		param1.setDescription("Test Param 1");
		param1.setComment("Test Param 1");
		param1.setRequired(true);
		paramList.add(param1);
		param2.setName("Test Param 2");
		param2.setDefault("Test Param 2");
		param2.setCpp_type("Test Param 2");
		param2.setDescription("Test Param 2");
		param2.setComment("Test Param 2");
		paramList.add(param2);
		block.setParameters(paramList);
		blockList.add(subBlock);
		block.setSubblocks(blockList);

		// Create a TreeComposite from the Block
		TreeComposite tree = block.toTreeComposite();

		// Check the tree
		assertEquals(name, tree.getName());
		assertEquals(description, tree.getDescription());
		assertEquals(type, block.getType());
		// Check the parameters
		assertEquals(1, tree.getNumberOfDataNodes());
		assertTrue(tree.getComponent(1) instanceof DataComponent);
		DataComponent parameters = (DataComponent) tree.getComponent(1);
		assertEquals(paramList.size(), parameters.retrieveAllEntries().size());
		// Check the subblocks. For YAML blocks they should be configured as
		// child exemplars of the TreeComposite.
		assertEquals(0, tree.getNumberOfChildren());
		assertTrue(tree.hasChildExemplars());
		assertEquals(blockList.size(), tree.getChildExemplars().size());

		return;
	}
}
