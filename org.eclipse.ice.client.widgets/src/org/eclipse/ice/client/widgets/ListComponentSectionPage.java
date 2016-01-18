/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This is a FormPage that can render ListComponents into pages usable by the
 * ICEFormEditor.
 * 
 * @author Jay Jay Billings, Kasper Gammeltoft
 * 
 */
public class ListComponentSectionPage extends ICEFormPage {

	/**
	 * The ListComponent that is the input for this page.
	 */
	private ListComponent list;

	/**
	 * The IElementSource from the ListComponent that is used to feed it new
	 * entries.
	 */
	private IElementSource source;

	/**
	 * The shell used for the dialog
	 */
	private Shell shell;

	/**
	 * The composite that will act as the client of the section where everything
	 * is drawn.
	 */
	private Composite sectionClient;

	/**
	 * The NatTable that is displayed
	 */
	private ListComponentNattable table;

	/**
	 * The Constructor.
	 * 
	 * @param editor
	 *            The FormEditor for which the Page should be constructed.
	 * @param id
	 *            The id of the page.
	 * @param title
	 *            The title of the page.
	 */
	public ListComponentSectionPage(FormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui
	 * .forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

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

			shell = parent.getShell();
			// Create the section and set its layout info
			Section listSection = formToolkit.createSection(parent,
					ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
							| ExpandableComposite.TWISTIE
							| ExpandableComposite.EXPANDED
							| ExpandableComposite.COMPACT);
			listSection.setLayout(new GridLayout(1, false));
			listSection.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			// Create the section client, which is the client area of the
			// section that will actually render data.
			sectionClient = new Composite(listSection, SWT.FLAT);
			sectionClient.setLayout(new GridLayout(2, false));
			sectionClient.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			// Fixes section header bug where label color is spammed
			sectionClient.setBackground(
					Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			// Fixes background color bug for NatTable
			sectionClient.setBackgroundMode(SWT.INHERIT_FORCE);

			// Draws the table and sets that instance variable
			table = new ListComponentNattable(sectionClient, list, true);

			// Create the buttons for add, delete, up, and down
			createButtons();

			// Set the section client.
			listSection.setClient(sectionClient);
		}

		return;
	}

	/**
	 * This operation creates the add and delete buttons that are used to add
	 * layers to the table. Also creates buttons for moving layers around.
	 */
	private void createButtons() {

		// Create a composite for holding Add/Delete buttons to manipulate
		// the table and lay it out.
		Composite listButtonComposite = new Composite(sectionClient, SWT.NONE);
		listButtonComposite.setLayout(new GridLayout(1, false));
		listButtonComposite.setLayoutData(
				new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));

		// Create the add button to add a new element to the list.
		Button addMaterialButton = new Button(listButtonComposite, SWT.PUSH);
		addMaterialButton.setText("Add");
		addMaterialButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// We need to add the element based on whether or not the
				// IElementSource is available to provide selections.
				if (source == null) {
					// If it is not available, just duplicate the last one
					// on the list. I'm not entirely sure if this will work
					// because it will just be adding the same element twice
					// and may result in both being updated. We don't have a
					// good test case for it at the moment, so we will have
					// to cross that bridge when we get to it.
					int index = list.size() - 1;
					// Lock the list before adding the selction
					list.getReadWriteLock().writeLock().lock();
					try {
						list.add(list.get(index));
					} finally {
						// Unlock it
						list.getReadWriteLock().writeLock().unlock();
					}
				} else {
					// Otherwise, if the IElementSource is available, throw
					// up the source selection dialog
					ElementSourceDialog dialog = new ElementSourceDialog(shell,
							source);
					if (dialog.open() == Window.OK) {
						// Lock the list to avoid concurrent modifications
						list.getReadWriteLock().writeLock().lock();
						try {
							// Get the selection and add it if they actually
							// selected something.
							list.add(dialog.getSelection());
						} finally {
							// Unlock the list
							list.getReadWriteLock().writeLock().unlock();
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

		});

		// Create the delete button to delete the currently selected element
		// from the list.
		Button deleteMaterialButton = new Button(listButtonComposite, SWT.PUSH);
		deleteMaterialButton.setText("Delete");
		deleteMaterialButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				// checks if list has something to delete
				if (list.size() > 0) {
					ListComponent selected = table.getSelectedObjects();
					if (selected.size() > 0) {

						// removes that material from the list
						// lock the list before removing the selection
						list.getReadWriteLock().writeLock().lock();
						try {
							for (Object o : selected) {
								list.remove(o);
							}
						} finally {
							// Unlock it
							list.getReadWriteLock().writeLock().unlock();
						}
					}
				}
			}

		});

		// Move up button, moves the selected rows up one index.
		Button moveUpButton = new Button(listButtonComposite, SWT.PUSH);
		moveUpButton.setText("^");
		moveUpButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// Makes sure there is actually data in the list to manipulate
				if (list.size() > 0) {
					// Gets selected rows
					ListComponent selected = table.getSelectedObjects();
					// Makes sure there are selected rows
					if (selected.size() > 0) {
						int numSelected = selected.size();
						// Makes sure that the user does not move the cell at
						// position 0 to position -1 (past top of table)
						if (!(selected.get(0).equals(list.get(0)))) {

							list.getReadWriteLock().writeLock().lock();

							// Gets the object in the list that will be
							// overridden
							int index = 0;
							Object toMove = list.get(0);

							// Overrides the list entries to move the selected
							// rows up by one row
							for (int i = 0; i < numSelected; i++) {
								index = list.indexOf(selected.get(i)) - 1;
								toMove = list.get(index);
								list.set(index, selected.get(i));
								list.set(index + 1, toMove);

							}

							// Resets the overridden row to be at the end of the
							// selected rows
							list.set(index + 1, toMove);

							// Unlocks the list
							list.getReadWriteLock().writeLock().unlock();
							table.setSelection(selected);

						}

					}
				}
			}

		});

		// Move down button, moves the currently selected rows down one index.
		Button moveDownButton = new Button(listButtonComposite, SWT.PUSH);
		moveDownButton.setText("v");
		moveDownButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// makes sure there is actually data in the list to manipulate
				if (list.size() > 0) {
					// Gets selected rows
					ListComponent selected = table.getSelectedObjects();
					// Makes sure there are selected rows
					if (selected.size() > 0) {
						int numSelected = selected.size();
						// Makes sure that the user does not move the selected
						// cell past the end of the table.
						if (!(selected.get(numSelected - 1)
								.equals(list.get(list.size() - 1)))) {

							list.getReadWriteLock().writeLock().lock();

							// Gets the object in the list that will be
							// overridden
							int index = 0;
							Object toMove = list.get(0);

							// Overrides the list entries to move the selected
							// rows up by one row
							for (int i = numSelected - 1; i >= 0; i--) {
								index = list.indexOf(selected.get(i)) + 1;
								toMove = list.get(index);
								list.set(index, selected.get(i));
								list.set(index - 1, toMove);

							}

							// Resets the overridden row to be at the end of the
							// selected rows
							list.set(index - 1, toMove);

							// Unlocks the list
							list.getReadWriteLock().writeLock().unlock();
							table.setSelection(selected);
						}
					}
				}
			}

		});

		return;
	}

	/**
	 * This operation sets the ListComponent that should be used as input for
	 * the section page.
	 * 
	 * @param list
	 *            The ListComponent to set as the new list for this section page
	 *            and for the data to be displayed in its table.
	 */
	public void setList(ListComponent list) {
		this.list = list;
		this.source = list.getElementSource();
	}

}
