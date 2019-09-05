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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Child class for moving a file locally without a remote connection
 * 
 * @author Joe Osborn
 *
 */
public class LocalMoveFileCommand extends LocalCommand {

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
	public LocalMoveFileCommand() {
	}

	/**
	 * This function actually executes the move file command. It checks that the
	 * move command was completed successfully. It returns a CommandStatus
	 * indicating whether or not the move was successful.
	 * 
	 * @return CommandStatus
	 */
	@Override
	public CommandStatus execute() {
		status = run();

		return status;
	}

	/**
	 * This function contains the command to actually move the file. Returns a
	 * CommandStatus indicating that the command is currently running and needs to
	 * be checked that it completed correctly.
	 * 
	 * @return CommandStatus
	 */
	@Override
	protected CommandStatus run() {

		// Get the directory structure to test if we are moving to a new directory
		// or simply changing the name of a file
		String[] sourceDirs = source.toString().split("/");
		String[] destinationDirs = destination.toString().split("/");
		boolean different = false;
		
		// If the number of directories is different, then the source/destination are 
		// definitely different
		if(sourceDirs.length != destinationDirs.length)
			different = true;
		// Otherwise they are the same length and we can iterate over either happily
		else {
			for(int i = 0; i< sourceDirs.length-1; i++) {
				if(!sourceDirs[i].equals(destinationDirs[i])) {
					different = true;
					break;
				}
			}
		}
		// If all of the directories are the same, check to see if the final entry in the path
		// is either a filename or a directory by looking at its .* file extension
		
		String[] sourceFileNames = sourceDirs[sourceDirs.length-1].split("\\.");
		String[] destinationFileNames = destinationDirs[destinationDirs.length-1].split("\\.");
	
		// Check if the file extensions are equal to one another
		boolean sameFileExt = sourceFileNames[sourceFileNames.length-1].equals(destinationFileNames[destinationFileNames.length-1]);
		
		// If the directory paths are the same and the files have the same file extension
		// type, then we are just changing a file name
		if(!different && sameFileExt) {
			try {
				// destinationDirs will have the desired file name in the length-1 entry
				Files.move(source, source.resolveSibling(destinationDirs[destinationDirs.length-1]), REPLACE_EXISTING);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		// All other cases the file is moving directory
		else {
			try {
				Files.move(source, destination.resolve(source.getFileName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return CommandStatus.RUNNING;
	}

	/**
	 * This function cancels the command when called. See also
	 * {@link org.eclipse.ice.commands.Command#cancel()}
	 */
	@Override
	public CommandStatus cancel() {
		status = CommandStatus.CANCELED;
		return CommandStatus.CANCELED;
	}

	/**
	 * This function sets the Paths for source and destination to the given strings
	 * 
	 * @param src  - string corresponding to the source file
	 * @param dest - string corresponding to the destination file
	 */
	public void setConfiguration(String src, String dest) {
		source = Paths.get(src);
		destination = Paths.get(dest);
		return;
	}

	/**
	 * A function that returns the source path in string form
	 * 
	 * @return - String
	 */
	public String getSource() {
		return source.toString();
	}

	/**
	 * A function that returns the destination path in string form
	 * 
	 * @return - String
	 */
	public String getDestination() {
		return destination.toString();
	}

}
