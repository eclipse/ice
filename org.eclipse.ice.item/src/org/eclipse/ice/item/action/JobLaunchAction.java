/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.item.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.IRemoteProcessService;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * <p>
 * This class launches a job, either locally or remotely. It is used for
 * launching simulations and other tasks in ICE. It uses ssh to connect to
 * remote machines for which it may require a username and password. If it needs
 * a username and password, execute() will post a FormStatus.NeedsInfo status
 * message and the getForm() operation will return a LoginInfoForm that contains
 * a DataComponent with the login request.
 * </p>
 * <p>
 * This Action launches the command "${executable} ${inputFile}" by default
 * where ${executable} is the executable to run and ${inputFile} is the name of
 * the input file.
 * </p>
 * <p>
 * The execute operation implements execute from the Action base class to launch
 * the job. It requires that the following key-value pairs are defined in its
 * dictionary in order to execute:
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
 * hostname
 * </p>
 * </td>
 * <td>
 * <p>
 * The hostname of the remote machine on which the job should be launched.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * noAppendInput
 * </p>
 * </td>
 * <td>
 * <p>
 * If this option is set to "true" then the JobLaunchAction will assume that the
 * input information is supplied to the executable in some other way than the
 * default and it will not append the name of the input file to the executable
 * name during the launch. It will still upload the input file. (So, erroneously
 * marking this value as none could cause a crash if the executable actually
 * needs it.)
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
 * uploadInput
 * </p>
 * </td>
 * <td>
 * <p>
 * If this option is set to true (default), then the JobLaunchAction will upload
 * the specified input file(s) to the remote location. If this option is set to
 * false, no input file(s) will be uploaded, with the exception of the job
 * launching script.
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
 * workingDir
 * </p>
 * </td>
 * <td>
 * <p>
 * The directory from which the executable should be run and in which the user's
 * input and output data should be stored. This directory will always be a
 * subdirectory of ~/ICEFiles/default/jobs. The action will store its files in a
 * generated directory in ~/ICEFiles/default/jobs if this value is not set.
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
 * numOMPThreads
 * </p>
 * </td>
 * <td>
 * <p>
 * The number of OpenMP threads that should be used by the job. The default
 * value of this is always 1.
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
 * <tr>
 * <td>
 * <p>
 * downloadDirectory
 * </p>
 * </td>
 * <td>
 * <p>
 * The directory on the remote machine from which data should be downloaded.
 * This is optional and no data will be downloaded if this is not specified.
 * </p>
 * </td>
 * </tr>
 * </table>
 *
 * The JobLaunchAction adds the working directory to the map with the key
 * "workingDir".
 *
 * The Action appends to the end of each output file listed in the map. It never
 * overwrites these files.
 *
 * Additional parameters may be specified in the dictionary and the
 * JobLaunchAction will replace them in the executable if required. For a key
 * "v" in the dictionary, each instance of the search string "${v}" in the
 * executable string will be placed with the value of the key in the dictionary.
 * Multiple input or data files may be specified as additional parameters, but
 * their key should end with the word "file" so that the JobLaunchAction can
 * place them in the local or remote working directory as needed. Failure to
 * properly name files will result in a failure. Capitalization does not matter.
 * The stdOutFile and stdErrFile are exceptions that are not transferred to
 * remote machines.
 *
 * The cancel() operation attempts to kill the process if it is still running.
 *
 * This class launches the job on a separate thread. An AtomicReference is used
 * for managing access to the Form information an the LoginInfoForm is used
 * simply as an internal reference to the current Form within an operation. It
 * also assumes that the command "cd" is used to change the working directory
 * and that a semicolon at the end of the line is acceptable to denote the end
 * of a command. This assumption is valid on Windows, Linux and Unix systems so
 * long as the Windows shell is Powershell.
 * <p>
 *
 * @author Jay Jay Billings, Anna Wojtowicz
 */
public class JobLaunchAction extends Action implements Runnable {

	/**
	 * The username with which to log into the remote system.
	 */
	private String username;

	/**
	 * The absolute path of the IProject being used by this job launch. It can
	 * be retrieved with IProject.getLocation().toOSString().
	 */
	private String projectSpaceDir;

	/**
	 * An atomic boolean used to notify the thread that it should proceed with
	 * the launch because the Form has been submitted.
	 */
	private AtomicBoolean formSubmitted;

	/**
	 * AtomicBoolean to handle cancellations.
	 */
	private AtomicBoolean cancelled;

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
	 * An AtomicBoolean that is true if the job is to be launched on the local
	 * machine and false otherwise. It is set in execute().
	 */
	private AtomicBoolean isLocal;

	/**
	 * The dictionary that contains the executable properties
	 */
	private Dictionary<String, String> execDictionary;

	/**
	 * Output streams for stdout and stderr
	 */
	BufferedWriter stdOut = null, stdErr = null;

	/**
	 * A process for storing the process information from the launch.
	 */
	private Process job = null;

	/**
	 * An IRemoteProcess that is used by PTP for remote execution.
	 */
	private IRemoteProcess remoteJob = null;

	/**
	 * A IFolder reference to the local job launch directory in
	 * projectSpace/jobs.
	 */
	private IFolder localLaunchFolder;

	/**
	 * A private flag to mark whether or not the input file name should be
	 * appended to the executable command. This is the opposite of the value of
	 * the "noAppendInput" argument in the argument map, if that key exists.
	 * This variable is only used on the thread and should not be reused off the
	 * thread.
	 */
	private boolean appendInput = true;

	/**
	 * Private flag to indicate if the input file for the JobLaunchAction should
	 * be uploaded on a remote machine. Be default, the behavior is true.
	 * Setting this flag to false will still allow the "launchJob.sh" script to
	 * be uploaded, but only this.
	 */
	private boolean uploadInput = true;

	/**
	 * The full command string of all stages that will be executed. If the
	 * executable is single stage, then the last piece of this string will be
	 * the input file. This variable is only used on the thread and should not
	 * be reused off the thread.
	 */
	private String fullCMD = "";

	/**
	 * The set of commands to execute split into stages. Each command is
	 * executed separately. If the executable is single stage, then the only
	 * entry in this list is at index zero and is identically equal to fullCMD.
	 * This variable is only used on the thread and should not be reused off the
	 * thread.
	 */
	private ArrayList<String> splitCMD = new ArrayList<String>();

	/**
	 * The name of the working directory in which the job will be launched. The
	 * default value is the prefix and the current date is appended to it.
	 */
	private String workingDirectoryBaseName = "iceLaunch_";

	/**
	 * A map for storing the input files that need to be uploaded. The map is
	 * populated when the executable name is fixed and read when the job is
	 * launched. The key is the "short" name of the file - the name minus the
	 * rest of the path - and the value is the full path of the file on the
	 * local file system.
	 */
	private Hashtable<String, String> fileMap;

	/**
	 * The maximum size limit of any file that will be downloaded from a remote
	 * machine, in bytes. The default size is 50 MB and is set as a VM argument
	 * called "max_download_size", which can be edited by the user in the ICE
	 * config file.
	 */
	private long maxFileSize;

	private IRemoteConnection connection;

	private IRemoteConnectionType connectionType;

	/**
	 * The Constructor.
	 */
	public JobLaunchAction() {

		// Setup the local flags
		isLocal = new AtomicBoolean();
		fileMap = new Hashtable<String, String>();
		cancelled = new AtomicBoolean(false);

		// Get the maxFileSize from the system properties
		String fileSize = System.getProperty("max_download_size");
		if (fileSize != null) {
			maxFileSize = Integer.parseInt(fileSize);
		} else {
			// If the system property is invalid for any reason, default to a
			// hardcoded value
			maxFileSize = 52428800;
		}

		return;
	}

	/**
	 * This operation fixes the name of the executable that will be launched. It
	 * replaces ${inputFile}, ${installDir} and other keys from the dictionary
	 * according to the specification. It also configures the commands to setup
	 * the parallel execution environment if indicated by the number of
	 * processors or threads.
	 *
	 * @return The name of the executable with all variable references and
	 *         required string replacements fixed.
	 */
	private String fixExecutableName() {

		// Local Declarations
		int numProcs = Math.max(1, Integer.parseInt(execDictionary.get("numProcs")));
		int numTBBThreads = Math.max(1, Integer.parseInt(execDictionary.get("numTBBThreads")));
		String fixedExecutableName = execDictionary.get("executable");
		String installDir = execDictionary.get("installDir");
		String workingDir = execDictionary.get("workingDir");
		String separator = "/";
		String shortInputName = null;

		// Print some debug information
		logger.info("JobLaunchAction Message: Raw executable command = " + fixedExecutableName);

		// Check to see whether the input file should be appended or if it was
		// already configured some other way.
		if (appendInput) {
			fixedExecutableName += " ${inputFile}";
		}
		// Set single stage command
		fullCMD = fixedExecutableName;

		// Determine the proper separator - just check to see if it is Windows,
		// which will have a :\ in it somewhere.
		if (installDir != null && installDir.contains(":\\")) {
			separator = "\\";
		}
		// Append a final separator to the install directory if required
		if (installDir != null && !installDir.endsWith(separator)) {
			installDir = installDir + separator;
		}

		// Replace any file name variables found in the executable string by
		// looking them up in the map.
		Enumeration<String> keys = execDictionary.keys();
		String fixedFileName;
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key.toLowerCase().endsWith("file")) {
				// Default to the normal name of the file on the local machine
				fixedFileName = execDictionary.get(key);

				// NOTE, we used to only shorten these file paths for a remote
				// launch
				// but now we create a temp working dir for local launches to,
				// so we must
				// do this for both remote and local launches.

				// if (!isLocal.get()) {
				// The name has to be changed on a remote machine to conform
				// to the target file system. Shorten the input name - look
				// for the last / or \
				shortInputName = execDictionary.get(key);
				if (shortInputName.contains("/")) {
					shortInputName = shortInputName.substring(shortInputName.lastIndexOf("/") + 1);
				} else if (shortInputName.contains("\\")) {
					shortInputName = shortInputName.substring(shortInputName.lastIndexOf("\\") + 1);
				}
				// Update the fixed file name
				fixedFileName = shortInputName;

				// Put the file name in the map
				fileMap.put(shortInputName, execDictionary.get(key));
				// }
				// Update the executable name to account for any changes that
				// may result because of it being on a remote machine.
				fixedExecutableName = fixedExecutableName.replace("${" + key + "}", fixedFileName);
			}
		}
		// Fix the installation directory by replacing the ${installDir} flags
		// in the executable string.
		if (fixedExecutableName.contains("${installDir}") && installDir != null) {
			fixedExecutableName = fixedExecutableName.replace("${installDir}", installDir);
		}
		// Fix the working directory by replacing the ${workingDir} flags in the
		// executable string.
		if (fixedExecutableName.contains("${workingDir}") && workingDir != null) {
			fixedExecutableName = fixedExecutableName.replace("${workingDir}", workingDir);
		}
		// Figure out whether or not MPI should be used.
		if (numProcs > 1) {
			// A temporary modification to work with Titan & PBS.
			if (!execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
				// Add the MPI command if there are multiple cores
				fixedExecutableName = "mpiexec -n " + numProcs + " " + fixedExecutableName;
			} else {
				// Add the MPI command for aprun if there are multiple cores
				fixedExecutableName = "aprun -n " + numProcs + " " + fixedExecutableName;
			}
		}

		// Figure out whether or not TBB should be used.
		if (numTBBThreads > 1) {
			// Add the threads flag
			fixedExecutableName = fixedExecutableName + " --n-threads=" + numTBBThreads;
		}

		// Clean up any leading or trailing whitespace
		fixedExecutableName = fixedExecutableName.trim();

		// Split the command into its stages. Most of the time there will only
		// be one stage, so check that branch first. This should always be the
		// last thing done by this operation.
		if (!fixedExecutableName.contains(";")) {
			// Add the full CMD since there is only one stage
			splitCMD.add(fixedExecutableName);
		} else {
			for (String stage : fixedExecutableName.split(";")) {
				splitCMD.add(stage);
			}
		}

		// Print launch stages
		for (int i = 0; i < splitCMD.size(); i++) {
			String cmd = splitCMD.get(i);
			logger.info("JobLaunchAction Message: Launch stage " + i + " = " + cmd);
		}

		return fixedExecutableName;
	}

	/**
	 * This operation returns a buffered writer to the caller that will append
	 * to file specified in the call.
	 *
	 * @param filename
	 *            The name of the file to which the BufferedWriter should
	 *            append.
	 * @return The BufferedWriter.
	 */
	private BufferedWriter getBufferedWriter(String filename) {

		// Local Declarations
		FileWriter writer = null;
		BufferedWriter bufferedWriter = null;

		// Check the file name and the create the writer
		if (filename != null) {
			try {
				writer = new FileWriter(filename, true);
			} catch (IOException e) {
				// Complain
				logger.error(getClass().getName() + " Exception!", e);
			}
			bufferedWriter = new BufferedWriter(writer);
			return bufferedWriter;
		} else {
			return null;
		}
	}

	/**
	 * This operation checks the hostname to determine whether or not it is the
	 * same as localhost.
	 *
	 * @param hostname
	 *            The hostname of the target platform on which the job will be
	 *            launched.
	 * @return True if the hostname is the same as localhost, false otherwise.
	 */
	private boolean isLocalhost(String hostname) {

		// Local Declarations
		boolean retVal = false;
		String localHostname = null;

		// The simplest names to check are 127.0.0.1 and localhost.localdomain.
		// These names are always the local machine on Unix systems.
		if ("127.0.0.1".equals(hostname) || "localhost.localdomain".equals(hostname) || "localhost".equals(hostname)) {
			retVal = true;
		} else {
			// Get the local hostname by looking up the InetAddress
			try {
				// Get the address of localhost
				InetAddress addr = InetAddress.getLocalHost();
				// Get the hostname
				localHostname = addr.getHostName();
			} catch (UnknownHostException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
			// Compare the names
			if (hostname.equals(localHostname)) {
				retVal = true;
			}
		}

		logger.info("JobLaunchAction Message: Localhost hostname = " + localHostname);
		logger.info("JobLaunchAction Message: Target " + "Platform hostname = " + hostname);
		logger.info("JobLaunchAction Message: Host is" + ((retVal) ? " " : " NOT ") + "localhost.");

		return retVal;
	}

	/**
	 * This operation launches the job on the local machine.
	 */
	protected void launchLocally() {

		// Local Declarations
		FormStatus launchStatus;

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

	protected FormStatus launchStageLocally(String cmd, BufferedWriter stdOut, BufferedWriter stdErr) {

		// Local Declarations
		String errMsg = null;
		ProcessBuilder jobBuilder = null;
		InputStream stdOutStream = null, stdErrStream = null;
		int exitValue = -1;
		String os = execDictionary.get("os");
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
		logger.info("JobLaunchAction Message: " + "Launching local command: " + "\"" + cmd + "\"");

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
				exitValue = (isLocal.get()) ? job.exitValue() : remoteJob.exitValue();
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
			if (isLocal.get()) {
				if (!job.isAlive()) {
					break;
				}
			} else {
				if (remoteJob.isCompleted()) {
					break;
				}
			}
		}
		logger.info("JobLaunchAction Message: Exit value = " + exitValue);

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
				//logger.info(nextLine);
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
			logger.error(getClass().getName() + " Exception!", e);
			return FormStatus.InfoError;
		}

		return FormStatus.Processing;
	}

	/**
	 * This operation launches the job on a remote machine.
	 */
	protected void launchRemotely() {

		// Local Declarations
		IRemoteConnectionWorkingCopy workingCopy = null;
		IRemoteProcessService processService = null;
		Date currentDate = new Date();
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		String hostname = execDictionary.get("hostname");
		String launchCMDFileName = "", launchCMD = "";
		List<IFile> files = new ArrayList<IFile>();
		Dictionary<String, String> uploadDataMap = new Hashtable<String, String>();
		Dictionary<String, String> downloadDataMap = new Hashtable<String, String>();

		// Get the directory where local files should be stored
		IFileStore localDirectory = EFS.getLocalFileSystem().getStore(localLaunchFolder.getLocationURI());

		// Block until the Form is submitted
		while (!formSubmitted.get()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// Complain
				logger.error(getClass().getName() + " Exception!", e1);
				return;
			}
			// DEBUG - logger.info("Form not yet submitted.");
		} // FIXME - Need to be able to cancel!

		// Write the command script that contains all of the commands to launch.
		try {
			launchCMDFileName = writeRemoteCommandFile();
		} catch (Exception e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
			return;
		}

		// Create the command to launch based on whether this is a normal
		// machine or Titan.
		if (!execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
			launchCMD = "./" + launchCMDFileName;
		} else {
			launchCMD = "qsub" + launchCMDFileName;
		}

		// Create the List of Files that needs to be uploaded
		// and set the remote upload actions data map data.
		uploadDataMap.put("remoteDir", "ICEJobs" + workingDirectoryBaseName);
		downloadDataMap.put("localDir", localLaunchFolder.getLocation().toOSString());
		for (String shortInputName : fileMap.keySet()) {
			files.add(localLaunchFolder.getFile(shortInputName));
		}

		// If we don't have a valid connection, then we should
		// have gotten valid user credentials for the remote machine
		// through the NeedsInfo status. Now we create the
		// IRemoteConnection
		if (connection == null) {

			if (connectionType == null) {
				logger.error("Invalid ConnectionType! Cannot create a new IRemoteConnection.");
				status = FormStatus.InfoError;
				return;
			}

			// Get the DataComponent containing the username and password
			DataComponent credentials = (DataComponent) formAtomic.get().getComponent(1);

			// Create a new IRemoteConnectionWorkingCopy
			try {
				workingCopy = connectionType.newConnection(hostname + "_" + shortDate.format(currentDate));
			} catch (RemoteConnectionException e3) {
				e3.printStackTrace();
				status = FormStatus.InfoError;
				return;
			}

			// FIXME THIS IS BAD, BUT I HAVE NO CLUE HOW TO OTHERWISE
			// Set the hostname, username, and password
			workingCopy.setAttribute("JSCH_ADDRESS_ATTR", hostname);
			workingCopy.setAttribute("JSCH_USERNAME_ATTR", credentials.retrieveAllEntries().get(0).getValue());
			workingCopy.setSecureAttribute("JSCH_PASSWORD_ATTR", credentials.retrieveAllEntries().get(1).getValue());

			// Create the IRemoteConnection
			try {
				connection = workingCopy.save();
			} catch (RemoteConnectionException e) {
				// TODO Auto-generated catch block
				logger.error(getClass().getName() + " Exception!", e);
				status = FormStatus.InfoError;
				return;
			}
		}

		// Try to open the connection and fail if it will not open
		try {
			connection.open(null);
		} catch (RemoteConnectionException e) {
			// Print diagnostic information and fail
			logger.error(getClass().getName() + " Exception!", e);
			status = FormStatus.InfoError;
			return;
		}

		// Do the upload(s), launch the job, and download the results if the
		// connection is open
		if (connection.isOpen() && !cancelled.get()) {
			// Diagnostic info
			logger.info("JobLaunchAction Message:"
					+ " PTP connection established. Uploading required files to remote machine.");

			status = FormStatus.Processing;
			
			// !============= FILES UPLOAD ==============!
			
			// Get the file separator on the remote system
			String remoteSeparator = connection.getProperty(IRemoteConnection.FILE_SEPARATOR_PROPERTY);
			uploadDataMap.put("remoteDir", "ICEJobs" + remoteSeparator + workingDirectoryBaseName);
			uploadDataMap.put("remoteHost", execDictionary.get("hostname"));
			String filesString = "";
			for (IFile f : files) {
				filesString += f.getLocation().toOSString() + ";";
			}
			filesString = filesString.substring(0, filesString.length()-1);
			uploadDataMap.put("uploadFiles", filesString);
			uploadDataMap.put("localFilesLocation", localLaunchFolder.getLocation().toOSString());

			// Create and execute a Remote File Upload action
			RemoteFileUploadAction uploadAction = new RemoteFileUploadAction();
			status = uploadAction.execute(uploadDataMap);
			if (status == FormStatus.InfoError) {
				logger.error("JobLaunchAction Error - Failed to upload files to remote machine.");
				return;
			}

			// !============= JOB EXECUTION ==============!
			
			// Get the IRemoteProcessService
			processService = connection.getService(IRemoteProcessService.class);

			// Set the new working directory
			processService.setWorkingDirectory(uploadAction.getRemoteUploadDirectoryPath());

			// Dump the new working directory
			logger.info(
					"JobLaunchActionMessage: " + "PTP working directory set to " + processService.getWorkingDirectory());

			// Create the process builder for the remote job
			IRemoteProcessBuilder processBuilder = processService.getProcessBuilder("sh", launchCMD);

			// Do not redirect the streams
			processBuilder.redirectErrorStream(false);
			
			try {
				logger.info("JobLaunchAction Message: " + "Attempting to launch with PTP...");
				logger.info("JobLaunchAction Message: " + "Command sent to PTP = " + "sh ./" + launchCMDFileName);
				remoteJob = processBuilder.start(IRemoteProcessBuilder.FORWARD_X11);
			} catch (IOException e) {
				// Print diagnostic information and fail
				logger.error(getClass().getName() + " Exception!", e);
				status = FormStatus.InfoError;
				return;
			}

			// Log the ouput
			InputStream stdOutStream = remoteJob.getInputStream();
			InputStream stdErrStream = remoteJob.getErrorStream();
			if (logOutput(stdOutStream, stdErrStream).equals(FormStatus.InfoError)) {
				// Throw an error if the streaming fails
				status = FormStatus.InfoError;
				return;
			}

			// !========== JOB MONITORING ============!
			
			// Monitor the job
			monitorJob();

			// !=========== DOWNLOAD FILES ===========!

			// - Download the output if possible and if it wasn't cancelled - //
			// Check to see if the job should be cancelled.
			if (!cancelled.get()) {

				// Get download directory
				String remoteDir = processService.getWorkingDirectory();
				downloadDataMap.put("remoteDir", remoteDir);
				downloadDataMap.put("remoteHost", execDictionary.get("hostname"));

				logger.info("JobLaunchAction Message: " + "Downloading files to local directory "
						+ localDirectory.getName() + " from remote directory" + remoteDir + ".");

				// Create and execute the remote files download action!
				RemoteFileDownloadAction downloadAction = new RemoteFileDownloadAction();
				status = downloadAction.execute(downloadDataMap);
				if (status == FormStatus.InfoError) {
					logger.error("JobLaunchAction Error - Failed to download files from remote machine.");
					return;
				}
			}
		}

		// Set the status
		status = FormStatus.Processed;

		// Clear the files we care about
		fileMap.clear();
		
		// Close the connection
		connection.close();

		return;
	}

	/**
	 * This operation creates a file that contains all of the commands that need
	 * to be launched, one per line.
	 *
	 * It adds the file to file map so that it is automatically moved to the
	 * working directory by the calling routine.
	 *
	 * It writes it in the local working directory from which ICE was run.
	 *
	 * @return The full path to the launch file.
	 * @throws IOException
	 */
	private String writeRemoteCommandFile() throws IOException {

		// Local Declarations
		Date currentDate = new Date();
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		IFile launchFile = localLaunchFolder.getFile("launchJob_" + shortDate.format(currentDate) + ".sh");

		try {
			launchFile.create(new ByteArrayInputStream("".getBytes()), true, null);
			String contents = "";

			// Write the header if the target machine is Titan
			if (execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
				// Get the number of cores
				int numCores = Math.max(1, Integer.parseInt(execDictionary.get("numProcs")));
				// Calculate the number of nodes.
				int numNodes = Math.min(numCores / 16 + 1, 18688);
				contents = "#!/bin/bash\n" + "# Begin PBS directives\n" + "# Begin PBS directives\n" + "#PBS -A "
						+ execDictionary.get("accountCode") + "\n" + "#PBS -N " + workingDirectoryBaseName + "\n"
						+ "#PBS -j oe\n" + "#PBS -l walltime=1:00:00,nodes=" + numNodes + "\n"
						+ "# End PBS directives and begin shell commands\n";

			}

			// Write each command into the file
			for (String singleCMD : splitCMD) {
				contents += singleCMD + "\n";
				// launchFileWriter.write(singleCMD + "\n");
			}

			// Write the file
			launchFile.appendContents(new ByteArrayInputStream(contents.getBytes()), IResource.FORCE, null);

			// Put the file into the file map
			fileMap.put(launchFile.getName(), launchFile.getName());

			return launchFile.getName();

		} catch (CoreException e) {
			logger.error("JobLaunchAction Error: Error creating launchJob.sh", e);
		}

		return null;
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

	private IProject project;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Local Declarations
		DataComponent loginInfoComp;
		Entry usernameEntry;
		Thread processThread = new Thread(this);
		formSubmitted = new AtomicBoolean();
		formSubmitted.set(true);

		// Determine if this is a local launch or not
		String hostname = dictionary.get("hostname");
		isLocal.set(isLocalhost(hostname));

		// Set the default value of the status to processing
		status = FormStatus.Processing;

		// Set the dictionary reference
		execDictionary = dictionary;

		// Setup for remote launch if needed
		if (!isLocal.get() && !connectionIsValid()) {

			// Create the new Form
			actionForm = new LoginInfoForm();

			// Create the UIInfo
			jschUIInfo = new ICEJschUIInfo();

			// Get the username and password entries
			loginInfoComp = (DataComponent) actionForm.getComponent(1);
			usernameEntry = loginInfoComp.retrieveEntry("Username");
			loginInfoComp.retrieveEntry("Password");

			// Set the username
			username = System.getProperty("user.name");
			usernameEntry.setValue(username);

			// Mark the form as not yet submitted
			formSubmitted.set(false);
			formAtomic = new AtomicReference<LoginInfoForm>();

			// Set the status
			status = FormStatus.NeedsInfo;
		}

		// Start the thread
		processThread.start();

		return status;
	}

	/**
	 * This utility method checks that the JobLaunchAction's IRemoteConnection
	 * is valid. By valid we mean that it is not null (so it's been set
	 * correctly) and it's provided host name is the same as the host name in
	 * the execDictionary (ie, the host name specified by the user).
	 *
	 * @return
	 */
	private boolean connectionIsValid() {

		if (connection != null) {
			// Get the hostname and the IRemoteConnection's hostname
			String hostname = execDictionary.get("hostname");
			String connectionHost = connection.getService(IRemoteConnectionHostService.class).getHostname();
			try {
				// Make sure they are the same
				if (InetAddress.getByName(hostname).getHostAddress()
						.equals(InetAddress.getByName(connectionHost).getHostAddress())) {
					return true;
				} else {
					return false;
				}
			} catch (UnknownHostException e) {
				logger.error(getClass().getName() + " Exception!", e);
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Action#cancel()
	 */
	@Override
	public FormStatus cancel() {

		// Throw the flag
		cancelled.set(true);

		// Stop local jobs
		if (isLocal.get() && job != null) {
			job.destroy();
		} else {
			// Stop remote jobs
			if (remoteJob != null) {
				remoteJob.destroy();
			}
		}

		return FormStatus.ReadyToProcess;
	}

	/**
	 * Set the working directory name.
	 */
	private void setWorkingDirectoryName() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String launchDir = "";

		// Set the base name of the working directory.
		workingDirectoryBaseName = execDictionary.get("localJobLaunchDirectory");// "iceLaunch_"
																					// +
																					// shortDate.format(currentDate);

		// Set the name of the working directory properly if it is a local
		// launch
		// if (isLocal.get()) {
		// launchDir = projectSpaceDir + separator + "jobs" + separator +
		// workingDirectoryBaseName;
		/* } else */
		if (execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
			// Get the project directory
			String projId = execDictionary.get("accountCode");
			// Set the directory to the $PROJ_WORK/projId directory on Titan
			launchDir = "$PROJWORK" + separator + projId + separator + workingDirectoryBaseName;
		} else {
			// Otherwise just leave it in the home directory
			launchDir = workingDirectoryBaseName;
		}

		logger.info("JobLaunchAction: Setting " + launchDir + " as the local job launch directory.");

		// Put the directory in the dictionary
		execDictionary.put("workingDir", launchDir);

		// Dump some debug info
		logger.info("JobLaunchAction Message: Working directory = " + execDictionary.get("workingDir"));

		return;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Runnable#run()
	 */
	@Override
	public void run() {

		// Local Declarations
		String executable = null, inputFile = null, hostname = null;
		String stdOutFileName = null, stdErrFileName = null;
		String stdOutHeader = null, stdErrHeader = null;

		// Make sure the dictionary exists
		if (execDictionary != null) {
			// Make sure the dictionary contains the keys we need
			executable = execDictionary.get("executable");
			inputFile = execDictionary.get("inputFile");
			stdOutFileName = execDictionary.get("stdOutFileName");
			stdErrFileName = execDictionary.get("stdErrFileName");
			hostname = execDictionary.get("hostname");
			projectSpaceDir = execDictionary.get("projectSpaceDir");
			uploadInput = Boolean.valueOf(execDictionary.get("uploadInput"));
		}

		// Check the info and return if it is not available
		if (executable == null || (uploadInput && inputFile == null) || stdOutFileName == null || stdErrFileName == null
				|| hostname == null) {
			status = FormStatus.InfoError;
			return;
		}

		// Get the flag from the dictionary that dictates whether or not the
		// input file name should be appended to the executable command. It is
		// optional, so treat it specially.
		if (execDictionary.get("noAppendInput") != null && ("true").equals(execDictionary.get(("noAppendInput")))) {
			appendInput = false;
		}

		// Get a reference to the IProject
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(new File(projectSpaceDir).getName());
		localLaunchFolder = project.getFolder("jobs").getFolder(execDictionary.get("localJobLaunchDirectory"));

		// Set the appropriate working directory name
		setWorkingDirectoryName();

		// Set the command to execute.
		fullCMD = fixExecutableName();

		// Setup the output streams, stdout first
		stdOutFileName = execDictionary.get("stdOutFileName");
		stdOut = getBufferedWriter(stdOutFileName);
		// stderr second
		stdErrFileName = execDictionary.get("stdErrFileName");
		stdErr = getBufferedWriter(stdErrFileName);

		// Setup the stdout and stderr headers
		stdOutHeader = createOutputHeader("standard output");
		stdErrHeader = createOutputHeader("standard error");
		// And write them
		try {
			stdOut.write(stdOutHeader);
			stdErr.write(stdErrHeader);
		} catch (IOException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
		}

		// Copy all files needed to the local launch directory
		try {
			for (String fileName : fileMap.keySet()) {
				logger.info("JobLaunchAction copying " + fileName + " to local job launch folder: " + localLaunchFolder.getLocation().toOSString() + ".");
				IFile newFile = localLaunchFolder.getFile(fileName);
				newFile.create(project.getFile(fileName).getContents(), true, null);
			}
		} catch (CoreException e) {
			logger.error("JobLaunchAction Error - Could not copy files from the project space to the job folder.", e);
			status = FormStatus.InfoError;
			return;
		}

		// Determine where to launch
		if (isLocal.get()) {
			// Launch on the local machine
			launchLocally();
		} else {
			// Launch on a remote machine
			launchRemotely();
		}

		// Close the both output streams now that the work is done.
		try {
			stdOut.close();
			stdErr.close();
		} catch (IOException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
			status = FormStatus.InfoError;
			return;
		}

		return;
	}

	/**
	 * This operation creates a standard header that contains information about
	 * the job being launched. It is used primarily by the run() operation.
	 *
	 * @param logName
	 *            The name that should be used to identify the log in its
	 *            header.
	 * @return The header.
	 */
	private String createOutputHeader(String logName) {

		// Local Declarations
		String header = null, localHostname = null;

		// Get the identity of this machine as the point of origin for the job
		// launch
		try {
			// Get the address of localhost
			InetAddress addr = InetAddress.getLocalHost();
			// Get the hostname
			localHostname = addr.getHostName();
		} catch (UnknownHostException e) {
			logger.error(getClass().getName() + " Exception!", e);
		}

		// Add the date and time
		header = "# Job launch date: ";
		header += new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "\n";
		// Add the point of origin
		header += "# Launch host: " + localHostname + "\n";
		// Add the target machine
		header += "# Target host: " + execDictionary.get("hostname") + "\n";
		// Add the execution command
		header += "# Command Executed: " + fullCMD.replace("\n", ";") + "\n";
		// Add the input file name
		if (uploadInput) {
			header += "# Input file: " + execDictionary.get("inputFile") + "\n";
		}
		// Add the working directory
		header += "# Working directory: " + execDictionary.get("workingDir") + "\n";
		// Add an empty line
		header += "\n";

		return header;
	}

	/**
	 * Provide the JobLaunchAction with an existing IRemoteConnection. This
	 * connection will be used for remote launches if its corresponding hostname
	 * is the same as the user specified host name as defined in the
	 * execDictionary.
	 *
	 * @param remoteConnection
	 */
	public void setRemoteConnection(IRemoteConnection remoteConnection) {
		connection = remoteConnection;
	}

	public void setRemoteConnectionType(IRemoteConnectionType type) {
		connectionType = type;
	}

	@Override
	public String getActionName() {
		return "Job Launch Action";
	}

}
