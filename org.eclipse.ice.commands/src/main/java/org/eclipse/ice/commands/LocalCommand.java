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
package org.eclipse.ice.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class inherits from Command and gives available functionality for local
 * commands. The processing and running of the LocalCommand is performed here.
 * 
 * @author Joe Osborn
 *
 */
public class LocalCommand extends Command {

	/**
	 * Default constructor
	 */
	public LocalCommand() {
	}

	/**
	 * Constructor which specifies a particular command configuration. The command
	 * configuration should contain all of the necessary details for a particular
	 * command to run. See in particular the function
	 * {@link org.eclipse.ice.commands.LocalCommand#setConfiguration(CommandConfiguration)}
	 * for details about what details are required.
	 * 
	 * @param _configuration
	 */
	public LocalCommand(CommandConfiguration _configuration) {

		status = CommandStatus.PROCESSING;
		
		// Local commands by definition have a local connection, so just set it here
		// We only need the hostname for some header output file information, so the 
		// username and password are arbitrary.
		connectionConfig = new ConnectionConfiguration("username","password",getLocalHostname());
				
		status = setConfiguration(_configuration);
		
	}

	/**
	 * Method that overrides {@link org.eclipse.ice.commands.Command#execute()} and
	 * actually implements the particular LocalCommand to be executed.
	 */
	@Override
	public CommandStatus execute() {

		// Check that the status of the job is good after setting up the configuration
		// in the constructor. If not, exit
		// See CheckStatus function in {@link org.eclipse.ice.commands.Command}
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now that all of the prerequisites have been set, start the job running
		status = run();

		logger.info("The job finished with status: " + status);
		return status;
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.Command#setConfiguration(CommandConfiguration)}
	 */
	@Override
	protected CommandStatus setConfiguration(final CommandConfiguration config) {

		// Set the configuration for the command
		commandConfig = config;

		// Now extract the information from the command dictionary
		String executable = null, inputFile = null;
		String stdOutFileName = null, stdErrFileName = null;
		String stdOutHeader = null, stdErrHeader = null;
		String numProcs = null, os = null;
		String directory = null;

		// Make sure the configuration was actually set
		if (commandConfig != null) {

			// Make sure the dictionary contains the keys we need
			executable = commandConfig.executable;
			inputFile = commandConfig.inputFile;
			stdOutFileName = commandConfig.stdOutFileName;
			stdErrFileName = commandConfig.stdErrFileName;
			numProcs = commandConfig.numProcs;
			os = commandConfig.os;
			directory = commandConfig.workingDirectory;
		} else
			return CommandStatus.INFOERROR;

		// Check the info and return failure if something was not set correctly
		if (executable == null || inputFile == null || stdOutFileName == null || stdErrFileName == null
				|| numProcs == null || os == null || directory == null)
			return CommandStatus.INFOERROR;

		// Set the command to actually run and execute
		commandConfig.fullCommand = getExecutableName();

		// Set the output and error buffer writers
		stdOut = getBufferedWriter(stdOutFileName);
		stdErr = getBufferedWriter(stdErrFileName);

		// Create unique headers for the output files which include useful information
		stdOutHeader = createOutputHeader("standard output");
		stdErrHeader = createOutputHeader("standard error");

		// Now write them out
		try {
			stdOut.write(stdOutHeader);
			stdOut.write("# Executable to be run is: " + commandConfig.fullCommand + "\n");
			stdOut.close();
			stdErr.write(stdErrHeader);
			stdErr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// All setup completed, return that the job will now run
		return CommandStatus.RUNNING;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {

		// Loop over the stages and launch them. This needs to be done
		// sequentially, so use a regular, non-concurrent access loop
		for (int i = 0; i < commandConfig.splitCommand.size(); i++) {

			// Check the status to ensure job has not been canceled
			if (status == CommandStatus.CANCELED) {
				logger.info("Job has been canceled, quitting now.");
				break;
			}

			// Get the command
			String thisCommand = commandConfig.splitCommand.get(i);

			// Set up the process builder
			status = setupProcessBuilder(thisCommand);

			// Run the job
			status = runProcessBuilder();

			// Check the status to ensure the job hasn't failed
			try {
				checkStatus(status);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Monitor the job to ensure it finished successfully or to watch it
			// if it is still running
			if (status != CommandStatus.SUCCESS && status != CommandStatus.FAILED) {
				logger.info("Monitoring job");
				status = monitorJob();
			}

			// Now check again to see if the job succeeded
			try {
				checkStatus(status);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Close up the output streams
		try {
			stdOut.close();
			stdErr.close();
		} catch (IOException e) {
			status = CommandStatus.INFOERROR;
			logger.error("Could not close the output and/or error files, returning INFOERROR");
			return status;
		}

		// Return the result
		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#fixExecutableName()}
	 */
	@Override
	protected String getExecutableName() {

		// Get the information from the executable dictionary
		int numProcs = Math.max(1, Integer.parseInt(commandConfig.numProcs));
		String fixedExecutableName = commandConfig.executable;
		String installDirectory = commandConfig.installDirectory;
		String inputFile = commandConfig.inputFile;
		String separator = "/";

		// If the input file should be appended, append it
		if (commandConfig.appendInput)
			fixedExecutableName += " " + inputFile;

		commandConfig.fullCommand = fixedExecutableName;

		// Determine the proper separator
		if (installDirectory != null && installDirectory.contains(":\\"))
			separator = "\\";

		// Append a final separator to the install directory if required
		if (installDirectory != null && !installDirectory.endsWith(separator))
			installDirectory = installDirectory + separator;

		// Search for and replace the ${inputFile} to properly configure the input file
		if (fixedExecutableName.contains("${inputFile}") && !commandConfig.appendInput)
			fixedExecutableName = fixedExecutableName.replace("${inputFile}", inputFile);

		if (fixedExecutableName.contains("${installDir}") && installDirectory != null)
			fixedExecutableName = fixedExecutableName.replace("${installDir}", installDirectory);

		// If MPI should be used if there are multiple cores, add it to the executable
		if (numProcs > 1)
			fixedExecutableName = "mpirun -np " + numProcs + " " + fixedExecutableName;

		// Clean up any unnecessary white space
		fixedExecutableName = fixedExecutableName.trim();

		// Split the full command into its stages, if there are any.
		if (!fixedExecutableName.contains(";"))
			commandConfig.splitCommand.add(fixedExecutableName);
		// Otherwise split the full command and put it into
		// CommandConfiguration.splitCommand
		else {
			for (String stage : fixedExecutableName.split(";"))
				commandConfig.splitCommand.add(stage);
		}

		// Print launch stages so that user can confirm
		for (int i = 0; i < commandConfig.splitCommand.size(); i++) {
			String cmd = commandConfig.splitCommand.get(i);
			logger.info("LocalCommand Message: Launch stage " + i + " = " + cmd);
		}

		return fixedExecutableName;

	}

	/**
	 * Method that overrides Commmand:Cancel and actually implements the particular
	 * LocalCommand to be cancelled.
	 */
	@Override
	public CommandStatus cancel() {
		status = CommandStatus.CANCELED;
		return status;
	}

	
	
	/**
	 * This function just returns the local hostname of your local computer. It is
	 * useful for testing a variety of local commands.
	 * 
	 * @return - String - local hostname
	 */
	protected String getLocalHostname() {
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();

		return hostname;
	}
	
	
}
