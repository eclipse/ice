/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Examples for running Commands API package org.eclipse.ice.commands 
 *   Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.demo.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;

/**
 * This class shows an example for how to use the CommandFactory class to
 * generate a Command which can execute on a local machine.
 * 
 * @author Joe Osborn
 *
 */
public class CommandFactoryExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Run an example test script
		runLocalCommand();

		// Run an example test script on a remote host
		runRemoteCommand();

		// Run an example python script
		runPythonScript();

		return;
	}

	/**
	 * This method runs a test dummy script on a remote host. The host ssh
	 * credentials are stored in a file in this case, for the CI build pipeline
	 * within gitlab to work. However, one could set their username/hostname in the
	 * configuration, and then enter their password when prompted.
	 */
	static void runRemoteCommand() {

		/**
		 * Create a CommandConfiguration with the necessary information to execute the
		 * command. See {@link org.eclipse.ice.commands.CommandConfiguration} for
		 * relevant member variables/constructor that one can set up.
		 */
		// Create a factory to get the Command
		CommandFactory factory = new CommandFactory();

		// Get the present working directory
		String pwd = System.getProperty("user.dir");

		// Create the path relative to the current directory where the test script lives
		String scriptDir = pwd + "/../org.eclipse.ice.commands/src/test/java/org/eclipse/ice/tests/commands/";

		String script = "./test_code_execution.sh";

		String inputFile = "someInputFile.txt";

		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(2); // Give an ID to the job for tracking
		// Set the executable. Alternatively one could type setExecutable("ls -lrt")
		// for example, to list the directories in the remote host
		commandConfig.setExecutable(script);
		commandConfig.addInputFile("inputfile", inputFile); // Set the input file for the script to run
		commandConfig.setErrFileName("someRemoteErrFile.txt"); // Give an error file name
		commandConfig.setOutFileName("someRemoteOutFile.txt"); // Give an out file name
		commandConfig.setNumProcs("1"); // Set the number of processes
		commandConfig.setInstallDirectory(""); // Set the install directory where libraries live, if needed
		commandConfig.setWorkingDirectory(scriptDir); // Set the working directory where the scripts live
		commandConfig.setAppendInput(true); // Append the input file to the script
		commandConfig.setOS(System.getProperty("os.name")); // Get the OS
		// Set the remote working directory for the process to be performed
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");

		// Get the connection configuration credentials for the dummy host
		ConnectionConfiguration connectionConfig = makeDumConnectionConfig();

		// Get the command
		Command command = null;
		try {
			command = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run the command
		CommandStatus status = command.execute();

		// Ensure it finished properly
		assert (status == CommandStatus.SUCCESS);
	}

	/**
	 * This function creates a connection configuration for the dummy remote host
	 * used in CI testing. Create a ConnectionConfiguration with the necessary
	 * information to open a remote connection. See
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration} for relevant member
	 * variables that one can set. Note about passwords: The password can be set to
	 * open the connection; however, it is in general not recommended since Strings
	 * are immutable and thus the password could in principle be identified with a
	 * code profiler. The password is entered here for the dummy ssh account set up
	 * for the CI build pipeline. Users have two options:
	 * 
	 * 1. They can set their password as shown below
	 * 
	 * 2. They can not set a password and they will be prompted for the password at
	 * the console once the connection is trying to be established. This password is
	 * received in an array of chars and subsequently deleted once the connection is
	 * established.
	 */
	private static ConnectionConfiguration makeDumConnectionConfig() {

		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		// Set the connection configuration to a dummy remote connection
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// dummy remote host credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text",
				"/tmp/analysis-remote-creds.txt");
		/**
		 * Alternatively, one can authorize with their password at the console line by
		 * performing the following set of code
		 * 
		 * ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("console");
		 * auth.setHostname("hostname");
		 * auth.setUsername("username");
		 */
		// Set it so that the connection can authorize itself
		connectionConfig.setAuthorization(auth);
		// Give the connection a name
		connectionConfig.setName("dummyConnection");

		// Delete the remote working directory once we are finished running the job
		connectionConfig.setDeleteWorkingDirectory(true);

		return connectionConfig;
	}

	/**
	 * This method runs a test dummy script on one's local computer. The dummy
	 * script is locating in the JUnit test directory of the Commands API project.
	 */
	static void runLocalCommand() {

		/**
		 * Create a CommandConfiguration with the necessary information to execute a
		 * Command. See {@link org.eclipse.ice.commands.CommandConfiguration} for
		 * relevant member variables/constructor.
		 */

		// Create a factory to get the Command
		CommandFactory factory = new CommandFactory();

		// Get the local hostname for processing
		String hostname = getLocalHostname();

		// Get the present working directory
		String pwd = System.getProperty("user.dir");

		// Create the path relative to the current directory where the test script lives
		String scriptDir = pwd + "/../org.eclipse.ice.commands/src/test/java/org/eclipse/ice/tests/commands/";

		// Create the script path
		String script = "./test_code_execution.sh";

		// Create the input file path
		String inputFile = "someInputFile.txt";

		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable(script);
		commandConfig.addInputFile("inputFile", inputFile);
		commandConfig.addInputFile("otherInputFile", "someOtherInputFile.txt");
		commandConfig.setErrFileName("someLocalErrFile.txt");
		commandConfig.setOutFileName("someLocalOutFile.txt");
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(scriptDir);
		commandConfig.setAppendInput(true);
		commandConfig.setOS(System.getProperty("os.name"));

		// Make a ConnectionConfiguration to indicate that we want to run locally
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to
		// "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		connectionConfig.setAuthorization(auth);

		// Get the command
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it
		CommandStatus status = localCommand.execute();

		// Get a string of the output that is produced from the job
		String output = commandConfig.getStdOutputString();

		return;
	}

	/**
	 * This function shows an example of how to run with a python script. The
	 * functionality is very similar to above, except that the interpreter needs to
	 * be specified in the CommandConfiguration. This appends the string "python"
	 * before the executable name, allowing it to run in python.
	 */
	public static void runPythonScript() {

		System.out.println("Testing python script");
		// Get the present working directory
		String pwd = System.getProperty("user.dir");

		// Create the path relative to the current directory where the test script lives
		String scriptDir = pwd + "/../org.eclipse.ice.commands/src/test/java/org/eclipse/ice/tests/commands/";

		// Create a command configuration corresponding to a python script
		// This is very similar to above, except that we need to specifically
		// set the interpreter to be python.
		CommandConfiguration configuration = new CommandConfiguration();
		configuration.setExecutable("test_python_script.py");
		// Here we set the interpreter to run in python, instead of in e.g. bash
		configuration.setInterpreter("python");
		configuration.setCommandId(9);
		configuration.setErrFileName("somePythErrFile.txt");
		configuration.setOutFileName("somePythOutFile.txt");
		configuration.setNumProcs("1");
		configuration.setInstallDirectory("");
		configuration.setOS(System.getProperty("os.name"));
		configuration.setAppendInput(true);
		configuration.addInputFile("inputfile", "someInputFile.txt");
		configuration.addInputFile("inputfile2", "someOtherInputFile.txt");
		configuration.setWorkingDirectory(scriptDir);

		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to
		// "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		connectionConfig.setAuthorization(auth);

		// Create a factory to get the Command
		CommandFactory factory = new CommandFactory();

		// Get the command and run it
		Command command = null;
		try {
			command = factory.getCommand(configuration, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		CommandStatus status = command.execute();

	}

	/**
	 * This function just returns the local hostname of your local computer
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
