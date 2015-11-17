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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewICEItemWizard extends Wizard implements INewWizard {

	private static final String DESCRIPTION = NewICEItemWizardMessages.NewICEItemWizard_Description;
	private static final String WIZARD_NAME = NewICEItemWizardMessages.NewICEItemWizard_Wizard_Name;
	
	private WizardNewProjectCreationPage _pageOne;
	
	
	public NewICEItemWizard() {
		setWindowTitle(NewICEItemWizardMessages.NewICEItemWizard_Window_Title);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPages() {
		super.addPages();
		
		_pageOne = new WizardNewProjectCreationPage(DESCRIPTION);
		_pageOne.setTitle(WIZARD_NAME);
		_pageOne.setDescription(DESCRIPTION);
		
		addPage(_pageOne);
	}
	
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
