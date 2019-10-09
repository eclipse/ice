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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
	 * Reference to the Java process that is the job to be executed
	 */
	private Process job;

	/**
	 * The variable that actually handles the job execution at the command line
	 */
	private ProcessBuilder jobBuilder;

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
			status = processJob();

			// Finish the job by logging output. If the job is not completed, then
			// it will be monitored in monitorJob
			status = finishJob();

			// Check the status to ensure the job hasn't failed
			try {
				checkStatus(status);
			} catch (IOException e) {
				// If not, return failed
				e.printStackTrace();
				return CommandStatus.FAILED;
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
				// If not, return failed
				e.printStackTrace();
				return CommandStatus.FAILED;
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
	 * This function sets up the ProcessBuilder member variable to prepare for
	 * actually submitting the job process to the command line from Java. The
	 * function adjusts the command based on the OS on which it shall run, and then
	 * creates the variables necessary for the command line execution.
	 * 
	 * @param command - Command to be prepared for shell execution
	 */
	protected CommandStatus setupProcessBuilder(String command) {

		// Local declarations
		String os = commandConfig.getOS();
		ArrayList<String> commandList = new ArrayList<String>();

		// If the OS is anything other than Windows, then the process builder
		// needs to be configured to launch bash in command mode to avoid weird
		// escape sequences.
		if (!os.toLowerCase().contains("win")) {
			commandList.add("/bin/bash");
			commandList.add("-c");
		}

		// Now add the actual command to be processed, prepended with /bin/bash -c if
		// the OS is not windows
		commandList.add(command);

		logger.info("Full command going to ProcessBuilder is: " + commandList);
		// Make the ProcessBuilder to execute the command
		jobBuilder = new ProcessBuilder(commandList);

		// Set the directory to execute the job in
		File directory = new File(commandConfig.getWorkingDirectory());
		jobBuilder.directory(directory);
		jobBuilder.redirectErrorStream(false);

		return CommandStatus.RUNNING;
	}

	/**
	 * This function is responsible for actually running the Process in the command
	 * line. It catches exceptions in the event that the job can't be started.
	 * 
	 * See also {@link org.eclipse.ice.commands.Command#processJob()}
	 */
	@Override
	protected CommandStatus processJob() {

		String os = commandConfig.getOS();
		List<String> commandList = jobBuilder.command();

		// Check that the job hasn't been canceled and is ready to run
		try {
			if (status != CommandStatus.CANCELED)
				job = jobBuilder.start();
		} catch (IOException e) {

			// If not a windows machine, there was an error
			if (!os.toLowerCase().contains("win")) {
				// If there is an error, add it to errMsg
				commandConfig.addToErrString(e.getMessage() + "\n");
			} else {
				// If this is a windows machine, try to run in the command prompt
				commandList.add(0, "CMD");
				commandList.add(1, "/C");

				// Reset the ProcessBuilder to reflect these changes
				jobBuilder = new ProcessBuilder(commandList);
				File directory = new File(commandConfig.getWorkingDirectory());
				jobBuilder.directory(directory);
				jobBuilder.redirectErrorStream(false);

				// Now try again to start the job
				try {
					if (status != CommandStatus.CANCELED)
						job = jobBuilder.start();
				} catch (IOException e2) {
					// If there is an error, add it to errMsg
					commandConfig.addToErrString(e2.getMessage() + "\n");
				}
			}
		}

		return status;

	}

	/**
	 * This function cleans up the remaining tasks left after job processing. This
	 * is mostly logging output files, and checking that the process actually
	 * finished successfully according to the ProcessBuilder.
	 * 
	 * See also {@link org.eclipse.ice.commands.Command#finishJob()}
	 * 
	 * @return - CommandStatus indicating whether or not the function processed
	 *         correctly
	 */
	@Override
	protected CommandStatus finishJob() {

		InputStream stdOutStream = null, stdErrStream = null;
		String stdErrFileName = null, stdOutFileName = null;

		// Get the output file names
		stdErrFileName = commandConfig.getErrFileName();
		stdOutFileName = commandConfig.getOutFileName();

		// If errMsg is not an empty String, then there were some errors and they
		// should be written out to the log file
		if (commandConfig.getErrString() != "") {
			try {
				// Get the filenames so that they can be written to
				commandConfig.setStdErr(commandConfig.getBufferedWriter(stdErrFileName));
				commandConfig.setStdOut(commandConfig.getBufferedWriter(stdOutFileName));

				// Write and close
				commandConfig.getStdErr().write(commandConfig.getErrString());
				commandConfig.getStdOut().close();
				commandConfig.getStdErr().close();
			} catch (IOException e) {
				logger.error("There were errors in the job running, but they could not write to the error log file!");
				return CommandStatus.FAILED;
			}

			return CommandStatus.FAILED;
		}

		// Log the output of the job execution
		stdOutStream = job.getInputStream();
		stdErrStream = job.getErrorStream();

		// Check that output was correctly logged. If not, return error
		if (logOutput(stdOutStream, stdErrStream) == false) {
			logger.error("Couldn't log output, marking job as failed");
			return CommandStatus.FAILED;
		}

		// Try to get the exit value of the job
		int exitValue = -1; // arbitrary value indicating not completed (yet)

		try {
			exitValue = job.exitValue();
		} catch (IllegalThreadStateException e) {
			// The job is still running, so it should be watched by the
			// {@link org.eclipse.ice.commands.Command.monitorJob()} function

			logger.info("Job didn't finish, going to monitorJob now");
			return CommandStatus.RUNNING;
		}
		// By convention exit values other than zero mean that the program
		// failed. If it is not 0, mark the job as failed (since it finished).
		if (exitValue == 0) {
			return CommandStatus.SUCCESS;
		} else {
			return CommandStatus.FAILED;
		}

	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#monitorJob()}
	 */
	@Override
	protected CommandStatus monitorJob() {

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

}
