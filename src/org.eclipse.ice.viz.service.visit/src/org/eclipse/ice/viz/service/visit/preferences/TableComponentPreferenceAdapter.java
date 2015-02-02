package org.eclipse.ice.viz.service.visit.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.osgi.service.prefs.BackingStoreException;

public class TableComponentPreferenceAdapter {

	/**
	 * Each row should be stored in a separate node. This is necessary because
	 * removing nodes is the only way to delete preferences from the store,
	 * e.g., when a row was removed.
	 */
	private static final String SEPARATOR = ";";

	public static void toPreferences(TableComponent table,
			IEclipsePreferences parentNode) {

		// The path to the preference node containing the table.
		String path = parentNode.absolutePath() + ".table." + table.getName();
		IEclipsePreferences node;

		// Remove the previous table preferences.
		try {
			if (parentNode.nodeExists(path)) {
				parentNode.node(path).removeNode();
				parentNode.flush();
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		// Create a new node for the table preferences.
		node = (IEclipsePreferences) parentNode.node(path);
		// Make sure the parent node has the new child node.
		try {
			parentNode.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		// Write the number of rows.
		int rows = table.numberOfRows();
		node.putInt("rows", rows);

		// Write the column names. This implicitly yields the number of columns.
		int columns = table.numberOfColumns();
		if (columns > 0) {
			List<Entry> template = table.getRowTemplate();
			String columnNames = template.get(0).getName();
			for (int j = 0; j < columns; j++) {
				columnNames += SEPARATOR + template.get(j).getName();
			}
			node.put("columns", columnNames);
		} else {
			node.put("columns", "");
		}

		// Write the value of each Entry (per row, column).
		for (int i = 0; i < rows; i++) {
			List<Entry> row = table.getRow(i);
			for (int j = 0; j < columns; j++) {
				String key = Integer.toString(i) + "." + Integer.toString(j);
				node.put(key, row.get(j).getValue());
			}
		}

		// Make sure the child node is updated.
		try {
			node.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		return;
	}

	public static void toTableComponent(IEclipsePreferences parentNode,
			TableComponent table) {

		// The path to the preference node containing the table.
		String path = parentNode.absolutePath() + ".table." + table.getName();
		IEclipsePreferences node;

		// Make sure the table's preferences exist. If not, return.
		try {
			if (!parentNode.nodeExists(path)) {
				return;
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		// Get the node for the table preferences.
		node = (IEclipsePreferences) parentNode.node(path);

		// Get the number of rows.
		int rows = node.getInt("rows", 0);

		// Get the column names.
		String columnNames = node.get("columns", "");
		String[] split = columnNames.split(SEPARATOR);
		int columns = columnNames.isEmpty() ? 0 : split.length;
		if (columns != table.numberOfColumns()) {
			throw new IllegalArgumentException(
					"TableComponentPreferenceAdapter error: "
							+ "Specified TableComponent has incorrect number of columns.");
		}
		if (columns > 0) {
			List<Entry> template = table.getRowTemplate();
			List<String> columnNameList = new ArrayList<String>(columns);
			for (int j = 0; j < columns; j++) {
				String columnName = split[j];
				columnNameList.add(columnName);

				if (!columnName.equals(template.get(j).getName())) {
					throw new IllegalArgumentException(
							"TableComponentPreferenceAdapter error: "
									+ "Specified TableComponent has mismatching column names. Expected \""
									+ columnName + "\" but found \""
									+ template.get(j).getName() + "\".");
				}
			}
		}

		// Read in each row.
		for (int i = 0; i < rows; i++) {
			int id = table.addRow();
			List<Entry> row = table.getRow(id);
			for (int j = 0; j < columns; j++) {
				Entry entry = row.get(j);
				String key = Integer.toString(i) + "." + Integer.toString(j);
				String value = node.get(key, entry.getDefaultValue());
				if (!entry.setValue(value)) {
					System.out
							.println("TableComponentPreferenceAdapter warning: "
									+ "Value \""
									+ value
									+ "\" was not accepted for row "
									+ i
									+ ", column \"" + entry.getName() + "\".");
				}
			}
		}

		return;
	}

}
