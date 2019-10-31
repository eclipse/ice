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
package org.eclipse.ice.tests.commands;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.LocalMoveFileCommand;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalMoveFileCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class LocalMoveFileCommandTest {

	/**
	 * A source file that is created for testing
	 */
	String source = null;

	/**
	 * A destination path that is created for testing
	 */
	String dest = null;

	/**
	 * This function sets up and creates a dummy test file for the testing of the
	 * class
	 * {@link org.eclipse.ice.commands.LocalMoveFileCommand#LocalMoveFileCommand(String, String)}
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Set a dummy source and destination to copy

		// First create a dummy text file to test
		source = "dummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, source);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Turn the path into a string to pass to the command
		source = sourcePath.toString();

		// Do the same for the destination
		Path destinationPath = null;
		dest = "testCopyDirectory";
		try {
			destinationPath = Files.createTempDirectory(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Turn the path into a string to give to the command
		dest = destinationPath.toString();
	}

	/**
	 * Deletes the temporarily made files since they are not useful
	 * 
	 * @throws java.lang.Exception
	 */
	
	public void deleteFiles() throws Exception {

		// Need to get the filename individually
		String delims = "[/]";
		String separator = FileSystems.getDefault().getSeparator();
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
			// Have to add another forward slash for windows
			separator += "\\";
			delims = separator;
		}
		String[] tokens = source.split(delims);
		String filename = tokens[tokens.length - 1];

		// Make the destination path + the filename
		String fullDestination = dest + separator + filename;

		// Get the paths
		Path destFile = Paths.get(fullDestination);
		Path destDir = Paths.get(dest);
		Path srcFile = Paths.get(source);
		
		// Delete the files
		// Only need to delete the moved file and destination directory,
		// since the source file was created in the default system directory
		// which can't be deleted and the moved file, by definition, doesn't
		// exist in the original directory
		System.out.println("Deleting " + fullDestination + "  " + dest);
		try {
			Files.deleteIfExists(destFile);
			Files.deleteIfExists(srcFile);
			
		} catch (IOException e) {
			System.err.println(e);
		}
		try {
			Files.deleteIfExists(destDir);
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalMoveFileCommand()}
	 * Tests moving a file to a different directory, with the same name
	 * @throws Exception 
	 */
	@Test
	public void testLocalMoveFileCommand() throws Exception {

		System.out.println("Moving: " + source + " to destination: " + dest);
		// Make the command
		LocalMoveFileCommand command = new LocalMoveFileCommand();
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();
		String filename = "";
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
			filename = source.substring(source.lastIndexOf("\\"));
		} else {
			filename = source.substring(source.lastIndexOf("/"));
		}
		// Check if the path exists now
		Path path = Paths.get(dest + filename);
		assert (Files.exists(path));
		
		deleteFiles();

	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalMoveFileCommand()} where
	 * the file doesn't change directories and just changes names
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testLocalMoveSameDir() throws Exception {
		// First just delete the temp directory that was created, since we don't need it
		Path destDir = Paths.get(dest);
		Files.deleteIfExists(destDir);
		
		String separator = FileSystems.getDefault().getSeparator();
		// Change the name of dest to a new name with the same path structure
		String fullPath = source.substring(0,source.lastIndexOf(separator)+1);
		// Give dest the full path + a new file name
		dest = fullPath + "newFileName.txt";
		System.out.println("Moving: " + source + " to destination: " + dest);
		
		// Make the command and execute the move
		LocalMoveFileCommand command = new LocalMoveFileCommand();
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();

		assert(status == CommandStatus.SUCCESS);
		
		// Check that the file exists with the new name
		Path path = Paths.get(dest);
		assert (Files.exists(path));
		
		deleteFiles();
	}
	
	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalMoveFileCommand()} where
	 * the file changes directories and changes names
	 * @throws Exception 
	 */
	@Test
	public void testLocalMoveNewName() throws Exception {
		String destDir = dest;
		String separator = FileSystems.getDefault().getSeparator();
		dest = dest + separator + "otherFileName.txt";
		System.out.println("Moving: " + source + " to destination: " + dest);
		// Make the command
		LocalMoveFileCommand command = new LocalMoveFileCommand();
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();

		assert(status == CommandStatus.SUCCESS);
		
		// Check if the path exists now
		Path path = Paths.get(dest);
		assert (Files.exists(path));
		
		deleteFiles();
		// Delete the extra directory
		Files.deleteIfExists(Paths.get(destDir));
	}
	
	
	
	
}
