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

import java.util.Collections;
import java.util.Comparator;

import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * This class is a JFace Dialog for rendering IElementSources that are used by
 * ListComponents.
 * 
 * 
 * @author Jay Jay Billings, Kasper Gammeltoft
 * 
 */
public class ElementSourceDialog<T> extends Dialog {

	/**
	 * The source that should be drawn
	 */
	private IElementSource<T> source;

	/**
	 * The list of the data for the table to display
	 */
	private ListComponent<T> list;

	/**
	 * The NatTable that shows the list
	 */
	private ListComponentNattable listTable;

	/**
	 * The selection made by the user or null if the dialog was closed.
	 */
	private T selection;

	/**
	 * The list of elements rendered in the table
	 */
	private EventList<T> elements;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 *            The shell in which the dialog should be drawn
	 * @param elementSource
	 *            The IElementSource that should be drawn
	 */
	public ElementSourceDialog(Shell parentShell,
			IElementSource<T> elementSource) {
		super(parentShell);
		source = elementSource;
		// Create the list component from source
		list = new ListComponent<T>();
		list.setTableFormat((WritableTableFormat<T>) source.getTableFormat());
		elements = source.getElements();
		list.addAll(elements);

		// Sorts the list according to the item's comparator, if it is
		// available
		if (!list.isEmpty() && list.get(0) instanceof Comparable) {
			Collections.sort(list, new Comparator<T>() {
				@Override
				public int compare(Object first, Object second) {
					return ((Comparable) first).compareTo(second);
				}
			});
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// Create the composite from the parent
		Composite comp = (Composite) super.createDialogArea(parent);
		comp.setLayout(new GridLayout(1, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Set the background to white (visible on the borders)
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		// Add filter to the Dialog to filter the table results
		final Text filter = new Text(comp, SWT.BORDER | SWT.SEARCH);
		filter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		// Get a copy of the list to give to the NatTable so that we can keep a
		// fresh copy to compare to.
		ListComponent<T> copy = new ListComponent<T>();
		copy.setTableFormat(list.getTableFormat());
		for (int i = 0; i < list.size(); i++) {
			copy.add(list.get(i));
		}

		// Create the Nattable from the Composite parent and the ListComponent
		// list
		// We do NOT want this table to be editable!
		listTable = new ListComponentNattable(comp, copy, false);

		// Set the size of the shell, have the list fill the entire available
		// area.
		int width = listTable.getPreferredWidth();
		int height = listTable.getPreferredHeight();
		comp.getShell().setSize(width * 3 / 4, height);

		// Forces the table to grab the extra area in the gridlayout.
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(listTable.getTable());

		// Selects the first component by default
		ListComponent<T> select = new ListComponent<T>();
		select.add(list.get(0));
		listTable.setSelection(select);

		// Add a modify listener to filter the table as the user types in the
		// filter.
		filter.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				ListComponent listFromTable = listTable.getList();
				// Get the filter text
				String filterText = filter.getText().toLowerCase();

				// Checks to see if this is a search for a specific
				// isotope or a element (in which case all isotopes should be
				// shown through the filter).
				boolean useElementName = !((filterText.length() > 0) && (Character
						.isDigit(filterText.charAt(0))));

				// Iterate over the list and pick the items to keep from the
				// filter text.
				int numRemoved = 0;
				for (int i = 0; i < list.size(); i++) {

					// Lock the list to protect thread issues.
					listFromTable.getReadWriteLock().writeLock().lock();
					// If the list contains materials, get the material
					if (list.get(i) instanceof Material) {
						Material mat = (Material) list.get(i);
						// Finally, if the material fits the filter, make sure
						// it is in the list. Otherwise,
						// take it out of the list.

						// Get whether to compare entire name or just elemental
						// name.
						String matName = "";
						if (useElementName) {
							matName = mat.getElementalName();
						} else {
							matName = mat.getName();
						}

						// If the material meets the criteria
						if (matName.toLowerCase().startsWith(filterText)) {

							// Make sure material is in list
							if (!listFromTable.contains(mat)) {
								listFromTable.add(i - numRemoved, mat);
							}

							// If the material does not meet the criteria
						} else {

							// Remove materials that do not fit the search
							// criteria.
							if (listFromTable.contains(mat)) {
								listFromTable.remove(mat);
							}
							numRemoved++;
						}

					}

					// Unlock the list
					listFromTable.getReadWriteLock().writeLock().unlock();
				}
			}
		});

		return comp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Select Material");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {

		// Sets the selection if OK is pressed, will be the first selected
		// object if there are multiple selections.
		selection = ((T) listTable.getSelectedObjects().get(0));
		super.okPressed();
	}

	/**
	 * This operation returns the selection made in the dialog.
	 * 
	 * @return The selection. If multiple items were selected, only the first is
	 *         returned.
	 */
	public T getSelection() {
		return selection;
	}

}