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
package org.eclipse.ice.tests.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.LocalCommand;
import org.eclipse.ice.commands.RemoteCommand;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.Command}.
 * 
 * @author Joe Osborn
 *
 */
public class CommandTest {

	// Get the present working directory
	// Add the following directories where the tests live
	String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()} with a
	 * particular instance of a RemoteCommand
	 */

	@Test
	public void testRemoteCommand() {

		System.out.println("Test a remote command!");
		// Set up a command configuration with instructions on how to run the script
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(2); // an id indicating the job number
		commandConfig.setExecutable("./test_code_execution.sh"); // the shell script to execute
		commandConfig.setInputFile("someInputFile.txt"); // an input file needed by the script
		commandConfig.setErrFileName("someRemoteErrFile.txt"); // a file to contain errors thrown
		commandConfig.setOutFileName("someRemoteOutFile.txt"); // a file to contain the output
		commandConfig.setInstallDirectory(""); // no install dir needed for this script
		commandConfig.setWorkingDirectory(pwd); // working directory which contains the files
		commandConfig.setAppendInput(true); // append the input file name to the script executable command
		commandConfig.setNumProcs("1"); // number of processes is 1
		commandConfig.setOS(System.getProperty("os.name"));

		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");
		// Set the connection configuration to a dummy remote connection
		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// Get the credentials line by line
		scanner.useDelimiter("\n");
		// Get the credentials for the dummy remote account

		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		// Make the ConnectionConfiguration and set it up
		ConnectionConfiguration connectConfig = new ConnectionConfiguration();
		connectConfig.setHostname(hostname);
		connectConfig.setUsername(username);
		// Set password to "" to force input of remote connection password at console
		// line, for example: connectConfig.setPassword("");
		connectConfig.setPassword(password);
		// Give the connection a name
		connectConfig.setName("dummyConnection");
		// Tell the connection where you want the executable to be run in the remote
		// system
		// Delete remote working directory after job completion
		connectConfig.setDeleteWorkingDirectory(true);

		// Make the command and execute it
		Command remoteCommand = new RemoteCommand(commandConfig, connectConfig, null);
		CommandStatus status = remoteCommand.execute();

		// Assert that it finished correctly
		assert (status == CommandStatus.SUCCESS);

		// You can get the output in string form if desired
		String output = remoteCommand.getCommandConfiguration().getStdOutputString();
		System.out.println(output);
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()} with a
	 * particular instance of LocalCommand.
	 */
	@Test
	public void testLocalCommand() {
		System.out.println("\n\n\n\nTest a local command!");

		// Set the CommandConfiguration class
		// See {@link org.eclipse.ice.commands.CommandConfiguration} for detailed info
		// on each
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.setInputFile("someInputFile.txt");
		commandConfig.setErrFileName("someLocalErrFile.txt");
		commandConfig.setOutFileName("someLocalOutFile.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");
		commandConfig.setOS("osx");

		// Use the function already defined in the command factory to get the
		// local host name
		CommandFactoryTest factory = new CommandFactoryTest();
		String hostname = factory.getLocalHostname();

		// Create a connectionConfiguration to indicate a local command
		ConnectionConfiguration connection = new ConnectionConfiguration();
		connection.setHostname(hostname);

		// Make the command and execute it
		Command localCommand = new LocalCommand(connection, commandConfig);
		CommandStatus status = localCommand.execute();

		// Check that it completed correctly
		System.out.println("Command finished with: " + status);
		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.Command#checkStatus(CommandStatus)}
	 */
	@Test
	public void testCheckStatus() {

		System.out.println("\n\n\n\nTesting that Command::CheckStatus throws an exception for a bad status");
		// Create instances of a command and associated (bad) status
		CommandStatus status = CommandStatus.INFOERROR;
		Command command = new LocalCommand();

		// Check to make sure that an exception is thrown for a bad status when the
		// status is checked
		try {
			command.checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
