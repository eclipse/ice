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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

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
		assertTrue(registry.register(fakeExodusProxyFactory));
		assertTrue(registry.register(fakeSiloProxyFactory));

		// Registering the same factory again should still return true, as it
		// becomes the preferred factory for its supported extensions.
		assertTrue(registry.register(fakeExodusProxyFactory));
		assertTrue(registry.register(fakeSiloProxyFactory));

		// Registering a factory with no supported extensions should return
		// false (because nothing was registered!).
		assertFalse(registry.register(fakeEmptyProxyFactory));

		// Registering a null factory should return false (because nothing was
		// registered!).
		assertFalse(registry.register(nullFactory));

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
		assertFalse(registry.unregister(fakeExodusProxyFactory));
		assertFalse(registry.unregister(fakeSiloProxyFactory));
		assertFalse(registry.unregister(fakeEmptyProxyFactory));
		assertFalse(registry.unregister(nullFactory));

		// Register the factories. Note that really only the Exodus and Silo
		// factories will be registered.
		registry.register(fakeExodusProxyFactory);
		registry.register(fakeSiloProxyFactory);
		registry.register(fakeEmptyProxyFactory);
		registry.register(nullFactory);

		// We should now be able to unregister the Exodus and Silo factories.
		// Those requests will return true. The other two reqquests will return
		// false because nothing was unregistered.
		assertTrue(registry.unregister(fakeExodusProxyFactory));
		assertTrue(registry.unregister(fakeSiloProxyFactory));
		assertFalse(registry.unregister(fakeEmptyProxyFactory));
		assertFalse(registry.unregister(nullFactory));

		// Make sure everything was really unregistered. If so, then nothing
		// will be unregistered, and all calls will return false.
		assertFalse(registry.unregister(fakeExodusProxyFactory));
		assertFalse(registry.unregister(fakeSiloProxyFactory));
		assertFalse(registry.unregister(fakeEmptyProxyFactory));
		assertFalse(registry.unregister(nullFactory));

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
			uri = createTestURI(extension);
			assertNull(registry.getFactory(uri));
		}
		// Check this for all Silo extensions.
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			assertNull(registry.getFactory(uri));
		}
		// Check this for invalid URIs.
		assertNull(registry.getFactory(createTestURI("bad")));
		assertNull(registry.getFactory(nullURI));

		// Register the main fake Exodus and the single Silo factories.
		registry.register(fakeExodusProxyFactory);
		registry.register(fakeSiloProxyFactory);

		// Check that all of their extensions return the correct factory.
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			assertSame(fakeExodusProxyFactory, registry.getFactory(uri));
		}
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			assertSame(fakeSiloProxyFactory, registry.getFactory(uri));
		}

		// Register the second fake Exodus factory. It should now be returned
		// when the extension is "e", but all other Exodus extensions should
		// still be the first factory.
		registry.register(fakeExodusProxyFactory2);
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			if (!"e".equals(extension)) {
				assertSame(fakeExodusProxyFactory, registry.getFactory(uri));
			} else {
				assertSame(fakeExodusProxyFactory2, registry.getFactory(uri));
			}
		}

		// Now re-register the main fake Exodus factory. It should now take
		// precedence over the "e" extension.
		registry.register(fakeExodusProxyFactory);
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			assertSame(fakeExodusProxyFactory, registry.getFactory(uri));
		}

		// Unregister the Silo factory. All factory requests for Silo extensions
		// should return null.
		registry.unregister(fakeSiloProxyFactory);
		for (String extension : fakeSiloProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			assertNull(registry.getFactory(uri));
		}

		// Now unregister the main fake Exodus factory. Only the "e" extension
		// will still be supported by the second fake Exodus factory.
		registry.unregister(fakeExodusProxyFactory);
		for (String extension : fakeExodusProxyFactory.getExtensions()) {
			uri = createTestURI(extension);
			if (!"e".equals(extension)) {
				assertNull(registry.getFactory(uri));
			} else {
				assertSame(fakeExodusProxyFactory2, registry.getFactory(uri));
			}
		}

		// Now unregister the second fake Exodus factory. Requesting a factory
		// for the extension "e" should return null.
		registry.unregister(fakeExodusProxyFactory2);
		assertNull(registry.getFactory(createTestURI("e")));

		return;
	}

	/**
	 * Creates a simple URI for the provided extension.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. Assumed to not be {@code null}.
	 * @return A correctly formed URI with the provided extension.
	 */
	private URI createTestURI(String extension) {
		return new File("kung_fury." + extension).toURI();
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