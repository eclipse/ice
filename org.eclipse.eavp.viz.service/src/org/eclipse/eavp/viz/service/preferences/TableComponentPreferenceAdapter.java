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
package org.eclipse.eavp.viz.service.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizTableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@code TableComponentPreferenceAdapter} can marshal a
 * {@link VizTableComponent} to or from a {@link CustomScopedPreferenceStore}. The
 * resulting preferences are stored in an {@link IEclipsePreferences} node,
 * whose relative path is "table.name" where "name" is the name of the
 * {@code TableComponent}. This node is a child node of the store's underlying
 * preferences node.
 * 
 * <p>
 * Use of this class requires explicit knowledge of the structure of the
 * {@code TableComponent}. For instance, for a table with two columns "one" and
 * "two", the following would <i>not</i> work:
 * </p>
 * 
 * <pre>
 * <code>
 *  TableComponent table = new TableComponent();
 *  TableComponentPreferenceAdapter adapter = new TableComponentPreferenceAdapter();
 *  adapter.toTableComponent(store, table);
 * </code>
 * </pre>
 * 
 * Instead, you would need to construct the {@code TableComponent}'s template:
 * 
 * <pre>
 * <code>
 *  TableComponent table = new TableComponent();
 *  List{@literal <Entry>} template = new ArrayList{@literal <Entry>}(2);
 *  // Create the template Entry for column "one" and add it to the list...
 *  // Create the template Entry for column "two" and add it to the list... 
 *  table.setRowTemplate(template);
 *  TableComponentPreferenceAdapter adapter = new TableComponentPreferenceAdapter();
 *  adapter.toTableComponent(store, table);
 * </code>
 * </pre>
 * <p>
 * Future implementations of this class may be able to handle
 * {@code TableComponent} template creation automatically. Currently, the
 * approach is simply to write only the value for each {@code Entry} in the
 * table.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class TableComponentPreferenceAdapter {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TableComponentPreferenceAdapter.class);

	/**
	 * The separator used when a serialized list of strings is written to a
	 * preference node.
	 */
	private static final String SEPARATOR = ";";

	/**
	 * Stores the rows of the specified {@link VizTableComponent} in the specified
	 * preference store under a node named "table.name", where "name" is the
	 * table's name.
	 * 
	 * @param table
	 *            The source table whose rows should be stored in the preference
	 *            store.
	 * @param store
	 *            The target preference store.
	 */
	public void toPreferences(VizTableComponent table,
			CustomScopedPreferenceStore store) {
		// Check the parameters.
		if (table == null || store == null) {
			throw new NullPointerException(
					"TableComponentPreferenceAdapter error: "
							+ "Cannot store preferences based from null arguments.");
		}

		// Clear out the old preferences.
		clearPreferences(table, store);

		String prefix = getPrefix(table);

		// Write the number of rows.
		int rows = table.numberOfRows();
		store.setValue(prefix + "rows", rows);

		// Write the column names. This implicitly yields the number of columns.
		int columns = table.numberOfColumns();
		// If there is more than one column, serialize their names.
		if (columns > 0) {
			List<VizEntry> template = table.getRowTemplate();
			String columnNames = template.get(0).getName();
			for (int j = 1; j < columns; j++) {
				columnNames += SEPARATOR + template.get(j).getName();
			}
			store.setValue(prefix + "columns", columnNames);
		}
		// Otherwise, we just need to use the empty string.
		else {
			store.setValue(prefix + "columns", "");
		}

		// Write the value of each Entry (per row, column).
		for (int i = 0; i < rows; i++) {
			List<VizEntry> row = table.getRow(i);
			for (int j = 0; j < columns; j++) {
				VizEntry entry = row.get(j);
				String key = prefix + Integer.toString(i) + "."
						+ Integer.toString(j);

				// If the Entry shouldn't be secret, just put it in the store.
				// Otherwise, it should be stored securely.
				if (!entry.isSecret()) {
					store.setValue(key, entry.getValue());
				} else {
					store.setSecureValue(key, entry.getValue());
				}
			}
		}

		return;
	}

	/**
	 * Reads in the rows of the specified {@link VizTableComponent} from the
	 * specified preference store.
	 * <p>
	 * <b>Note:</b> The table's template should match the values stored in the
	 * preference store. If not, then the table will not be updated.
	 * </p>
	 * 
	 * @param store
	 *            The source preference store.
	 * @param table
	 *            The target table whose rows should be read in from the
	 *            preference store.
	 */
	public void toTableComponent(CustomScopedPreferenceStore store,
			VizTableComponent table) {
		// Check the parameters.
		if (table == null || store == null) {
			throw new NullPointerException(
					"TableComponentPreferenceAdapter error: "
							+ "Cannot load preferences based from null arguments.");
		}

		String prefix = getPrefix(table);

		// Get the number of rows.
		int rows = store.getInt(prefix + "rows");

		boolean badTemplate = false;

		// Get the column names.
		String columnNames = store.getString(prefix + "columns");
		String[] split = columnNames.split(SEPARATOR);
		int columns = columnNames.isEmpty() ? 0 : split.length;
		// Log a problem with the table's template if the column count does not
		// match.
		if (columns != table.numberOfColumns()) {
			logger.info("TableComponentPreferenceAdapter warning: "
					+ "Specified TableComponent has incorrect number of columns.");
			badTemplate = true;
		}
		// Otherwise, check all of the column names.
		else if (columns > 0) {
			List<VizEntry> template = table.getRowTemplate();
			List<String> columnNameList = new ArrayList<String>(columns);
			for (int j = 0; j < columns; j++) {
				String columnName = split[j];
				columnNameList.add(columnName);

				// Log a problem with the table's template if the column names
				// don't match. We should exit before updating the
				// TableComponent's rows.
				if (!columnName.equals(template.get(j).getName())) {
					logger.info("TableComponentPreferenceAdapter warning: "
							+ "Specified TableComponent has "
							+ "mismatching column names. Expected \""
							+ columnName + "\" but found \""
							+ template.get(j).getName() + "\".");
					badTemplate = true;
				}
			}
		}

		// Do not proceed any further if the TableComponent is invalid.
		if (badTemplate) {
			return;
		}

		// Read in each row.
		for (int i = 0; i < rows; i++) {
			// Add a new row.
			int id = table.addRow();
			List<VizEntry> row = table.getRow(id);
			// Set the value for each Entry in the row.
			for (int j = 0; j < columns; j++) {
				VizEntry entry = row.get(j);
				// The key is stored as row.column using their indices.
				String key = prefix + Integer.toString(i) + "."
						+ Integer.toString(j);

				// Try to get the Entry's value from the primary store. If the
				// value is not there, it should be in secure storage.
				if (store.contains(key)) {
					entry.setValue(store.getString(key));
				} else {
					entry.setValue(store.getSecureString(key));
				}
			}
		}

		return;
	}

	/**
	 * Gets the relative path that should be used for the table.
	 * 
	 * @param table
	 *            The {@code TableComponent} that is being stored or loaded.
	 * @return The table's relative path. This is "table.name" where "name" is
	 *         the name of the {@code TableComponent}.
	 */
	private String getPrefix(VizTableComponent table) {
		return "table." + table.getName() + ".";
	}

	/**
	 * Removes all preferences associated with the specified table.
	 * 
	 * @param table
	 *            The table whose preferences should be removed. Only its
	 *            structure is required.
	 * @param store
	 *            The store from which to remove the preferences.
	 */
	private void clearPreferences(VizTableComponent table,
			CustomScopedPreferenceStore store) {

		final String prefix = getPrefix(table);

		// Get the number of rows, then remove it from the preferences.
		int rows = store.getInt(prefix + "rows");
		store.removeValue(prefix + "rows");

		// Get the column names (and number of columns), then remove it from the
		// preferences.
		String columnNames = store.getString(prefix + "columns");
		String[] split = columnNames.split(SEPARATOR);
		int columns = columnNames.isEmpty() ? 0 : split.length;
		store.removeValue(prefix + "columns");

		// Remove each row from the preferences.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				String key = prefix + Integer.toString(i) + "."
						+ Integer.toString(j);
				// We need to remove its value and its defaults or remove its
				// secure value.
				if (store.contains(key)) {
					store.removeValue(key);
				} else {
					store.removeSecureString(key);
				}
			}
		}

		return;
	}
}
