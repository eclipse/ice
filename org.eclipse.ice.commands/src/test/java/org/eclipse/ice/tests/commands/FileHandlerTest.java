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
import org.eclipse.ice.commands.FileHandler;
import org.eclipse.ice.commands.LocalFileHandler;
import org.eclipse.ice.commands.RemoteFileHandler;
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
	IFileHandlerFactoryTest factory = new IFileHandlerFactoryTest();

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
		System.out.println("Created source file at: " + localSource);
		System.out.println("Created destination file at: " + localDestination);
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
	//@Test
	public void testLocalCopy() throws Exception {
		System.out.println("Testing testLocalCopy() function.");

		// Try to make a local temp file to play with
		createLocalTempFile();

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
			boolean exist = handler.exists(localDestination);
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
	//@Test
	public void testLocalMove() throws Exception {
		System.out.println("Testing testLocalMove() function.");

		// Try to make a local temp file to play with
		createLocalTempFile();

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
			boolean exist = handler.exists(localDestination);
			assert (exist == true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete the local dummy file
		deleteLocalTempFile();

		System.out.println("Finished testing testLocalMove() function.");

	}

	/**
	 * Test method for testing remote moving capabilities
	 */
	@Test
	public void testRemoteMove() throws Exception {

		// Make a local test file to play with
		// Make a remote destination directory to move to
		IFileHandlerFactoryTest.setUpBeforeClass(); // Setup the connection
		factory.createLocalSource();
		factory.createRemoteDestination();

		// Get the dummy connection configuration
		ConnectionConfiguration config = makeConnectionConfiguration();
		// Get the remote file handler
		RemoteFileHandler handler = new RemoteFileHandler(config);

		String theSource = factory.getSource();
		String theDestination = factory.getDestination();

		// Now try to move the file
		CommandStatus status = handler.move(theSource, theDestination);
		assert (status == CommandStatus.SUCCESS);

		// Check that the file exists now
		assert (handler.exists(theDestination));

		// Lets try a file move also where we change the name of the file
		theDestination = factory.getDestination() + "newFileName.txt";
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
	//@Test
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
	 * Test the exists function for remote file handlers
	 */
	//@Test
	public void testRemoteExists() throws Exception {
		System.out.println("Testing remote exists function");

		// Set up the connection first to create the file
		IFileHandlerFactoryTest.setUpBeforeClass();
		factory.createRemoteSource();

		String theSource = factory.getSource();

		ConnectionConfiguration config = makeConnectionConfiguration();

		FileHandler handler = new RemoteFileHandler(config);

		assert (handler.exists(theSource));
		assert (!handler.exists("/some/nonexistent/path/file.txt"));

		factory.deleteRemoteSource();

	}

	/**
	 * Dummy function which makes the connection configuration for the dummy remote
	 * ssh connection. This way functions can grab the configuration at will with
	 * one line of code.
	 * 
	 * @return
	 */
	private ConnectionConfiguration makeConnectionConfiguration() {
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
