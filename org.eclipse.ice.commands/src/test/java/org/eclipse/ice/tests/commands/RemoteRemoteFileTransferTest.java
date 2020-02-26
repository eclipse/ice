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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.OpenMode;
import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.HandleType;
import org.eclipse.ice.commands.KeyPathConnectionAuthorizationHandler;
import org.eclipse.ice.commands.RemoteRemoteFileTransferCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests remote to remote file transfers executed from a local host
 * A, i.e. from host A a remote file transfer between host B and host C
 * 
 * @author Joe Osborn
 *
 */
public class RemoteRemoteFileTransferTest {

	/**
	 * A source file to use for testing
	 */
	String source = "";

	/**
	 * A destination to move the file
	 */
	String destination = "/tmp/";

	/**
	 * Connection information for host B
	 */
	ConnectionConfiguration hostBConnection;

	/**
	 * Remote host C key path that is needed to establish connection between host B
	 * and host C
	 */
	String remoteHostCKeyPath = "/path/to/keykey";

	/**
	 * Authorization for remote host C
	 */
	KeyPathConnectionAuthorizationHandler remoteHostC;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Delete the extra output files that were made during clean up
		String rm = "lsOut.txt lsErr.txt";

		List<String> command = new ArrayList<String>();
		// Build a command
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			command.add("powershell.exe");
		} else {
			command.add("/bin/bash");
			command.add("-c");
		}
		command.add("rm " + rm);

		// Execute the command with the process builder api
		ProcessBuilder builder = new ProcessBuilder(command);
		// Files exist in the top most directory of the package
		String topDir = System.getProperty("user.dir");
		File file = new File(topDir);
		builder.directory(file);
		// Process it
		Process job = builder.start();
		job.waitFor(); // wait for it to finish

		// Remove connections
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();
	}

	/**
	 * A test function to test the remote to remote file transfer functionality,
	 * where the file transfer goes from one remote host B to another remote host C
	 * 
	 * @throws Exception
	 */
	@Test
	// @Ignore // ignore for now until we get second dummy host running
	public void testRemoteRemoteFileTransfer() throws Exception {
		System.out.println("Testing RemoteRemote \n\n\n");

		setupConnectionConfigs();

		createRemoteHostBSourceFile();

		RemoteRemoteFileTransferCommand command = new RemoteRemoteFileTransferCommand();
		command.setConnectionConfiguration(hostBConnection);
		command.setRemoteHostCAuthorization(remoteHostC);
		command.setConfiguration(source, destination);
		command.createCommand();
		CommandStatus status = command.execute();

		// Check that command completed correctly
		assertEquals(CommandStatus.SUCCESS, status);

		// Check if file transfer was successful and deletes the moved file on host C
		assertTrue(checkPathExistsRemoteHostC());

		// Now clean up the file created
		deleteHostBSource();
		System.out.println("End Test \n\n\n");
	}

	/**
	 * Deletes the source file from the remote host B
	 * 
	 * @throws IOException
	 */
	protected void deleteHostBSource() throws IOException {
		Connection conn = ConnectionManagerFactory.getConnectionManager().getConnection(hostBConnection.getName());
		conn.setSftpChannel(SftpClientFactory.instance().createSftpClient(conn.getSession()));
		SftpClient channel = conn.getSftpChannel();
		channel.remove(source);
	}

	/**
	 * Creates a source file to play with on remote host B
	 * 
	 * @throws IOException
	 */
	protected void createRemoteHostBSourceFile() throws IOException {
		Connection conn = ConnectionManagerFactory.getConnectionManager().openConnection(hostBConnection);
		conn.setSftpChannel(SftpClientFactory.instance().createSftpClient(conn.getSession()));
		SftpClient sftpChannel = conn.getSftpChannel();
		String hostBsource = "/tmp/";

		try {
			sftpChannel.lstat(hostBsource);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it for source file.");
			sftpChannel.mkdir(hostBsource);
		}

		// Create a dummy file to test
		String src = "remoteRemoteDummyfile.txt";
		Path sourcePath = null;
		try {
			sourcePath = Files.createTempFile(null, src);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the filename by splitting the path by "/"
		String separator = FileSystems.getDefault().getSeparator();
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			separator += "\\";
		String[] tokens = sourcePath.toString().split(separator);

		// Get the last index of tokens, which will be the filename
		String filename = tokens[tokens.length - 1];

		if (hostBsource.endsWith("/")) {
			hostBsource += filename;
		}

		try (OutputStream dstStream = sftpChannel.write(hostBsource, OpenMode.Create, OpenMode.Write,
				OpenMode.Truncate)) {
			try (InputStream srcStream = new FileInputStream(sourcePath.toString())) {
				byte[] buf = new byte[32 * 1024];
				while (srcStream.read(buf) > 0) {
					dstStream.write(buf);
				}
			}
		}

		// Delete the local directory that was created since it is no longer needed
		Path path = Paths.get(sourcePath.toString());

		try {
			Files.deleteIfExists(path);
		} catch (NoSuchFileException e) {
			// If somehow the directory doesn't exist, indicate so
			System.err.format("%s: somehow this path doesn't exist... no such" + " file or directory%n", path);
			e.printStackTrace();
		}

		// set the source string in this class to that just created
		source = hostBsource;
	}

	/**
	 * Function that just sets up the connection information for the test to run
	 */
	protected void setupConnectionConfigs() {
		remoteHostC = new KeyPathConnectionAuthorizationHandler();
		remoteHostC.setUsername("dummy");
		remoteHostC.setHostname("osbornjd-ice-host.ornl.gov");
		remoteHostC.setOption(remoteHostCKeyPath);

		hostBConnection = new ConnectionConfiguration();
		hostBConnection.setName("hostB");
		ConnectionAuthorizationHandler bauth = new KeyPathConnectionAuthorizationHandler();
		bauth.setHostname("host");
		bauth.setUsername("user");
		bauth.setOption("/path/to/key");

		hostBConnection.setAuthorization(bauth);

	}

	/**
	 * This function checks if the file was properly moved to the remote host C
	 * 
	 * @throws IOException
	 */
	protected boolean checkPathExistsRemoteHostC() throws IOException {

		// Get the filename by splitting the path by "/"
		String separator = "/";
		if (source.contains("\\"))
			separator = "\\";

		String[] tokens = source.split(separator);
		String filename = tokens[tokens.length - 1];

		// add the file name to the destination
		if (!destination.endsWith(separator))
			destination += separator;

		destination += filename;

		// Create a remote command to ls the remote path

		String command = "ssh -i " + remoteHostCKeyPath + " " + remoteHostC.getUsername() + "@"
				+ remoteHostC.getHostname();
		command += " \"ls " + destination + "\"";

		// Create the command configuration
		CommandConfiguration config = new CommandConfiguration();
		config.setExecutable(command);
		config.setAppendInput(false);
		config.setCommandId(99);
		config.setErrFileName("lsErr.txt");
		config.setOutFileName("lsOut.txt");
		config.setNumProcs("1");
		// WD doesn't matter since we are lsing with an absolute path
		config.setWorkingDirectory("/tmp/doesnt/matter");

		// Get the command
		CommandFactory factory = new CommandFactory();
		Command Command = factory.getCommand(config, hostBConnection);

		CommandStatus status = Command.execute();

		// If the command was successful, it means that it could successfully ls
		// indicating that the file was there.
		if (!status.equals(CommandStatus.SUCCESS))
			return false;

		// Delete the file that was moved with another command
		command = "ssh -i " + remoteHostCKeyPath + " " + remoteHostC.getUsername() + "@" + remoteHostC.getHostname();
		command += " \"rm " + destination + "\"";

		config = new CommandConfiguration();
		config.setExecutable(command);
		config.setAppendInput(false);
		config.setCommandId(99);
		config.setErrFileName("lsErr.txt");
		config.setOutFileName("lsOut.txt");
		config.setNumProcs("1");
		// WD doesn't matter since we are rming with an absolute path
		config.setWorkingDirectory("/tmp/doesnt/matter");

		// Get the command
		Command rmCommand = factory.getCommand(config, hostBConnection);

		status = rmCommand.execute();
		// Just warn, since it isn't a huge deal if a file wasn't successfully deleted
		if (!status.equals(CommandStatus.SUCCESS))
			System.out.println("Couldn't delete destination file at : " + destination);
		return true;
	}

	/**
	 * Add some functions that can be used by other classes to take advantage of the
	 * file creation and deletion code that was already created here
	 */
	public RemoteRemoteFileTransferTest() {

	}

	protected ConnectionConfiguration getRemoteHostBConnectionConfig() {
		return hostBConnection;
	}

	protected KeyPathConnectionAuthorizationHandler getRemoteHostCAuth() {
		return remoteHostC;
	}

	protected String getSource() {
		return source;
	}

	protected String getDestination() {
		return destination;
	}

}
