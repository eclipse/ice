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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		source = Paths.get(src);
		destination = Paths.get(dest);
		
		boolean destExists = false;
		try {
			destExists = exists(dest);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// If destination doesn't exist, create it
		if(!destExists) {
			try {
				Files.createDirectories(destination);
			}
			catch(IOException e) {
				System.out.println("Couldn't create directory for local move! Failed.");
				e.printStackTrace();
			}
		}
		
		move();
	}
	
	
	
	@Override
	protected void move() {
		
		try {
			Files.move(source, destination.resolve(source.getFileName()));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		return;
	}
	
}
