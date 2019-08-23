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

import java.io.IOException;

/**
 * This class inherits from Command and gives available functionality for local commands.
 * The processing and running of the LocalCommand is performed here.
 * @author Joe Osborn
 *
 */
public class LocalCommand extends Command{

	

	/**
	 * Constructor which specifies a particular command configuration. The command configuration
	 * should contain all of the necessary details for a particular command to run. See in particular
	 * the function {@link org.eclipse.ice.commands.LocalCommand#setConfiguration(CommandConfiguration)}
	 * for details about what details are required.
	 * @param _configuration
	 */
	public LocalCommand(CommandConfiguration _configuration) {
		status = CommandStatus.LAUNCHING;
		status = setConfiguration(_configuration);
		
	}
	
	
	/**
	 * Method that overrides {@link org.eclipse.ice.commands.Command#execute()} and actually implements
	 * the particular LocalCommand to be executed.
	 */
	@Override
	public CommandStatus execute() {
		
		// Check that the status of the job is good after setting up the configuration
		// in the constructor. If not, exit 
		// See CheckStatus function in {@link org.eclipse.ice.commands.Command}
		checkStatus(status);
			
		// Now that all of the prerequisites have been set, start the job running
		status = run();
		
		return status;
	}
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#setConfiguration(CommandConfiguration)}
	 */
	@Override
	protected CommandStatus setConfiguration(CommandConfiguration config) {
		
		// Set the configuration for the command
		configuration = config;
		
		// Now extract the information from the command dictionary
		String executable = null, inputFile = null;
		String stdOutFileName = null, stdErrFileName = null;
		String stdOutHeader = null, stdErrHeader = null;
		String numProcs = null, installDir = null, os = null;
		String directory = null;

		// Make sure the dictionary was actually set
		if (configuration.execDictionary != null) {
			
			// Make sure the dictionary contains the keys we need
			executable = configuration.execDictionary.get("executable");
			inputFile = configuration.execDictionary.get("inputFile");
			stdOutFileName = configuration.execDictionary.get("stdOutFileName");
			stdErrFileName = configuration.execDictionary.get("stdErrFileName");
			numProcs = configuration.execDictionary.get("numProcs");
			installDir = configuration.execDictionary.get("installDir");
			os = configuration.execDictionary.get("os");
			directory = configuration.execDictionary.get("workingDirectory");
		}
		else
			return CommandStatus.INFOERROR;
		
		// Check the info and return failure if something was not set correctly
		if (executable == null || inputFile == null || stdOutFileName == null
					|| stdErrFileName == null || numProcs == null || os == null
					|| directory == null) 
				return CommandStatus.INFOERROR;
				
		
		
		// If the input file name should not be appended to the executable, set it
		if ( configuration.execDictionary.get("noAppendInput") != null 
				&& configuration.execDictionary.get("noAppendInput") == "true")
			configuration.appendInput = false;
		
		// Set the command to actually run and execute
		configuration.fullCommand = fixExecutableName();
		
		//Set the output and error buffer writers
		stdOutFileName = configuration.execDictionary.get("stdOutFileName");
		stdOut = getBufferedWriter(stdOutFileName);
		stdErrFileName = configuration.execDictionary.get("stdErrFileName");
		stdErr = getBufferedWriter(stdErrFileName);

		// Create unique headers for the output files which include useful information
		stdOutHeader = createOutputHeader("standard output");
		stdErrHeader = createOutputHeader("standard error");
		
		// Now write them out
		try { 
			stdOut.write(stdOutHeader);
			stdOut.write("# Executable to be run is: " + configuration.fullCommand +"\n");
			stdOut.close();
			stdErr.write(stdErrHeader);
			stdErr.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// All setup completed, return that the job will now run
		return CommandStatus.RUNNING;
	}
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {
		
		// Loop over the stages and launch them. This needs to be done 
		// sequentially, so use a regular, non-concurrent access loop
		for ( int i = 0; i < configuration.splitCommand.size(); i++) {
			
			// Check the status to ensure job has not been canceled
			if (status == CommandStatus.CANCELED) {
				System.out.println("INFO: Job has been canceled, quitting now.");
				break;
			}
			
			// Get the command
			String thisCommand = configuration.splitCommand.get(i);
			
			// Set up the process builder
			status = setupProcessBuilder(thisCommand);
			
			// Run the job
			status = runProcessBuilder();
			
			// Check the status to ensure the job hasn't failed
			checkStatus(status);
				
			
			// Monitor the job to ensure it finished successfully or to watch it
			// if it is still running
			System.out.println("INFO: Monitoring job");
			monitorJob();
			
			if(status != CommandStatus.SUCCESS) {
				System.out.println("FAILURE: The status of job " + i + " at job finish is " + status);
				System.out.println("FAILURE: Something went wrong with job " + i + "! Moving to next job");
				continue;
			}
			
		}
		
		// Close up the output streams
		try { 
			stdOut.close();
			stdErr.close();
		}
		catch (IOException e) {
			status = CommandStatus.INFOERROR;
			System.out.println("INFO: Could not close the output and/or error files, returning INFOERROR");
			return status;
		}
		
		
		// Return the result
		return status;
	}
	
	
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#fixExecutableName()}
	 */
	@Override
	protected String fixExecutableName() {
		
		// Get the information from the executable dictionary
		int numProcs = Math.max(1,
				Integer.parseInt(configuration.execDictionary.get("numProcs")));
		String fixedExecutableName = configuration.execDictionary.get("executable");
		String installDirectory = configuration.execDictionary.get("installDir");
		String inputFile = configuration.execDictionary.get("inputFile");
		String separator = "/";
		
		// Set the name of the working directory
		configuration.workingDirectoryName = configuration.execDictionary.get("workingDirectory");
		
		// If the input file should be appended, append it
		if(configuration.appendInput)
			fixedExecutableName += " " + inputFile;
		
		configuration.fullCommand = fixedExecutableName;
		
		// Determine the proper separator 
		if (installDirectory != null && installDirectory.contains(":\\"))
			separator = "\\";
		
		// Append a final separator to the install directory if required
		if (installDirectory != null && !installDirectory.endsWith(separator))
			installDirectory = installDirectory + separator;
		
		// Search for and replace the ${inputFile} to properly configure the input file
		if (fixedExecutableName.contains("${inputFile}") && !configuration.appendInput)
			fixedExecutableName = fixedExecutableName.replace("${inputFile}", inputFile);
		
		if (fixedExecutableName.contains("${installDir}") && installDirectory != null) 
			fixedExecutableName = fixedExecutableName.replace("${installDir}", installDirectory);
		
		// If MPI should be used if there are multiple cores, add it to the executable
		if (numProcs > 1) 
			fixedExecutableName = "mpirun -np " + numProcs + " " + fixedExecutableName;
		
		//Clean up any unnecessary white space
		fixedExecutableName = fixedExecutableName.trim();
		
		// Split the full command into its stages, if there are any.
		if (!fixedExecutableName.contains(";")) 
			configuration.splitCommand.add(fixedExecutableName);
		//Otherwise split the full command and put it into CommandConfiguration.splitCommand
		else {
			for (String stage : fixedExecutableName.split(";"))
				configuration.splitCommand.add(stage);
		}

		// Print launch stages so that user can confirm
		for (int i = 0; i < configuration.splitCommand.size(); i++) {
			String cmd = configuration.splitCommand.get(i);
			System.out.println("LocalCommand Message: Launch stage " + i
					+ " = " + cmd);
		}

		return fixedExecutableName;
		
	}
	
	@Override
	/**
	 * Method that overrides Commmand:Cancel and actually implements
	 * the particular LocalCommand to be cancelled.
	 */
	public CommandStatus cancel() {
		status = CommandStatus.CANCELED;
		return status;
	}

	
}
