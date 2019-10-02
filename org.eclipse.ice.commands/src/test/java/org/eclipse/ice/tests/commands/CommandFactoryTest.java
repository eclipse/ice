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
import org.eclipse.ice.commands.ConnectionManager;
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
		commandConfig.setOS(System.getProperty("os.name"));

	}

	/**
	 * This function tests a multi-hop remote command, where the command logs into a
	 * remote host and then executes on a different remote host.
	 */
	@Test
	public void testMultiHopRemoteCommand() {
		System.out.println("\n\n\nTesting a multi-hop remote command");
		// Set the CommandConfiguration class
		commandConfig.setCommandId(99);
		commandConfig.setErrFileName("hopRemoteErrFile.txt");
		commandConfig.setOutFileName("hopRemoteOutFile.txt");
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");
		// Just put in a dummy directory for now
		commandConfig.setWorkingDirectory("/home/user/somedirectory");
		// Set the connection configuration to a dummy remote connection
		// This is the connection where the job will be executed
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
		connectionConfig.setName("executeConnection");
		connectionConfig.setDeleteWorkingDirectory(false);

		// Make another connection which we will log into first, and then execute from
		// there
		File secondFile = new File("/tmp/denisovan.txt");
		try {
			scanner = new Scanner(secondFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		scanner.useDelimiter("\n");
		hostname = scanner.next();
		username = scanner.next();

		ConnectionConfiguration intermConnection = new ConnectionConfiguration();
		intermConnection.setHostname(hostname);
		intermConnection.setUsername(username);
		intermConnection.setPassword("some_password");
		intermConnection.setName("intermediateConnection");
		intermConnection.setDeleteWorkingDirectory(false);

		// Get the command
		Command remoteCommand = null;
		try {
			remoteCommand = factory.getCommand(commandConfig, intermConnection, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it
		CommandStatus status = remoteCommand.execute();

		// assert that it was successful
		assert (status == CommandStatus.SUCCESS);

		// Delete the connections
		ConnectionManager.removeAllConnections();

	}

	/**
	 * This function tests a more boring command, e.g. just executing "ls" at the
	 * command prompt This shows that the API can be used to execute basic command
	 * line prompts.
	 */
	@Test
	public void testBoringCommand() {

		CommandConfiguration cmdCfg = new CommandConfiguration();
		cmdCfg.setExecutable("ls -lrt");
		cmdCfg.setNumProcs("1");
		cmdCfg.setInstallDirectory("");
		cmdCfg.setWorkingDirectory(pwd);
		cmdCfg.setAppendInput(false);
		cmdCfg.setOS(System.getProperty("os.name"));
		cmdCfg.setCommandId(1);
		cmdCfg.setErrFileName("someLsErrFile.txt");
		cmdCfg.setOutFileName("someLsOutFile.txt");
		ConnectionConfiguration ctCfg = new ConnectionConfiguration();
		ctCfg.setHostname(hostname);

		Command cmd = null;
		try {
			cmd = factory.getCommand(cmdCfg, ctCfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CommandStatus status = cmd.execute();
		assert (status == CommandStatus.SUCCESS);
	}

	/**
	 * This function tests with real files to test an actual job processing. The job
	 * executes a script with some hello world commands in it.
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
	 * execution chain with an incomplete Command dictionary. This function is
	 * intended to catch the "exception" catching of the API, where the exception is
	 * in quotes due to the return of a bad CommandStatus rather than e.g. a true
	 * java Exception.
	 */
	@Test
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

		assert (status2 == CommandStatus.INFOERROR);
	}

	/**
	 * This function tests a functional remote command with the full command factory
	 * implementation
	 */
	@Test
	public void testFunctionalRemoteCommand() {

		System.out.println("\n\n\nTesting a functional remote command");
		// Set the CommandConfiguration class
		commandConfig.setCommandId(4);
		commandConfig.setErrFileName("someRemoteErrFile.txt");
		commandConfig.setOutFileName("someRemoteOutFile.txt");
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");
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

		// assert that it was successful
		assert (status == CommandStatus.SUCCESS);

		// Delete the connections
		ConnectionManager.removeAllConnections();

	}

	/**
	 * This tests a command which requires multiple input files to run remotely
	 */
	@Test
	public void testMultipleInputFilesRemotely() {
		// Set the CommandConfiguration class
		commandConfig.setCommandId(5);
		commandConfig.setErrFileName("someMultRemoteErrFile.txt");
		commandConfig.setOutFileName("someMultRemoteOutFile.txt");
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectoryMult");
		// Add another input file to the list of input files already started
		commandConfig.setInputFile("someOtherInputFile.txt");
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

		// assert that it was successful
		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * This tests a command which requires multiple input files to run locally
	 */
	@Test
	public void testMultipleInputFilesLocally() {

		// Set some things specific to the local command
		commandConfig.setCommandId(6);
		commandConfig.setErrFileName("someMultLocalErrFile.txt");
		commandConfig.setOutFileName("someMultLocalOutFile.txt");
		// Add another input file
		commandConfig.setInputFile("someOtherInputFile.txt");
		commandConfig.setExecutable("./test_code_execution.sh ${inputFile0} ${inputFile1}");
		commandConfig.setAppendInput(false);
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
	 * This function just returns the local hostname of your local computer. It is
	 * useful for testing a variety of local commands.
	 * 
	 * @return - String - local hostname
	 */
	protected static String getLocalHostname() {
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
