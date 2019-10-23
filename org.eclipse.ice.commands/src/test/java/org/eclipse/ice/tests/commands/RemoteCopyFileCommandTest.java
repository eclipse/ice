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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.RemoteCopyFileCommand;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * Test for class {@link org.eclipse.ice.commands.RemoteCopyFileCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCopyFileCommandTest {

	/**
	 * A string for a source file for the remote move
	 */
	String source = null;

	/**
	 * A string for a destination for the remote move
	 */
	String dest = null;

	/**
	 * Make a RemoteFileHandlerTest to take advantage of much of the code which
	 * makes/deletes local/remote files
	 */
	RemoteFileHandlerTest handlerTest = new RemoteFileHandlerTest();

	/**
	 * This function sets up the dummy connection for file transferring
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set up the connection using the code already established in
		// RemoteFileHandlerTest
		RemoteFileHandlerTest.setUpBeforeClass();
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
	 * Test for copying a file only on the remote system
	 */
	@Test
	public void testRemoteCopyFileCommand() throws Exception {

		handlerTest.createRemoteSource();
		handlerTest.createRemoteDestination();
		source = handlerTest.getSource();
		dest = handlerTest.getDestination();

		RemoteCopyFileCommand command = new RemoteCopyFileCommand();
		// These functions are nominally handled by the FileHandler. But, when testing
		// this class alone, we need to set them individually
		command.setCopyType(3);
		command.setConnection(handlerTest.getConnection());
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();

		// Assert that the command status was properly configured
		assert (status == CommandStatus.SUCCESS);

		// Assert that the command was actually successful and that command status
		// wasn't inadvertently set to successful
		assert (remotePathExists());

		// Delete the temporary files that were created to test
		handlerTest.deleteRemoteSource();
		handlerTest.deleteRemoteDestination();

	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteMoveFileCommand()}
	 * where the file is downloaded from the remote host
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRemoteCopyFileCommandDownload() throws Exception {
		// Create a remote source to download
		handlerTest.createRemoteSource();
		source = handlerTest.getSource();

		// Create a local destination to put that source
		handlerTest.createLocalDestination();
		dest = handlerTest.getDestination();

		RemoteCopyFileCommand command = new RemoteCopyFileCommand();
		// These functions are nominally handled by the FileHandler. But, when testing
		// this class alone, we need to set them individually
		command.setCopyType(2);
		command.setConnection(handlerTest.getConnection());
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();

		// Assert that the command status was properly configured
		assert (status == CommandStatus.SUCCESS);

		// Assert that the command was actually successful and that command status
		// wasn't inadvertently set to successful
		assert (localPathExists());

		// Delete the temporary files that were created to test
		handlerTest.deleteRemoteSource();
		handlerTest.deleteLocalDestination();

	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteMoveFileCommand()} for
	 * uploading a file to the remote host
	 */
	@Test
	public void testRemoteCopyFileCommandUpload() throws Exception {

		// Create a local source file to move
		handlerTest.createLocalSource();
		source = handlerTest.getSource();
		// Create a remote destination to move it to
		handlerTest.createRemoteDestination();
		dest = handlerTest.getDestination();

		// Make a command and execute it
		RemoteCopyFileCommand command = new RemoteCopyFileCommand();
		// These functions are nominally handled by the FileHandler. But, when testing
		// this class alone, we need to set them individually
		command.setConnection(handlerTest.getConnection());
		command.setCopyType(1);
		command.setConfiguration(source, dest);

		// execute the command
		CommandStatus status = command.execute();

		assert (status == CommandStatus.SUCCESS);

		// Check that the path exists
		try {
			assert (remotePathExists());
		} catch (Exception e) {
			e.printStackTrace();
		}

		handlerTest.deleteLocalSource();
		handlerTest.deleteRemoteDestination();
	}

	/**
	 * This function checks if a local path exists on the host
	 * 
	 * @return
	 */
	public boolean localPathExists() {
		Path path = Paths.get(dest);
		return Files.exists(path);
	}

	/**
	 * This function checks if a remote file exists on the host
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean remotePathExists() throws Exception {

		// Get the connected channel from the connection
		ChannelSftp sftpChannel = (ChannelSftp) handlerTest.getConnection().getChannel();

		try {
			sftpChannel.lstat(dest);
		} catch (SftpException e) {
			// If an exception is caught, this means the file was not there
			return false;
		}

		// So if we get here, then the file is there
		return true;
	}

}
