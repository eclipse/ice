/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.common;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.client.widgets.wizards.ImportFileWizard;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class is an action for importing files.
 * 
 * @author Jay Jay Billings
 * 
 * @see ImportFileWizardHandler
 * @see ImportFileWizard
 */
public class ImportFileAction extends Action implements
		IWorkbenchAction {

	// FIXME This class is no longer used due to using the
	// org.eclipse.ui.commands extensions to add the ImportFileWizardHandler to
	// the main toolbar.

	// Handle to the workbench window
	private final IWorkbenchWindow workbenchWindow;

	// Action ID - for internal use
	public static final String ID = "org.eclipse.ice.client.common.importFileAction";

	/**
	 * Constructor
	 */
	public ImportFileAction(IWorkbenchWindow window) {

		// Set the action ID, Menu Text, and a tool tip.
		workbenchWindow = window;

		// Set the text properties.
		setId(ID);
		setText("&Import a file");
		setToolTipText("Import a file into ICE's "
				+ "project space for use by your items.");

		// Find the client bundle
		Bundle bundle = FrameworkUtil.getBundle(ImportFileAction.class);
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "importArrow.gif");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		setImageDescriptor(ImageDescriptor.createFromURL(imageURL));

		return;
	}

	/**
	 * This operation runs the action.
	 */
	@Override
	public void run() {

		// Get the Client
		IClient client = ClientHolder.getClient();

		// Create the dialog and get the files
		FileDialog fileDialog = new FileDialog(workbenchWindow.getShell(),
				SWT.MULTI);
		fileDialog.setText("Select a file to import into ICE");
		fileDialog.open();

		// Import the files
		String filterPath = fileDialog.getFilterPath();
		for (String name : fileDialog.getFileNames()) {
			File importedFile = new File(filterPath, name);
			client.importFile(importedFile.toURI());
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {
		// Nothing to do.
	}

}
