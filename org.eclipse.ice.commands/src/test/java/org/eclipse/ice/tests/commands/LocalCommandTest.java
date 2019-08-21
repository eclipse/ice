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

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * @author Joe Osborn
 *
 */
public class LocalCommandTest {

	AtomicBoolean localjob = new AtomicBoolean();
	Dictionary<String, String> executableDictionary;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		
		// Set up some default instance variables
		localjob.set( true );
		executableDictionary = new Hashtable<String, String>();
		executableDictionary.put( "executable" , "someExecutable.sh" );
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
		
		// Test the default constructor
		LocalCommand defaultLocalCommand = new LocalCommand();
		
		System.out.println( "Some default LocalCommand constructor test values" );
		System.out.println( defaultLocalCommand.GetConfiguration().GetCommandId() );
		System.out.println( defaultLocalCommand.GetConfiguration().GetFullCommand() );
		System.out.println( defaultLocalCommand.GetStatus()+"\n" );
		
		// Test a constructor with a particular CommandConfiguration
		CommandConfiguration interestingCommandConfig = new CommandConfiguration(
				4, localjob, executableDictionary, true );
		LocalCommand interestingLocalCommand = new LocalCommand( interestingCommandConfig );
		
		System.out.println( "The non-default LocalCommand constructor test" );
		System.out.println( interestingLocalCommand.GetConfiguration().GetCommandId() );
		System.out.println( interestingLocalCommand.GetConfiguration().GetFullCommand() );
		System.out.println( interestingLocalCommand.GetStatus() );
		System.out.println( interestingLocalCommand.GetConfiguration().GetExecDictionary().get("executable") );
		
	
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.Command#CreateOutputHeader()}
	 */
	@Test
	public void testCreateOutputHeader() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String logName = "some LogFile";
		LocalCommand defaultLocalCommand = new LocalCommand();
		
		String header = defaultLocalCommand.CreateOutputHeader(logName);
		System.out.println("\n The header string is: \n");
		System.out.println(header);
	
	}
	
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#FixExecutableName()}
	 */
	@Test
	public void testFixExecutableName() {
				
		CommandConfiguration commandConfig = new CommandConfiguration(
						4, localjob, executableDictionary, true );
		
		//Make a dummy test command
		LocalCommand testCommand = new LocalCommand(commandConfig);
				
		String executableName = testCommand.FixExecutableName();
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Execute(Dictionary)}
	 */
	@Test
	public void testExecute() {
		LocalCommand testCommand = new LocalCommand();
		CommandStatus testStatus = testCommand.Execute();
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Launch()}
	 */
	@Test
	public void testLaunch() {
		// Test that, in the lack of dictionary setting, Launch returns a 
		// CommandStatus error
		LocalCommand testCommand = new LocalCommand();
		CommandStatus testStatus = testCommand.Launch();
		assert( testStatus == CommandStatus.INFOERROR );
		
		CommandConfiguration commandConfig = new CommandConfiguration(
				4, localjob, executableDictionary, true );
		
		testCommand.SetConfiguration(commandConfig);
		
		testStatus = testCommand.Launch();
		
		assert( testStatus == CommandStatus.RUNNING );
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Run()}
	 */
	public void testRun() {
		fail("Not yet implemented");
	}
	
}
