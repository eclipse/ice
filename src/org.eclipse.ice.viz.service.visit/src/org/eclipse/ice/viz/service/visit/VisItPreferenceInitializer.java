package org.eclipse.ice.viz.service.visit;

import org.eclipse.ice.viz.service.AbstractVizPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class initializes default preferences for the VisIt visualization
 * service.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItPreferenceInitializer extends
		AbstractVizPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		// Get the PreferenceStore used for the VisIt viz service preferences.
		IPreferenceStore store = getPreferenceStore();

		// Set all defaults.
		store.setDefault("BOOLEAN_VALUE", true);

		return;
	}

}
