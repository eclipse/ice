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

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

/**
 * This class provides an implementation of the IColumnPropertyAccessor
 * interface from Nattable for ICE's ListComponent class. It delegates all of
 * its calls to existing operations on ListComponent described by the
 * GlazedLists TableFormat or WritableTableFormat interfaces that it implements
 * except for getColumnIndex, which is implemented here.
 * 
 * @author Jay Jay Billings
 *
 */
public class ListComponentColumnPropertyAccessor<T>
		implements IColumnPropertyAccessor<T> {

	/**
	 * The list component wrapped by this accessor.
	 */
	private ListComponent<T> component;

	/**
	 * The list of column names of the component.
	 */
	ArrayList<String> columnNames;

	/**
	 * The constructor
	 */
	public ListComponentColumnPropertyAccessor(ListComponent<T> component) {
		this.component = component;
		// Map the column names to ids
		columnNames = new ArrayList<String>(component.getColumnCount());
		for (int i = 0; i < component.getColumnCount(); i++) {
			columnNames.add(i, component.getColumnName(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#getDataValue(
	 * java.lang.Object, int)
	 */
	@Override
	public Object getDataValue(T rowObject, int columnIndex) {
		return component.getColumnValue(rowObject, columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#setDataValue(
	 * java.lang.Object, int, java.lang.Object)
	 */
	@Override
	public void setDataValue(T rowObject, int columnIndex, Object newValue) {
		component.setColumnValue(rowObject, newValue, columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return component.getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.nebula.widgets.nattable.data.IColumnPropertyResolver#
	 * getColumnProperty(int)
	 */
	@Override
	public String getColumnProperty(int columnIndex) {
		return component.getColumnName(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.nebula.widgets.nattable.data.IColumnPropertyResolver#
	 * getColumnIndex(java.lang.String)
	 */
	@Override
	public int getColumnIndex(String propertyName) {
		return columnNames.indexOf(propertyName);
	}

}
