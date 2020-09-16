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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.RemoteCommand;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
	static CommandConfiguration commandConfig;

	/**
	 * A connect configuration instance for testing
	 */
	static ConnectionConfiguration connectConfig = new ConnectionConfiguration();

	/**
	 * Get the present working directory and add the extra directories to get the
	 * directory where the executable lives
	 */
	static String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

	/**
	 * A TDP for collecting config files
	 */
	static TestDataPath dataPath = new TestDataPath();

	@After
	public void tearDown() throws Exception {
		ConnectionManagerFactory.getConnectionManager().listAllConnections();
	}

	@Before
	public void setUp() throws Exception {
		commandConfig = new CommandConfiguration();

		// Set the command to configure to a dummy hello world command
		// See {@link org.eclipse.ice.commands.CommandConfiguration} for detailed info
		// on each
		// This should stay *nix style since it is being executed on the dummy remote
		// host
		commandConfig.setCommandId(0);
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.addInputFile("someInputFile", "someInputFile.txt");
		commandConfig.setErrFileName("someErrFile.txt");
		commandConfig.setOutFileName("someOutFile.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");
		commandConfig.setOS(System.getProperty("os.name"));
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");

	}

	/**
	 * This function sets up the command and connection information to hand to the
	 * command
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set the connection configuration to a dummy remote connection
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
		// Delete the remote working directory when finished since we don't want the
		// dummy
		// host piling up with random directories
		connectConfig.deleteWorkingDirectory(true);

		ConnectionManagerFactory.getConnectionManager().openConnection(connectConfig);
	}

	/**
	 * Run after the tests have finished processing. This function just removes the
	 * dummy text files that are created with log/error information from running
	 * various commands tests.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@AfterClass
	public static void tearDownAfterClass() throws IOException, InterruptedException {

		// Make and execute a simple command to remove the text files created
		// in these tests.

		// Make a string of all the output file names in this test
		String rm = "someOutFile.txt someErrFile.txt errfile.txt outfile.txt";
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
	 * Test for method {@link org.eclipse.ice.commands.RemoteCommand()}
	 */
	@Test
	public void testRemoteCommand() {
		System.out.println("Testing remote command configuration");

		// Get a command which just sets everything up
		RemoteCommand command = new RemoteCommand(commandConfig, connectConfig, null);

		// Get the status
		CommandStatus status = command.getStatus();

		// Check that the return is processing, i.e. we are ready to execute
		assertEquals(CommandStatus.PROCESSING, status);

		System.out.println("Finished remote command configuration test.");
	}

	/**
	 * This tests that the job status is set to failed if an incorrect connection is
	 * established. Expect an exception since the connection will not be able to be
	 * established.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFailedConnectionRemoteCommand() {
		System.out.println("Testing remote command with a bad connection");

		// Make up some bad connection to test
		ConnectionConfiguration cfg = new ConnectionConfiguration();
		// Make the connection configuration
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text",
				"/non/existent/path/creds.txt");
		// Set it
		cfg.setAuthorization(auth);
		// Make a command with a bad connection
		RemoteCommand command = new RemoteCommand(commandConfig, cfg, null);

		// Check that the command gives an error in its status due to poor connection
		assertEquals(CommandStatus.INFOERROR, command.getStatus());
	}

	/**
	 * Test method for a nonexistent executable. Expect a null pointer exception
	 * because the code will try to transfer the executable, but be unable to find
	 * it. Can't have it throw an error because of the possibility that the
	 * executable is a simple shell command like ls
	 *
	 * @throws JSchException
	 */
	@Test(expected = NullPointerException.class)
	public void testBadExecute() throws IOException {
		CommandConfiguration badConfig = new CommandConfiguration();

		badConfig.setCommandId(24);
		badConfig.setExecutable("./fake_exec.sh");
		badConfig.addInputFile("inputfile", "inputfile");
		badConfig.setErrFileName("errfile.txt");
		badConfig.setOutFileName("outfile.txt");
		badConfig.setInstallDirectory("installDir");
		badConfig.setWorkingDirectory(pwd);
		badConfig.setAppendInput(true);
		badConfig.setNumProcs("1");
		badConfig.setOS(System.getProperty("os.name"));

		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();

		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		ConnectionConfiguration cfg = new ConnectionConfiguration();
		cfg.setAuthorization(auth);
		cfg.setName("connectionForBadExec");
		// Delete the remote working directory when finished since we don't want the
		// dummy host piling up with random directories
		cfg.deleteWorkingDirectory(true);

		RemoteCommand testCommand = new RemoteCommand(badConfig, cfg, null);

		testCommand.execute();

	}

	/**
	 * Test method for executing remote command
	 * {@link org.eclipse.ice.commands.RemoteCommand#execute()}
	 */
	@Test
	public void testExecute() {
		System.out.println("\n\n\nTest remote command execute");

		// Make a command and execute the command
		RemoteCommand command = new RemoteCommand(commandConfig, connectConfig, null);
		CommandStatus status = command.execute();

		// Check that the command was successfully completed
		assertEquals(CommandStatus.SUCCESS, status);

		System.out.println("Finished testing remote command execute");
	}

	/**
	 * This function tests an intentionally long running script in the background to
	 * determine what JSch response is to connections being broken, etc. It is
	 * commented out for now since it doesn't test different functionality for the
	 * above tests. It will be useful in the future when we want to test things like
	 * querying the status, job monitoring on remote hosts, etc. TODO
	 */
	// @Test
	public void testLongRemoteJob() {

		CommandConfiguration longConfig = new CommandConfiguration();

		longConfig.setCommandId(24);
		longConfig.setExecutable("./test_long_job.sh");
		longConfig.setErrFileName("longErrFile.txt");
		longConfig.setOutFileName("longOutFile.txt");
		longConfig.setWorkingDirectory(pwd);
		longConfig.setRemoteWorkingDirectory("/tmp/longJob/");
		longConfig.setAppendInput(true);
		longConfig.setNumProcs("1");
		longConfig.setOS(System.getProperty("os.name"));

		RemoteCommand testCommand = new RemoteCommand(longConfig, connectConfig, null);

		testCommand.execute();

	}

}
