/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ice.item.ICEActionFactory;
import org.eclipse.ice.item.action.Action;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is used for testing the ICEActionFactory. 
 * 
 * @author Alex McCaskey
 *
 */
public class ICEActionFactoryTester {

	/**
	 * Reference to the factory being tested. 
	 */
	private ICEActionFactory factory;

	/**
	 * Create the factory before each test method. 
	 */
	@Before
	public void before() {
		factory = new ICEActionFactory();
	}
		
	/**
	 * Check that we can get the list of names from the 
	 * ICEActionFactory. 
	 */
	@Test
	public void checkGetNames() {
		List<String> list = Arrays.asList(factory.getAvailableActions());
		assertTrue(list.size() > 0);
		assertTrue(list.contains("Job Launch Action"));
	}
	
	/**
	 * Check that getting Actions works as expected. 
	 */
	@Test
	public void checkGetAction() {
		Action action = factory.getAction("Bad Name");
		assertNull(action);
		action = factory.getAction("Job Launch Action");
		assertNotNull(action);
	}
}
