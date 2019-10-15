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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Joe Osborn
 *
 */
public class ConsoleConnectionAuthorizationHandler extends ConnectionAuthorizationHandler {

	/**
	 * Default constructor
	 */
	public ConsoleConnectionAuthorizationHandler() {
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandler#getPassword()}
	 */
	@Override
	protected char[] getPassword() {

		String password = "";
		// Start up a console eraser class to erase characters as they are typed to the
		// console screen
		ConsoleEraser eraser = new ConsoleEraser();

		logger.info("Please enter your password: ");

		// Read in the password
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		// Start erasing the characters that are input to the console screen
		eraser.start();

		try {
			// Read in the password
			password = in.readLine();
			in.close();
		} catch (IOException e) {
			logger.error("Couldn't read the password...");
			return null;
		}

		// Stop the thread from erasing the previous character, since other output
		// is important to see
		eraser.stopErasing();
		System.out.print("\b");

		// Return the password as a char array for added safety, since strings are
		// immutable
		return password.toCharArray();
	}

}
