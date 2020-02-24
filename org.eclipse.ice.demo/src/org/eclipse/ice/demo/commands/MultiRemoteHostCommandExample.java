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
package org.eclipse.ice.demo.commands;

import java.io.IOException;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.FileHandlerFactory;
import org.eclipse.ice.commands.IFileHandler;

/**
 * This class demonstrates an example of executing a job on a remote host which
 * executes a job on another remote host. For example, if Commands is running on
 * host A, and the user desires to orchestrate the execution of a Command on
 * host B to run on host C while operating the Commands package on host A.
 * 
 * The idea here is to run a RemoteCommand from host A to host B, where the
 * command is a script in which the script contains the logic to run a remote
 * command from host B to host C.
 * 
 * Note that one needs to set the host keys below to hosts that they actually have
 * keys to in order for this example to work.
 * 
 * @author Joe Osborn
 *
 */
public class MultiRemoteHostCommandExample {

	/**
	 * Set these parameters to make the job run to hosts of your choosing
	 */
	static String hostB = "host";
	static String userB = "user";
	static String hostBKeyPath = "/some/path/to/key";
	static String hostC = "dummy@osbornjd-ice-host.ornl.gov";
	// This key exists on host B, connecting B to C
	static String hostCKeyPath = "~/.ssh/dummyhostkey";
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		/**
		 * We will run this command from host A (your local computer) such that it
		 * executes a Command on host B to be remotely executed on host C. In this case,
		 * host B will be osbornjd-ice-host.ornl.gov and host C will be Denisovan.
		 * 
		 * Since the example assumes that you are using an arbitrary remote host, we
		 * must first create the files on host A and move them to host B. Nominally,
		 * these files would already exist on host B, but because of the arbitrary
		 * remote host assumption we put them there for you
		 */

		// Now we will actually execute the remote command on remote host B, where in
		// turn the script will execute a command on remote host C.
		runRemoteCommand();

	}

	/**
	 * This function sets up host B in the assumed configuration that it would
	 * actually be in. Since this example assumes that you are using an arbitrary
	 * remote host, the files to run this dummy job would not nominally be on that
	 * remote host. So we move them there in this function.
	 * 
	 * @throws IOException
	 */
	private static void runRemoteCommand() throws IOException {
		// Setup the connection to the host
		ConnectionConfiguration connectionConfig = createConnection();

		// Setup some strings of files to move
		String pwd = System.getProperty("user.dir");
		String workingDir = pwd + "/src/org/eclipse/ice/demo/commands/";
		// This is the script to be run on host B. Need to move it from host A to B
		// so that the environment is set to run the script which will run scriptName
		// through Commands
		String scriptName = "test_code_execution.sh";
		String inputFileName = "someInputFile.txt";
		String remoteWorkingDir = "/tmp/remoteRemoteCommand/";

		// We need to explicitly move the script and input file to host B since we don't
		// want it appended as an argument to the script running on remote host B. The
		// script remoteCommand.sh will take care of that
		FileHandlerFactory FHFactory = new FileHandlerFactory();
		IFileHandler handler = FHFactory.getFileHandler(connectionConfig);

		CommandStatus fileStatus = handler.move(workingDir + inputFileName, remoteWorkingDir);

		assert (fileStatus == CommandStatus.SUCCESS);

		fileStatus = handler.move(workingDir + scriptName, remoteWorkingDir);

		assert (fileStatus == CommandStatus.SUCCESS);

		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(workingDir);
		commandConfig.setOS(System.getProperty("os.name"));
		commandConfig.setExecutable("./remoteCommand.sh");
		commandConfig.setAppendInput(true);
		commandConfig.setCommandId(1);
		commandConfig.setErrFileName("RemoteRemoteErr.txt");
		commandConfig.setOutFileName("RemoteRemoteOut.txt");
		commandConfig.setRemoteWorkingDirectory(remoteWorkingDir);

		// add arguments that remoteCommand.sh needs to run on host C
		commandConfig.addArgument(hostC + " /tmp/remoteDir " + hostCKeyPath);
		CommandFactory factory = new CommandFactory();
		Command remoteCommand = null;
		try {
			remoteCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		CommandStatus status = remoteCommand.execute();

		assert (status == CommandStatus.SUCCESS);

		
	}

	/**
	 * Create a connection to the host B
	 */
	private static ConnectionConfiguration createConnection() {

		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		// Set the connection configuration to a dummy remote connection
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// dummy remote host credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("keypath",
				hostBKeyPath);
		auth.setHostname(hostB);
		auth.setUsername(userB);
		// Set it so that the connection can authorize itself
		connectionConfig.setAuthorization(auth);
		// Give the connection a name
		connectionConfig.setName("PCConnection");

		// Delete the remote working directory once we are finished running the job
		connectionConfig.deleteWorkingDirectory(true);

		return connectionConfig;
	}

}
