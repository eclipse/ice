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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.client.widgets.wizards.ImportItemWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class launches the ImportWizard for importing ICE Items.
 * 
 * @author Jay Jay Billings
 * 
 * @see ImportItemWizardHandler
 * @see ImportItemWizard
 */
public class ImportItemWizardAction extends Action implements IWorkbenchAction {

	// FIXME This class is no longer used due to using the
	// org.eclipse.ui.commands extensions to add the ImportItemWizardHandler to the
	// main toolbar.

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
	public ImportItemWizardAction(IWorkbenchWindow window) {

		// Set the window handle
		workbenchWindow = window;

		// Set the text properties.
		setId(ID);
		setText("&Import an Item");
		setToolTipText("Import an input file for an Item into ICE.");

		// Find the client bundle
		Bundle bundle = FrameworkUtil.getBundle(ImportItemWizardAction.class);
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "itemImport.gif");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		setImageDescriptor(ImageDescriptor.createFromURL(imageURL));

		return;
	}

	/**
	 * This operation runs the action.
	 */
	@Override
	public void run() {

		// Add wizard stuff
		WizardDialog importDialog = new WizardDialog(
				workbenchWindow.getShell(), new ImportItemWizard(
						workbenchWindow));
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
		// Nothing to do.
	}

}
