/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.internal.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.eavp.viz.service.internal.VizServiceFactoryHolder;
import org.eclipse.eavp.viz.service.test.FakeVizService;
import org.junit.Test;

/**
 * A class which tests the functionality of the VizServiceFactoryHolder.
 * 
 * @author Robert Smith
 *
 */
public class VizServiceFactoryHolderTester {

	/**
	 * Checks that the factory holder's methods for registering, getting, and
	 * removing a factory are functioning.
	 * 
	 */
	@Test
	public void checkFactorySetting() {

		// Create a test factory with a test service.
		IVizServiceFactory factory = new FakeVizServiceFactory();
		String serviceName = "TestService";
		IVizService service = new FakeVizService(serviceName);
		factory.register(service);

		// Test that the factory has been registered
		VizServiceFactoryHolder.setVizServiceFactory(factory);
		assertNotNull(VizServiceFactoryHolder.getFactory());

		// Test that the factory in the holder is the same factory registered.
		assertEquals("TestService", VizServiceFactoryHolder.getFactory().getServiceNames()[0]);

		// Test that the holder is empty after removing the factory.
		VizServiceFactoryHolder.unsetVizServiceFactory(factory);
		assertNull(VizServiceFactoryHolder.getFactory());

	}

	/**
	 * Creates a basic VizServiceFactory for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class FakeVizServiceFactory implements IVizServiceFactory {
		private IVizService service;

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#register(org.eclipse.eavp.viz.service.IVizService)
		 */
		@Override
		public void register(IVizService newService) {
			service = newService;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#unregister(org.eclipse.eavp.viz.service.IVizService)
		 */
		@Override
		public void unregister(IVizService service) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#getServiceNames()
		 */
		@Override
		public String[] getServiceNames() {
			// TODO Auto-generated method stub
			String[] serviceNames = new String[1];
			serviceNames[0] = service.getName();
			return serviceNames;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#get(java.lang.String)
		 */
		@Override
		public IVizService get(String serviceName) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.IVizServiceFactory#get()
		 */
		@Override
		public IVizService get() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
