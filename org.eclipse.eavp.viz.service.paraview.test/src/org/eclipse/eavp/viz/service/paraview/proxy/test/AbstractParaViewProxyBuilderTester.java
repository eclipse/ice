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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxyBuilder;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.test.TestUtils;
import org.junit.Test;

/**
 * This class tests the basic features provided by the
 * {@link AbstractParaViewProxyBuilder}.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractParaViewProxyBuilderTester {

	/**
	 * The builder that will be tested in each test.
	 */
	private AbstractParaViewProxyBuilder proxyBuilder;

	/**
	 * Checks that the default set of extensions is empty and that the returned
	 * set of extensions is ordered alphabetically and contains no duplicates.
	 */
	@Test
	public void checkExtensions() {

		Set<String> supportedExtensions;
		String[] extensions = new String[] { "one", "two", "three" };

		// The default builder should provide no extensions.
		proxyBuilder = new FakeParaViewProxyBuilder();
		supportedExtensions = proxyBuilder.getExtensions();
		assertNotNull(supportedExtensions);
		assertTrue(supportedExtensions.isEmpty());

		// Adding to the protected set of extensions should cause the builder to
		// return a lexicographically ordered set of extensions with no
		// duplicates.
		proxyBuilder = new FakeParaViewProxyBuilder("one", "two", "one",
				"three");
		supportedExtensions = proxyBuilder.getExtensions();
		assertNotNull(supportedExtensions);
		assertEquals(3, supportedExtensions.size());
		// Check the order of the extensions. Should be alphabetical.
		Iterator<String> iter = supportedExtensions.iterator();
		assertEquals("one", iter.next());
		assertEquals("three", iter.next());
		assertEquals("two", iter.next());

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
	 * Checks that the URI is properly checked for consistency against the
	 * supported extensions and that, if valid, the proxy creation
	 * implementation method is called.
	 */
	@Test
	public void checkProxy() {

		FakeParaViewProxyBuilder fakeProxyBuilder;
		IParaViewProxy proxy;
		URI uri;
		final URI nullURI = null;

		// The default builder should not be able to validate any URI, as it has
		// no supported extensions.
		fakeProxyBuilder = new FakeParaViewProxyBuilder();
		proxyBuilder = fakeProxyBuilder;

		// Passing a null URI should throw an exception.
		try {
			proxyBuilder.createProxy(nullURI);
			fail("AbstractParaViewProxyBuilderTester failure: "
					+ "NullPointerException not thrown when URI is null.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyBuilder.createdProxy.get());

		// Passing in any URI should throw an exception because the extension is
		// not supported.
		uri = TestUtils.createURI("one");
		try {
			proxyBuilder.createProxy(uri);
			fail("AbstractParaViewProxyBuilderTester failure: "
					+ "IllegalArgumentException not thrown when URI extension "
					+ "is not supported.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyBuilder.createdProxy.get());

		// Now try a builder with some extensions.
		fakeProxyBuilder = new FakeParaViewProxyBuilder("one", "two", "one",
				"three");
		proxyBuilder = fakeProxyBuilder;

		// Passing a null URI should throw an exception.
		try {
			proxyBuilder.createProxy(nullURI);
			fail("AbstractParaViewProxyBuilderTester failure: "
					+ "NullPointerException not thrown when URI is null.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyBuilder.createdProxy.get());

		// Passing in an invalid URI should throw an exception because the
		// extension is not supported.
		uri = TestUtils.createURI("fail");
		try {
			proxyBuilder.createProxy(uri);
			fail("AbstractParaViewProxyBuilderTester failure: "
					+ "IllegalArgumentException not thrown when URI extension "
					+ "is not supported.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyBuilder.createdProxy.get());

		// Passing in a valid URI should call the implementation and should
		// return the implementation's IParaViewProxy. The file's existence or
		// validity is not checked.
		uri = TestUtils.createURI("one");
		proxy = proxyBuilder.createProxy(uri);
		assertSame(fakeProxyBuilder.createdProxy.getAndSet(null), proxy);
		uri = TestUtils.createURI("two", "localhost");
		proxy = proxyBuilder.createProxy(uri);
		assertSame(fakeProxyBuilder.createdProxy.getAndSet(null), proxy);
		uri = TestUtils.createURI("three", "foo.bar.com");
		proxy = proxyBuilder.createProxy(uri);
		assertSame(fakeProxyBuilder.createdProxy.getAndSet(null), proxy);

		// Files without extensions should not be supported, even though "null"
		// is a "supported" extension.
		uri = TestUtils.createURI(null);
		try {
			proxyBuilder.createProxy(uri);
			fail("AbstractParaViewProxyBuilderTester failure: "
					+ "IllegalArgumentException not thrown when URI has no "
					+ "extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		}
		// A proxy should not have been created.
		assertNull(fakeProxyBuilder.createdProxy.get());

		return;
	}

	/**
	 * A fake {@link AbstractParaViewProxyBuilder} used to test the set of
	 * supported extensions and the default
	 * {@link AbstractParaViewProxyBuilder#createProxy(URI)} implementation,
	 * which should re-direct to the sub-class' implementation.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeParaViewProxyBuilder
			extends AbstractParaViewProxyBuilder {

		/**
		 * The proxy that was created. If set to a non-null value, then
		 * {@link #createConcreteProxy(URI)} was, in fact, called.
		 */
		public final AtomicReference<IParaViewProxy> createdProxy;

		/**
		 * A collection of supported extensions provided to the constructor.
		 */
		private final List<String> extensions;

		/**
		 * The default constructor. Adds all specified extensions to the set of
		 * supported extensions.
		 * 
		 * @param extensions
		 *            The supported extensions. May be any strings.
		 */
		public FakeParaViewProxyBuilder(String... extensions) {
			createdProxy = new AtomicReference<IParaViewProxy>();
			this.extensions = new ArrayList<String>();
			for (String extension : extensions) {
				this.extensions.add(extension);
			}
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxyBuilder#createConcreteProxy(java.net.URI)
		 */
		@Override
		protected IParaViewProxy createConcreteProxy(URI uri) {
			// Create a proxy. What's in it doesn't matter for these tests.
			IParaViewProxy proxy = new AbstractParaViewProxy(uri) {
				// Nothing to add.
			};
			createdProxy.set(proxy);
			return proxy;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxyBuilder#findExtensions()
		 */
		@Override
		protected Set<String> findExtensions() {
			return new HashSet<String>(extensions);
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder#getName()
		 */
		@Override
		public String getName() {
			return "Fake ParaView Proxy Builder";
		}
	}
}
