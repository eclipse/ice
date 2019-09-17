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

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * This class provides the complete configuration for a remote
 * {@link org.eclipse.ice.commands.Connection}.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class ConnectionConfiguration implements UIKeyboardInteractive, UserInfo {

	/**
	 * Username to configure a particular connection
	 */
	private String username = "";

	/**
	 * The hostname on which to host the particular session, or where the
	 * RemoteCommand will eventually be run
	 */
	private String hostname = "";

	/**
	 * A name given to this particular connection configuration, which can be used
	 * to identify the forthcoming connection
	 */
	private String name = "";

	/**
	 * The password used to make the connection. Used for unit tests with the dummy
	 * ssh account only
	 */
	private String password;

	/**
	 * A string which contains the directory in which to execute the job on the
	 * remote system
	 */
	private String workingDirectory = "";

	/**
	 * A boolean indicating whether or not the files/directories created on the remote host
	 * should be deleted or not. Default set to false
	 */
	private boolean deleteWorkingDirectory = false;
	
	/**
	 * Default constructor
	 */
	public ConnectionConfiguration() {
	}

	/**
	 * Setter function for
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#username}
	 * 
	 * @param uname
	 */
	public void setUsername(String uname) {
		username = uname;
	}

	/**
	 * Setter function for
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#hostname}
	 * 
	 * @param hname
	 */
	public void setHostname(String hname) {
		hostname = hname;
	}

	/**
	 * Create getter and setter functions to access member variables
	 */

	public String getHostname() {
		return hostname;
	}

	/**
	 * A getter to access
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#name}
	 * 
	 * @return - name
	 */
	public String getName() {
		return name;
	}

	/**
	 * A setter to access
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#name}
	 * 
	 * @return - name
	 */
	public void setName(String _name) {
		name = _name;
	}

	/**
	 * Getter for the username for a connection
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for the remote working directory
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#workingDirectory}
	 * 
	 * @param dir
	 */
	public void setWorkingDirectory(String dir) {
		workingDirectory = dir;
	}

	/**
	 * Getter for the remote working directory
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#workingDirectory}
	 * 
	 * @return
	 */
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * Getter for whether or not to delete the remote working directory upon completion
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#deleteWorkingDirectory}
	 * @return
	 */
	public boolean getDeleteWorkingDirectory() {
		return deleteWorkingDirectory;
	}
	
	/**
	 * Setter for whether or not to delete the remote working directory upon completion
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#deleteWorkingDirectory}
	 * @param
	 */
	public void setDeleteWorkingDirectory(boolean _delete) {
		deleteWorkingDirectory = _delete;
	}
	
	/**
	 * Inherited function from UserInfo
	 */
	public String getPassphrase() {
		return null;
	}

	/**
	 * Inherited function from UserInfo
	 */
	public String getPassword() {
		return password.toString();
	}

	/**
	 * Setter for {@link org.eclipse.ice.commands.ConnectionConfiguration#password}
	 * Use with caution, only if you are comfortable putting your password into a
	 * String variable (which you rarely should be!!). This function is primarily
	 * for unit testing with a dummy ssh account in CI.
	 * 
	 * @param _pass
	 */
	public void setPassword(String _pass) {
		password = _pass;
	}

	/**
	 * Inherited function from UserInfo
	 */
	public boolean promptPassphrase(String message) {
		return false;
	}

	/**
	 * Inherited function from UserInfo
	 */
	public boolean promptPassword(String message) {
		return false;
	}

	/**
	 * Inherited from UserInfo
	 */
	public boolean promptYesNo(String arg0) {
		return false;
	}

	/**
	 * Inherited from UserInfo
	 */
	public void showMessage(String arg0) {
		return;
	}

	/**
	 * Inherited from UserInfo
	 */
	public String[] promptKeyboardInteractive(String arg0, String arg1, String arg2, String[] arg3, boolean[] arg4) {
		return null;
	}

}
