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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Description
 * 
 * @author arbennett
 */
public class WizardNewICEJobLauncherPage extends WizardNewFileCreationPage {

	/**
	 * Constructor
	 * 
	 * @param selection
	 */
	public WizardNewICEJobLauncherPage(String pageName, IStructuredSelection selection) {
		super("NewICEJobLauncherWizardPage", selection);
		setTitle("ICE Model Builder Item");
		setDescription("Creates a new ICE job launcher item class");
		setFileExtension("java");
	}

	
	/**
	 * Defines the initial content of the file that results from completing
	 * the wizard.
	 */
	@Override
	protected InputStream getInitialContents() {
		return new ByteArrayInputStream("Hello world!".getBytes());
	}
}
