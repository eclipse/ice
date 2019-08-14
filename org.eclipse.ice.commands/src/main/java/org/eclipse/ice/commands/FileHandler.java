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

import java.io.IOException;

/**
 * The FileHandler class is a utility class for using commands that move files.
 * The class uses source and destination designations to identify how files
 * should be handled, and it can handle both local and remote files as sources
 * and destinations. Files can be moved, copied or checked for existence.
 * 
 * @author Jay Jay Billings
 *
 */
public class FileHandler {

	/**
	 * Constructor
	 */
	public FileHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This operations moves files from the source (src) to the destination
	 * (dest). If the operation fails, an IOException will be thrown.
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void move(final String src, final String dest) throws IOException {

	}

	/**
	 * This operations copies files from the source (src) to the destination
	 * (dest). If the operation fails, an IOException will be thrown.
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void copy(final String src, final String dest) throws IOException {

	}

	/**
	 * This operations determines whether or not the file argument exists.
	 * 
	 * @param file the file for which to search
	 * @return true if the file exists, false if not
	 * @throws IOException
	 */
	public boolean exists(final String file) throws IOException {
		return false;
	}

}
