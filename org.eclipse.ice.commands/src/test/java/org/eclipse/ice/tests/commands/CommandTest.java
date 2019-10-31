/**
 * /*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.tests.commands;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.LocalCommand;
import org.eclipse.ice.commands.RemoteCommand;
import org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.Command}.
 * 
 * @author Joe Osborn
 *
 */
public class CommandTest {

	/**
	 * Get the present working directory Add the following directories where the
	 * tests live
	 */
	String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()} with a
	 * particular instance of a RemoteCommand
	 */
	@Test
	public void testRemoteCommand() {

		System.out.println("Test a remote command!");
		// Set up a command configuration with instructions on how to run the script
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(2); // an id indicating the job number
		commandConfig.setExecutable("./test_code_execution.sh"); // the shell script to execute
		commandConfig.addInputFile("someInputFile", "someInputFile.txt"); // an input file needed by the script
		commandConfig.setErrFileName("someRemoteErrFile.txt"); // a file to contain errors thrown
		commandConfig.setOutFileName("someRemoteOutFile.txt"); // a file to contain the output
		commandConfig.setInstallDirectory(""); // no install dir needed for this script
		commandConfig.setWorkingDirectory(pwd); // working directory which contains the files
		commandConfig.setAppendInput(true); // append the input file name to the script executable command
		commandConfig.setNumProcs("1"); // number of processes is 1
		commandConfig.setOS(System.getProperty("os.name"));
		// Set the remote working directory, where the command will be processed
		commandConfig.setRemoteWorkingDirectory("/tmp/remoteCommandTestDirectory");
		// Set the connection configuration to a dummy remote connection

		// Make the ConnectionConfiguration and set it up
		ConnectionConfiguration connectConfig = new ConnectionConfiguration();
		// Make the connection configuration
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text",
				"/tmp/ice-remote-creds.txt");
		// Set it
		connectConfig.setAuthorization(auth);
		connectConfig.setName("dummyConnection");
		connectConfig.deleteWorkingDirectory(true);

		// Make the command and execute it
		Command remoteCommand = new RemoteCommand(commandConfig, connectConfig, null);
		CommandStatus status = remoteCommand.execute();

		// Assert that it finished correctly
		assert (status == CommandStatus.SUCCESS);

		// You can get the output in string form if desired
		String output = remoteCommand.getCommandConfiguration().getStdOutputString();
		System.out.println(output);
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()} with a
	 * particular instance of LocalCommand.
	 */
	@Test
	public void testLocalCommand() {
		System.out.println("\n\n\n\nTest a local command!");
		String os = System.getProperty("os.name");
		// Set the CommandConfiguration class
		// See {@link org.eclipse.ice.commands.CommandConfiguration} for detailed info
		// on each
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable("./test_code_execution.sh");
		// If the os is windows, set the executable appropriately
		if (os.toLowerCase().contains("win")) {
			// two slashes so that java doesn't read it as a tab
			commandConfig.setExecutable(".\\test_code_execution.ps1");
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

		// Use the function already defined in the command factory to get the
		// local host name
		CommandFactoryTest factory = new CommandFactoryTest();
		String hostname = factory.getLocalHostname();

		// Create a connectionConfiguration to indicate a local command
		ConnectionConfiguration connection = new ConnectionConfiguration();
		ConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		handler.setHostname(hostname);
		connection.setAuthorization(handler);

		// Make the command and execute it
		Command localCommand = new LocalCommand(connection, commandConfig);
		CommandStatus status = localCommand.execute();

		// Check that it completed correctly
		System.out.println("Command finished with: " + status);
		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.Command#checkStatus(CommandStatus)}
	 */
	@Test
	public void testCheckStatus() {

		System.out.println("\n\n\n\nTesting that Command::CheckStatus throws an exception for a bad status");
		// Create instances of a command and associated (bad) status
		CommandStatus status = CommandStatus.INFOERROR;
		Command command = new LocalCommand();

		// Check to make sure that an exception is thrown for a bad status when the
		// status is checked
		assert (!command.checkStatus(status));
	}
	
	/**
	 * This function tests a command with an acceptable exit value that is non zero, for 
	 * example the grep command for a file in a directory with no files (grep returns 1, but
	 * this is just indicating that it did not find anything).
	 */
	@Test
	public void testExceptionalExitValue() {
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
        // Set the connection configuration to a dummy remote connection
        // Get a factory which determines the type of authorization
        ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
        // Request a ConnectionAuthorization of type text file which contains the
        // dummy remote host credentials
        ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text",
                "/tmp/ice-remote-creds.txt");
        // Set it so that the connection can authorize itself
        connectionConfig.setAuthorization(auth);
        // Give the connection a name
        connectionConfig.setName("DummyConnection");

        String script = "ls -lt /home/dummy/emptyDir | grep \"^-\"";

        CommandConfiguration commandConfiguration = new CommandConfiguration();
        commandConfiguration.setCommandId(2);
        commandConfiguration.setExecutable(script);
        commandConfiguration.setErrFileName("someRemoteErrFile.txt");
        commandConfiguration.setOutFileName("outputFileName.txt");
        commandConfiguration.setNumProcs("1");
        commandConfiguration.setOS(System.getProperty("os.name"));
        commandConfiguration.setWorkingDirectory("/");
        commandConfiguration.setRemoteWorkingDirectory("");
        
        System.out.println("script is : " + script);
        
        // Get the command
        Command command = new RemoteCommand(commandConfiguration, connectionConfig, null);
        
        // Run the command
        CommandStatus status = command.execute(); 
        
        assert (status == CommandStatus.SUCCESS);
        // Check that the empty directory really did return nothing from the grep command
        assert (command.getCommandConfiguration().getStdOutputString().equals(""));
	}

}
