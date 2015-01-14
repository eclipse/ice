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

import org.eclipse.ice.datastructures.ICEObject.IElementSource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swt.DefaultEventTableViewer;

/**
 * This class is a JFace Dialog for rendering IElementSources that are used by
 * ListComponents.
 * 
 * Only single selections are supported.
 * 
 * @author Jay Jay Billings
 *
 */
public class ElementSourceDialog<T> extends Dialog {

	/**
	 * The source that should be drawn
	 */
	private IElementSource<T> source;

	/**
	 * The SWT table that shows the list
	 */
	private Table listTable;

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
	 * @param source
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
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite comp = (Composite) super.createDialogArea(parent);

		// Create the table to hold the ListComponent.
		listTable = new Table(parent, SWT.FLAT);
		elements = source.getElements();
		DefaultEventTableViewer listTableViewer = new DefaultEventTableViewer(
				elements, listTable, source.getTableFormat());
		listTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		return comp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		// Set the selection if the OK button was pressed
		int index = listTable.getSelectionIndex();
		selection = elements.get(index);
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
