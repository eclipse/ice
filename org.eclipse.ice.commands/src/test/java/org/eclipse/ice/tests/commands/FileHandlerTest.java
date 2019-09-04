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
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ice.commands.FileHandler;
import org.eclipse.ice.commands.LocalFileHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.FileHandler}.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class FileHandlerTest {

	String localSource = null;
	String localDestination = null;

	/**
	 * Set up some dummy local files to work with
	 * 
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
	 * Deletes the temporarily made files since they are not useful
	 * 
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

			// Need to get the filename individually
			String delims = "[/]";
			String[] tokens = localSource.split(delims);
			String filename = tokens[tokens.length - 1];

			// Make the destination path + the filename
			String fullDestination = localDestination + "/" + filename;

			// Get the paths
			Path destFile = Paths.get(fullDestination);
			Path destDir = Paths.get(localDestination);

			// Try to delete the destination file. If it can't be deleted, then
			// there is really a problem now and it will complain
			try {
				Files.deleteIfExists(destFile);
			} catch (NoSuchFileException e1) {
				System.err.format("%s: no such" + " file or directory%n", destFile);
				e1.printStackTrace();
			} catch (DirectoryNotEmptyException e1) {
				System.err.format("%s not empty%n", destFile);
				e1.printStackTrace();
			} catch (IOException e1) {
				System.err.println(e1);
				e1.printStackTrace();
			}

			// Try to delete the destination directory. If it can't be deleted, then
			// there is really a problem now and it will complain
			try {
				Files.deleteIfExists(destDir);
			} catch (NoSuchFileException e1) {
				System.err.format("%s: no such" + " file or directory%n", destDir);
				e1.printStackTrace();
			} catch (DirectoryNotEmptyException e1) {
				System.err.format("%s not empty%n", destDir);
				e1.printStackTrace();
			} catch (IOException e1) {
				System.err.println(e);
				e1.printStackTrace();
			}

		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandler#copy(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLocalCopy() {
		System.out.println("Testing testLocalCopy() function.");
		FileHandler handler = null;
		try {
			handler = new LocalFileHandler(localSource, localDestination);
			handler.copy();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that it exists
		try {
			boolean exist = handler.exists(localDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished testing testLocalCopy() function.");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandler#move(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLocalMove() {
		System.out.println("Testing testLocalMove() function.");

		FileHandler handler = null;
		try {
			handler = new LocalFileHandler(localSource, localDestination);
			handler.move();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that it exists
		try {
			boolean exist = handler.exists(localDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Finished testing testLocalMove() function.");

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandler#exists(java.lang.String)}.
	 */
	@Test
	public void testExists() {
		System.out.println("Testing testExists() function.");

		/**
		 * Test a temp file that was created
		 */
		FileHandler handler = new LocalFileHandler();
		try {
			assert (handler.exists(localSource));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Testing testExists() with a non existing file");
		try {
			assert (!handler.exists("/usr/file_that_definitely_doesnot_exist.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Finished testing testExists()");
	}

}
