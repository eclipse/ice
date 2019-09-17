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
import java.util.HashMap;
import java.util.Scanner;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.RemoteCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.RemoteCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCommandTest {

	HashMap<String, String> executableDictionary;

	CommandConfiguration commandConfig;

	ConnectionConfiguration connectConfig;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		// Get the present working directory
		String pwd = System.getProperty("user.dir");

		// Add the following directories where the tests live
		pwd += "/src/test/java/org/eclipse/ice/tests/commands/";

		commandConfig = new CommandConfiguration();

		// Set the command to confiugre to a dummy hello world command
		commandConfig.setCommandId(0);
		commandConfig.setExecutable("./someExecutable.sh ${installDir}");
		commandConfig.setInputFile("someInputFile.txt");
		commandConfig.setErrFileName("someErrFile.txt");
		commandConfig.setOutFileName("someOutFile.txt");
		commandConfig.setInstallDirectory("~/install");
		commandConfig.setWorkingDirectory(pwd);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs("1");

		// Set the connection configuration to a dummy remote connection

		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\n");
		// Get the credentials for the dummy remote account

		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		connectConfig = new ConnectionConfiguration();
		connectConfig.setHostname(hostname);
		connectConfig.setUsername(username);
		connectConfig.setPassword(password);
		connectConfig.setName("dummyConnection");
		connectConfig.setWorkingDirectory("/tmp/remoteCommandTestDirectory");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteCommand()}
	 */
	@Test
	public void testRemoteCommand() {
		System.out.println("Testing remote command configuration");

		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);

		CommandStatus status = command.getStatus();

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

		connectConfig.setUsername("someBadUsername");
		connectConfig.setHostname("someBadHostname");
		connectConfig.setPassword("someBadPassword");
		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);

		assert (command.getStatus() == CommandStatus.INFOERROR);
	}

	/**
	 * Test method for executing remote command
	 * {@link org.eclipse.ice.commands.RemoteCommand#execute()}
	 */
	@Test
	public void testExecute() {
		System.out.println("\n\n\nTest remote command execute");
		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);

		CommandStatus status = command.execute();

		assert (status == CommandStatus.SUCCESS);

		System.out.println("Finished testing remote command execute");
	}

}
