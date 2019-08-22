/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

/**
 * The command factory simplifies the creation of Commands without revealing how
 * they are constructed. It is primarily used for creating generic commands,
 * whereas other factories are use for creating specific commands such as file
 * transfer commands.
 * 
 * @author Jay Jay Billings, Joe Osborn
 */
public class CommandFactory {

	/**
	 * Constructor
	 */
	public CommandFactory() {
		
	}

	/**
	 * Method which gets a command and subsequently executes it on a host
	 * See also Command class {@link org.eclipse.ice.commands.Command}.
	 * @param command - command to execute
	 * @param host - host on which to execute command
	 * @return Command
	 */
	public static Command getCommand(String command, String host) {

		return null;
	}

}
