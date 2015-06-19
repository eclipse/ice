package org.eclipse.ice.viz.service.internal.test;

import static org.junit.Assert.*;

import org.eclipse.ice.viz.service.BasicVizServiceFactory;
import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.IVizServiceFactory;
import org.eclipse.ice.viz.service.internal.VizServiceFactoryHolder;
import org.eclipse.ice.viz.service.test.FakeVizService;
import org.junit.Test;

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

		VizServiceFactoryHolder holder = new VizServiceFactoryHolder();

		// Test that the factory has been registered
		holder.setVizServiceFactory(factory);
		assertNotNull(holder.getFactory());

		// Test that the factory in the holder is the same factory registered.
		assertEquals("TestService", holder.getFactory().getServiceNames()[0]);

		// Test that the holder is empty after removing the factory.
		holder.unsetVizServiceFactory();
		assertNull(holder.getFactory());

	}

	/**
	 * Creates a basic VizServiceFactory for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class FakeVizServiceFactory implements IVizServiceFactory {
		private IVizService service;

		@Override
		public void register(IVizService newService) {
			// TODO Auto-generated method stub
			service = newService;
		}

		@Override
		public void unregister(IVizService service) {
			// TODO Auto-generated method stub

		}

		@Override
		public String[] getServiceNames() {
			// TODO Auto-generated method stub
			String[] serviceNames = new String[1];
			serviceNames[0] = service.getName();
			return serviceNames;
		}

		@Override
		public IVizService get(String serviceName) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IVizService get() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
