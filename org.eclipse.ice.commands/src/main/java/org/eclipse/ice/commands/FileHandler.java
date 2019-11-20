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
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FileHandler class is a utility class for using commands that move files.
 * The class uses source and destination designations to identify how files
 * should be handled, and it can handle both local and remote files as sources
 * and destinations. Files can be moved, copied or checked for existence.
 * 
 * TODO - this class is not thread safe at the moment. This needs to be updated
 * with either Atomic or synchronized thread safety.
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
	 * The command member variable that will actually execute the transfer that was
	 * requested by the user
	 */
	protected AtomicReference<Command> command = new AtomicReference<Command>();

	/**
	 * A connection that is associated with this file handler
	 */
	protected AtomicReference<Connection> connection = new AtomicReference<Connection>(new Connection());

	/**
	 * A status member variable that indicates the status of the file transfer. See
	 * also {@link org.eclipse.ice.commands.CommandStatus}
	 */
	protected CommandStatus transferStatus;

	/**
	 * An enun to determine what the actual handle type is to set for the command.
	 * Default to null so that the RemoteFileHandler tries to determine it on its
	 * own - however, user can set this explicitly.
	 */
	protected HandleType HANDLE_TYPE = null;


	/**
	 * Have a connection manager for commands that defaults to the static object from
	 * the factory method. Users can override this if they want to through a specific
	 * constructor which sets the manager.
	 */
	protected ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
	
	

	/**
	 * Default constructor
	 */
	public FileHandler() {
	}

	/**
	 * See {@link org.eclipse.ice.commands.IFileHandler#move(String, String)}
	 */
	@Override
	public CommandStatus move(final String source, final String destination) {
		// Set the transfer status to processing, to indicate the transfer is beginning
		transferStatus = CommandStatus.PROCESSING;

		// Check the file existence. If they don't exist, an exception is thrown
		try {
			checkExistence(source, destination);
		} catch (IOException e) {
			logger.error("The source and/or destination file could not be confirmed to exist...", e);
			return CommandStatus.INFOERROR;
		}

		// Set the commands to have the appropriate properties
		configureMoveCommand(source, destination);

		// Execute and process the file transfer
		try {
			transferStatus = executeTransfer(destination);
		} catch (IOException e) {
			logger.error("Destination file does not exist! File transfer failed!", e);
			return CommandStatus.FAILED;
		}

		// Return whether or not it succeeded
		return transferStatus;
	}

	/**
	 * See {@link org.eclipse.ice.commands.IFileHandler#copy(String, String)}
	 */
	@Override
	public CommandStatus copy(final String source, final String destination) {
		// Set the transfer status to processing, to indicate the transfer is beginning
		transferStatus = CommandStatus.PROCESSING;

		// Check the file existence. If one or both don't exist, an exception is thrown
		try {
			checkExistence(source, destination);
		} catch (IOException e1) {
			logger.error("The source and/or destination file could not be confirmed to exist...", e1);
			return CommandStatus.INFOERROR;
		}

		// Set the commands to have the appropriate properties
		configureCopyCommand(source, destination);

		// Execute and process the file transfer
		try {
			transferStatus = executeTransfer(destination);
		} catch (IOException e) {
			logger.error("Destination file does not exist! File transfer failed!", e);
			return CommandStatus.FAILED;
		}

		// Return whether or not it succeeded
		return transferStatus;
	}

	/**
	 * This function sets the command member variables to have the source and
	 * destination strings. It is delegated to the subclasses so that the commands
	 * can be cast appropriately
	 * 
	 * @param source      - string of the source file
	 * @param destination - string of the destination file
	 */
	protected abstract void configureMoveCommand(final String source, final String destination);

	/**
	 * This function sets the command member variables to have the source and
	 * destination strings. It is delegated to the subclasses so that the commands
	 * can be cast appropriately
	 * 
	 * @param source      - string of the source file
	 * @param destination - string of the destination file
	 */
	protected abstract void configureCopyCommand(final String source, final String destination);

	/**
	 * See {@link org.eclipse.ice.commands.IFileHandler#exists(String)}
	 */
	@Override
	public abstract boolean exists(final String file) throws IOException;

	/**
	 * See
	 * {@link org.eclipse.ice.commands.IFileHandler#checkExistence(String, String)}
	 */
	@Override
	public abstract void checkExistence(final String source, final String destination) throws IOException;

	/**
	 * See {@link org.eclipse.ice.commands.IFileHandler#getFileBrowser()}
	 */
	@Override
	public abstract FileBrowser getFileBrowser();

	/**
	 * This function gets and returns the private member variable command of type
	 * Command
	 * 
	 * @return Command - the command associated with this FileHandler
	 */
	public Command getCommand() {
		return command.get();
	}

	/**
	 * Function to determine whether or not a given string is located on the local
	 * machine
	 * 
	 * @param file
	 * @return
	 */
	protected boolean isLocal(String file) {
		// Get the path
		Path path = Paths.get(file);
		// Return whether or not it exists on the local machine
		return Files.exists(path);

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
				logger.error("Couldn't create directory for local move! Failed.", e);
				return false;
			}
		}

		return exists;
	}

	/**
	 * This function actually executes the file transfer and then checks that it was
	 * completed correctly
	 * 
	 * @param destination - destination for the file to go to
	 * @return - CommandStatus indicating whether or not the transfer completed
	 *         successfully
	 * @throws IOException
	 */
	protected CommandStatus executeTransfer(final String destination) throws IOException {
		// Execute the file transfer
		transferStatus = command.get().execute();

		// Check that the move succeeded
		if (!exists(destination))
			return CommandStatus.FAILED;

		logger.info("File transfer successful!");
		return CommandStatus.SUCCESS;

	}

	/**
	 * A setter to set the type of file handle this is. See
	 * {@link org.eclipse.ice.commands.RemoteFileHandler#HANDLE_TYPE}
	 * 
	 * @param HANDLE_TYPE
	 */
	public void setHandleType(HandleType HANDLE_TYPE) {
		this.HANDLE_TYPE = HANDLE_TYPE;
	}

	/**
	 * Get the connection for this file handler
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return connection.get();
	}

	/**
	 * Set the connection associated to this file handler
	 * 
	 * @param connection
	 */
	public void setConnection(Connection connection) {
		this.connection.set(connection);
	}

}
