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
package org.eclipse.ice.viz.service.paraview;

import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.preferences.VizConnectionPreferencePage;

/**
 * This class provides a preference page for the ParaView visualization service.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPreferencePage extends VizConnectionPreferencePage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.paraview.VizConnectionPreferencePage#
	 * applyKeyChangeInfo(java.util.Map, java.util.Set, java.util.Set)
	 */
	@Override
	protected void applyKeyChangeInfo(Map<String, String> changedKeys,
			Set<String> addedKeys, Set<String> removedKeys) {
		ParaViewVizService.getInstance().preferencesChanged(changedKeys,
				addedKeys, removedKeys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.AbstractVizPreferencePage#getVizService()
	 */
	@Override
	protected IVizService getVizService() {
		return ParaViewVizService.getInstance();
	}

}
