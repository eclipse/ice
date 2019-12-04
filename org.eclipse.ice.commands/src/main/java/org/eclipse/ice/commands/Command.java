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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the instantiation class of the CommandFactory class and thus is
 * responsible for executing particular commands. It is the super class for
 * local and remote commands, and thus delegates the creation of a LocalCommand
 * or RemoteCommand, depending on the hostname.
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
	 * information about whether or not the command should be run locally or
	 * remotely. If remote, it contains all of the necessary ssh information for
	 * opening the remote connection. This is the configuration to connect to the
	 * host that will ultimately perform the job.
	 */
	protected ConnectionConfiguration connectionConfig;

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(Command.class);

	/**
	 * An exit value that is determined when the job is processing. The convention
	 * is that anything other than 0 indicates a failure. Set by default to -1 to
	 * indicate that the job is not running (or hasn't finished).
	 */
	protected int exitValue = -1;

	/**
	 * Have a connection manager for commands that defaults to the static object
	 * from the factory method. Users can override this if they want to through a
	 * specific constructor which sets the manager.
	 */
	protected ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

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
	public CommandStatus execute() {
		// Check that the commandConfig and connectionConfig(s) file was properly
		// instantiated in the constructor
		if (!checkStatus(status))
			return CommandStatus.INFOERROR;

		// Configure the command to be ready to run.
		status = setConfiguration();
		// Ensure that the command was properly configured
		if (!checkStatus(status))
			return CommandStatus.INFOERROR;

		// Now that all of the prerequisites have been set, start the job running
		status = run();

		// Clean up any remaining items from the job, in preparation for a (potential)
		// next job to be run
		status = cleanUp();

		// Confirm the job finished with some status
		logger.info("The job finished with status: " + status);
		return status;

	}

	/**
	 * This function actually runs the particular command in question. It is called
	 * in execute() after all of the setup for the job execution is finished. It
	 * processes the several steps required to run the job, e.g. setting it up,
	 * monitoring it, etc.
	 * 
	 * @return - CommandStatus indicating the result of the function.
	 */
	protected abstract CommandStatus run();

	/**
	 * This function runs the job through the relevant API and performs the process.
	 * 
	 * @return - CommandStatus indicating the result of the function.
	 */
	protected abstract CommandStatus processJob();

	/**
	 * This operation is responsible for monitoring the exit value of the running
	 * job. If it does not finish after some time then the function will print a
	 * message to the error output file. If the job has failed then it stops
	 * monitoring and returns that the exit value of the job was unsuccessful. The
	 * function also writes to the output logfile what the actual final job exit
	 * value is, so the user can always see if their job finished successfully.
	 * 
	 * @return - CommandStatus indicating the result of the function.
	 */
	protected abstract CommandStatus monitorJob();

	/**
	 * This function is responsible for cleaning up any remaining things from the
	 * job after the process builder or JSch API has submitted the job to be
	 * processed. These are things related to, for example, logging output.
	 * 
	 * @return - CommandStatus indicating the result of the function.
	 */
	protected abstract CommandStatus finishJob();

	/**
	 * This function cancels the already submitted command, if possible.
	 * 
	 * @return CommandStatus - indicates whether or not the Command was properly
	 *         cancelled.
	 */
	public CommandStatus cancel() {
		status = CommandStatus.CANCELED;
		return status;
	}

	/**
	 * This function is intended to clean up any (possible) remaining loose ends
	 * after the job is finished processing.
	 * 
	 * @return CommandStatus - indicates whether or not the Command was properly
	 *         cancelled.
	 */
	protected CommandStatus cleanUp() {

		// Clear the command list, so as to not inadvertently concatenate more commands
		// if the same instance of Command is used again.
		// Same for input file list
		commandConfig.getSplitCommand().clear();
		commandConfig.getInputFileList().clear();

		// Return the already set status once the job was finished processing
		return status;
	}

	/**
	 * This function sets up the configuration in preparation for the job running.
	 * It checks to make sure the necessary strings are set and then constructs the
	 * executable to be run. It also creates the output files which contain
	 * log/error information.
	 * 
	 * @return - CommandStatus indicating that configuration completed and job can
	 *           start running
	 */
	protected CommandStatus setConfiguration() {

		// Check the info and return failure if something was not set
		if (commandConfig.getExecutable() == null || commandConfig.getOutFileName() == null
				|| commandConfig.getErrFileName() == null || commandConfig.getNumProcs() == null) {
			logger.error("An important piece of information is missing from the CommandConfiguration. Exiting.");
			return CommandStatus.INFOERROR;
		}

		// Get a string of the executable to manipulate
		String exec = commandConfig.getExecutable();
		// If the executable contains a prefix, remove it
		if (exec.contains("./"))
			exec = exec.substring(2, exec.length());
		String separator = "/";
		if (commandConfig.getOS().toLowerCase().contains("win"))
			separator = "\\";

		// Check if the working directory exists
		String workingDir = commandConfig.getWorkingDirectory();
		if (!workingDir.endsWith(separator))
			workingDir += separator;

		// Check that the directory exists
		// Get the file handler factory
		FileHandlerFactory factory = new FileHandlerFactory();
		boolean exists = false, execExists = false;
		try {
			// Get the handler for this particular connection, whether local or remote
			FileHandler handler = factory.getFileHandler(connectionConfig);

			// If the working directory was set, check that it exists. If it wasn't set,
			// then the paths should have been explicitly identified
			if (workingDir != null)
				exists = handler.exists(workingDir);

			// Check if the executable exists in the working directory
			execExists = handler.exists(workingDir + exec);

		} catch (IOException e) {
			// If we can't get the file handler, then there was an error in the connection
			// configuration
			logger.error("Unable to connect to filehandler and check file existence. Exiting.", e);
			return CommandStatus.INFOERROR;
		}
		// If the working directory doesn't exist, we won't be able to continue the job
		// processing unless the full paths were specified. Warn the user
		if (!exists) {
			logger.warn("Directory containing files does not exist!");
			logger.warn("If you did not specify absolute paths for all your files, the job will fail!");
		}
		// If the executable does not exist but the working directory was set, warn the
		// user again that the executable is unavailable
		if (!execExists && exists) {
			// If the executable doesn't exist, we shouldn't cancel the job because it is
			// possible that the command is a simple shell command. So warn the user
			logger.warn("Warning: Executable file could not be found");
			logger.warn(
					"If you are running a simple shell command, or specified the full path to the executable, ignore this warning");
			logger.warn("Otherwise, the job will fail");
		}

		// Set the command to actually run and execute
		commandConfig.setFullCommand(commandConfig.getExecutableName());

		// Create the output files associated to the job for logging
		commandConfig.createOutputFiles();

		// All setup completed, return that the job will now run
		return CommandStatus.RUNNING;
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

		// Set the objects to the updated readers
		commandConfig.setStdErr(commandConfig.getBufferedWriter(commandConfig.getErrFileName()));
		commandConfig.setStdOut(commandConfig.getBufferedWriter(commandConfig.getOutFileName()));

		// Make a new line for writing out the output
		String newLine = "\n";
		// Add a \r for Windows systems.
		if (commandConfig.getOS().contains("windows"))
			newLine = "\r\n";

		// Catch the stdout and stderr output
		try {
			// Write to the stdOut file
			while ((nextLine = stdOutReader.readLine()) != null) {

				commandConfig.getStdOut().write(nextLine);
				// Only add to the string if it is not a commented out line
				if (!nextLine.startsWith("#")) {
					commandConfig.addToStdOutputString(nextLine);
				}
				// MUST put a new line for this type of writer
				commandConfig.getStdOut().write(newLine);
				commandConfig.getStdOut().flush();
			}

			// Write to the stdErr file
			while ((nextLine = stdErrReader.readLine()) != null) {
				commandConfig.getStdErr().write(nextLine);
				// If the next line isn't commented out, add it to the error string
				if (!nextLine.startsWith("#")) {
					commandConfig.addToErrString(nextLine);
				}

				// MUST put a new line for this type of writer
				commandConfig.getStdErr().write(newLine);
				commandConfig.getStdErr().flush();
			}
		} catch (IOException e) {
			// Or fail and complain about it.
			logger.error("Could not logOutput, returning error!", e);
			return false;
		}

		// Completed successfully, return true
		return true;
	}

	/**
	 * This function is a simple helper function to check and make sure that the
	 * command status is not set to a flagged error, e.g. failed.
	 * 
	 * @param current_status - status that should be checked
	 * @return boolean indicating whether or not status is good to continue (true)
	 *         or whether or not job has failed (returns false)
	 */
	public boolean checkStatus(CommandStatus current_status) {

		if (current_status != CommandStatus.FAILED && current_status != CommandStatus.INFOERROR) {
			logger.info("The current status is: " + current_status);
			return true;
		} else {
			logger.error("The job failed with status: " + current_status);
			logger.error("Check your error logfile for more details! Exiting now!");
			return false;
		}

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
	public void setStatus(CommandStatus status) {
		this.status = status;
		return;
	}

	/**
	 * This function returns to the user the configuration that was used to create a
	 * particular command.
	 * 
	 * @return - the particular CommandConfiguration for this command
	 */
	public CommandConfiguration getCommandConfiguration() {
		return commandConfig;
	}

	/**
	 * This function sets the command configuration for a particular command
	 * 
	 * @param config - CommandConfiguration to be set
	 */
	public void setCommandConfiguration(CommandConfiguration commandConfig) {
		this.commandConfig = commandConfig;
	}

	/**
	 * This function returns to the user the configuration that was used to set up a
	 * particular connection.
	 * 
	 * @return - the particular connection configuration for this command
	 */
	public ConnectionConfiguration getConnectionConfiguration() {
		return connectionConfig;
	}

	/**
	 * This function sets the configuration that is to be used to set up a
	 * particular connection.
	 * 
	 * @param connectionConfig - ConnectionConfiguration to be set
	 */
	public void setConnectionConfiguration(ConnectionConfiguration connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

}
