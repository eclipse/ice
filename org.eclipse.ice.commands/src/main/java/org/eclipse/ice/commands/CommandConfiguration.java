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
	protected AtomicBoolean isLocal;
	
	
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
	}

	
	
	
}
