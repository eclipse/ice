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
 * This class handles authentication via a path for a key which contains an ssh
 * fingerprint that can establish the connection.
 * 
 * @author Joe Osborn
 *
 */
public class KeyPathConnectionAuthorizationHandler extends ConnectionAuthorizationHandler {

	/**
	 * A string that holds the path to the key used for a connection authorization
	 */
	private String keyPath = null;

	/**
	 * Default constructor
	 */
	public KeyPathConnectionAuthorizationHandler() {
	}

	/**
	 * Constructor with a given key path
	 * 
	 * @param keyPath - String corresponding to the path for the key
	 */
	public KeyPathConnectionAuthorizationHandler(String keyPath) {
		this.keyPath = keyPath;
	}

	/**
	 * Setter for
	 * {@link org.eclipse.ice.commands.KeyPathConnectionAuthorizationHandler#keyPath}
	 * 
	 * @param keyPath - String corresponding to the path for the key
	 */
	@Override
	public void setOption(String option) {
		this.keyPath = option;
	}

	/**
	 * Getter for
	 * {@link org.eclipse.ice.commands.KeyPathConnectionAuthorizationHandler#keyPath}
	 * 
	 * @return - String corresponding to the path for the key
	 */
	public String getKeyPath() {
		return keyPath;
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 * No need for implementation here since the password should not be necessary if
	 * a key has already been generated
	 */
	@Override
	protected char[] getPassword() {
		return null;
	}

}
