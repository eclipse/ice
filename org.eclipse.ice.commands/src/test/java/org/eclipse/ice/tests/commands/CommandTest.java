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
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.Command}.
 * @author Joe Osborn
 *
 */
public class CommandTest {

	
	/**
	 * An AtomicBoolean indicating whether or not the job should be local. Set in the 
	 * various test functions below.
	 */
	AtomicBoolean localjob = new AtomicBoolean();
	
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#Command()}
	 */
	@Test
	public void testLocalCommand() {
		
		localjob.set( true );
		
		// Use the already defined function in CommandFactoryTest to get the
		// local hostname
		CommandFactoryTest commandFactory = new CommandFactoryTest();
		String hostname = commandFactory.getLocalHostname();
		
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "./test_code_execution.sh" );
		executableDictionary.put( "inputFile" , "someInputFile.txt" );
		executableDictionary.put( "stdOutFileName",  "someOutFile.txt" );
		executableDictionary.put( "stdErrFileName",  "someErrFile.txt" );
		executableDictionary.put( "numProcs",  "1");
		executableDictionary.put( "os",  "osx");
		executableDictionary.put( "workingDirectory", "/Users/4jo/git/icefork2/org.eclipse.ice.commands/src/test/java/org/eclipse/ice/tests/commands");
		executableDictionary.put( "hostname", hostname);
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration(
				1, localjob, executableDictionary, true );
		
		Command localCommand = new LocalCommand(commandConfig);
		CommandStatus status = localCommand.execute();
		
		System.out.println("Command finished with: " + status);
		assert( status == CommandStatus.SUCCESS );
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#checkStatus(CommandStatus)}
	 */
	//@Test
	public void testCheckStatus() {
		
		System.out.println("\nTesting that Command::CheckStatus throws an exception for a bad status");
		// Create instances of a command and associated status
		CommandStatus status = CommandStatus.INFOERROR;
		Command command = new LocalCommand();
		
		// Check to make sure that an exception is thrown for a bad status
		try {
			command.checkStatus(status);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
