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

import java.util.Collections;
import java.util.Comparator;

import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * This class is a JFace Dialog for rendering IElementSources that are used by
 * ListComponents.
 * 
 * Only single selections are supported.
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite comp = (Composite) super.createDialogArea(parent);
		comp.setLayout(new GridLayout(1, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//Set the background to white (visible on the borders)
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		//Get the source's elements to convert to a ListComponent
		elements = source.getElements();
		
		//Create the list component from source
		ListComponent list = new ListComponent();
		list.setTableFormat((WritableTableFormat) source.getTableFormat());
		list.addAll(elements);
		
		//Sorts the list according to the material names
		Collections.sort(list, new Comparator() {
			public int compare(Object first, Object second) {
				return ((Material)first).getName().compareTo(((Material)second).getName());
			}
		});
		
		//Create the Nattable from the Composite parent and the ListComponent list
		//We do NOT want this table to be editable!
		listTable = new ListComponentNattable(comp, list, false);

		//Set the size of the shell, have the list fill the entire available area. 
		int width = listTable.getPreferredWidth();
		int height = listTable.getPreferredHeight();
		comp.getShell().setSize(width*3/4, height);
		
		//forces the table to grab the extra area in the gridlayout. 
		GridDataFactory.fillDefaults().grab(true,  true).applyTo(listTable.getTable());

		//Selects the first component by default
		ListComponent select = new ListComponent();
		select.add(list.get(0));
		listTable.setSelection(select);
		
		return comp;

	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell shell){
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
		// Set the selection if the OK button was pressed
		/**
		int index = listTable.getSelectionIndex();
		selection = elements.get(index);
		*/
		//Sets the selection, will be the first selected object if there are multiple selections. 
		selection = (T) listTable.getSelectedObjects().get(0);
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