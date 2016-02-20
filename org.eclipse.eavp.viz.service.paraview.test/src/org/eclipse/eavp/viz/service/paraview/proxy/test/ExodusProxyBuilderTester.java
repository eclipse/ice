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
package org.eclipse.eavp.viz.service.paraview.proxy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder;
import org.eclipse.eavp.viz.service.paraview.proxy.exodus.ExodusProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.exodus.ExodusProxyBuilder;
import org.eclipse.eavp.viz.service.paraview.test.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link ExodusProxyBuilder}'s implementation of
 * {@link IParaViewProxyBuilder}.
 * 
 * @author Jordan Deyton
 *
 */
public class ExodusProxyBuilderTester {

	/**
	 * The proxy builder that will be tested. This is re-initialized before each
	 * test.
	 */
	private ExodusProxyBuilder proxyBuilder;

	/**
	 * Sets up the {@link #proxyBuilder} before each test.
	 */
	@Before
	public void beforeEachTest() {
		proxyBuilder = new ExodusProxyBuilder();
	}

	/**
	 * Unsets the {@link #proxyBuilder} after each test.
	 */
	@After
	public void afterEachTest() {
		proxyBuilder = null;
	}

	/**
	 * Checks that the user/developer friendly name provided by the builder is
	 * correct.
	 */
	@Test
	public void checkName() {
		assertEquals("Default Exodus Proxy Builder", proxyBuilder.getName());
	}

	/**
	 * Checks that the proxy builder supports the correct Exodus file
	 * extensions.
	 */
	@Test
	public void checkExtensions() {
		// Set up an array containing all supported extensions.
		String[] extensions = new String[] { "e", "ex", "exo", "ex2", "exii",
				"gen", "exodus", "nemesis" };

		// Get the set of supported extensions from the proxy builder.
		Set<String> supportedExtensions = proxyBuilder.getExtensions();

		// Check the contents of the set of supported extensions. It should
		// match in size and have every value in the array.
		assertEquals(extensions.length, supportedExtensions.size());
		for (String extension : extensions) {
			assertTrue(supportedExtensions.contains(extension));
		}

		// Check that a new, equivalent set is returned from each request.
		assertNotSame(supportedExtensions, proxyBuilder.getExtensions());
		assertEquals(supportedExtensions, proxyBuilder.getExtensions());

		// Check that modifying the returned set does not affect the extensions.
		proxyBuilder.getExtensions().clear();
		supportedExtensions = proxyBuilder.getExtensions();
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

		// Re-create the proxy builder to set the reference to the created proxy
		// based on the createProxyImpl(URI) method inherited from
		// AbstractParaViewProxyBuilder.
		proxyBuilder = new ExodusProxyBuilder() {
			@Override
			protected IParaViewProxy createConcreteProxy(URI uri) {
				IParaViewProxy proxy = super.createConcreteProxy(uri);
				createdProxy.set(proxy);
				return proxy;
			}
		};

		// Passing a null URI should throw an exception.
		try {
			proxyBuilder.createProxy(nullURI);
			fail("ExodusProxyBuilderTester failure: "
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
			proxyBuilder.createProxy(uri);
			fail("ExodusProxyBuilderTester failure: "
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
			proxyBuilder.createProxy(uri);
			fail("ExodusProxyBuilderTester failure: "
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
		for (String extension : proxyBuilder.getExtensions()) {
			uri = TestUtils.createURI(extension);
			proxy = proxyBuilder.createProxy(uri);
			assertSame(createdProxy.getAndSet(null), proxy);
			// Check the type of the proxy.
			assertTrue(proxy instanceof ExodusProxy);
			// Check the proxy's URI. It should be the same.
			assertEquals(uri, proxy.getURI());
		}

		return;
	}
}
