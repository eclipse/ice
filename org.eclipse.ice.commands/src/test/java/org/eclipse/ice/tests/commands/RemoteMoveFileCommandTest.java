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
import java.util.Scanner;

import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.RemoteMoveFileCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * Test for class {@link org.eclipse.ice.commands.RemoteMoveFileCommand}.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteMoveFileCommandTest {

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

		// Create a local source file to move
		factory.createLocalSource();
		source = factory.getSource();
		// Create a remote destination to move it to
		factory.createRemoteDestination();
		dest = factory.getDestination();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Delete the test file
		factory.deleteRemoteDestination();
	}

	/**
	 * Test for method {@link org.eclipse.ice.commands.RemoteMoveFileCommand()}
	 */
	@Test
	public void testRemoteMoveFileCommand() {
		System.out.println("Moving " + source + " to destination " + dest);

		RemoteMoveFileCommand command = new RemoteMoveFileCommand();
		command.setConfiguration(source, dest);
		command.execute();

		try {
			assert (pathExists());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function checks if a remote file exists on the host
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean pathExists() throws Exception {

		// Connect the channel from the connection
		ChannelSftp sftpChannel = (ChannelSftp) dummyConnection.getSession().openChannel("sftp");
		sftpChannel.connect();

		try {
			sftpChannel.lstat(dest);
		} catch (SftpException e) {
			if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
				return false;
			} else {
				System.out.println("File was there but some other error occured?");
				throw new Exception();
			}
		}
		return true;
	}

}
