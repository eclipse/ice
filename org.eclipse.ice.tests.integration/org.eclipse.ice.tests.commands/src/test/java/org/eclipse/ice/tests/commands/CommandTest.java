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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.LocalCommand;
import org.eclipse.ice.commands.RemoteCommand;
import org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.Command}.
 *
 * @author Joe Osborn
 *
 */
public class CommandTest {

	/**
	 * Get the present working directory Add the following directories where the
	 * tests live
	 */
	private String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

	/**
	 * A TDP for collecting config files
	 */
	private TestDataPath dataPath = new TestDataPath();

	/**
	 * Remove output files after tests finish running
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@AfterClass
	public static void tearDownAfterClass() throws IOException, InterruptedException {

		// Make and execute a simple command to remove the text files created
		// in these tests.

		// Make a string of all the output file names in this test
		String rm = "someRemoteErrFile.txt someRemoteOutFile.txt someLocalOutFile.txt someLocalErrFile.txt";
		rm += " someRemoteErrFile1.txt someRemoteErrFile2.txt someRemoteOutFile1.txt someRemoteOutFile2.txt";
		ArrayList<String> command = new ArrayList<String>();
		// Build a command
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			command.add("powershell.exe");
		} else {
			command.add("/bin/bash");
			command.add("-c");
		}
		command.add("rm " + rm);
		// Execute the command with the process builder api
		ProcessBuilder builder = new ProcessBuilder(command);
		// Files exist in the top most directory of the package
		String topDir = System.getProperty("user.dir");
		File file = new File(topDir);
		builder.directory(file);
		// Process it
		Process job = builder.start();
		job.waitFor(); // wait for it to finish

		// Remove all connections that may remain from the manager
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		manager.removeAllConnections();

	}

	/**
	 * Set no strict host key checking just for tests
	 *
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

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
		commandConfig.addInputFile("someInputFile", "someInputFile.txt"); // an input file needed by the script
		commandConfig.setErrFileName("someRemoteErrFile.txt"); // a file to contain errors thrown
		commandConfig.setOutFileName("someRemoteOutFile.txt"); // a file to contain the output
		commandConfig.setInstallDirectory(""); // no install dir needed for this script
		commandConfig.setWorkingDirectory(pwd); // working directory which contains the files
		commandConfig.setAppendInput(true); // append the input file name to the script executable command
		commandConfig.setNumProcs("1"); // number of processes is 1
		commandConfig.setOS(System.getProperty("os.name"));

		// Set the remote working directory, where the command will be processed
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");
		// Set the connection configuration to a dummy remote connection

		// Make the ConnectionConfiguration and set it up
		ConnectionConfiguration connectConfig = new ConnectionConfiguration();

		// Make the connection configuration
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();

		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);

		// Set it
		connectConfig.setAuthorization(auth);
		connectConfig.setName("dummyConnection");
		connectConfig.deleteWorkingDirectory(true);

		// Make the command and execute it
		Command remoteCommand = new RemoteCommand(commandConfig, connectConfig, null);
		CommandStatus status = remoteCommand.execute();

		// Assert that it finished correctly
		assertEquals(CommandStatus.SUCCESS, status);

		// You can get the output in string form if desired
		String output = remoteCommand.getCommandConfiguration().getStdOutputString();
		System.out.println(output);
		// Remove the connection from the manager to close it appropriately
		ConnectionManagerFactory.getConnectionManager().removeConnection("dummyConnection");
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()} with a
	 * particular instance of LocalCommand.
	 */
	@Test
	public void testLocalCommand() {
		System.out.println("\n\n\n\nTest a local command!");
		String os = System.getProperty("os.name");

		// Set the CommandConfiguration class
		// See {@link org.eclipse.ice.commands.CommandConfiguration} for detailed info
		// on each
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable("./test_code_execution.sh");

		// If the os is windows, set the executable appropriately
		if (os.toLowerCase().contains("win")) {
			// two slashes so that java doesn't read it as a tab
			commandConfig.setExecutable(".\\test_code_execution.ps1");
			commandConfig.setInterpreter("powershell.exe");
		}

		commandConfig.addInputFile("someInputFile", "someInputFile.txt");
		commandConfig.setErrFileName("someLocalErrFile.txt");
		commandConfig.setOutFileName("someLocalOutFile.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");
		commandConfig.setOS(System.getProperty("os.name"));

		// Use the function already defined in the command factory to get the
		// local host name
		String hostname = CommandFactoryTest.getLocalHostname();

		// Create a connectionConfiguration to indicate a local command
		ConnectionConfiguration connection = new ConnectionConfiguration();
		ConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		handler.setHostname(hostname);
		connection.setAuthorization(handler);

		// Make the command and execute it
		Command localCommand = new LocalCommand(connection, commandConfig);
		CommandStatus status = localCommand.execute();

		// Check that it completed correctly
		System.out.println("Command finished with: " + status);
		assertEquals(CommandStatus.SUCCESS, status);

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
		assertFalse(command.checkStatus(status));
	}

	/**
	 * Tests a command where the script is local and the input is remote
	 */
	@Test
	public void testMultipleRemoteCommands() {

		// Make the ConnectionConfiguration and set it up
		ConnectionConfiguration connectConfig = new ConnectionConfiguration();

		// Make the connection configuration
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();

		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		connectConfig.setAuthorization(auth);
		connectConfig.setName("dummyConnection");
		connectConfig.deleteWorkingDirectory(true);

		// Open the connection
		try {
			ConnectionManagerFactory.getConnectionManager().openConnection(connectConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set up a command configuration with instructions on how to run the script
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(3);
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.addInputFile("someInputFile", "someInputFile.txt");
		commandConfig.setErrFileName("someRemoteErrFile1.txt");
		commandConfig.setOutFileName("someRemoteOutFile1.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");
		commandConfig.setOS(System.getProperty("os.name"));
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");

		// Make the command and execute it
		Command remoteCommand = new RemoteCommand(commandConfig, connectConfig, null);
		CommandStatus status = remoteCommand.execute();

		// Assert that it finished correctly
		assertEquals(CommandStatus.SUCCESS, status);

		// Try a second command with the same instance of command configuration and
		// remote command
		commandConfig.setCommandId(4);
		commandConfig.addInputFile("someOtherFile", "someOtherInputFile.txt");
		commandConfig.setErrFileName("someRemoteErrFile2.txt");
		commandConfig.setOutFileName("someRemoteOutFile2.txt");
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory2/");

		remoteCommand = new RemoteCommand(commandConfig, connectConfig, null);
		status = remoteCommand.execute();

		// Assert successful completion
		assertEquals(CommandStatus.SUCCESS, status);

	}

	/**
	 * This tests a command with a few input files and an argument that is not an
	 * input file, just something that the script needs.
	 */
	@Test
	public void testArgumentCommand() {
		// Make the ConnectionConfiguration and set it up
		ConnectionConfiguration connectConfig = new ConnectionConfiguration();

		// Make the connection configuration
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();

		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		connectConfig.setAuthorization(auth);
		connectConfig.setName("dummyConnection");
		connectConfig.deleteWorkingDirectory(false);

		// Open the connection
		try {
			ConnectionManagerFactory.getConnectionManager().openConnection(connectConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set up a command configuration with instructions on how to run the script
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(3);
		commandConfig.setExecutable("./test_codearg_execution.sh");
		commandConfig.addInputFile("someInputFile", "someInputFile.txt");
		commandConfig.addInputFile("someOtherInputFile", "someOtherInputFile.txt");
		commandConfig.addArgument("someString");
		commandConfig.setErrFileName("someRemoteErrFile1.txt");
		commandConfig.setOutFileName("someRemoteOutFile1.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");
		commandConfig.setOS(System.getProperty("os.name"));
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");

		// Make the command and execute it
		Command remoteCommand = new RemoteCommand(commandConfig, connectConfig, null);
		CommandStatus status = remoteCommand.execute();

		// Assert that it finished correctly
		assertEquals(CommandStatus.SUCCESS, status);

	}

}
