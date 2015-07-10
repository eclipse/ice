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

import org.eclipse.ice.viz.service.connections.VizConnection;
import org.eclipse.ice.viz.service.connections.VizConnectionManager;

import gov.lbnl.visit.swt.VisItSwtConnection;

/**
 * This class provides an {@link VizConnectionManager} that creates and manages
 * preferences for {@link VisItConnection}s.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnectionManager extends VizConnectionManager<VisItSwtConnection> {

	/*
	 * Implements an abstract method from VizConnectionManager.
	 */
	@Override
	protected VizConnection<VisItSwtConnection> createConnection(String name, String preferences) {
		VisItConnection connection = new VisItConnection();

		// Split the string using the delimiter. The -1 is necessary to include
		// empty values from the split.
		String[] split = preferences.split(getConnectionPreferenceDelimiter(), -1);

		try {
			// The name, host, port, and path will be taken care of by the super
			// class.

			// Ensure the additional custom preferences are set.
			connection.setProperty("gateway", split[3]);
			connection.setProperty("localGatewayPort", split[4]);
			connection.setProperty("username", split[5]);
		} catch (IndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
			// Cannot add the connection.
			connection = null;
		}

		return connection;
	}

}
