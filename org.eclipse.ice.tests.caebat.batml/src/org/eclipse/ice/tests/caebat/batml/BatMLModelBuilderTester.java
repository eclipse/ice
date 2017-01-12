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
package org.eclipse.ice.tests.caebat.batml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.caebat.batml.BatMLModel;
import org.eclipse.ice.caebat.batml.BatMLModelBuilder;
import org.eclipse.ice.item.ItemType;
import org.junit.Test;

/**
 * <p>
 * This class tests the CaebatModelBuilder.
 * </p>
 */
public class BatMLModelBuilderTester {
	/**
	 * <p>
	 * This operation checks the CaebatModelBuilder.getItemName().
	 * </p>
	 * 
	 */
	@Test
	public void checkGetItemName() {


		// Local declarations
		BatMLModelBuilder batMLBuilder = new BatMLModelBuilder();

		// Check the Item name
		assertEquals("BatML Model", batMLBuilder.getItemName());


	}

	/**
	 * <p>
	 * This operation checks the BatMLModelBuilder.build(). This should check
	 * to see if a form is created, but it should not check the contents of that
	 * form. That is checked in a lower level tester.
	 * </p>
	 */
	@Test
	public void checkBuild() {


		// Local declarations
		BatMLModelBuilder batMLBuilder = new BatMLModelBuilder();
		BatMLModel batMLModel = null;

		// Builder can be passed a null project.
		batMLModel = (BatMLModel) batMLBuilder.build(null);

		// Check that the item builder name is set
		assertEquals(batMLBuilder.getItemName(),
				batMLModel.getItemBuilderName());

		// Just make sure the Form is created. Its contents will be checked later.
		assertNotNull(batMLModel.getForm());
		

	}

	/**
	 * <p>
	 * This operation checks the CaebatModelBuilder.getItemType().
	 * </p>
	 */
	@Test
	public void checkGetItemType() {


		// Local declarations
		BatMLModelBuilder batMLBuilder = new BatMLModelBuilder();

		// Check the Item name
		assertEquals(ItemType.Model, batMLBuilder.getItemType());


	}
}
