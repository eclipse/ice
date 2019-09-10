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

import java.util.Scanner;

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
	 * The password for establishing a connection
	 */
	private String password = "";
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
	 * Default constructor
	 */
	public ConnectionConfiguration() {
	}

	/**
	 * Constructor which takes a particular hostname. Useful for local command
	 * execution since a username/password is unnecessary.
	 * 
	 * @param hname - hostname
	 */
	public ConnectionConfiguration(String hname) {
		hostname = hname;
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
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#password}
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		password = pwd;
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
	 * Inherited function from UserInfo
	 */
	public String getPassphrase() {
		return null;
	}

	/**
	 * Inherited function from UserInfo
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Inherited function from UserInfo
	 */
	public boolean promptPassphrase(String message) {
		return false;
	}

	/**
	 * Inherited function from UserInfo that prompts for the password to be entered
	 */
	public boolean promptPassword(String message) {
		System.out.println(message);
		String pass = "";

		// Get a scanner to input the passphrase from the user
		Scanner user_input = new Scanner(System.in);
		pass = user_input.next();

		// Set the password to the input
		password = pass;

		// Close the input stream
		user_input.close();

		// Return true. If the passphrase was input incorrectly from the user, then the
		// connection will not be able to be established and the job will catch this in
		// {@link org.eclipse.ice.commands.Connection#connectionSession()}
		return true;
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

	}

	/**
	 * Inherited from UserInfo
	 */
	public String[] promptKeyboardInteractive(String arg0, String arg1, String arg2, String[] arg3, boolean[] arg4) {
		return null;
	}

}
