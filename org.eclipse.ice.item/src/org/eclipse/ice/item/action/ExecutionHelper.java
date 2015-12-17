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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ExecutionHelper is a utility class that provides common preprocessing and
 * postprocessing tasks and data for local or remote job executions.
 * 
 * @author Alex McCaskey
 *
 */
public class ExecutionHelper {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExecutionHelper.class);

	/**
	 * Reference to the map of input parameters for the execution.
	 */
	private Dictionary<String, String> execDictionary;

	/**
	 * Flag to indicate whether the execution this object is assisting is local
	 * or remote.
	 */
	private boolean isLocal = true;

	/**
	 * Reference to the list of commands to be executed.
	 */
	private ArrayList<String> splitCMD;

	/**
	 * Reference to the files required by the execution this object is assisting
	 * with.
	 */
	private Hashtable<String, String> fileMap;

	/**
	 * The constructor.
	 * 
	 * @param map
	 *            The map of input parameters.
	 */
	public ExecutionHelper(Dictionary<String, String> map) {
		execDictionary = map;
		fileMap = new Hashtable<String, String>();
		splitCMD = new ArrayList<String>();
	}

	/**
	 * Set whether this ExecutionHelper is assisting with an execution that is
	 * remote or local.
	 * 
	 * @param local
	 *            True if local execution, false otherwise
	 */
	public void setIsLocal(boolean local) {
		isLocal = local;
	}

	/**
	 * This private method returns a configured BufferedWriter for the provided
	 * file name.
	 * 
	 * @param filename
	 * @return
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
	 * This operation returns a BufferedWriter for standard output.
	 * 
	 * @return
	 */
	public BufferedWriter getOutputBufferedWriter() {
		return getBufferedWriter(execDictionary.get("stdOutFileName"));
	}

	/**
	 * This operation returns a BufferedWriter for standard error.
	 * 
	 * @return
	 */
	public BufferedWriter getErrorBufferedWriter() {
		return getBufferedWriter(execDictionary.get("stdErrFileName"));
	}

	/**
	 * Return the IProject instance corresponding to the projectSpaceDir key in
	 * the provided input data map.
	 * 
	 * @return
	 */
	public IProject getProject() {
		return ResourcesPlugin.getWorkspace().getRoot()
				.getProject(new File(execDictionary.get("projectSpaceDir")).getName());
	}

	/**
	 * Return the IFolder instance corresponding to the localJobLaunchDirectory
	 * key in the input data map.
	 * 
	 * @return
	 */
	public IFolder getLocalLaunchFolder() {
		return getProject().getFolder("jobs").getFolder(execDictionary.get("localJobLaunchDirectory"));
	}

	/**
	 * This operations checks that the provided input data map is valid for
	 * local or remote launches.
	 * 
	 * @return
	 */
	public boolean isDataValid() {

		// Get the required input parameters for 
		// both local and remote executions. 
		String executable = execDictionary.get("executable");
		String inputFile = execDictionary.get("inputFile");
		String stdOutFileName = execDictionary.get("stdOutFileName");
		String stdErrFileName = execDictionary.get("stdErrFileName");
		String projectSpaceDir = execDictionary.get("projectSpaceDir");
		String localDir = execDictionary.get("localJobLaunchDirectory");
		String installDir = execDictionary.get("installDir");
		String os = execDictionary.get("os");
		String nProcs = execDictionary.get("numProcs");
		String nTBB = execDictionary.get("numTBBThreads");

		// Make sure these are all valid.
		if (executable == null || inputFile == null || stdOutFileName == null || stdErrFileName == null
				|| projectSpaceDir == null || localDir == null || os == null || installDir == null 
				|| nProcs == null || nTBB == null) {
			logger.error("ExecutionHelper Error - invalid local data map.");
			return false;
		}
		
		// If remote, make sure we have the hostname
		if (!isLocal) {
			String host = execDictionary.get("hostname");
			if (host == null) {
				return false;
			}
		}
		
		// Return that this is valid data. 
		return true;
	}

	/**
	 * This operation returns the value of the parameter for the provided key,
	 * or null if the key is invalid.
	 * 
	 * @param param
	 * @return
	 */
	public String getParameter(String param) {
		return execDictionary.get(param);
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
	public String createOutputHeader(String logName, String fullCMD) {

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
		header += "# Target host: localhost \n";
		// Add the execution command
		header += "# Command Executed: " + fullCMD.replace("\n", ";") + "\n";
		// Add the working directory
		header += "# Working directory: " + execDictionary.get("localJobLaunchDirectory") + "\n";
		// Add an empty line
		header += "\n";

		return header;
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
	public String fixExecutableName() {

		// Local Declarations
		int numProcs = Math.max(1, Integer.parseInt(execDictionary.get("numProcs")));
		int numTBBThreads = Math.max(1, Integer.parseInt(execDictionary.get("numTBBThreads")));
		String fixedExecutableName = execDictionary.get("executable");
		String installDir = execDictionary.get("installDir");
		// String workingDir = execDictionary.get("workingDir");
		String separator = "/";
		String shortInputName = null;

		// Print some debug information
		logger.info("Execution Helper Message: Raw executable command = " + fixedExecutableName);

		fixedExecutableName += " ${inputFile}";

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
		// // Fix the working directory by replacing the ${workingDir} flags in
		// the
		// // executable string.
		// if (fixedExecutableName.contains("${workingDir}") && workingDir !=
		// null) {
		// fixedExecutableName = fixedExecutableName.replace("${workingDir}",
		// workingDir);
		// }
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
			logger.info("Execution Helper Message: Launch stage " + i + " = " + cmd);
		}

		return fixedExecutableName;
	}

	/**
	 * Return the list of commands to execute.
	 * 
	 * @return
	 */
	public ArrayList<String> getSplitCommand() {
		return splitCMD;
	}

	/**
	 * Return the table of files that are required for this execution.
	 * 
	 * @return
	 */
	public Hashtable<String, String> getInputFileMap() {
		return fileMap;
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
	public String writeRemoteCommandFile() throws IOException {

		// Local Declarations
		Date currentDate = new Date();
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		IFolder jobFolder = getLocalLaunchFolder();
		IFile launchFile = jobFolder.getFile("launchJob_" + shortDate.format(currentDate) + ".sh");

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
						+ execDictionary.get("accountCode") + "\n" + "#PBS -N " + jobFolder.getName() + "\n"
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

}
