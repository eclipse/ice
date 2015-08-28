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
package org.eclipse.ice.proteus.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.proteus.PROTEUSModel;
import org.eclipse.ice.proteus.PROTEUSModelBuilder;
import org.junit.Test;

/**
 * Class that tests PROTEUSModelBuilder methods.
 * @author Anna Wojtowicz
 *
 */

public class PROTEUSModelBuilderTester {

	/**
	 * Tests the getItemName() and getItemType() methods.
	 * @author Anna Wojtowicz
	 * 
	 */
	@Test
	public void checkGetters() {
		
		// Create a model builder and default values to test against
		PROTEUSModelBuilder modelBuilder = new PROTEUSModelBuilder();
		String defaultName = "PROTEUS Model Builder";
		ItemType defaultType = ItemType.Model;
		
		// Check the Item name and type is set correctly
		assertEquals(defaultName, modelBuilder.getItemName());
		assertEquals(defaultType, modelBuilder.getItemType());
	}
	
	/**
	 * Tests the build() method that actually constructs a PROTEUSModel. The
	 * default model (ie. when passing a null project) should contain one 
	 * DataComponent in its Form, with no entries.
	 * @author Anna Wojtowicz
	 * 
	 */
	@Test
	public void checkBuild() {
		
		// Create a model builder and default objects to check against
		PROTEUSModelBuilder modelBuilder = new PROTEUSModelBuilder();
		PROTEUSModel model = null;
		
		// Build the model
		model = (PROTEUSModel) modelBuilder.build(null);
		
		// Check the build operation correctly created a Form
		assertNotNull(model.getForm());
		
		// Check that the default Form (ie. null project) was constructed 
		// correctly (should empty, ie. 0 DataComponents)
		assertEquals(0, model.getForm().getComponents().size());
	}
}
