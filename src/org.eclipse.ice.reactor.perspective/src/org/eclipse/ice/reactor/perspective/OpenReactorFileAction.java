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
package org.eclipse.ice.reactor.perspective;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This Action is used to open a FileDialog that allows the user to select
 * compatible files as {@link ICEResource}s into a "Resources" View.
 * 
 * @author Taylor Patterson
 */
public class OpenReactorFileAction extends Action {

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public OpenReactorFileAction(ViewPart parent) {
		viewer = parent;

		// Set the action's tool tip text.
		setText("Open reactor file");
		setToolTipText("Open a new reactor file");

		// Set the action's image (the folder button).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "folder.gif");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {

		// Get the Shell of the workbench
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		// Open the file system exploration dialog. Use SWT.OPEN for an open
		// file dialog and SWT.MULTI to allow multi-selection of files.
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);

		// Filter files by *.h5 or all files.
		String[] filterNames = new String[] { ".h5 Files", "All Files (*)" };
		String[] filterExtensions = new String[] { "*.h5", "*" };

		// Check the OS and adjust if on Windows.
		String platform = SWT.getPlatform();
		if ("win32".equals(platform) || "wpf".equals(platform)) {
			filterNames[1] = "All Files (*.*)";
			filterExtensions[1] = "*.*";
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
			ReactorViewer reactorViewer = (ReactorViewer) viewer;

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
					ICEResource resource = new ICEResource(file);
					reactorViewer.addReactorFile(resource);
				} catch (IOException e) {
					System.err.println("OpenReactorFileAction error: "
							+ "Failed to create an ICEResource for the file at"
							+ " \"" + filePath + "\".");
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("OpenReactorFileAction message: "
					+ "No file selected.");
		}

		return;
	}

}
