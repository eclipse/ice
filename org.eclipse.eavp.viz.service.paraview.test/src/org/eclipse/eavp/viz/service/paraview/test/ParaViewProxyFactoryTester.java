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
package org.eclipse.eavp.viz.service.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.eavp.viz.service.paraview.proxy.ParaViewProxyFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link ParaViewProxyFactory}'s implementation of
 * {@link IParaViewProxyFactory}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewProxyFactoryTester {

	/**
	 * The factory for testing purposes. This will be re-created before each
	 * test.
	 */
	private IParaViewProxyFactory factory;

	/**
	 * A builder that contains no supported extensions.
	 */
	private IParaViewProxyBuilder fakeEmptyProxyBuilder;

	/**
	 * A fake proxy builder that supports the common extensions for the Exodus
	 * file format.
	 */
	private FakeProxyBuilder fakeExodusProxyBuilder;

	/**
	 * A fake proxy builder that supports the common extensions for the Silo
	 * file format.
	 */
	private FakeProxyBuilder fakeSiloProxyBuilder;

	/**
	 * Instantiates the {@link #factory} and the fake proxy builders.
	 */
	@Before
	public void beforeEachTest() {
		// Create a new factory.
		factory = new ParaViewProxyFactory();

		// Create the fake proxy builders.
		fakeEmptyProxyBuilder = new FakeProxyBuilder();
		fakeExodusProxyBuilder = new FakeProxyBuilder() {
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
		fakeSiloProxyBuilder = new FakeProxyBuilder() {
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
	 * Checks the return values for register depending on whether the builder
	 * was registered previously or not and for invalid builders.
	 */
	@Test
	public void checkRegister() {

		final IParaViewProxyBuilder nullBuilder = null;

		// Registering new builders with supported extensions should return
		// true.
		assertTrue(factory.registerProxyBuilder(fakeExodusProxyBuilder));
		assertTrue(factory.registerProxyBuilder(fakeSiloProxyBuilder));

		// Registering the same builder again should still return true, as it
		// becomes the preferred builder for its supported extensions.
		assertTrue(factory.registerProxyBuilder(fakeExodusProxyBuilder));
		assertTrue(factory.registerProxyBuilder(fakeSiloProxyBuilder));

		// Registering a builder with no supported extensions should return
		// false (because nothing was registered!).
		assertFalse(factory.registerProxyBuilder(fakeEmptyProxyBuilder));

		// Registering a null builder should return false (because nothing was
		// registered!).
		assertFalse(factory.registerProxyBuilder(nullBuilder));

		return;
	}

	/**
	 * Checks the return values for unregister depending on whether the builder
	 * was registered previously or not and for invalid builders.
	 */
	@Test
	public void checkUnregister() {

		final IParaViewProxyBuilder nullBuilder = null;

		// Unregistering a builder that's not registered should return false.
		assertFalse(factory.unregisterProxyBuilder(fakeExodusProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(fakeSiloProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(fakeEmptyProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(nullBuilder));

		// Register the builders. Note that really only the Exodus and Silo
		// builders will be registered.
		factory.registerProxyBuilder(fakeExodusProxyBuilder);
		factory.registerProxyBuilder(fakeSiloProxyBuilder);
		factory.registerProxyBuilder(fakeEmptyProxyBuilder);
		factory.registerProxyBuilder(nullBuilder);

		// We should now be able to unregister the Exodus and Silo builders.
		// Those requests will return true. The other two reqquests will return
		// false because nothing was unregistered.
		assertTrue(factory.unregisterProxyBuilder(fakeExodusProxyBuilder));
		assertTrue(factory.unregisterProxyBuilder(fakeSiloProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(fakeEmptyProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(nullBuilder));

		// Make sure everything was really unregistered. If so, then nothing
		// will be unregistered, and all calls will return false.
		assertFalse(factory.unregisterProxyBuilder(fakeExodusProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(fakeSiloProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(fakeEmptyProxyBuilder));
		assertFalse(factory.unregisterProxyBuilder(nullBuilder));

		return;
	}

	/**
	 * Checks that the correct builder is queried to create a proxy based on the
	 * specified URI.
	 */
	@Test
	public void checkCreateProxy() {
		URI uri;
		final URI nullURI = null;

		// Create another fake Exodus proxy builder that registers only for the
		// "e" extension.
		final FakeProxyBuilder fakeExodusProxyBuilder2 = new FakeProxyBuilder() {
			@Override
			public Set<String> getExtensions() {
				Set<String> extensions = super.getExtensions();
				extensions.add("e");
				return extensions;
			}
		};

		// Initial calls should return null regardless of the URI, because there
		// are no registered builders.

		// Check this for all Exodus extensions.
		for (String extension : fakeExodusProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			try {
				factory.createProxy(uri);
				fail("ParaViewProxyFactory error: "
						+ "IllegalArgumentException not thrown for unsupported extension.");
			} catch (IllegalArgumentException e) {
				// Exception thrown as expected.
			}
		}
		// Check this for all Silo extensions.
		for (String extension : fakeSiloProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			try {
				factory.createProxy(uri);
				fail("ParaViewProxyFactory error: "
						+ "IllegalArgumentException not thrown for unsupported extension.");
			} catch (IllegalArgumentException e) {
				// Exception thrown as expected.
			}
		}

		// Check that a null URI throws an NPE.
		try {
			factory.createProxy(nullURI);
			fail("ParaViewProxyFactory error: "
					+ "NullPointerException not thrown for null URI.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		// Check another bad URI.
		try {
			factory.createProxy(TestUtils.createURI("bad"));
			fail("ParaViewProxyFactory error: "
					+ "IllegalArgumentException not thrown for unsupported extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		// Register the main fake Exodus and the single Silo builders.
		factory.registerProxyBuilder(fakeExodusProxyBuilder);
		factory.registerProxyBuilder(fakeSiloProxyBuilder);

		// Check that all of their extensions return the correct builder,
		// and that the query is case insensitive.
		for (String extension : fakeExodusProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension.toLowerCase());
			factory.createProxy(uri);
			assertTrue(fakeExodusProxyBuilder.createdProxy.getAndSet(false));
			uri = TestUtils.createURI(extension.toUpperCase());
			factory.createProxy(uri);
			assertTrue(fakeExodusProxyBuilder.createdProxy.getAndSet(false));
		}
		for (String extension : fakeSiloProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension.toLowerCase());
			factory.createProxy(uri);
			assertTrue(fakeSiloProxyBuilder.createdProxy.getAndSet(false));
			uri = TestUtils.createURI(extension.toUpperCase());
			factory.createProxy(uri);
			assertTrue(fakeSiloProxyBuilder.createdProxy.getAndSet(false));
		}

		// Register the second fake Exodus builder. It should now be returned
		// when the extension is "e", but all other Exodus extensions should
		// still be the first builder.
		factory.registerProxyBuilder(fakeExodusProxyBuilder2);
		for (String extension : fakeExodusProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			factory.createProxy(uri);
			if (!"e".equals(extension)) {
				assertTrue(fakeExodusProxyBuilder.createdProxy.getAndSet(false));
			} else {
				assertTrue(fakeExodusProxyBuilder2.createdProxy
						.getAndSet(false));
			}
		}

		// Now re-register the main fake Exodus builder. It should now take
		// precedence over the "e" extension.
		factory.registerProxyBuilder(fakeExodusProxyBuilder);
		for (String extension : fakeExodusProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			factory.createProxy(uri);
			assertTrue(fakeExodusProxyBuilder.createdProxy.getAndSet(false));
		}

		// Unregister the Silo builder. All builder requests for Silo extensions
		// should return null.
		factory.unregisterProxyBuilder(fakeSiloProxyBuilder);
		for (String extension : fakeSiloProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			try {
				factory.createProxy(uri);
				fail("ParaViewProxyFactory error: "
						+ "IllegalArgumentException not thrown for unsupported extension.");
			} catch (IllegalArgumentException e) {
				// Exception thrown as expected.
			}
		}

		// Now unregister the main fake Exodus builder. Only the "e" extension
		// will still be supported by the second fake Exodus builder.
		factory.unregisterProxyBuilder(fakeExodusProxyBuilder);
		for (String extension : fakeExodusProxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			if (!"e".equals(extension)) {
				try {
					factory.createProxy(uri);
					fail("ParaViewProxyFactory error: "
							+ "IllegalArgumentException not thrown for unsupported extension.");
				} catch (IllegalArgumentException e) {
					// Exception thrown as expected.
				}
			} else {
				factory.createProxy(uri);
				assertTrue(fakeExodusProxyBuilder2.createdProxy
						.getAndSet(false));
			}
		}

		// Now unregister the second fake Exodus builder. Requesting a builder
		// for the extension "e" should return null.
		factory.unregisterProxyBuilder(fakeExodusProxyBuilder2);
		try {
			factory.createProxy(TestUtils.createURI("e"));
			fail("ParaViewProxyFactory error: "
					+ "IllegalArgumentException not thrown for unsupported extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that all extensions for all registered builders can be queried
	 * from the factory.
	 */
	@Test
	public void checkExtensions() {

		// Create another fake Exodus proxy builder that registers only for the
		// "e" extension.
		final IParaViewProxyBuilder fakeExodusProxyBuilder2 = new FakeProxyBuilder() {
			@Override
			public Set<String> getExtensions() {
				Set<String> extensions = super.getExtensions();
				extensions.add("e");
				return extensions;
			}
		};

		// Register the proxy builders.
		factory.registerProxyBuilder(fakeExodusProxyBuilder);
		factory.registerProxyBuilder(fakeSiloProxyBuilder);
		factory.registerProxyBuilder(fakeExodusProxyBuilder2);

		// The set of returned extensions should contain all Exodus and Silo
		// extensions, regardless of the existence of shared extensions.
		Set<String> expectedExtensions = new TreeSet<String>();
		for (String extension : fakeExodusProxyBuilder.getExtensions()) {
			expectedExtensions.add(extension);
		}
		for (String extension : fakeSiloProxyBuilder.getExtensions()) {
			expectedExtensions.add(extension);
		}

		// The returned set should be ordered (usually via a TreeSet).
		Set<String> supportedExtensions = factory.getExtensions();
		assertEquals(expectedExtensions, supportedExtensions);

		// The returned set should be a new, equivalent set each time.
		assertNotSame(supportedExtensions, factory.getExtensions());
		assertEquals(supportedExtensions, factory.getExtensions());

		// Modifying the returned set should make no difference.
		factory.getExtensions().clear();
		assertEquals(expectedExtensions, factory.getExtensions());

		return;
	}

	/**
	 * A fake implementation of {@link IParaViewProxyBuilder} simply for testing
	 * purposes.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeProxyBuilder implements IParaViewProxyBuilder {

		/**
		 * If true, then {@link #createProxy(URI)} was called, false otherwise.
		 */
		public final AtomicBoolean createdProxy = new AtomicBoolean();

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
				throws NullPointerException, IllegalArgumentException {
			createdProxy.set(true);
			return null;
		}
	}
}
