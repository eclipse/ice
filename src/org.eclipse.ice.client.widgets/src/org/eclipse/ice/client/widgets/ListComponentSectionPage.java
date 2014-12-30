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

import java.awt.Toolkit;

import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ca.odell.glazedlists.swt.DefaultEventTableViewer;

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
	 * The ListComponent that is the input for this page.
	 */
	private ListComponent list;

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
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui
	 * .forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		// begin-user-code

		// Get the parent form and the toolkit
		final ScrolledForm scrolledForm = managedForm.getForm();
		final FormToolkit formToolkit = managedFormRef.getToolkit();

		// Set a GridLayout with a single column. Remove the default margins.
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scrolledForm.getBody().setLayout(layout);

		// Set the class reference to the managed form
		managedFormRef = managedForm;

		// Only create something if there is valid input.
		if (list != null) {
			Composite parent = managedForm.getForm().getBody();
			Section listSection = formToolkit.createSection(parent,
					Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
							| Section.EXPANDED | Section.COMPACT);
			Composite sectionClient = new Composite(parent, SWT.FLAT);
			Table table = formToolkit.createTable(sectionClient, SWT.FLAT);
			DefaultEventTableViewer tableViewer = new DefaultEventTableViewer(list, table, list);
			
			listSection.setClient(sectionClient);
		}

		return;
		// end-user-code
	}

	/**
	 * This operation sets the ListComponent that should be used as input for
	 * the section page.
	 * 
	 * @param list
	 *            The ListComponent
	 */
	public void setList(ListComponent list) {
		this.list = list;
	}

}
