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
	 * Default constructor
	 */
	public Command() {}
	
	/**
	 * 
	 * @param command - Command to be executed
	 * @return CommandStatus - indicating whether or not the Command was properly executed
	 */
	public CommandStatus Execute(String command) {
		return null;
	}
	
	/**
	 * 
	 * @return - return current status for a particular command
	 */
	public CommandStatus GetStatus() {
		return null;
	}
}
