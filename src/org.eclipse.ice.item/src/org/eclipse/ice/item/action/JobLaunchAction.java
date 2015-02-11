/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import org.eclipse.ice.datastructures.form.Form;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ptp.remote.core.IRemoteConnection;
import org.eclipse.ptp.remote.core.IRemoteFileManager;
import org.eclipse.ptp.remote.core.IRemoteProcess;
import org.eclipse.ptp.remote.core.IRemoteProcessBuilder;
import org.eclipse.ptp.remote.core.IRemoteServices;
import org.eclipse.ptp.remote.core.RemoteServices;
import org.eclipse.ptp.remote.core.exception.RemoteConnectionException;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;

/**
 * <!-- begin-UML-doc -->
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
 * 
 * @author Jay Jay Billings
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An atomic boolean used to notify the thread that it should proceed with
	 * the launch because the Form has been submitted.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicBoolean formSubmitted;

	/**
	 * AtomicBoolean to handle cancellations.
	 */
	private AtomicBoolean cancelled;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An AtomicReference that is used to synchronize the Form for multiple
	 * thread access.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicReference<LoginInfoForm> formAtomic;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ICEJschUIInfo class used to provide password information to Jsch.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEJschUIInfo jschUIInfo;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An AtomicBoolean that is true if the job is to be launched on the local
	 * machine and false otherwise. It is set in execute().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * machine in bytes. It is 20MB by default.
	 */
	private long maxFileSize = 20971520;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public JobLaunchAction() {
		// begin-user-code

		// Setup the local flags
		isLocal = new AtomicBoolean();
		fileMap = new Hashtable<String, String>();
		cancelled = new AtomicBoolean(false);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation fixes the name of the executable that will be launched. It
	 * replaces ${inputFile}, ${installDir} and other keys from the dictionary
	 * according to the specification. It also configures the commands to setup
	 * the parallel execution environment if indicated by the number of
	 * processors or threads.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The name of the executable with all variable references and
	 *         required string replacements fixed.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String fixExecutableName() {
		// begin-user-code

		// Local Declarations
		int numProcs = Math.max(1,
				Integer.parseInt(execDictionary.get("numProcs")));
		int numTBBThreads = Math.max(1,
				Integer.parseInt(execDictionary.get("numTBBThreads")));
		String fixedExecutableName = execDictionary.get("executable");
		String installDir = execDictionary.get("installDir");
		String workingDir = execDictionary.get("workingDir");
		String separator = "/";
		String shortInputName = null;

		// Print some debug information
		System.out.println("JobLaunchAction Message: Raw executable command = "
				+ fixedExecutableName);

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
					shortInputName = shortInputName.substring(shortInputName
							.lastIndexOf("/") + 1);
				} else if (shortInputName.contains("\\")) {
					shortInputName = shortInputName.substring(shortInputName
							.lastIndexOf("\\") + 1);
				}
				// Update the fixed file name
				fixedFileName = shortInputName;
				// Put the file name in the map
				fileMap.put(shortInputName, execDictionary.get(key));
				// }
				// Update the executable name to account for any changes that
				// may result because of it being on a remote machine.
				fixedExecutableName = fixedExecutableName.replace("${" + key
						+ "}", fixedFileName);
			}
		}
		// Fix the installation directory by replacing the ${installDir} flags
		// in the executable string.
		if (fixedExecutableName.contains("${installDir}") && installDir != null) {
			fixedExecutableName = fixedExecutableName.replace("${installDir}",
					installDir);
		}
		// Fix the working directory by replacing the ${workingDir} flags in the
		// executable string.
		if (fixedExecutableName.contains("${workingDir}") && workingDir != null) {
			fixedExecutableName = fixedExecutableName.replace("${workingDir}",
					workingDir);
		}
		// Figure out whether or not MPI should be used.
		if (numProcs > 1) {
			// A temporary modification to work with Titan & PBS.
			if (!execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
				// Add the MPI command if there are multiple cores
				fixedExecutableName = "mpiexec -n " + numProcs + " "
						+ fixedExecutableName;
			} else {
				// Add the MPI command for aprun if there are multiple cores
				fixedExecutableName = "aprun -n " + numProcs + " "
						+ fixedExecutableName;
			}
		}

		// Figure out whether or not TBB should be used.
		if (numTBBThreads > 1) {
			// Add the threads flag
			fixedExecutableName = fixedExecutableName + " --n-threads="
					+ numTBBThreads;
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
			System.out.println("JobLaunchAction Message: Launch stage " + i
					+ " = " + cmd);
		}

		return fixedExecutableName;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the username from the LoginInfoForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The username.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String getUsernameFromForm() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a new SSH session for the given username.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dictionary
	 *            <p>
	 *            The dictionary of values to be used to create the session.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createSession(Dictionary<String, String> dictionary) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a buffered writer to the caller that will append
	 * to file specified in the call.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param filename
	 *            <p>
	 *            The name of the file to which the BufferedWriter should
	 *            append.
	 *            </p>
	 * @return <p>
	 *         The BufferedWriter.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private BufferedWriter getBufferedWriter(String filename) {
		// begin-user-code

		// Local Declarations
		FileWriter writer = null;
		BufferedWriter bufferedWriter = null;

		// Check the file name and the create the writer
		if (filename != null) {
			try {
				writer = new FileWriter(filename, true);
			} catch (IOException e) {
				// Complain
				e.printStackTrace();
			}
			bufferedWriter = new BufferedWriter(writer);
			return bufferedWriter;
		} else {
			return null;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the hostname to determine whether or not it is the
	 * same as localhost.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param hostname
	 *            <p>
	 *            The hostname of the target platform on which the job will be
	 *            launched.
	 *            </p>
	 * @return <p>
	 *         True if the hostname is the same as localhost, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean isLocalhost(String hostname) {
		// begin-user-code

		// Local Declarations
		boolean retVal = false;
		String localHostname = null;

		// The simplest names to check are 127.0.0.1 and localhost.localdomain.
		// These names are always the local machine on Unix systems.
		if ("127.0.0.1".equals(hostname)
				|| "localhost.localdomain".equals(hostname)
				|| "localhost".equals(hostname)) {
			retVal = true;
		} else {
			// Get the local hostname by looking up the InetAddress
			try {
				// Get the address of localhost
				InetAddress addr = InetAddress.getLocalHost();
				// Get the hostname
				localHostname = addr.getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			// Compare the names
			if (hostname.equals(localHostname)) {
				retVal = true;
			}
		}

		System.out.println("JobLaunchAction Message: Localhost hostname = "
				+ localHostname);
		System.out.println("JobLaunchAction Message: Target "
				+ "Platform hostname = " + hostname);
		System.out.println("JobLaunchAction Message: Host is"
				+ ((retVal) ? " " : " NOT ") + "localhost.");

		return retVal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation launches the job on the local machine.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void launchLocally() {
		// begin-user-code

		// Local Declarations
		FormStatus launchStatus;
		// String separator = System.getProperty("file.separator");
		// String userHome = System.getProperty("user.home");
		// String localProjectDir = userHome + separator + "ICEFiles" +
		// separator
		// + "default";
		// File workingDirectory = new File(execDictionary.get("workingDir"));

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
		// end-user-code
	}

	protected FormStatus launchStageLocally(String cmd, BufferedWriter stdOut,
			BufferedWriter stdErr) {

		// Local Declarations
		String errMsg = null;
		ProcessBuilder jobBuilder = null;
		InputStream stdOutStream = null, stdErrStream = null;
		int exitValue = -1;
		String os = execDictionary.get("os");
		ArrayList<String> cmdList = new ArrayList<String>();
		File directory = new File(execDictionary.get("workingDir"));

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

		// Create the directory if it doesn't already exist
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// IF YOU ARE GOING TO LAUNCH FROM A SPECIFIED WORKING DIR
		// YOU MUST HAVE THE FILES THERE!!!
		for (String fileString : fileMap.keySet()) {

			// Get the local file to copy and create the File
			// reference to where it should be copied
			File localFile = new File(fileMap.get(fileString));
			File copyToDirFile = new File(directory.getAbsolutePath()
					+ System.getProperty("file.separator") + fileString);
			try {
				Files.copy(Paths.get(localFile.toURI()),
						Paths.get(copyToDirFile.toURI()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
				e.printStackTrace();
			}
			return FormStatus.InfoError;
		}

		// Print the execution command
		System.out.println("JobLaunchAction Message: "
				+ "Launching local command: " + "\"" + cmd + "\"");

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
				exitValue = (isLocal.get()) ? job.exitValue() : remoteJob
						.exitValue();
			} catch (IllegalThreadStateException e) {
				// Complain, but keep watching
				e.printStackTrace();
			}
			// Give it a second
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Complain
				e.printStackTrace();
			}
		}
		System.out
				.println("JobLaunchAction Message: Exit value = " + exitValue);

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
			e.printStackTrace();
			return FormStatus.InfoError;
		}

		return FormStatus.Processing;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation launches the job on a remote machine.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void launchRemotely() {
		// begin-user-code

		IRemoteServices remoteServices = RemoteServices
				.getRemoteServices("org.eclipse.ptp.remote.RemoteTools");
		IRemoteConnection connection = null;
		String remoteDownloadDirectory = execDictionary
				.get("downloadDirectory");
		String separator = System.getProperty("file.separator");
		File localStorageDir = null;
		Date currentDate = new Date();
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		String homeDir = System.getProperty("user.home");
		String localDirectoryPath = "";

		// Create a local directory where created files can be downloaded
		// from the remote host
		localDirectoryPath = projectSpaceDir + separator + "jobs" + separator
				+ "remoteIceLaunch_" + shortDate.format(currentDate);
		localStorageDir = new File(localDirectoryPath);

		// Create the directory if it doesn't already exist
		if (!localStorageDir.exists()) {
			localStorageDir.mkdirs();
		}

		// Place this in the action map so others can reference it later
		execDictionary.put("workingDir", localDirectoryPath);

		// Get the directory where local files should be stored
		IFileStore localDirectory = EFS.getLocalFileSystem().fromLocalFile(
				localStorageDir);
		String launchCMDFileName = "";

		// Search for existing connections and make a new one
		if (remoteServices.canCreateConnections()) {
			try {
				// Get a new connection
				connection = remoteServices.getConnectionManager()
						.newConnection(execDictionary.get("hostname"));
			} catch (RemoteConnectionException e) {
				// Print diagnostic information and fail
				e.printStackTrace();
				status = FormStatus.InfoError;
				return;
			}
		}

		// Block until the Form is submitted
		while (!formSubmitted.get()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// Complain
				e1.printStackTrace();
				return;
			}
			// DEBUG - System.out.println("Form not yet submitted.");
		} // FIXME - Need to be able to cancel!

		// Write the command script that contains all of the commands to launch.
		try {
			launchCMDFileName = writeRemoteCommandFile();
		} catch (Exception e) {
			// Complain
			e.printStackTrace();
			return;
		}

		// Set the hostname of the connection
		connection.setAddress(execDictionary.get("hostname"));
		// Get the username and password from the login component
		DataComponent credentials = (DataComponent) formAtomic.get()
				.getComponent(1);
		connection.setUsername(credentials.retrieveAllEntries().get(0)
				.getValue());
		connection.setPassword(credentials.retrieveAllEntries().get(1)
				.getValue());
		// Try to open the connection and fail if it will not open
		try {
			connection.open(null);
		} catch (RemoteConnectionException e) {
			// Print diagnostic information and fail
			e.printStackTrace();
			status = FormStatus.InfoError;
			return;
		}

		// Do the upload(s) and launch the job if the connection is open
		if (connection.isOpen() && !cancelled.get()) {
			// Diagnostic info
			System.out.println("JobLaunchAction Message:"
					+ " PTP connection established.");
			// Get the remote file manager
			IRemoteFileManager fileManager = remoteServices
					.getFileManager(connection);
			// Get the working directory
			IFileStore fileStore = fileManager.getResource(connection
					.getWorkingDirectory());
			try {
				IFileStore directory = fileStore.getChild(
						workingDirectoryBaseName).mkdir(EFS.SHALLOW, null);
				System.out.println("JobLaunchAction Message: "
						+ "Created directory on remote system, "
						+ directory.getName());

				// Loop over all of the files in the file table and upload them
				for (String shortInputName : fileMap.keySet()) {

					// Check to see if the job should be cancelled.
					if (cancelled.get()) {
						break;
					}

					// If input file uploading is enabled, OR, if input file
					// uploading is disabled BUT this is the launch script, then
					// upload the file
					if (uploadInput
							|| (!uploadInput && shortInputName
									.equals("launchJob.sh"))) {

						// Get a handle where the input file will be stored
						// remotely
						IFileStore remoteFileStore = directory
								.getChild(shortInputName);
						// Get a file store handle to the local copy of the
						// input
						// file
						File localFile = new File(fileMap.get(shortInputName));
						IFileStore localFileStore = EFS.getLocalFileSystem()
								.fromLocalFile(localFile);

						// Copy the local file to the remote file
						localFileStore.copy(remoteFileStore, EFS.NONE, null);
						System.out.println("JobLaunchAction Message: "
								+ "Uploaded file " + shortInputName);
					}
				}

			} catch (CoreException e) {
				// Print diagnostic information and fail
				e.printStackTrace();
				status = FormStatus.InfoError;
				return;
			}

			// Get the file separator on the remote system
			String remoteSeparator = connection
					.getProperty(IRemoteConnection.FILE_SEPARATOR_PROPERTY);
			// Set the new working directory
			String currentWd = connection.getWorkingDirectory();
			connection.setWorkingDirectory(currentWd + remoteSeparator
					+ workingDirectoryBaseName);
			// Dump the new working directory
			System.out.println("JobLaunchActionMessage: "
					+ "PTP working directory set to"
					+ connection.getWorkingDirectory());

			// Create the command to launch based on whether this is a normal
			// machine or Titan.
			String launchCMD = "";
			if (!execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
				launchCMD = "./" + launchCMDFileName;
			} else {
				launchCMD = "qsub" + launchCMDFileName;
			}
			// Create the process builder for the remote job
			IRemoteProcessBuilder processBuilder = remoteServices
					.getProcessBuilder(connection, "sh", launchCMD);
			// Do not redirect the streams
			processBuilder.redirectErrorStream(false);
			try {
				System.out.println("JobLaunchAction Message: "
						+ "Attempting to launch with PTP...");
				System.out.println("JobLaunchAction Message: "
						+ "Command sent to PTP = " + "sh ./"
						+ launchCMDFileName);
				remoteJob = processBuilder
						.start(IRemoteProcessBuilder.FORWARD_X11);
			} catch (IOException e) {
				// Print diagnostic information and fail
				e.printStackTrace();
				status = FormStatus.InfoError;
				return;
			}
			// Log the ouput
			InputStream stdOutStream = remoteJob.getInputStream();
			InputStream stdErrStream = remoteJob.getErrorStream();
			if (logOutput(stdOutStream, stdErrStream).equals(
					FormStatus.InfoError)) {
				// Throw an error if the streaming fails
				status = FormStatus.InfoError;
				return;
			}
			// Monitor the job
			monitorJob();

			// - Download the output if possible and if it wasn't cancelled - //
			// Check to see if the job should be cancelled.
			if (!cancelled.get()) {
				// Get download directory
				String fixedRemoteWD = currentWd + remoteSeparator
						+ workingDirectoryBaseName;
				String remoteDir = (remoteDownloadDirectory == null) ? fixedRemoteWD
						: remoteDownloadDirectory;
				System.out
						.println("JobLaunchAction Message: "
								+ "Downloading remote files to "
								+ localDirectory.getName() + " from "
								+ remoteDir + ".");
				IFileStore downloadFileStore = fileManager
						.getResource(remoteDir);
				try {
					// Get the children
					IFileStore[] remoteStores = downloadFileStore.childStores(
							EFS.NONE, null);
					// Download all of the children
					for (IFileStore remoteFile : remoteStores) {
						// Check to see if the job should be cancelled.
						if (cancelled.get()) {
							break;
						}
						// Get the information about the current child
						IFileInfo fileInfo = remoteFile.fetchInfo();
						if (fileInfo.getLength() < maxFileSize) {
							// Print some debug information about the download
							String msg = "JobLaunchAction Message: "
									+ "Downloading " + fileInfo.getName()
									+ " with length " + fileInfo.getLength()
									+ ".";
							System.out.println(msg);
							stdOut.write(msg + "\n");
							// Get a handle to the local file. Note that it may
							// not
							// exist yet.
							IFileStore childStore = localDirectory
									.getChild(remoteFile.getName());
							// Copy the file from the remote machine to the
							// local
							// machine.
							remoteFile.copy(childStore, EFS.OVERWRITE, null);
						} else {
							long sizeDiff = fileInfo.getLength() - maxFileSize;
							// Print a debug note saying that the file is too
							// big to
							// download.
							String msg = "JobLaunchAction Message: "
									+ "File exceeds download limit. "
									+ "File with size " + fileInfo.getLength()
									+ " is " + sizeDiff + " bytes over the "
									+ maxFileSize + " byte limit.";
							System.out.println(msg);
							stdOut.write(msg + "\n");
						}
						// Flush the messages so that clients can be updated.
						stdOut.flush();
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Set the status
		status = FormStatus.Processed;

		// Close the connection
		connection.close();

		return;
		// end-user-code
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
		String separator = System.getProperty("file.separator");
		String launchFileName = System.getProperty("user.dir") + separator
				+ "launchJob_" + shortDate.format(currentDate);
		File launchFile = new File(launchFileName);
		FileWriter launchFileWriter = new FileWriter(launchFile);
		String shortName = "launchJob.sh";

		// Write the header if the target machine is Titan
		if (execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
			// Get the number of cores
			int numCores = Math.max(1,
					Integer.parseInt(execDictionary.get("numProcs")));
			// Calculate the number of nodes.
			int numNodes = Math.min(numCores / 16 + 1, 18688);
			// Write the file
			launchFileWriter.write("#!/bin/bash\n");
			launchFileWriter.write("# Begin PBS directives\n");
			launchFileWriter.write("#PBS -A "
					+ execDictionary.get("accountCode") + "\n");
			launchFileWriter
					.write("#PBS -N " + workingDirectoryBaseName + "\n");
			launchFileWriter.write("#PBS -j oe\n");
			launchFileWriter.write("#PBS -l walltime=1:00:00,nodes=" + numNodes
					+ "\n");
			launchFileWriter
					.write("# End PBS directives and begin shell commands\n");
		}

		// Write each command into the file
		for (String singleCMD : splitCMD) {
			launchFileWriter.write(singleCMD + "\n");
		}

		// Close the writer
		launchFileWriter.close();

		// Put the file into the file map
		fileMap.put(shortName, launchFileName);

		return shortName;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides Action.submitForm to add the Form to an Atomic
	 * container instead of the default Form class variable.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param form
	 *            <p>
	 *            The form being submitted.
	 *            </p>
	 * @return <p>
	 *         The status of the submission.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public FormStatus submitForm(Form form) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#execute(Dictionary<String,String> dictionary)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus execute(Dictionary<String, String> dictionary) {
		// begin-user-code

		// Local Declarations
		DataComponent loginInfoComp;
		Entry usernameEntry;
		Thread processThread = new Thread(this);

		// Determine if this is a local launch or not
		String hostname = dictionary.get("hostname");
		isLocal.set(isLocalhost(hostname));

		// Set the default value of the status to processing
		status = FormStatus.Processing;

		// Set the dictionary reference
		execDictionary = dictionary;

		// Setup for remote launch if needed
		if (!isLocal.get()) {
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
			formSubmitted = new AtomicBoolean();
			formSubmitted.set(false);
			formAtomic = new AtomicReference<LoginInfoForm>();

			// Set the status
			status = FormStatus.NeedsInfo;
		}

		// Start the thread
		processThread.start();

		return status;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#cancel()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancel() {
		// begin-user-code

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
		// end-user-code
	}

	private void setWorkingDirectoryName() {

		// Local Declarations
		Date currentDate = new Date();
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		String separator = System.getProperty("file.separator");
		String homeDir = System.getProperty("user.home");
		String launchDir = "";

		// Set the base name of the working directory.
		workingDirectoryBaseName = "iceLaunch_" + shortDate.format(currentDate);

		// Set the name of the working directory properly if it is a local
		// launch
		if (isLocal.get()) {
			launchDir = projectSpaceDir + separator + "jobs" + separator
					+ workingDirectoryBaseName;
		} else if (execDictionary.get("hostname").equals("titan.ccs.ornl.gov")) {
			// Get the project directory
			String projId = execDictionary.get("accountCode");
			// Set the directory to the $PROJ_WORK/projId directory on Titan
			launchDir = "$PROJWORK" + separator + projId + separator
					+ workingDirectoryBaseName;
		} else {
			// Otherwise just leave it in the home directory
			launchDir = workingDirectoryBaseName;
		}

		// Put the directory in the dictionary
		execDictionary.put("workingDir", launchDir);

		// Dump some debug info
		System.out.println("JobLaunchAction Message: Working directory = "
				+ execDictionary.get("workingDir"));

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Runnable#run()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void run() {
		// begin-user-code

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
		if (executable == null || (uploadInput && inputFile == null)
				|| stdOutFileName == null || stdErrFileName == null
				|| hostname == null) {
			status = FormStatus.InfoError;
			return;
		}

		// Get the flag from the dictionary that dictates whether or not the
		// input file name should be appended to the executable command. It is
		// optional, so treat it specially.
		if (execDictionary.get("noAppendInput") != null
				&& ("true").equals(execDictionary.get(("noAppendInput")))) {
			appendInput = false;
		}

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
			e.printStackTrace();
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
			e.printStackTrace();
			status = FormStatus.InfoError;
			return;
		}

		return;
		// end-user-code
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
			e.printStackTrace();
		}

		// Add the date and time
		header = "# Job launch date: ";
		header += new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar
				.getInstance().getTime()) + "\n";
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
		header += "# Working directory: " + execDictionary.get("workingDir")
				+ "\n";
		// Add an empty line
		header += "\n";

		return header;
	}

}
