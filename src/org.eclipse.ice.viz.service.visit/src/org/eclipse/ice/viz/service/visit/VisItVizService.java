/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtConnectionManager;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.FrameworkUtil;

/**
 * This is an implementation of the IVizService interface for the VisIt
 * visualization tool.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItVizService implements IVizService {

	/**
	 * A reference to the associated preference page's {@link IPreferenceStore}.
	 * If this has been determined previously, then it should be returned in
	 * {@link #getPreferenceStore()}.
	 */
	private IPreferenceStore preferenceStore = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "VisIt";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "2.8.2";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * hasConnectionProperties()
	 */
	@Override
	public boolean hasConnectionProperties() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * getConnectionProperties()
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		Map<String, String> preferences = new TreeMap<String, String>();
		IPreferenceStore store = getPreferenceStore();

		for (ConnectionPreference p : ConnectionPreference.values()) {
			String preferenceId = p.toString();
			preferences.put(preferenceId, store.getString(preferenceId));
		}

		return preferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * setConnectionProperties(java.util.Map)
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		if (props != null) {
			IPreferenceStore store = getPreferenceStore();

			for (Entry<String, String> prop : props.entrySet()) {
				String preferenceId = prop.getKey();
				try {
					ConnectionPreference.valueOf(preferenceId);
					store.setValue(preferenceId, prop.getValue());
				} catch (IllegalArgumentException e) {
					// Could not process the current property.
				}
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		boolean connected = false;
		IPreferenceStore store = getPreferenceStore();

		Map<String, String> visitPreferences = new HashMap<String, String>();
		for (ConnectionPreference p : ConnectionPreference.values()) {
			visitPreferences.put(p.getID(), store.getString(p.toString()));
		}
		
		String id = store.getString(ConnectionPreference.ConnectionID.getID());
		if (VisItSwtConnectionManager.hasConnection(id)) {
			connection = VisItSwtConnectionManager.getConnection(id);
		} else {
			connection = VisItSwtConnectionManager.createConnection(id,
					Display.getDefault(), visitPreferences);
		}

		connected = (connection != null);
		System.out.println("Connected to VisIt: " + connected);

		return connected;
	}
	
	private VisItSwtConnection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizService#createPlot(java
	 * .net.URI)
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {
		VisItPlot plot = new VisItPlot(file, connection);
		plot.setProperties(getConnectionProperties()); // FIXME It should have
														// other properties...
		return plot;
	}

	/**
	 * Gets the {@link IPreferenceStore} for the associated preference page.
	 * 
	 * @return The {@code IPreferenceStore} whose defaults should be set.
	 */
	private IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			// Get the PreferenceStore for the bundle.
			String id = FrameworkUtil.getBundle(getClass()).getSymbolicName();
			preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
					id);
		}
		return preferenceStore;
	}

}
