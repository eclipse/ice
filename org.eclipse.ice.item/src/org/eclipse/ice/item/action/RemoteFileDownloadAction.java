/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.item.action;

import java.io.File;
import java.util.Dictionary;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.remote.core.IRemoteFileService;

/**
 * The RemoteFileDownloadAction is an ICE Action that downloads files from a
 * remote directory to the current localhost. It requires that clients provide a
 * remoteDir and localDir key-value pair in the provided execution dictionary.
 * 
 * @author Alex McCaskey
 *
 */
public class RemoteFileDownloadAction extends RemoteAction {

	/**
	 * The maximum size limit of any file that will be downloaded from a remote
	 * machine, in bytes. The default size is 50 MB and is set as a VM argument
	 * called "max_download_size", which can be edited by the user in the ICE
	 * config file.
	 */
	private long maxFileSize;

	/**
	 * The Constructor, takes the remote connection to use.
	 * 
	 * @param remote
	 */
	public RemoteFileDownloadAction() {
		// Get the maxFileSize from the system properties
		String fileSize = System.getProperty("max_download_size");
		if (fileSize != null) {
			maxFileSize = Integer.parseInt(fileSize);
		} else {
			// If the system property is invalid for any reason, default to a
			// hardcoded value
			maxFileSize = 52428800;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 * 
	 * This implementation of execute requires that the keys remoteDir and
	 * localDir be defined in the provided dictionary, and that they point to
	 * valid directory absolute paths.
	 * 
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Get the remote and local directory
		String remoteDir = dictionary.get("remoteDir");
		String localDir = dictionary.get("localDir");
		String hostName = dictionary.get("remoteHost");

		// Check that we've been given valid directories
		if (remoteDir == null || localDir == null || hostName == null) {
			logger.error("No remote host name or remote/local directory provided. Can't download files.");
			return FormStatus.InfoError;
		}

		// Get the remote connection
		connection = getRemoteConnection(hostName);
		if (connection == null) {
			logger.error("Could not get a valid connection to " + hostName);
			return FormStatus.InfoError;
		}
		
		// Get the remote file manager
		IRemoteFileService fileManager = connection.getService(IRemoteFileService.class);

		// Get the Local Directory
		IFileStore localDirectory = EFS.getLocalFileSystem().fromLocalFile(new File(localDir));

		// Get the remote directory.
		IFileStore downloadFileStore = fileManager.getResource(remoteDir);
		try {
			// Get the children
			IFileStore[] remoteStores = downloadFileStore.childStores(EFS.NONE, null);
			// Download all of the children
			for (IFileStore remoteFile : remoteStores) {
				// Get the information about the current child
				IFileInfo fileInfo = remoteFile.fetchInfo();
				if (fileInfo.getLength() < maxFileSize) {

					// Print some debug information about the download
					String msg = "RemoteFileDownloadAction Message: " + "Downloading " + fileInfo.getName()
							+ " with length " + fileInfo.getLength() + ".";
					logger.info(msg);

					// Get a handle to the local file. Note that it may
					// not exist yet.
					IFileStore childStore = localDirectory.getChild(remoteFile.getName());

					// Copy the file from the remote machine to the
					// local machine.
					remoteFile.copy(childStore, EFS.OVERWRITE, null);
				} else {
					long sizeDiff = fileInfo.getLength() - maxFileSize;
					// Print a debug note saying that the file is too
					// big to
					// download.
					String msg = "RemoteFileDownloadAction Message: " + "File exceeds download limit. "
							+ "File with size " + fileInfo.getLength() + " is " + sizeDiff + " bytes over the "
							+ maxFileSize + " byte limit.";
					logger.info(msg);
				}

			}
		} catch (CoreException e) {
			logger.error(getClass().getName() + " Exception! Error in downloading the files.", e);
			return FormStatus.InfoError;
		}

		return FormStatus.Processed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.action.Action#cancel()
	 */
	@Override
	public FormStatus cancel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.action.Action#getActionName()
	 */
	@Override
	public String getActionName() {
		return "Remote File Download";
	}

}
