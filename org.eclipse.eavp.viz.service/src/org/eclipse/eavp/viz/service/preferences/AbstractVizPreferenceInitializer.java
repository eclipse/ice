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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This is the base class for setting default preferences for visualization
 * service preference pages added to the Eclipse IDE's Window > Preferences
 * dialog.
 * <p>
 * For an example of how to intitialize defaults for a preference page using
 * this class, see {@link VizPreferenceInitializer} and its class documentation.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractVizPreferenceInitializer extends
		AbstractPreferenceInitializer {

	/**
	 * A reference to the associated preference page's {@link IPreferenceStore}.
	 * If this has been determined previously, then it should be returned in
	 * {@link #getPreferenceStore()}.
	 */
	private IPreferenceStore preferenceStore = null;

	/**
	 * Gets the {@link IPreferenceStore} for the associated preference page.
	 * 
	 * @return The {@code IPreferenceStore} whose defaults should be set.
	 */
	protected IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			// Get the PreferenceStore for the bundle.
			preferenceStore = new CustomScopedPreferenceStore(getClass());
		}
		return preferenceStore;
	}

}
