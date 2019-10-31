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
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class LocalCommandTest {

	/**
	 * Create some strings that correspond to an actual test script
	 */
	String executable = "./someExecutable.sh ${installDir}";
	String inputFile = "someInputFile.txt";
	String errorFile = "someErrFile.txt";
	String outputFile = "someOutFile.txt";
	String procs = "1";
	String installDir = "~/install";
	String workingDirectory = "/";
	String os = System.getProperty("os.name");
	String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

	/**
	 * Put these in a command configuration instance to use
	 */
	CommandConfiguration commandConfig = new CommandConfiguration();

	/**
	 * The connection used (in this case, it is a local connection)
	 */
	ConnectionConfiguration connection;

	/**
	 * Set up by establishing that the test connection is local
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		// Use the function already defined in the command factory to get the
		// local host name
		CommandFactoryTest factory = new CommandFactoryTest();
		String hostname = factory.getLocalHostname();

		connection = new ConnectionConfiguration();
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		connection.setAuthorization(authFactory.getConnectionAuthorizationHandler("local"));

		commandConfig.setCommandId(1);
		commandConfig.setExecutable(executable);
		commandConfig.addInputFile("inputFile", inputFile);
		commandConfig.setErrFileName(errorFile);
		commandConfig.setOutFileName(outputFile);
		commandConfig.setInstallDirectory(installDir);
		commandConfig.setWorkingDirectory(workingDirectory);
		commandConfig.setAppendInput(true);
		commandConfig.setNumProcs(procs);
		commandConfig.setOS(os);
	}

	/**
	 * This class runs at the end of test execution and just deletes the outfiles that 
	 * were created in the testing.
	 */
	@AfterClass
	public static void tearDownAfterClass() throws IOException, InterruptedException {
		System.out.println("processing tear down");
		String rm = "someLocalErrFile.txt someLocalOutFile.txt";
		ArrayList<String> command = new ArrayList<String>();
		// Build a command
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
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
	}
	
	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalCommand()} Tests check
	 * for proper configuration and checking of the LocalCommand member variables so
	 * that the Command can actually be run.
	 */
	@Test
	public void testLocalCommand() {

		System.out.println("Starting testLocalCommand");

		// Now make a "real" command configuration to test

		LocalCommand realCommand = new LocalCommand(connection, commandConfig);
		CommandStatus testStatus = realCommand.getStatus();
		System.out.println(testStatus);
		assert (testStatus == CommandStatus.PROCESSING);
		System.out.println("Finished testConfiguration\n");

	}

	/**
	 * This test tests the execute method in local command, which is responsible for
	 * actually processing and running a job.
	 */
	@Test
	public void testExecute() {
		System.out.println("begin normal test execute");
		// Set the CommandConfiguration class
		// See {@link org.eclipse.ice.commands.CommandConfiguration} for detailed info
		// on each
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable("./test_code_execution.sh");
		// Check if the OS is windows, and thus adjust the executable if so
		if (os.toLowerCase().contains("win")) {
			// Two slashes so that java doesn't read it as a tab "\t"
			executable = ".\\test_code_execution.ps1";
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

		// Make the command and execute it
		LocalCommand localCommand = new LocalCommand(connection, commandConfig);
		CommandStatus status = localCommand.execute();

		// Check that it completed correctly
		System.out.println("Command finished with: " + status);
		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Execute()}. This
	 * test should "fail" by default since the CommandConfiguration
	 * executableDictionary does not point to a real executable. This is a test of
	 * the API catching an incorrectly configured command. For a test of a fully
	 * functional command, see {@link org.eclipse.ice.commands.testCommandFactory()}
	 */
	@Test
	public void testBadExecute() {
		System.out.println("Starting testExecute with a non-existant executable.\n");

		CommandConfiguration badConfig = new CommandConfiguration();

		badConfig.setCommandId(2);
		badConfig.setExecutable("fake_exec.sh");
		badConfig.addInputFile("inputfile", "inputfile");
		badConfig.setErrFileName("someLocalErrFile.txt");
		badConfig.setOutFileName("someLocalOutFile.txt");
		badConfig.setInstallDirectory("installDir");
		badConfig.setWorkingDirectory(pwd);
		badConfig.setAppendInput(true);
		badConfig.setNumProcs("1");
		badConfig.setOS(os);

		LocalCommand testCommand = new LocalCommand(connection, badConfig);

		CommandStatus testStatus = testCommand.execute();

		assert (testStatus == CommandStatus.FAILED);
		System.out.println("Finished testExecute\n");
	}

}
