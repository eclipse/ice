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
 * This class provides the complete configuration for a remote
 * {@link org.eclipse.ice.commands.Connection}.
 * 
 * @author Jay Jay Billings
 *
 */
public class ConnectionConfiguration {

	/**
	 * Username to configure a particular connection
	 */
	private String username = "";

	/**
	 * Password to configure a particular connection
	 */
	private String password = "";
	
	/**
	 * The hostname on which to host the particular session, or 
	 * where the RemoteCommand will eventually be run
	 */
	private String hostname = "";

	/**
	 * Default constructor
	 */
	public ConnectionConfiguration() {
	}
	
	
	/**
	 * Constructor which gives a particular username, password, and hostname
	 * @param uname - username for a connection
	 * @param pwd - password for the connection
	 * @param hname - hostname for the connection
	 */
	public ConnectionConfiguration(String uname, String pwd, String hname) {
		username = uname;
		password = pwd;
		hostname = hname;
	}

}
