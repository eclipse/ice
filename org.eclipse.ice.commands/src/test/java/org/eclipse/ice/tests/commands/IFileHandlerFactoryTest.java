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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.FileHandler;
import org.eclipse.ice.commands.FileHandlerFactory;
import org.eclipse.ice.commands.HandleType;
import org.eclipse.ice.commands.IFileHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class implements several test methods for
 * {@link org.eclipse.ice.commands.FileHandlerFactory} and its use for file
 * transfers. The test methods are split up between local and remote file
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
	 * A string which contains a local temporary source file to work with
	 */
	String theSource = "";

	/**
	 * A string which contains a local temporary destination directory to work with
	 */
	String theDestination = "";

	/**
	 * A remote file handler test to take advantage of the file creation/deletion
	 * code and remote connection establishment code already developed there.
	 */
	RemoteFileHandlerTest fileCreator = new RemoteFileHandlerTest();

	/**
	 * Establish the connection first
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		RemoteFileHandlerTest.setUpBeforeClass();
	}

	/**
	 * This function deletes all of the connections in the connection manager once
	 * the tests have run and completed. Run it to clear the connection manager out
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		manager.removeAllConnections();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * local file copying.
	 */
	@Test
	public void testLocalFileHandlerFactoryCopyCommand() throws Exception {
		// Make a local test file to play with
		fileCreator.createLocalSource();
		fileCreator.createLocalDestination();

		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		IFileHandler handler = null;
		// Make a "local" connection configuration
		ConnectionConfiguration config = new ConnectionConfiguration();
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		config.setAuthorization(auth);

		// Get the file transfer handler
		handler = factory.getFileHandler(config);

		// Now try to copy the file
		CommandStatus status = handler.copy(theSource, theDestination);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(theDestination);
		assertTrue(exist);

		// Delete the test file/directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteLocalDestination();

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * local file moving.
	 */
	@Test
	public void testLocalFileHandlerFactoryMoveCommand() throws Exception {

		// Make a local test file to play with

		fileCreator.createLocalSource();
		fileCreator.createLocalDestination();
		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		IFileHandler handler = null;

		// Make a "local" connection configuration
		ConnectionConfiguration config = new ConnectionConfiguration();
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		config.setAuthorization(auth);

		// Get the file transfer handler
		handler = factory.getFileHandler(config);

		// Now try to move the file
		CommandStatus status = handler.move(theSource, theDestination);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(theDestination);
		assertTrue(exist);

		// Delete the test file/directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteLocalDestination();

	}

	/**
	 * Test method for a source file that exists but a destination directory that
	 * does not exist. Tests
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()}
	 */
	@Test
	public void testLocalFileHandlerFactoryDestinationNonexistent() throws Exception {
		// Make a local test file to play with
		fileCreator.createLocalSource();
		fileCreator.createLocalDestination();

		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		IFileHandler handler = null;
		// Make a new subdirectory to put the file into
		String newDirectory = "/some/new/directory/";

		// Make a "local" connection configuration
		ConnectionConfiguration config = new ConnectionConfiguration();
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		config.setAuthorization(auth);

		// Get the file transfer handler with a nonexistent destination
		handler = factory.getFileHandler(config);

		// Now try to move the file
		CommandStatus status = handler.move(theSource, theDestination + newDirectory);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(theDestination + newDirectory);
		assertTrue(exist);

		// Delete the test file/directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteLocalDestination();

	}

	/**
	 * This method intends to test the functionality of moving a new file not to a
	 * new directory but just to a new name in the same directory
	 */
	@Test
	public void testLocalFileHandlerFactoryChangeName() throws Exception {
		// Make a local test file to play with
		fileCreator.createLocalSource();
		fileCreator.createLocalDestination();

		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		System.out.println("Test local file handler factory change name");
		IFileHandler handler = null;

		// Make the new file name be the same file in the same directory, just a new
		// name
		String localNewName = theSource.substring(0, theSource.lastIndexOf("/") + 1);
		localNewName += "NewFileName.txt";

		System.out.println("New file path: " + localNewName);

		// Make a "local" connection configuration
		ConnectionConfiguration config = new ConnectionConfiguration();
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Get the authorization type. In this case, local, which is basically
		// equivalent to "no authorization"
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("local");
		// Set the connectionConfig to have access to e.g. the hostname
		config.setAuthorization(auth);

		handler = factory.getFileHandler(config);

		CommandStatus status = handler.move(theSource, localNewName);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(localNewName);
		assertTrue(exist);

		// If the file was successfully created, delete it here
		// Needs a special delete since the filename was created in this function
		File fileToDelete = new File(localNewName);
		fileToDelete.delete();

		// Delete the remaining directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteLocalDestination();

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * remote file copying.
	 */
	@Test
	public void testRemoteFileHandlerCopyCommand() throws Exception {

		fileCreator.createLocalSource();
		fileCreator.createRemoteDestination();
		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		// Get the file transfer handler
		IFileHandler handler = factory.getFileHandler(fileCreator.getConnection().getConfiguration());
		String separator = FileSystems.getDefault().getSeparator();
		String filename = theSource.substring(theSource.lastIndexOf(separator));
		// Now try to copy the file

		CommandStatus status = handler.copy(theSource, theDestination);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(theDestination + filename);
		assertTrue(exist);

		// Delete the test file/directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteRemoteDestination();

	}

	/**
	 * Test method for setting the type of file handle type for remote file handlers
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSetHandleType() throws Exception {

		fileCreator.createLocalSource();
		fileCreator.createRemoteDestination();

		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();
		String separator = FileSystems.getDefault().getSeparator();
		String filename = theSource.substring(theSource.lastIndexOf(separator));

		FileHandler handler = factory.getFileHandler(fileCreator.getConnection().getConfiguration());

		handler.setHandleType(HandleType.localRemote);
		CommandStatus status = handler.copy(theSource, theDestination);

		assertEquals(CommandStatus.SUCCESS, status);

		assertTrue(handler.exists(theDestination + filename));

		fileCreator.deleteLocalSource();
		fileCreator.deleteRemoteDestination();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * remote file moving.
	 */
	@Test
	public void testRemoteFileHandlerMoveCommand() throws Exception {
		// Make a local test file to play with
		fileCreator.createLocalSource();
		fileCreator.createRemoteDestination();
		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		// Get the file transfer handler
		IFileHandler handler = factory.getFileHandler(fileCreator.getConnection().getConfiguration());
		String separator = FileSystems.getDefault().getSeparator();
		String filename = theSource.substring(theSource.lastIndexOf(separator));
		// Now try to move the file
		CommandStatus status = handler.move(theSource, theDestination);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(theDestination + filename);
		assertTrue(exist);

		// Delete the test file/directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteRemoteDestination();

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.FileHandlerFactory#getFileHandler()} and
	 * remote file moving where the remote directory doesn't exist.
	 */
	@Test
	public void testRemoteFileHandlerFactoryDestinationNonexistent() throws Exception {
		// Make a local test file to play with
		fileCreator.createLocalSource();
		fileCreator.createRemoteDestination();
		theSource = fileCreator.getSource();
		theDestination = fileCreator.getDestination();

		IFileHandler handler = factory.getFileHandler(fileCreator.getConnection().getConfiguration());

		// Now try to move the file to some new directory
		String newDirectory = "some/other/dir/";
		// theDestination = theDestination + newDirectory;
		CommandStatus status = handler.move(theSource, theDestination + newDirectory);
		assertEquals(CommandStatus.SUCCESS, status);

		// Check that the file exists now
		boolean exist = handler.exists(theDestination + newDirectory);
		assertTrue(exist);

		// Delete the test file/directory now that the test is finished
		fileCreator.deleteLocalSource();
		fileCreator.deleteRemoteDestination();

	}

	/**
	 * This function just returns the local hostname of your local computer. It is
	 * useful for testing a variety of local commands.
	 * 
	 * @return - String - local hostname
	 */
	protected static String getLocalHostname() {
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();

		return hostname;
	}

}
