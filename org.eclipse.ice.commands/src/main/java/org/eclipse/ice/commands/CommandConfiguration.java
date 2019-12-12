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
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
	private static final Logger logger = LoggerFactory.getLogger(CommandConfiguration.class);

	/**
	 * An integer ID to associate with a job
	 */
	private int commandId;

	/**
	 * A string that lets the user optionally set an interpreter name to be attached
	 * before the executable name. For example, if the executable is script.py,
	 * interpreter would allow the user to put python2 or python3 in front of
	 * script.py to run it in either version of python. Set to null by default,
	 * which means by default the interpreter is bash
	 */
	private String interpreter = null;

	/**
	 * The file name of the executable that is to be processed or run
	 */
	private String executable;

	/**
	 * The input file(s) that the executable needs or takes as an argument in its
	 * processing. The first string is the name of the file as in the executable
	 * string, while the second string is the path to that file
	 */
	private HashMap<String, String> inputFiles = new HashMap<String, String>();

	/**
	 * This is a list of arguments that the user might want to append to the
	 * executable name that are _not_ input files. These will not be explicitly
	 * checked by the file handler for whether or not they exist, as they are
	 * presumed to be flags/arguments for the job to run.
	 */
	private ArrayList<String> argumentList = new ArrayList<String>();
	/**
	 * The name of the file that will contain the output of the job
	 */
	private String stdOutFileName;

	/**
	 * The name of the file that will contain any error messages the job might give,
	 * if it fails for some reason
	 */
	private String stdErrFileName;

	/**
	 * The number of processes for the job to run
	 */
	private String numProcs;

	/**
	 * The installation directory of some libraries the job may need, if it needs
	 * access to them
	 */
	private String installDirectory;

	/**
	 * The working directory for the job to be executed in, and thus where e.g. the
	 * output files will be located
	 */
	private String workingDirectory;

	/**
	 * A string which contains the directory in which to execute the job on the
	 * remote system, if necessary
	 */
	private String remoteWorkingDirectory = "";

	/**
	 * The operating system that the command will be run on. Set by default to the
	 * local OS
	 */
	private String os = System.getProperty("os.name");

	/**
	 * The hostname that the command will be executed on. This is the same as the
	 * hostname in {@link org.eclipse.ice.commands.Connection} and is only used here
	 * for logging purposes in the output files.
	 */
	private String hostname;

	/**
	 * Output streams for the job
	 */
	private BufferedWriter stdOut = null, stdErr = null;

	/**
	 * This is a string that contains all of the output of the job. This is the same
	 * text that gets written out to
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOut}, just in string
	 * form. It is used for easy access to the output at the end of the job
	 * processing, if desired.
	 */
	private String stdOutput = "";

	/**
	 * This is a string that contains all of the error output from the job. This is
	 * the same text that gets written out to
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdErr}, just in string
	 * form for easy access and for the same purposes as stdOutput above.
	 */
	private String errMsg = "";

	/**
	 * A flag to mark whether or not the input file name should be appended to the
	 * executable command. Marked as true by default so that the user (by default)
	 * specifies the input file name.
	 */
	private boolean appendInput = true;

	/**
	 * The full command string of all stages that will be executed.
	 */
	private String fullCommand = "";

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
			stdOut.close();
			stdErr.write(stdErrHeader);
			stdErr.close();
		} catch (IOException e) {
			logger.error("Could not write header logs to output files!", e);
		}

		return;
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
				logger.error("Could not retrieve a file writer for buffer writing", e);
				return null;
			}
			bufferedWriter = new BufferedWriter(writer);
			return bufferedWriter;
		}

		// If filename is null, then we need to return null
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
			logger.error("Could not identify local host name in output header creation.", e);
			// To handle this exception, we can just set the local host name since it's only
			// purpose is for logging in the header file
			localHostname = "UNKNOWN";
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
		header += "# Input files: " + getInputFiles() + "\n";

		// Add the install directory name
		header += "# Install directory: " + installDirectory + "\n";

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

		// Make the full executable to be processed
		String fixedExecutableName = "";

		// Check to see if this is a python script or a bash script
		if (interpreter != null) {
			// If it is a python script, add "python" in front
			fixedExecutableName += interpreter + " ";
		}

		fixedExecutableName += executable;
		String separator = "/";

		// Add the arguments to the executable name
		for (String arg : argumentList) {
			fixedExecutableName += " " + arg;
		}

		// If the input files should be appended, append it
		if (appendInput)
			fixedExecutableName += " " + getInputFiles();

		fullCommand = fixedExecutableName;

		// Determine the proper separator
		if (installDirectory != null && installDirectory.contains(":\\"))
			separator = "\\";

		// Append a final separator to the install directory if required
		if (installDirectory != null && !installDirectory.endsWith(separator))
			installDirectory = installDirectory + separator;

		// Search for and replace the ${inputFile} to properly configure the input file
		for (Map.Entry<String, String> entry : inputFiles.entrySet()) {
			if (fixedExecutableName.contains("${" + entry.getKey() + "}") && !appendInput)
				fixedExecutableName = fixedExecutableName.replace("${" + entry.getKey() + "}", entry.getValue());
		}
		if (fixedExecutableName.contains("${installDir}") && installDirectory != null)
			fixedExecutableName = fixedExecutableName.replace("${installDir}", installDirectory);

		// If MPI should be used if there are multiple cores, add it to the executable
		if (numProcsInt > 1)
			fixedExecutableName = "mpirun -np " + numProcs + " " + fixedExecutableName;

		// Clean up any unnecessary white space
		fixedExecutableName = fixedExecutableName.trim();

		// Split the full command into its stages, if there are any.
		if (!fixedExecutableName.contains(";"))
			// Add a ; to the end so that the command is properly closed
			splitCommand.add(fixedExecutableName + ";");
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
			logger.info("CommandConfiguration Message: Launch stage " + i + " = " + cmd);
		}

		return fixedExecutableName;

	}

	/**
	 * Make some getter and setter functions to access the CommandConfigurations
	 * protected variables
	 */

	/**
	 * Setter for CommandId, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#commandId}
	 * 
	 * @param _commandId - integer of command ID to be run
	 */
	public void setCommandId(int commandId) {
		this.commandId = commandId;
		return;
	}

	/**
	 * Getter for CommandId, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#commandId}
	 * 
	 * @return commandId - CommandConfiguration ID
	 */
	public int getCommandId() {
		return commandId;
	}

	/**
	 * Setter for executable, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#executable}
	 * 
	 * @param executable - executable to be run
	 */
	public void setExecutable(String executable) {
		this.executable = executable;
		return;
	}

	/**
	 * Getter for executable, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#executable}
	 * 
	 * @return executable - executable to be run
	 */
	public String getExecutable() {
		return executable;
	}

	/**
	 * Adder for inputFile, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#inputFile}
	 * 
	 * @param name - name of inputFile to add
	 * @param path - path to inputFile relative to workingDirectory
	 */
	public void addInputFile(String name, String path) {
		inputFiles.put(name, path);
		return;
	}

	/**
	 * Getter for a concatenated string of inputFiles, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#inputFiles}
	 * 
	 * @return String - a string of all of the inputFiles concatenated
	 */
	public String getInputFiles() {
		String files = "";
		for (Map.Entry<String, String> entry : inputFiles.entrySet()) {
			files += entry.getValue() + " ";
		}
		return files;
	}

	/**
	 * Getter for the inputFile hashmap itself, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#inputFiles}
	 * 
	 * @return inputFiles - HashMap with input file list
	 */
	public HashMap<String, String> getInputFileList() {
		return inputFiles;
	}

	/**
	 * Setter for stdErrFileName, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdErrFileName}
	 * 
	 * @param stdErrFileName - name for StdErr file
	 */
	public void setErrFileName(String stdErrFileName) {
		this.stdErrFileName = stdErrFileName;
		return;
	}

	/**
	 * Getter for stdErrFileName, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdErrFileName}
	 * 
	 * @return stdErrFileName - name of StdErr file
	 */
	public String getErrFileName() {
		return stdErrFileName;
	}

	/**
	 * Setter for stdOutFileName, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOutFileName}
	 * 
	 * @param stdOutFileName - name of StdOut file
	 */
	public void setOutFileName(String stdOutFileName) {
		this.stdOutFileName = stdOutFileName;
		return;
	}

	/**
	 * Getter for stdOutFileName, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOutFileName}
	 * 
	 * @return stdOutFileName - name of StdOut file
	 */
	public String getOutFileName() {
		return stdOutFileName;
	}

	/**
	 * Setter for numProcs, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#numProcs}
	 * 
	 * @param numProcs - number of processes
	 */
	public void setNumProcs(String numProcs) {
		this.numProcs = numProcs;
		return;
	}

	/**
	 * Getter for numProcs, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#numProcs}
	 * 
	 * @return numProcs - number of Processes
	 */
	public String getNumProcs() {
		return numProcs;
	}

	/**
	 * Setter for installDirectory, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#installDirectory}
	 * 
	 * @param installDir - String corresponding to path of install directory
	 */
	public void setInstallDirectory(String installDirectory) {
		this.installDirectory = installDirectory;
		return;
	}

	/**
	 * Getter for installDirectory, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#installDirectory}
	 * 
	 * @return installDirectory - path of install directory
	 */
	public String getInstallDirectory() {
		return installDirectory;
	}

	/**
	 * Getter for os, see {@link org.eclipse.ice.commands.CommandConfiguration#os}
	 * Note that this is set to the default of the local OS
	 * 
	 * @return os - operating system that Commands is running on
	 */
	public String getOS() {
		return os;
	}

	/**
	 * Setter for operating system, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#os}
	 * 
	 * @param os - operating system that Commands is running on
	 */
	public void setOS(String os) {
		this.os = os;
	}

	/**
	 * Setter for workingDirectory, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#workingDirectory}
	 * 
	 * @param workingDirectory - working directory containing files/scripts
	 */
	public void setWorkingDirectory(String workingDirectory) {
		// Check to see if the directory ends with a separator
		String separator = "/";
		// If the string has windows separator, then change the separator
		if (workingDirectory.contains("\\"))
			separator = "\\";

		if (!workingDirectory.endsWith(separator))
			workingDirectory += separator;
		this.workingDirectory = workingDirectory;
		return;
	}

	/**
	 * Getter for workingDirectory, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#workingDirectory}
	 * 
	 * @return workingDirectory - working directory containing files/scripts
	 */
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * Setter for appendInput, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#appendInput}
	 * 
	 * @param appendInput - boolean of whether or not to append input file names to
	 *                    executable
	 */
	public void setAppendInput(boolean appendInput) {
		this.appendInput = appendInput;
		return;
	}

	/**
	 * Getter for appendInput, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#appendInput}
	 * 
	 * @return appendInput - boolean of whether or not to append input file names to
	 *         executable
	 */
	public boolean getAppendInput() {
		return appendInput;
	}

	/**
	 * Setter for hostname, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#hostname}. Make the
	 * setter protected with the intent that only ConnectionConfiguration can modify
	 * this member variable.
	 * 
	 * @param hostname - hostname for command to run on
	 */
	protected void setHostname(String hostname) {
		this.hostname = hostname;
		return;
	}

	/**
	 * Getter for hostname, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#hostname}.
	 * 
	 * @return hostname - hostname for command to run on
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Setter for stdErr, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdErr}
	 * 
	 * @param stdErr - BufferedWriter to write error output
	 */
	public void setStdErr(BufferedWriter stdErr) {
		this.stdErr = stdErr;
		return;
	}

	/**
	 * Getter for stdErr, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdErr}
	 * 
	 * @return stdErr - Buffered writer to write error output
	 */
	public BufferedWriter getStdErr() {
		return stdErr;
	}

	/**
	 * Setter for stdOut, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOut}
	 * 
	 * @param stdOut - Buffered writer to write standard output
	 */
	public void setStdOut(BufferedWriter stdOut) {
		this.stdOut = stdOut;
		return;
	}

	/**
	 * Getter for stdOut, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOut}
	 * 
	 * @return stdOut - Buffered writer to write standard output
	 */
	public BufferedWriter getStdOut() {
		return stdOut;
	}

	/**
	 * Setter for stdOutput, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOutput}
	 * 
	 * @return stdOutput - String for StdOutput name
	 */
	public void setStdOutputString(String stdOutput) {
		this.stdOutput = stdOutput;
	}

	/**
	 * This function adds the String string to the String
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOutput}
	 * 
	 * @param string - String to concatenate to the stdOutput string
	 */
	public void addToStdOutputString(String string) {
		stdOutput += "\n" + string;
	}

	/**
	 * Getter for stdOutput, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#stdOutput}
	 * 
	 * @return stdOutput - String of all stdOutput, separated by '\n'
	 */
	public String getStdOutputString() {
		return stdOutput;
	}

	/**
	 * Getter for splitCommand, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#splitCommand}. No setter
	 * since splitCommand is determined by
	 * {@link org.eclipse.ice.commands.CommandConfiguration#getExecutableName()}
	 * 
	 * @return splitCommand - ArrayList<String> of split commands
	 */
	public ArrayList<String> getSplitCommand() {
		return splitCommand;
	}

	/**
	 * Setter for fullCommand, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#fullCommand}. We make
	 * the setter for full command protected so that it can only be accessed within
	 * the package and not by (e.g.) the user
	 * 
	 * @param fullCommand - String of the full command to be executed
	 */
	protected void setFullCommand(String fullCommand) {
		this.fullCommand = fullCommand;
	}

	/**
	 * Getter for fullCommand, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#fullCommand}
	 * 
	 * @return fullCommand - String of the full command to be executed
	 */
	public String getFullCommand() {
		return fullCommand;
	}

	/**
	 * Add to error message string, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#errMsg}
	 * 
	 * @param errMsg - String to add to the error message string
	 */
	public void addToErrString(String errMsg) {
		this.errMsg += errMsg;
	}

	/**
	 * Setter for error message string, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#errMsg}
	 * 
	 * @param errMsg - String to set the error message string
	 */
	public void setErrString(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * Getter for error message string, see
	 * {@link org.eclipse.ice.commands.CommandConfiguration#errMsg}
	 * 
	 * @return - String of error message
	 */
	public String getErrString() {
		return errMsg;
	}

	/**
	 * Setter for the remote working directory
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#workingDirectory}
	 * 
	 * @param remoteWorkingDirectory - path for remote working directory
	 */
	public void setRemoteWorkingDirectory(String remoteWorkingDirectory) {
		this.remoteWorkingDirectory = remoteWorkingDirectory;
	}

	/**
	 * Getter for the remote working directory
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#workingDirectory}
	 * 
	 * @return - path for the remote working directory
	 */
	public String getRemoteWorkingDirectory() {
		return remoteWorkingDirectory;
	}

	/**
	 * Setter for interpreter
	 * {@link org.eclipse.ice.commands.CommandConfiguration#interpreter}
	 * 
	 * @param interpreter - string of the interpreter to be used for the command
	 */
	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	/**
	 * Getter for interpreter
	 * {@link org.eclipse.ice.commands.CommandConfiguration#interpreter}
	 * 
	 * @return - the interpreter to be used for the command
	 */
	public String getInterpreter() {
		return interpreter;
	}

	/**
	 * Getter for argument list
	 * {@link org.eclipse.ice.commands.CommandConfiguration#argumentList}
	 *
	 * @return - ArrayList<String> of the argument list
	 */
	public ArrayList<String> getArgumentList() {
		return argumentList;
	}

	/**
	 * Function that adds an argument to the argument list
	 * 
	 * @param argument - argument to be added 
	 */
	public void addArgument(String argument) {
		argumentList.add(argument);
	}

}
