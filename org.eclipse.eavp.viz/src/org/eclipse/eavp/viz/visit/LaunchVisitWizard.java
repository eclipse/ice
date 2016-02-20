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
package org.eclipse.eavp.viz.visit;

import org.eclipse.jface.wizard.Wizard;

/**
 * This class extends Wizard to manage the WizardDialog for establishing a VisIt
 * client connection.
 * 
 * @author Taylor Patterson
 * 
 */
public class LaunchVisitWizard extends Wizard {

	/**
	 * The WizardPage contained in this Wizard
	 */
	LaunchVisitWizardPage page;

	/**
	 * The constructor
	 */
	public LaunchVisitWizard() {

		// Call Wizard's constructor
		super();

		// Add the content by adding an instance of LaunchVisitWizardPage
		page = new LaunchVisitWizardPage("VisIt Launch Page");
		addPage(page);

		return;
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		// Call the LaunchWizardPage method that sets that classes fields
		return page.setFinishFields();
	}

	/**
	 * This operation retrieves the WizardPage contained in this Wizard.
	 * 
	 * @return The WizardPage of this Wizard
	 */
	public LaunchVisitWizardPage getPage() {
		return page;
	}

}
