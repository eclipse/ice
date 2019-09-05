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

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.FileHandlerFactory;
import org.eclipse.ice.commands.IFileHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class implements several test methods for
 * {@link org.eclipse.ice.commands.FileHandlerFactory}
 * 
 * @author Joe Osborn
 *
 */
public class IFileHandlerFactoryTest {

	/**
	 * A default factory with which to create FileHandler instances
	 */
	FileHandlerFactory factory = new FileHandlerFactory();

	/**
	 * A command factory test that is only useful for accessing some of its member
	 * functions
	 */
	CommandFactoryTest factorytest = new CommandFactoryTest();

	/**
	 * A string which contains a local temporary source file to work with
	 */
	String localSource;

	/**
	 * A string which contains a local temporary destination directory to work with
	 */
	String localDestination;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

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
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("Delete temporary files/directories that were created.");

		// Get the paths
		Path sourcePath = Paths.get(localSource);
		Path destPath = Paths.get(localDestination);

		// Delete the files
		try {
			Files.deleteIfExists(sourcePath);
		} catch (NoSuchFileException e) {
			System.err.format("%s: no such" + " file or directory%n", sourcePath);
			e.printStackTrace();
		} catch (DirectoryNotEmptyException e) {
			System.err.format("%s not empty%n", sourcePath);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}

		try {
			Files.deleteIfExists(destPath);
		} catch (NoSuchFileException e) {
			System.err.format("%s: no such" + " file or directory%n", destPath);
			e.printStackTrace();
		} catch (DirectoryNotEmptyException e) {

			// If the directory is not empty, that is because it was a move command
			// and the moved file is in there. So delete the file first and then
			// delete the directory
			File localDestinationFile = new File(localDestination);
			boolean deleted = deleteDirectory(localDestinationFile);

			// Something went wrong and couldn't be deleted
			if (!deleted) {
				System.err.println(e);
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()}
	 * and local file copying.
	 */
	@Test
	public void testLocalFileHandlerFactoryCopyCommand() {
		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to copy the file
		try {
			CommandStatus status = handler.copy(localSource, localDestination);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(localDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()}
	 * and local file moving.
	 */
	@Test
	public void testLocalFileHandlerFactoryMoveCommand() {
		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to move the file
		try {
			CommandStatus status = handler.move(localSource, localDestination);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(localDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for a source file that exists but a destination directory that
	 * does not exist. Tests
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()}
	 */
	@Test
	public void testLocalFileHandlerFactoryDestinationNonExistant() {

		IFileHandler handler = null;

		String newDirectory = "/some/new/directory/";

		// Get the file transfer handler with a nonexistent destination
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to move the file
		try {
			CommandStatus status = handler.move(localSource, localDestination + newDirectory);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(localDestination + newDirectory);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * A simple test method to recursively delete temporary files/directories
	 * created in this test class
	 * 
	 * @param directory - top level directory from which to delete everything
	 *                  underneath
	 * @return - boolean - true if everything deleted, false if not
	 */
	private boolean deleteDirectory(File directory) {
		File[] contents = directory.listFiles();
		if (contents != null) {
			for (File file : contents) {
				deleteDirectory(file);
			}
		}
		return directory.delete();
	}

}
