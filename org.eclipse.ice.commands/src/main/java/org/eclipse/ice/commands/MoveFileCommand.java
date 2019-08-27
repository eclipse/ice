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
 * Parent class for remote and local move file commands. 
 * @author Joe Osborn
 *
 */
public abstract class MoveFileCommand extends FileHandler {

	/**
	 * The path to the source file which is to be moved
	 */
	String source = "";
	
	/**
	 * The path of the destination for which the source file will be moved to
	 */
	String destination = "";
	
	/**
	 * Default constructor
	 */
	public MoveFileCommand() {
	}
	
	/**
	 * This class does the actual handling of executing the move command and is
	 * called at constructor time. The class is abstract so that remote and local
	 * moves can be handled individually.
	 */
	protected abstract void move();
	
}
