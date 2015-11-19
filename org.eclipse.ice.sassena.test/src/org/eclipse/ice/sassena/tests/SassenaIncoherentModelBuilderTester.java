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
package org.eclipse.ice.sassena.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.sassena.SassenaIncoherentModel;
import org.eclipse.ice.sassena.SassenaIncoherentModelBuilder;
import org.junit.Test;

/**
 * <p>
 * This class tests the SassenaIncoherentModelBuilder.
 * </p>
 */
public class SassenaIncoherentModelBuilderTester {
	/**
	 * <p>
	 * This operation checks the CaebatModelBuilder.getItemName().
	 * </p>
	 * 
	 */
	@Test
	public void checkGetItemName() {


		// Local declarations
		SassenaIncoherentModelBuilder sassenaBuilder = new SassenaIncoherentModelBuilder();

		// Check the Item name
		assertEquals("Sassena Incoherent Model", sassenaBuilder.getItemName());


	}

	/**
	 * <p>
	 * This operation checks the SassenaIncoherentModelBuilder.build(). This should check
	 * to see if a form is created, but it should not check the contents of that
	 * form. That is checked in a lower level tester.
	 * </p>
	 */
	@Test
	public void checkBuild() {


		// Local declarations
		SassenaIncoherentModelBuilder sassenaBuilder = new SassenaIncoherentModelBuilder();
		SassenaIncoherentModel sassenaModel = null;

		// Builder can be passed a null project.
		sassenaModel = (SassenaIncoherentModel) sassenaBuilder.build(null);

		// Check that the item builder name is set
		assertEquals(sassenaBuilder.getItemName(),
				sassenaModel.getItemBuilderName());

		// Just make sure the Form is created. Its contents will be checked later.
		assertNotNull(sassenaModel.getForm());
		

	}

	/**
	 * <p>
	 * This operation checks the SassenaIncohrerentModelBuilder.getItemType().
	 * </p>
	 */
	@Test
	public void checkGetItemType() {


		// Local declarations
		SassenaIncoherentModelBuilder sassenaBuilder = new SassenaIncoherentModelBuilder();

		// Check the Item name
		assertEquals(ItemType.Model, sassenaBuilder.getItemType());


	}
}
