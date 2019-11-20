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
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.RemoteFileBrowser;
import org.eclipse.ice.commands.RemoteFileHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

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
	RemoteFileHandlerTest fileCreator = new RemoteFileHandlerTest();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		ConnectionConfiguration config = makeConnectionConfiguration();

		fileTransferConn = manager.openConnection(config);

		fileTransferConn.setSftpChannel(fileTransferConn.getSession().openChannel("sftp"));
		fileTransferConn.getSftpChannel().connect();

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
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
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
	 * @throws SftpException
	 */
	public void testRemoteFileBrowsing(String topDirectory) throws IOException, SftpException {

		RemoteFileHandler handler = new RemoteFileHandler();
		RemoteFileBrowser browser = new RemoteFileBrowser(fileTransferConn);

		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());

		ArrayList<String> files = browser.listFiles(topDirectory);

		// files should only be 4 entries since there are only 4 files in the tree
		// structure we created
		assert (files.size() == 4);

		for (int i = 0; i < files.size(); i++) {
			// Assert that the file exists
			assert (handler.exists(files.get(i)));

			ChannelSftp channel = fileTransferConn.getSftpChannel();
			Collection<ChannelSftp.LsEntry> structure = channel.ls(files.get(i));
			// This should just be a one file vector, since we are ls-ing the filename
			for (ChannelSftp.LsEntry filename : structure) {
				// Assert that the file is indeed a regular file
				assert (filename.getAttrs().isReg());
			}
		}
	}

	/**
	 * Test for directory browsing on remote system
	 * 
	 * @throws IOException
	 * @throws SftpException
	 */
	public void testRemoteDirectoryBrowsing(String topDirectory) throws IOException, SftpException {

		RemoteFileHandler handler = new RemoteFileHandler();
		handler.setConnectionConfiguration(fileTransferConn.getConfiguration());
		RemoteFileBrowser browser = new RemoteFileBrowser(fileTransferConn);

		ArrayList<String> files = browser.listDirectories(topDirectory);

		// directories should only be 3 entries since there are only 3 directories in
		// the tree structure we created
		assert (files.size() == 3);

		// Use the default separator of the remote dummy system
		String separator = "/";

		for (int i = 0; i < files.size(); i++) {
			assert (handler.exists(files.get(i)));
			ChannelSftp channel = fileTransferConn.getSftpChannel();

			// ls the previous directory so that we can look at the subdirectories
			Collection<ChannelSftp.LsEntry> structure = channel
					.ls(files.get(i).substring(0, files.get(i).lastIndexOf(separator)));
			// Iterate through the ls
			for (ChannelSftp.LsEntry filename : structure) {
				// Check that the ls filename is actually in the directory list (and thus is a
				// directory)
				if (files.get(i).contains(filename.getFilename())) {
					// assert that it is a directory
					assert (filename.getAttrs().isDir());
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
	 * @throws SftpException
	 */
	protected void createRemoteFileStructure(String topDirectory) throws Exception, SftpException {

		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		// Check if the directory already exists
		SftpATTRS attrs = null;
		try {
			attrs = sftpChannel.lstat(topDirectory);
		} catch (Exception e) {
			System.out.println("Remote directory not found, trying to make it");
		}
		if (attrs == null) {
			// Remote directory doesn't exist, so make it
			// Create a remote source directory
			sftpChannel.mkdir(topDirectory);
		}

		// make another directory in top directory
		sftpChannel.cd(topDirectory);
		sftpChannel.mkdir("dir1");
		sftpChannel.mkdir("dir2");
		sftpChannel.mkdir("dir3");

		// create a local file to put there
		fileCreator.createLocalSource();
		// Get the filename that was just created
		String filename = fileCreator.getSource();
		// put it in a few places in the directory structure
		sftpChannel.put(filename, topDirectory);
		sftpChannel.put(filename, topDirectory + "/dir1/");
		sftpChannel.put(filename, topDirectory + "/dir3/");
		sftpChannel.put(filename, topDirectory + "/dir3/newfile.txt");

	}

	/**
	 * Function that deletes the remote file structure tree created for testing file
	 * browsing
	 * 
	 * @param - topDirectory - refers to top directory whose contents hold the dummy
	 *          directories/files
	 * @throws JSchException
	 * @throws SftpException
	 */
	protected void deleteRemoteFileStructure(String topDirectory) throws SftpException, JSchException {
		// Connect the channel from the connection
		ChannelSftp sftpChannel = fileTransferConn.getSftpChannel();

		System.out.println("Deleting remote destination at : " + topDirectory);
		// Recursively delete the directory and its contents
		deleteRemoteDirectory(sftpChannel, topDirectory);

	}

	/**
	 * Recurisve function that deletes a remote directory and its contents
	 * 
	 * @param sftpChannel
	 * @param path
	 * @throws SftpException
	 * @throws JSchException
	 */
	private void deleteRemoteDirectory(ChannelSftp sftpChannel, String path) throws SftpException, JSchException {

		// Get the path's directory structure
		Collection<ChannelSftp.LsEntry> fileList = sftpChannel.ls(path);

		// Iterate through the list to get the file/directory names
		for (ChannelSftp.LsEntry file : fileList) {
			// If it isn't a directory delete it
			if (!file.getAttrs().isDir()) {
				// Can use / here because we know the dummy directory is on linux, not windows
				sftpChannel.rm(path + "/" + file.getFilename());
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
		} catch (SftpException e) {
			// If the sftp channel can't delete it, just manually rm it with a execution
			// channel
			ChannelExec execChannel = (ChannelExec) fileTransferConn.getSession().openChannel("exec");
			execChannel.setCommand("rm -r " + path);
			execChannel.connect();
			execChannel.disconnect();
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
		String credFile = "/tmp/ice-remote-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\ice-remote-creds.txt";
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
