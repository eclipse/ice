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
		String filename = localSource.substring(localSource.lastIndexOf("/"));

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
		String filename = localSource.substring(localSource.lastIndexOf("/"));
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

}
