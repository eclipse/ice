/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.widgets.properties.ButtonCellEditor;
import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.client.widgets.properties.TextCellEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides {@link EditingSupport} for an
 * {@link ICellContentProvider} that requires a checkbox <code>Button</code> to
 * be used for editing the cell elements.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CheckboxCellEditingSupport extends TextCellEditingSupport {

	/**
	 * A <code>CellEditor</code> built around a <code>Button</code> with the
	 * <code>SWT.CHECK</code> style bit set.
	 */
	private final ButtonCellEditor checkboxCell;

	/**
	 * The default constructor.
	 * 
	 * @param viewer
	 *            The viewer that is using this <code>EditingSupport</code>.
	 *            <code>Control</code>s required by this class will be
	 *            constructed under this viewer's <code>Control</code> (usually
	 *            a <code>Table</code>).
	 * @param contentProvider
	 *            The content provider. The methods required as an
	 *            <code>EditingSupport</code> are passed to this content
	 *            provider.
	 */
	public CheckboxCellEditingSupport(ColumnViewer viewer,
			ICellContentProvider contentProvider) {
		super(viewer, contentProvider);

		// Get the viewer's Composite so we can create the CellEditors.
		Composite parent = (Composite) viewer.getControl();

		// Create the ButtonCellEditor.
		checkboxCell = new ButtonCellEditor(parent, SWT.CHECK);

		return;
	}

	/**
	 * Returns a {@link ButtonCellEditor} with its style bit set to
	 * <code>SWT.CHECK</code>.
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {

		// If all else fails, we should return null.
		CellEditor editor = null;

		// By default, return a Text-based CellEditor.
		if (contentProvider.isValid(element)) {
			editor = checkboxCell;
		}

		return editor;
	}

}
