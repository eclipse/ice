/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Alexander J. McCaskey, Robert Smith
 *******************************************************************************/
package org.eclipse.ice.item.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.core.IRemotePortForwardingService;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.IRemoteProcessService;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * The RemoteExecutionAction is a subclass of RemoteAction that executes a
 * specified executable on a remote host.
 * 
 * This Action requires a key-value pair list that contains the following:
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
 * hostname
 * </p>
 * </td>
 * <td>
 * <p>
 * The name of the remote host to upload files to.
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
 * *
 * <tr>
 * <td>
 * <p>
 * localJobLaunchDirectory
 * </p>
 * </td>
 * <td>
 * <p>
 * The name of the directory within the project/jobs folder where the files to
 * be uploaded can be found (optional).
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
 * </table>
 *
 * @author Alex McCaskey
 *
 */
public class RemoteExecutionAction extends RemoteAction implements Runnable {

	/**
	 * Reference to the IFolder where this local execution is to take place.
	 */
	private IFolder localLaunchFolder;

	/**
	 * Output streams for stdout and stderr
	 */
	private BufferedWriter stdOut, stdErr;

	/**
	 * An IRemoteProcess that is used by PTP for remote execution.
	 */
	private IRemoteProcess remoteJob = null;

	/**
	 * AtomicBoolean to handle cancellations.
	 */
	private AtomicBoolean cancelled;

	/**
	 * The username with which to log into the remote system.
	 */
	private String username;

	/**
	 * An atomic boolean used to notify the thread that it should proceed with
	 * the launch because the Form has been submitted.
	 */
	private AtomicBoolean formSubmitted;

	/**
	 * An AtomicReference that is used to synchronize the Form for multiple
	 * thread access.
	 */
	private AtomicReference<LoginInfoForm> formAtomic;

	/**
	 * The ICEJschUIInfo class used to provide password information to Jsch.
	 */
	private ICEJschUIInfo jschUIInfo;

	/**
	 * Reference to the ExecutionHelper which is used to perform pre and post
	 * processing tasks for this Action.
	 */
	private ExecutionHelper helper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Now that we're executing, set the
		// status to Processing
		status = FormStatus.Processing;

		// Initialize the cancelled and submitted booleans
		cancelled = new AtomicBoolean(false);
		formSubmitted = new AtomicBoolean(true);

		// Create the Thread to process this remote execution
		Thread processThread = new Thread(this);

		// Create the ExecutionHelper.
		helper = new ExecutionHelper(dictionary);

		// Indicate that this is remote launch
		helper.setIsLocal(false);

		// Validate the data
		if (helper.isDataValid()) {
			// Get a reference to the IFolder
			// for this local execution
			localLaunchFolder = helper.getLocalLaunchFolder();

			// Get the hostname
			String hostName = helper.getParameter("hostname");

			// Get the Remote Connection if available
			// If subclasses set it, then don't do anything
			String connectionName = dictionary.get("remoteConnectionName");
			if (connectionName == null) {
				connection = getRemoteConnection(hostName);
			} else {
				IRemoteConnectionType connectionType = getService(IRemoteServicesManager.class)
						.getRemoteConnectionTypes().get(0);
				for (IRemoteConnection c : connectionType.getConnections()) {
					if (connectionName.equals(c.getName())) {
						connection = c;
					}
				}
			}
			if (connection == null) {
				return actionError("Remote Execution Action could not get a valid connection to " + hostName + ".",
						null);
			}

			// Start the remote execution thread.
			processThread.start();

			return status;
		} else {
			return actionError("Remote Execution Error - the input data map was not valid.", null);
		}
	}

	/**
	 * This operation overrides Action.submitForm to add the Form to an Atomic
	 * container instead of the default Form class variable.
	 *
	 * @param form
	 *            The form being submitted.
	 * @return The status of the submission.
	 */
	@Override
	public FormStatus submitForm(Form form) {

		// Check the Form to make sure it is valid
		if (form != null && !formSubmitted.get()) {
			formAtomic.set((LoginInfoForm) form);
			jschUIInfo.setForm((LoginInfoForm) form);

			// Mark the Form as submitted
			formSubmitted.set(true);

			// Set the status
			status = FormStatus.Processing;
		} else {
			status = FormStatus.InfoError;
		}
		return status;
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
		if (remoteJob != null) {
			remoteJob.destroy();
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
		return "Remote Execution";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
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
			actionError("Remote Execution Action could not write headers to std out and err.", e);
			return;
		}

		// Launch the Job!
		launchRemotely();

		// Close the both output streams now that the work is done.
		try {
			stdOut.close();
			stdErr.close();
		} catch (IOException e) {
			// Complain
			actionError("Remote Execution Action could not close stdout or stderr!", e);
			return;
		}

		// Return successful FormStatus flag.
		status = FormStatus.Processed;
		return;
	}

	/**
	 * This operation launches the job on a remote machine.
	 */
	protected void launchRemotely() {

		// Local Declarations
		IRemoteProcessService processService = null;
		String hostname = helper.getParameter("hostname");
		String launchCMDFileName = "", launchCMD = "";

		// Block until the Form is submitted
		while (!formSubmitted.get()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// Complain
				logger.error(getClass().getName() + " Exception!", e1);
				return;
			}

			if (cancelled.get()) {
				logger.info("Remote Execution Action cancelled while waiting for form to be submitted.");
				status = FormStatus.ReadyToProcess;
				return;
			}
		}

		// Write the command script that contains all of the commands to launch.
		try {
			launchCMDFileName = helper.writeRemoteCommandFile();
		} catch (Exception e) {
			// Complain
			actionError("Remote Execution Action error in writing remote command file!", e);
			return;
		}

		// Create the command to launch based on whether this is a normal
		// machine or Titan.
		if (!hostname.equals("titan.ccs.ornl.gov")) {
			launchCMD = "./" + launchCMDFileName;
		} else {
			launchCMD = "qsub" + launchCMDFileName;
		}

		// If we have an alternate port then set it,
		// otherwise keep it as 22.
		if (helper.getParameter("port") != null) {
			int port = Integer.valueOf(helper.getParameter("port"));
			connection.getService(IRemoteConnectionHostService.class).setPort(port);
		}

		// Try to open the connection and fail if it will not open
		if (!connection.isOpen()) {
			try {
				connection.open(null);
			} catch (RemoteConnectionException e) {
				// Print diagnostic information and fail
				actionError("Remote Execution Action could not open the connection.!", e);
				return;
			}
		}

		// Launch the job!
		if (connection.isOpen() && !cancelled.get()) {

			// Get the IRemoteProcessService
			processService = connection.getService(IRemoteProcessService.class);

			// Set the new working directory
			String remoteSeparator = connection.getProperty(IRemoteConnection.FILE_SEPARATOR_PROPERTY);
			String userHome = connection.getProperty(IRemoteConnection.USER_HOME_PROPERTY);
			processService.setWorkingDirectory(userHome + remoteSeparator + "ICEJobs" + remoteSeparator
					+ helper.getParameter("localJobLaunchDirectory"));

			// Move the Launch Script to the Remote Directory!!
			try {
				File launchScript = localLaunchFolder.getFile(launchCMDFileName).getLocation().toFile();
				IRemoteFileService fileManager = connection.getService(IRemoteFileService.class);
				IFileStore remoteDirectory = EFS.getStore(fileManager.toURI(processService.getWorkingDirectory()));
				remoteDirectory.mkdir(EFS.NONE, null);
				IFileStore remoteFileStore = remoteDirectory.getChild(launchScript.getName());
				IFileStore localFileStore = EFS.getLocalFileSystem().fromLocalFile(launchScript);
				localFileStore.copy(remoteFileStore, EFS.OVERWRITE, null);
				logger.info("RemoteExecutionAction Message: " + "Uploaded file " + launchScript.getName());
			} catch (CoreException e) {
				// Print diagnostic information and fail
				actionError(
						getClass().getName() + " Exception! Could not move " + launchCMDFileName + " to remote host.",
						e);
				return;

			}

			// Dump the new working directory
			logger.info("Remote Execution Action Message: " + "PTP working directory set to "
					+ processService.getWorkingDirectory());

			// Create the process builder for the remote job
			IRemoteProcessBuilder processBuilder = processService.getProcessBuilder("sh", launchCMD);

			// Do not redirect the streams
			processBuilder.redirectErrorStream(false);

			try {
				logger.info("Remote Execution Action Message: " + "Attempting to launch with PTP...");
				logger.info(
						"Remote Execution Action Message: " + "Command sent to PTP = " + "sh ./" + launchCMDFileName);
				remoteJob = processBuilder.start(IRemoteProcessBuilder.FORWARD_X11);
			} catch (IOException e) {
				// Print diagnostic information and fail
				actionError("Error in executing the remote command.", e);
				return;
			}

			// Log the ouput
			InputStream stdOutStream = remoteJob.getInputStream();
			InputStream stdErrStream = remoteJob.getErrorStream();
			if (logOutput(stdOutStream, stdErrStream).equals(FormStatus.InfoError)) {
				// Throw an error if the streaming fails
				actionError("Remote Execution Error in logging the output.", null);
				return;
			}

			// Monitor the job
			monitorJob();

		}

		// Clear the files we care about
		helper.getInputFileMap().clear();

		// Close the connection
		connection.close();

		return;
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
				exitValue = remoteJob.exitValue();
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
			if (remoteJob.isCompleted()) {
				break;
			}
		}
		logger.info("Remote Execution Action Message: Exit value = " + exitValue);

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
				postConsoleText(nextLine);
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
			logger.error(getClass().getName() + " Exception!", e);
			return FormStatus.InfoError;
		}

		return FormStatus.Processing;
	}
}
