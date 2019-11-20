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

/**
 * This class is intended to offer the possibility to establish a connection via
 * a basic authorization process, wherein one just obtains the username,
 * hostname, and password from the user. It is intended to be a concrete
 * implementation of ConnectionAuthorizationHandler in its most basic form. It
 * is recommended to use alternative authorization handlers to establish a
 * connection, as this is the only one where the password is stored in memory
 * for some time period. See comments in
 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#password} and
 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#setPassword(char[])}
 * 
 * @author Joe Osborn
 *
 */
public class BasicConnectionAuthorizationHandler extends ConnectionAuthorizationHandler {

	/**
	 * Default constructor
	 */
	public BasicConnectionAuthorizationHandler() {
	}

	/**
	 * See {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 * and the above comments on use of this class
	 */
	@Override
	protected char[] getPassword() {
		return password;
	}

	/**
	 * No options required for console authorization
	 * See {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#setOption(String)}
	 */
	@Override
	public void setOption(String option) {
		return;
	}
	

	
}
