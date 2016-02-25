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
package org.eclipse.eavp.viz.service.paraview.preferences;

import org.eclipse.eavp.viz.service.preferences.AbstractVizPreferenceInitializer;

/**
 * This class initializes default preferences for the ParaView visualization
 * service.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPreferenceInitializer extends AbstractVizPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		// // Get the PreferenceStore used for the ParaView viz service
		// preferences.
		// IPreferenceStore store = getPreferenceStore();
		//
		// // Set all defaults.
		// store.setDefault("preference_name", "preference_value_of_some_type");

		return;
	}

}
