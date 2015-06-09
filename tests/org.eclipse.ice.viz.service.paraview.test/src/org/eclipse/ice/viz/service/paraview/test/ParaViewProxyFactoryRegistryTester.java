/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactoryRegistry;
import org.eclipse.ice.viz.service.paraview.proxy.ParaViewProxyFactoryRegistry;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link ParaViewProxyFactoryRegistry}'s implementation of
 * {@link IParaViewProxyFactoryRegistry}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewProxyFactoryRegistryTester {

	/**
	 * The registry for testing purposes. This will be re-created before each
	 * test.
	 */
	private IParaViewProxyFactoryRegistry registry;

	/**
	 * A factory that contains no supported extensions.
	 */
	private IParaViewProxyFactory fakeEmptyProxyFactory;

	/**
	 * A fake proxy factory that supports the common extensions for the Exodus
	 * file format.
	 */
	private FakeProxyFactory fakeExodusProxyFactory;

	/**
	 * A fake proxy factory that supports the common extensions for the Silo
	 * file format.
	 */
	private FakeProxyFactory fakeSiloProxyFactory;

	/**
	 * Instantiates the {@link #registry} and the fake proxy factories.
	 */
	@Before
	public void beforeEachTest() {
		// Create a new registry.
		registry = new ParaViewProxyFactoryRegistry();

		// Create the fake proxy factories.
		fakeEmptyProxyFactory = new FakeProxyFactory();
		fakeExodusProxyFactory = new FakeProxyFactory() {
			@Override
			public Set<String> getExtensions() {
				Set<String> extensions = super.getExtensions();
				extensions.add("ex");
				extensions.add("e");
				extensions.add("exo");
				extensions.add("ex2");
				extensions.add("exii");
				extensions.add("gen");
				extensions.add("exodus");
				extensions.add("nemesis");
				return extensions;
			}
		};
		fakeSiloProxyFactory = new FakeProxyFactory() {
			@Override
			public Set<String> getExtensions() {
				Set<String> extensions = super.getExtensions();
				extensions.add("silo");
				return extensions;
			}
		};

		return;
	}

	/**
	 * Checks the return values for register depending on whether the factory
	 * was registered previously or not and for invalid factories.
	 */
	@Test
	public void checkRegister() {

		final IParaViewProxyFactory nullFactory = null;

		// Registering new factories with supported extensions should return
		// true.
		assertTrue(registry.registerProxyFactory(fakeExodusProxyFactory));
		assertTrue(registry.registerProxyFactory(fakeSiloProxyFactory));

		// Registering the same factory again should still return true, as it
		// becomes the preferred factory for its supported extensions.
		assertTrue(registry.registerProxyFactory(fakeExodusProxyFactory));
		assertTrue(registry.registerProxyFactory(fakeSiloProxyFactory));

		// Registering a factory with no supported extensions should return
		// false (because nothing was registered!).
		assertFalse(registry.registerProxyFactory(fakeEmptyProxyFactory));

		// Registering a null factory should return false (because nothing was
		// registered!).
		assertFalse(registry.registerProxyFactory(nullFactory));

		return;
	}

	/**
	 * Checks the return values for unregister depending on whether the factory
	 * was registered previously or not and for invalid factories.
	 */
	@Test
	public void checkUnregister() {

		final IParaViewProxyFactory nullFactory = null;

		// Unregistering a factory that's not registered should return false.
		assertFalse(registry.unregisterProxyFactory(fakeExodusProxyFactory));
		assertFalse(registry.unregisterProxyFactory(fakeSiloProxyFactory));
		assertFalse(registry.unregisterProxyFactory(fakeEmptyProxyFactory));
		assertFalse(registry.unregisterProxyFactory(nullFactory));

		// Register the factories. Note that really only the Exodus and Silo
		// factories will be registered.
		registry.registerProxyFactory(fakeExodusProxyFactory);
		registry.registerProxyFactory(fakeSiloProxyFactory);
		registry.registerProxyFactory(fakeEmptyProxyFactory);
		registry.registerProxyFactory(nullFactory);

		// We should now be able to unregister the Exodus and Silo factories.
		// Those requests will return true. The other two reqquests will return
		// false because nothing was unregistered.
		assertTrue(registry.unregisterProxyFactory(fakeExodusProxyFactory));
		assertTrue(registry.unregisterProxyFactory(fakeSiloProxyFactory));
		assertFalse(registry.unregisterProxyFactory(fakeEmptyProxyFactory));
		assertFalse(registry.unregisterProxyFactory(nullFactory));

		// Make sure everything was really unregistered. If so, then nothing
		// will be unregistered, and all calls will return false.
		assertFalse(registry.unregisterProxyFactory(fakeExodusProxyFactory));
		assertFalse(registry.unregisterProxyFactory(fakeSiloProxyFactory));
		assertFalse(registry.unregisterProxyFactory(fakeEmptyProxyFactory));
		assertFalse(registry.unregisterProxyFactory(nullFactory));

		return;
	}

	/**
	 * Checks that the correct factory is returned based on the registered
	 * factories.
	 */
	@Test
	public void checkGetFactory() {

		URI uri;
		final URI nullURI = null;

		// Create another fake Exodus proxy factory that registers only for the
		// "e" extension.
		final IParaViewProxyFactory fakeExodusProxyFactory2 = new FakeProxyFactory() {
			@Override
			public Set<String> getExtensions() {
				Set<String> extensions = super.getExtensions();
				extensions.add("e");
				return extensions;
			}
		};

		// Initial calls should return null regardless of the URI, because there
		// are no registered factories.

		// Check this for all Exodus extensions.
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension);
			assertNull(registry.getProxyFactory(uri));
		}
		// Check this for all Silo extensions.
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension);
			assertNull(registry.getProxyFactory(uri));
		}
		// Check this for invalid URIs.
		assertNull(registry.getProxyFactory(TestUtils.createTestURI("bad")));
		assertNull(registry.getProxyFactory(nullURI));

		// Register the main fake Exodus and the single Silo factories.
		registry.registerProxyFactory(fakeExodusProxyFactory);
		registry.registerProxyFactory(fakeSiloProxyFactory);

		// Check that all of their extensions return the correct factory,
		// and that the query is case insensitive.
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension.toLowerCase());
			assertSame(fakeExodusProxyFactory, registry.getProxyFactory(uri));
			uri = TestUtils.createTestURI(extension.toUpperCase());
			assertSame(fakeExodusProxyFactory, registry.getProxyFactory(uri));
		}
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension.toLowerCase());
			assertSame(fakeSiloProxyFactory, registry.getProxyFactory(uri));
			uri = TestUtils.createTestURI(extension.toUpperCase());
			assertSame(fakeSiloProxyFactory, registry.getProxyFactory(uri));
		}

		// Register the second fake Exodus factory. It should now be returned
		// when the extension is "e", but all other Exodus extensions should
		// still be the first factory.
		registry.registerProxyFactory(fakeExodusProxyFactory2);
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension);
			if (!"e".equals(extension)) {
				assertSame(fakeExodusProxyFactory,
						registry.getProxyFactory(uri));
			} else {
				assertSame(fakeExodusProxyFactory2,
						registry.getProxyFactory(uri));
			}
		}

		// Now re-register the main fake Exodus factory. It should now take
		// precedence over the "e" extension.
		registry.registerProxyFactory(fakeExodusProxyFactory);
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension);
			assertSame(fakeExodusProxyFactory, registry.getProxyFactory(uri));
		}

		// Unregister the Silo factory. All factory requests for Silo extensions
		// should return null.
		registry.unregisterProxyFactory(fakeSiloProxyFactory);
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension);
			assertNull(registry.getProxyFactory(uri));
		}

		// Now unregister the main fake Exodus factory. Only the "e" extension
		// will still be supported by the second fake Exodus factory.
		registry.unregisterProxyFactory(fakeExodusProxyFactory);
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = TestUtils.createTestURI(extension);
			if (!"e".equals(extension)) {
				assertNull(registry.getProxyFactory(uri));
			} else {
				assertSame(fakeExodusProxyFactory2,
						registry.getProxyFactory(uri));
			}
		}

		// Now unregister the second fake Exodus factory. Requesting a factory
		// for the extension "e" should return null.
		registry.unregisterProxyFactory(fakeExodusProxyFactory2);
		assertNull(registry.getProxyFactory(TestUtils.createTestURI("e")));

		return;
	}

	/**
	 * Checks that all extensions for all registered factories can be queried
	 * from the registry.
	 */
	@Test
	public void checkExtensions() {

		// Create another fake Exodus proxy factory that registers only for the
		// "e" extension.
		final IParaViewProxyFactory fakeExodusProxyFactory2 = new FakeProxyFactory() {
			@Override
			public Set<String> getExtensions() {
				Set<String> extensions = super.getExtensions();
				extensions.add("e");
				return extensions;
			}
		};

		// Register the proxy factories.
		registry.registerProxyFactory(fakeExodusProxyFactory);
		registry.registerProxyFactory(fakeSiloProxyFactory);
		registry.registerProxyFactory(fakeExodusProxyFactory2);

		// The set of returned extensions should contain all Exodus and Silo
		// extensions, regardless of the existence of shared extensions.
		Set<String> expectedExtensions = new TreeSet<String>();
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			expectedExtensions.add(extension);
		}
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			expectedExtensions.add(extension);
		}

		// The returned set should be ordered (usually via a TreeSet).
		Set<String> supportedExtensions = registry.getExtensions();
		assertEquals(expectedExtensions, supportedExtensions);

		// The returned set should be a new, equivalent set each time.
		assertNotSame(supportedExtensions, registry.getExtensions());
		assertEquals(supportedExtensions, registry.getExtensions());

		// Modifying the returned set should make no difference.
		registry.getExtensions().clear();
		assertEquals(expectedExtensions, registry.getExtensions());

		return;
	}

	/**
	 * A fake implementation of {@link IParaViewProxyFactory} simply for testing
	 * purposes.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeProxyFactory implements IParaViewProxyFactory {

		/**
		 * Returns {@code null}.
		 */
		@Override
		public String getName() {
			return null;
		}

		/**
		 * Returns an empty hash set.
		 */
		@Override
		public Set<String> getExtensions() {
			return new HashSet<String>();
		}

		/**
		 * Returns null. This method shouldn't be used in this test.
		 */
		@Override
		public IParaViewProxy createProxy(URI file)
				throws IllegalArgumentException {
			return null;
		}
	}
}