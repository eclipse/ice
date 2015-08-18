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

import org.eclipse.ice.viz.service.connections.preferences.ConnectionTable;
import org.eclipse.ice.viz.service.connections.preferences.PortEntry;
import org.eclipse.ice.viz.service.connections.preferences.PortEntryContentProvider;
import org.eclipse.ice.viz.service.connections.preferences.VizConnectionPreferencePage;
import org.eclipse.ice.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.ice.viz.service.datastructures.VizEntry;
import org.eclipse.ice.viz.service.visit.VisItVizService;
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
	 * Overrides a method from VizConnectionPreferencePage.
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
	 * Implements an abstract method from VizConnectionPreferencePage.
	 */
	@Override
	protected String getConnectionsPreferenceNodeId() {
		return VisItVizService.CONNECTIONS_NODE_ID;
	}

	/*
	 * Overrides a method from VizConnectionPreferencePage.
	 */
	@Override
	public void init(IWorkbench workbench) {
		super.init(workbench);

		// Replace the default title.
		setDescription("VisIt Visualization Preferences");
	}

}