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
import java.nio.file.Paths;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
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

	/**
	 * A string for a source file to move or copy
	 */
	static String localSource;

	/**
	 * A string for a destination directory/file to move or copy the source to
	 */
	static String localDestination;
	/**
	 * A string with a new name for the move command to use
	 */
	static String newName = "/newfilename.txt";

	/**
	 * Main function to show an example of how the Commands API moves and copies
	 * files
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		executeLocalFileHandleExamples();

		executeRemoteFileHandleExamples();
	}

	/**
	 * This function executes the local file handle examples related to copying and
	 * moving a file. Dummy temporary files are created to move around, and then
	 * deleted at the end.
	 */
	public static void executeLocalFileHandleExamples() {
		// Copy a file to a new directory, from start to finish
		// i.e. create file, copy it to destination, then delete the temporary files
		copyLocalFile();

		// Move the file to the same directory with a new name
		moveLocalFile();

		return;
	}

	/**
	 * Function that shows an example of the full workflow for a local copy command.
	 * If one doesn't need the files/destination, then the copyFileLocally function
	 * is the only relevant function
	 */
	public static void copyLocalFile() {
		// Create a temporary dummy file locally to play around with
		createDummyLocalFile();

		// Create a temporary dummy destination locally to move it to
		createDummyLocalDestination();

		// Copy the file to the directory
		copyFileLocally();

		// Delete the created file and directory
		cleanUpLocalFiles();
	}

	/**
	 * Function that shows an example of the full workflow for a local move command.
	 * If one doesn't need the files/destination, then the moveFileLocally function
	 * is the only relevant function
	 */
	public static void moveLocalFile() {
		// Create a dummy local file to move
		createDummyLocalFile();

		// Create a dummy local destination to move the file to
		createDummyLocalDestination();

		// Move it
		moveFileLocally();

		// Delete the remaining files that we don't need
		cleanUpLocalFiles();
	}

	/**
	 * This function executes the remote file handle examples related to copying and
	 * moving a file. Dummy temporary files are created to move around, and then
	 * deleted at the end. Note that in some senses, remote moving and copying are
	 * the same. This is only true for local --> remote or vice versa file
	 * transfers. The file handling API also allows for remote --> remote moving or
	 * copying, where, at the moment, the remote host is the same. Nonetheless, in
	 * this case, moving and copying on a single file system are obviously
	 * different.
	 * 
	 * Note also that these functions show just one example of moving or copying.
	 * For a full set of examples, see
	 * {@link org.eclipse.ice.commands.IFileHandlerFactoryTest} and associated
	 * functions. In all cases the API looks the same to the user, but it may be
	 * instructive to look at the tests available in the above class.
	 */
	public static void executeRemoteFileHandleExamples() {

		// Create a dummy local file to move
		createDummyLocalFile();

		// This example uses a dummy ssh connection established for running the tests
		// in the commands package. Here we just use an already created directory on
		// this host
		copyFileRemotely();

		// Move the file to the same directory with a new name
		// We don't need to make a new file since remote sftps don't delete the original
		// file off of the host machine
		moveRemoteFile();

		return;
	}

	/**
	 * This shows the remote copy file API functionality, and moves a dummy text
	 * file to a directory on the dummy ssh remote host with a new name.
	 */
	public static void moveRemoteFile() {

		// Get the dummy connection configuration with the credentials
		ConnectionConfiguration configuration = makeConnectionConfiguration();

		// Get the filename of the dummy file
		String filename = localSource.substring(localSource.lastIndexOf("/") + 1);

		// Get the file handler factory to create the transfer
		FileHandlerFactory factory = new FileHandlerFactory();

		IFileHandler handler = null;
		CommandStatus status = null;

		try {
			// Get the handler for this particular connection configuration
			handler = factory.getFileHandler(configuration);
			// move the file
			status = handler.move(localSource, "/tmp/exampleDirectory/newfile" + filename);
			// Check it exists (handler.move actually does this also, but this is an
			// example)
			assert (handler.exists("/tmp/exampleDirectory/newfile" + filename));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This shows the remote copy file API functionality, and copies a dummy text
	 * file to a directory on the dummy ssh remote host.
	 */
	public static void copyFileRemotely() {
		// Get the dummy connection configuration with the credentials
		ConnectionConfiguration configuration = makeConnectionConfiguration();

		// Get the filename of the dummy file
		String filename = localSource.substring(localSource.lastIndexOf("/"));

		// Get the file handler factory to create the transfer
		FileHandlerFactory factory = new FileHandlerFactory();

		IFileHandler handler = null;
		CommandStatus status = null;

		try {
			// Get the handler for this particular connection configuration
			handler = factory.getFileHandler(configuration);
			// copy the file
			status = handler.copy(localSource, "/tmp/exampleDirectory/");
			// Check it exists (handler.copy actually does this also, but this is an
			// example)
			assert (handler.exists("/tmp/exampleDirectory" + filename));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This function copies a test file to a test directory locally. It demonstrates
	 * the use of the FileHandler API within the Commands API
	 */
	public static void copyFileLocally() {
		// Make the factory and handler which will deal with file moving
		FileHandlerFactory factory = new FileHandlerFactory();
		IFileHandler handler = null;

		// Make a connection configuration and set the host to local
		ConnectionConfiguration cfg = new ConnectionConfiguration();
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to
		// "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		cfg.setAuthorization(auth);

		// Get the appropriate remote/local FileHandler
		try {
			handler = factory.getFileHandler(cfg);
			// Do the copying
			CommandStatus status = handler.copy(localSource, localDestination);
			// Check that the copy was completed successfully
			if (status != CommandStatus.SUCCESS) {
				System.out.println("Copy file failed! Check console for error messages");
			}
			// You can also check if a file exists if desired
			boolean exist = handler.exists(localDestination);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This function moves a test file to a test directory locally and gives it a
	 * new name. It demonstrates the use of the FileHandler API within the Commands
	 * API
	 * 
	 * @throws IOException
	 */
	public static void moveFileLocally() {

		// Get the handler from the file handling factory
		FileHandlerFactory factory = new FileHandlerFactory();
		IFileHandler handler = null;

		// Make a connection configuration and set the host to local
		ConnectionConfiguration cfg = new ConnectionConfiguration();
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to
		// "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		cfg.setAuthorization(auth);

		// Get the appropriate remote/local FileHandler
		try {
			handler = factory.getFileHandler(cfg);
			// Do the actual file moving
			CommandStatus status = handler.move(localSource, localDestination);

			// Ensure that that the status indicates the move was successful
			if (status != CommandStatus.SUCCESS) {
				System.out.println("Move file failed! Check console for error messages");
			}
			// Check that it exists
			String filename = localSource.substring(localSource.lastIndexOf("/"));
			boolean exist = handler.exists(localDestination + filename);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This function creates a dummy temporary file locally for the copy and move
	 * commands to use.
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
	}

	/**
	 * This function creates a dummy temporary directory to copy/move the local file
	 * to.
	 */
	public static void createDummyLocalDestination() {
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
	}

	/**
	 * This function cleans up the temporary files and directory created
	 */
	public static void cleanUpLocalFiles() {
		boolean deleteDestination = false;
		Path sourcePath = Paths.get(localSource);

		// If source path exists, delete it
		if (Files.exists(sourcePath)) {
			try {
				Files.delete(sourcePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Don't need to delete the source file since we moved it, so by definition the
		// temporary file is no longer at it's source location
		deleteDestination = deleteDirectory(new File(localDestination));
		if (!deleteDestination) {
			System.out.println("Couldn't delete destination file/directory at: " + localDestination);
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

	/**
	 * Dummy function which makes the connection configuration for the dummy remote
	 * ssh connection. This way functions can grab the configuration at will with
	 * one line of code.
	 * 
	 * @return
	 */
	private static ConnectionConfiguration makeConnectionConfiguration() {
		// Set the connection configuration to a dummy remote connection
		ConnectionConfiguration config = new ConnectionConfiguration();

		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text",
				"/tmp/ice-remote-creds.txt");
		// Set it
		config.setAuthorization(auth);

		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		config.setName("dummyConnection");

		config.setDeleteWorkingDirectory(true);

		return config;
	}
}
