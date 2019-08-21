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
		
		// Test the default constructor
		LocalCommand defaultLocalCommand = new LocalCommand();
		
		System.out.println( "Some default LocalCommand constructor test values" );
		System.out.println( defaultLocalCommand.GetConfiguration().GetCommandId() );
		System.out.println( defaultLocalCommand.GetConfiguration().GetFullCommand() );
		System.out.println( defaultLocalCommand.GetStatus() + "\n" );
		
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
		System.out.println("Starting testFixExecutableName\n");
		
		//Make a dictionary to test a few features with
		Dictionary<String, String> dict = new Hashtable<String, String>();
		dict.put( "executable", "fixexecutableTest.sh");
		dict.put( "inputFile" , "fixexecutableInputTest.txt" );
		dict.put( "stdOutFileName",  "someOutFile.txt" );
		dict.put( "stdErrFileName",  "someErrFile.txt" );
		dict.put( "numProcs",  "1");
		
		CommandConfiguration commandConfig = new CommandConfiguration(
						1, localjob, dict, true );
		
		//Make a dummy test command
		LocalCommand testCommand = new LocalCommand(commandConfig);
		String executableName = testCommand.FixExecutableName();
		
		
		Dictionary<String, String> dict2 = new Hashtable<String, String>();
		dict2.put( "executable", "fixexecutableTest.sh ${installDir} ${inputFile}");
		dict2.put( "inputFile" , "fixexecutableInputTest.txt" );
		dict2.put( "installDir" , "~/testinstall");
		dict2.put( "numProcs", "2");
		
		commandConfig = new CommandConfiguration(2, localjob, dict2, false);
		LocalCommand testCommand2 = new LocalCommand(commandConfig);
		executableName = testCommand2.FixExecutableName();
		
		
		System.out.println("Finished testFixExecutableName\n");
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Execute(Dictionary)}
	 */
	@Test
	public void testExecute() {
		System.out.println("Starting testExecute\n");
		
		LocalCommand testCommand = new LocalCommand(new CommandConfiguration(2, localjob,
				executableDictionary, true));
		CommandStatus testStatus = testCommand.execute();
		
		System.out.println("Finished testExecute\n");
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Launch()}
	 */
	@Test
	public void testLaunch() {
		System.out.println("Starting testLaunch");
		// Test that, in the lack of dictionary setting, Launch returns a 
		// CommandStatus error
		LocalCommand testCommand = new LocalCommand();
		CommandStatus testStatus = testCommand.Launch();
		assert( testStatus == CommandStatus.INFOERROR );
		
		CommandConfiguration commandConfig = new CommandConfiguration(
				3, localjob, executableDictionary, true );
		
		testCommand.SetConfiguration(commandConfig);
		
		testStatus = testCommand.Launch();
		
		assert( testStatus == CommandStatus.RUNNING );
		System.out.println("Finished testLaunch\n");	
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Run()}
	 */
	public void testRun() {
		fail("Not yet implemented");
	}
	
}
