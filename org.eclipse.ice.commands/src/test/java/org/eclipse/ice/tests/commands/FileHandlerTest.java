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
import java.util.Scanner;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.FileHandler;
import org.eclipse.ice.commands.LocalFileHandler;
import org.eclipse.ice.commands.RemoteFileHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.FileHandler}.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class FileHandlerTest {
	/**
	 * A local source string to play with
	 */
	String localSource = null;
	/**
	 * a local destination string to play with
	 */
	String localDestination = null;

	/**
	 * A FileHandlerFactoryTest instance to take advantage of all the file
	 * creation/deletion functionality available
	 */
	static private IFileHandlerFactoryTest factory = new IFileHandlerFactoryTest();

	/**
	 * Run before the test to setup the connection information for the
	 * IFileHandlerFactoryTest, so that we can take advantage of all of the file
	 * creation/deletion code
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Set up the dummy connection
		ConnectionConfiguration config = makeConnectionConfiguration();

		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		// Open the connection
		manager.openConnection(config);

		// Give the IFileHandlerFactoryTest class the connection information
		// so that we can use all of the file creation/deletion code that is in
		// there already. This way we can use the same connection throughout
		// the test code
		factory.setConnection(manager.getConnection(config.getName()));
		factory.setConnectionConfiguration(config);

	}

	/**
	 * After the tests run, disconnect the connections so that each time we are
	 * starting with a fresh connection, and thus testing all steps of the file
	 * handling
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		manager.closeAllConnections();
	}

	/**
	 * This function deletes all of the connections in the connection manager once
	 * the tests have run and completed.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		manager.removeAllConnections();
	}

	/**
	 * Set up some dummy local files to work with
	 * 
	 * @throws java.lang.Exception
	 */
	public void createLocalTempFile() throws Exception {

		factory.createLocalSource();
		factory.createLocalDestination();
		localSource = factory.getSource();
		localDestination = factory.getDestination();
	}

	/**
	 * Deletes the temporarily made local files since they are not useful
	 * 
	 * @throws java.lang.Exception
	 */
	public void deleteLocalTempFile() throws Exception {
		System.out.println("Delete temporary files/directories that were created.");
		factory.deleteLocalSource();
		factory.deleteLocalDestination();
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
		String filename = localSource.substring(localSource.lastIndexOf("/"));
		
		FileHandler handler = null;
		try {
			handler = new LocalFileHandler();
			CommandStatus status = handler.copy(localSource, localDestination);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		String filename = localSource.substring(localSource.lastIndexOf("/"));
		FileHandler handler = null;
		try {
			handler = new LocalFileHandler();
			
			CommandStatus status = handler.move(localSource, localDestination);
			assert (status == CommandStatus.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check that it exists
		try {
			boolean exist = handler.exists(localDestination + filename);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the local dummy file
		deleteLocalTempFile();

		System.out.println("Finished testing testLocalMove() function.");

	}

	/**
	 * This function tests remote to remote file handling. Only remote to remote
	 * copying needs an additional test, because the functionality is identical
	 * except for the remote-to-remote handling
	 */
	@Test
	public void testRemoteToRemoteCopy() throws Exception {
		factory.createRemoteSource();
		factory.createRemoteDestination();

		ConnectionConfiguration config = makeConnectionConfiguration();
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(config);

		String src = factory.getSource();
		String dest = factory.getDestination();
		// Get the filename to check for existence
		String filename = src.substring(src.lastIndexOf("/"));

		// Now try to copy the file
		CommandStatus status = handler.copy(src, dest);

		assert (status == CommandStatus.SUCCESS);

		// Check that the file exists now
		assert (handler.exists(dest + filename));

		factory.deleteRemoteDestination();
		factory.deleteRemoteSource();

	}

	/**
	 * This function tests remote to remote file handling
	 */
	@Test
	public void testRemoteToRemoteMove() throws Exception {
		factory.createRemoteSource();
		factory.createRemoteDestination();

		ConnectionConfiguration config = makeConnectionConfiguration();
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(config);

		String src = factory.getSource();
		String dest = factory.getDestination();
		// Get the filename to check for existence
		String filename = src.substring(src.lastIndexOf("/"));

		// Now try to move the file
		CommandStatus status = handler.move(src, dest);

		assert (status == CommandStatus.SUCCESS);

		// Check that the file exists now
		assert (handler.exists(dest + filename));

		factory.deleteRemoteDestination();
		factory.deleteRemoteSource();

	}

	/**
	 * Test method for testing remote moving capabilities when moving a remote file
	 * to the local host. Also tests throwing an exception for a nonexistent local
	 * directory
	 */
	@Test
	public void testRemoteToLocalMove() throws Exception {

		// Make a remote file to play with and a local directory to move it to
		factory.createRemoteSource();
		factory.createLocalDestination();

		ConnectionConfiguration config = makeConnectionConfiguration();
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(config);

		String src = factory.getSource();
		String dest = factory.getDestination();
		// Get the filename to check for existence
		String filename = src.substring(src.lastIndexOf("/"));

		// Now try to move the file
		CommandStatus status = handler.move(src, dest);

		assert (status == CommandStatus.SUCCESS);

		// Check that the file exists now
		assert (handler.exists(dest + filename));

		factory.deleteLocalDestination();

		// Keep the same source, but add a new subdirectory and file name for the
		// destination.
		// This test checks if an error is thrown for a nonexistent local directory
		// The API can't just make the local directory if it isn't found, because
		// otherwise there is no way to differentiate between a remote --> local and
		// remote --> remote move. Thus, it is up to the user to ensure that their
		// local destination directory exists
		dest = factory.getDestination() + "/newDirectory/newFilename.txt";
		try {
			status = handler.move(src, dest);
		} catch (IOException e) {
			System.out.println("Expected exception thrown. Continue test.");
		}

		factory.deleteRemoteSource();

	}

	/**
	 * Test method for testing remote moving capabilities when moving a local file
	 * to a remote host
	 */
	@Test
	public void testLocalToRemoteMove() throws Exception {

		// Make a local test file to play with
		// Make a remote destination directory to move to

		factory.createLocalSource();
		factory.createRemoteDestination();

		// Get the dummy connection configuration from the helper function
		// which allows reusing the dummy config code
		ConnectionConfiguration config = makeConnectionConfiguration();
		// Get the remote file handler
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(config);

		String theSource = factory.getSource();
		String theDestination = factory.getDestination();

		// Now try to move the file
		CommandStatus status = handler.move(theSource, theDestination);
		assert (status == CommandStatus.SUCCESS);

		// Check that the file exists now
		assert (handler.exists(theDestination));

		// Lets try a file move also where we change the name of the file
		// and add an additional directory
		theDestination = factory.getDestination() + "newDirectory/newFileName.txt";
		status = handler.move(theSource, theDestination);

		assert (status == CommandStatus.SUCCESS);

		assert (handler.exists(theDestination));

		// Delete the test file/directory now that the test is finished
		factory.deleteLocalSource();
		factory.deleteRemoteDestination();

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
		factory.createLocalSource();
		localSource = factory.getSource();

		FileHandler handler = new LocalFileHandler();
		assert (handler.exists(localSource));

		System.out.println("Testing testExists() with a non existing file");
		assert (!handler.exists("/usr/file_that_definitely_doesnot_exist.txt"));

		// delete the dummy file
		factory.deleteLocalSource();

		System.out.println("Finished testing testExists()");
	}

	/**
	 * Function which checks nonexistent source exception throwing for both local
	 * and remote file handlers
	 * 
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testNonexistentSource() throws Exception {
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(makeConnectionConfiguration());

		handler.move("/non/existent/source/", "/non/existent/destination/");

	}

	/**
	 * Test the exists function for remote file handlers
	 */
	@Test
	public void testRemoteExists() throws Exception {
		System.out.println("Testing remote exists function");

		// Set up the connection first to create the file
		factory.createRemoteSource();

		String theSource = factory.getSource();

		// Get the dummy configuration
		ConnectionConfiguration config = makeConnectionConfiguration();

		// Make the remote file handler with the dummy configuration
		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(config);

		// Check two asserts - that the created file exists, and that
		// some other nonexistent file throws an error
		assert (handler.exists(theSource));
		assert (!handler.exists("/some/nonexistent/path/file.txt"));
		// Done with the remote source, delete it
		factory.deleteRemoteSource();

		// check and see if a nonexistent file remotely, which does exist locally,
		// returns true
		factory.createLocalSource();
		theSource = factory.getSource();
		assert (handler.exists(theSource));

		// Done with the local source, delete it
		factory.deleteLocalSource();
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

		ConnectionConfiguration config = new ConnectionConfiguration();
		// Make the connection configuration
		config.setHostname(hostname);
		config.setUsername(username);
		config.setPassword(password);
		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		config.setName("dummyConnection");

		config.setDeleteWorkingDirectory(true);

		return config;
	}

}
