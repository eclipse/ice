/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz;

import org.eclipse.eavp.viz.service.datastructures.resource.IVizResource;
import org.eclipse.eavp.viz.service.datastructures.resource.VisualizationResource;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The action to add a file set where each file is a separate time
 * 
 * @author Matthew Wang
 * 
 */
public class AddFileSetAction extends Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AddFileSetAction.class);
	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The AddFileAction whose drop-down owns the object of this class.
	 */
	private AddFileAction parentAction;

	/**
	 * The constructor
	 * 
	 * @param parentView
	 *            The ViewPart to whom the object of this class belongs.
	 * @param parentAction
	 *            The AddFileAction to whom the object of this class belongs.
	 */
	public AddFileSetAction(ViewPart parentView, AddFileAction parentAction) {

		// Keep track of the viewer and parent Action containing this Action
		viewer = parentView;
		this.parentAction = parentAction;

		// Set the display text
		setText("Add a file set");
	}

	/**
	 * The function called whenever the Action is selected from the drop-down.
	 */
	@Override
	public void run() {

		// Set this as the default action for the parent Action
		parentAction.setDefaultAction(this);

		// Get the Shell of the workbench
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		// Open the file system exploration dialog. Use SWT.OPEN for an open
		// file dialog and SWT.MULTI to allow multi-selection of files.
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);

		// Filter files by images (*.silo) or all files.
		String[] filterNames = new String[] { "All Files (*)", ".csv Files",
				"VisIt Files" };
		String[] filterExtensions = new String[] { "*", "*.csv", "*.silo;*.e" };

		// Check the OS and adjust if on Windows.
		String platform = SWT.getPlatform();
		if ("win32".equals(platform) || "wpf".equals(platform)) {
			filterNames[0] = "All Files (*.*)";
			filterExtensions[0] = "*.*";
		}

		// Set the dialog's file filters.
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);

		// Get the OS file separator character.
		String separator = System.getProperty("file.separator");

		// Set the default location.
		String filterPath = System.getProperty("user.home") + separator
				+ "ICEFiles" + separator + "default" + separator;
		dialog.setFilterPath(filterPath);

		// If a file was selected in the dialog, create an ICEResource for it
		// and add it to the viewer.
		if (dialog.open() != null) {
			// Get a reference to the VizFileViewer.
			VizFileViewer vizViewer = (VizFileViewer) viewer;
			// The file names selected
			String[] fileNames = dialog.getFileNames();
			// Create a new vizResource for a fileSet
			IVizResource vizResource = new VisualizationResource();
			// Name of the resource
			String resourceName = "";
			resourceName += fileNames[0];
			fileNames[0] = dialog.getFilterPath() + separator + fileNames[0];
			// Loop to set the file names with their file paths and name the
			// resource
			for (int i = 1; i < fileNames.length; i++) {
				// Set the resource name by appending all the other names
				resourceName += ", " + fileNames[i];
				// Set the file paths and separator to the file names
				fileNames[i] = dialog.getFilterPath() + separator
						+ fileNames[i];
			}
			// Set the name of the resource
			vizResource.setName(resourceName);
			// Set the title of the VizResource
			vizResource.setFileSetTitle(resourceName);
			// set the fileSet of the vizResource
			vizResource.setFileSet(fileNames);
			// vizViewer adds the resource
			vizViewer.addFile(vizResource);
		} else {
			logger.info("AddFileSetAction message: No file selected.");
		}

		return;
	}
}
