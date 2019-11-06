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
	 * Default constructor
	 */
	public RemoteFileBrowser() {
		// Make sure we start with a fresh list everytime the walker is called
		fileList.clear();
		directoryList.clear();
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
		// Make sure the top directory ends with the appropriate separator
		String separator = "/";
		// Check for windows first
		if(System.getProperty("os.name").toLowerCase().contains("win"))
			separator = "\\";
		// Now check the path name
		if(!topDirectory.endsWith(separator))
			topDirectory += separator;
		
		try {
			// Get the path's directory structure
			Collection<ChannelSftp.LsEntry> directoryStructure = channel.ls(topDirectory);
			// Iterate through the structure
			for (ChannelSftp.LsEntry file : directoryStructure) {
				if(!file.getAttrs().isDir()) {
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
