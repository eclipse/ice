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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class implements password connection authorization via a text file that
 * lives on the users computer. This ensures that the password is never held in
 * a String. Note that in order for the connection to work, the text file must
 * contain the username, password, and hostname and _nothing_ else. An example
 * of what the text file must look like would be: username password hostname
 * 
 * @author Joe Osborn
 *
 */
public class TxtFileConnectionAuthorizationHandler extends ConnectionAuthorizationHandler {

	/**
	 * A string which contains the path to the text file containing the password
	 * information
	 */
	private String path = "";

	/**
	 * Default constructor
	 */
	public TxtFileConnectionAuthorizationHandler() {

	}

	/**
	 * Constructor where a particular pathname to the text file is given
	 * 
	 * @param path
	 */
	public TxtFileConnectionAuthorizationHandler(String path) {
		this.path = path;
		setUsernameHostname();
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 */
	@Override
	protected char[] getPassword() {

		File credFile = new File(path);

		Scanner scanner = null;
		try {
			scanner = new Scanner(credFile);
		} catch (FileNotFoundException e) {
			logger.error("A path was given where the ssh credentials live, but that path doesn't exist!", e);
			return null;
		}
		// Skip the username since it has already been set
		username = scanner.next();
		// Get the password
		char[] pwd = scanner.next().toCharArray();

		return pwd;

	}

	/**
	 * Set the username and hostname from the constructor, so that they are
	 * immediately accessible via the getters. Important for the factory classes to
	 * determine where to run commands or file handles
	 */
	private void setUsernameHostname() {
		File credFile = new File(path);

		Scanner scanner = null;
		try {
			scanner = new Scanner(credFile);
		} catch (FileNotFoundException e) {
			logger.error("A path was given where the ssh credentials live, but that path doesn't exist!", e);
		}
		username = scanner.next();
		// Get the next line, and then set it to null so that it is picked up by the
		// garbage collector and erased
		char[] password = scanner.next().toCharArray();
		// Erase contents of pwd and fill with null
		Arrays.fill(password, Character.MIN_VALUE);

		hostname = scanner.next();

	}

	/**
	 * Getter for
	 * {@link org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler#path}
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Setter for
	 * {@link org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler#path}
	 * 
	 * @param
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
