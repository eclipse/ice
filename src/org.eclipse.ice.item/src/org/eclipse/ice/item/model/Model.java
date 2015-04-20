/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.model;

import org.eclipse.ice.item.Item;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.core.resources.IProject;

/**
 * A Model in ICE contains all of the information necessary to describe a
 * physical system that will be simulated.
 * 
 * @author Jay Jay Billings
 */
public class Model extends Item {

	/**
	 * The materials database that is available to the Model Item and provided,
	 * usually, by the OSGi Declarative Services Framework.
	 */
	private IMaterialsDatabase materialsDatabase;

	/**
	 * The constructor.
	 * 
	 * @param projectSpace
	 *            The Eclipse project where the Item should store files and from
	 *            which they should be retrieved.
	 */
	public Model(IProject projectSpace) {
		// Call the super class constructor
		super(projectSpace);
	}

	/**
	 * This operations sets the materials database service that should be used
	 * by the Model.
	 * 
	 * @param materialDatabase
	 *            The database
	 */
	public void setMaterialsDatabase(IMaterialsDatabase database) {
		System.out.println("Model Message: IMaterialsDatabase Registered!");
		materialsDatabase = database;
	}

	/**
	 * This operation returns the materials database available to the Model
	 * without exposing the handle in a protected or public way that would allow
	 * it to be modified.
	 * 
	 * @return the database
	 */
	protected IMaterialsDatabase getMaterialsDatabase() {
		return materialsDatabase;
	}

}