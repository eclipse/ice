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

import java.util.HashMap;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.RemoteCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.RemoteCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCommandTest {

	
	HashMap<String, String> executableDictionary;
	
	String host = "somehost";
	String username = "someusername";
	String password = "p@55w0rd";
	
	CommandConfiguration commandConfig; 
	
	ConnectionConfiguration connectConfig; 
	
	
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
		executableDictionary = new HashMap<String, String>();
		executableDictionary.put("executable", "someExecutable.sh ${installDir}");
		executableDictionary.put("inputFile", "someInputFile.txt");
		executableDictionary.put("stdOutFileName", "someOutFile.txt");
		executableDictionary.put("stdErrFileName", "someErrFile.txt");
		executableDictionary.put("installDir", "~/install");
		executableDictionary.put("numProcs", "1");
		executableDictionary.put("os", "OSX");
		executableDictionary.put("workingDirectory", "/");
		
		commandConfig = new CommandConfiguration(0, "someExecutable.sh ${installDir}",
				"someInputFile.txt", "someOutFile.txt", "someErrFile.txt", "~/install",
				"1", "OSX", "/", true);
		connectConfig = new ConnectionConfiguration(username, password, host);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteCommand()}
	 */
	public void testRemoteCommand() {
		System.out.println("Testing remote command configuration");
		
		
		RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);
		
		CommandStatus status = command.getStatus();
		
		assert ( status == CommandStatus.RUNNING );
		
		System.out.println("Finished remote command configuration test.");
	}

	
	/**
	 * Test method for executing remote command
	 * {@link org.eclipse.ice.commands.RemoteCommand#execute()}
	 */
	public void testExecute() {
		System.out.println("Test remote command execute");
		 RemoteCommand command = new RemoteCommand(connectConfig, commandConfig);
		
		 CommandStatus status = command.execute();
		 
		 assert ( status == CommandStatus.SUCCESS );
		 
		 System.out.println("Finished testing remote command execute");
	}
	
	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.RemoteCommand#SetConnection(String)}
	 */
	public void testSetConnection() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commans.RemoteCommand#GetConnection(String)}
	 */
	public void testGetConnection() {
		fail("Not yet implemented");
	}
}
