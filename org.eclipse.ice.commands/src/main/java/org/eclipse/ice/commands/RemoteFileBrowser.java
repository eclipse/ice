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
package org.eclipse.ice.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;

/**
 * This class allows for remote file and directory browsing on a remote host
 * 
 * @author Joe Osborn
 *
 */
public class RemoteFileBrowser implements FileBrowser {
	/**
	 * Default  constructor
	 */
	public RemoteFileBrowser() {
		fileList.clear();
		directoryList.clear();
	}
	
	/**
	 * Default constructor with connection and top directory name
	 */
	public RemoteFileBrowser(Connection connection, final String topDirectory) {
		// Make sure we start with a fresh list every time the browser is called
		fileList.clear();
		directoryList.clear();
		
		// Fill the arrays with the relevant file information
		fillArrays(topDirectory, connection.getSftpChannel());
	}

	/**
	 * This function uses the ChannelSftp to look in the directory structure on the
	 * remote host and fill the member variable arrays with the files and
	 * directories
	 * 
	 * @param channel
	 * @param topDirectory
	 */
	protected void fillArrays(String topDirectory, SftpClient channel) {
		try {
			// Make sure the top directory ends with the appropriate separator
			String separator = "/";
			// If the remote file system returns a home directory with \, then it
			// must be windows
			// TODO: I don't know how to do this using Mina. Is it really needed anyway?
//			if (channel.getHome().contains("\\"))
//				separator = "\\";

			// Now check the path name
			if (!topDirectory.endsWith(separator))
				topDirectory += separator;

			// Iterate through the structure
			for (DirEntry file : channel.readDir(topDirectory)) {
				if (!file.getAttributes().isDirectory()) {
					fileList.add(topDirectory + file.getFilename());
					// Else if it is a subdirectory and not '.' or '..'
				} else if (!(".".equals(file.getFilename()) || "..".equals(file.getFilename()))) {
					// Then add it to the directory list
					directoryList.add(topDirectory + file.getFilename());
					// Recursively iterate over this subdirectory to get its contents
					fillArrays(topDirectory + file.getFilename(), channel);
				}

			}

		} catch (IOException e) {
			logger.error("Could not use channel to connect to browse directories!", e);
		}

	}

	/**
	 * See {@link org.eclipse.ice.commands.FileBrowser#getDirectoryList()}
	 * 
	 * @return
	 */
	@Override
	public ArrayList<String> getDirectoryList() {
		return directoryList;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileBrowser#getFileList()}
	 * 
	 * @return
	 */
	@Override
	public ArrayList<String> getFileList() {
		return fileList;
	}

}
