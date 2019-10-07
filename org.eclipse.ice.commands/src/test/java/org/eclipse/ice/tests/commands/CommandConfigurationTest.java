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

import java.util.ArrayList;

import org.eclipse.ice.commands.CommandConfiguration;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.CommandConfiguration}.
 * 
 * @author Joe Osborn
 *
 */
public class CommandConfigurationTest {

	// Create a default instance to test
	CommandConfiguration config = new CommandConfiguration();

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.CommandConfiguration#CommandConfiguration()}.
	 * Check some of the getters and setters
	 */
	@Test
	public void testCommandConfiguration() {

		// Set some things
		config.setCommandId(3);
		config.setExecutable("./some_executable.sh");
		config.setErrFileName("errorFile.txt");
		config.setOutFileName("outFile.txt");

		// Assert whether or not things are/aren't set
		assert (config.getOutFileName() != null);
		assert (config.getOS() != null);

		// Didn't set working directory
		assert (config.getWorkingDirectory() == null);

		// Assert that the default local OS is set
		assert (config.getOS().equals(System.getProperty("os.name")));

		config.setOS("JoeOsbornOS");

		assert (config.getOS().equals("JoeOsbornOS"));

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.CommandConfiguration#getExecutableName()}
	 */
	@Test
	public void testGetExecutableName() {

		// Test that if one wants to append inputfile, it is appended
		config.setCommandId(1);
		config.setExecutable("./test_code_execution.sh");
		config.addInputFile("someInputFile", "someInputFile.txt");
		config.setNumProcs("1");
		config.setAppendInput(true);
		config.setOS("osx");
		String executable = config.getExecutableName();
		assert (executable.equals("./test_code_execution.sh someInputFile.txt"));

		// Test that if num processes is more than 1, mpi options are added
		config.setNumProcs("4"); // arbitrary number
		// We can test append input as well when it is false
		config.setAppendInput(false);
		executable = config.getExecutableName();
		assert (executable.equals("mpirun -np 4 ./test_code_execution.sh"));

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.CommandConfiguration#getExecutableName()} and
	 * for testing the split command functionality
	 */
	@Test
	public void testGetExecutableNameSplitCommand() {
		CommandConfiguration splitConfig = new CommandConfiguration();
		splitConfig.setCommandId(2);
		splitConfig.setExecutable(
				"./dummy.sh ${inputfile}; ./next_file.sh ${otherinputfile}; ./other_file.sh ${installDir}");
		// Test if the user falsifies append input whether or not the environment
		// variable is replaced
		splitConfig.setAppendInput(false);
		splitConfig.setNumProcs("1");
		splitConfig.addInputFile("inputfile", "inputfile.txt");
		splitConfig.addInputFile("otherinputfile", "/some/dummy/path/to/an/inputfile.txt");
		splitConfig.setInstallDirectory("~/install_dir");
		splitConfig.setOS(System.getProperty("os.name"));
		String executable = splitConfig.getExecutableName();
		assert (executable.equals(
				"./dummy.sh inputfile.txt; ./next_file.sh /some/dummy/path/to/an/inputfile.txt; ./other_file.sh ~/install_dir/"));

		ArrayList<String> split = new ArrayList<String>();
		split = splitConfig.getSplitCommand();

		// Create an array list to check the split command against
		ArrayList<String> checkSplit = new ArrayList<String>();
		checkSplit.add("./dummy.sh inputfile.txt");
		checkSplit.add("./next_file.sh /some/dummy/path/to/an/inputfile.txt");
		checkSplit.add("./other_file.sh ~/install_dir/");

		for (int i = 0; i < split.size(); i++) {
			assert (split.get(i).equals(checkSplit.get(i)));
		}

	}

}
