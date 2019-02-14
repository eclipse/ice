/******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.tests.data;

import static org.junit.Assert.*;

import org.apache.jena.rdf.model.Model;
import org.eclipse.ice.data.ComponentBuilder;
import org.eclipse.ice.data.ICEModelFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the ModelFactory used to build ICE's data structures.
 * 
 * @author Jay Jay Billings
 *
 */
public class ICEModelFactoryTest {
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Test method for {@link org.eclipse.ice.data.ICEModelFactory#createModel()}.
	 */
	@Test
	public void testCreateModel() {
		ICEModelFactory modelFactory = new ICEModelFactory();
		Model model = modelFactory.createModel();
		assertNotNull(model);
		
		return;
	}

	/**
	 * Test method for {@link org.eclipse.ice.data.ICEModelFactory#createComponent()}.
	 */
	@Test
	public void testCreateComponent() {
		ICEModelFactory modelFactory = new ICEModelFactory();
		ComponentBuilder builder = modelFactory.createComponent();
		assertNotNull(builder);
	}

}
