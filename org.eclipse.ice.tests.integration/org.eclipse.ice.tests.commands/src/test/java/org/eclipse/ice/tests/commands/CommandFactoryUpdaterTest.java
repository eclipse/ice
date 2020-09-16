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

import java.io.IOException;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.EmailUpdateHandler;
import org.eclipse.ice.commands.HTTPCommandUpdateHandler;
import org.eclipse.ice.commands.ICommandUpdateHandler;
import org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.Test;

/**
 * This class tests local commands in conjunction with a given
 * ICommandUpdaterHandler class. The class is not intended to test the Command
 * logic, but rather the updater logic associated with the Command.
 *
 * @author Joe Osborn
 *
 */
public class CommandFactoryUpdaterTest {

	/**
	 * A TDP for collecting config files
	 */
	private TestDataPath dataPath = new TestDataPath();

	/**
	 * This function tests a command with an HTTPUpdater
	 */
	@Test
	public void testCommandWithHTTPUpdater() {
		// Create and setup the command
		CommandConfiguration commandConfig = setupCommandConfiguration();
		commandConfig.setCommandId(3);

		ConnectionConfiguration connectionConfig = setupConnectionConfiguration();

		CommandFactory factory = new CommandFactory();
		Command command = null;
		try {
			command = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create an HTTP updater to be added to the command
		HTTPCommandUpdateHandler updater = new HTTPCommandUpdateHandler();
		updater.setHTTPAddress("someaddress.com");
		command.setUpdateHandler(updater);

		CommandStatus status = command.execute();

		assertEquals(CommandStatus.SUCCESS, status);

	}

	/**
	 * This function tests with real files to test an actual job processing with an
	 * email updater attached to the command, so that when the job finishes an email
	 * is sent
	 */
	 @Test
	public void testCommandWithEmailUpdater() {

		// Set some things specific to the local command
		CommandConfiguration commandConfig = setupCommandConfiguration();
		commandConfig.setCommandId(4);
		ConnectionConfiguration connectionConfig = setupConnectionConfiguration();

		// Get the command
		CommandFactory factory = new CommandFactory();
		Command localCommand = null;
		try {
			localCommand = factory.getCommand(commandConfig, connectionConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ICommandUpdateHandler updater = setupEmailUpdateHandler();
		localCommand.setUpdateHandler(updater);

		// Run it
		CommandStatus status = localCommand.execute();

		assertEquals(CommandStatus.SUCCESS, status);

	}

	/**
	 * Helper function to create and return a local connection configuration
	 *
	 * @return
	 */
	private ConnectionConfiguration setupConnectionConfiguration() {
		String hostname = CommandFactoryTest.getLocalHostname();
		// Make a default boring connection authorization
		ConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		handler.setHostname(hostname);
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
		connectionConfig.setAuthorization(handler);
		return connectionConfig;
	}

	/**
	 * Helper function to setup and create a local command configuration
	 *
	 * @return
	 */
	private CommandConfiguration setupCommandConfiguration() {
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setNumProcs("1");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory(System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/");
		commandConfig.setOS(System.getProperty("os.name"));
		commandConfig.setExecutable("./test_code_execution.sh");
		// If it is windows, configure the test to run on windows
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			commandConfig.setExecutable(".\\test_code_execution.ps1");
			commandConfig.setInterpreter("powershell.exe");
		}
		commandConfig.addInputFile("someInputFile", "someInputFile.txt");
		commandConfig.setAppendInput(true);
		commandConfig.setCommandId(1);
		commandConfig.setErrFileName("someLocalErrFile.txt");
		commandConfig.setOutFileName("someLocalOutFile.txt");

		return commandConfig;
	}

	/**
	 * Sets up the dummy email address via a text file credential for CI
	 *
	 * @return
	 */
	private EmailUpdateHandler setupEmailUpdateHandler() {
		// Get a text file with credentials

		String credFile = dataPath.resolve("commands/ice-email-creds.txt").toString();

		TxtFileConnectionAuthorizationHandler auth = new TxtFileConnectionAuthorizationHandler();
		auth.setOption(credFile);
		EmailUpdateHandler handler = new EmailUpdateHandler();
		handler.setCredHandler(auth);
		handler.setMailPort("587");
		return handler;
	}
}
