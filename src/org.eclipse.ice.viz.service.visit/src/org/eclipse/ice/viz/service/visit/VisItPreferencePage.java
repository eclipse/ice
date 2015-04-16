/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import java.util.Map;
import java.util.Set;

import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.viz.service.connections.ConnectionTable;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionTable;
import org.eclipse.ice.viz.service.preferences.VizConnectionPreferencePage;

/**
 * This class provides a preferences page for the VisIt visualization service.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItPreferencePage extends VizConnectionPreferencePage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.preferences.VizConnectionPreferencePage#
	 * applyKeyChangeInfo(java.util.Map, java.util.Set, java.util.Set)
	 */
	@Override
	protected void applyKeyChangeInfo(Map<String, String> changedKeys,
			Set<String> addedKeys, Set<String> removedKeys) {
		VisItVizService.getInstance().preferencesChanged(changedKeys,
				addedKeys, removedKeys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.preferences.VizConnectionPreferencePage#
	 * createConnectionTable()
	 */
	@Override
	protected ConnectionTable createConnectionTable() {
		return new VisItConnectionTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.AbstractVizPreferencePage#getVizService()
	 */
	@Override
	protected IVizService getVizService() {
		return VisItVizService.getInstance();
	}
}