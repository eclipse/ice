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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

	public CommandFactoryTest() {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * This function tests with real files to test an actual job processing. For
	 * this test to work, make sure you change the workingDirectory to your actual
	 * workingDirectory where the Commands API lives.
	 */
	@Test
	public void testFunctionalLocalCommand() {

		/**
		 * Create a CommandConfiguration with the necessary information to execute a
		 * Command. See {@link org.eclipse.ice.commands.CommandConfiguration} for
		 * relevant member variables/constructor.
		 */

		// Get the present working directory
		String pwd = System.getProperty("user.dir");
		
		// Add the following directories where the tests live
		pwd += "/src/test/java/org/eclipse/ice/tests/commands/";
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.setInputFile("someInputFile.txt");
		commandConfig.setErrFileName("someErrFile.txt");
		commandConfig.setOutFileName("someOutFile.txt");
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);

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
		CommandConfiguration commandConfig = new CommandConfiguration();

		ConnectionConfiguration connectConfig = new ConnectionConfiguration(hostname);
		// Get the command
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(commandConfig, connectConfig);
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

		// Set the CommandConfiguration class
		CommandConfiguration commandConfiguration = new CommandConfiguration();
		commandConfiguration.setCommandId(1);
		commandConfiguration.setExecutable("./test_code_execution.sh");
		commandConfiguration.setInputFile("someInputFile.txt");
		commandConfiguration.setErrFileName("someErrFile.txt");
		commandConfiguration.setOutFileName("someOutFile.txt");
		commandConfiguration.setNumProcs("1");
		commandConfiguration.setInstallDirectory("~/installDir");
		commandConfiguration.setWorkingDirectory("~/some_nonexistent_directory");
		commandConfiguration.setAppendInput(true);

		ConnectionConfiguration connectConfig = new ConnectionConfiguration(hostname);
		// Get the command
		Command localCommand2 = null;
		try {
			localCommand2 = factory.getCommand(commandConfiguration, connectConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run it and expect that it fails
		CommandStatus status2 = localCommand2.execute();

		assert (status2 == CommandStatus.FAILED);
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
