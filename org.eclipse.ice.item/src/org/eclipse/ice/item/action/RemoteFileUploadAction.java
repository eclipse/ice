/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
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
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.core.IRemoteProcessService;

/**
 * The RemoteFileUpload Action is a subclass of ICE Action that takes a list of
 * IFiles and a valid IRemoteConnection and uploads the files to the host
 * specified by the connection. By default, this Action uploads the files to
 * $HOME/iceLaunch_$timestamp.
 * 
 * @author Alex McCaskey
 *
 */
public class RemoteFileUploadAction extends RemoteAction {

	/**
	 * Reference to the list of files to be uploaded
	 */
	private List<File> filesToUpload;

	/**
	 * AtomicBoolean to handle cancellations.
	 */
	private AtomicBoolean cancelled;

	/**
	 * Reference to the remotely hosted directory that will contain the uploaded
	 * files.
	 */
	private IFileStore remoteDirectory;

	/**
	 * The constructor. Takes a list of files to upload and a remote connection
	 * for the remote host.
	 * 
	 * @param files
	 *            List of files to upload
	 * 
	 */
	public RemoteFileUploadAction(List<File> files) {
		super();
		filesToUpload = files;
		cancelled = new AtomicBoolean(false);
	}

	/**
	 * The nullary constructor
	 */
	public RemoteFileUploadAction() {
		filesToUpload = new ArrayList<File>();
		cancelled = new AtomicBoolean(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 * 
	 * This implementation of Action.execute creates the remote working
	 * directory and then uploads the files to that directory.
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Get the input parameters.
		String workingDirectoryBaseName = dictionary.get("remoteDir");
		String hostName = dictionary.get("remoteHost");
		String filesString = dictionary.get("uploadFiles");
		String localFilesDir = dictionary.get("localFilesLocation");

		// Validate input parameters
		if (hostName == null || workingDirectoryBaseName == null) {
			logger.error("Invalid input parameters for RemoteFileUpload. See class documentation.");
			return FormStatus.InfoError;
		}

		if (filesToUpload.isEmpty()) {
			if (filesString == null && localFilesDir == null) {
				logger.error("Invalid input parameters for RemoteFileUpload. See class documentation.");
				return FormStatus.InfoError;
			}
			for (String filePath : filesString.split(";")) {
				filesToUpload.add(new File(filePath));
			}
		}

		// Get the remote connection
		connection = getRemoteConnection(hostName);
		if (connection == null) {
			logger.error("Could not get a valid connection to " + hostName);
			return FormStatus.InfoError;
		}

		// Get the remote file manager
		IRemoteFileService fileManager = connection.getService(IRemoteFileService.class);

		// Get the IRemoteProcessService
		IRemoteProcessService processService = connection.getService(IRemoteProcessService.class);

		// Get the working directory
		IFileStore fileStore;
		try {
			fileStore = EFS.getStore(fileManager.toURI(processService.getWorkingDirectory()));
		} catch (CoreException e1) {
			logger.error(getClass().getName() + " Exception!", e1);
			status = FormStatus.InfoError;
			return status;
		}

		// Create the remote working directory and upload required files.
		try {
			remoteDirectory = fileStore.getChild(workingDirectoryBaseName).mkdir(EFS.NONE, null);
			logger.info("RemoteFileUploadAction Message: " + "Created directory on remote system, "
					+ remoteDirectory.getName());

			// Loop over all of the files in the file table and upload them
			for (File file : filesToUpload) {

				// Check to see if the job should be cancelled.
				if (cancelled.get()) {
					break;
				}

				// Get a handle where the input file will be stored
				// remotely
				IFileStore remoteFileStore = remoteDirectory.getChild(file.getName());

				// Get a file store handle to the local copy of the
				// input file
				IFileStore localFileStore = EFS.getLocalFileSystem().fromLocalFile(file);

				// Copy the local file to the remote file
				localFileStore.copy(remoteFileStore, EFS.OVERWRITE, null);
				logger.info("RemoteFileUploadAction Message: " + "Uploaded file " + file.getName());
			}

		} catch (CoreException e) {
			// Print diagnostic information and fail
			logger.error(getClass().getName() + " Exception!", e);
			status = FormStatus.InfoError;
			return status;
		}

		// If we make it here, then we've successfully uploaded
		// the files.
		status = FormStatus.Processing;
		return status;
	}

	@Override
	public FormStatus cancel() {
		// Throw the flag
		cancelled.set(true);
		return FormStatus.ReadyToProcess;
	}

	/**
	 * Return the absolute path for the remotely hosted directory as a String.
	 * 
	 * @return
	 */
	public String getRemoteUploadDirectoryPath() {
		return remoteDirectory.toURI().getRawPath();
	}

	public IFileStore getRemoteUploadDirectory() {
		return remoteDirectory;
	}

	/**
	 * Remove the remote directory.
	 */
	public void deleteUploadRemoteDirectory() {
		try {
			remoteDirectory.delete(EFS.NONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			logger.error(getClass().getName() + " Exception!", e);
		}
	}

	@Override
	public String getActionName() {
		return "Remote File Upload";
	}

}
