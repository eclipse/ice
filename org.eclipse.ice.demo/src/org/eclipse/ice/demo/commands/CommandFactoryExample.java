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

		return;
	}

	/**
	 * This method runs a test dummy script on one's local computer. The dummy script is locating
	 * in the JUnit test directory of the Commands API project. 
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
		String script = scriptDir + "test_code_execution.sh";

		// Create the input file path
		String inputFile = scriptDir + "someInputFile.txt";

		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable(script);
		commandConfig.setInputFile(inputFile);
		commandConfig.setErrFileName("someErrFile.txt");
		commandConfig.setOutFileName("someOutFile.txt");
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setOS(System.getProperty("os.name"));
	
		// Make a ConnectionConfiguration to indicate that we want to run locally
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration(hostname);

		// Get the command
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it
		CommandStatus status = localCommand.execute();
		
		System.out.println("Status of Command after execution: " + status);
		
		// Get a string of the output that is produced from the job
		String output = commandConfig.getStdOutputString();
		
		
		return;
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
