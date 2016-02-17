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
package org.eclipse.ice.item.nuclear;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Dictionary;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ice.datastructures.entry.FileEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.iterator.BreadthFirstTreeCompositeIterator;
import org.eclipse.ice.datastructures.jaxbclassprovider.ICEJAXBClassProvider;
import org.eclipse.ice.item.action.RemoteAction;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.IRemoteProcessService;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * The CheckMooseInputAction is an ICE Action that takes a constructed Moose
 * Input Tree and validates it using the provided Moose application's
 * --check-input command line argument. This Action works for both local and
 * remote applications.
 * 
 * This Action requires the following key-value pairs be passed to the 
 * execute method: projectName - the name of the IProject instance, isRemote - 
 * a boolean as a String indicating whether the Moose App is local/remote, 
 * inputTree - the Moose input tree in XML form, appComp - the Files DataComponent 
 * containing the App URI and output file name in XML form.
 * 
 * @author Alex McCaskey
 *
 */
public class CheckMooseInputAction extends RemoteAction {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> map) {
		// Local Declarations
		IRemoteProcessService processService = null;
		IRemoteProcess checkInputRemoteJob = null;
		MOOSEFileHandler writer = new MOOSEFileHandler();
		Form tempForm = new Form();
		InputStream errorStream = null;
		String checkInputString = "", line;
		status = FormStatus.ReadyToProcess;
		
		// Get the input params
		String projectName = map.get("projectSpaceDir");
		String treeXML = map.get("inputTree");
		String appCompXML = map.get("appComp");
		String localFilesDir = map.get("localJobLaunchDirectory");
		boolean isRemote = Boolean.valueOf(map.get("isRemote"));
		
		// Validate we got the right ones. 
		if (projectName == null || localFilesDir == null || treeXML == null || appCompXML == null || map.get("isRemote") == null) {
			logger.error("Invalid input for CheckMooseInputAction.");
			return FormStatus.InfoError;
		}
		
		// Convert the input XML to the objects we need
		TreeComposite mooseTree = loadComponent(treeXML);
		DataComponent appComponent = loadComponent(appCompXML);
		new MOOSEModel().setActiveDataNodes(mooseTree);
		
		// Get the IProject reference
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		// Get the output file reference and the application URI
		IFile inputFile = project.getFile(appComponent.retrieveEntry("Output File Name").getValue());
		URI appUri = URI.create(appComponent.retrieveEntry("MOOSE-Based Application").getValue());

		// Make sure we have the correct files in the workspace
		if (!validateFileEntries(project, mooseTree)) {
			status = FormStatus.InfoError;
			return status;
		}

		// We have to write the input to file
		tempForm.addComponent(mooseTree);
		writer.write(tempForm, inputFile);

		// Check if this MOOSE app is local or remote
		if (isRemote) {

			// Get the remote connection
			connection = getRemoteConnection(appUri.getHost());
			
			// Try to open the connection and fail if it will not open
			try {
				connection.open(null);
			} catch (RemoteConnectionException e) {
				// Print diagnostic information and fail
				logger.error(getClass().getName() + " Exception!", e);
				String errorMessage = "Could not create connection to remote machine.";
				throwErrorMessage("Connection Invalid", "", errorMessage, errorMessage);
				status = FormStatus.InfoError;
				return status;
			}

			// Do the upload(s) and launch the job if the connection is open
			if (connection.isOpen()) {

				// Get the file separator on the remote system
				String remoteSeparator = connection.getProperty(IRemoteConnection.FILE_SEPARATOR_PROPERTY);

				// Get the IRemoteProcessService
				processService = connection.getService(IRemoteProcessService.class);

				// Set the working directory to be where the files were uploaded
				processService.setWorkingDirectory("ICEJobs" + remoteSeparator + localFilesDir);
				
				logger.info("Setting Remote command to " + appUri.getRawPath() + " --no-color --check-input -i " + inputFile.getName());
				
				// Create the process builder for the remote job
				IRemoteProcessBuilder checkInputProcessBuilder = processService.getProcessBuilder("sh", "-c",
						appUri.getRawPath() + " --no-color --check-input -i " +inputFile.getName());

				// Do not redirect the streams
				//checkInputProcessBuilder.redirectErrorStream(true);

				logger.info("Remote Connection open - Beginning execution of Moose App with --check-input");
				// Execute the remote job
				try {
					checkInputRemoteJob = checkInputProcessBuilder.start(IRemoteProcessBuilder.FORWARD_X11);
				} catch (IOException e) {
					// Print diagnostic information and fail
					logger.error(getClass().getName() + " Exception!", e);
					String errorMessage = "Could not execute application --check-input on remote machine.";
					throwErrorMessage("--check-input execution failed.", "", errorMessage, errorMessage);
					status = FormStatus.InfoError;
					return status;
				}

				// Get the InputStream - this contains standard out 
				// and standard err
				errorStream = checkInputRemoteJob.getErrorStream();
				
				// Get the remote file manager
				//IRemoteFileService fileManager = connection.getService(IRemoteFileService.class);

//				try {
//					IFileStore fileStore = EFS.getStore(fileManager.toURI(processService.getWorkingDirectory()));
//					//fileStore.delete(EFS.NONE, null); HOLY FUCK ALEX WHAT WERE YOU THINKING!!!!!
//				} catch (CoreException e) {
//					e.printStackTrace();
//				}
			} else {
				// Print diagnostic information and fail
				logger.error(getClass().getName() + " Exception!");
				String errorMessage = "Could not open remote connection.";
				throwErrorMessage("Connection Error", "", errorMessage, errorMessage);
				status = FormStatus.InfoError;
				return status;
			}

		} else {

			// Create a File so we can easily get its file name
			File execFile = new File(appUri);

			// Create the exec strings
			String[] checkInputCmd = { "/bin/sh", "-c",
					execFile.getAbsolutePath() + " --check-input -i " + project.getLocation().toOSString()
							+ System.getProperty("file.separator") + inputFile.getName() + " --no-color" };

			try {

				// Launch the process
				ProcessBuilder builder = new ProcessBuilder(checkInputCmd)
						.directory(new File(project.getLocation().toOSString()));
				//builder.redirectErrorStream(true);
				Process checkInputProcess = builder.start();

				// Get the InputStream from the process - this contains 
				// standard out and standard err
				errorStream = checkInputProcess.getErrorStream();
				
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(getClass().getName() + " Exception!", e);
				status = FormStatus.InfoError;
				return status;
			}

		}

		// Now we have an InputStream from either a local or 
		// remote launch - let's get the text from that stream
		// and analyze it for errors. 
		try {
			// Read the output from the process
			BufferedReader error = new BufferedReader(new InputStreamReader(errorStream));
			while ((line = error.readLine()) != null) {
				//logger.info("Error: " + line);
				checkInputString += line + "\n";
			}
			
			// Close the streams
			//input.close();
			error.close();
			
			
			// Check for any errors
			if (checkInputString.contains("*** ERROR ***")) {
				int indexOfError = checkInputString.indexOf("*** ERROR ***");
				String errorString = checkInputString.substring(indexOfError,
						checkInputString.indexOf("\n", indexOfError+15));
				errorString = errorString.trim();
				throwErrorMessage("MOOSE Tree Validation", "org.eclipse.ice.item.nuclear.moose", errorString,
						checkInputString.trim());
				status = FormStatus.InfoError;
				return status;
			}

		} catch (IOException e) {
			logger.error(getClass().getName() + " Exception!", e);
			status = FormStatus.InfoError;
			return status;
		}
		
		// If we make it here, then we should be good with ReadyToProcess
		return status;
			
	}

	/**
	 * This method searches the Model input tree and locates all file Entries
	 * and loads them on the Model File DataComponent.
	 */
	private ArrayList<IEntry> getFileEntries(TreeComposite mooseTree) {
		// protected void loadFileEntries() {
		// Walk the tree and get all Entries that may represent a file
		ArrayList<IEntry> files = new ArrayList<IEntry>();
		BreadthFirstTreeCompositeIterator iter = new BreadthFirstTreeCompositeIterator(mooseTree);
		while (iter.hasNext()) {
			TreeComposite child = iter.next();

			// Make sure we have a valid DataComponent
			if (child.getActiveDataNode() != null && child.isActive()) {
				DataComponent data = (DataComponent) child.getActiveDataNode();
				for (IEntry e : data.retrieveAllEntries()) {

					// If the Entry's tag is "false" it is a commented out
					// parameter.
					if (!"false".equals(e.getTag()) && e.getValue() != null && !e.getValue().isEmpty()
							&& e instanceof FileEntry) {

						IEntry clonedEntry = (IEntry) e.clone();
						files.add(clonedEntry);
					}
				}
			}
		}

		return files;
	}

	/**
	 * This method is invoked before the check-input capability to verify that
	 * we have any files we may need for the simulation. Moose will throw an
	 * error before any input validation occurs if it can't find a file.
	 * 
	 * @return
	 */
	private boolean validateFileEntries(IProject project, TreeComposite tree) {
		refreshProjectSpace(project);

		// Loop over all file entries and make sure they exist
		for (final IEntry entry : getFileEntries(tree)) {
			try {
				// Check the entry value validity, if bad throw an exception
				if (entry.getValue().isEmpty() || !project.getFile(entry.getValue()).exists()) {
					throw new Exception("Error launching the Job, can't find file " + entry.getValue());
				}
			} catch (Exception e) {
				// Let's catch the Exception in a somewhat graceful way...
				logger.error(getClass().getName() + " Exception! ", e);
				String errorMessage = "The MOOSE Application could not be launched because all required files "
						+ "could not be found in " + project.getLocation().toOSString()
						+ ". Please click 'Browse' on the following Entry to import the files.\n\nFile = "
						+ (entry.getValue().isEmpty() ? entry.getName() : entry.getValue()) + "\n" + e.getMessage();

				// Invoke Item's throwErrorMessage to display a
				// descriptive error to the user
				throwErrorMessage("MOOSE Application Launch", "org.eclipse.ice.item.nuclear", "Missing Required Files",
						errorMessage);

				// Tell the client InfoError.
				return false;
			}

		}
		return true;
	}

	/**
	 * This operations allows subclasses to throw a visual error message to
	 * users of the subclassed Item to indicate an error in the use of the Item.
	 * This operation takes the title of the error, the java package location,
	 * and a descriptive error message, all of which is used to throw a
	 * descriptive UI message to the user. Subclasses that use this method are
	 * advised to set the proper FormStatus flag when this method is used, ie
	 * returning FormStatus.InfoError.
	 * 
	 * @param title
	 * @param packageLocation
	 * @param errorMessage
	 */
	protected void throwErrorMessage(String title, String packageLocation, String errorMessage, String detailsMessage) {
		// Local Declarations
		final String location = packageLocation;
		final String message = errorMessage;
		final String details = detailsMessage;

		// Create a "Failed Job", which gets run,
		// immediately fails and presents a dialog to the user
		// explaining what went wrong based on the provided errorMessage.
		Job badJob = new Job(title) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus s = new Status(IStatus.ERROR, location, 1, details, null);
				MultiStatus parent = new MultiStatus("plugin", IStatus.ERROR, message, null);
				parent.add(s);
				return parent;
			}
		};

		// Start the job
		badJob.schedule();

		return;
	}

	/**
	 * This utility method can be used by subclasses to refresh the project
	 * space after the addition or removal of files and folders.
	 */
	protected void refreshProjectSpace(IProject project) {
		// Refresh the Project just in case
		if (project != null) {
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}
		return;
	}

	/**
	 * This operation loads an ICE Component from an XML String.
	 * @param <T>
	 *
	 * @param file
	 *            The IFile that should be loaded as an Item from XML.
	 * @return the Item
	 */
	@SuppressWarnings("unchecked")
	public <T> T loadComponent(String xmlForm) {

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
			e1.printStackTrace();
			logger.error("Could not get JAXBContext.", e1);
		}

		try {
			// Create the unmarshaller and load the item
			Unmarshaller unmarshaller = context.createUnmarshaller();
			comp = (T) unmarshaller.unmarshal(new ByteArrayInputStream(xmlForm.getBytes()));
		} catch (JAXBException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
			// Null out the Item so that it can't be returned uninitialized
			comp = null;
		}

		return comp;
	}
	
	@Override
	public FormStatus cancel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getActionName() {
		return "Check Moose Input";
	}

}
