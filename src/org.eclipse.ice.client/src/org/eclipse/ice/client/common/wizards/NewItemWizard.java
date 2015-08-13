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

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * This class provides a wizard for creating ICE {@code Item}s.
 * <p>
 * This wizard should be created either via an {@link IHandler} (for adding it
 * to the workbench {@code ToolBar}) or by using an
 * {@code org.eclipse.ui.newWizards} extension in the plugin (for adding it to
 * the workbench new wizards).
 * </p>
 *
 * @author Jay Jay Billings, Jordan Deyton
 *
 */
public class NewItemWizard extends Wizard implements INewWizard {

	/**
	 * The wizard page used to create the Item.
	 */
	private NewItemWizardPage page;

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
	 * {@link IHandler}, use {@link #NewItemWizard(IWorkbenchWindow)} </b>.
	 */
	public NewItemWizard() {
		// Nothing to do.
	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers.
	 *
	 * @param window
	 *            The workbench window.
	 */
	public NewItemWizard(IWorkbenchWindow window) {
		// Initialize the default information.
		this();
		// Store a reference to the workbench window.
		workbenchWindow = window;
	}

	/**
	 * This is called when initialized by the Eclipse workbench (i.e., via the
	 * basic new wizard).
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// Save the window
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
		if (page == null) {
			page = new NewItemWizardPage("Select an Item to create");
		}
		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		// Setup the return value. Default to false so "finished" cannot be
		// clicked.
		boolean finished = false;

		// Get the client reference
		IClient client = ClientHolder.getClient();

		if (client != null) {

			// Get the selected Item from the dialog
			String selectedItem = page.getSelectedItem();

			// Direct the client to create a new Item if a selection was made
			if (selectedItem != null) {
				finished = client.createItem(selectedItem, project) > 0;
			}

		} else {
			// Throw an error if the client is not available.
			String msg = "ICE does not have any plugins configured! "
					+ "This most likely means that you are missing some "
					+ "required data files or that ICE hasn't completely "
					+ "loaded yet. In rare instances it may indicate a bug in "
					+ "the plug-in that you are trying to use. If you feel "
					+ "like you have configured everything properly, feel free "
					+ "to submit a bug report at niceproject.sourceforge.net "
					+ "and reference error code #2.";
			MessageDialog.openError(workbenchWindow.getShell(),
					"Unable to import files!", msg);
		}

		return finished;
	}

}
