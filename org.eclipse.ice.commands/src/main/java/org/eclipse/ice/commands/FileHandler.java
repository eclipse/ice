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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FileHandler class is a utility class for using commands that move files.
 * The class uses source and destination designations to identify how files
 * should be handled, and it can handle both local and remote files as sources
 * and destinations. Files can be moved, copied or checked for existence.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class FileHandler {

	/**
	 * Map keys for cleaner command mapping
	 */

	private static final int LOCAL_COPY = 0;
	private static final int LOCAL_MOVE = 1;
	private static final int REMOTE_COPY = 2;
	private static final int REMOTE_MOVE = 3;

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(FileHandler.class);

	/**
	 * Default constructor
	 */
	public FileHandler() {
	}

	/**
	 * This operation moves files from the source (src) to the destination
	 * (dest). If the operation fails, an IOException will be thrown.
	 * 
	 * @param src  - source file to be moved
	 * @param dest - destination for the source file to be moved to
	 * @return Command - The command to be executed
	 * @throws IOException
	 */
	public static Command move(final String src, final String dest)
			throws IOException {

		// Just test local moving for now.

		// TODO need to determine how to differentiate local vs. remote moves
		// with just the strings and not a hostname
		boolean isLocal = true;
		// Set the command type based on whether or not the move is local
		int commandType = (isLocal) ? LOCAL_MOVE : REMOTE_MOVE;

		return getCommand(commandType, src, dest);
	}

	/**
	 * This operations copies files from the source (src) to the destination
	 * (dest). If the operation fails, an IOException will be thrown.
	 * 
	 * @param src  - source file to be copied
	 * @param dest - destination to be copied to
	 * @return Command - The actual Command to be executed
	 * @throws IOException
	 */
	public static Command copy(final String src, final String dest)
			throws IOException {

		// TODO need to determine how to differentiate local vs. remote
		// copies/moves with just the strings and not a hostname
		boolean isLocal = true;
		// Set the command type based on whether or not the move is local
		int commandType = (isLocal) ? LOCAL_COPY : REMOTE_COPY;

		return getCommand(commandType, src, dest);
	}

	/**
	 * This private operation is a command factory for internal use.
	 * 
	 * @param commandType the type of command to be executed. Must be one of the
	 *                    private member keys.
	 * @param src         the source file
	 * @param dest        the final destination
	 * @return the command to execute, either a move or copy executed locally or
	 *         remotely
	 * @throws IOException thrown if the source or destination doesn't exist
	 */
	private static Command getCommand(int commandType, String src, String dest)
			throws IOException {

		// Check to make sure the source exists
		boolean sourceExists = exists(src);

		Command command = null;
		// If destination doesn't exist, create it, then get the command
		if (sourceExists && createDirectories(dest)) {
			if (commandType == LOCAL_COPY) {
				command = new LocalCopyFileCommand(src, dest);
			} else if (commandType == LOCAL_MOVE) {
				command = new LocalMoveFileCommand(src, dest);
			} else if (commandType == REMOTE_COPY) {
				command = new RemoteCopyFileCommand(src, dest);
			} else if (commandType == REMOTE_MOVE) {
				command = new RemoteMoveFileCommand(src, dest);
			}
		} else {
			String msg = "Aborting. The source file " + src
					+ " or destination does not exist!";
			logger.error(msg);
			throw new IOException(msg);
		}

		return command;
	}

	/**
	 * This operation creates all the directories that are parents of the
	 * destination.
	 * 
	 * @param dest the destination for which parent directories should be
	 *             created
	 * @return true if the directories were created
	 * @throws IOException thrown if the dest cannot be created
	 */
	private static boolean createDirectories(String dest) throws IOException {

		boolean exists = false;
		if (exists(dest)) {
			try {
				Path destination = Paths.get(dest);
				Files.createDirectories(destination);
				// If an exception wasn't thrown, then destination now exists
				exists = true;
			} catch (IOException e) {
				logger.error(
						"Couldn't create directory for local move! Failed.");
				e.printStackTrace();
			}
		}

		return exists;
	}

	/**
	 * This operations determines whether or not the file argument exists. TODO
	 * - this only works for local files at the moment.
	 * 
	 * @param file the file for which to search
	 * @return true if the file exists, false if not
	 * @throws IOException
	 */
	public static boolean exists(final String file) throws IOException {

		// Get the path from the passed string
		Path path = Paths.get(file);

		// Check if the path exists or not. Symbolic links are followed
		// by default, see {@link java.nio.file.Files#exists}
		return Files.exists(path);
	}

}
