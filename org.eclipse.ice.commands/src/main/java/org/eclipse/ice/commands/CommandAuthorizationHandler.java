/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a super class for the password authorization and authentication
 * within the commands package. It lays the foundation for the various password
 * authentication methods that one may use when executing e.g. remote commands.
 * 
 * @author Joe Osborn
 *
 */
public abstract interface CommandAuthorizationHandler {

	/**
	 * Logger for handling event messages and other information. All password 
	 * authentication classes can use a single logger
	 */
	public final Logger logger = LoggerFactory.getLogger(CommandAuthorizationHandler.class);

	
	/**
	 * This function gets a password for the command authentication. The password
	 * is returned in a char array since Strings are immutable, so it is generally
	 * ill advised to store passwords in strings.
	 * @return - obtained password
	 */
	public abstract char[] getPassword();
	
	

}
