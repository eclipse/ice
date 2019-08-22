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

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * @author Joe Osborn
 *
 */
public class LocalCommandTest {

	AtomicBoolean localjob = new AtomicBoolean();
	HashMap<String, String> executableDictionary;
	
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		// Set up some default instance variables
		localjob.set( true );
		executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "someExecutable.sh ${installDir}" );
		executableDictionary.put( "inputFile" , "someInputFile.txt" );
		executableDictionary.put( "stdOutFileName",  "someOutFile.txt" );
		executableDictionary.put( "stdErrFileName",  "someErrFile.txt" );
		executableDictionary.put( "installDir" ,  "~/install");
		executableDictionary.put( "numProcs",  "1");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	
	
	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalCommand()}
	 */
	@Test
	public void testLocalCommand() {
		
		System.out.println("Starting testLocalCommand");
		
		// Test that, in the lack of dictionary setting, the default constructor returns a 
		// CommandStatus error
		LocalCommand testCommand = new LocalCommand();
		CommandStatus testStatus = testCommand.getStatus();
		assert( testStatus == CommandStatus.INFOERROR );
		
		// Now make a "real" command configuration to test
		CommandConfiguration commandConfig = new CommandConfiguration(
				3, localjob, executableDictionary, true );
		
		LocalCommand realCommand = new LocalCommand(commandConfig);
		testStatus = realCommand.getStatus();
		
		assert( testStatus == CommandStatus.RUNNING );
		System.out.println("Finished testConfiguration\n");	
	
	}
	

	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Execute()}
	 */
	@Test
	public void testExecute() {
		System.out.println("Starting testExecute\n");
		
		LocalCommand testCommand = new LocalCommand(new CommandConfiguration(2, localjob,
				executableDictionary, true));
		
		CommandStatus testStatus = testCommand.execute();
		
		System.out.println("Finished testExecute\n");
	}
	
	
	
	
}
