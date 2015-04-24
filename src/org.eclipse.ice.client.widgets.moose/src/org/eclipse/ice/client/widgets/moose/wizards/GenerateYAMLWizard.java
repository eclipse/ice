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
 * 
 * @author Alex McCaskey
 *
 */
public class GenerateYAMLWizard extends Wizard {

	/**
	 * Handle to the workbench window.
	 */
	protected IWorkbenchWindow workbenchWindow;

	/**
	 * 
	 */
	private GenerateYAMLWizardPage page;
	
	/**
	 * 
	 */
	private ArrayList<String> launchData;
	
	/**
	 * A nullary constructor. This is used by the platform. <b>If called from an
	 * {@link IHandler}, use {@link #NewItemWizard(IWorkbenchWindow)} </b>.
	 */
	public GenerateYAMLWizard() {
		page = new GenerateYAMLWizardPage("Generate MOOSE YAML/Action Syntax Files");
		setWindowTitle("MOOSE YAML/Action Syntax File Generation");
		this.setForcePreviousAndNextButtons(false);
		
	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers.
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public GenerateYAMLWizard(IWorkbenchWindow window) {
		// Initialize the default information.
		this();
		// Store a reference to the workbench window.
		workbenchWindow = window;
		page = new GenerateYAMLWizardPage("Generate MOOSE YAML/Action Syntax Files");
		
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
		launchData = new ArrayList<String>();
		launchData.add(page.getHostName());
		launchData.add(page.getExecPath());
		launchData.add(page.getUsername());
		launchData.add(page.getPassword());
		
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public String getHostName() {
		return launchData.get(0);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getExecPath() {
		return launchData.get(1);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return launchData.get(2);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return launchData.get(3);
	}
}
