/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jay Jay Billings (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *
 *******************************************************************************/
/**
 *
 */
package org.eclipse.ice.materials.ui;

import org.eclipse.ice.materials.IMaterialsDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple class that catches the active Materials Database service and
 * holds the reference for the Materials UI. It is a singleton, which is not
 * ideal but in this instance convenient.
 *
 * This class has no tests because it is a simple bean.
 *
 * @author Jay Jay Billings
 *
 */
public class MaterialsDatabaseServiceHolder {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MaterialsDatabaseServiceHolder.class);

	/**
	 * The reference to the IMaterialsDatabase service.
	 */
	private static IMaterialsDatabase materialsDB;

	/**
	 * This operation sets the reference to the service.
	 *
	 * @param database
	 *            The database service
	 */
	public void set(IMaterialsDatabase database) {
		materialsDB = database;
		logger.info("MaterialsDatabaseServiceHolder Message: "
				+ "Service Handle Received!");
	}

	/**
	 * This operation retrieves the reference to the service.
	 *
	 * @return The database service
	 */
	public IMaterialsDatabase get() {
		return materialsDB;
	}

}
