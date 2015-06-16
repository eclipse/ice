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
	protected ArrayList<Entry> getConnectionTemplate() {
		// Get the default connection template so we can add additional columns.
		ArrayList<Entry> template = super.getConnectionTemplate();

		// TODO These Entries need descriptions.

		IEntryContentProvider contentProvider;

		// ---- proxy ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setDefaultValue("");
		Entry proxyEntry = new Entry(contentProvider);
		proxyEntry.setName("Proxy");
		template.add(proxyEntry);
		// ---- proxy port ---- //
		PortEntryContentProvider portContentProvider = new PortEntryContentProvider();
		portContentProvider.setRange(PortEntryContentProvider.MIN_PORT,
				PortEntryContentProvider.MAX_PORT);
		portContentProvider.setDefaultValue(22);
		Entry proxyPortEntry = new PortEntry(portContentProvider);
		proxyPortEntry.setName("Proxy Port");
		template.add(proxyPortEntry);
		// ---- visit user ---- //
		contentProvider = new BasicEntryContentProvider();
		contentProvider.setAllowedValueType(AllowedValueType.Undefined);
		contentProvider.setDefaultValue("");
		Entry visitUserEntry = new Entry(contentProvider);
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
