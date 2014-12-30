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
package org.eclipse.ice.client.widgets;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This is a FormPage that can render ListComponents into pages usable by the
 * ICEFormEditor.
 * 
 * @author Jay Jay Billings
 *
 */
public class ListComponentSectionPage extends ICEFormPage {

	/**
	 * The IManagedForm for the SectionPage.
	 */
	private IManagedForm managedFormRef;

	/**
	 * The Constructor
	 * 
	 * @param editor
	 *            The FormEditor for which the Page should be constructed.
	 * @param id
	 *            The id of the page.
	 * @param title
	 *            The title of the page.
	 */
	public ListComponentSectionPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		// begin-user-code

		// Get the parent form.
		final ScrolledForm scrolledForm = managedForm.getForm();

		// Set a GridLayout with a single column. Remove the default margins.
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scrolledForm.getBody().setLayout(layout);

		// Set the class reference to the managed form
		managedFormRef = managedForm;

		return;
		// end-user-code
	}
	
	
}
