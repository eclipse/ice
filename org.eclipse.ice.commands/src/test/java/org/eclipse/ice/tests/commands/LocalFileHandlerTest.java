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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.FileHandler;
import org.eclipse.ice.commands.LocalFileHandler;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.FileHandler}, and more
 * specifically the LocalFileHandler implementation
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class LocalFileHandlerTest {
	/**
	 * A local source string to play with
	 */
	String localSource = null;
	/**
	 * a local destination string to play with
	 */
	String localDestination = null;

	/**
	 * A connection with which to make files to transfer around
	 */
	static Connection fileTransferConn = null;

	/**
	 * A remote file handler test to utilize the file creation code already
	 * developed there
	 */
	RemoteFileHandlerTest fileCreator = new RemoteFileHandlerTest();

	/**
	 * After the tests run, disconnect the connections
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();
	}

	/**
	 * Set up some dummy local files to work with
	 * 
	 * @throws java.lang.Exception
	 */
	public void createLocalTempFile() throws Exception {

		fileCreator.createLocalSource();
		fileCreator.createLocalDestination();
		localSource = fileCreator.getSource();
		localDestination = fileCreator.getDestination();
	}

	/**
	 * Deletes the temporarily made local files since they are not useful
	 * 
	 * @throws java.lang.Exception
	 */
	public void deleteLocalTempFile() throws Exception {
		System.out.println("Delete temporary files/directories that were created.");
		fileCreator.deleteLocalSource();
		fileCreator.deleteLocalDestination();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandler#copy(java.lang.String, java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocalCopy() throws Exception {
		System.out.println("Testing testLocalCopy() function.");

		// Try to make a local temp file to play with
		createLocalTempFile();
		// Get the filename for testing exists later
		String separator = FileSystems.getDefault().getSeparator();
		String filename = localSource.substring(localSource.lastIndexOf(separator));

		FileHandler handler = null;
		handler = new LocalFileHandler();
		CommandStatus status = handler.copy(localSource, localDestination);
		assert (status == CommandStatus.SUCCESS);

		// Check that it exists
		try {
			boolean exist = handler.exists(localDestination + filename);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the local dummy file
		deleteLocalTempFile();

		System.out.println("Finished testing testLocalCopy() function.");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandler#move(java.lang.String, java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocalMove() throws Exception {
		System.out.println("Testing testLocalMove() function.");

		// Try to make a local temp file to play with
		createLocalTempFile();
		// Get the filename for testing exists later
		String separator = FileSystems.getDefault().getSeparator();
		String filename = localSource.substring(localSource.lastIndexOf(separator));
		FileHandler handler = null;
		handler = new LocalFileHandler();

		CommandStatus status = handler.move(localSource, localDestination);
		assert (status == CommandStatus.SUCCESS);

		// Delete the local dummy file
		deleteLocalTempFile();

		System.out.println("Finished testing testLocalMove() function.");

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandler#exists(java.lang.String)}.
	 */
	@Test
	public void testLocalExists() throws Exception {
		System.out.println("Testing testExists() function.");

		/**
		 * Test a temp file that was created
		 */

		// Create the temp file
		fileCreator.createLocalSource();
		localSource = fileCreator.getSource();

		FileHandler handler = new LocalFileHandler();
		assert (handler.exists(localSource));

		System.out.println("Testing testExists() with a non existing file");
		assert (!handler.exists("/usr/file_that_definitely_doesnot_exist.txt"));

		// delete the dummy file
		fileCreator.deleteLocalSource();

		System.out.println("Finished testing testExists()");
	}

	/**
	 * Function to execute the local file browsing and local directory browsing
	 * test. We call one main function so that a file structur can be created at the
	 * beginning, accessed by both "subtests", and then deleted at the end.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLocalBrowsing() throws IOException {
		String topDirectory = "fileBrowsingDir";

		Path topPath = createLocalFileStructure(topDirectory);

		testLocalFileBrowsing(topPath);

		testLocalDirectoryBrowsing(topPath);

		deleteLocalFileStructure(topPath);
	}

	/**
	 * Test for file browsing on local system
	 * 
	 * @throws IOException
	 */
	public void testLocalFileBrowsing(Path topPath) throws IOException {
		LocalFileHandler handler = new LocalFileHandler();

		ArrayList<String> files = handler.listFiles(topPath.toString());

		// four files were created
		assert (files.size() == 4);

		// Let's assert that each file we created is in the list
		for (int i = 0; i < files.size(); i++) {
			String file = files.get(i);
			// assert that the file is actually there
			assert(handler.exists(file));
			Path path = Paths.get(file);
			
			// assert that it is a file
			assert (Files.isRegularFile(path));
			
		}

	}

	/**
	 * Test for directory browsing on local system
	 */
	public void testLocalDirectoryBrowsing(Path topPath) throws IOException {
		LocalFileHandler handler = new LocalFileHandler();

		ArrayList<String> files = handler.listDirectories(topPath.toString());
		for (int i = 0; i < files.size(); i++) {
			System.out.println(files.get(i));
		}
		// Four directories total, including the top directory
		assert (files.size() == 4);

		// Let's assert that each directory we created is in the list
		for (int i = 0; i < files.size(); i++) {
			String file = files.get(i);
			// assert that it exists
			assert(handler.exists(file));
			
			// assert that it is a directory
			Path path = Paths.get(file);
			assert(Files.isDirectory(path));
		}
		
	}

	/**
	 * Create a local file structure for testing local file and directory browsing
	 * 
	 * @param topDir
	 */
	protected Path createLocalFileStructure(String topDir) throws IOException {
		// Create a destination path name
		Path destinationPath = null;
		// Try to create the path
		try {
			destinationPath = Files.createTempDirectory(topDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fullPath = destinationPath.toString();
		System.out.println("Created top directory at: " + fullPath);
		String separator = "/";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			separator = "\\";
		Files.createDirectory(Paths.get(fullPath + separator + "dir1"));
		Files.createDirectory(Paths.get(fullPath + separator + "dir2"));
		Files.createDirectory(Paths.get(fullPath + separator + "dir3"));

		Files.createFile(Paths.get(fullPath + separator + "filename.txt"));
		Files.createFile(Paths.get(fullPath + separator + "dir1" + separator + "otherfile.txt"));
		Files.createFile(Paths.get(fullPath + separator + "dir2" + separator + "newfile.txt"));
		Files.createFile(Paths.get(fullPath + separator + "dir2" + separator + "newotherfile.txt"));

		return destinationPath;
	}

	/**
	 * Delete the local file structure for testing local file and directory browsing
	 * 
	 * @param topDir
	 */
	protected void deleteLocalFileStructure(Path topPath) {
		// Delete the destination path that was created
		System.out.println("Deleting " + topPath.toString());
		try {
			Files.deleteIfExists(topPath);
		} catch (NoSuchFileException e) {
			System.err.format("%s: no such" + " file or directory%n", topPath);
			e.printStackTrace();
		} catch (DirectoryNotEmptyException e) {

			// If the directory is not empty, that is because it was a move command
			// and the moved file is in there. So delete the file first and then
			// delete the directory
			File localDestinationFile = new File(topPath.toString());
			boolean deleted = deleteLocalDirectory(localDestinationFile);

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
	 * A simple test method to recursively delete temporary files/directories
	 * created in this test class
	 * 
	 * @param directory - top level directory from which to delete everything
	 *                  underneath
	 * @return - boolean - true if everything deleted, false if not
	 */
	private boolean deleteLocalDirectory(File directory) {
		File[] contents = directory.listFiles();
		if (contents != null) {
			for (File file : contents) {
				deleteLocalDirectory(file);
			}
		}
		return directory.delete();
	}

}
