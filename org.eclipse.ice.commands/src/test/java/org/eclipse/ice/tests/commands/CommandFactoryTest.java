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


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.CommandFactory}.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class CommandFactoryTest {


	/**
	 * The hostname for which the job should run on. Default to local host name for now
	 */
	String hostname = getLocalHostname();
	

	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()} 
	 * execution chain with a fully functional command dictionary
	 */
	
	public CommandFactoryTest() {}
	

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	
	@Test
	public void testFunctionalLocalCommand() {
		
		
		String hostname = getLocalHostname();
		
		/**
		 * Create a HashMap which holds the executable instructions
		 * Requirements for a Command to work:
		 * executable - executable to be run
		 * inputFile - input file, can set to "" if no input file
		 * stdOutFileName - output file name
		 * stdErrFileName - error file name
		 * numProcs - number of processes
		 * os - operating system to execute command on
		 * workingDirectory - directory in which to execute the job
		 * hostname - the hostname on which the job is to be hosted
		 */
		/**
		 * This is a test with real files to test an actual job processing.
		 * For this test to work, make sure you change the workingDirectory
		 * to your actual workingDirectory where the Commands API lives
		 */
		
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "./test_code_execution.sh" );
		executableDictionary.put( "inputFile" , "someInputFile.txt" );
		executableDictionary.put( "stdOutFileName",  "someOutFile.txt" );
		executableDictionary.put( "stdErrFileName",  "someErrFile.txt" );
		executableDictionary.put( "numProcs",  "1");
		executableDictionary.put( "os",  "osx");
		executableDictionary.put( "workingDirectory",  "/Users/4jo/git/icefork2/org.eclipse.ice.commands/src/test/java/org/eclipse/ice/tests/commands");
		executableDictionary.put( "hostname", hostname);
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration(
				1, executableDictionary, true );
		
		
		
		// Get the command
		Command localCommand = CommandFactory.getCommand(commandConfig);
		
		// Run it
		CommandStatus status = localCommand.execute();
		
		assert( status == CommandStatus.SUCCESS );
		System.out.println("Status of functional command: " + status);
		
	}
	
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()} 
	 * execution chain with an uncompleted Command dictionary. This is function is 
	 * intended to test some of the exception catching.
	 */
	//@Test
	public void testNonFunctionalLocalCommand() {
		
		System.out.println("\nTesting some commands where not enough command information was provided.");
	
		
		
		// Create a HashMap that doesn't have all of the necessary ingredients
		// a good job should have
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "./test_code_execution.sh" );
		
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration(
				2, executableDictionary, true );
		
		// Get the command
		Command localCommand = CommandFactory.getCommand(commandConfig);
						
		// Run it and expect that it fails
		CommandStatus status = localCommand.execute();
						
		assert( status == CommandStatus.INFOERROR );
				
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()} 
	 * execution chain with an uncompleted Command dictionary. This function is 
	 * intended to test some of the exception catching.
	 */
	//@Test
	public void testIncorrectWorkingDirectory() {
		/**
		 * Run another non functional command, with a non existing working directory
		 */
		
		System.out.println("\nTesting some commands where not enough command information was provided.");
		
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "./test_code_execution.sh" );
		executableDictionary.put( "inputFile" , "someInputFile.txt" );
		executableDictionary.put( "stdOutFileName",  "someOutFile.txt" );
		executableDictionary.put( "stdErrFileName",  "someErrFile.txt" );
		executableDictionary.put( "numProcs",  "1");
		executableDictionary.put( "os",  "osx");
		executableDictionary.put( "workingDirectory",  "~/some_nonexistant_directory");
		executableDictionary.put( "hostname", hostname);
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfiguration = new CommandConfiguration(
				1, executableDictionary, true );
		
		
		
		// Get the command
		Command localCommand2 = CommandFactory.getCommand(commandConfiguration);
		
		// Run it and expect that it fails
		CommandStatus status2 = localCommand2.execute();
	
		
	}
	
	
	
	
	
	/**
	 * This function just returns the local hostname of your local computer. 
	 * It is useful for testing a variety of local commands.
	 * @return - String - local hostname
	 */
	protected String getLocalHostname() {
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
				
		String hostname = addr.getHostName();
		
		return hostname;
	}

}
