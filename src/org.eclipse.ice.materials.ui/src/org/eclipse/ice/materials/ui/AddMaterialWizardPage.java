/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.materials.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jay Jay Billings
 *
 */
public class AddMaterialWizardPage extends WizardPage {

	/**
	 * The constructor
	 * @param pageName the page name
	 * 	@wbp.parser.constructor
	 */
	public AddMaterialWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * An alternative constructor for creating the wizard page
	 * @param pageName the name of the page
	 * @param title the title to show in the title bar
	 * @param titleImage an image to go along with the title
	 */
	public AddMaterialWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		// Create the base container for the wizard
		Composite container = new Composite(parent, SWT.NULL);
		
		// Add the composite to hold the name and density blocks
		Composite nameAndDensityComposite = new Composite(container, SWT.None);
		
		// Set the control to the base container
		setControl(container);

	}

}
