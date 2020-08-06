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
package org.eclipse.ice.tests.commands;

import java.io.File;
import java.io.IOException;

/**
 * This class implements functionality to perform remote to remote file
 * transfers where the two remote hosts are not the same. In other words, with
 * the Commands API running on local host A, executing a transfer from remote
 * host B to remote host C. The requirement for this to work is that remote host
 * B must have an ssh key pair that connects it to remote host C, so that the
 * connection can occur.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteRemoteFileTransferCommand extends RemoteCommand {

	/**
	 * A string containing the absolute path to the source on remote host B
	 */
	private String source;

	/**
	 * A string containing the absolute path to the destination on remote host C
	 */
	private String destination;

	/**
	 * The ConnectionAuthorizationHandler which will contain the necessary
	 * configuration information to connect remote host B, where the source file is,
	 * to remote host C, where the destination is. Required to be a keypath
	 * connection for the authorization to work, should not be a generic
	 * ConnectionAuthorizationHandler
	 */
	private KeyPathConnectionAuthorizationHandler remoteHostC = null;

	/**
	 * Default constructor
	 */
	public RemoteRemoteFileTransferCommand() {
		// Create a default CommandConfig to work with
		commandConfig = new CommandConfiguration();
	}

	/**
	 * Function which sets the two paths, source and destination, to those given by
	 * the arguments. The ConnectionConfiguration also gives the remote connection
	 * configuration for setting up the ssh and sftp channels to the remote host b.
	 * 
	 * @param src  - source file to be moved
	 * @param dest - destination for source file to be moved to
	 */
	public void setConfiguration(String source, String destination) {
		this.source = source;
		this.destination = destination;
		status = CommandStatus.PROCESSING;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {

		status = processJob();

		// Check the status to ensure file transfer was successful
		if (!checkStatus(status))
			return CommandStatus.FAILED;

		status = monitorJob();

		// Check the status to ensure file transfer was successful
		if (!checkStatus(status))
			return CommandStatus.FAILED;

		status = finishJob();

		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.RemoteCommand#finishJob()}
	 */
	@Override
	protected CommandStatus finishJob() {
		// Disconnect the channels and return success
		try {
			connection.get().getExecChannel().close();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
		try {
			if (connection.get().getSftpChannel() != null)
				connection.get().getSftpChannel().close();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}

		// Delete err and out files that were created
		String outPath = commandConfig.getOutFileName();
		String errPath = commandConfig.getErrFileName();
		String workDir = commandConfig.getWorkingDirectory();
		File outFile = new File(workDir + outPath);
		File errFile = new File(workDir + errPath);
		// Only delete if the command was successful. If it wasn't, the files
		// could be useful for debugging
		if (status.equals(CommandStatus.SUCCESS)) {
			if (!outFile.delete() || !errFile.delete()) {
				logger.warn("Couldn't delete out/err file in directory " + workDir);
			}
		}

		return status;
	}

	/**
	 * Configures the CommandConfiguration to be run, which is just an scp command
	 * utilizing the remoteHostC member variables
	 * 
	 * @return
	 */
	public void createCommand() {

		String keypath = remoteHostC.getKeyPath();
		String remoteHostCUser = remoteHostC.getUsername();
		String remoteHostCHost = remoteHostC.getHostname();
		String fullCommand = "scp -i " + keypath + " " + source + " " + remoteHostCUser + "@" + remoteHostCHost + ":"
				+ destination;

		commandConfig.setExecutable(fullCommand);
		commandConfig.setAppendInput(false);
		// Just set the remote working directory to be /tmp since it doesn't
		// need a directory to work in
		commandConfig.setRemoteWorkingDirectory("/tmp/");
		commandConfig.setCommandId(9999);
		commandConfig.setNumProcs("1");
		// set working directory to current directory
		// commandConfig.setWorkingDirectory(System.getProperty("user.dir"));
		// We'll set these and delete them at the end, so as to not create a bunch of
		// err/out files
		commandConfig.setErrFileName("scpErr.txt");
		commandConfig.setOutFileName("scpOut.txt");

		return;
	}

	/**
	 * Get the source file string
	 * 
	 * @return - string of the source path
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get the destination file string
	 * 
	 * @return - string of the destination path
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Set the connection information for connecting remote host B to remote host C
	 * 
	 * @param remoteHostC
	 */
	public void setRemoteHostCAuthorization(KeyPathConnectionAuthorizationHandler remoteHostC) {
		this.remoteHostC = remoteHostC;
	}

	/**
	 * Getter for remotehost C keypath authorization
	 * 
	 * @return
	 */
	protected KeyPathConnectionAuthorizationHandler getRemoteHostCAuthorization() {
		return remoteHostC;
	}

	/**
	 * Function that sets the connection configuration and checks to make sure it is
	 * open. If it isn't, opens it
	 */
	@Override
	public void setConnectionConfiguration(ConnectionConfiguration connectionConfig) {
		this.connectionConfig = connectionConfig;
		openAndSetConnection();
	}
}
