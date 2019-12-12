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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * This class allows for remote file and directory browsing on a remote host
 * 
 * @author Joe Osborn
 *
 */
public class RemoteFileBrowser implements FileBrowser {

	/**
	 * A connection with which to browse files
	 */
	private Connection connection;

	/**
	 * Default  constructor
	 */
	public RemoteFileBrowser() {
		fileList.clear();
		directoryList.clear();
		this.connection = null;
	}
	
	/**
	 * Default constructor with connection and top directory name
	 */
	public RemoteFileBrowser(Connection connection, final String topDirectory) {
		// Make sure we start with a fresh list every time the browser is called
		fileList.clear();
		directoryList.clear();
		this.connection = connection;
		
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
	protected void fillArrays(String topDirectory, ChannelSftp channel) {

		try {
			// Make sure the top directory ends with the appropriate separator
			String separator = "/";
			// If the remote file system returns a home directory with \, then it
			// must be windows
			if (channel.getHome().contains("\\"))
				separator = "\\";

			// Now check the path name
			if (!topDirectory.endsWith(separator))
				topDirectory += separator;

			// Get the path's directory structure
			Collection<ChannelSftp.LsEntry> directoryStructure = channel.ls(topDirectory);
			// Iterate through the structure
			for (ChannelSftp.LsEntry file : directoryStructure) {
				if (!file.getAttrs().isDir()) {
					fileList.add(topDirectory + file.getFilename());
					// Else if it is a subdirectory and not '.' or '..'
				} else if (!(".".equals(file.getFilename()) || "..".equals(file.getFilename()))) {
					// Then add it to the directory list
					directoryList.add(topDirectory + file.getFilename());
					// Recursively iterate over this subdirectory to get its contents
					fillArrays(topDirectory + file.getFilename(), channel);
				}

			}

		} catch (SftpException e) {
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
