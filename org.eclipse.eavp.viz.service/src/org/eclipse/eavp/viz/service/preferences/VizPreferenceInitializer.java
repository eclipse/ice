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
package org.eclipse.eavp.viz.service.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * An instance of this class is used by the platform to initialize default
 * values for visualization service preferences.
 * <p>
 * In addition to the code here, the extension point
 * {@code org.eclipse.core.runtime.preferences} must have an {@code initializer}
 * attribute set to this class. In this case, the extension point in
 * {@code plugin.xml} looks like:
 * </p>
 * 
 * <pre>
 * <code>
 * {@literal <plugin>}
 *    {@literal <}extension
 *          point="org.eclipse.core.runtime.preferences"{@literal >}
 *       {@literal <}initializer
 *             class="org.eclipse.eavp.viz.service.VizPreferenceInitializer"{@literal >}
 *       {@literal </initializer>}
 *    {@literal </extension>}
 * {@literal </plugin>}
 * </code>
 * </pre>
 * <p>
 * The same method should be used for preference initializers for other
 * preference pages.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class VizPreferenceInitializer extends AbstractVizPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		// Get the PreferenceStore for the general viz service preferences.
		IPreferenceStore store = getPreferenceStore();

		// Set all defaults for the visualization perspective.

		// By default, the default connections should automatically be
		// established on startup.
		store.setDefault("autoConnectToDefaults", true);

		return;
	}

}
