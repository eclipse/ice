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
package org.eclipse.ice.viz.service.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.paraview.ParaViewVizService;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactoryRegistry;
import org.eclipse.ice.viz.service.paraview.proxy.ParaViewProxyFactoryRegistry;
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

	private ParaViewVizService vizService;

	@Before
	public void beforeEachTest() {
		vizService = new ParaViewVizService();
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
	@Ignore
	@Test
	public void checkPlot() {
		fail("Not implemented.");
	}

	/**
	 * Checks that the {@link IParaViewProxyFactoryRegistry} can be set and
	 * unset, and that the supported extensions are based on the set registry.
	 */
	@Test
	public void checkProxyFactoryRegistry() {

		// Create the set of supported extensions.
		final Set<String> supportedExtensions = new HashSet<String>();
		supportedExtensions.add("one");
		supportedExtensions.add("two");

		// Create a fake registry that has a few supported extensions.
		IParaViewProxyFactoryRegistry registry = new IParaViewProxyFactoryRegistry() {
			@Override
			public boolean unregisterProxyFactory(IParaViewProxyFactory factory) {
				return false;
			}

			@Override
			public boolean registerProxyFactory(IParaViewProxyFactory factory) {
				return false;
			}

			@Override
			public IParaViewProxyFactory getProxyFactory(URI uri) {
				return null;
			}

			@Override
			public Set<String> getExtensions() {
				return supportedExtensions;
			}
		};

		// Test all extensions. Currently, none of them are supported, as the
		// registry has not been set.
		for (String extension : supportedExtensions) {
			try {
				vizService.createPlot(createTestURI(extension));
				fail("ParaViewVizServiceTester error: "
						+ "No exception thrown for unsupported extension.");
			} catch (IllegalArgumentException e) {
				// Exception thrown as expected.
			} catch (Exception e) {
				fail("ParaViewVizServiceTester error: "
						+ "Wrong exception type thrown for unsupported extension.");
			}
		}

		// Set the proxy factory registry for the viz service.
		vizService.setProxyFactoryRegistry(registry);
		// Test all (now supported) extensions.
		for (String extension : supportedExtensions) {
			try {
				vizService.createPlot(createTestURI(extension));
			} catch (IllegalArgumentException e) {
				fail("ParaViewVizServiceTester error: "
						+ "Exception thrown for supported extension.");
			} catch (Exception e) {
				// It's okay as long as the exception is not about the supported
				// extension.
				// TODO There may be a way to get around this...
			}
		}

		// Unset the registry from the viz service.
		vizService.unsetProxyFactoryRegistry(registry);
		// Test all extensions. Currently, none of them are supported.
		for (String extension : supportedExtensions) {
			try {
				vizService.createPlot(createTestURI(extension));
				fail("ParaViewVizServiceTester error: "
						+ "No exception thrown for unsupported extension.");
			} catch (IllegalArgumentException e) {
				// Exception thrown as expected.
			} catch (Exception e) {
				fail("ParaViewVizServiceTester error: "
						+ "Wrong exception type thrown for unsupported extension.");
			}
		}

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
}
