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
 * This Action presents a FileDialog to select files from the local file system
 * and add them to the {@link VizFileViewer}.
 * 
 * @author Taylor Patterson
 */
public class AddLocalFileAction extends Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AddLocalFileAction.class);

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
	public AddLocalFileAction(ViewPart parentView, AddFileAction parentAction) {

		// Keep track of the viewer and parent Action containing this Action
		viewer = parentView;
		this.parentAction = parentAction;

		// Set the display text
		setText("Add a local file");
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

		// Filter files by all files, .csv files, or VisIt (*.silo, *.e) files.
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

			// Loop over the selected files and create an ICEResource for each
			// one. The resources can be passed to the VizFileViewer.
			for (String fileName : dialog.getFileNames()) {
				// Construct a file from the fileName. The names are relative to
				// the dialog's filter path.
				String filePath = dialog.getFilterPath() + separator + fileName;
				File file = new File(filePath);

				// Try to construct an ICEResource from the File, then add it to
				// the viewer.
				try {
					IVizResource resource = new VisualizationResource(file);
					resource.setHost("localhost");
					vizViewer.addFile(resource);
				} catch (IOException e) {
					System.err.println("AddLocalFileAction error: Failed to "
							+ "create an ICEResource for the file at \""
							+ filePath + "\".");
					logger.error(getClass().getName() + " Exception!",e);
				}
			}
		} else {
			logger.info("AddLocalFileAction message: No file selected.");
		}

		return;
	}

}
