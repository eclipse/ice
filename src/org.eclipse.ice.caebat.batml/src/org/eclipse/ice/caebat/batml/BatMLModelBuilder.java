/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex McCaskey (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    Jordan Deyton (UT-Battelle, LLC.) - doc cleanup
 *******************************************************************************/
package org.eclipse.ice.caebat.batml;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class builds the BatMLModel. It inherits its operations from
 * ItemBuilder.
 * 
 * @author Scott Forest Hull II
 */
public class BatMLModelBuilder extends AbstractItemBuilder {

	/**
	 * The Constructor
	 */
	public BatMLModelBuilder() {
		setName("BatML Model");
		setType(ItemType.Model);
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		BatMLModel batML = new BatMLModel(projectSpace);
		batML.setItemBuilderName(getItemName());
		return batML;
	}

}
