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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Scanner;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.FileHandlerFactory;
import org.eclipse.ice.commands.IFileHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * This class implements several test methods for
 * {@link org.eclipse.ice.commands.FileHandlerFactory} and its use for file
 * transfers.
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
	String theSource = "";

	/**
	 * A string which contains a local temporary destination directory to work with
	 */
	String theDestination = "";

	/**
	 * A connection for remote file handling tests
	 */
	static Connection dummyConnection = new Connection();

	/**
	 * Create some getters and setters so that other tests can take advantage of the
	 * functions here for making and deleting local and remote files
	 */
	/**
	 * Getter for the connection
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return dummyConnection;
	}

	/**
	 * Getter for the source file string
	 * 
	 * @return
	 */
	public String getSource() {
		return theSource;
	}

	/**
	 * Getter for the destination file string
	 * 
	 * @return
	 */
	public String getDestination() {
		return theDestination;
	}

	/**
	 * Setter for the connection
	 * 
	 * @return
	 */
	public void setConnection(Connection _conn) {
		dummyConnection = _conn;
	}

	/**
	 * Setter for the source file string
	 * 
	 * @return
	 */
	public void setSource(String src) {
		theSource = src;
	}

	/**
	 * Setter for the destination file string
	 * 
	 * @return
	 */
	public void setDestination(String dest) {
		theDestination = dest;
	}

	/**
	 * This function runs before the class execution, and it's primary use is to
	 * establish the remote connection for remote file handling tests
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConnectionConfiguration config = new ConnectionConfiguration();
		// Set the connection configuration to a dummy remote connection
		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// Scan line by line
		scanner.useDelimiter("\n");

		// Get the credentials for the dummy remote account
		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		// Make the connection configuration
		config.setHostname(hostname);
		config.setUsername(username);
		config.setPassword(password);
		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		config.setName("dummyConnection");
		// We'll delete the files manually since there will be many directories involved
		config.setDeleteWorkingDirectory(false);

		dummyConnection = ConnectionManager.openConnection(config);
	}

	/**
	 * This setup creates a test dummy file to transfer around. It creates the file
	 * by default locally.
	 * 
	 * @throws java.lang.Exception
	 */
	public void createLocalSource() throws Exception {

		// First create a dummy text file to test
		String source = "dummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Turn the path into a string to pass to the command
		theSource = sourcePath.toString();
		System.out.println("Created source file at: " + theSource);

	}

	/**
	 * This setup function creates a test local destination path to transfer a file
	 * to.
	 * 
	 * @throws Exception
	 */
	public void createLocalDestination() throws Exception {

		// Create a destination path name
		Path destinationPath = null;
		String dest = "testDirectory";
		// Try to create the path
		try {
			destinationPath = Files.createTempDirectory(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Turn the path into a string to give to the command
		theDestination = destinationPath.toString();
		System.out.println("Created destination file at: " + theDestination);

	}

	/**
	 * This function deletes/removes a test dummy file after the test has finished.
	 * 
	 * @throws java.lang.Exception
	 */

	public void deleteLocalSource() throws Exception {
		System.out.println("Delete temporary files/directories that were created.");

		// Get the paths
		Path localSourcePath = Paths.get(theSource);

		// Delete the source file that was created
		try {
			Files.deleteIfExists(localSourcePath);
		} catch (NoSuchFileException e) {
			System.err.format("%s: no such" + " file or directory%n", localSourcePath);
			e.printStackTrace();
		} catch (DirectoryNotEmptyException e) {
			System.err.format("%s not empty%n", localSourcePath);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		System.out.println("Successfully deleted local source file");
	}

	/**
	 * This function deletes/removes a test dummy destination directory after the
	 * test has finished.
	 * 
	 * @throws java.lang.Exception
	 */
	public void deleteLocalDestination() throws Exception {
		// Get the path that was created
		Path localDestPath = Paths.get(theDestination);
		// Delete the destination path that was created
		try {
			Files.deleteIfExists(localDestPath);
		} catch (NoSuchFileException e) {
			System.err.format("%s: no such" + " file or directory%n", localDestPath);
			e.printStackTrace();
		} catch (DirectoryNotEmptyException e) {

			// If the directory is not empty, that is because it was a move command
			// and the moved file is in there. So delete the file first and then
			// delete the directory
			File localDestinationFile = new File(theDestination);
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
	 * This function creates a remote source file to move/copy
	 * 
	 * @throws Exception
	 */
	public void createRemoteSource() throws Exception {
		ChannelSftp sftpChannel = (ChannelSftp) dummyConnection.getSession().openChannel("sftp");
		sftpChannel.connect();

		String remoteDest = "/tmp/remoteFileHandlerSource/";

		// Check if the directory already exists
		SftpATTRS attrs = null;
		try {
			attrs = sftpChannel.lstat(remoteDest);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
		}
		if (attrs == null) {
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(remoteDest);
		}

		// Create a local source file since JSch doesn't have a way to make a dummy
		// file
		createLocalSource();

		// Get the filename by splitting the path by "/"
		String[] tokens = theSource.split("/");

		// Get the last index of tokens, which will be the filename
		String filename = tokens[tokens.length - 1];

		// Move it to the remote host
		sftpChannel.put(theSource, remoteDest);

		// Now set the source file name to the new location at the remote destination
		theSource = remoteDest + filename;

		// Delete the local directory that was created since it is no longer needed
		Path path = Paths.get(remoteDest);
		try {
			Files.deleteIfExists(path);
		} catch (NoSuchFileException e) {
			// If somehow the directory doesn't exist, indicate so
			System.err.format("%s: somehow this path doesn't exist... no such" + " file or directory%n", path);
			e.printStackTrace();
		}

		System.out.println("Created remote source file at : " + theSource);
		// Disconnect the channel when finished
		sftpChannel.disconnect();

	}

	/**
	 * This function deletes the remote source that was created for testing
	 * 
	 * @throws Exception
	 */
	public void deleteRemoteSource() throws Exception {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = (ChannelSftp) dummyConnection.getSession().openChannel("sftp");
		sftpChannel.connect();

		// Get the path to the source file
		String[] tokens = theSource.split("/");
		String sourcePath = "";
		// Build the source path
		for (int i = 0; i < tokens.length - 1; i++)
			sourcePath += tokens[i] + "/";

		// Recursively delete the source directory and its contents
		deleteRemoteDirectory(sftpChannel, sourcePath);

		System.out.println("Deleted remote source directory at : " + sourcePath);

		// Disconnect the channel
		sftpChannel.disconnect();
	}

	/**
	 * This function creates a remote directory to move/copy a file
	 * 
	 * @throws Exception
	 */
	public void createRemoteDestination() throws Exception {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = (ChannelSftp) dummyConnection.getSession().openChannel("sftp");
		sftpChannel.connect();

		String remoteDest = "/tmp/remoteFileHandlerDestination/";

		// Check if the directory already exists
		SftpATTRS attrs = null;
		try {
			attrs = sftpChannel.lstat(remoteDest);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
		}
		if (attrs == null) {
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(remoteDest);
		}

		// Set the global variable
		theDestination = remoteDest;

		System.out.println("Created remote destination at : " + theDestination);

		// Disconnect channel
		sftpChannel.disconnect();

	}

	/**
	 * This function deletes the remote destination that was created for testing
	 * 
	 * @throws Exception
	 */
	public void deleteRemoteDestination() throws Exception {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = (ChannelSftp) dummyConnection.getSession().openChannel("sftp");
		sftpChannel.connect();

		System.out.println("Deleting remote destination at : " + theDestination);
		// Recurisvely delete the directory and its contents
		deleteRemoteDirectory(sftpChannel, theDestination);

		// Disconnect channel
		sftpChannel.disconnect();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * local file copying.
	 */
	@Test
	public void testLocalFileHandlerFactoryCopyCommand() {
		// Make a local test file to play with
		try {
			createLocalSource();
			createLocalDestination();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to copy the file
		try {
			CommandStatus status = handler.copy(theSource, theDestination);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(theDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the test file/directory now that the test is finished
		try {
			deleteLocalSource();
			deleteLocalDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * local file moving.
	 */
	@Test
	public void testLocalFileHandlerFactoryMoveCommand() {

		// Make a local test file to play with
		try {
			createLocalSource();
			createLocalDestination();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to move the file
		try {
			CommandStatus status = handler.move(theSource, theDestination);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(theDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the test file/directory now that the test is finished
		try {
			deleteLocalSource();
			deleteLocalDestination();
		} catch (Exception e) {
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

		// Make a local test file to play with
		try {
			createLocalSource();
			createLocalDestination();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;
		// Make a new subdirectory to put the file into
		String newDirectory = "/some/new/directory/";

		// Get the file transfer handler with a nonexistent destination
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to move the file
		try {
			CommandStatus status = handler.move(theSource, theDestination + newDirectory);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(theDestination + newDirectory);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the test file/directory now that the test is finished
		try {
			deleteLocalSource();
			deleteLocalDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method intends to test the functionality of moving a new file not to a
	 * new directory but just to a new name in the same directory
	 */
	@Test
	public void testLocalFileHandlerFactoryChangeName() {
		// Make a local test file to play with
		try {
			createLocalSource();
			createLocalDestination();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;

		// Make the new file name be the same file in the same directory, just a new
		// name
		String[] tokens = theSource.split("/");
		String localNewName = "";
		for (int i = 0; i < tokens.length - 1; i++)
			localNewName += tokens[i] + "/";

		localNewName += "NewFileName.txt";

		System.out.println("New file path: " + localNewName);

		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			CommandStatus status = handler.move(theSource, localNewName);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that the file exists now
		try {
			boolean exist = handler.exists(localNewName);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If the file was successfully created, delete it here
		// Needs a special delete since the filename was created in this function
		File fileToDelete = new File(localNewName);
		fileToDelete.delete();

		// Delete the remaining directory now that the test is finished
		try {
			deleteLocalSource();
			deleteLocalDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * remote file copying.
	 */
	@Test
	public void testRemoteFileHandlerCopyCommand() {
		// Make a local test file to play with
		// Make a remote destination directory to copy to
		try {
			createLocalSource();
			createRemoteDestination();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to copy the file
		try {
			CommandStatus status = handler.copy(theSource, theDestination);
			assert (status == CommandStatus.SUCCESS);

			// Check that the file exists now
			boolean exist = handler.exists(theDestination);
			assert (exist == true);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the test file/directory now that the test is finished
		try {
			deleteLocalSource();
			deleteRemoteDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * remote file moving.
	 */
	@Test
	public void testRemoteFileHandlerMoveCommand() {
		// Make a local test file to play with
		// Make a remote destination directory to move to
		try {
			createLocalSource();
			createRemoteDestination();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to move the file
		try {
			CommandStatus status = handler.move(theSource, theDestination);
			assert (status == CommandStatus.SUCCESS);

			// Check that the file exists now
			boolean exist = handler.exists(theDestination);
			assert (exist == true);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the test file/directory now that the test is finished
		try {
			// Source file is already "deleted" since it was moved
			deleteRemoteDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * remote file moving where the remote directory doesn't exist.
	 */
	/**
	* 
	*/
	@Test
	public void testRemoteFileHandlerFactoryDestinationNonExistant() {
		// Make a local test file to play with
		try {
			createLocalSource();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		IFileHandler handler = null;

		// Get the file transfer handler
		try {
			handler = factory.getFileHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now try to move the file to some new directory
		String newDirectory = "/some/new/directory";
		try {
			CommandStatus status = handler.move(theSource, theDestination + newDirectory);
			assert (status == CommandStatus.SUCCESS);

			// Check that the file exists now
			boolean exist = handler.exists(theDestination);
			assert (exist == true);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the test file/directory now that the test is finished
		try {
			// Source file is already "deleted" since it was moved
			deleteRemoteDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteRemoteDirectory(ChannelSftp sftpChannel, String path) throws SftpException {

		// Get the path's directory structure
		Collection<ChannelSftp.LsEntry> fileList = sftpChannel.ls(path);

		// Iterate through the list to get the file/directory names
		for (ChannelSftp.LsEntry file : fileList) {
			// If it isn't a directory delete it
			if (!file.getAttrs().isDir()) {
				sftpChannel.rm(path + "/" + file.getFilename());
			} else if (!(".".equals(file.getFilename()) || "..".equals(file.getFilename()))) { // If it is a subdir.
				// Otherwise its a subdirectory, so try deleting it
				try {
					// remove the sub directory
					sftpChannel.rmdir(path + "/" + file.getFilename());
				} catch (Exception e) {
					// If the subdirectory is not empty, then iterate with this
					// subdirectory to remove the contents
					deleteRemoteDirectory(sftpChannel, path + "/" + file.getFilename());
				}
			}
		}
		sftpChannel.rmdir(path); // delete the parent directory after empty
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
