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
package org.eclipse.ice.client.common;

import java.io.File;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.wizard.Wizard;

/**
 * This class is a Wizard that makes it easy for users to import files into
 * ICE. It walks them through the process of selecting a file and an Item
 * represented by that file.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ImportWizard extends Wizard {

	/**
	 * The wizard page used to import the Item and its input file.
	 */
	ItemImportWizardPage page;

	/**
	 * The constructor
	 */
	public ImportWizard() {
		super();
		page = new ItemImportWizardPage("Select an input "
				+ "file and Item type");
	}

	/**
	 * This operation adds the main page to the wizard.
	 */
	@Override
	public void addPages() {
		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		// Get the ICE Client
		IClient client = ClientHolder.getClient();

		// Get the file to be imported
		String filename = page.getSelectedFile();
		String filterPath = page.getFilterPath();
		File file = new File(filterPath,filename);

		// Get the Item type to create
		String itemType = page.getSelectedItem();

		// Import the new Item
		int id = client.importFileAsItem(file.toURI(), itemType);
		client.loadItem(id);
		
		return (id > 0);
	}

}
