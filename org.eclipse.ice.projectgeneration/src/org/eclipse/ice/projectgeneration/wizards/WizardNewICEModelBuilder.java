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
 * This class defines the steps for creating a new New ICE Item project
 * via the wizard that us accessible via:
 *   'File -> New... -> Other -> New ICE Item Project'
 * 
 * @author arbennett
 */
public class WizardNewICEModelBuilder extends Wizard implements INewWizard {

	private static final String DESCRIPTION = "Create a new model builder.";
	private static final String WIZARD_NAME = "New ICE Model Builder";
	private static final String WIZARD_TITLE = "Create a new model builder";
	
	private IStructuredSelection selection;
	private IWorkbench workbench;
	
	private WizardNewICEModelBuilderPage _pageOne;

	
	/**
	 *	Constructor
	 */
	public WizardNewICEModelBuilder() {
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
		
		_pageOne = new WizardNewICEModelBuilderPage(DESCRIPTION, selection);
		_pageOne.setTitle(WIZARD_NAME);
		_pageOne.setDescription(DESCRIPTION);
		
		// Put the pages into the wizard
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
		boolean retval = true;
		
		// Do something
		
		return retval;
	}
}
