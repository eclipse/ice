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
package org.eclipse.ice.viz.service.visit.connections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ice.viz.service.connections.preferences.ConnectionTable;
import org.eclipse.ice.viz.service.connections.preferences.PortEntry;
import org.eclipse.ice.viz.service.connections.preferences.PortEntryContentProvider;
import org.eclipse.ice.viz.service.connections.preferences.VizConnectionPreferencePage;
import org.eclipse.ice.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.ice.viz.service.datastructures.VizEntry;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.visit.VisItVizService;
import org.eclipse.ui.IWorkbench;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class provides a preferences page for the VisIt viz service's
 * connections.
 * 
 * @author Jordan Deyton
 * 
 */
public class VisItConnectionPreferencePage extends VizConnectionPreferencePage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.preferences.
	 * VizConnectionPreferencePage#createConnectionTable()
	 */
	@Override
	protected ConnectionTable createConnectionTable() {
		// Add a proxy, proxy port, and visit user to the table.
		return new ConnectionTable() {
			@Override
			protected ArrayList<VizEntry> createConnectionTemplate() {
				// Get the default connection template so we can add additional
				// columns.
				ArrayList<VizEntry> template = super.createConnectionTemplate();

				// TODO These Entries need descriptions.

				IVizEntryContentProvider contentProvider;

				// ---- proxy ---- //
				contentProvider = new BasicVizEntryContentProvider();
				contentProvider.setDefaultValue("");
				VizEntry proxyEntry = new VizEntry(contentProvider);
				proxyEntry.setName("Proxy");
				template.add(proxyEntry);
				// ---- proxy port ---- //
				PortEntryContentProvider portContentProvider = new PortEntryContentProvider();
				portContentProvider.setRange(PortEntryContentProvider.MIN_PORT, PortEntryContentProvider.MAX_PORT);
				portContentProvider.setDefaultValue(22);
				VizEntry proxyPortEntry = new PortEntry(portContentProvider);
				proxyPortEntry.setName("Proxy Port");
				template.add(proxyPortEntry);
				// ---- visit user ---- //
				contentProvider = new BasicVizEntryContentProvider();
				contentProvider.setAllowedValueType(VizAllowedValueType.Undefined);
				contentProvider.setDefaultValue("");
				VizEntry visitUserEntry = new VizEntry(contentProvider);
				visitUserEntry.setName("VisIt User");
				template.add(visitUserEntry);

				Iterator<VizEntry> it = template.iterator();
				while (it.hasNext()) {
					VizEntry entry = it.next();
					if ("Path".equals(entry.getName())) {
						it.remove();
					}
				}

				contentProvider = new BasicVizEntryContentProvider();
				contentProvider.setAllowedValueType(VizAllowedValueType.Executable);
				ArrayList<String> allowedValues = new ArrayList<String>();
				String defaultPath = System.getProperty("visit.binpath");
				if (defaultPath != null) {
					if (defaultPath.contains("@user.home")) {
						defaultPath = defaultPath.replace("@user.home", System.getProperty("user.home"));
						allowedValues.add(defaultPath);
					}
				}
				contentProvider.setAllowedValues(allowedValues);
				VizEntry pathEntry = new VizEntry(contentProvider);
				pathEntry.setName("Path");
				pathEntry.setDescription("The full path to the local or remote installation.");
				template.add(3, pathEntry);

				// ---- visit password ---- //
				// contentProvider = new BasicEntryContentProvider();
				// contentProvider.setAllowedValueType(AllowedValueType.Undefined);
				// contentProvider.setDefaultValue("");
				// Entry visitPasswordEntry = new Entry(contentProvider);
				// visitPasswordEntry.setName("VisIt Password");
				// template.add(visitPasswordEntry);

				return template;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.preferences.
	 * VizConnectionPreferencePage#getConnectionsPreferenceNodeId()
	 */
	@Override
	protected String getConnectionsPreferenceNodeId() {
		return VisItVizService.CONNECTIONS_NODE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.preferences.
	 * VizConnectionPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		super.init(workbench);

		// Replace the default title.
		setDescription("VisIt Visualization Preferences");
	}

	/**
	 * Serializes the specified connection preferences into a string that can be
	 * stored in the preferences.
	 * 
	 * @param connection
	 *            The table row for the connection.
	 * @return The serialized connection preferences.
	 */
	@Override
	protected String serializeConnectionPreferences(List<VizEntry> connection) {
		String preferences = "";
		String delimiter = getConnectionPreferenceDelimiter();

		if (connection.size() >= 2) {
			// Add the first preference.
			preferences += connection.get(1).getValue();
			// Add all remaining preferences.
			for (int i = 2; i < connection.size(); i++) {
				VizEntry e = connection.get(i);
				if ("Path".equals(e.getName())) {
					String pathSave = "";
					for (String path : e.getAllowedValues()) {
						pathSave += path + ";";
					}
					// Remove the last ;
					pathSave.substring(0, pathSave.lastIndexOf(";"));
					preferences += delimiter + pathSave;
				} else {
					preferences += delimiter + e.getValue();
				}
			}
		}

		return preferences;
	}

	/**
	 * Loads the specified {@link ConnectionTable} based on the current
	 * preferences.
	 * 
	 * @param table
	 *            The table to load the current preferences into.
	 */
	@Override
	protected void loadPreferences(ConnectionTable table) {
		// Get the preference node for connection preferences.
		CustomScopedPreferenceStore store = (CustomScopedPreferenceStore) getPreferenceStore();
		IEclipsePreferences node = store.getNode(getConnectionsPreferenceNodeId());

		// Load the current preferences into the table.
		String[] existingKeys;
		try {
			existingKeys = node.keys();
			for (String key : existingKeys) {
				int index = table.addRow();
				List<VizEntry> row = table.getRow(index);

				// Update the key/name in the table.
				row.get(0).setValue(key);
				// Update the other properties.
				String[] preferences = unserializeConnectionPreferences(node.get(key, null));
				for (int i = 0; i < preferences.length; i++) {
					if ("Path".equals(row.get(i + 1).getName())) {
						String[] paths = preferences[i].split(";");
						IVizEntryContentProvider contentProvider = new BasicVizEntryContentProvider();
						contentProvider.setAllowedValueType(VizAllowedValueType.Executable);
						contentProvider.setAllowedValues(new ArrayList<String>(Arrays.asList(paths)));
						row.get(i + 1).setContentProvider(contentProvider);
						row.get(i + 1).setValue(paths[0]);
					} else {
						row.get(i + 1).setValue(preferences[i]);
					}
				}
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}