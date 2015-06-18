/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.materials;

import org.eclipse.ice.datastructures.form.MaterialStack;

import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * Implements the WritableTableFormat for use with glazed lists to display data
 * about material stacks. Mainly used for the stoichiometry calculator at the
 * moment.
 * 
 * 
 * @author Kasper Gammeltoft
 * 
 */
public class MaterialStackWritableTableFormat implements
		WritableTableFormat<MaterialStack> {

	/**
	 * The constructor
	 */
	public MaterialStackWritableTableFormat() {
		// Nothing TODO
	}

	/**
	 * Gets the column count, should be just 2 columns. The first displays the
	 * name and the second the amount in the stack.
	 * 
	 * @return The number of columns
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/**
	 * Gets the column name for the specified column.
	 * 
	 * @param column
	 *            The column to get the name for. Should either be 0 or 1
	 * @return Returns "Material" if column is 0, and "Amount" if otherwise.
	 */
	@Override
	public String getColumnName(int column) {
		String colName;
		if (column == 0) {
			colName = "Material";
		} else {
			colName = "Amount";
		}
		return colName;
	}

	/**
	 * Gets the value for the specified material stack and column.
	 * 
	 * @param stack
	 *            The material stack
	 * @param column
	 *            The column (either 0 or 1).
	 * @return Returns either the name of the material (if column is 0) or the
	 *         amount in the stack.
	 */
	@Override
	public Object getColumnValue(MaterialStack stack, int column) {
		Object value;
		if (column == 0) {
			value = stack.getMaterial().getName();
		} else {
			value = stack.getAmount();
		}
		return value;
	}

	/**
	 * Should allow only for editing the amount, not the name of the material
	 * for the specified material stack.
	 * 
	 * @param stack
	 *            The material stack
	 * @param column
	 *            The column to edit. Ignores if this is 0, otherwise returns
	 *            true.
	 * @return Returns true if the stack is editable at that column, false if
	 *         otherwise.
	 */
	@Override
	public boolean isEditable(MaterialStack stack, int column) {
		boolean val;
		if (column == 0) {
			val = false;
		} else {
			val = true;
		}
		val = true;
		return val;
	}

	/**
	 * Sets the column value for the specified material stack and column. Will
	 * only allow for the amount in the stack to be set.
	 * 
	 * @param stack
	 *            The material stack
	 * @param newValue
	 *            The new value for the amount in the material stack. Should
	 *            accept int, double, and String values.
	 */
	@Override
	public MaterialStack setColumnValue(MaterialStack stack, Object newValue,
			int column) {
		if (column > 0) {
			int value = stack.getAmount();
			if (newValue instanceof String) {
				try {
					value = (int) Double.parseDouble((String) newValue);
				} catch (Exception e) {

				}
			} else {
				try {
					value = (Integer) newValue;
				} catch (Exception e) {

				}
			}
			stack.setAmount(value);
		}
		return stack;
	}

}
