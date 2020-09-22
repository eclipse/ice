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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;
import org.apache.sshd.client.subsystem.sftp.SftpClient.OpenMode;
import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.apache.sshd.common.subsystem.sftp.SftpConstants;
import org.apache.sshd.common.subsystem.sftp.SftpException;
import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.RemoteFileBrowser;
import org.eclipse.ice.commands.RemoteFileHandler;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the RemoteFileBrowser and associated functionality
 *
 * @author Joe Osborn
 *
 */
public class RemoteFileBrowserTest {

	/**
	 * A connection to use throughout the test
	 */
	static Connection fileTransferConn = new Connection();

	/**
	 * A remote file handler test to take advantage of the file creation/deletion
	 * code and remote connection establishment code already developed there.
	 */
	private RemoteFileHandlerTest fileCreator = new RemoteFileHandlerTest();

	/**
	 * A TDP for collecting config files
	 */
	static TestDataPath dataPath = new TestDataPath();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		ConnectionConfiguration config = makeConnectionConfiguration();

		fileTransferConn = manager.openConnection(config);

		fileTransferConn.setSftpChannel(SftpClientFactory.instance().createSftpClient(fileTransferConn.getSession()));

		// To set up the file creator information for use later
		RemoteFileHandlerTest.setUpBeforeClass();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();
	}

	/**
	 * Function to execute the remote file browsing and remote directory browsing
	 * test. We call one main function so that a file structure can be created at
	 * the beginning of the test, accessed by both "subtests", and then deleted at
	 * the end
	 *
	 * @throws Exception
	 */
	@Test
	public void testRemoteBrowsing() throws Exception {
		// We'll create our own code here to create the file structure rather than
		// using the already developed functions, since we need multiple files and
		// directories
		String topDirectory = "/tmp/fileBrowsingDir/";
		// put this in a try and finally so that the remote file structure
		// always gets deleted. Otherwise the next test run might fail when it
		// tries to build the remote file structure and see that it already
		// exists
		try {
			createRemoteFileStructure(topDirectory);

			testRemoteFileBrowsing(topDirectory);

			testRemoteDirectoryBrowsing(topDirectory);
		} finally {
			deleteRemoteFileStructure(topDirectory);
		}
	}

	/**
	 * Test for file browsing on remote system
	 *
	 * @throws IOException
	 */
	public void testRemoteFileBrowsing(String topDirectory) throws IOException {

		RemoteFileHandler handler = new RemoteFileHandler();
		RemoteFileBrowser browser = new RemoteFileBrowser(fileTransferConn, topDirectory);

		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		ArrayList<String> files = browser.getFileList();

		// files should only be 4 entries since there are only 4 files in the tree
		// structure we created
		assertEquals(4, files.size());

		SftpClient channel = fileTransferConn.getSftpChannel();
		for (int i = 0; i < files.size(); i++) {
			// Assert that the file exists
			assertTrue(handler.exists(files.get(i)));

			// This should just be a one file vector, since we are ls-ing the filename
			assertTrue(channel.stat(files.get(i)).isRegularFile());
		}
	}

	/**
	 * Test for directory browsing on remote system
	 *
	 * @throws IOException
	 */
	public void testRemoteDirectoryBrowsing(String topDirectory) throws IOException {

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());
		RemoteFileBrowser browser = new RemoteFileBrowser(fileTransferConn, topDirectory);

		ArrayList<String> files = browser.getDirectoryList();

		// directories should only be 3 entries since there are only 3 directories in
		// the tree structure we created
		assertEquals(3, files.size());

		// Use the default separator of the remote dummy system
		String separator = "/";

		for (int i = 0; i < files.size(); i++) {
			assertTrue(handler.exists(files.get(i)));
			SftpClient channel = fileTransferConn.getSftpChannel();

			// ls the previous directory so that we can look at the subdirectories
			// Iterate through the ls
			for (DirEntry filename : channel.readDir(files.get(i).substring(0, files.get(i).lastIndexOf(separator)))) {
				// Check that the ls filename is actually in the directory list (and thus is a
				// directory)
				if (files.get(i).contains(filename.getFilename())) {
					// assert that it is a directory
					assertTrue(filename.getAttributes().isDirectory());
				}
			}
		}

	}

	/**
	 * Function that creates a dummy remote file structure tree to test the file
	 * browsing source code
	 *
	 * @param - topDirectory - refers to top directory whose contents will hold the
	 *          dummy directories/files
	 * @throws Exception
	 */
	protected void createRemoteFileStructure(String topDirectory) throws Exception {

		SftpClient sftpChannel = fileTransferConn.getSftpChannel();

		// Check if the directory already exists
		try {
			sftpChannel.lstat(topDirectory);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(topDirectory);
		}

		// make another directory in top directory
		sftpChannel.mkdir(topDirectory + "/dir1");
		sftpChannel.mkdir(topDirectory + "/dir2");
		sftpChannel.mkdir(topDirectory + "/dir3");

		// create a local file to put there
		fileCreator.createLocalSource();
		// Get the filename that was just created
		String filename = fileCreator.getSource();
		// put it in a few places in the directory structure
		putFile(sftpChannel, filename, topDirectory);
		putFile(sftpChannel, filename, topDirectory + "/dir1/");
		putFile(sftpChannel, filename, topDirectory + "/dir3/");
		putFile(sftpChannel, filename, topDirectory + "/dir3/newfile.txt");

	}

	private void putFile(SftpClient client, String src, String dest) throws IOException {
		try {
			if (client.stat(dest).isDirectory()) {
				Path path = FileSystems.getDefault().getPath(src);
				String sep = "";
				if (!dest.endsWith("/")) {
					sep = "/";
				}
				dest += sep + path.getFileName();
			}
		} catch (SftpException e) {
            if (!(e.getStatus() == SftpConstants.SSH_FX_NO_SUCH_FILE)) {
                throw e;
            }
        }
		try (OutputStream dstStream = client.write(dest, OpenMode.Create, OpenMode.Write, OpenMode.Truncate)) {
			try (InputStream srcStream = new FileInputStream(src)) {
				byte[] buf = new byte[32*1024];
				while (srcStream.read(buf) > 0) {
					dstStream.write(buf);
				}
			}
		}
	}

	/**
	 * Function that deletes the remote file structure tree created for testing file
	 * browsing
	 *
	 * @param - topDirectory - refers to top directory whose contents hold the dummy
	 *          directories/files
	 * @throws IOException
	 */
	protected void deleteRemoteFileStructure(String topDirectory) throws IOException {
		// Connect the channel from the connection
		SftpClient sftpChannel = fileTransferConn.getSftpChannel();

		System.out.println("Deleting remote destination at : " + topDirectory);
		// Recursively delete the directory and its contents
		deleteRemoteDirectory(sftpChannel, topDirectory);

	}

	/**
	 * Recurisve function that deletes a remote directory and its contents
	 *
	 * @param sftpChannel
	 * @param path
	 * @throws IOException
	 */
	private void deleteRemoteDirectory(SftpClient sftpChannel, String path) throws IOException {

		// Iterate through the list to get the file/directory names
		for (DirEntry file : sftpChannel.readDir(path)) {
			// If it isn't a directory delete it
			if (!file.getAttributes().isDirectory()) {
				// Can use / here because we know the dummy directory is on linux, not windows
				sftpChannel.remove(path + "/" + file.getFilename());
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
		try {
			sftpChannel.rmdir(path); // delete the parent directory after empty
		} catch (IOException e) {
			// If the sftp channel can't delete it, just manually rm it with a execution
			// channel
			fileTransferConn.getSession().executeRemoteCommand("rm -r " + path);
		}
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
		ConnectionConfiguration config = new ConnectionConfiguration();

		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();

		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		config.setAuthorization(auth);

		// Note the password can be input at the console by not setting the
		// the password explicitly in the connection configuration
		String name = "dummyConnection" + Math.random();

		config.setName(name);

		config.deleteWorkingDirectory(false);

		return config;
	}

}
