/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.reactor.perspective.internal;

import org.eclipse.ice.iclient.IClient;

public class ReactorClientHolder {

	/**
	 * The reference to the ICEClient provided by the OSGi.
	 */
	private static IClient client = null;

	/**
	 * Retrieve the reference to the client
	 */
	public static IClient getClient() {
		return client;
	}

	/**
	 * Retrieve the reference to the client
	 */
	public static void setClient(IClient iceClient) {

		// Set the reference and do some reporting
		if (iceClient != null) {
			client = iceClient;
			System.out.println("ICE Reactor Perspective Message: "
					+ "Client set successfully!");
		} else {
			System.out.println("ICE Reactor Perspective Message:"
					+ "Framework attempted to set client, but the "
					+ "reference was null.");
		}

		return;
	}

	/**
	 * "Unset" the reference to the client. That is, notify the Common bundle
	 * that the client has shutdown.
	 */
	public static void unsetClient(IClient iceClient) {
		if (iceClient == client) {
			client = null;
		}
	}

}
