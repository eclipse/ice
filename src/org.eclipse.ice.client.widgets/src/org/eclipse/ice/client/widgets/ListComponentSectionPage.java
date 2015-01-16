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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ca.odell.glazedlists.swt.DefaultEventTableViewer;

import org.eclipse.swt.layout.GridData;

/**
 * This is a FormPage that can render ListComponents into pages usable by the
 * ICEFormEditor.
 * 
 * @author Jay Jay Billings
 * 
 */
public class ListComponentSectionPage extends ICEFormPage {

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
		final FormToolkit formToolkit = managedForm.getToolkit();

		// Set a GridLayout with a single column. Remove the default margins.
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scrolledForm.getBody().setLayout(layout);

		// Only create something if there is valid input.
		if (list != null) {

			// Get the parent
			Composite parent = managedForm.getForm().getBody();
			// Create the section and set its layout info
			Section listSection = formToolkit.createSection(parent,
					Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
							| Section.EXPANDED | Section.COMPACT);
			listSection.setLayout(new GridLayout(1, false));
			listSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));
			// Create the section client, which is the client area of the
			// section that will actually render data.
			Composite sectionClient = new Composite(listSection, SWT.FLAT);
			sectionClient.setLayout(new GridLayout(2, false));
			sectionClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));

			// Create the table to hold the ListComponent.
			Table listTable = formToolkit.createTable(sectionClient, SWT.FLAT);
			DefaultEventTableViewer listTableViewer = new DefaultEventTableViewer(
					list, listTable, list);
			listTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));

			// Create a composite for holding Add/Delete buttons to manipulate
			// the table and lay it out.
			Composite listButtonComposite = new Composite(sectionClient,
					SWT.NONE);
			listButtonComposite.setLayout(new GridLayout(1, false));
			listButtonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL,
					false, true, 1, 1));

			// Create the add button to add a new element to the list.
			Button addMaterialButton = new Button(listButtonComposite, SWT.PUSH);
			addMaterialButton.setText("Add");

			// Create the delete button to delete the currently selected element
			// from the list.
			Button deleteMaterialButton = new Button(listButtonComposite,
					SWT.PUSH);
			deleteMaterialButton.setText("Delete");

			// Set the section client.
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
