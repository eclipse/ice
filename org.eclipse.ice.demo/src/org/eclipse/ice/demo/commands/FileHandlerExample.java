/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Examples for running Commands API package org.eclipse.ice.commands 
 *   Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.demo.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.FileHandlerFactory;
import org.eclipse.ice.commands.IFileHandler;

/**
 * This class is intended to be an example showing how to use the local
 * FileHandler API as given in the Commands API
 * 
 * @author Joe Osborn
 *
 */
public class FileHandlerExample {

	static String localSource;

	static String localDestination;

	// A string with a new name for the move command to use
	static String newName = "/newfilename.txt";

	/**
	 * Main function to show an example of how the Commands API moves and copies
	 * files
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Create a temporary dummy file and directory locally to play around with
		createDummyLocalFile();

		// Copy the file to the directory
		copyFileLocally();

		// Move the file to the same directory with a new name
		moveFileLocally();

		// Delete the created file and directory
		cleanUpFiles();

		return;
	}

	/**
	 * This function copies a test file to a test directory locally. It demonstrates
	 * the use of the FileHandler API within the Commands API
	 */
	public static void copyFileLocally() {
		FileHandlerFactory factory = new FileHandlerFactory();
		IFileHandler handler = null;

		// Get the appropriate remote/local FileHandler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to copy the file
		try {
			CommandStatus status = handler.copy(localSource, localDestination);
			if (status != CommandStatus.SUCCESS)
				System.out.println("Copy file failed! Check console for error messages");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// We can also check that the file exists
		try {
			boolean exist = handler.exists(localDestination);
			if (exist)
				System.out.println("Copy file successful!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This function moves a test file to a test directory locally and gives it a
	 * new name. It demonstrates the use of the FileHandler API within the Commands
	 * API
	 */
	public static void moveFileLocally() {
		FileHandlerFactory factory = new FileHandlerFactory();
		IFileHandler handler = null;

		// Get the appropriate remote/local FileHandler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			CommandStatus status = handler.move(localSource, localDestination + newName);
			if (status != CommandStatus.SUCCESS)
				System.out.println("Move file failed! Check console for error messages");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// We can also check that the file exists
		try {
			boolean exist = handler.exists(localDestination + newName);
			if (exist)
				System.out.println("Move file successful!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This function creates a dummy temporary file locally for the copy and move
	 * commands to use. It also creates a dummy temporary directory to copy/move the
	 * file to.
	 */
	public static void createDummyLocalFile() {
		// First create a dummy text file to test
		String source = "dummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Turn the path into a string to pass to the command
		localSource = sourcePath.toString();
		System.out.println("Created source file at: " + localSource);

		// Do the same for the destination
		Path destinationPath = null;
		String dest = "testCopyDirectory";
		try {
			destinationPath = Files.createTempDirectory(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Turn the path into a string to give to the command
		localDestination = destinationPath.toString();
		System.out.println("Created destination file at: " + localDestination);
	}

	/**
	 * This function cleans up the temporary files and directory created
	 */
	public static void cleanUpFiles() {
		boolean deleteDestination = false;

		// Don't need to delete the source file since we moved it, so by definition the temporary file
		// is no longer at it's source location
		deleteDestination = deleteDirectory(new File(localDestination));
		if (!deleteDestination) {
			System.out.println("Couldn't delete destination file/directory at: " + localDestination);
		}
		else {
			System.out.println("Deleted files successfully!");
		}
		
		return;
	}

	/**
	 * A simple test method to recursively delete temporary files/directories
	 * created in this test class
	 * 
	 * @param directory - top level directory from which to delete everything
	 *                  underneath
	 * @return - boolean - true if everything deleted, false if not
	 */
	public static boolean deleteDirectory(File directory) {
		File[] contents = directory.listFiles();
		if (contents != null) {
			for (File file : contents) {
				deleteDirectory(file);
			}
		}
		return directory.delete();
	}

}
