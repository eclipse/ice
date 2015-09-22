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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Dictionary;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.iterator.BreadthFirstTreeCompositeIterator;
import org.eclipse.ice.item.action.Action;
import org.eclipse.ice.item.action.RemoteFileUploadAction;
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
 * @author Alex McCaskey
 *
 */
public class CheckMooseInputAction extends Action {

	/**
	 * Reference to the Moose Tree to validate.
	 */
	private TreeComposite mooseTree;

	/**
	 * Reference to the IRemoteConnection for a remote application. This should
	 * be null if the application is local.
	 */
	private IRemoteConnection connection;

	/**
	 * Reference to the DataComponent containing the Moose Application URI
	 * string and input file name.
	 */
	private DataComponent appComponent;

	/**
	 * Reference to the IProject for the Moose Item.
	 */
	private IProject project;

	/**
	 * The constructor, takes the input tree to be validated, the DataComponent
	 * containing the application and file name, the MOOSE Item's IProject
	 * instance, and a possible IRemoteConnection if the application is remote.
	 * 
	 * @param inputTree
	 * @param data
	 * @param proj
	 * @param conn
	 */
	public CheckMooseInputAction(TreeComposite inputTree, DataComponent data, IProject proj, IRemoteConnection conn) {
		mooseTree = inputTree;
		connection = conn;
		appComponent = data;
		project = proj;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {
		// Local Declarations
		IRemoteProcessService processService = null;
		IRemoteProcess checkInputRemoteJob = null;
		MOOSEFileHandler writer = new MOOSEFileHandler();
		Form tempForm = new Form();
		String checkInputString = "", line;
		status = FormStatus.ReadyToProcess;
		IFile inputFile = project.getFile(appComponent.retrieveEntry("Output File Name").getValue());
		URI appUri = URI.create(appComponent.retrieveEntry("MOOSE-Based Application").getValue());

		// Make sure we have the correct files in the workspace
		if (!validateFileEntries()) {
			status = FormStatus.InfoError;
			return status;
		}

		// We have to write the input to file
		tempForm.addComponent(mooseTree);
		writer.write(tempForm, inputFile);

		// Check if this MOOSE app is local or remote
		if (connection != null) {

			// If remote, we have to upload the input file and
			// the files it references to correctly validate the tree

			// Create a list of IFiles and add the input file to it
			ArrayList<IFile> files = new ArrayList<IFile>();
			files.add(inputFile);

			// Now add all files it needs
			// FYI This list has already been validated and shown to exist
			// in Moose.fullTreeValidation, if you are using this somewhere
			// else, make sure they exist!
			for (Entry fileE : getFileEntries()) {
				files.add(project.getFile(fileE.getValue()));
			}

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

				// Upload the input files
				RemoteFileUploadAction uploadAction = new RemoteFileUploadAction(files, connection);
				uploadAction.execute(null);

				// Get the IRemoteProcessService
				processService = connection.getService(IRemoteProcessService.class);

				// Set the working directory to be where the files were uploaded
				processService.setWorkingDirectory(uploadAction.getRemoteUploadDirectoryPath());

				// Create the process builder for the remote job
				IRemoteProcessBuilder checkInputProcessBuilder = processService.getProcessBuilder("sh", "-c",
						appUri.getRawPath() + " --no-color --check-input -i "
								+ uploadAction.getRemoteUploadDirectoryPath() + System.getProperty("file.separator")
								+ inputFile.getName());

				// Do not redirect the streams
				checkInputProcessBuilder.redirectErrorStream(true);

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

				// Monitor the job
				while (!checkInputRemoteJob.isCompleted()) {
					// Give it a second
					try {
						Thread.currentThread();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// Complain
						logger.error(getClass().getName() + " Exception!", e);
					}
				}

				// Get the output
				try {
					// Read the output from the process
					BufferedReader input = new BufferedReader(
							new InputStreamReader(checkInputRemoteJob.getInputStream()));
					while ((line = input.readLine()) != null) {
						System.out.println(line);
						checkInputString += line + "\n";
					}

					input.close();

					// Check for any errors
					if (checkInputString.contains("ERROR")) {
						String errorString = checkInputString.substring(checkInputString.indexOf("*** ERROR ***"),
								checkInputString.indexOf("Stack"));
						errorString = "-------------- Error Summary --------------\n" + errorString.trim()
								+ "\n----------------------------------------------";
						throwErrorMessage("MOOSE Tree Validation", "org.eclipse.ice.item.nuclear.moose", errorString,
								"\n--------- Full Moose Stack Trace ---------\n" + checkInputString.trim());
						status = FormStatus.InfoError;
						return status;
					}

				} catch (IOException e) {
					logger.error(getClass().getName() + " Exception!", e);
					status = FormStatus.InfoError;
					return status;
				}

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
				builder.redirectErrorStream(true);
				System.out.println("COMMAND: " + builder.command());
				Process checkInputProcess = builder.start();
				System.out.println("Starting the local process");

				BufferedReader input = new BufferedReader(new InputStreamReader(checkInputProcess.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line);
					checkInputString += line;
				}
				input.close();

			} catch (IOException e) {
				e.printStackTrace();
				logger.error(getClass().getName() + " Exception!", e);
				status = FormStatus.InfoError;
				return status;
			}

			System.out.println("STRING IS " + checkInputString);

			if (checkInputString.contains("ERROR")) {
				checkInputString = checkInputString.substring(checkInputString.indexOf("*** ERROR ***"),
						checkInputString.length());
				throwErrorMessage("MOOSE Tree Validation", "org.eclipse.ice.item.nuclear.moose", "error",
						checkInputString);
				status = FormStatus.InfoError;
				return status;
			}

		}

		// If we make it here, then we should be good with ReadyToProcess
		return status;
	}

	/**
	 * This method searches the Model input tree and locates all file Entries
	 * and loads them on the Model File DataComponent.
	 */
	private ArrayList<Entry> getFileEntries() {
		// protected void loadFileEntries() {
		// Walk the tree and get all Entries that may represent a file
		ArrayList<Entry> files = new ArrayList<Entry>();
		BreadthFirstTreeCompositeIterator iter = new BreadthFirstTreeCompositeIterator(mooseTree);
		while (iter.hasNext()) {
			TreeComposite child = iter.next();

			// Make sure we have a valid DataComponent
			if (child.getActiveDataNode() != null && child.isActive()) {
				DataComponent data = (DataComponent) child.getActiveDataNode();
				for (Entry e : data.retrieveAllEntries()) {

					// If the Entry's tag is "false" it is a commented out
					// parameter.
					if (!"false".equals(e.getTag()) && e.getValue() != null && !e.getValue().isEmpty()
							&& e.getValueType().equals(AllowedValueType.File)) {

						Entry clonedEntry = (Entry) e.clone();
						files.add(clonedEntry);
					}
				}
			}
		}

		return files;
	}

	private boolean validateFileEntries() {
		refreshProjectSpace();

		// Loop over all file entries and make sure they exist
		for (final Entry entry : getFileEntries()) {
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
	protected void refreshProjectSpace() {
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

	@Override
	public FormStatus cancel() {
		// TODO Auto-generated method stub
		return null;
	}

}
