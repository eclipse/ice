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

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IVizService;
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
		IVizService vizService = VisItVizService.getInstance();
		// We don't want to return a null value if the service is not available.
		// Create a blank viz service that does nothing.
		if (vizService == null) {
			// Print a debug message!
			System.err.println("VizItPreferencePage error: "
					+ "The VisItVizService is not running!");
			vizService = new AbstractVizService() {
				@Override
				public String getName() {
					return "VisIt";
				}

				@Override
				public String getVersion() {
					return "Not Available";
				}

				@Override
				public boolean hasConnectionProperties() {
					return false;
				}

				@Override
				public Map<String, String> getConnectionProperties() {
					return null;
				}

				@Override
				public void setConnectionProperties(Map<String, String> props) {
					return;
				}

				@Override
				public boolean connect() {
					return false;
				}

				@Override
				public boolean disconnect() {
					return false;
				}
			};
		}
		return vizService;
	}
}