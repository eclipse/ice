/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.common.wizards;

import java.io.File;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ImportItemWizardHandler;
import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * This class provides a wizard for importing files into ICE for use with an
 * <code>Item</code>.
 * <p>
 * This wizard should be created either via an {@link IHandler} (for adding it
 * to the workbench <code>ToolBar</code>) or by using an
 * <code>org.eclipse.ui.importWizards</code> extension in the plugin (for adding
 * it to the workbench import wizards).
 * </p>
 * 
 * @author Jay Jay Billings, Jordan Deyton, Alex McCaskey
 * 
 */
public class ImportItemWizard extends ImportFileWizard {

	/**
	 * A nullary constructor. This is used by the platform. <b>If called from an
	 * {@link IHandler}, use {@link #ImportItemWizard(IWorkbenchWindow)} </b>.
	 */
	public ImportItemWizard() {
		super();
	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers (in this case, {@link ImportItemWizardHandler}).
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public ImportItemWizard(IWorkbenchWindow window) {
		super(window);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.wizards.ImportFileWizard#addPages()
	 */
	@Override
	public void addPages() {
		// Create the main page if necessary.
		if (page == null) {
			page = new ImportItemWizardPage("Import a file", project != null ? project.getName() : null);
		}
		// Add the main page.
		addPage(page);
	}

	/**
	 * Set the currently selected project.
	 * 
	 * @param proj
	 */
	public void setProject(IProject proj) {
		project = proj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		boolean finished = false;

		// Get the client.
		IClient client = null;

		try {
			client = IClient.getClient();
		} catch (CoreException e) {
			logger.error("Could not get a valid IClient reference.", e);
		}

		// Present a selection dialog if Items are available.
		if (client != null) {

			// Get the file to be imported.
			String fileName = ((ImportItemWizardPage) page).getSelectedFile();
			String filterPath = page.getFilterPath();
			File file = new File(filterPath, fileName);

			// Get the type of Item to create.
			String itemType = ((ImportItemWizardPage) page).getSelectedItem();

			// Get the name of the Project to import this File to
			String selectedProject = ((ImportItemWizardPage) page).getSelectedProject();
			
			// Import the new Item
			int id = client.importFileAsItem(file.toURI(), itemType, selectedProject);

			// Load the Item if successful
			if (id > 0) {
				client.loadItem(id);
				finished = true;
			} else {
				finished = false;
			}

		} else {
			// Throw an error if the client is not available.
			String msg = "ICE does not have any plugins configured! "
					+ "This most likely means that you are missing some "
					+ "required data files or that ICE hasn't completely "
					+ "loaded yet. In rare instances it may indicate a bug in "
					+ "the plug-in that you are trying to use. If you feel "
					+ "like you have configured everything properly, feel free "
					+ "to submit a bug report at niceproject.sourceforge.net " + "and reference error code #2.";
			MessageDialog.openError(workbenchWindow.getShell(), "Unable to import files!", msg);
		}

		return finished;
	}
}
