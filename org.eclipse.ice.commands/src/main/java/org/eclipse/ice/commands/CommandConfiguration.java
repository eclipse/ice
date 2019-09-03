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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class configures particular commands to be executed. It contains all of
 * the relevant information about configuring the command. The user should
 * specify the details of the command in the declaration of a Command instance
 * by providing a CommandConfiguration. The methods in this class actually
 * prepare the configuration of the executable and output file logs.
 * 
 * @author Joe Osborn
 *
 */
public class CommandConfiguration {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(CommandConfiguration.class);

	/**
	 * An integer ID to associate with a job
	 */
	protected int commandId;

	/**
	 * The file name of the executable that is to be processed or run
	 */
	protected String executable;

	/**
	 * The input file that the executable needs or takes as an argument in its
	 * processing
	 */
	protected String inputFile;

	/**
	 * The name of the file that will contain the output of the job
	 */
	protected String stdOutFileName;

	/**
	 * The name of the file that will contain any error messages the job might give,
	 * if it fails for some reason
	 */
	protected String stdErrFileName;

	/**
	 * The number of processes for the job to run
	 */
	protected String numProcs;

	/**
	 * The installation directory of some libraries the job may need, if it needs
	 * access to them
	 */
	protected String installDirectory;

	/**
	 * The operating system on which the job will run
	 */
	protected String os;

	/**
	 * The working directory for the job to be executed in, and thus where e.g. the
	 * output files will be located
	 */
	protected String workingDirectory;

	/**
	 * The hostname that the command will be executed on. This is the same as the
	 * hostname in {@link org.eclipse.ice.commands.Connection} and is just used for
	 * output file purposes.
	 */
	protected String hostname;

	/**
	 * Output streams for the job
	 */
	protected BufferedWriter stdOut = null, stdErr = null;

	/**
	 * A flag to mark whether or not the input file name should be appended to the
	 * executable command. Marked as true by default so that the user (by default)
	 * specifies the input file name.
	 */
	protected boolean appendInput = true;

	/**
	 * The full command string of all stages that will be executed.
	 */
	protected String fullCommand = "";

	/**
	 * The set of commands in the fullCommand string split into stages. Each command
	 * is then executed separately. If the command is single stage, then
	 * splitCommand is identical to fullCommand.
	 */
	protected ArrayList<String> splitCommand = new ArrayList<String>();

	/**
	 * Default constructor
	 */
	public CommandConfiguration() {
		// Assume some default variables
		commandId = -999;
	}




	/**
	 * This function creates the output files that contain the logging/error
	 * information from the job. It makes the headers for the output files and then
	 * creates writers for the files.
	 */
	protected void createOutputFiles() {
		// Create unique headers for the output files which include useful information
		String stdOutHeader = createOutputHeader("standard output");
		String stdErrHeader = createOutputHeader("standard error");

		// Set the output and error buffer writers
		stdOut = getBufferedWriter(stdOutFileName);
		stdErr = getBufferedWriter(stdErrFileName);

		// Now write them out
		try {
			stdOut.write(stdOutHeader);
			stdOut.write("# Executable to be run is: " + fullCommand + "\n");
			stdOut.close();
			stdErr.write(stdErrHeader);
			stdErr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		header += "# Target host: " + hostname + "\n";

		// Add the execution command
		header += "# Command Executed: " + fullCommand + "\n";

		// Add the input file name
		header += "# Input file: " + inputFile + "\n";

		// Add an empty line
		header += "\n";

		return header;
	}

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
	public String getExecutableName() {

		// Get the information from the executable dictionary
		int numProcsInt = Math.max(1, Integer.parseInt(numProcs));
		String fixedExecutableName = executable;
		String separator = "/";

		// If the input file should be appended, append it
		if (appendInput)
			fixedExecutableName += " " + inputFile;

		fullCommand = fixedExecutableName;

		// Determine the proper separator
		if (installDirectory != null && installDirectory.contains(":\\"))
			separator = "\\";

		// Append a final separator to the install directory if required
		if (installDirectory != null && !installDirectory.endsWith(separator))
			installDirectory = installDirectory + separator;

		// Search for and replace the ${inputFile} to properly configure the input file
		if (fixedExecutableName.contains("${inputFile}") && !appendInput)
			fixedExecutableName = fixedExecutableName.replace("${inputFile}", inputFile);

		if (fixedExecutableName.contains("${installDir}") && installDirectory != null)
			fixedExecutableName = fixedExecutableName.replace("${installDir}", installDirectory);

		// If MPI should be used if there are multiple cores, add it to the executable
		if (numProcsInt > 1)
			fixedExecutableName = "mpirun -np " + numProcs + " " + fixedExecutableName;

		// Clean up any unnecessary white space
		fixedExecutableName = fixedExecutableName.trim();

		// Split the full command into its stages, if there are any.
		if (!fixedExecutableName.contains(";"))
			splitCommand.add(fixedExecutableName);
		// Otherwise split the full command and put it into
		// CommandConfiguration.splitCommand
		else {
			for (String stage : fixedExecutableName.split(";")) {
				splitCommand.add(stage.trim());
			}
		}

		// Print launch stages so that user can confirm
		for (int i = 0; i < splitCommand.size(); i++) {
			String cmd = splitCommand.get(i);
			logger.info("LocalCommand Message: Launch stage " + i + " = " + cmd);
		}

		return fixedExecutableName;

	}

	/**
	 * Make some getter and setter functions to access the CommandConfigurations
	 * protected variables
	 */

	public void setCommandId(int _commandId) {
		commandId = _commandId;
		return;
	}

	public void setExecutable(String exec) {
		executable = exec;
		return;
	}

	public void setInputFile(String input) {
		inputFile = input;
		return;
	}

	public void setErrFileName(String errFile) {
		stdErrFileName = errFile;
		return;
	}

	public void setOutFileName(String outFile) {
		stdOutFileName = outFile;
		return;
	}

	public void setNumProcs(String procs) {
		numProcs = procs;
		return;
	}

	public void setInstallDirectory(String installDir) {
		installDirectory = installDir;
		return;
	}

	public void setOS(String operatingSys) {
		os = operatingSys;
		return;
	}

	public void setWorkingDirectory(String workingDir) {
		workingDirectory = workingDir;
		return;
	}

	public void setAppendInput(boolean _appendInput) {
		appendInput = _appendInput;
		return;
	}
	public void setHostname(String host) {
		hostname = host;
		return;
	}

	public void setStdErr(BufferedWriter writer) {
		stdErr = writer;
		return;
	}
	public void setStdOut(BufferedWriter writer) {
		stdOut = writer;
		return;
	}
	public BufferedWriter getStdOut() {
		return stdOut;
	}
	public BufferedWriter getStdErr() {
		return stdErr;
	}
	public ArrayList<String> getSplitCommand(){
		return splitCommand;
	}
	
	public String getExecutable() {
		return executable;
	}

	public String getInputFile() {
		return inputFile;
	}

	public String getErrFileName() {
		return stdErrFileName;
	}

	public String getOutFileName() {
		return stdOutFileName;
	}

	public String getNumProcs() {
		return numProcs;
	}

	public String getInstallDirectory() {
		return installDirectory;
	}

	public String getOS() {
		return os;
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public boolean getAppendInput() {
		return appendInput;
	}

	public int getCommandId() {
		return commandId;
	}

	/**
	 * We make the setter for full command protected so that it can only be 
	 * accessed within the package and not by (e.g.) the user
	 */
	protected void setFullCommand(String command) {
		fullCommand = command;
		return;
	}
	public String getFullCommand() {
		return fullCommand;
	}
}