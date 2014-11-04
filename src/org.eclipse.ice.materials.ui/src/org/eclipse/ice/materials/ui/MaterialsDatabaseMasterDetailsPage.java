/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.materials.ui;

import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This page is responsible for rendering the contents of the Materials Database
 * in a Master-Details Block where the list of materials is separated from the
 * set of properties.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MaterialsDatabaseMasterDetailsPage extends FormPage {

	/**
	 * The IMaterialsDatabase that will be rendered by this page.
	 */
	private IMaterialsDatabase materialsDatabase;

	/**
	 * The constructor
	 * 
	 * @param editor
	 *            The FormEditor that holds this page
	 * @param id
	 *            The id of the page
	 * @param title
	 *            The title of the page
	 * @param database
	 *            The IMaterialsDatabase that should be rendered
	 */
	public MaterialsDatabaseMasterDetailsPage(FormEditor editor, String id,
			String title, IMaterialsDatabase database) {
		super(editor, id, title);
		// Store the database
		materialsDatabase = database;
	}

	/**
	 * This operation creates the custom content for rendering the materials
	 * database.
	 * 
	 * @param managedForm
	 *            the ManagedForm that is responsible for rendering this page
	 */
	protected void createFormContent(IManagedForm managedForm) {

		FormToolkit toolkit = managedForm.getToolkit();
		Composite parent = managedForm.getForm().getBody();
		// The layout of the form body MUST be set or nothing will render!
		parent.setLayout(new GridLayout(1, true));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
	
		// Add the block
		MaterialsDatabaseMasterDetailsBlock block = new MaterialsDatabaseMasterDetailsBlock(
				materialsDatabase);
		block.createContent(managedForm, parent);
	}
}
