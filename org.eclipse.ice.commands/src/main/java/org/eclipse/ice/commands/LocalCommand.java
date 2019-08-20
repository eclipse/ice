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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;

/**
 * This class inherits from Command and gives available functionality for local commands.
 * @author Joe Osborn
 *
 */
public class LocalCommand extends Command{

	
	
	
	@Override
	/**
	 * Method that overrides Commmand:Execute and actually implements
	 * the particular LocalCommand to be executed.
	 */
	public CommandStatus Execute(Dictionary<String, String> dictionary) {
		
		// Local Declarations

		// Set the default value of the status to processing
		status = CommandStatus.PROCESSING;

		// Set the dictionary reference
		configuration.execDictionary = dictionary;
		
		status = CommandStatus.LAUNCHING;
		status = Launch();
		
		
		return status;
	}
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#Launch()}
	 */
	@Override
	public CommandStatus Launch() {
		
		String executable = null, inputFile = null;
		String stdOutFileName = null, stdErrFileName = null;
		String stdOutHeader = null, stdErrHeader = null;

		// Make sure the dictionary was actually set
		if (configuration.execDictionary != null) {
			
			// Make sure the dictionary contains the keys we need
			executable = configuration.execDictionary.get("executable");
			inputFile = configuration.execDictionary.get("inputFile");
			stdOutFileName = configuration.execDictionary.get("stdOutFileName");
			stdErrFileName = configuration.execDictionary.get("stdErrFileName");
		}
		
		// Check the info and return failure if something was not set correctly
		if (executable == null || inputFile == null || stdOutFileName == null
					|| stdErrFileName == null) {
				status = CommandStatus.INFOERROR;
				return status;
		}
		
		// If the input file name should not be appended to the executable, set it
		if ( configuration.execDictionary.get("noAppendInput") != null 
				&& configuration.execDictionary.get("noAppendInput") == "true")
			configuration.appendInput = false;
		
		// Set the command to actually run and execute
		configuration.fullCommand = FixExecutableName();
		
		//Set the output and error buffer writers
		stdOutFileName = configuration.execDictionary.get("stdOutFileName");
		stdOut = GetBufferedWriter(stdOutFileName);
		stdErrFileName = configuration.execDictionary.get("stdErrFileName");
		stdErr = GetBufferedWriter(stdErrFileName);

		// Create unique headers for the output files which include useful information
		stdOutHeader = CreateOutputHeader("standard output");
		stdErrHeader = CreateOutputHeader("standard error");
		
		// Now write them out
		try { 
			stdOut.write(stdOutHeader);
			stdOut.close();
			stdErr.write(stdErrHeader);
			stdErr.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Now that all of the prerequisites have been set, start the job running
		status = Run();
		
		return status;
	}
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#Run()}
	 */
	@Override
	public CommandStatus Run() {
		
		
		return status;
	}
	
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#FixExecutableName()}
	 */
	@Override
	protected String FixExecutableName() {
		
		Date currentDate = new Date();
		int numProcs = Math.max(1,
				Integer.parseInt(configuration.execDictionary.get("numProcs")));
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		String fixedExecutableName = configuration.execDictionary.get("executable");
		String installDirectory = configuration.execDictionary.get("installDir");
		String inputFile = configuration.execDictionary.get("inputFile");
		String separator = "/";
		
		// Set the name of the working directory
		configuration.workingDirectoryName = "Launch_" + shortDate.format(currentDate);
		
		// If the input file should be appended, append it
		if(configuration.appendInput)
			fixedExecutableName += " " + configuration.execDictionary.get("inputFile");
		
		configuration.fullCommand = fixedExecutableName;
		
		// Determine the proper separator 
		if (installDirectory != null && installDirectory.contains(":\\"))
			separator = "\\";
		
		// Append a final separator to the install directory if required
		if (installDirectory != null && !installDirectory.endsWith(separator))
			installDirectory = installDirectory + separator;
		
		// Search for and replace the ${inputFile} to properly configure the input file
		if (fixedExecutableName.contains("${inputFile}"))
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
			System.out.println("JobLaunchAction Message: Launch stage " + i
					+ " = " + cmd);
		}

		return fixedExecutableName;
		
	}
	
	@Override
	/**
	 * Method that overrides Commmand:Cancel and actually implements
	 * the particular LocalCommand to be cancelled.
	 */
	public CommandStatus Cancel() {
		return null;
	}
	/**
	 * Default constructor
	 */
	public LocalCommand() {}

	
}
