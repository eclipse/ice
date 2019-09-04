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
public abstract class FileHandler implements IFileHandler {

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
	 * A status member variable that indicates the status of the file transfer. See
	 * also {@link org.eclipse.ice.commands.CommandStatus}
	 */
	CommandStatus transferStatus;

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
	 * This operations determines whether or not the file argument exists.
	 * 
	 * @param file the file for which to search
	 * @return true if the file exists, false if not
	 * @throws IOException
	 */
	public abstract boolean exists(final String file) throws IOException;

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
		if (!exists(dest)) {
			try {
				Path destination = Paths.get(dest);
				Files.createDirectories(destination);
				// If an exception wasn't thrown, then destination now exists
				exists = exists(dest);
			} catch (IOException e) {
				logger.error("Couldn't create directory for local move! Failed.");
				e.printStackTrace();
			}
		}

		return exists;
	}

	protected CommandStatus executeTransfer() {
		// Execute the file transfer
		transferStatus = command.execute();

		// Check that the move succeeded
		boolean check = false;
		try {
			check = exists(source.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (check)
			return CommandStatus.SUCCESS;
		else
			return CommandStatus.FAILED;

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
	
	/**
	 * This function returns the source file string
	 * @return - String - the source string
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * This function returns the destination file string
	 * @return - String - the destination string
	 */
	public String getDestination() {
		return destination;
	}

}
