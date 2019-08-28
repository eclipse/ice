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

import java.nio.file.Path;

/**
 * Parent class for remote and local copy file commands. Inherits
 * from FileHandler and is responsible for executing copy file commands.
 * @author Joe Osborn
 *
 */
public abstract class CopyFileCommand extends Command {

	
	/**
	 * The path to the source file which is to be copied
	 */
	Path source;
	
	/**
	 * The path of the destination for which the source file will be copied to
	 */
	Path destination;
	
	
	/**
	 * Default constructor
	 */
	public CopyFileCommand() {}
	
	
}
