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
package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.*;

import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.client.widgets.viz.service.IVizServiceFactory;
import org.eclipse.ice.viz.service.BasicVizServiceFactory;
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
	 * {@link org.eclipse.ice.viz.service.BasicVizServiceFactory#register(org.eclipse.ice.client.widgets.viz.service.IVizService)}
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
	 * {@link org.eclipse.ice.viz.service.BasicVizServiceFactory#getServiceNames()}
	 * .
	 */
	@Test
	public void testGetServiceNames() {

		// Create the service factory
		IVizServiceFactory factory = new BasicVizServiceFactory();

		// Register a two fake IVizServices with it
		String serviceName1 = "Bassnectar";
		String serviceName2 = "Velvetine";
		IVizService service1 = new FakeVizService(serviceName1);
		IVizService service2 = new FakeVizService(serviceName2);
		factory.register(service1);
		factory.register(service2);

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

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.BasicVizServiceFactory#get()} .
	 * 
	 * It makes sure that the default service, "ice-plot," can be retreived if
	 * it is available.
	 */
	@Test
	public void testGetDefaultService() {

		// Create the service factory
		IVizServiceFactory factory = new BasicVizServiceFactory();

		// Register a fake IVizService with it
		String serviceName = "ice-plot";
		IVizService service = new FakeVizService(serviceName);
		factory.register(service);

		// Try to get the service
		assertEquals(serviceName, factory.get(serviceName).getName());

		// Unregister the service
		factory.unregister(service);

		// Make sure it cannot be retrieved
		assertNull(factory.get(serviceName));

	}

}
