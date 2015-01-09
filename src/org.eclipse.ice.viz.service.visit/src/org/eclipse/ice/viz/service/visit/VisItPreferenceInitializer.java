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

		// TODO Each of these preferences will need to be customtailored, e.g.,
		// some may be booleans.
		for (ConnectionPreference p : ConnectionPreference.values()) {
			store.setDefault(p.toString(), p.getDefaultValue());
		}

		return;
	}

}
