/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

/**
 * This class provides the ability to edit cells in the
 * {@link TableComponentComposite}'s JFace table.
 * <p>
 * Each column in the table needs its own instance of this editing support with
 * the correct column index. Each row in the table is a {@link List}, whose
 * elements are instances of {@link Entry}.
 * </p>
 * <p>
 * The behavior below describes typical usage for this class:
 * </p>
 * <ol>
 * <li>A cell in the table is clicked.</li>
 * <li>The column's editing support is queried by passing the row's element (a
 * {@code List<Entry}).</li>
 * <li>The {@code Entry} in the list is determined by the index specified at
 * construction.</li>
 * <li>The column's editing support in turn queries the underlying
 * {@code EntryCellEditingSupport}.</li>
 * </ol>
 * <p>
 * Thus, this editing support effectively wraps the underlying {@code Entry}
 * editing support so that it can be re-used across the entire viewer.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class TableComponentCellEditingSupport extends EditingSupport {

	/**
	 * The underlying editing support for Entries that should really be used.
	 */
	private final EntryCellEditingSupport realEditingSupport;
	/**
	 * The column's index. This is used to pass along the correct element from
	 * the input list to the underlying editing support.
	 */
	private final int index;

	/**
	 * The default constructor.
	 * 
	 * @param viewer
	 *            The JFace viewer for which editing capabilities are desired.
	 * @param realEditingSupport
	 *            The real, underlying editing support used to edit the cells in
	 *            the JFace viewer.
	 * @param index
	 *            The column's index. This is used to pass along the correct
	 *            element from the input list to the underlying editing support.
	 */
	public TableComponentCellEditingSupport(ColumnViewer viewer, EntryCellEditingSupport realEditingSupport,
			int index) {
		super(viewer);

		this.realEditingSupport = realEditingSupport;
		this.index = index;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		boolean enabled = false;

		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				enabled = realEditingSupport.canEdit(list.get(index));
			}
		}

		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		// If all else fails, we should return null.
		CellEditor editor = null;

		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				editor = realEditingSupport.getCellEditor(list.get(index));
			}
		}

		return editor;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		Object value = null;

		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				value = realEditingSupport.getValue(list.get(index));
			}
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				realEditingSupport.setValue(list.get(index), value);
				// Force the viewer to refresh this row.
				getViewer().update(element, null);
			}
		}

		return;
	}

}
