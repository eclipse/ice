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
package org.eclipse.ice.client.common.wizards;

import java.io.File;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.ImportFileWizardHandler;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a wizard for importing files into ICE projects. This
 * wizard should be created either via an {@link IHandler} (for adding it to the
 * workbench <code>ToolBar</code>) or by using an
 * <code>org.eclipse.ui.importWizards</code> extension in the plugin (for adding
 * it to the workbench import wizards).
 * </p>
 * 
 * @author Jay Jay Billings, Jordan
 * 
 */
public class ImportFileWizard extends Wizard implements IImportWizard {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ImportFileWizard.class);

	/**
	 * A page containing widgets for importing one or more files.
	 */
	protected ImportFileWizardPage page;

	/**
	 * Handle to the workbench window.
	 */
	protected IWorkbenchWindow workbenchWindow;

	/**
	 * The project selected when the wizard was used.
	 */
	protected IProject project;

	/**
	 * A nullary constructor. This is used by the platform. <b>If called from an
	 * {@link IHandler}, use {@link #ImportFileWizard(IWorkbenchWindow)} </b>.
	 */
	public ImportFileWizard() {
		// Nothing to do.
	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers (in this case, {@link ImportFileWizardHandler}).
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public ImportFileWizard(IWorkbenchWindow window) {
		// Initialize the default information.
		this();
		// Store a reference to the workbench window.
		workbenchWindow = window;
	}

	/**
	 * This is called when initialized by the Eclipse workbench (i.e., via the
	 * basic import wizard).
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		workbenchWindow = workbench.getActiveWorkbenchWindow();

		// Get and save the project. This just casts to IResource since
		// IProjects are IResources and doing otherwise would require additional
		// checks.
		Object element = selection.getFirstElement();
		if (element instanceof IResource) {
			project = ((IResource) element).getProject();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		// Create the main page if necessary.
		if (page == null) {
			page = new ImportFileWizardPage("Import a file");
		}
		// Add the main page.
		addPage(page);
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
			e.printStackTrace();
			logger.error("Could not get a reference to the IClient.", e);
		}

		// Present a selection dialog if Items are available.
		if (client != null) {

			// Import the files.
			String filterPath = page.getFilterPath();
			for (String name : page.getSelectedFiles()) {
				File importedFile = new File(filterPath, name);
				if (project == null) {
					client.importFile(importedFile.toURI());
				} else {
					client.importFile(importedFile.toURI(), project);
				}
			}
			// We've successfully finished the wizard.
			finished = true;

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
