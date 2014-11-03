/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.servicetester;

import org.eclipse.ice.core.iCore.ICore;

/* This class is used to test the OSGi/Equinox Declarative Services setup.
 */
public class ServiceTester {

	// Reference to the ICore service
	ICore iceCore;

	// This operation prints a message when the component is started
	public void start() {
		System.out.println("ServiceTester started.");
	}

	// This operation registers the ICore service with the ServiceTester
	public void registerService(ICore core) {
		this.iceCore = core;
		System.out.println("ICore service registered with ServiceTester");
	}

	// This operation un-registers the ICore service with the ServiceTester
	public void unregisterService(ICore core) {
		if (iceCore == core) {
			iceCore = null;
			System.out.println("ICore has been unregistered with the "
					+ "ServiceTester");
		}

	}

}
