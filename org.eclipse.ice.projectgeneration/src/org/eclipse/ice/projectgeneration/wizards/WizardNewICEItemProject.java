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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.projectgeneration.NewICEItemProjectSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.pde.internal.ui.wizards.plugin.NewPluginProjectWizard;
import org.eclipse.ui.IWorkbench;

/**
 * This class defines the steps for creating a new New ICE Item project via the
 * wizard that us accessible via: 'File -> New... -> Other -> New ICE Item
 * Project'
 * 
 * @author arbennett
 */
public class WizardNewICEItemProject extends NewPluginProjectWizard {

	private static final String DESCRIPTION = "Create a new ICE item project.";
	private static final String WIZARD_NAME = "New ICE Item Project";
	private static final String WIZARD_TITLE = "Create a new ICE item project";

	private IStructuredSelection selection;
	private IWorkbench workbench;

	/**
	 * Constructor
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
	 * Take all of the given information and set up a new New ICE Item Project.
	 * 
	 * @return whether the project creation was successful
	 */
	@SuppressWarnings("restriction")
	@Override
	public boolean performFinish() {
		boolean retval = true;

		super.performFinish();
		try {
			NewICEItemProjectSupport.setNature(this.fMainPage.getProjectHandle());
		} catch (CoreException e) {
		}

		return retval;
	}
}
