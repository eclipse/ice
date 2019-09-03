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
public abstract class FileHandler {

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
	protected static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

	/**
	 * The command member variable that will actually execute the move or copy that
	 * was requested by the user
	 */
	Command command;

	/**
	 * A string that contains the path for the source file to be transfered
	 */
	String source;

	/**
	 * A string that contains the path for the destination for the source to be
	 * transferred to
	 */
	String destination;

	/**
	 * The ConnectionConfiguration associated with the source file
	 */
	ConnectionConfiguration sourceConfiguration;

	/**
	 * The ConnectionConfiguration associated with the destination file
	 */
	ConnectionConfiguration destinationConfiguration;

	/**
	 * Default constructor
	 */
	public FileHandler() {
	}



	/**
	 * This operation moves files from the source (src) to the destination (dest).
	 * If the operation fails, an IOException will be thrown.
	 * 
	 * @return Command - The command to be executed
	 * @throws IOException
	 */
	public abstract CommandStatus move() throws IOException;

	/**
	 * This operations copies files from the source (src) to the destination (dest).
	 * If the operation fails, an IOException will be thrown.
	 * 
	 * @param src  - source file to be copied
	 * @param dest - destination to be copied to
	 * @return Command - The actual Command to be executed
	 * @throws IOException
	 */
	public abstract CommandStatus copy() throws IOException;

	/**
	 * This function gets and returns the private member variable command of type
	 * Command
	 * 
	 * @return Command - the command associated with this FileHandler
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * This operation creates all the directories that are parents of the
	 * destination.
	 * 
	 * @param dest the destination for which parent directories should be created
	 * @return true if the directories were created
	 * @throws IOException thrown if the dest cannot be created
	 */
	protected boolean createDirectories(String dest) throws IOException {

		boolean exists = false;
		if (exists(dest)) {
			try {
				Path destination = Paths.get(dest);
				Files.createDirectories(destination);
				// If an exception wasn't thrown, then destination now exists
				exists = true;
			} catch (IOException e) {
				logger.error("Couldn't create directory for local move! Failed.");
				e.printStackTrace();
			}
		}

		return exists;
	}

	/**
	 * This operations determines whether or not the file argument exists. TODO -
	 * this only works for local files at the moment.
	 * 
	 * @param file the file for which to search
	 * @return true if the file exists, false if not
	 * @throws IOException
	 */
	public boolean exists(final String file) throws IOException {

		// Get the path from the passed string
		Path path = Paths.get(file);

		// Check if the path exists or not. Symbolic links are followed
		// by default, see {@link java.nio.file.Files#exists}
		return Files.exists(path);
	}

	/**
	 * This function returns the current status of the copy or move, as it is given
	 * by the member variable {@link org.eclipse.ice.commands.FileHandler#command}
	 * 
	 * @return - CommandStatus indicating the status of the file transfer
	 */
	public CommandStatus getStatus() {
		return command.getStatus();
	}

}
