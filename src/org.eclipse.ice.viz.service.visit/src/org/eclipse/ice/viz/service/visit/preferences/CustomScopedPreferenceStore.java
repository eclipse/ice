package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class CustomScopedPreferenceStore extends ScopedPreferenceStore {

	public CustomScopedPreferenceStore(IScopeContext context, String qualifier) {
		super(context, qualifier);
		// TODO Auto-generated constructor stub
	}
	
	public void removeValue(String name) {
		// TODO Remove the value from the IEclipsePreferences
		// e.g. preferences.remove(name)
		// To do this, we must have a reference to the scoped preferences!
	}

}
