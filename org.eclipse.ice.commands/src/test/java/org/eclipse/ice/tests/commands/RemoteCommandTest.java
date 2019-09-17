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
import java.util.Scanner;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.RemoteCommand;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.RemoteCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCommandTest {

	/**
	 * A command configuration instance for testing
	 */
	CommandConfiguration commandConfig;

	/**
	 * A connect configuration instance for testing
	 */
	ConnectionConfiguration connectConfig;

	/**
	 * This function sets up the command and connection information to hand to the
	 * command
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		// Get the present working directory
		String pwd = System.getProperty("user.dir");

		// Add the following directories where the tests live
		pwd += "/src/test/java/org/eclipse/ice/tests/commands/";

		commandConfig = new CommandConfiguration();

		// Set the command to configure to a dummy hello world command
		// See {@link org.eclipse.ice.commands.CommandConfiguration} for detailed info
		// on each
		commandConfig.setCommandId(0);
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.setInputFile("someInputFile.txt");
		commandConfig.setErrFileName("someErrFile.txt");
		commandConfig.setOutFileName("someOutFile.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");
		commandConfig.setOS("linux");

		// Set the connection configuration to a dummy remote connection
		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = new Scanner(file);
		// Scan line by line
		scanner.useDelimiter("\n");

		// Get the credentials for the dummy remote account
		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		// Make the connection configuration
		connectConfig = new ConnectionConfiguration();
		connectConfig.setHostname(hostname);
		connectConfig.setUsername(username);

		// Note the password can be input at the console by just setting
		// connectConfig.setPassword(""); in the event that you don't want your
		// password held in a string object
		connectConfig.setPassword(password);
		connectConfig.setName("dummyConnection");
		connectConfig.setWorkingDirectory("/tmp/remoteCommandTestDirectory");
		connectConfig.setDeleteWorkingDirectory(true);
	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteCommand()}
	 */
	@Test
	public void testRemoteCommand() {
		System.out.println("Testing remote command configuration");

		// Get a command which just sets everything up
		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);

		// Get the status
		CommandStatus status = command.getStatus();

		// Check that the return is processing, i.e. we are ready to execute
		assert (status == CommandStatus.PROCESSING);

		System.out.println("Finished remote command configuration test.");
	}

	/**
	 * This tests that the job status is set to failed if an incorrect connection is
	 * established.
	 */
	@Test
	public void testFailedConnectionRemoteCommand() {
		System.out.println("Testing remote command with a bad connection");

		// Make up some bad connection to test
		connectConfig.setUsername("someBadUsername");
		connectConfig.setHostname("someBadHostname");
		connectConfig.setPassword("someBadPassword");
		// Make a command with a bad connection
		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);

		// Check that the command gives an error in its status due to poor connection
		assert (command.getStatus() == CommandStatus.INFOERROR);
	}

	/**
	 * Test method for executing remote command
	 * {@link org.eclipse.ice.commands.RemoteCommand#execute()}
	 */
	@Test
	public void testExecute() {
		System.out.println("\n\n\nTest remote command execute");

		// Make a command and execute the command
		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);
		CommandStatus status = command.execute();

		// Check that the command was successfully completed
		assert (status == CommandStatus.SUCCESS);

		System.out.println("Finished testing remote command execute");
	}

}
