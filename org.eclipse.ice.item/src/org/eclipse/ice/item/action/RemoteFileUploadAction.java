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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.jaxbclassprovider.ICEJAXBClassProvider;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.core.IRemoteProcessService;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
* The RemoteFileUploadAction is a subclass of Action that uploads a list 
* of files to a remote host. Clients can specify those files in two ways: 
* (1) and XML-serialized DataComponent containing File Entries, (2) a local 
* directory name. This Action checks for the DataComponent first, and if it 
* is not provided tries to find files in the local directory. 
* 
* This Action requires a key-value pair list that contains the following:
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
* The name of the directory within the project/jobs folder where 
* the files to be uploaded can be found (optional).
* </p>
* </td>
* </tr>
* <tr>
* <td>
* <p>
* filesDataComponent
* </p>
* </td>
* <td>
* <p>
* The XML-serialized DataComponent String (optional). 
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

		// Now that we're executing, set the status 
		// as Processing 
		status = FormStatus.Processing;
		
		// Get the input parameters we need.
		String hostName = dictionary.get("hostname");
		String localFilesDir = dictionary.get("localJobLaunchDirectory");
		String filesDataComponent = dictionary.get("filesDataComponent");
		String projectDir = dictionary.get("projectSpaceDir");

		// Validate hostname and projectDir input parameters first
		if (hostName == null || projectDir == null) {
			return actionError("Invalid Host Name or Project Directory "
					+ "input parameters for RemoteFileUpload. See class "
					+ "documentation.", null);
		}

		// Get the project reference
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(new File(projectDir).getName());

		// We can get our files to upload in 2 ways:
		// (1) Clients can pass an XML serialized DataComponent
		// containing the file names as Entries
		// (2) If there is no XML, then we look for a localJobLaunchDirectory
		// and upload all files in that directory
		if (filesDataComponent != null) {
			// De-serialize the DataComponent with the file Entries. 
			DataComponent data = loadComponent(filesDataComponent);
			if (data == null) {
				return actionError("Remote File Upload - Invalid DataComponent XML.", null);
			}
			
			// Add those files to the list 
			for (IEntry e : data.retrieveAllEntries()) {
				filesToUpload.add(project.getFile(e.getName()).getLocation().toFile());
			}
		} else if (localFilesDir != null) {
			// If we weren't given a DataComponent, look for all files in the 
			//provided localJobLaunchDirectory folder
			try {
				for (IResource resource : new ExecutionHelper(dictionary).getLocalLaunchFolder().members()) {
					if (!resource.getName().contains("processOutput") && !resource.getName().contains("stderr")
							&& !resource.getName().contains("stdout")) {
						filesToUpload.add(resource.getLocation().toFile());
					}
				}
			} catch (CoreException e) {
				return actionError("Exception in adding files from " + localFilesDir + ". See error log.", e);
			}
		} else {
			return actionError("Invalid input parameters for Remote File Upload. Clients must "
					+ "provided an XML serialized DataComponent with files added as "
					+ "Entries, or a local job launch directory name.", null);
		}

		// Get the remote connection
		String connectionName = dictionary.get("remoteConnectionName");
		if (connectionName == null) {
			connection = getRemoteConnection(hostName);
		} else {
			IRemoteConnectionType connectionType = getService(IRemoteServicesManager.class).getRemoteConnectionTypes()
					.get(0);
			for (IRemoteConnection c : connectionType.getConnections()) {
				if (connectionName.equals(c.getName())) {
					connection = c;
				}
			}
		}
		if (connection == null) {
			return actionError("Remote File Upload could not get a valid connection to " + hostName + ".", null);
		}
		
		// Make sure the connection is actually open
		if (!connection.isOpen()) {
			try {
				connection.open(null);
			} catch (RemoteConnectionException e) {
				return actionError("Remote File Upload could not open the IRemoteConnection.", e);
			}
		}
		
		// Get the remote file manager
		IRemoteFileService fileManager = connection.getService(IRemoteFileService.class);

		// Get the IRemoteProcessService
		IRemoteProcessService processService = connection.getService(IRemoteProcessService.class);

		// Set the working directory as $HOME/ICEJobs/`localFilesLocation`
		String remoteSeparator = connection.getProperty(IRemoteConnection.FILE_SEPARATOR_PROPERTY);
		String userHome = connection.getProperty(IRemoteConnection.USER_HOME_PROPERTY);
		processService.setWorkingDirectory(userHome + remoteSeparator + "ICEJobs" + remoteSeparator + localFilesDir);

		// Get the working directory
		try {
			remoteDirectory = EFS.getStore(fileManager.toURI(processService.getWorkingDirectory()));
		} catch (CoreException e1) {
			return actionError("Remote File Upload could not get a reference to the remote working directory.", e1);
		}

		// Create the remote working directory and upload required files.
		try {
			// Make that directory if necessary
			remoteDirectory.mkdir(EFS.NONE, null);
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
				postConsoleText("Remote File Upload - Uploaded " + file.getName() + " to " + hostName + ":" + userHome
						+ remoteSeparator + "ICEJobs" + remoteSeparator + localFilesDir + ".");
				logger.info("RemoteFileUploadAction Message: " + "Uploaded file " + file.getName());
			}

		} catch (CoreException e) {
			// Print diagnostic information and fail
			return actionError("Remote File Upload could not upload file.", e);
		}

		// If we make it here, then we've successfully uploaded
		// the files.
		status = FormStatus.Processed;
		return status;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.action.Action#cancel()
	 */
	@Override
	public FormStatus cancel() {
		// Throw the flag
		cancelled.set(true);
		return FormStatus.ReadyToProcess;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.action.Action#getActionName()
	 */
	@Override
	public String getActionName() {
		return "Remote File Upload";
	}

	/**
	 * This operation loads an ICE Component from an XML String.
	 * 
	 * @param <T>
	 *
	 * @param file
	 *            The IFile that should be loaded as an Item from XML.
	 * @return the Item
	 */
	@SuppressWarnings("unchecked")
	private <T> T loadComponent(String xmlForm) {

		T comp = null;
		// Make an array to store the class list of registered Items
		ArrayList<Class> classList = new ArrayList<Class>();
		Class[] classArray = {};
		classList.addAll(new ICEJAXBClassProvider().getClasses());
		// Create new JAXB class context and unmarshaller
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(classList.toArray(classArray));
		} catch (JAXBException e1) {
			actionError("Remote File Upload could not get JAXBContext.", e1);
		}

		try {
			// Create the unmarshaller and load the item
			Unmarshaller unmarshaller = context.createUnmarshaller();
			comp = (T) unmarshaller.unmarshal(new ByteArrayInputStream(xmlForm.getBytes()));
		} catch (JAXBException e) {
			// Complain
			actionError("Remote File Upload error in unmarshalling XML data.", e);
			// Null out the Item so that it can't be returned uninitialized
			comp = null;
		}

		return comp;
	}

}
