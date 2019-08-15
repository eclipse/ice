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

/**
 * This class is the instantiation class of the CommandFactory class and thus
 * is responsible for executing particular commands.
 * @author Joe Osborn
 *
 */
public class Command{

	/**
	 * Default constructor
	 */
	public Command() {
		
	}
	
	/**
	 * 
	 * @param command - Command to be executed
	 * @return CommandStatus - indicating whether or not the Command was properly executed
	 */
	public CommandStatus execute(String command) {
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
