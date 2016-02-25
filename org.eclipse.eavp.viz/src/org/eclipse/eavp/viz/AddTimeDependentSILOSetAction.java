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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

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
 * This action adds a set of time-dependent SILO files. The files must have file
 * names that include the time step represented in that file. For example:
 * wave0000.silo, wave0001.silo, wave0002.silo, etc. This Action assumes the
 * files have been ordered by the SWT FileDialog.
 * 
 * @author Alex McCaskey
 * 
 */
public class AddTimeDependentSILOSetAction extends Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AddTimeDependentSILOSetAction.class);

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
	public AddTimeDependentSILOSetAction(ViewPart parentView,
			AddFileAction parentAction) {

		// Keep track of the viewer and parent Action containing this Action
		viewer = parentView;
		this.parentAction = parentAction;

		// Set the display text
		setText("Add a SILO file set");
	}

	/**
	 * The function called whenever the Action is selected from the drop-down.
	 * It will grab the set of silo files and create a .visit file from them
	 * which just sequentially lists the files, separated by a newline
	 * character.
	 */
	@Override
	public void run() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String[] fileNames;
		String visitFileName = null;
		ArrayList<String> siloFilesToAdd;
		IVizResource vizResource = null, child = null;
		ArrayList<IVizResource> children = new ArrayList<IVizResource>();

		// Filter files by images (*.silo) or all files.
		String[] filterNames = new String[] { "All Files (*)", ".silo Files",
				"VisIt Files" };
		String[] filterExtensions = new String[] { "*.silo" };

		// Set this as the default action for the parent Action
		parentAction.setDefaultAction(this);

		// Get the Shell of the workbench
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		// Open the file system exploration dialog. Use SWT.OPEN for an open
		// file dialog and SWT.MULTI to allow multi-selection of files.
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);

		// Check the OS and adjust if on Windows.
		String platform = SWT.getPlatform();
		if ("win32".equals(platform) || "wpf".equals(platform)) {
			filterNames[0] = "All Files (*.*)";
			filterExtensions[0] = "*.*";
		}

		// Set the dialog's file filters.
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);

		// If a file was selected in the dialog, create an VizResource for it
		// and add it to the viewer.
		if (dialog.open() != null) {
			// Get a reference to the VizFileViewer.
			VizFileViewer vizViewer = (VizFileViewer) viewer;
			// The file names selected
			fileNames = dialog.getFileNames();
			visitFileName = dialog.getFilterPath() + separator
					+ "visit_time_dependent.visit";
			siloFilesToAdd = new ArrayList<String>(Arrays.asList(fileNames));

			// Write the .visit file
			try {
				Files.write(Paths.get(visitFileName), siloFilesToAdd,
						Charset.defaultCharset(), StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);

				for (String name : fileNames) {
					child = new VisualizationResource(new File(dialog.getFilterPath()
							+ separator + name));
					child.setHost("localhost");
					children.add(child);
				}

				// Create the VizResource from it
				vizResource = new VisualizationResource(new File(visitFileName));// ,
																		// children);

				// Set the host, this should just be local
				vizResource.setHost("localhost");

				// Add the File names so they show up in the tree
				vizResource.setFileSet(fileNames);

				// Give the .visit file to the FileViewer
				vizViewer.addFile(vizResource);

			} catch (IOException e) {
				logger.error(getClass().getName() + " Exception!",e);
			}

		} else {
			logger.info("AddFileSetAction message: No file selected.");
		}

		return;
	}
}
