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

import java.util.ArrayList;

/**
 * 
 * This class configures particular commands to be executed. It contains all of
 * the relevant information about configuring the command. The user should
 * specify the details of the command in the declaration of a Command instance
 * by providing a CommandConfiguration.
 * 
 * @author Joe Osborn
 *
 */
public class CommandConfiguration {

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
	 * Constructor which initializes several of the member variables See member
	 * variables in class CommandConfiguration for descriptions of variables
	 * 
	 * @param _commandId
	 * @param exec
	 * @param input
	 * @param errFile
	 * @param outFile
	 * @param procs
	 * @param installDir
	 * @param operatingSystem
	 * @param workingDir
	 * @param _appendInput
	 */
	public CommandConfiguration(int _commandId, String exec, String input, String errFile, String outFile, String procs,
			String installDir, String operatingSystem, String workingDir, boolean _appendInput) {

		commandId = _commandId;
		executable = exec;
		inputFile = input;
		stdErrFileName = errFile;
		stdOutFileName = outFile;
		numProcs = procs;
		installDirectory = installDir;
		os = operatingSystem;
		workingDirectory = workingDir;
		appendInput = _appendInput;
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
	 * Don't want a setter function for FullCommand since this is determined in
	 * {@link org.eclipse.ice.commands.LocalCommand#fixExecutableName()}
	 */
	public String getFullCommand() {
		return fullCommand;
	}
}
