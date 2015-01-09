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
package org.eclipse.ice.item;

import org.eclipse.ice.materials.IMaterialsDatabase;

/**
 * This is a subclass of AbstractItemBuilder specifically focused on adding
 * services for Models, which are Items that generate input files or data.
 * 
 * @author Jay Jay Billings
 *
 */
public class AbstractModelBuilder extends AbstractItemBuilder {

	/**
	 * This operation sets the service reference for the IMaterialsDatabase
	 * service.
	 * 
	 * @param database the service
	 */
	public void setMaterialsDatabase(IMaterialsDatabase database) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.AbstractItemBuilder#setServices(org.eclipse.ice.
	 * item.Item)
	 */
	@Override
	protected void setServices(Item item) {

	}

}
