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
 * Child class for moving a file locally without a remote connection
 * @author Joe Osborn
 *
 */
public class LocalMoveFileCommand extends MoveFileCommand {

	/**
	 * Default constructor
	 */
	public LocalMoveFileCommand() {
		
	}
	
	/**
	 * Constructor which sets the two paths, source and destination,
	 * to those given by the arguments of the constructor. See 
	 * {@link org.eclipse.ice.tests.commands.CopyFileCommand} for member
	 * variable descriptions.
	 * @param src 
	 * @param dest
	 */
	public LocalMoveFileCommand(String src, String dest) {
		source = src;
		destination = dest;
		move();
	}
	
	
	
	@Override
	protected void move() {
		
		//Files.move(source, destination, REPLACE_EXISTING);
		
		
		
		return;
	}
	
}
