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
package org.eclipse.ice.client.common.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides basic {@link EditingSupport} for an
 * {@link ICellContentProvider}. The default behavior provides a
 * {@link TextCellEditor} for editing the value of the model managed by the
 * <code>ICellContentProvider</code>.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class TextCellEditingSupport extends EditingSupport {

	/**
	 * The content provider. The methods required as an
	 * <code>EditingSupport</code> are passed to this content provider.
	 */
	protected final ICellContentProvider contentProvider;

	/**
	 * A <code>CellEditor</code> built around a <code>Text</code> widget.
	 */
	protected final TextCellEditor textCell;

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
	public TextCellEditingSupport(ColumnViewer viewer,
			ICellContentProvider contentProvider) {
		super(viewer);

		this.contentProvider = contentProvider;

		// Get the viewer's Composite so we can create the CellEditors.
		Composite parent = (Composite) viewer.getControl();

		// Create the TextCellEditor.
		textCell = new TextCellEditor(parent, SWT.LEFT);

		return;
	}

	/**
	 * By default, this returns a {@link TextCellEditor}.
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {

		// If all else fails, we should return null.
		CellEditor editor = null;

		// By default, return a Text-based CellEditor.
		if (contentProvider.isValid(element)) {
			editor = textCell;
		}

		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return contentProvider.isEnabled(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		return contentProvider.getValue(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (contentProvider.setValue(element, value)) {
			// Force the viewer to refresh for this specific element. This means
			// the change will be reflected in the viewer.
			getViewer().update(element, null);
		}
	}

}
