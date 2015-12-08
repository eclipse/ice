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
package org.eclipse.ice.projectgeneration.wizards;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.ice.projectgeneration.NewICEItemProjectSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * This class defines the steps for creating a new job launcher
 * via the wizard that us accessible via:
 *   'File -> New... -> Other -> New Job Launcher'
 * 
 * @author arbennett
 */
public class WizardNewICEJobLauncher extends Wizard implements INewWizard {

	// Messages are externalized in the messages.properties file
	private static final String DESCRIPTION = "Create a new job launcher.";
	private static final String WIZARD_NAME = "New ICE Job Launcher";
	private static final String WIZARD_TITLE = "Create a new job launcher";
	
	private IStructuredSelection selection;
	private IWorkbench workbench;
	
	private NewJobLauncherPage _pageOne;
	
	/**
	 *	Constructor
	 */
	public WizardNewICEJobLauncher() {
		setWindowTitle(WIZARD_TITLE);
	}

	
	/**
	 * Initialize the wizard
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	
	/**
	 * Defines the wizard pages
	 */
	@Override
	public void addPages() {
		super.addPages();
		
		_pageOne = new NewJobLauncherPage(DESCRIPTION, selection);
		_pageOne.setTitle(WIZARD_NAME);
		_pageOne.setDescription(DESCRIPTION);
		
		// Put the pages into the wizard
		addPage(_pageOne);
	}
	
	
	/**
	 * Take all of the given information and set up a new 
	 * model builder class.
	 * 
	 * @return whether the code creation was successful
	 */
	@Override
	public boolean performFinish() {
		boolean retval = true;
		
		// Create the file
		IFile file = _pageOne.createNewFile();
		if (file != null) {
			retval = retval && true;
		} else {
			retval = false;
		}
		
		// Update the plugin definition
		// TODO
		
		return retval;
	}
}
