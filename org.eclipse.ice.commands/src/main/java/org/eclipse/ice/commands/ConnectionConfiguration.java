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

import java.util.concurrent.atomic.AtomicReference;

/**
 * This class provides the complete configuration for a remote
 * {@link org.eclipse.ice.commands.Connection}.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class ConnectionConfiguration {

	/**
	 * A name given to this particular connection configuration, which can be used
	 * to identify the forthcoming connection and, for example, get it from the
	 * ConnectionManager class
	 */
	private String name = "";

	/**
	 * This is a connection authorization handler which deals with the method
	 * through which remote connections are authorized, i.e. the password
	 * manipulation. This is another method which allows users to decide how to
	 * input their password.
	 */
	private AtomicReference<ConnectionAuthorizationHandler> authorization = new AtomicReference<ConnectionAuthorizationHandler>(
			null);

	/**
	 * A boolean indicating whether or not the files/directories created on the
	 * remote host should be deleted or not. Default set to false, for obvious
	 * reasons. This will delete the working directory of a remote command on the
	 * remote host, if it is set to true.
	 */
	private boolean deleteWorkingDirectory = false;

	/**
	 * Default constructor
	 */
	public ConnectionConfiguration() {
	}

	/**
	 * A getter to access
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#name}
	 * 
	 * @return - name of the ConnectionConfiguration
	 */
	public String getName() {
		return name;
	}

	/**
	 * A setter to access
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#name}
	 * 
	 * @param - name of this ConnectionConfiguration
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for whether or not to delete the remote working directory upon
	 * completion
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#deleteWorkingDirectory}
	 * 
	 * @return - boolean indicating whether or not remote working directory should be deleted
	 * 			 upon completion
	 */
	public boolean deleteWorkingDirectory() {
		return deleteWorkingDirectory;
	}

	/**
	 * Setter for whether or not to delete the remote working directory upon
	 * completion
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#deleteWorkingDirectory}
	 * 
	 * @param deleteWorkingDirectory - boolean indicating whether or not remote working 
	 *                                 directory should be deleted upon completion
	 */
	public void deleteWorkingDirectory(boolean deleteWorkingDirectory) {
		this.deleteWorkingDirectory = deleteWorkingDirectory;
	}

	/**
	 * Setter for the authorization method, if desired. See
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#authorization}
	 * 
	 * @param authorization - authorization for this ConnectionConfiguration
	 */
	public void setAuthorization(ConnectionAuthorizationHandler authorization) {
		this.authorization = new AtomicReference<ConnectionAuthorizationHandler>(authorization);
	}

	/**
	 * Getter for the authorization method, if desired. See
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#authorization}
	 * 
	 * @param authorization - authorization for this ConnectionConfiguration
	 */
	public ConnectionAuthorizationHandler getAuthorization() {
		return authorization.get();
	}
}
