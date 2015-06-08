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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.junit.Test;

/**
 * This class tests the basic features provided by the
 * {@link AbstractParaViewProxyFactory}.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractParaViewProxyFactoryTester {

	/**
	 * The factory that will be tested in each test.
	 */
	private AbstractParaViewProxyFactory proxyFactory;

	/**
	 * Checks that the default set of extensions is empty and that the returned
	 * set of extensions is ordered alphabetically and contains no duplicates.
	 */
	@Test
	public void checkExtensions() {

		Set<String> supportedExtensions;
		String[] extensions = new String[] { "one", "two", "three" };

		// The default AbstractParaViewProxyFactory should provide no
		// extensions.
		proxyFactory = new FakeParaViewProxyFactory();
		supportedExtensions = proxyFactory.getExtensions();
		assertNotNull(supportedExtensions);
		assertTrue(supportedExtensions.isEmpty());

		// Adding to the protected set of extensions should cause the factory to
		// return a lexicographically ordered set of extensions with no
		// duplicates.
		proxyFactory = new FakeParaViewProxyFactory("one", "two", "one",
				"three");
		supportedExtensions = proxyFactory.getExtensions();
		assertNotNull(supportedExtensions);
		assertEquals(3, supportedExtensions.size());
		// Check the order of the extensions. Should be alphabetical.
		Iterator<String> iter = supportedExtensions.iterator();
		assertEquals("one", iter.next());
		assertEquals("three", iter.next());
		assertEquals("two", iter.next());

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
	 * Checks that the URI is properly checked for consistency against the
	 * supported extensions and that, if valid, the proxy creation
	 * implementation method is called.
	 */
	@Test
	public void checkProxy() {

		FakeParaViewProxyFactory fakeProxyFactory;
		IParaViewProxy proxy;
		URI uri;
		final URI nullURI = null;

		// The default AbstractParaViewProxyFactory should not be able to
		// validate any URI.
		fakeProxyFactory = new FakeParaViewProxyFactory();
		proxyFactory = fakeProxyFactory;

		// Passing a null URI should throw an exception.
		try {
			proxyFactory.createProxy(nullURI);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "NullPointerException not thrown when URI is null.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyFactory.createdProxy.get());

		// Passing in any URI should throw an exception because the extension is
		// not supported.
		uri = createTestURI("one");
		try {
			proxyFactory.createProxy(uri);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "IllegalArgumentException not thrown when URI extension "
					+ "is not supported.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyFactory.createdProxy.get());

		// Now try an AbstractParaViewProxyFactory with some extensions.
		fakeProxyFactory = new FakeParaViewProxyFactory("one", "two", "one",
				null, "three");
		proxyFactory = fakeProxyFactory;

		// Passing a null URI should throw an exception.
		try {
			proxyFactory.createProxy(nullURI);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "NullPointerException not thrown when URI is null.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyFactory.createdProxy.get());

		// Passing in an invalid URI should throw an exception because the
		// extension is not supported.
		uri = createTestURI("fail");
		try {
			proxyFactory.createProxy(uri);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "IllegalArgumentException not thrown when URI extension "
					+ "is not supported.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyFactory.createdProxy.get());

		// Passing in a valid URI should call the implementation and should
		// return the implementation's IParaViewProxy. The file's existence or
		// validity is not checked.
		proxy = proxyFactory.createProxy(createTestURI("one"));
		assertSame(fakeProxyFactory.createdProxy.getAndSet(null), proxy);
		proxy = proxyFactory.createProxy(createTestURI("two"));
		assertSame(fakeProxyFactory.createdProxy.getAndSet(null), proxy);
		proxy = proxyFactory.createProxy(createTestURI("three"));
		assertSame(fakeProxyFactory.createdProxy.getAndSet(null), proxy);

		// Files without extensions should not be supported, even though "null"
		// is a "supported" extension.
		uri = createTestURI(null);
		try {
			proxyFactory.createProxy(uri);
			fail("AbstractParaViewProxyFactoryTester failure: "
					+ "IllegalArgumentException not thrown when URI has no "
					+ "extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyFactory.createdProxy.get());

		return;
	}

	/**
	 * Creates a simple URI for the provided extension.
	 * 
	 * @param extension
	 *            The extension for the test URI file. This file probably will
	 *            not actually exist. If {@code null}, then the file will have
	 *            no extension.
	 * @return A correctly formed URI with the provided extension.
	 */
	private URI createTestURI(String extension) {
		String filename = (extension != null ? "kung_fury." + extension
				: "future_cop");
		return new File(filename).toURI();
	}

	/**
	 * A fake {@link AbstractParaViewProxyFactory} used to test the set of
	 * supported extensions and the default
	 * {@link AbstractParaViewProxyFactory#createProxy(URI)} implementation,
	 * which should re-direct to the sub-class' implementation.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeParaViewProxyFactory extends AbstractParaViewProxyFactory {

		/**
		 * The proxy that was created. If set to a non-null value, then
		 * {@link #createProxyImpl(URI)} was, in fact, called.
		 */
		public final AtomicReference<IParaViewProxy> createdProxy;

		/**
		 * The default constructor. Adds all specified extensions to the set of
		 * supported extensions.
		 * 
		 * @param extensions
		 *            The supported extensions. May be any strings.
		 */
		public FakeParaViewProxyFactory(String... extensions) {
			// Add all extensions.
			for (String extension : extensions) {
				super.extensions.add(extension);
			}
			// Initialize the reference to the created proxy.
			createdProxy = new AtomicReference<IParaViewProxy>();
		}

		/**
		 * Returns a dummy name.
		 */
		@Override
		public String getName() {
			return "Fake ParaView Proxy Factory";
		}

		/**
		 * Sets {@link #proxyCreated} to true when called. Returns {@code null}.
		 */
		@Override
		protected IParaViewProxy createProxyImpl(URI uri) {
			// Create a proxy. What's in it doesn't matter for these tests.
			IParaViewProxy proxy = new AbstractParaViewProxy(uri) {
			};
			createdProxy.set(proxy);
			return proxy;
		}
	}
}