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
	private Path source;

	/**
	 * The path of the destination for which the source file will be copied to
	 */
	private Path destination;

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

		// Check to see if the file paths are the same and if the filename has the same
		// extension for determining whether or not it should be a move or change file
		// name
		boolean sameFileExtension = checkFileExtension();

		// Split the destination by path delimiter to get the desired filename
		String[] destinationDirs = destination.toString().split("/");

		// If the directory paths are the same and the files have the same file
		// extension
		// type, then we are just changing a file name
		if (sameFileExtension) {
			try {
				status = CommandStatus.RUNNING;
				// destinationDirs will have the desired file name in the length-1 entry
				Files.move(source, source.resolveSibling(destinationDirs[destinationDirs.length - 1]),
						REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				status = CommandStatus.FAILED;
				return status;
			}
		}
		// All other cases the file is moving directory
		else {
			try {
				status = CommandStatus.RUNNING;
				Files.move(source, destination.resolve(source.getFileName()));
			} catch (IOException e) {
				e.printStackTrace();
				return CommandStatus.FAILED;
			}
		}

		return CommandStatus.SUCCESS;
	}

	/**
	 * This function determines whether or not the source and destination file path
	 * are the same and whether or not to treat the file move as moving to different
	 * directories or simply just a name change of the file. They have to be called
	 * differently in Files.move, hence it is necessary to determine whether or not
	 * the filename is just changing in the same directory or if it is actually
	 * moving to a new directory.
	 * 
	 * @return - boolean - if the directory path is the exact same and the file
	 *         extension is the exact same, then return true. Otherwise return false
	 */
	private boolean checkFileExtension() {
		// Get the directory structure to test if we are moving to a new directory
		// or simply changing the name of a file
		String[] sourceDirs = source.toString().split("/");
		String[] destinationDirs = destination.toString().split("/");
		boolean different = false;

		// If the number of directories is different, then the source/destination are
		// definitely different
		if (sourceDirs.length != destinationDirs.length)
			different = true;
		// Otherwise they are the same length and we can iterate over either happily
		else {
			for (int i = 0; i < sourceDirs.length - 1; i++) {
				if (!sourceDirs[i].equals(destinationDirs[i])) {
					different = true;
					break;
				}
			}
		}
		// If all of the directories are the same, check to see if the final entry in
		// the path is either a filename or a directory by looking at its .* file
		// extension

		String[] sourceFileNames = sourceDirs[sourceDirs.length - 1].split("\\.");
		String[] destinationFileNames = destinationDirs[destinationDirs.length - 1].split("\\.");

		// Check if the file extensions are equal to one another
		boolean sameFileExt = sourceFileNames[sourceFileNames.length - 1]
				.equals(destinationFileNames[destinationFileNames.length - 1]);

		// If the source and destination have the same path and have the same file
		// extension then we should treat this as a file name change, otherwise it is a
		// file directory move
		if (!different && sameFileExt)
			return true;
		else
			return false;

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
