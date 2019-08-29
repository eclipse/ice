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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The FileHandler class is a utility class for using commands that move files.
 * The class uses source and destination designations to identify how files
 * should be handled, and it can handle both local and remote files as sources
 * and destinations. Files can be moved, copied or checked for existence.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class FileHandler {

	/**
	 * Default constructor
	 */
	public FileHandler() {
	}

	/**
	 * This operations moves files from the source (src) to the destination
	 * (dest). If the operation fails, an IOException will be thrown.
	 * 
	 * @param src
	 * @param dest
	 * @param hostname
	 * @return CommandStatus - status indicating move was successfully completed
	 * @throws IOException
	 */
	public static void move(final String src, final String dest) throws IOException {
		
		MoveFileCommand command = null;
		
		// Just test local moving for now. 
		
		//TODO need to determine how to differentiate local vs. remote moves with just
		// the strings and not a hostname
		boolean isLocal = true;
		
		// Check to make sure the paths exist
		boolean sourceExists = exists(src);
		boolean destExists = exists(dest);
	
		// If destination doesn't exist, create it
		if(!destExists) {
			try {
				Path destination = Paths.get(dest);
				Files.createDirectories(destination);
				// If an exception wasn't thrown, the destination exists
				destExists = true;
			}
			catch(IOException e) {
				System.out.println("Couldn't create directory for local copy! Failed.");
				e.printStackTrace();
			}
		}
				
		if(sourceExists && destExists) {
			if(isLocal) {
				command = new LocalMoveFileCommand(src, dest);
			}
			else {
				command = new RemoteMoveFileCommand(src, dest);
			}
		}
		
		return;
	}

	/**
	 * This operations copies files from the source (src) to the destination
	 * (dest). If the operation fails, an IOException will be thrown. 
	 * 
	 * @param src
	 * @param dest
	 * @return CommandStatus - status indicating copy was successfully completed
	 * @throws IOException
	 */
	public static void copy(final String src, final String dest) throws IOException {
		
		CopyFileCommand command = null;
	
		//TODO need to determine how to differentiate local vs. remote copies/moves with just
		// the strings and not a hostname
		boolean isLocal = true;
		
		
		// Check to make sure the source exists
		boolean sourceExists = exists(src);
		boolean destExists = exists(dest);
		
		// If destination doesn't exist, create it
		if(!destExists) {
			try {
				Path destination = Paths.get(dest);
				Files.createDirectories(destination);
				// If an exception wasn't thrown, then destination now exists
				destExists = true;
			}
			catch(IOException e) {
				System.out.println("Couldn't create directory for local move! Failed.");
				e.printStackTrace();
			}
		}
		
		if(sourceExists && destExists) {
			if(isLocal) {
				command = new LocalCopyFileCommand(src, dest);
			}
			else {
				command = new RemoteCopyFileCommand(src, dest);
			}
		}
		else {
			System.out.println("The source file does not exist! Doing nothing.");
		}
			
		return;
	}

	/**
	 * This operations determines whether or not the file argument exists.
	 * TODO - this only works for local files at the moment.
	 * @param file the file for which to search
	 * @return true if the file exists, false if not
	 * @throws IOException
	 */
	public static boolean exists(final String file) throws IOException {

		// Get the path from the passed string
		Path path = Paths.get(file);
		
		// Check if the path exists or not. Symbolic links are followed
		// by default, see {@link java.nio.file.Files#exists}
		return Files.exists(path);
	}

	

	
}
