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
import java.util.Calendar;
import java.util.Dictionary;
import java.util.concurrent.atomic.AtomicBoolean;



/**
 * This class is the instantiation class of the CommandFactory class and thus
 * is responsible for executing particular commands.
 * @author Joe Osborn
 *
 */
public abstract class Command{

	/**
	 * An atomic boolean for notifying the thread that it should proceed launching
	 * the job since the form has been submitted.
	 */
	private AtomicBoolean formSubmitted;
	
	/**
	 * The current status of the command
	 */
	protected CommandStatus status;
	
	/**
	 * The configuration parameters of the command - should contain some information about
	 * what the command is actually intended to do.
	 */
	protected CommandConfiguration configuration;
	
	/**
	 * Output streams for the job
	 */
	BufferedWriter stdOut = null, stdErr = null;
	
	/**
	 * Default constructor
	 */
	public Command() {}
	
	/**
	 * This function executes the command based on the information provided in the dictionary
	 * 
	 * @param dictionary - Command to be executed
	 * @return CommandStatus - indicating whether or not the Command was properly executed
	 */
	public abstract CommandStatus Execute(Dictionary<String, String> dictionary);
	
	/**
	 * This function sets up the necessary prequisites to actually run the particular command.
	 * @return - CommandStatus indicating the result of the function
	 */
	public abstract CommandStatus Launch();
	
	/**
	 * This function actually runs the particular command in question. It is called in Launch
	 * after all of the setup for the job execution is finished.
	 * @return - CommandStatus indicating the result of the function.
	 */
	public abstract CommandStatus Run();
	
	/**
	 * This function cancels the already submitted command, if possible.
	 * @return CommandStatus - indicates whether or not the Command was properly cancelled.
	 */
	public abstract CommandStatus Cancel();
	
	
	/**
	 * This function actually assembles and fixes the name of the executable to be launched.
	 * It replaces ${inputFile}, ${installDir} and other keys from the dictionary. The function
	 * is abstract so that Local and Remote executable names can be handled individually,
	 * since the remote target file system is not necessarily the same as the local.
	 * @return - String that is the executable to be run
	 */
	protected abstract String FixExecutableName();
	
	
	/**
	 * This function returns the status for a particular command at a given time
	 * in the operation of the command.
	 * @return - return current status for a particular command
	 */
	public CommandStatus GetStatus() {
		return status;
	}
	
	/**
	 * This function sets the status for a particular command to be stat
	 * @param stat - new CommandStatus to be set
	 */
	public void SetStatus(CommandStatus stat) {
		status = stat;
	}
	
	/**
	 * This function sets the CommandConfiguration for a particular command.
	 * @param config - the configuration to be used for a particular command.
	 */
	public void SetConfiguration(CommandConfiguration config) {
		configuration = config;
	}
	
	/**
	 * This function returns to the user the configuration that was used 
	 * to create a particular command. 
	 * @return - the particular configuration for this command
	 */
	public CommandConfiguration GetConfiguration() {
		return configuration;
	}
	
	/**
	 * This function creates and returns a BufferedWriter for appending text to the
	 * file specified in the call
	 * @param filename - file to append to
	 * @return - buffered writer for appending
	 */
	protected BufferedWriter GetBufferedWriter(String filename) {
		
		FileWriter writer = null;
		BufferedWriter bufferedWriter = null;

		// Check the file name and the create the writer
		if (filename != null) {
			try {
				writer = new FileWriter(filename, true);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			bufferedWriter = new BufferedWriter(writer);
			return bufferedWriter;
		}
		else
			return null;
	}
	
	
	/**
	 * This function creates a set of generic output header text useful for debugging
	 * purposes, for example in log files.
	 * @param logName - the particular log name
	 * @return - A string with the corresponding header text
	 */
	protected String CreateOutputHeader(String logName) {
		String header = null, localHostname = null;

		// Get the machine identity since the local machine launches the job
		// regardless of whether or not the job is local or remote
		try {
			InetAddress addr = InetAddress.getLocalHost();
			localHostname = addr.getHostName();
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Add the date and time
		header = "# Job launch date: ";
		header += new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar
				.getInstance().getTime()) + "\n";
		
		// Add the point of origin
		header += "# Launch host: " + localHostname + "\n";
		
		// Add the target machine
		header += "# Target host: " + configuration.execDictionary.get("hostname") + "\n";
		
		// Add the execution command
		header += "# Command Executed: " + configuration.fullCommand + "\n";
		
		// Add the input file name
		header += "# Input file: " + configuration.execDictionary.get("inputFile") + "\n";
		
		// Add an empty line
		header += "\n";

		return header;
	}
	
	
	
	
}
