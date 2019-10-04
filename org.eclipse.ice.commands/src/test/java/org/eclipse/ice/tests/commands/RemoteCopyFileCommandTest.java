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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.RemoteCopyFileCommand;
import org.junit.Before;
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
	 * A connection with which to test
	 */
	ConnectionConfiguration connectionConfig = new ConnectionConfiguration();

	/**
	 * A connection to move files with
	 */
	Connection dummyConnection = null;

	/**
	 * A connection manager for opening the connection to test with
	 */
	ConnectionManager manager = new ConnectionManager();

	/**
	 * Make a IFileHandlerFactoryTest to take advantage of much of the code which
	 * makes/deletes local/remote files
	 */
	IFileHandlerFactoryTest factory = new IFileHandlerFactoryTest();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

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
		connectionConfig.setHostname(hostname);
		connectionConfig.setUsername(username);
		connectionConfig.setPassword(password);
		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		connectionConfig.setName("dummyConnection");
		connectionConfig.setDeleteWorkingDirectory(false);

		// open the connection and set it so that the factory can create the
		// remote files
		dummyConnection = manager.openConnection(connectionConfig);
		factory.setConnection(dummyConnection);

	
	}


	/**
	 * Test for copying a file only on the remote system
	 */
	@Test
	public void testRemoteCopyFileCommand() throws Exception {
		
		factory.createRemoteSource();
		factory.createRemoteDestination();
		source = factory.getSource();
		dest = factory.getDestination();
		
		RemoteCopyFileCommand command = new RemoteCopyFileCommand();
		// These functions are nominally handled by the FileHandler. But, when testing
		// this class alone, we need to set them individually
		command.setCopyType(3);
		command.setConnection(dummyConnection);
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();

		// Assert that the command status was properly configured
		assert (status == CommandStatus.SUCCESS);

		// Assert that the command was actually successful and that command status
		// wasn't inadvertently set to successful
		assert (remotePathExists());

		// Delete the temporary files that were created to test
		factory.deleteRemoteSource();
		factory.deleteRemoteDestination();
		
		
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
		factory.createRemoteSource();
		source = factory.getSource();

		// Create a local destination to put that source
		factory.createLocalDestination();
		dest = factory.getDestination();

		RemoteCopyFileCommand command = new RemoteCopyFileCommand();
		// These functions are nominally handled by the FileHandler. But, when testing
		// this class alone, we need to set them individually
		command.setCopyType(2);
		command.setConnection(dummyConnection);
		command.setConfiguration(source, dest);
		CommandStatus status = command.execute();

		// Assert that the command status was properly configured
		assert (status == CommandStatus.SUCCESS);

		// Assert that the command was actually successful and that command status
		// wasn't inadvertently set to successful
		assert (localPathExists());

		// Delete the temporary files that were created to test
		factory.deleteRemoteSource();
		factory.deleteLocalDestination();

	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteMoveFileCommand()}
	 * for uploading a file to the remote host
	 */
	@Test
	public void testRemoteCopyFileCommandUpload() throws Exception {
		
		// Create a local source file to move
		factory.createLocalSource();
		source = factory.getSource();
		// Create a remote destination to move it to
		factory.createRemoteDestination();
		dest = factory.getDestination();
		
		// Make a command and execute it
		RemoteCopyFileCommand command = new RemoteCopyFileCommand();
		// These functions are nominally handled by the FileHandler. But, when testing
		// this class alone, we need to set them individually
		command.setConnection(dummyConnection);
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
		
		factory.deleteLocalSource();
		factory.deleteRemoteDestination();
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

		// Connect the channel from the connection
		ChannelSftp sftpChannel = (ChannelSftp) dummyConnection.getSession().openChannel("sftp");
		sftpChannel.connect();

		try {
			sftpChannel.lstat(dest);
		} catch (SftpException e) {
			// If an exception is caught, this means the file was not there
			return false;
			}
		
		return true;
	}

}
