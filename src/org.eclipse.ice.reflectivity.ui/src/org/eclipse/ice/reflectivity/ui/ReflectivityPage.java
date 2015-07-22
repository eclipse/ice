/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft, Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.reflectivity.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ice.client.widgets.ElementSourceDialog;
import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.client.widgets.ListComponentNattable;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This is the custom reflectivity section page used to render the reflectivity
 * model. It sets up the <code>ListComponentNattable</code> in the top of a
 * <code>SashForm</code>, with an <code>ICEResourcePage</code> on the bottom. It
 * uses the tabbed properties view to display information about the model as a
 * whole and about the cells for the table.
 * 
 * @author Kasper Gammeltoft
 * @author Jay Billings
 *
 */
public class ReflectivityPage extends ICEResourcePage
		implements ITabbedPropertySheetPageContributor, ISelectionProvider {

	public static final String ID = "org.eclipse.ice.reflectivity.ui.ReflectivityPage";

	/**
	 * The shell of the page, used to create the element source dialog for
	 * adding materials to the list component
	 */
	private Shell shell;

	/**
	 * The list component, displays the layer data in the top of the sash form
	 */
	private ListComponent<Material> list;

	/**
	 * The section client for the list component
	 */
	private Composite sectionClient;

	/**
	 * THe List component nattable that displays the list data
	 */
	private ListComponentNattable listTable;

	/**
	 * The data component that should have its entries displayed in the
	 * properties tab
	 */
	private DataComponent data;

	/**
	 * The sash form that is the base composite for the editor in this page
	 */
	private SashForm sashForm;

	/**
	 * The constructor
	 * 
	 * @param editor
	 *            The ICEFormEditor to use. Should always be an instance of
	 *            ReflectivityFormEditor.
	 * @param id
	 *            The id of this section page.
	 * @param title
	 *            The title of this section page.
	 */
	public ReflectivityPage(FormEditor editor, String id, String title) {
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
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scrolledForm.getBody().setLayout(layout);

		// Get the parent
		Composite parent = managedForm.getForm().getBody();

		// Get the shell
		shell = parent.getShell();

		// Create the sashForm
		sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayout(new GridLayout());
		sashForm.setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Create the list's section and set its layout info
		Section listSection = formToolkit.createSection(sashForm,
				Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
						| Section.EXPANDED | Section.COMPACT);
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
		listTable = new ListComponentNattable(sectionClient, list, true, true,
				false);

		// Add the buttons to the table
		createButtons();

		// Set the section client.
		listSection.setClient(sectionClient);

		// Enable a new selection listener to listen to the table's selection
		// events
		RowSelectionProvider provider = listTable.getSelectionProvider();
		provider.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				IStructuredSelection structSelect = (IStructuredSelection) selection;
				Object obj = structSelect.getFirstElement();

			}

		});

		// Create the scrolled and managed forms to pass to
		// super.createFormContent() to create the resource page in the sash
		// form
		ScrolledForm form = formToolkit.createScrolledForm(sashForm);
		IManagedForm resourceForm = new ManagedForm(formToolkit, form);

		// Set this page as a selection provider (for the whole page)
		getSite().setSelectionProvider(this);

		// Create the resource form page contents
		super.createFormContent(resourceForm);

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
				if (list.getElementSource() == null) {
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
					ElementSourceDialog<Material> dialog = new ElementSourceDialog<Material>(
							shell, list.getElementSource());
					if (dialog.open() == Window.OK) {
						// Lock the list to avoid concurrent modifications
						list.getReadWriteLock().writeLock().lock();
						try {
							// Get the selection and add it if they actually
							// selected something.
							list.add((Material) dialog.getSelection());
						} finally {
							// Unlock the list
							list.getReadWriteLock().writeLock().unlock();
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
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
					ListComponent<Material> selected = listTable
							.getSelectedObjects();
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
					ListComponent<Material> selected = listTable
							.getSelectedObjects();
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
							Material toMove = list.get(0);

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
							listTable.setSelection(selected);

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
					ListComponent<Material> selected = listTable
							.getSelectedObjects();
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
							Material toMove = list.get(0);

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
							listTable.setSelection(selected);
						}
					}
				}
			}

		});

		// Clear button- removes all entries from the list.
		Button clearButton = new Button(listButtonComposite, SWT.PUSH);
		clearButton.setText("clear");
		clearButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// makes sure there is actually data in the list to manipulate
				if (list.size() > 0) {

					// Now just clear the list.
					list.getReadWriteLock().writeLock().lock();
					list.clear();
					list.getReadWriteLock().writeLock().unlock();
				}
			}

		});

		return;
	}

	/**
	 * Sets the data component to use as the inputs for this model.
	 * 
	 * @param dataComp
	 *            The data component to use.
	 */
	public void setDataComponent(DataComponent dataComp) {
		data = dataComp;
	}

	/**
	 * Sets the list component to use for this page. The list is displayed at
	 * the top of the page in the upper part of the sash form.
	 * 
	 * @param listComp
	 *            The list component to display.
	 */
	public void setList(ListComponent listComp) {
		list = listComp;
	}

	/**
	 * Needed for the properties tab attributes to be registered for this page.
	 * Makes sure that the IPropertySheetPage class returns this as a new
	 * TabbedPropertySheetPage so that the properties tab uses the Reflectivity
	 * properties rather than the standard eclipse properties view
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Also needed for the properties view to work. Must return the same thing
	 * as denoted for the tabbed property contributor ID in the plugin.xml
	 */
	@Override
	public String getContributorId() {
		return getSite().getId();
	}

	/**
	 * Adds a selection changed listener. Part of the ISelectionProvider
	 * interface. Currently does nothing.
	 */
	@Override
	public void addSelectionChangedListener(
			ISelectionChangedListener listener) {

	}

	/**
	 * Gets the current selection for this page. If nothing in the list
	 * component table is selected, should return the data component for editing
	 * its entries.
	 * 
	 * TODO: Need to have the list component table selections be availiable as
	 * selections, so as to place constrictions on them! Currently only returns
	 * the data component!
	 * 
	 * Part of the ISelectionProvider interface.
	 */
	@Override
	public ISelection getSelection() {
		// Create the new selection- currently only (and always) contains just
		// the data component to be displayed in the properties view
		return new IStructuredSelection() {

			@Override
			public boolean isEmpty() {
				// It is never empty- always has data component
				return false;
			}

			@Override
			public Object getFirstElement() {
				// Get the data
				return data;
			}

			@Override
			public Iterator iterator() {
				// Create an iterator over a list containing just the data
				List<DataComponent> list = new ArrayList<DataComponent>();
				list.add(data);
				return list.iterator();
			}

			@Override
			// Always 1 for now
			public int size() {
				return 1;
			}

			@Override
			public Object[] toArray() {
				// An array containing the data component
				return new DataComponent[] { data };
			}

			@Override
			public List toList() {
				// A list containing the data component
				List<DataComponent> list = new ArrayList<DataComponent>();
				list.add(data);
				return list;
			}

		};
	}

	/**
	 * Removes a selection changed listener. Part of the ISelectionProvider
	 * interface
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {

	}

	/**
	 * Sets the current selection. Not used as of now. Part of the
	 * ISelectionProvider interface.
	 */
	@Override
	public void setSelection(ISelection selection) {
		// Do nothing
	}

}
