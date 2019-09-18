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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.CommandFactory}.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class CommandFactoryTest {

	/**
	 * The hostname for which the job should run on. Default to local host name for
	 * now
	 */
	String hostname = getLocalHostname();

	/**
	 * Create a command factory to use for getting the commands.
	 */
	CommandFactory factory = new CommandFactory();

	/**
	 * A string containing the working directory for the executable to run in
	 */
	String workingDirectory;

	// Get the present working directory
	String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

	/**
	 * A command configuration with which to test
	 */
	CommandConfiguration commandConfig = new CommandConfiguration();

	/**
	 * A connection configuration with which to test
	 */
	ConnectionConfiguration connectionConfig = new ConnectionConfiguration();

	public CommandFactoryTest() {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		/**
		 * Create a CommandConfiguration with the necessary information to execute a
		 * Command. See {@link org.eclipse.ice.commands.CommandConfiguration} for
		 * relevant member variables/constructor.
		 */

		// Set the CommandConfiguration class with some default things that are relevant
		// for all
		// the test functions here
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.setInputFile("someInputFile.txt");
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setOS("osx");

	}

	/**
	 * This function tests with real files to test an actual job processing. For
	 * this test to work, make sure you change the workingDirectory to your actual
	 * workingDirectory where the Commands API lives.
	 */
	@Test
	public void testFunctionalLocalCommand() {

		// Set some things specific to the local command
		commandConfig.setCommandId(1);
		commandConfig.setErrFileName("someLocalErrFile.txt");
		commandConfig.setOutFileName("someLocalOutFile.txt");
		connectionConfig.setHostname(hostname);

		// Get the command
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it
		CommandStatus status = localCommand.execute();

		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()}
	 * execution chain with an uncompleted Command dictionary. This is function is
	 * intended to test some of the exception catching, thus it is expected to
	 * "fail." It expect a null pointer exception, since the hostname is not given
	 * and thus the hostname string is null
	 */
	@Test(expected = NullPointerException.class)
	public void testNonFunctionalLocalCommand() {

		System.out.println("\nTesting some commands where not enough command information was provided.");

		// Create a command configuration that doesn't have all the necessary
		// information
		// Set the CommandConfiguration class
		CommandConfiguration badCommandConfig = new CommandConfiguration();

		ConnectionConfiguration badConnectConfig = new ConnectionConfiguration();
		badConnectConfig.setHostname(hostname);
		// Get the command
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(badCommandConfig, badConnectConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it and expect that it fails
		CommandStatus status = localCommand.execute();

		assert (status == CommandStatus.INFOERROR);

	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()}
	 * execution chain with an uncompleted Command dictionary. This function is
	 * intended to test some of the exception catching, thus it is expected to
	 * "fail."
	 */
	@Test
	public void testIncorrectWorkingDirectory() {
		/**
		 * Run another non functional command, with a non existing working directory
		 */

		System.out.println("\nTesting a command where a nonexistent working directory was provided.");

		// Set the commandConfig class
		commandConfig.setCommandId(3);
		commandConfig.setErrFileName("someLocalErrFileDir.txt");
		commandConfig.setOutFileName("someLocalOutFileDir.txt");
		commandConfig.setWorkingDirectory("~/some_nonexistent_directory");

		connectionConfig.setHostname(hostname);
		// Get the command
		Command localCommand2 = null;
		try {
			localCommand2 = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it and expect that it fails
		CommandStatus status2 = localCommand2.execute();

		assert (status2 == CommandStatus.FAILED);
	}

	@Test
	public void testFunctionalRemoteCommand() {

		// Set the CommandConfiguration class
		commandConfig.setCommandId(4);
		commandConfig.setErrFileName("someRemoteErrFile.txt");
		commandConfig.setOutFileName("someRemoteOutFile.txt");

		// Set the connection configuration to a dummy remote connection
		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// Scan line by line
		scanner.useDelimiter("\n");

		// Get the credentials for the dummy remote account
		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		// Make the connection configuration
		connectionConfig.setHostname(hostname);
		connectionConfig.setUsername(username);
		connectionConfig.setPassword(password);
		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		connectionConfig.setName("dummyConnection");
		connectionConfig.setWorkingDirectory("/tmp/remoteCommandTestDirectory");
		connectionConfig.setDeleteWorkingDirectory(true);

		// Get the command
		Command remoteCommand = null;
		try {
			remoteCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it
		CommandStatus status = remoteCommand.execute();

		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * This function just returns the local hostname of your local computer. It is
	 * useful for testing a variety of local commands.
	 * 
	 * @return - String - local hostname
	 */
	protected String getLocalHostname() {
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();

		return hostname;
	}

}
