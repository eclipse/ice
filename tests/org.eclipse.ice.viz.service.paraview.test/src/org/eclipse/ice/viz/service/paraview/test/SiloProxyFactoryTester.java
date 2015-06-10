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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.silo.SiloProxy;
import org.eclipse.ice.viz.service.paraview.proxy.silo.SiloProxyFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link SiloProxyFactory}'s implementation of
 * {@link IParaViewProxyFactory}.
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxyFactoryTester {

	/**
	 * The proxy factory that will be tested. This is re-initialized before each
	 * test.
	 */
	private SiloProxyFactory proxyFactory;

	/**
	 * Sets up the {@link #proxyFactory} before each test.
	 */
	@Before
	public void beforeEachTest() {
		proxyFactory = new SiloProxyFactory();
	}

	/**
	 * Unsets the {@link #proxyFactory} after each test.
	 */
	@After
	public void afterEachTest() {
		proxyFactory = null;
	}

	/**
	 * Checks that the user/developer friendly name provided by the factory is
	 * correct.
	 */
	@Test
	public void checkName() {
		assertEquals("Default Silo Proxy Factory", proxyFactory.getName());
	}

	/**
	 * Checks that the proxy factory supports the correct Silo file extensions.
	 */
	@Test
	public void checkExtensions() {
		// Set up an array containing all supported extensions.
		String[] extensions = new String[] { "silo" };

		// Get the set of supported extensions from the proxy factory.
		Set<String> supportedExtensions = proxyFactory.getExtensions();

		// Check the contents of the set of supported extensions. It should
		// match in size and have every value in the array.
		assertEquals(extensions.length, supportedExtensions.size());
		for (String extension : extensions) {
			assertTrue(supportedExtensions.contains(extension));
		}

		// Check that a new, equivalent set is returned from each request.
		assertNotSame(supportedExtensions, proxyFactory.getExtensions());
		assertEquals(supportedExtensions, proxyFactory.getExtensions());

		// Check that modifying the returned set does not affect the extensions.
		proxyFactory.getExtensions().clear();
		supportedExtensions = proxyFactory.getExtensions();
		assertEquals(extensions.length, supportedExtensions.size());
		for (String extension : extensions) {
			assertTrue(supportedExtensions.contains(extension));
		}

		return;
	}

	/**
	 * Checks that, for valid URIs (not null, extension supported), an
	 * {@link IParaViewProxy} is created and returned, or that exceptions are
	 * thrown or null is returned if the URI is invalid.
	 */
	@Test
	public void checkProxy() {

		IParaViewProxy proxy;
		URI uri;
		final URI nullURI = null;
		final AtomicReference<IParaViewProxy> createdProxy = new AtomicReference<IParaViewProxy>();

		// Re-create the proxy factory to set the reference to the created proxy
		// based on the createProxyImpl(URI) method inherited from
		// AbstractParaViewProxyFactory.
		proxyFactory = new SiloProxyFactory() {
			@Override
			protected IParaViewProxy createConcreteProxy(URI uri) {
				IParaViewProxy proxy = super.createConcreteProxy(uri);
				createdProxy.set(proxy);
				return proxy;
			}
		};

		// Passing a null URI should throw an exception.
		try {
			proxyFactory.createProxy(nullURI);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "NullPointerException not thrown when URI is null.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(createdProxy.get());

		// Passing in an invalid URI should throw an exception because the
		// extension is not supported.
		uri = TestUtils.createURI("fail");
		try {
			proxyFactory.createProxy(uri);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "IllegalArgumentException not thrown when URI extension "
					+ "is not supported.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(createdProxy.get());

		// Files without extensions should not be supported.
		uri = TestUtils.createURI(null);
		try {
			proxyFactory.createProxy(uri);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "IllegalArgumentException not thrown when URI has no "
					+ "extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(createdProxy.get());

		// Passing in a valid URI should call the implementation and should
		// return the implementation's IParaViewProxy. The file's existence or
		// validity is not checked (currently).
		for (String extension : proxyFactory.getExtensions()) {
			uri = TestUtils.createURI(extension);
			proxy = proxyFactory.createProxy(uri);
			assertSame(createdProxy.getAndSet(null), proxy);
			// Check the type of the proxy.
			assertTrue(proxy instanceof SiloProxy);
			// Check the proxy's URI. It should be the same.
			assertEquals(uri, proxy.getURI());
		}

		return;
	}
}
