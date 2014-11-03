/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.test.pwr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactorXMLAdapter;
import org.junit.Test;

/**
 * This class tests the custom XML adapter for PressurizedWaterReactors.
 * 
 * @author bkj
 * 
 */
public class PressurizedWaterReactorXMLAdapterTester {

	/**
	 * This operation tests the PWR XML adapter used by the persistence
	 * provider.
	 */
	@Test
	public void checkPWRAdapter() {

		PressurizedWaterReactorXMLAdapter adapter = new PressurizedWaterReactorXMLAdapter();
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(15);

		try {
			assertNotNull(adapter.marshal(reactor));
			assertNotNull(adapter.unmarshal("Test string"));
		} catch (Exception e) {
			// Complain
			e.printStackTrace();
			// And fail
			fail();
		}

		return;
	}

}
