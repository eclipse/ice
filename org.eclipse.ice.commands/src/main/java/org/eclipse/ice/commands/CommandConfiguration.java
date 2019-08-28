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
import java.util.HashMap;

/**
 * 
 * This class configures particular commands to be executed. It contains all of the 
 * relevant information about configuring the command. The user should specify the
 * details of the command in the declaration of a Command instance by providing a 
 * CommandConfiguration.
 * @author Joe Osborn
 *
 */
public class CommandConfiguration {

	/**
	 * An integer ID to associate with a job
	 */
	protected int commandId;
	
	/**
	 * The dictionary that contains the command properties.
	 */
	protected HashMap<String,String> execDictionary = new HashMap<String, String>();
	
	/**
	 * A flag to mark whether or not the input file name should be appended to the executable command.
	 * Marked as true by default so that the user (by default) specifies the input file name.
	 */
	protected boolean appendInput = true;
	
	
	/**
	 * The full command string of all stages that will be executed.
	 */
	protected String fullCommand = "";
	
	/**
	 * The set of commands in the fullCommand string split into stages. Each command is then executed separately.
	 * If the command is single stage, then splitCommand is identical to fullCommand.
	 */
	protected ArrayList<String> splitCommand = new ArrayList<String>();
	
	
	/**
	 * The name of the working directory in which the job will be launched. The
	 * default value is the prefix.
	 */
	protected String workingDirectoryName = "";
	
	
	/**
	 * Default constructor
	 */
	public CommandConfiguration() {
		// Assume some default variables
		commandId = -999;
	}

	/**
	 * Constructor which initializes several of the member variables
	 * See member variables in class CommandConfiguration for descriptions of variables
	 * @param _commandId
	 * @param _isLocal
	 * @param _execDictionary
	 * @param _appendInput
	 */
	public CommandConfiguration(int _commandId, 
			HashMap<String,String> _execDictionary, boolean _appendInput) {
		commandId = _commandId;
		execDictionary = _execDictionary;
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
	public int getCommandId() {
		return commandId;
	}
	public void setExecDictionary(HashMap<String, String> _dictionary) {
		execDictionary = _dictionary;
		return;
	}
	public void setAppendInput(boolean _appendInput) {
		appendInput = _appendInput;
		return;
	}
	
	/** 
	 * Don't want a setter function for FullCommand since this is determined in
	 * {@link org.eclipse.ice.commands.LocalCommand#fixExecutableName()}
	 */
	public String getFullCommand() {
		return fullCommand;
	}
}
