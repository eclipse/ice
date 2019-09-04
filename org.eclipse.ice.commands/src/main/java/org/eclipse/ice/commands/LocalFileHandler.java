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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class inherits from FileHandler and deals with the processing of local
 * file transfer commands
 * 
 * @author Joe Osborn
 *
 */
public class LocalFileHandler extends FileHandler {

	/**
	 * Default constructor
	 */
	public LocalFileHandler() {

	}

	public LocalFileHandler(String _source, String _destination) {
		source = _source;
		destination = _destination;

	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#move()}
	 */
	@Override
	public CommandStatus move() throws IOException {
		// Make the command
		command = new LocalMoveFileCommand(source, destination);

		// Execute and process the file transfer
		transferStatus = executeTransfer();

		// Return whether or not it succeeded
		return transferStatus;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#copy()}
	 */
	@Override
	public CommandStatus copy() throws IOException {
		// Make the command
		command = new LocalCopyFileCommand(source, destination);

		// Execute and process the file transfer
		transferStatus = executeTransfer();

		// Return whether or not it succeeded
		return transferStatus;

	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(final String file) throws IOException {

		// Get the path from the passed string
		Path path = Paths.get(file);

		// Check if the path exists or not. Symbolic links are followed
		// by default, see {@link java.nio.file.Files#exists}
		return Files.exists(path);

	}

}
