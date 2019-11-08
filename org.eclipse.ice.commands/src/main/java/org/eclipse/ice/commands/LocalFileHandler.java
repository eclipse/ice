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
import java.util.ArrayList;

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
	 * See {@link org.eclipse.ice.commands.IFileHandler#listFiles(String)}
	 */
	@Override
	public ArrayList<String> listFiles(String topDirectory) {
		logger.info("Searching " + topDirectory);
		// Get the local file walker which executes the file walking logic
		LocalFileBrowser walker = walkTree(topDirectory);
		
		// Return the resulting file list hash map
		return walker.getFileList();
	}

	/**
	 * See {@link org.eclipse.ice.commands.IFileHandler#listDirectories(String)}
	 */
	@Override
	public ArrayList<String> listDirectories(String topDirectory) {
		logger.info("Searching " + topDirectory);
		// Get the local file walker which executes the file walking logic
		LocalFileBrowser walker = walkTree(topDirectory);
		
		// Return the resulting directory array list
		return walker.getDirectoryList();
	}

	/**
	 * This function performs the action of walking the file tree for the file browsing 
	 * capabilities of LocalFileHandler. It returns a LocalFileWalker so that the 
	 * files or directories could be obtained
	 * @return
	 */
	private LocalFileBrowser walkTree(String topDirectory) {
		
		// Make a path variable of the topDirectory
		Path topPath = Paths.get(topDirectory);
		// Make a dummy path that just gets the return top directory from
		// Files.walkFileTree
		Path path = null;
		
		// Make a local file walker instance, which contains the logic of what to do
		// with the results from Files.walkFileTree
		LocalFileBrowser walker = new LocalFileBrowser();
		try {
			path = Files.walkFileTree(topPath, walker);
		} catch (IOException e) {
			logger.error("Unable to walk file tree at path " + topPath.toString(), e);
		}
		
		return walker;
	}
	
}
