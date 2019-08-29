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

import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class LocalCommandTest {

	HashMap<String, String> executableDictionary;

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
		CommandConfiguration commandConfig = new CommandConfiguration(3, executableDictionary, true);

		LocalCommand realCommand = new LocalCommand(commandConfig);
		CommandStatus testStatus = realCommand.getStatus();

		assert (testStatus == CommandStatus.RUNNING);
		System.out.println("Finished testConfiguration\n");

	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Execute()}. This
	 * test should "fail" by default since the CommandConfiguration
	 * executableDictionary does not point to a real executable. This is a test of
	 * the API catching an incorrectly configured command. For a test of a fully
	 * functional command, see {@link org.eclipse.ice.commands.testCommandFactory()}
	 */
	@Test
	public void testExecute() {
		System.out.println("Starting testExecute\n");

		LocalCommand testCommand = new LocalCommand(new CommandConfiguration(2, executableDictionary, true));

		CommandStatus testStatus = testCommand.execute();

		assert (testStatus == CommandStatus.FAILED);
		System.out.println("Example incorrect executable status should be FAILED and is: " + testStatus);
		System.out.println("Finished testExecute\n");
	}

}
