package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.ice.datastructures.form.TableComponent;

public class TableComponentPreferenceAdapter {

	/**
	 * Each row should be stored in a separate node. This is necessary because
	 * removing nodes is the only way to delete preferences from the store,
	 * e.g., when a row was removed.
	 */
	private static final String SEPARATOR = ";";

	public static void toPreferences(TableComponent table,
			CustomScopedPreferenceStore preferences) {

	}

	public static void toTableComponent(CustomScopedPreferenceStore prefNode,
			TableComponent table) {
		
	}

}
