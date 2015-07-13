/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.item.test;

import static org.junit.Assert.*;

import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.IActionFactory;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.junit.Test;

/**
 * This class is responsible for testing the AbstractItemBuilder.
 * 
 * It use a simple subclass of AbstractItemBuilder that provides the internal
 * state and makes sure 1.) that the base class can return it correctly and 2.)
 * that the base class can construct an Item correctly.
 * 
 * @author Jay Jay Billings
 * 
 */
public class AbstractItemBuilderTester {

	/**
	 * Test method for
	 * {@link org.eclipse.ice.item.AbstractItemBuilder#getItemName()}.
	 */
	@Test
	public void testGetItemName() {

		// Local Declarations
		String name = "test name";
		ItemType type = ItemType.AnalysisSession;

		// Create a FakeItemBuilder
		FakeItemBuilder builder = new FakeItemBuilder();
		builder.setNameForTest(name);
		builder.setTypeForTest(type);

		// Check them. Again, this is to make sure that the *getters* and the
		// (internal) setters work, not that FakeItemBuilder behaves.
		assertEquals(name, builder.getItemName());
		assertEquals(type, builder.getItemType());

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.item.AbstractItemBuilder#build(org.eclipse.core.resources.IProject)}
	 * .
	 */
	@Test
	public void testBuild() {

		// Local Declarations
		String name = "test name";
		ItemType type = ItemType.AnalysisSession;
		IActionFactory fakeFactory = new FakeActionFactory();

		// Create a FakeItemBuilder
		FakeItemBuilder builder = new FakeItemBuilder();
		builder.setNameForTest(name);
		builder.setTypeForTest(type);

		// Set the Fake Action Factory Service
		builder.setActionFactory(fakeFactory);

		// Do the build
		Item item = builder.build(null);

		// Check the fake
		IActionFactory returnedFactory = ((TestJobLauncher) item).getActionFactoryForTest();
		assertNotNull(returnedFactory);
		
		// Check that setupFormWithServices was called
		assertTrue(((TestJobLauncher) item).setupFormWithServicesWasCalled());

	}

}
