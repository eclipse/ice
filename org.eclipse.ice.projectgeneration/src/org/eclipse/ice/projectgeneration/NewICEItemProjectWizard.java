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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * This class defines the steps for creating a new New ICE Item project
 * via the wizard that us accessible via:
 *   'File -> New... -> Other -> New ICE Item Project'
 * 
 * @author arbennett
 */
public class NewICEItemProjectWizard extends Wizard implements INewWizard {

	// Messages are externalized in the messages.properties file
	private static final String DESCRIPTION = NewICEItemProjectWizardMessages.NewICEItemWizard_Description;
	private static final String WIZARD_NAME = NewICEItemProjectWizardMessages.NewICEItemWizard_Wizard_Name;
	
	private WizardNewProjectCreationPage _pageOne;
	
	/**
	 *	Constructor
	 */
	public NewICEItemProjectWizard() {
		setWindowTitle(NewICEItemProjectWizardMessages.NewICEItemWizard_Window_Title);
	}

	/**
	 * Initialize the wizard
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
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
		
		addPage(_pageOne);
	}
	
	/**
	 * Take all of the given information and set up a new 
	 * New ICE Item Project.
	 * 
	 * @return whether the project creation was successful
	 */
	@Override
	public boolean performFinish() {
		String name = _pageOne.getProjectName();
		URI location = null;
		if (!_pageOne.useDefaults()) {
			location = _pageOne.getLocationURI();
		}
		
		NewICEItemProjectSupport.createProject(name, location);
		return true;
	}

}
