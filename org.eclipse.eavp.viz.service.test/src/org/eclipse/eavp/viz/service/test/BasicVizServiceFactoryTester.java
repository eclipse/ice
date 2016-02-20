/******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.eavp.viz.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.BasicVizServiceFactory;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.junit.Test;

/**
 * This class is responsible for testing the BasicVizServiceFactory.
 * 
 * @author jaybilly
 *
 */
public class BasicVizServiceFactoryTester {

	/**
	 * Test method for
	 * {@link org.eclipse.eavp.viz.service.BasicVizServiceFactory#register(org.eclipse.eavp.viz.service.IVizService)}
	 * .
	 */
	@Test
	public void testRegister() {

		// Create the service factory
		IVizServiceFactory factory = new BasicVizServiceFactory();

		// Register a fake IVizService with it
		String serviceName = "Bassnectar";
		IVizService service = new FakeVizService(serviceName);
		factory.register(service);

		// Try to get the service
		assertEquals(serviceName, factory.get(serviceName).getName());

		// Unregister the service
		factory.unregister(service);

		// Make sure it cannot be retrieved
		assertNull(factory.get(serviceName));

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.eavp.viz.service.BasicVizServiceFactory#getServiceNames()}
	 * .
	 */
	@Test
	public void testGetServiceNames() {

		// Create the service factory
		BasicVizServiceFactory factory = new BasicVizServiceFactory();
		factory.start();

		// Register a two fake IVizServices with it
		String serviceName1 = "Bassnectar";
		String serviceName2 = "ice-plot";
		IVizService service1 = new FakeVizService(serviceName1);
		factory.register(service1);

		// Get the service names and check them
		String[] serviceNames = factory.getServiceNames();
		assertNotEquals(serviceNames[0], serviceNames[1]);
		assertTrue(serviceNames[0] == serviceName1
				|| serviceNames[0] == serviceName2);
		assertTrue(serviceNames[1] == serviceName1
				|| serviceNames[1] == serviceName2);

		// Try to get them both
		assertEquals(serviceName1, factory.get(serviceName1).getName());
		assertEquals(serviceName2, factory.get(serviceName2).getName());

		return;
	}

}
