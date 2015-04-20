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

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.model.AbstractModelBuilder;

/**
 * This is a fake model builder that provides a fake model class that will
 * return the service reference to the materials database and the action factory
 * to make sure the overridden AbstractModelBuilder.setServices works.
 * 
 * @author Jay Jay Billings
 * 
 */
public class FakeModelBuilder extends AbstractModelBuilder {
	
	/**
	 * The constructor
	 */
	public FakeModelBuilder() {
		setName("Eyes on Fire");
		setType(ItemType.AnalysisSession);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	protected Item getInstance(IProject projectSpace) {
		return new FakeModel(projectSpace);
	}
}
