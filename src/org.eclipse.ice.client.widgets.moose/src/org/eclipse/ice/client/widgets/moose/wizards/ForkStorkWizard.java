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
package org.eclipse.ice.client.widgets.moose.wizards;

import java.util.ArrayList;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * The ForkStorkWizard is a subclass of Wizard that provides a
 * ForkStorkWizardPage to get user input for forking the MOOSE Stork.
 * 
 * @author Alex McCaskey
 *
 */
public class ForkStorkWizard extends Wizard {

	/**
	 * Handle to the workbench window.
	 */
	protected IWorkbenchWindow workbenchWindow;

	/**
	 * Reference to the ForStorkWizardPage
	 */
	private ForkStorkWizardPage page;

	/**
	 * List of retrieved user input data from the ForkStorkWizardPage
	 */
	private ArrayList<String> forkData;

	/**
	 * A nullary constructor. This is used by the platform. <b>If called from an
	 * {@link IHandler}, use {@link #ForkStorkWizard(IWorkbenchWindow)} </b>.
	 */
	public ForkStorkWizard() {
		page = new ForkStorkWizardPage("Fork the Stork!");
		setWindowTitle("MOOSE Fork the Stork");
		this.setForcePreviousAndNextButtons(false);

	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers.
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public ForkStorkWizard(IWorkbenchWindow window) {
		// Initialize the default information.
		this();
		// Store a reference to the workbench window.
		workbenchWindow = window;
		page = new ForkStorkWizardPage("Fork the Stork!");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(page);
	}

	/**
	 * 
	 */
	@Override
	public boolean performFinish() {

		// Gather up all the data entered by the user
		forkData = new ArrayList<String>();
		forkData.add(page.getMooseAppName());
		forkData.add(page.getGitUserName());
		forkData.add(page.getGitPassword());

		return true;
	}

	/**
	 * Return the input Moose App name.
	 * 
	 * @return
	 */
	public String getMooseAppName() {
		return forkData.get(0);
	}

	/**
	 * Retrieve the users GitHub username
	 * 
	 * @return
	 */
	public String getGitUsername() {
		return forkData.get(1);
	}

	// Retrieve the GitHub password
	public String getGitPassword() {
		return forkData.get(2);
	}
}
