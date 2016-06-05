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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.january.form.FormStatus;

/**
 * The LocalFilesCopyAction is a subclass of Action that copies files 
 * required for a local or remote job execution to a local working directory.
 * 
 * It requires a key-value pair list
 * that contains the following:
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
 * executable
 * </p>
 * </td>
 * <td>
 * <p>
 * The name of the executable as it exists on the system path or, alternatively,
 * the fully qualified path to the executable.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * inputFile
 * </p>
 * </td>
 * <td>
 * <p>
 * The path of the input file used by the executable. This may, alternatively,
 * represent any stream of characters.
 * </p>
 * </td>
 * </tr>
 * * <tr>
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
 * stdOutFile
 * </p>
 * </td>
 * <td>
 * <p>
 * The path of the file to which information printed to stdout by the job should
 * be written.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * stdErrFile
 * </p>
 * </td>
 * <td>
 * <p>
 * The path of the file to which information printed to stderr by the job should
 * be written.
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
 * <tr>
 * <td>
 * <p>
 * installDir
 * </p>
 * </td>
 * <td>
 * <p>
 * The directory in which the executable is installed on the target platform.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * numProcs
 * </p>
 * </td>
 * <td>
 * <p>
 * The number of MPI processes that should be used by the job. The default value
 * of this is always 1.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * numTBBThreads
 * </p>
 * </td>
 * <td>
 * <p>
 * The number of Intel TBB threads that should be used by the job. The default
 * value of this is always 1.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * os
 * </p>
 * </td>
 * <td>
 * <p>
 * The operating system of the target host.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * accountCode
 * </p>
 * </td>
 * <td>
 * <p>
 * The account code or project name that should be used for billing on the
 * remote machine.
 * </p>
 * </td>
 * </tr>
 * * <tr>
* <td>
* <p>
* os
* </p>
* </td>
* <td>
* <p>
* The name of the operating system.
* </p>
* </td>
* </tr>
 * </table>
 *
 * @author Alex McCaskey
 *
 */
public class LocalFilesCopyAction extends Action {

	/**
	 * Reference to the IProject instance for this Action.
	 */
	private IProject project;

	/**
	 * Reference to the IFolder where this local execution is to take place.
	 */
	private IFolder localLaunchFolder;

	/**
	 * Reference to the ExecutionHelper which is used to perform pre and post
	 * processing tasks for this Action.
	 */
	private ExecutionHelper helper;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.action.Action#execute(java.util.Dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {

		// Create the ExecutionHelper. 
		helper = new ExecutionHelper(dictionary);

		// Validate the data
		if (helper.isDataValid()) {
			status = FormStatus.Processing;
			// Get a reference to the IProject and IFolders
			// for this local execution
			project = helper.getProject();
			localLaunchFolder = helper.getLocalLaunchFolder();

			// Get the actual executable String, with all flags
			// replaced. Just doing this to figure out what 
			// files we need copied.
			helper.fixExecutableName();

			// Copy all files needed to the local launch directory
			try {
				for (String fileName : helper.getInputFileMap().keySet()) {
					logger.info("LocalFilesCopyAction copying " + fileName + " to local job launch folder: "
							+ localLaunchFolder.getLocation().toOSString() + ".");
					IFile newFile = localLaunchFolder.getFile(fileName);
					newFile.create(project.getFile(fileName).getContents(), true, null);
				}
			} catch (CoreException e) {
				return actionError(
						"LocalExecutionAction Error - Could not copy files from the project space to the job folder.",
						e);
			}
			
			status = FormStatus.Processed;
			return status;
		} else {
			return actionError("Invalid input for Local Files Copy. See class documentation.", null);
		}
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
		// TODO Auto-generated method stub
		return "Local Files Copy";
	}

}
