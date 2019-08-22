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
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * This class configures particular commands to be executed and depends on the Command class.
 * @author Joe Osborn
 *
 */
public class CommandConfiguration {

	/**
	 * An integer ID to associate with a job
	 */
	protected int commandId;
	
	/**
	 * An AtomicBoolean that is true if the job is to be launched locally and false otherwise.
	 */
	protected AtomicBoolean isLocal = new AtomicBoolean();
	
	
	/**
	 * The dictionary that contains the command properties.
	 */
	protected Dictionary<String,String> execDictionary;
	
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
	protected String workingDirectoryName = "Launch_";
	
	
	/**
	 * Default constructor
	 */
	public CommandConfiguration() {
		// Assume some default variables
		commandId = -999;
		isLocal.set(true);
		execDictionary = new Hashtable<String, String>();
				
	}

	/**
	 * Constructor which initializes several of the member variables
	 * See member variables in class CommandConfiguration for descriptions of variables
	 * @param _commandId
	 * @param _isLocal
	 * @param _execDictionary
	 * @param _appendInput
	 */
	public CommandConfiguration(int _commandId, AtomicBoolean _isLocal, 
			Dictionary<String,String> _execDictionary, boolean _appendInput) {
		commandId = _commandId;
		isLocal = _isLocal;
		execDictionary = _execDictionary;
		appendInput = _appendInput;
		
	}
	
	
	
	/**
	 * Make some getter and setter functions to access the CommandConfigurations
	 * protected variables
	 */
	
	public void setCommandId(int _commandId) {
		commandId = _commandId;
	}
	public int getCommandId() {
		return commandId;
	}
	public void setExecDictionary(Dictionary<String, String> _dictionary) {
		execDictionary = _dictionary;
	}
	public Dictionary<String, String> getExecDictionary() {
		return execDictionary;
	}
	public void setAppendInput(boolean _appendInput) {
		appendInput = _appendInput;
	}
	/** 
	 * Don't want a setter function for FullCommand since this is determined in
	 * {@link org.eclipse.ice.commands.LocalCommand#fixExecutableName()}
	 */
	public String getFullCommand() {
		return fullCommand;
	}
}
