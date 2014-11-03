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
package org.eclipse.ice.client.common;

import java.net.URL;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.osgi.framework.Bundle;

/**
 * This class launches the ImportWizard for importing ICE Items.
 * 
 * @author Jay Jay Billings
 * 
 */
public class OpenImportWizardAction extends Action implements IWorkbenchAction {

	/**
	 * Handle to the workbench window
	 */
	private final IWorkbenchWindow workbenchWindow;

	/**
	 * Action ID
	 */
	public static final String ID = "org.eclipse.ice.client.common.OpenImportWizardAction";

	/**
	 * Constructor
	 * 
	 * @param IWorkbenchWindow
	 *            window - the window that should be used by the action
	 */
	public OpenImportWizardAction(IWorkbenchWindow window) {
		// Local Declarations
		Bundle bundle = null;
		Path importerPath = null;
		URL importerImageURL = null;
		ImageDescriptor importerImage = null;

		// Set the window handle
		workbenchWindow = window;

		// Find the client bundle
		bundle = Platform.getBundle("org.eclipse.ice.client.widgets");

		// Create the image descriptor for the create item action
		importerPath = new Path("icons/itemImport.gif");
		importerImageURL = FileLocator.find(bundle, importerPath, null);
		importerImage = ImageDescriptor.createFromURL(importerImageURL);

		// Set the action ID, Menu Text, and a tool tip.
		setId(ID);
		setText("&Import an Item");
		setToolTipText("Import an input file for an Item into ICE.");
		setImageDescriptor(importerImage);

		return;
	}

	/**
	 * This operation runs the action.
	 */
	@Override
	public void run() {

		// Get the Client
		IClient client = ClientHolder.getClient();

		// Add wizard stuff
		WizardDialog importDialog = new WizardDialog(
				workbenchWindow.getShell(), new ImportWizard());
		importDialog.open();

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {

		return;
	}

}
