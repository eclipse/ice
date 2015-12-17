/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Alexander J. McCaskey, Robert Smith
 *******************************************************************************/
package org.eclipse.ice.item.action;

import java.util.Dictionary;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteFileService;

/**
 * The RemoteFileDownloadAction is an ICE Action that downloads files from a
 * remote directory to the current localhost. It requires the following input 
 * parameters: 
 * </p>
 * <table border="1">
 * <col width="50.0%"></col><col width="50.0%"></col>
 * <tr>
 * <td>
 * <p>
 * <b>Key</b>
 * </p>
 * </td>
 * <td>
 * <p>
 * <b>Value</b>
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * hostname
 * </p>
 * </td>
 * <td>
 * <p>
 * The name of the remote host to upload files to.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * localJobLaunchDirectory
 * </p>
 * </td>
 * <td>
 * <p>
 * The name of the directory within the project/jobs folder where the files to
 * be uploaded can be found (optional).
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * projectSpaceDir
 * </p>
 * </td>
 * <td>
 * <p>
 * The absolute path string of the ICE project directory. This can be found with
 * IProject.getLocation().toOSString().
 * </p>
 * </td>
 * </tr>
 * </table>
 *
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
	 * The Constructor
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
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Set the status to Processing since we are 
		// starting this execution.
		status = FormStatus.Processing;
		
		// Get the remote and local directory
		String localDir = dictionary.get("localJobLaunchDirectory");
		String hostName = dictionary.get("hostname");

		// Let's use parts of the ExecutionHelper...
		ExecutionHelper helper = new ExecutionHelper(dictionary);
		
		// Check that we've been given valid directories
		if (localDir == null || hostName == null) {
			return actionError("No remote host name or remote/local directory provided. Can't download files.", null);
		}

		// Get the remote connection
		connection = getRemoteConnection(hostName);
		if (connection == null) {
			return actionError("Could not get a valid connection to " + hostName, null);
		}
		
		// Get the remote file manager
		IRemoteFileService fileManager = connection.getService(IRemoteFileService.class);

		// Get the Local Directory
		IFileStore localDirectory = EFS.getLocalFileSystem()
				.fromLocalFile(helper.getLocalLaunchFolder().getLocation().toFile());

		// Get the remote directory.
		String remoteSeparator = connection.getProperty(IRemoteConnection.FILE_SEPARATOR_PROPERTY);
		String userHome = connection.getProperty(IRemoteConnection.USER_HOME_PROPERTY);
		IFileStore downloadFileStore = fileManager.getResource(userHome + remoteSeparator + "ICEJobs" + remoteSeparator + localDir);
		
		// Try to download the files. 
		try {
			// Get the children
			IFileStore[] remoteStores = downloadFileStore.childStores(EFS.NONE, null);
			// Download all of the children
			for (IFileStore remoteFile : remoteStores) {
				// Get the information about the current child
				IFileInfo fileInfo = remoteFile.fetchInfo();
				if (fileInfo.getLength() < maxFileSize) {

					// Print some debug information about the download
					String msg = "Remote File Download - " + "Downloading " + fileInfo.getName()
							+ " with length " + fileInfo.getLength() + ".";
					logger.info(msg);
					postConsoleText(msg);
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
					postConsoleText(msg);
					logger.info(msg);
				}

			}
		} catch (CoreException e) {
			return actionError(getClass().getName() + " Exception! Error in downloading the files.", e);
		}

		status = FormStatus.Processed;
		return status;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.action.Action#cancel()
	 */
	@Override
	public FormStatus cancel() {
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
