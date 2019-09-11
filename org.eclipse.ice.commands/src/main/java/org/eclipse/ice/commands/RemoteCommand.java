/**
 * /*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * This class inherits from Command and gives available functionality for remote
 * commands. These could be ssh connections or a remote process.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCommand extends Command {

	/**
	 * The particular connection associated to a particular RemoteCommand. Declare
	 * this up front since by definition a RemoteCommand must have a connection.
	 */
	protected Connection connection = new Connection();

	/**
	 * Default constructor
	 */
	public RemoteCommand() {

	}

	/**
	 * Constructor to instantiate the remote command with a particular
	 * CommandConfiguration and ConnectionConfiguration.
	 * 
	 * @param - ConnectionConfiguration which corresponds to the particular
	 *          connection
	 * @param - CommandConfiguration which corresponds to the particular command
	 */
	public RemoteCommand(ConnectionConfiguration connectConfig, CommandConfiguration _commandConfig) {
		commandConfig = _commandConfig;
		// Open and set the connection
		try {
			connection = ConnectionManager.openConnection(connectConfig);
		} catch (JSchException e) {
			e.printStackTrace();
		}

		// Set the commandConfig hostname to that of the connectionConfig - only used
		// for output info
		commandConfig.setHostname(connectConfig.getHostname());
		status = CommandStatus.PROCESSING;
	}

	/**
	 * Method that overrides Commmand:Execute and actually implements the particular
	 * RemoteCommand to be executed.
	 */
	@Override
	public CommandStatus execute() {
		// Check that the commandConfig and connectConfig were properly instantiated in
		// the constructor
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Configure the command to be ready to run.
		status = setConfiguration();

		// Ensure that the command was properly configured
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now that all of the prerequisites have been set, start the job running
		status = run();

		// Confirm the job finished with some status
		logger.info("The job finished with status: " + status);
		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {

		logger.info("Transferring files to remote host");
		try {
			status = transferFiles();
		} catch (SftpException e) {
			logger.error("Could not upload the input file to the remote host");
			e.printStackTrace();
		} catch (JSchException e) {
			logger.error("Session disconnected and could not upload the input file to the remote host");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.error("Input file not found! Could not upload to the remote host");
			e.printStackTrace();
		}

		// Setup the list of all of the commands that will be launched. JSch can not
		// launch multiple commands at once, so we need to take the splitCommand and 
		// cd to the correct working directory in front and then actually perform 
		// the execution. The commands are then split by the semi-colon
		ArrayList<String> completeCommands = new ArrayList<String>();
		for (String i : commandConfig.getSplitCommand()) {
			completeCommands.add("cd " + commandConfig.getWorkingDirectory() + "; " + i);
		}

		// Now loop over all commands and run them via JSch
		for (int i = 0; i < completeCommands.size(); i++) {
			
		}

		return status;
	}

	/**
	 * This function is responsible for transferring the files to the remote host
	 * 
	 * @return - CommandStatus indicating that the transfer was successful (or not)
	 * @throws SftpException
	 * @throws JSchException
	 * @throws FileNotFoundException
	 */
	protected CommandStatus transferFiles() throws SftpException, JSchException, FileNotFoundException {

		ChannelSftp sftpChannel = (ChannelSftp) connection.getSession().openChannel("sftp");
		sftpChannel.connect();

		logger.info("Make the working directory at: " + commandConfig.getWorkingDirectory());
		sftpChannel.mkdir(commandConfig.getWorkingDirectory());
		sftpChannel.cd(commandConfig.getWorkingDirectory());

		// Fix the inputFile name for remote machines
		String shortInputName = commandConfig.getInputFile();
		if (shortInputName.contains("/"))
			shortInputName = shortInputName.substring(shortInputName.lastIndexOf("/") + 1);
		else if (shortInputName.contains("\\"))
			shortInputName = shortInputName.substring(shortInputName.lastIndexOf("\\") + 1);

		sftpChannel.put(new FileInputStream(commandConfig.getInputFile()), shortInputName);

		sftpChannel.disconnect();
		return CommandStatus.RUNNING;
	}

	/**
	 * Set a particular connection for a particular RemoteCommand
	 * 
	 * @param connection - the connection for this command
	 */
	public void setConnection(Connection _connection) {
		connection = _connection;
	}

	/**
	 * Return the connection associated to this RemoteCommand
	 * 
	 * @return - {@link org.eclipse.ice.commands.RemoteCommand#connection}
	 */
	public Connection getConnection() {

		return connection;
	}

}
