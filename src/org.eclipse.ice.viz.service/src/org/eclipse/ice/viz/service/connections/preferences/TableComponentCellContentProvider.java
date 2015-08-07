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
package org.eclipse.ice.viz.service.connections.preferences;

import java.util.List;

import org.eclipse.ice.viz.service.datastructures.IVizCellContentProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides the content for cells in a JFace viewer when the
 * underlying data is a {@link TableComponent}.
 * <p>
 * This class is designed so that a single backing content provider can be
 * re-used for each column in a table, as all elements in a
 * {@code TableComponent} are the same data structure (see {@link Entry}).
 * </p>
 * <p>
 * The element object passed to instances of this content provider is expected
 * to be a {@link List} of {@code Entry}s.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class TableComponentCellContentProvider implements IVizCellContentProvider {

	/**
	 * The text to display when the element passed to this provider is invalid.
	 */
	protected static final String INVALID_ELEMENT_TEXT = "Invalid Row.";

	/**
	 * The underlying content provider that is used for each cell in the table.
	 */
	private final IVizCellContentProvider realProvider;

	/**
	 * The column index for this provider. This provider expects a row (which
	 * should be a {@code List}) as input, and this index specifies which column
	 * in that row provides the real content for the cell.
	 */
	private final int index;

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The underlying content provider used for each cell in the
	 *            JFace viewer.
	 * @param index
	 *            The column index for this provider. This provider expects a
	 *            row (which should be a {@code List}) as input, and this index
	 *            specifies which column in that row provides the real content
	 *            for the cell.
	 */
	public TableComponentCellContentProvider(IVizCellContentProvider contentProvider, int index) {
		realProvider = contentProvider;
		this.index = index;
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public Image getImage(Object element) {
		return realProvider.getImage(getIndexElement(element));
	}

	/**
	 * Converts the element passed to this content provider to a {@code List<?>}
	 * and returns the <i>i</i>-th element depending on the value of the index.
	 * 
	 * @param element
	 *            The input element. This should be a {@code List<?>}.
	 * @return The corresponding cell element for the column in the row/list, or
	 *         {@code null} if the input was somehow invalid (either not a list
	 *         or the index is invalid).
	 */
	private Object getIndexElement(Object element) {
		Object indexElement = null;
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				indexElement = list.get(index);
			}
		}
		return indexElement;
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public String getText(Object element) {
		String text = INVALID_ELEMENT_TEXT;
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				text = realProvider.getText(list.get(index));
			}
		}
		return text;
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public String getToolTipText(Object element) {
		String text = INVALID_ELEMENT_TEXT;
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				text = realProvider.getToolTipText(list.get(index));
			}
		}
		return text;
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public Object getValue(Object element) {
		return realProvider.getValue(getIndexElement(element));
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return realProvider.isEnabled(getIndexElement(element));
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public boolean isValid(Object element) {
		return realProvider.isValid(getIndexElement(element));
	}

	/*
	 * Implements a method from ICellContentProvider.
	 */
	@Override
	public boolean setValue(Object element, Object value) {
		return realProvider.setValue(getIndexElement(element), value);
	}
}
