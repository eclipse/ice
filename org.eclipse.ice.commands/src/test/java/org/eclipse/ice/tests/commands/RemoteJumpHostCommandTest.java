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

import static org.junit.Assert.*;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.RemoteJumpHostCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Joe Osborn
 *
 */
public class RemoteJumpHostCommandTest {

	/**
	 * A connection configuration for the jump connection (i.e. intermediate connection)
	 */
	static ConnectionConfiguration jumpConfig = new ConnectionConfiguration();

	/**
	 * A command configuration to run a test script
	 */
	static CommandConfiguration configuration = new CommandConfiguration();
	
	/**
	 * A connection configuration for the final (destination) host for the job
	 * to run on
	 */
	static ConnectionConfiguration finalConfig = new ConnectionConfiguration();
	
	/**
	 * Creates the dummy connection to be used for the tests
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
		String credFile = "/tmp/ice-remote-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			credFile = "C:\\Users\\Administrator\\ice-remote-creds.txt";
		}
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		finalConfig.setAuthorization(auth);
		finalConfig.setName("dummyConnection");
		// Delete the remote working directory when finished since we don't want the
		// dummy host piling up with random directories
		finalConfig.deleteWorkingDirectory(true);

		ConnectionAuthorizationHandler finalAuth = authFactory.getConnectionAuthorizationHandler("keypath",
				"~/.ssh/denisovankey");
		finalAuth.setHostname("denisovan");
		finalAuth.setUsername("4jo");
		// Set it
		jumpConfig.setAuthorization(auth);
		jumpConfig.setName("hopConnection");
		jumpConfig.deleteWorkingDirectory(false);
		
		ConnectionManagerFactory.getConnectionManager().openConnection(jumpConfig);

		// Create a command configuration corresponding to a python script
		configuration = new CommandConfiguration();
		configuration.setNumProcs("1");
		configuration.setInstallDirectory("");
		configuration.setOS(System.getProperty("os.name"));
		configuration.setExecutable("test_python_script.py");
		configuration.setInterpreter("python");
		configuration.setCommandId(9);
		configuration.setErrFileName("somePythErrFile.txt");
		configuration.setOutFileName("somePythOutFile.txt");
		configuration.setAppendInput(true);
		configuration.addInputFile("inputfile", "someInputFile.txt");
		configuration.addInputFile("inputfile2", "someOtherInputFile.txt");
		configuration.setWorkingDirectory("/tmp/pythonDir");
		configuration.setRemoteWorkingDirectory("/tmp/pythonTest");

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Function which tests the creation of the command
	 */
	@Test
	public void testRemoteJumpHostCommand() {
		// Make a command and check that the connections were opened
		RemoteJumpHostCommand command = new RemoteJumpHostCommand(configuration, jumpConfig, finalConfig);

		String jumpConnectionName = command.getConnectionConfiguration().getName();
		assert (jumpConnectionName.equals(jumpConfig.getName()));
	
		// Check that the jump connection was opened
		assert(ConnectionManagerFactory.getConnectionManager().isConnectionOpen(jumpConnectionName));
		// Check that the second connection was opened
		assert(ConnectionManagerFactory.getConnectionManager().isConnectionOpen(finalConfig.getName()));
	}

	/**
	 * Function which tests the execution of the remote jump host command
	 */
	@Test
	public void testRemoteJumpHostCommandExecute() {

		// Make a command 
		RemoteJumpHostCommand command = new RemoteJumpHostCommand(configuration, jumpConfig, finalConfig);
		// execute the command
		CommandStatus status = command.execute();

		// Check that it finished properly
		assert (status == CommandStatus.SUCCESS);
	
	}
}
