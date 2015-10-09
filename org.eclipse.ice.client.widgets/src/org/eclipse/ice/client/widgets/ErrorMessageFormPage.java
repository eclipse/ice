/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This is a simple form page that display a message saying that the Form did
 * not have any data and directs the user to contact and admin.
 * 
 * @author Jay Jay Billings
 *
 */
public class ErrorMessageFormPage extends FormPage {

	/**
	 * The constructor
	 * @param editor the FormEditor that created the page
	 * @param id the id of the page
	 * @param title the title of the page
	 */
	public ErrorMessageFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	public void createFormContent(IManagedForm managedForm) {
		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout();

		// Setup the layout and layout data
		layout.numColumns = 1;
		form.getBody().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		form.getBody().setLayout(new FillLayout());

		String errorMsg = "There is no data to show in your Form or in this "
				+ "particular component. Please file a bug or email "
				+ "eclipse.ice.project@gmail.com.";
		managedForm.getToolkit().createText(form.getBody(),errorMsg);
		
		return;
	}
	
}
