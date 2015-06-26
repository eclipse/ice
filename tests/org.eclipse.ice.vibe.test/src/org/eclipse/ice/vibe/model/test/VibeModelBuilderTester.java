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
package org.eclipse.ice.vibe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.vibe.model.VibeModel;
import org.eclipse.ice.vibe.model.VibeModelBuilder;
import org.junit.Test;

/**
 * <p>
 * This class tests the VibeModelBuilder.
 * </p>
 */
public class VibeModelBuilderTester {
	/**
	 * <p>
	 * This operation checks the VibeModelBuilder.getItemName().
	 * </p>
	 */
	@Test
	public void checkGetItemName() {
		VibeModelBuilder vibeBuilder = new VibeModelBuilder();
		assertEquals("VIBE Model", vibeBuilder.getItemName());
	}

	/**
	 * <p>
	 * This operation checks the VibeModelBuilder.build(). This should check
	 * to see if a form is created, but it should not check the contents of that
	 * form. That is checked in a lower level tester.
	 * </p>
	 */
	@Test
	public void checkBuild() {
		// Local declarations
		VibeModelBuilder vibeBuilder = new VibeModelBuilder();
		VibeModel vibeModel = null;

		// Builder can be passed a null project.
		vibeModel = (VibeModel) vibeBuilder.build(null);

		// Check that the item builder name is set
		assertEquals(vibeBuilder.getItemName(),
				vibeModel.getItemBuilderName());

		// A form is created
		assertNotNull(vibeModel.getForm());
	}

	/**
	 * <p>
	 * This operation checks the VibeModelBuilder.getItemType().
	 * </p>
	 */
	@Test
	public void checkGetItemType() {
		// Local declarations
		VibeModelBuilder vibeBuilder = new VibeModelBuilder();

		// Check the Item name
		assertEquals(ItemType.Model, vibeBuilder.getItemType());
	}
}
