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
package org.eclipse.ice.projectgeneration;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * This class defines the steps for creating a new New ICE Item project
 * via the wizard that us accessible via:
 *   'File -> New... -> Other -> New ICE Item Project'
 * 
 * @author arbennett
 */
public class WizardNewICEItemProject extends Wizard implements INewWizard {

	// Messages are externalized in the messages.properties file
	private static final String DESCRIPTION = "Create a new ICE item project.";
	private static final String WIZARD_NAME = "New ICE Item Project";
	private static final String WIZARD_TITLE = "Create a new ICE item project";
	
	private IStructuredSelection selection;
	private IWorkbench workbench;
	
	private WizardNewProjectCreationPage _pageOne;
	private WizardNewICEModelBuilderPage _pageTwo;

	
	/**
	 *	Constructor
	 */
	public WizardNewICEItemProject() {
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
		
		_pageOne = new WizardNewProjectCreationPage(DESCRIPTION);
		_pageOne.setTitle(WIZARD_NAME);
		_pageOne.setDescription(DESCRIPTION);
		
		// Add ICE Item setup page (model builder, job launcher, etc)
		_pageTwo = new WizardNewICEModelBuilderPage(selection);
		
		// Put the pages into the wizard
		addPage(_pageOne);
		addPage(_pageTwo);
	}
	
	
	/**
	 * Take all of the given information and set up a new 
	 * New ICE Item Project.
	 * 
	 * @return whether the project creation was successful
	 */
	@Override
	public boolean performFinish() {
		boolean retval = true;
		
		// Set up the project
		String name = _pageOne.getProjectName();
		URI location = null;
		if (!_pageOne.useDefaults()) {
			location = _pageOne.getLocationURI();
		}
		
		// Generate the model builder class files
		IFile file = _pageTwo.createNewFile();
		if (file != null) {
			retval = retval && true;
		} else {
			retval = false;
		}
		
		NewICEItemProjectSupport.createProject(name, location);
		return retval;
	}
}
