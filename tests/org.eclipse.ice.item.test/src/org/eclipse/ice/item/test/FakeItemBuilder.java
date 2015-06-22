/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/**
 * This class is used to build TestJobLaunchers for the MultiLauncherTester.
 * Calling the build() operation returns an instance of TestJobLauncher
 * regardless of the current type retrieved from getItemType(). The ability to
 * change the ItemType is only available to make sure the MultiLauncherBuilder
 * does not include things other than JobLaunchers.
 * 
 * @author Jay Jay Billings
 */
public class FakeItemBuilder extends AbstractItemBuilder {

	/**
	 * The constructor
	 */
	public FakeItemBuilder() {
		setName("Selina Kyle");
		setType(ItemType.Simulation);
	}
	
	/**
	 * This operation sets the name that the builder should return from
	 * getItemName();
	 * 
	 * @param itemName
	 *            The name
	 */
	public void setNameForTest(String itemName) {
		setName(itemName);
	}

	/**
	 * This operation sets the type of the FakeItemBuilder. This does not change
	 * what the build() operation returns - a TestJobLauncher - but it does
	 * change the output of getItemType().
	 * 
	 * @param itemType
	 *            The ItemType that this builder will report.
	 */
	public void setTypeForTest(ItemType itemType) {
		setType(itemType);
	}


	@Override
	protected Item getInstance(IProject projectSpace) {

		Item item = new TestJobLauncher(projectSpace);
		item.setName(getItemName());

		return item;
	}
}