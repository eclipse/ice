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
	 * The command member variable that will actually execute the move that
	 * was requested by the user
	 */
	Command moveCommand;
	
	/**
	 * The command member variable that will actually execute the copy that
	 * was requested by the user
	 */
	Command copyCommand;


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
	 * This method is responsible for moving a file from a source to a destination
	 * path If the operation fails, an exception is thrown
	 * 
	 * @return - CommandStatus - a CommandStatus indicating whether or not the move
	 *         was successful
	 * @throws IOException
	 */
	public abstract CommandStatus move(final String source, final String destination) throws IOException;

	/**
	 * This method is responsible for copying a file from a source to a destination
	 * path If the operation fails, an exception is thrown
	 * 
	 * @return - CommandStatus - a CommandStatus indicating whether or not the copy
	 *         was successful
	 * @throws IOException
	 */
	public abstract CommandStatus copy(final String source, final String destination) throws IOException;

	/**
	 * This method is responsible for determining whether or not a file or directory
	 * already exists for a given path.
	 * 
	 * @param - String - a string with the path for the method to check its
	 *          existence
	 * @return - boolean indicating whether or not the file exists (returns true) or
	 *         does not exist (returns false)
	 * @throws IOException
	 */
	public abstract boolean exists(final String file) throws IOException;

	/**
	 * This method checks the existence of the source and destination files. If the
	 * destination doesn't exist, it tries to make it. If the destination can't be
	 * made, or the source doesn't exist, the method throws an exception.
	 * 
	 * @param source
	 * @param destination
	 * @return
	 * @throws IOException
	 */
	public abstract void checkExistence(final String source, final String destination) throws IOException;

	/**
	 * This function gets and returns the private member variable moveCommand of type
	 * Command
	 * 
	 * @return Command - the move command associated with this FileHandler
	 */
	public Command getMoveCommand() {
		return moveCommand;
	}
	
	/**
	 * This function gets and returns the private member variable copyCommand of type
	 * Command
	 * 
	 * @return Command - the copy command associated with this FileHandler
	 */
	public Command getCopyCommand() {
		return copyCommand;
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

	/**
	 * This function actually executes the file transfer and then checks that it was 
	 * completed correctly
	 * @param destination - destination for the file to go to
	 * @return - CommandStatus indicating whether or not the transfer completed successfully
	 */
	protected CommandStatus executeCopy(final String destination) {
		// Execute the file transfer
		transferStatus = copyCommand.execute();

		// Check that the move succeeded
		boolean check = false;
		try {
			check = exists(destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (check)
			return CommandStatus.SUCCESS;
		else
			return CommandStatus.FAILED;

	}

	/**
	 * This function actually executes the file transfer and then checks that it was 
	 * completed correctly
	 * @param destination - destination for the file to go to
	 * @return - CommandStatus indicating whether or not the transfer completed successfully
	 */
	protected CommandStatus executeMove(final String destination) {
		
	
		// Execute the file transfer
		transferStatus = moveCommand.execute();

		// Check that the move succeeded
		boolean check = false;
		try {
			check = exists(destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (check)
			return CommandStatus.SUCCESS;
		else
			return CommandStatus.FAILED;

	}
	
	/**
	 * This function returns the current status of the copy, as it is given
	 * by the member variable {@link org.eclipse.ice.commands.FileHandler#copyCommand}
	 * 
	 * @return - CommandStatus indicating the status of the file transfer
	 */
	public CommandStatus getCopyStatus() {
		return copyCommand.getStatus();
	}

	/**
	 * This function returns the current status of the move, as it is given
	 * by the member variable {@link org.eclipse.ice.commands.FileHandler#copyCommand}
	 * 
	 * @return - CommandStatus indicating the status of the file transfer
	 */
	public CommandStatus getMoveStatus() {
		return moveCommand.getStatus();

	}
}
