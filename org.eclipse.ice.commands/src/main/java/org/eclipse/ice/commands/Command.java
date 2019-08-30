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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the instantiation class of the CommandFactory class and thus is
 * responsible for executing particular commands. It delegates the creation of a
 * LocalCommand or RemoteCommand, depending on the hostname.
 * 
 * @author Joe Osborn
 *
 */
public abstract class Command {

	/**
	 * The current status of the command
	 */
	protected CommandStatus status;

	/**
	 * The configuration parameters of the command - contains information about what
	 * the command is actually intended to do (e.g. the executable filename).
	 */
	protected CommandConfiguration commandConfig;

	/**
	 * The connection configuration parameters of the command - this will contain
	 * information about whether or not the command should be run locally or remotely.
	 * If remote, it contains all of the necessary ssh information for opening the
	 * remote connection.
	 */
	protected ConnectionConfiguration connectionConfig;
	
	/**
	 * Output streams for the job
	 */
	protected BufferedWriter stdOut = null, stdErr = null;

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(Command.class);

	/**
	 * Reference to the Java process that is the job to be executed
	 */
	private Process job;

	/**
	 * The variable that actually handles the job execution at the command line
	 */
	protected ProcessBuilder jobBuilder;

	/**
	 * Default constructor
	 */
	public Command() {
	}

	/**
	 * This function executes the command based on the information provided in the
	 * dictionary which is stored in the CommandConfiguration.
	 * 
	 * @return CommandStatus - indicating whether or not the Command was properly
	 *         executed
	 */
	public abstract CommandStatus execute();

	/**
	 * This function sets the CommandConfiguration for a particular command. It also
	 * prepares various files for job launch (e.g. logfiles) and is called at
	 * construction time. It is overriden by LocalCommand and RemoteCommand
	 * 
	 * @param config - the configuration to be used for a particular command.
	 * @return CommandStatus - status indicating whether the configuration was
	 *         properly set
	 */
	protected CommandStatus setConfiguration(CommandConfiguration config) {
		commandConfig = config;
		return CommandStatus.PROCESSING;
	}

	/**
	 * This function actually runs the particular command in question. It is called
	 * in execute() after all of the setup for the job execution is finished.
	 * 
	 * @return - CommandStatus indicating the result of the function.
	 */
	protected abstract CommandStatus run();

	/**
	 * This function cancels the already submitted command, if possible.
	 * 
	 * @return CommandStatus - indicates whether or not the Command was properly
	 *         cancelled.
	 */
	public abstract CommandStatus cancel();

	/**
	 * This function actually assembles and fixes the name of the executable to be
	 * launched. It replaces ${inputFile}, ${installDir} and other keys from the
	 * dictionary. The function is abstract so that Local and Remote executable
	 * names can be handled individually, since the remote target file system is not
	 * necessarily the same as the local. It is overridden by the subclasses that
	 * require executables.
	 * 
	 * @return - String that is the executable to be run
	 */
	protected String getExecutableName() {
		return commandConfig.executable;
	}

	/**
	 * This function returns the status for a particular command at a given time in
	 * the operation of the command.
	 * 
	 * @return - return current status for a particular command
	 */
	public CommandStatus getStatus() {
		return status;
	}

	/**
	 * This function sets the status for a particular command to be stat
	 * 
	 * @param stat - new CommandStatus to be set
	 */
	public void setStatus(CommandStatus stat) {
		status = stat;
		return;
	}

	/**
	 * This function returns to the user the configuration that was used to create a
	 * particular command. Could be useful for accessing particular details about
	 * the command.
	 * 
	 * @return - the particular configuration for this command
	 */
	public CommandConfiguration getConfiguration() {
		return commandConfig;
	}

	/**
	 * This function creates and returns a BufferedWriter for appending text to the
	 * file specified in the call
	 * 
	 * @param filename - file to append to
	 * @return - buffered writer for appending
	 */
	protected BufferedWriter getBufferedWriter(String filename) {

		FileWriter writer = null;
		BufferedWriter bufferedWriter = null;

		// Check the file name and the create the writer
		if (filename != null) {
			try {
				writer = new FileWriter(filename, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferedWriter = new BufferedWriter(writer);
			return bufferedWriter;
		} else
			return null;
	}

	/**
	 * This function creates a set of generic output header text useful for
	 * debugging or informational purposes, for example in log files.
	 * 
	 * @param logName - the particular log name
	 * @return - A string with the corresponding header text
	 */
	public String createOutputHeader(String logName) {
		String header = null, localHostname = null;

		// Get the machine identity since the local machine launches the job
		// regardless of whether or not the job is local or remote
		try {
			InetAddress addr = InetAddress.getLocalHost();
			localHostname = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Add the header file name so that it can be identified
		header = "# Logfile type : " + logName + "\n";

		// Add the date and time
		header += "# Job launch date: ";
		header += new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "\n";

		// Add the point of origin
		header += "# Launch host: " + localHostname + "\n";

		// Add the target machine
		header += "# Target host: " + connectionConfig.hostname + "\n";

		// Add the execution command
		header += "# Command Executed: " + commandConfig.fullCommand + "\n";

		// Add the input file name
		header += "# Input file: " + commandConfig.inputFile + "\n";

		// Add an empty line
		header += "\n";

		return header;
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
		String os = commandConfig.os;
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
		File directory = new File(commandConfig.workingDirectory);
		jobBuilder.directory(directory);
		jobBuilder.redirectErrorStream(false);

		return CommandStatus.RUNNING;
	}

	/**
	 * This function is responsible for actually running the Process in the command
	 * line. It catches exceptions in the event that the job can't be started.
	 * 
	 */
	protected CommandStatus runProcessBuilder() {

		String os = commandConfig.os;
		List<String> commandList = jobBuilder.command();
		String errMsg = "";

		// Check that the job hasn't been canceled and is ready to run
		try {
			if (status != CommandStatus.CANCELED)
				job = jobBuilder.start();
		} catch (IOException e) {

			// If not a windows machine, there was an error
			if (!os.toLowerCase().contains("win")) {
				// If there is an error, add it to errMsg
				errMsg += e.getMessage() + "\n";
			} else {
				// If this is a windows machine, try to run in the command prompt
				commandList.add(0, "CMD");
				commandList.add(1, "/C");

				// Reset the ProcessBuilder to reflect these changes
				jobBuilder = new ProcessBuilder(commandList);
				File directory = new File(commandConfig.workingDirectory);
				jobBuilder.directory(directory);
				jobBuilder.redirectErrorStream(false);

				// Now try again to start the job
				try {
					if (status != CommandStatus.CANCELED)
						job = jobBuilder.start();
				} catch (IOException e2) {
					// If there is an error, add it to errMsg
					errMsg += e2.getMessage() + "\n";
				}
			}
		}

		// Clean up and log the output of the job
		status = cleanProcessBuilder(errMsg);

		return status;

	}

	/**
	 * This function cleans up the remaining tasks left after job processing. This
	 * is mostly logging output files, and checking that the process actually
	 * finished successfully according to the ProcessBuilder
	 * 
	 * @param errorMessage - A string of any potential errors that were thrown
	 *                     during job execution
	 * @return - CommandStatus indicating whether or not the function processed
	 *         correctly
	 */
	protected CommandStatus cleanProcessBuilder(String errorMessage) {

		InputStream stdOutStream = null, stdErrStream = null;
		String stdErrFileName = null, stdOutFileName = null;

		// Get the output file names
		stdErrFileName = commandConfig.stdErrFileName;
		stdOutFileName = commandConfig.stdOutFileName;

		int exitValue = -1; // arbitrary value indicating not completed (yet)

		// If errMsg is not an empty String, then there were some errors and they
		// should be written out to the log file
		if (errorMessage != "") {
			try {
				// Get the filenames so that they can be written to
				stdErr = getBufferedWriter(stdErrFileName);
				stdOut = getBufferedWriter(stdOutFileName);

				// Write and close
				stdErr.write(errorMessage);
				stdOut.close();
				stdErr.close();
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
			return CommandStatus.FAILED;
		}

		// Try to get the exit value of the job
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
	 * This operation is responsible for monitoring the exit value of the running
	 * job. If it does not finish after some time then the function will print a
	 * message to the error output file. If the job has failed then it stops
	 * monitoring and returns that the exit value of the job was unsuccessful. The
	 * function also writes to the output logfile what the actual final job exit
	 * value is, so the user can always see if their job finished successfully.
	 */
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
					stdErr.write(getClass().getName() + "IllegalThreadStateException!: " + e);
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
					stdErr.write(getClass().getName() + " InterruptedException!: " + e);
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
			stdOut.write("INFO: Command::monitorJob Message: Exit value = " + exitValue + "\n");
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
	 * This function takes the given streams as parameters and logs them into an
	 * output file. The function returns a boolean on whether or not the function
	 * completed successfully (and thus the streams were correctly written out).
	 * 
	 * @param output - Output stream from the job
	 * @param errors - Error stream from the job
	 * @return - boolean - true if output was logged, false otherwise
	 */
	protected boolean logOutput(final InputStream output, final InputStream errors) {

		InputStreamReader stdOutStreamReader = null, stdErrStreamReader = null;
		BufferedReader stdOutReader = null, stdErrReader = null;
		String nextLine = null;

		// Setup the BufferedReader that will get stdout from the process.
		stdOutStreamReader = new InputStreamReader(output);
		stdOutReader = new BufferedReader(stdOutStreamReader);

		// Setup the BufferedReader that will get stderr from the process.
		stdErrStreamReader = new InputStreamReader(errors);
		stdErrReader = new BufferedReader(stdErrStreamReader);
		stdErr = getBufferedWriter(commandConfig.stdErrFileName);
		stdOut = getBufferedWriter(commandConfig.stdOutFileName);

		// Catch the stdout and stderr output
		try {
			// Write to the stdOut file
			while ((nextLine = stdOutReader.readLine()) != null) {
				stdOut.write(nextLine);
				// MUST put a new line for this type of writer. "\r\n" works on
				// Windows and Unix-based systems.
				stdOut.write("\r\n");
				stdOut.flush();
			}
			// Write to the stdErr file
			while ((nextLine = stdErrReader.readLine()) != null) {
				stdErr.write(nextLine);
				// MUST put a new line for this type of writer. "\r\n" works on
				// Windows and Unix-based systems.
				stdErr.write("\r\n");
				stdErr.flush();
			}
		} catch (IOException e) {
			// Or fail and complain about it.
			logger.error("Could not logOutput, returning error!");
			return false;
		}

		// Completed successfully, return true
		return true;
	}

	/**
	 * This function is a simple helper function to check and make sure that the
	 * command status is not set to a flagged error, e.g. failed.
	 * 
	 * @param current_status
	 */
	public void checkStatus(CommandStatus current_status) throws IOException {

		if (current_status != CommandStatus.FAILED && current_status != CommandStatus.INFOERROR) {
			logger.info("The current status is: " + current_status);
			return;
		} else {
			logger.error("The job failed with status: " + current_status);
			logger.error("Check your error logfile for more details! Exiting now!");
			throw new IOException();
		}

	}

}
