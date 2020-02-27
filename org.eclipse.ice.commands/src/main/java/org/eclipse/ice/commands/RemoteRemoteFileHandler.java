/*******************************************************************************
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.client.subsystem.sftp.SftpClient;

/**
 * This class implements the FileHandler class and contains the logic for
 * handling files across multiple remote connections, i.e. scp-ing files from
 * one remote host B to another remote host C from a local host A where the
 * Commands API is running
 * 
 * @author Joe Osborn
 *
 */
public class RemoteRemoteFileHandler extends RemoteFileHandler {

	/**
	 * A keypath authorization for connecting remote host B to remote host C.
	 */
	private KeyPathConnectionAuthorizationHandler remoteHostC = null;

	/**
	 * Default constructor
	 */
	public RemoteRemoteFileHandler() {
		// Create a new instance of the remote remote file transfer command for file
		// existence checks
		// HandleType is always the same for this type of move
		HANDLE_TYPE = HandleType.remoteOtherRemote;
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureMoveCommand(String, String)}
	 */
	@Override
	protected void configureMoveCommand(String source, String destination) {
		RemoteRemoteFileTransferCommand cmd = new RemoteRemoteFileTransferCommand();
		cmd.setRemoteHostCAuthorization(remoteHostC);
		cmd.setConfiguration(source, destination);
		cmd.setConnectionConfiguration(connection.get().getConfiguration());
		cmd.setConnection(connection.get());
		// Create the command config to be run
		cmd.createCommand();
		command.set(cmd);
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureCopyCommand(String, String)}
	 */
	@Override
	protected void configureCopyCommand(String source, String destination) {
		// copy and move are the same across remote hosts, i.e. scp
		configureMoveCommand(source, destination);

	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}. This
	 * implementation checks if the file exists on the destination remote host C
	 * only
	 */
	@Override
	public boolean exists(String file) {
		try {
			destinationExists(file);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#checkExistence(String, String)}
	 */
	@Override
	public void checkExistence(String source, String destination) throws IOException {

		destinationExists(destination);

		sourceExists(source);

		logger.info("FileHandler is moving/copying " + source + " on host "
				+ connection.get().getConfiguration().getName() + " to " + destination + " on host "
				+ remoteHostC.getHostname() + " with handle type " + HANDLE_TYPE);

	}

	/**
	 * Helper function to check if the destination directory exists on remote host C
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private void destinationExists(String file) throws IOException {
		// Run a command that checks whether or not the directory exists
		// Need to first check and see if it is a directory
		// Create an ls command
		String command = "ssh -i " + remoteHostC.getKeyPath() + " " + remoteHostC.getUsername() + "@"
				+ remoteHostC.getHostname();
		command += " \"ls " + file + "\"";

		CommandConfiguration config = new CommandConfiguration();
		config.setExecutable(command);
		config.setAppendInput(false);
		config.setCommandId(9999);
		config.setErrFileName("lsErr.txt");
		config.setOutFileName("lsOut.txt");
		config.setNumProcs("1");

		CommandFactory factory = new CommandFactory();
		Command lsCommand = factory.getCommand(config, connection.get().getConfiguration());
		CommandStatus lsStatus = lsCommand.execute();

		// If the command was successful, it means that it could successfully ls
		// indicating that the file was there.
		if (!lsStatus.equals(CommandStatus.SUCCESS)) {
			logger.error(
					"Could not confirm remote host C directory exists. Exiting. Check lsErr or lsOut .txt files for more information.");
			throw new IOException();
		}

		// Delete the lsErr and lsOut files that were made
		String rm = "lsOut.txt lsErr.txt";

		List<String> cmd = new ArrayList<String>();
		// Build a command
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			cmd.add("powershell.exe");
		} else {
			cmd.add("/bin/bash");
			cmd.add("-c");
		}
		cmd.add("rm " + rm);

		// Execute the command with the process builder api
		ProcessBuilder builder = new ProcessBuilder(cmd);
		// Files exist in the top most directory of the package
		String topDir = System.getProperty("user.dir");
		File filer = new File(topDir);
		builder.directory(filer);
		// Process it
		Process job = builder.start();
		try {
			// wait for it to finish
			job.waitFor();
		} catch (InterruptedException e) {
			logger.warn(
					"Existence output files couldn't be deleted - you can delete them yourself as they are extraneous.");
			logger.warn("The files are lsErr.txt and lsOut.txt, in your pwd.");
		}

		return;
	}

	/**
	 * Helper function to check if the source file exists on remote host B
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private void sourceExists(String file) throws IOException {
		SftpClient channel = connection.get().getSftpChannel();
		// try to ls the file on the remote host B
		// if an exception is caught, it isn't there
		try {
			channel.lstat(file);
		} catch (IOException e) {
			logger.error("Source file on remote host B does not exist. Exiting.");
			throw new IOException();
		}
		return;
	}

	/**
	 * Setter for the keypath information to connect remote host b to remote host c
	 * 
	 * @param key
	 */
	public void setDestinationAuthorization(KeyPathConnectionAuthorizationHandler remoteHostC) {
		this.remoteHostC = remoteHostC;
	}

}
