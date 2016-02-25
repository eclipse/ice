/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.eavp.viz.service.paraview.ParaViewVizService;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing the {@link ParaViewVizService}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewVizServiceTester {

	// TODO Implement these tests.

	/**
	 * The viz service under test. Usually, this is the same as
	 * {@link #fakeVizService}, but, of course, using this reference does not
	 * expose protected methods.
	 */
	private ParaViewVizService vizService;

	/**
	 * The viz service usually under test. This is for convenience.
	 */
	private FakeParaViewVizService fakeVizService;

	/**
	 * Initializes {@link #vizService} and {@link #fakeVizService}.
	 */
	@Before
	public void beforeEachTest() {
		fakeVizService = new FakeParaViewVizService();
		vizService = fakeVizService;
	}

	/**
	 * This test checks the name of the visualization service.
	 * 
	 * @see ParaViewVizService#getName()
	 */
	@Test
	public void checkName() {
		// The name should always be the same. Just try getting it a few times.
		assertEquals("ParaView", vizService.getName());
	}

	/**
	 * This test checks the version information for the service.
	 * 
	 * @see ParaViewVizService#getVersion()
	 */
	@Test
	public void checkVersion() {
		// TODO Update this test. For now, the version should always be the
		// same. However, it may be that we can connect to multiple versions at
		// run-time!
		assertEquals("", vizService.getVersion());
	}

	/**
	 * This test checks the service's connection properties, including their
	 * default values.
	 * 
	 * @see ParaViewVizService#hasConnectionProperties()
	 * @see ParaViewVizService#getConnectionProperties()
	 * @see ParaViewVizService#setConnectionProperties(java.util.Map)
	 */
	@Ignore
	@Test
	public void checkConnectionProperties() {
		fail("Not implemented.");
	}

	/**
	 * This test checks that the service connects properly.
	 * 
	 * @see ParaViewVizService#connect()
	 */
	@Ignore
	@Test
	public void checkConnect() {
		fail("Not implemented.");
	}

	/**
	 * This test checks the plots created by the service.
	 * 
	 * @see ParaViewVizService#createPlot(java.net.URI)
	 */
	@Test
	public void checkPlot() {

		final URI nullURI = null;

		// Passing in a null URI should throw an IllegalArgumentException.
		try {
			vizService.createPlot(nullURI);
			fail("ParaViewVizServiceTester error: "
					+ "No exception thrown for null URI.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		} catch (Exception e) {
			fail("ParaViewVizServiceTester error: "
					+ "Wrong exception type thrown for null URI.");
		}

		// Passing in an unsupported URI should throw an
		// IllegalArgumentException.
		try {
			vizService
					.createPlot(TestUtils.createURI("this-is-a-bad-extension"));
			fail("ParaViewVizServiceTester error: "
					+ "No exception thrown for unsupported extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		} catch (Exception e) {
			fail("ParaViewVizServiceTester error: "
					+ "Wrong exception type thrown for unsupported extension.");
		}

		// TODO Test successful plot creation.

		return;
	}

	/**
	 * Checks that the {@link IParaViewProxyFactory} can be set and unset, and
	 * that the supported extensions are based on the set factory.
	 */
	@Test
	public void checkProxyFactory() {

		// Create the set of supported extensions.
		final Set<String> supportedExtensions = new HashSet<String>();
		supportedExtensions.add("one");
		supportedExtensions.add("two");

		// Create a fake factory that has a few supported extensions.
		IParaViewProxyFactory factory = new IParaViewProxyFactory() {
			@Override
			public boolean unregisterProxyBuilder(
					IParaViewProxyBuilder builder) {
				return false;
			}

			@Override
			public boolean registerProxyBuilder(IParaViewProxyBuilder builder) {
				return false;
			}

			@Override
			public IParaViewProxy createProxy(URI uri)
					throws NullPointerException, IllegalArgumentException {
				return null;
			}

			@Override
			public Set<String> getExtensions() {
				return supportedExtensions;
			}
		};

		// Initially, the factory is null.
		assertNull(fakeVizService.getProxyFactory());
		// Test all extensions. Currently, none of them are supported, as the
		// factory has not been set.
		for (String extension : supportedExtensions) {
			assertFalse(fakeVizService.findSupportedExtensions()
					.contains(extension));
		}

		// Set the proxy factory for the viz service.
		fakeVizService.setProxyFactory(factory);
		// Now the factory should not be null.
		assertSame(factory, fakeVizService.getProxyFactory());
		// Test all (now supported) extensions.
		for (String extension : supportedExtensions) {
			assertTrue(fakeVizService.findSupportedExtensions()
					.contains(extension));
		}

		// Unset the factory from the viz service.
		fakeVizService.unsetProxyFactory(factory);
		// Again, the factory is null.
		assertNull(fakeVizService.getProxyFactory());
		// Test all extensions. Currently, none of them are supported.
		for (String extension : supportedExtensions) {
			assertFalse(fakeVizService.findSupportedExtensions()
					.contains(extension));
		}

		return;
	}

	/**
	 * A sub-class of {@link ParaViewVizService} used only for testing purposes,
	 * primarily to expose protected methods that may be used by other classes
	 * in the package but are not intended for "public" consumption.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeParaViewVizService extends ParaViewVizService {

		/*
		 * Exposes the super class' method.
		 */
		@Override
		protected void setProxyFactory(IParaViewProxyFactory factory) {
			super.setProxyFactory(factory);
		}

		/*
		 * Exposes the super class' method.
		 */
		@Override
		protected void unsetProxyFactory(IParaViewProxyFactory factory) {
			super.unsetProxyFactory(factory);
		}

		/*
		 * Exposes the super class' method.
		 */
		@Override
		protected IParaViewProxyFactory getProxyFactory() {
			return super.getProxyFactory();
		}

		/*
		 * Exposes the super class' method.
		 */
		@Override
		protected Set<String> findSupportedExtensions() {
			return super.findSupportedExtensions();
		}
	}
}
