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
package org.eclipse.eavp.viz.service.visit.connections;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.eavp.viz.service.connections.preferences.ConnectionTable;
import org.eclipse.eavp.viz.service.connections.preferences.PortEntry;
import org.eclipse.eavp.viz.service.connections.preferences.PortEntryContentProvider;
import org.eclipse.eavp.viz.service.connections.preferences.VizConnectionPreferencePage;
import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.visit.VisItVizService;
import org.eclipse.ui.IWorkbench;

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
	 * @see org.eclipse.eavp.viz.service.connections.preferences.VizConnectionPreferencePage#createConnectionTable()
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
				portContentProvider.setRange(PortEntryContentProvider.MIN_PORT,
						PortEntryContentProvider.MAX_PORT);
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
						String defaultPath = System.getProperty("visit.binpath");
						if (defaultPath != null) {
							if (defaultPath.contains("@user.home")) {
								defaultPath = defaultPath.replace("@user.home", System.getProperty("user.home"));
							}
							entry.setValue(defaultPath);
						}
					}
				}
				
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
	 * @see org.eclipse.eavp.viz.service.connections.preferences.VizConnectionPreferencePage#getConnectionsPreferenceNodeId()
	 */
	@Override
	protected String getConnectionsPreferenceNodeId() {
		return VisItVizService.CONNECTIONS_NODE_ID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.preferences.VizConnectionPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		super.init(workbench);

		// Replace the default title.
		setDescription("VisIt Visualization Preferences");
	}

}
