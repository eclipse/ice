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

import org.eclipse.ice.item.IActionFactory;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.model.AbstractModelBuilder;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.junit.Test;

/**
 * This class tests the Model class. Its only test is that the
 * IMaterialsDatabase is set as a service, which it checks using a
 * FakeModelBuilder that overrides setInstance to create a FakeModel that
 * returns the database. The IMaterialsDatabase is implemented by a fake as
 * well.
 * 
 * This strategy is OK because the only thing that needs to be tested is that
 * the AbstractModel.setServices() operation is correctly implemented, which is
 * not affected by overriding AbstractModelBuilder.getInstance().
 * 
 * @author Jay Jay Billings
 * 
 */
public class AbstractModelBuilderTester {

	/**
	 * Test method for
	 * {@link org.eclipse.ice.item.model.AbstractModelBuilder#setMaterialsDatabase(org.eclipse.ice.materials.IMaterialsDatabase)}
	 * .
	 */
	@Test
	public void testSetMaterialsDatabase() {
		// Create a fake database service and a fake action factory
		IMaterialsDatabase fakeService = new FakeMaterialsDatabase();
		IActionFactory factory = new FakeActionFactory();
		
		// Create a fake model and set the service
		AbstractModelBuilder builder = new FakeModelBuilder();
		builder.setActionFactory(factory);
		builder.setMaterialsDatabase(fakeService);
		FakeModel model = ((FakeModel) builder.build(null));
		
		// Make sure the database service was set
		assertNotNull(model.getMaterialsDatabaseForTest());
		assertNotNull(model.getActionFactoryForTest());
		
		return;
	}

}
