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
package org.eclipse.ice.viz.service.connections.visit;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.viz.service.connections.ConnectionTable;
import org.eclipse.ice.viz.service.connections.PortEntry;
import org.eclipse.ice.viz.service.connections.PortEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.ice.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.ice.viz.service.datastructures.VizEntry;

/**
 * This class extends {@link ConnectionTable} to provide additional connection
 * preferences specific to the VisIt vizualization service.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnectionTable extends ConnectionTable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionManager#
	 * getConnectionTemplate()
	 */
	@Override
	protected ArrayList<VizEntry> getConnectionTemplate() {
		// Get the default connection template so we can add additional columns.
		ArrayList<VizEntry> template = super.getConnectionTemplate();

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
		// ---- visit password ---- //
		// contentProvider = new BasicEntryContentProvider();
		// contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		// contentProvider.setDefaultValue("");
		// Entry visitPasswordEntry = new Entry(contentProvider);
		// visitPasswordEntry.setName("VisIt Password");
		// template.add(visitPasswordEntry);

		return template;
	}
}
