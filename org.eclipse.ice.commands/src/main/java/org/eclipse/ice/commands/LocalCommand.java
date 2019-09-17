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
import java.util.concurrent.TimeUnit;

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
	public LocalCommand(ConnectionConfiguration _connection, CommandConfiguration _configuration) {

		status = CommandStatus.PROCESSING;

		// Set the configuration for the command
		commandConfig = _configuration;

		// If commandConfig wasn't set properly, the job can't run
		if (commandConfig == null) 
			status = CommandStatus.FAILED;
		
		
		// Set the connection for the local command, which is only relevant for
		// accessing the hostname of the local computer
		connectionConfig = _connection;

	
		
		// Make sure both commandConfig and connectionConfig have the same
		// hostname, since commandConfig needs the hostname for several output
		// files (e.g. for debugging purposes).
		commandConfig.setHostname(connectionConfig.getHostname());

		
	}

	/**
	 * Method that overrides {@link org.eclipse.ice.commands.Command#execute()} and
	 * actually implements the particular LocalCommand to be executed.
	 */
	@Override
	public CommandStatus execute() {

		// Check that the commandConfig file was properly instantiated in the
		// constructor
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Configure the command to be ready to run.
		status = setConfiguration();

		// Ensure that the command was properly configured
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now that all of the prerequisites have been set, start the job running
		status = run();

		// Confirm the job finished with some status
		logger.info("The job finished with status: " + status);
		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {

		// Loop over the stages and launch them. This needs to be done
		// sequentially, so use a regular, non-concurrent access loop
		for (int i = 0; i < commandConfig.getSplitCommand().size(); i++) {

			// Check the status to ensure job has not been canceled
			if (status == CommandStatus.CANCELED) {
				logger.info("Job has been canceled, quitting now.");
				break;
			}

			// Get the command
			String thisCommand = commandConfig.getSplitCommand().get(i);

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
			commandConfig.getStdOut().close();
			commandConfig.getStdErr().close();
		} catch (IOException e) {
			status = CommandStatus.INFOERROR;
			logger.error("Could not close the output and/or error files, returning INFOERROR");
			return status;
		}

		// Return the result
		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#monitorJob()}
	 */
	@Override
	protected CommandStatus monitorJob(){

		// Local Declarations
		int exitValue = -1; // Totally arbitrary

		// Wait until the job exits. By convention an exit code of
		// zero means that the job has succeeded. Watch it until it
		// finishes.
		while (exitValue != 0) {
			// Try to get the exit value of the job
			// If the job completed successfully this will be 0
			try {
				exitValue = job.exitValue();
			} catch (IllegalThreadStateException e) {
				// Complain, but keep watching
				try {
					commandConfig.getStdErr().write(getClass().getName() + "IllegalThreadStateException!: " + e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			// Give it a second
			try {
				job.waitFor(1000, TimeUnit.MILLISECONDS);
				// Try again
				exitValue = job.exitValue();
			} catch (InterruptedException e) {
				// Complain
				try {
					commandConfig.getStdErr().write(getClass().getName() + " InterruptedException!: " + e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			// If for some reason the job has failed,
			// it shouldn't be alive and we should break;
			if (!job.isAlive()) {
				logger.info("Job is no longer alive, done monitoring");
				break;
			}
		}

		// Print the final exitValue of the job to the output log file
		try {
			commandConfig.getStdOut().write("INFO: Command::monitorJob Message: Exit value = " + exitValue + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("Finished monitoring job with exit value: " + exitValue);
		if (exitValue == 0)
			return CommandStatus.SUCCESS;
		else
			return CommandStatus.FAILED;
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

}
