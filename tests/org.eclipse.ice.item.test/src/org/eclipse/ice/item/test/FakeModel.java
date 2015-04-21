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
import org.eclipse.ice.item.IActionFactory;
import org.eclipse.ice.item.model.Model;
import org.eclipse.ice.materials.IMaterialsDatabase;

/**
 * This is a fake subclass of Model that returns the IMaterialsDatabase so it
 * can be used to verify that the AbstractModelBuilder set the service
 * correctly.
 * 
 * @author Jay Jay Billings
 * 
 */
public class FakeModel extends Model {

	/**
	 * The Constructor
	 * @param projectSpace
	 */
	public FakeModel(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 * This operation returns the materials database
	 * @return the materials database
	 */
	public IMaterialsDatabase getMaterialsDatabaseForTest() {
		return getMaterialsDatabase();
	}

	/**
	 * This operation returns the action factory
	 * @return the action factory
	 */
	public IActionFactory getActionFactoryForTest() {
		return getActionFactory();
	}
	
}
