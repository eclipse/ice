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

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.KeyPathConnectionAuthorizationHandler;
import org.eclipse.ice.commands.RemoteRemoteFileHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the functionality of the RemoteRemoteFileHandler class,
 * which contains the logic to perform file transfers from one remote system to
 * another remote system
 * 
 * @author Joe Osborn
 *
 */
public class RemoteRemoteFileHandlerTest {

	/**
	 * A source file to move around
	 */
	protected String source = "";

	/**
	 * A destination to move the source file
	 */
	protected String destination = "";

	/**
	 * The connection configuration for the remoteHostB where the source file lives
	 */
	protected static ConnectionConfiguration remoteHostB;

	/**
	 * A connection for remote host b
	 */
	protected static Connection bConn;

	/**
	 * The keypath authorization for remote host C
	 */
	protected static KeyPathConnectionAuthorizationHandler remoteHostCAuth;

	/**
	 * Create an instance of this class to take advantage of file creation/deletion
	 * code
	 */
	static private RemoteRemoteFileTransferTest transferTest = new RemoteRemoteFileTransferTest();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		makeRemoteHostConfigs();

		bConn = manager.openConnection(remoteHostB);

		// Setup the sftp channel to remote host b so that we can create dummy files
		// and put them there immediately
		bConn.setSftpChannel(SftpClientFactory.instance().createSftpClient(bConn.getSession()));
	}

	/**
	 * Make the connection configurations for the two remote hosts
	 * 
	 * @return
	 */
	private static void makeRemoteHostConfigs() {
		transferTest.setupConnectionConfigs();
		remoteHostB = transferTest.getRemoteHostBConnectionConfig();
		remoteHostCAuth = transferTest.getRemoteHostCAuth();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManagerFactory.getConnectionManager().closeAllConnections();
		
		// Delete the output files that were made
		RemoteRemoteFileTransferTest.tearDownAfterClass();
	}

	/**
	 * This method tests the checkExistence function, which identifies the existence
	 * of both the source and destination files
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCheckExistence() throws IOException {

		transferTest.createRemoteHostBSourceFile();
		source = transferTest.getSource();
		destination = "/tmp/";

		RemoteRemoteFileHandler handler = new RemoteRemoteFileHandler();
		handler.setConnectionConfiguration(remoteHostB);
		handler.setDestinationAuthorization(remoteHostCAuth);

		// This should not throw an exception. No exception == success
		handler.checkExistence(source, destination);

		transferTest.deleteHostBSource();

	}

	/**
	 * This tests checkExistence with a nonexistent source file. Expected exception
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testCheckExistenceNonExistentSource() throws IOException {
		destination = "/tmp/";

		RemoteRemoteFileHandler handler = new RemoteRemoteFileHandler();
		handler.setConnectionConfiguration(remoteHostB);
		handler.setDestinationAuthorization(remoteHostCAuth);

		handler.checkExistence("/nonexistent/source/file.txt", destination);

	}

	/**
	 * This tests checkExistence with a nonexistent destination directory. Expected
	 * exception
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testCheckExistenceNonExistentDestination() throws IOException {

		transferTest.createRemoteHostBSourceFile();
		source = transferTest.getSource();

		RemoteRemoteFileHandler handler = new RemoteRemoteFileHandler();
		handler.setConnectionConfiguration(remoteHostB);
		handler.setDestinationAuthorization(remoteHostCAuth);
		try {
			handler.checkExistence(source, "/nonexistent/destination/");
		} catch (IOException e) {
			// Expected exception, but still want to delete the host source file
			transferTest.deleteHostBSource();
			// If exception was caught, test was a success. So end it by throwing
			// an exception
			throw new IOException();
		}
	}

	/**
	 * Tests both the move function and the exists function, since they can be used
	 * in tandem together
	 * 
	 * We only test the move command since the copy command from the
	 * RemoteRemoteFileHandler calls the same function, since an scp does the same
	 * thing as both move and copy
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMove() throws IOException {

		transferTest.createRemoteHostBSourceFile();
		source = transferTest.getSource();
		destination = "/tmp/";
		// Get the filename by splitting the path by "/"
		String separator = "/";
		if (source.contains("\\"))
			separator = "\\";

		String[] tokens = source.split(separator);
		String filename = tokens[tokens.length - 1];

		RemoteRemoteFileHandler handler = new RemoteRemoteFileHandler();
		handler.setConnectionConfiguration(remoteHostB);
		handler.setDestinationAuthorization(remoteHostCAuth);

		// This also tests exists function, which is really tested by checkExistence
		// test above
		CommandStatus status = handler.move(source, destination);
		assertTrue(status.equals(CommandStatus.SUCCESS));
		transferTest.deleteHostBSource();

		// Delete file from remote host C
		// Delete the file that was moved with another command
		String remoteHostCKeyPath = remoteHostCAuth.getKeyPath();
		String command = "ssh -i " + remoteHostCKeyPath + " " + remoteHostCAuth.getUsername() + "@" + remoteHostCAuth.getHostname();
		command += " \"rm " + destination + filename + "\"";

		CommandConfiguration config = new CommandConfiguration();
		config.setExecutable(command);
		config.setAppendInput(false);
		config.setCommandId(99);
		config.setErrFileName("lsErr.txt");
		config.setOutFileName("lsOut.txt");
		config.setNumProcs("1");

		// Get the command
		CommandFactory factory = new CommandFactory();
		Command rmCommand = factory.getCommand(config, remoteHostB);

		status = rmCommand.execute();
		// Just warn, since it isn't a huge deal if a file wasn't successfully deleted
		if (!status.equals(CommandStatus.SUCCESS))
			System.out.println("Couldn't delete destination file at : " + destination + filename);

	}

}
