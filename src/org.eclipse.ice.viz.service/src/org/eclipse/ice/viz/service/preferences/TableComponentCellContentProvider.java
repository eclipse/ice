/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.preferences;

import java.util.List;

import org.eclipse.ice.client.common.properties.ICellContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.swt.graphics.Image;

// TODO Documentation
public class TableComponentCellContentProvider implements
		ICellContentProvider {

	/**
	 * The text to display when a cell's element (expected to be an
	 * {@link Entry}) is invalid.
	 */
	protected static final String INVALID_ELEMENT_TEXT = "Invalid Row.";

	private final ICellContentProvider realProvider;
	private final int index;

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

	public TableComponentCellContentProvider(ICellContentProvider contentProvider, int index) {
		realProvider = contentProvider;
		this.index = index;
	}

	@Override
	public boolean isValid(Object element) {
		return realProvider.isValid(getIndexElement(element));
	}

	@Override
	public boolean isEnabled(Object element) {
		return realProvider.isEnabled(getIndexElement(element));
	}

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

	@Override
	public Image getImage(Object element) {
		return realProvider.getImage(getIndexElement(element));
	}

	@Override
	public Object getValue(Object element) {
		return realProvider.getValue(getIndexElement(element));
	}

	@Override
	public boolean setValue(Object element, Object value) {
		return realProvider.setValue(getIndexElement(element), value);
	}

}
