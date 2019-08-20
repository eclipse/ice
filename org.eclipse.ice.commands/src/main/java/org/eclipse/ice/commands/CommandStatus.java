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
 * This class provides a status for commands, in particular in relation to whether 
 * or not a particular command executed properly (or not).
 * @author Joe Osborn
 *
 */


/**
 * 
 * Enumeration indicating what the status of the particular command was.
 */
enum Status{
	SUCCESS, WORKING, FAILED;
}

public class CommandStatus{

	/**
	 * Default constructor
	 */
	
	public CommandStatus() {
		
	}
	 
	

}
