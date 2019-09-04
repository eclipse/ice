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

import java.io.IOException;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.Command}.
 * 
 * @author Joe Osborn
 *
 */
public class CommandTest {

	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()} with a
	 * particular instance of LocalCommand.
	 */
	@Test
	public void testLocalCommand() {

		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration();
		commandConfig.setCommandId(1);
		commandConfig.setExecutable("./test_code_execution.sh");
		commandConfig.setInputFile("someInputFile.txt");
		commandConfig.setErrFileName("someErrFile.txt");
		commandConfig.setOutFileName("someOutFile.txt");
		commandConfig.setInstallDirectory("");
		commandConfig.setWorkingDirectory("/Users/4jo/git/icefork2/org.eclipse.ice.commands/src/test/java/org/eclipse/ice/tests/commands");
		commandConfig.setAppendInput(true);
		commandConfig.setOS("osx");
		commandConfig.setNumProcs("1");
		
		// Use the function already defined in the command factory to get the
		// local host name
		CommandFactoryTest factory = new CommandFactoryTest();
		String hostname = factory.getLocalHostname();
		ConnectionConfiguration connection = new ConnectionConfiguration(hostname);

		Command localCommand = new LocalCommand(connection, commandConfig);
		CommandStatus status = localCommand.execute();

		System.out.println("Command finished with: " + status);
		assert (status == CommandStatus.SUCCESS);

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.Command#checkStatus(CommandStatus)}
	 */
	// @Test
	public void testCheckStatus() {

		System.out.println("\nTesting that Command::CheckStatus throws an exception for a bad status");
		// Create instances of a command and associated status
		CommandStatus status = CommandStatus.INFOERROR;
		Command command = new LocalCommand();

		// Check to make sure that an exception is thrown for a bad status
		try {
			command.checkStatus(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
