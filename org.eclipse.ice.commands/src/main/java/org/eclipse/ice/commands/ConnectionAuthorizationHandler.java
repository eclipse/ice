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
public abstract class ConnectionAuthorizationHandler {

	/**
	 * Logger for handling event messages and other information. All password
	 * authentication classes can use a single logger
	 */
	public final Logger logger = LoggerFactory.getLogger(ConnectionAuthorizationHandler.class);

	/**
	 * A string which contains the username of the connection, to be taken from the
	 * text file
	 */
	String username = null;

	/**
	 * A string which contains the hostname of the connection, to be taken from the
	 * text file
	 */
	String hostname = null;

	/**
	 * A char array which can hold a password should the user desire. It is not 
	 * recommended to use this since this stores the password in memory for some
	 * time. Rather, it is recommended to use either the 
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 * function, which retrieves the password from the user in some way, or use
	 * the private key functionality for establishing a connection as in 
	 * {@link org.eclipse.ice.commands.ConnectionManagerTest#testOpenConnectionKeyPath}.
	 * Note that in all implementations of getPassword, the password is not actually
	 * stored in this char[] and is rather obtained where it is needed to establish
	 * a connection and then immediately destroyed. 
	 */
	char[] password = null;
	
	/**
	 * This function gets a password for the command authentication. The password is
	 * returned in a char array since Strings are immutable, so it is generally ill
	 * advised to store passwords in strings.
	 * 
	 * @return - obtained password
	 */
	protected abstract char[] getPassword();

	/**
	 * This function is intended to be a "jack of all trades" function where an option
	 * can be passed that may be specific to a particular subclass. For example, for
	 * text file authorization, a path to the text file can be passed as specified
	 * by the subclass. The purpose of this function is for ease of setting things
	 * in the factory method.
	 * 
	 * @param option
	 */
	public abstract void setOption(String option);
	
	/**
	 * Getter for authorization hostname
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#hostname}
	 * 
	 * @return
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Getter for authorization hostname
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#username}
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for authorization hostname
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#hostname}
	 * 
	 * @param
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Setter for authorization hostname
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#username}
	 * 
	 * @param
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Setter for {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#password}
	 * Please see the associated comment to this member variable before using this function.
	 * It is highly recommended that after a connection is established, this function is 
	 * called again to remove the password information from memory. For example:
	 * ```java
	 * ConnectionAuthorizationHandler handler = new BasicConnectionAuthorizationHandler();
	 * handler.setPassword("password".toCharArray());
	 * // Now establish some connection with the ConnectionManager
	 * (insert code to establish connection)
	 * // Now erase the password from memory
	 * handler.setPassword("".toCharArray());
	 * ```
	 * @param password
	 */
	public void setPassword(char[] password) {
		this.password = password;
	}
	
}
