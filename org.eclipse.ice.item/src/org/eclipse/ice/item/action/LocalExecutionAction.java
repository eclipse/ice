/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Alexander J. McCaskey
 *******************************************************************************/
package org.eclipse.ice.item.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.FormStatus;

/**
 * The LocalExecutionAction is a subclass of Action that executes a specified
 * executable locally, ie on the localhost. It requires a key-value pair list
 * that contains the following:
 * </p>
 * <table border="1">
 * <col width="50.0%"></col><col width="50.0%"></col>
 * <tr>
 * <td>
 * <p>
 * <b>Key</b>
 * </p>
 * </td>
 * <td>
 * <p>
 * <b>Value</b>
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * executable
 * </p>
 * </td>
 * <td>
 * <p>
 * The name of the executable as it exists on the system path or, alternatively,
 * the fully qualified path to the executable.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * inputFile
 * </p>
 * </td>
 * <td>
 * <p>
 * The path of the input file used by the executable. This may, alternatively,
 * represent any stream of characters.
 * </p>
 * </td>
 * </tr>
 * * <tr>
* <td>
* <p>
* localJobLaunchDirectory
* </p>
* </td>
* <td>
* <p>
* The name of the directory within the project/jobs folder where 
* the files to be uploaded can be found (optional).
* </p>
* </td>
* </tr>
 * <tr>
 * <td>
 * <p>
 * stdOutFile
 * </p>
 * </td>
 * <td>
 * <p>
 * The path of the file to which information printed to stdout by the job should
 * be written.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * stdErrFile
 * </p>
 * </td>
 * <td>
 * <p>
 * The path of the file to which information printed to stderr by the job should
 * be written.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * projectSpaceDir
 * </p>
 * </td>
 * <td>
 * <p>
 * The absolute path string of the ICE project directory. This can be found with
 * IProject.getLocation().toOSString().
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * installDir
 * </p>
 * </td>
 * <td>
 * <p>
 * The directory in which the executable is installed on the target platform.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * numProcs
 * </p>
 * </td>
 * <td>
 * <p>
 * The number of MPI processes that should be used by the job. The default value
 * of this is always 1.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * numTBBThreads
 * </p>
 * </td>
 * <td>
 * <p>
 * The number of Intel TBB threads that should be used by the job. The default
 * value of this is always 1.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * os
 * </p>
 * </td>
 * <td>
 * <p>
 * The operating system of the target host.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * accountCode
 * </p>
 * </td>
 * <td>
 * <p>
 * The account code or project name that should be used for billing on the
 * remote machine.
 * </p>
 * </td>
 * </tr>
 * * <tr>
* <td>
* <p>
* os
* </p>
* </td>
* <td>
* <p>
* The name of the operating system.
* </p>
* </td>
* </tr>
 * </table>
 *
 * @author Alex McCaskey
 *
 */
public class LocalExecutionAction extends Action {

	/**
	 * Reference to the IProject instance for this Action.
	 */
	private IProject project;

	/**
	 * Reference to the IFolder where this local execution is to take place.
	 */
	private IFolder localLaunchFolder;

	/**
	 * Output streams for stdout and stderr
	 */
	private BufferedWriter stdOut, stdErr;

	/**
	 * Reference to the Java Process that is this local execution.
	 */
	private Process job;

	/**
	 * AtomicBoolean to handle cancellations.
	 */
	private AtomicBoolean cancelled;

	/**
	 * Reference to the ExecutionHelper which is used to perform pre and post
	 * processing tasks for this Action.
	 */
	private ExecutionHelper helper;

	/**
	 * The constructor. 
	 */
	public LocalExecutionAction() {
		// Initialize the cancelled flag and 
		// the form status.
		status = FormStatus.ReadyToProcess;
		cancelled = new AtomicBoolean(false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Create the ExecutionHelper.
		helper = new ExecutionHelper(dictionary);

		// Validate the data
		if (helper.isDataValid()) {
			// Get a reference to the IProject and IFolders
			// for this local execution
			project = helper.getProject();
			localLaunchFolder = helper.getLocalLaunchFolder();

			// Get the actual executable String, with all flags
			// replaced
			String fullCMD = helper.fixExecutableName();

			// Create the standard out and standard error writers.
			stdOut = helper.getOutputBufferedWriter();
			stdErr = helper.getErrorBufferedWriter();

			// Write some header information first
			try {
				stdOut.write(helper.createOutputHeader("standard output", fullCMD));
				stdErr.write(helper.createOutputHeader("standard error", fullCMD));
			} catch (IOException e) {
				// Complain
				logger.error(getClass().getName() + " Exception!", e);
			}

			// Launch the Job!
			launchLocally();

			// Close the both output streams now that the work is done.
			try {
				stdOut.close();
				stdErr.close();
			} catch (IOException e) {
				// Complain
				logger.error(getClass().getName() + " Exception!", e);
				status = FormStatus.InfoError;
				return status;
			}

			// Return successful FormStatus flag.
			status = FormStatus.Processed;
			return status;
		} else {
			logger.error("Local Execution Error - the input data map was not valid.");
			status = FormStatus.InfoError;
			return status;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#cancel()
	 */
	@Override
	public FormStatus cancel() {
		// Throw the flag
		cancelled.set(true);

		// Stop local jobs
		if (job != null) {
			job.destroy();
		}

		return FormStatus.ReadyToProcess;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#getActionName()
	 */
	@Override
	public String getActionName() {
		return "Local Execution";
	}

	/**
	 * This operation launches the job on the local machine.
	 */
	protected void launchLocally() {

		// Local Declarations
		FormStatus launchStatus;
		ArrayList<String> splitCMD = helper.getSplitCommand();

		// Loop over the stages and launch them so long as the status marks them
		// as processed. This needs to be done sequentially, so use a regular,
		// non-concurrent access loop.
		for (int i = 0; i < splitCMD.size(); i++) {
			// Check to see if the job should be cancelled.
			if (cancelled.get()) {
				break;
			}
			// Launch the current stage of the job
			launchStatus = launchStageLocally(splitCMD.get(i), stdOut, stdErr);
			monitorJob();
			if (launchStatus.equals(FormStatus.InfoError)) {
				// Look for abnormal launches
				// // Look for still running jobs and watch them
				// Otherwise something has gone really wrong and the launch
				// is over.
				status = FormStatus.InfoError;
				return;
			}
		}

		// Return the right flag if everything worked
		status = FormStatus.Processed;

		return;
	}

	/**
	 * Launch each of the commands in this local execution. 
	 * 
	 * @param cmd Command to execute
	 * @param stdOut Standard out writer
	 * @param stdErr Standard error writer.
	 * @return
	 */
	protected FormStatus launchStageLocally(String cmd, BufferedWriter stdOut, BufferedWriter stdErr) {

		// Local Declarations
		String errMsg = null;
		ProcessBuilder jobBuilder = null;
		InputStream stdOutStream = null, stdErrStream = null;
		int exitValue = -1;
		String os = helper.getParameter("os");// execDictionary.get("os");
		ArrayList<String> cmdList = new ArrayList<String>();
		File directory = localLaunchFolder.getLocation().toFile();

		// If the OS is anything other than Windows, then the process builder
		// needs to be configured to launch bash in command mode to avoid weird
		// escape sequences.
		if (!os.toLowerCase().contains("win")) {
			cmdList.add("/bin/bash");
			cmdList.add("-c");
		}

		// Add the command to the list. It will either be just the command
		// itself or it will be prepended with /bin/bash -c.
		cmdList.add(cmd);

		// Launch the command. The command must be split on spaces to run
		// through a Java Process because the process tries to execute the whole
		// thing as a single command with the arguments give after it.
		jobBuilder = new ProcessBuilder(cmdList);
		jobBuilder.directory(directory);
		// Do not direct the error to stdout. Catch it separately.
		jobBuilder.redirectErrorStream(false);
		try {
			// Only launch the stage if it hasn't been cancelled.
			if (!cancelled.get()) {
				job = jobBuilder.start();
			}
		} catch (IOException e) {
			// Grab the error
			errMsg = e.getMessage();
			logger.error(getClass().getName() + " Exception! ", e);
		}
		// Log any errors and return
		if (errMsg != null) {
			try {
				// Write the message
				stdErr.write(errMsg);
				// Close the streams
				stdOut.close();
				stdErr.close();
			} catch (IOException e) {
				// Complain if the error can't be written
				logger.error(getClass().getName() + " Exception!", e);
			}
			return FormStatus.InfoError;
		}

		// Print the execution command
		logger.info("LocalExecutionAction Message: " + "Launching local command: " + "\"" + cmd + "\"");

		// Log the output
		stdOutStream = job.getInputStream();
		stdErrStream = job.getErrorStream();
		logOutput(stdOutStream, stdErrStream);
		if (logOutput(stdOutStream, stdErrStream).equals(FormStatus.InfoError)) {
			// Throw an error if the streaming fails
			return FormStatus.InfoError;
		}

		// Try to get the exit value of the job
		try {
			exitValue = job.exitValue();
		} catch (IllegalThreadStateException e) {
			// The job is still running, so it should be watched by someone
			// else.
			return FormStatus.Processing;
		}

		// By convention exit values other than zero mean that the program
		// failed. I follow that convention here.
		if (exitValue == 0) {
			return FormStatus.Processed;
		} else {
			return FormStatus.InfoError;
		}
	}

	/**
	 * This operation is responsible for monitoring the exit value of the
	 * running job. It uses the isLocal atomic to determine whether it should
	 * check the local job or the remote job. Ideally this operation would not
	 * rely on global variables, but since IRemoteProcess and Process are not
	 * part of the same inheritance hierarchy, there is no better way to deal
	 * with it.
	 */
	protected void monitorJob() {

		// Local Declarations
		int exitValue = -32; // Totally arbitrary

		// Wait until the job exits. By convention an exit code of
		// zero means that the job has succeeded. Watch it until it
		// finishes.
		while (exitValue != 0) {
			// Try to get the exit value of the job
			try {
				exitValue = job.exitValue();
			} catch (IllegalThreadStateException e) {
				// Complain, but keep watching
				logger.error(getClass().getName() + " Exception!", e);
			}
			// Give it a second
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Complain
				logger.error(getClass().getName() + " Exception!", e);
			}

			// If for some reason the job has failed,
			// it shouldn't be alive and we should break;
			if (!job.isAlive()) {
				break;
			}
		}
		logger.info("LocalExecutionAction Message: Exit value = " + exitValue);

		return;
	}

	/**
	 * This operation logs the content of the output and error streams
	 *
	 * @param output
	 *            The output stream from the code
	 * @param errors
	 *            The stream of errors from the code
	 * @return The status of the logging activities
	 */
	protected FormStatus logOutput(InputStream output, InputStream errors) {

		// Local Declarations
		InputStreamReader stdOutStreamReader, stdErrStreamReader;
		BufferedReader stdOutReader, stdErrReader;
		String nextLine;

		// Setup the BufferedReader that will get stdout from the process.
		stdOutStreamReader = new InputStreamReader(output);
		stdOutReader = new BufferedReader(stdOutStreamReader);
		// Setup the BufferedReader that will get stderr from the process.
		stdErrStreamReader = new InputStreamReader(errors);
		stdErrReader = new BufferedReader(stdErrStreamReader);

		// Catch the stdout and stderr output
		try {
			// Write to the stdOut file
			while ((nextLine = stdOutReader.readLine()) != null) {
				// logger.info(nextLine);
				stdOut.write(nextLine);
				// MUST put a new line for this type of writer. "\r\n" works on
				// Windows and Unix-based systems.
				stdOut.write("\r\n");
				postConsoleText(nextLine);
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
			logger.error(getClass().getName() + " Exception!", e);
			return FormStatus.InfoError;
		}

		return FormStatus.Processing;
	}
}
