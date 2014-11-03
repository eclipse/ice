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

import static org.junit.Assert.*;

import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.nek5000.NekModel;
import org.eclipse.ice.nek5000.NekModelBuilder;
import org.junit.Test;

/**
 * 
 */

/**
 * This class is responsible for testing the NekModelBuilder. It is a very
 * simple test.
 * 
 * @author bkj
 * 
 */
public class NekModelBuilderTester {

	/**
	 * This operation makes sure all the information on the builder is correct.
	 */
	@Test
	public void checkNekModelBuilder() {
		
		NekModelBuilder builder = new NekModelBuilder();
		
		// Check the name
		assertEquals("Nek5000 Model Builder", builder.getItemName());
		assertEquals("Nek5000 Model Builder", NekModelBuilder.name);
		// Check the type
		assertEquals(ItemType.Model, builder.getItemType());
		assertEquals(ItemType.Model, NekModelBuilder.type);
		// Check what it builds
		Item model = builder.build(null);
		assertTrue(model instanceof NekModel);
		
		return;
	}
	
}
