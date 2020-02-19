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

import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
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
 * @author Joe Osborn
 *
 */
public class MultiRemoteHostCommandExample {

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

		setupHostB();

		// Now we will actually execute the remote command on remote host B, where in
		// turn the script will execute a command on remote host C.
		runRemoteCommand();

		
	}

	/**
	 * This function sets up host B in the assumed configuration that it would
	 * actually be in. Since this example assumes that you are using an arbitrary
	 * remote host, the files to run this dummy job would not nominally be on that
	 * remote host. So we move them there in this function.
	 * @throws IOException 
	 */
	private static void setupHostB() throws IOException {
		// Setup the connection to the host
		ConnectionConfiguration connectionConfig = createConnection();

		// Setup some strings of files to move
		String pwd = System.getProperty("user.dir");
		String workingDir = pwd + "/src/org/eclipse/ice/demo/commands/";
		String scriptName = "remoteCommand.sh";
		String inputFileName = "someInputFile.txt";
		
		// Get the file handler for moving
		FileHandlerFactory factory = new FileHandlerFactory();
		IFileHandler handler = factory.getFileHandler(connectionConfig);
		
		// handler will create a new remote directory if the destination
		// directory doesn't already exist on the remote host B
		String destination = "/tmp/MultiRemoteHostCommandExample/";
		System.out.println("\n\n\n\n moving");
		CommandStatus status = handler.move(workingDir+scriptName, destination);
		
		// Check that the file was successful 
		assert(status == CommandStatus.SUCCESS);
		System.out.println("\n\n\n\n\n\n\nMoving done with status " + status);
		status = handler.move(workingDir+inputFileName, destination);
		
		assert(status == CommandStatus.SUCCESS);
	
	}

	/**
	 * Create a connection to the host B, assumed to be osbornjd-ice-host.ornl.gov
	 */
	private static ConnectionConfiguration createConnection() {

		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		// Set the connection configuration to a dummy remote connection
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// dummy remote host credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text",
				"/tmp/ice-remote-creds.txt");
		/**
		 * Alternatively, one can authorize with their password at the console line by
		 * performing the following set of code
		 * 
		 * ConnectionAuthorizationHandler auth =
		 * authFactory.getConnectionAuthorizationHandler("console");
		 * auth.setHostname("hostname"); auth.setUsername("username");
		 */
		// Set it so that the connection can authorize itself
		connectionConfig.setAuthorization(auth);
		// Give the connection a name
		connectionConfig.setName("dummyConnection");

		// Delete the remote working directory once we are finished running the job
		connectionConfig.deleteWorkingDirectory(true);

		return connectionConfig;
	}

	private static void runRemoteCommand() {

	}

}
