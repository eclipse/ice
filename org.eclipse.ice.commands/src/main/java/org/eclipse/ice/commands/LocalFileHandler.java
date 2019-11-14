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
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#setConfiguration(String, String)}
	 */
	@Override
	protected void configureMoveCommand(final String source, final String destination) {
		LocalMoveFileCommand cmd = new LocalMoveFileCommand();
		// Set the source and destination of the command
		cmd.setConfiguration(source, destination);
		// Set the member variable
		command.set(cmd);
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#setConfiguration(String, String)}
	 */
	@Override
	protected void configureCopyCommand(final String source, final String destination) {
		LocalCopyFileCommand cmd = new LocalCopyFileCommand();
		// Set the source and destination of the command
		cmd.setConfiguration(source, destination);
		// Set the member variable
		command.set(cmd);
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(final String file) throws IOException {
		// See {@link org.eclipse.ice.commands.FileHandler#isLocal(String)}
		return isLocal(file);

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

	/**
	 * See {@link org.eclipse.ice.commands.IFileHandler#getFileBrowser()}
	 */
	@Override
	public FileBrowser getFileBrowser() {
		LocalFileBrowser browser = new LocalFileBrowser();
		return browser;
	}

}
