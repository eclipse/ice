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
import java.util.concurrent.atomic.AtomicBoolean;

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
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}



	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()} 
	 * execution chain with a fully functional command dictionary
	 */
	@Test
	public void testFunctionalLocalCommand() {
		AtomicBoolean localjob = new AtomicBoolean();
		localjob.set( true );
		
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
		 */
		/**
		 * This is a test with non-existing files to test the exception catching
		 */
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "./boring_executable.sh" );
		executableDictionary.put( "inputFile" , "someInputFile.txt" );
		executableDictionary.put( "stdOutFileName",  "someOutFile.txt" );
		executableDictionary.put( "stdErrFileName",  "someErrFile.txt" );
		executableDictionary.put( "installDir" ,  "/Users/4jo/install");
		executableDictionary.put( "numProcs",  "1");
		executableDictionary.put( "os",  "osx");
		executableDictionary.put( "workingDirectory",  "/Users/4jo/dummy_localcommand_test");
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration(
				1, localjob, executableDictionary, true );
		
		String hostname = getLocalHostname();
		
		// Get the command
		Command localCommand = CommandFactory.getCommand(hostname, commandConfig);
		
		// Run it
		CommandStatus status = localCommand.execute();
		
		System.out.println("Status of functional command: " + status);
		
	}
	
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}
	 * and for the whole {@link org.eclipse.ice.commands.LocalCommand#execute()} 
	 * execution chain with an uncompleted Command dictionary
	 */
	@Test
	public void testNonFunctionalLocalCommand() {
		
		System.out.println("\nTesting a command where not enough command information was provided.");
		AtomicBoolean localjob = new AtomicBoolean();
		localjob.set( true );
		
		// Create a HashMap that doesn't have all of the necessary ingredients
		// a good job should have
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "./boring_executable.sh" );
		
		
		// Set the CommandConfiguration class
		CommandConfiguration commandConfig = new CommandConfiguration(
				2, localjob, executableDictionary, true );
		
		// Get the command
		Command localCommand = CommandFactory.getCommand(getLocalHostname(), commandConfig);
						
		// Run it
		CommandStatus status = localCommand.execute();
						
		System.out.println("Status of non-functional command: " + status);
				
				
		
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
