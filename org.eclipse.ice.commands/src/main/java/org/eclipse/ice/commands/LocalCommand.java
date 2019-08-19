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
		return null;
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
