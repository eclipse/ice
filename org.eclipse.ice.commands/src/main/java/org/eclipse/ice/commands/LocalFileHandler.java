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

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#move()}
	 */
	@Override
	public CommandStatus move(final String source, final String destination) throws IOException {

		// Check the file existence. If they don't exist, an exception is thrown
		checkExistence(source, destination);

		// Make the command
		command = new LocalMoveFileCommand(source, destination);

		// Execute and process the file transfer
		transferStatus = executeTransfer(destination);

		// Return whether or not it succeeded
		return transferStatus;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#copy()}
	 */
	@Override
	public CommandStatus copy(final String source, final String destination) throws IOException {
		// Check the file existence. If one or both don't exist, an exception is thrown
		checkExistence(source, destination);

		// Make the command
		command = new LocalCopyFileCommand(source, destination);

		// Execute and process the file transfer
		transferStatus = executeTransfer(destination);

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

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#checkExistence(String, String)}
	 */
	@Override
	public void checkExistence(final String source, final String destination) throws IOException {
		if (!exists(source)) {
			logger.error("Source doesn't exist! Exiting.");
			throw new IOException();
		}
		// If the destination doesn't exist, make a new directory
		if (!exists(destination)) {
			// If directory can't be made, throw an exception
			if (!createDirectories(destination)) {
				logger.error("Destination doesn't exist! Exiting.");
				throw new IOException();
			}
		}
	}

}
