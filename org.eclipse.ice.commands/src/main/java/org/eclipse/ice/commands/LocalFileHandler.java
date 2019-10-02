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
	 * See {@link org.eclipse.ice.commands.FileHandler#setConfiguration(String, String)}
	 */
	@Override
	protected void configureMoveCommand(final String source, final String destination) {
		command = new LocalMoveFileCommand();
		// Cast the Command as a LocalMoveFileCommand to set the source and destination paths
		((LocalMoveFileCommand) command).setConfiguration(source, destination);
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#setConfiguration(String, String)}
	 */
	@Override
	protected void configureCopyCommand(final String source, final String destination) {
		command = new LocalCopyFileCommand();
		// Cast the Command as a LocalCopyFileCommand to set the source and destination paths
		((LocalCopyFileCommand) command).setConfiguration(source, destination);

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
		// Check that the source file exists
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
