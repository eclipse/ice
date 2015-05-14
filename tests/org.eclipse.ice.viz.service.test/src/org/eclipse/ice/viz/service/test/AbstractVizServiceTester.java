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
package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.junit.Test;

/**
 * This class tests the {@link AbstractVizService}, which provides some helpful
 * utilities and implementations for IVizService.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractVizServiceTester {

	/**
	 * This method checks that the {@code AbstractVizService}'s extension
	 * checking code works correctly when calling either
	 * {@link AbstractVizService#extensionSupported(URI)} or
	 * {@link AbstractVizService#createPlot(URI)}.
	 */
	@Test
	public void checkExtensions() {

		FakeVizService service = new FakeVizService();

		// When calling extensionSupported(URI), we only need to check whether
		// it returns true (valid URI and extension) or false (invalid URI or
		// extension).

		// We also need to check that createPlot(URI)'s default implementation
		// either returns null (in which case the URI is accepted) or throws an
		// IllegalArgumentException (in which case the URI is invalid or has a
		// bad extension).

		// Add some supported extensions.
		Set<String> extensions = service.getExtensions();
		extensions.add("txt");
		extensions.add("tar");
		extensions.add("whoosh");

		URI uri;

		try {
			// ---- Try some valid URIs. ---- //
			// These should throw no exception.
			uri = new URI("/home/bob/file.txt");
			assertTrue(service.extensionSupported(uri));
			assertNull(service.createPlot(uri));
			uri = new URI("file:/C:Users/user/folder//one.two.three.tar");
			assertTrue(service.extensionSupported(uri));
			assertNull(service.createPlot(uri));
			uri = new URI("1.txt");
			assertTrue(service.extensionSupported(uri));
			assertNull(service.createPlot(uri));

			// ---- Try invalid URIs. ---- //
			// These should throw IllegalArgumentExceptions.

			// Try a null URI (invalid).
			uri = null;
			assertFalse(service.extensionSupported(uri));
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try an opaque URI (invalid).
			uri = new URI("mailto:foo@bar.com");
			assertFalse(service.extensionSupported(uri));
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try an invalid (unsupported type) URI.
			uri = new URI("/home/.bob/file.csv");
			assertFalse(service.extensionSupported(uri));
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try an invalid (unsupported type) URI.
			uri = new URI("file:/C:Users/user/folder/one.two.three");
			assertFalse(service.extensionSupported(uri));
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try a URI that's too short (blank name).
			uri = new URI(".txt");
			assertFalse(service.extensionSupported(uri));
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try a URI that's missing an extension.
			uri = new URI("/tmp/txt");
			assertFalse(service.extensionSupported(uri));
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}
			// --------------------------- //
		} catch (URISyntaxException e) {
			fail("AbstractVizServiceTester error: " + "A test URI was invalid.");
			e.printStackTrace();
		} catch (Exception e) {
			fail("AbstractVizServiceTester error: " + "Unknown exception!");
			e.printStackTrace();
		}

		return;
	}

	/**
	 * Provides a basic, empty implementation for general testing purposes and
	 * exposes the super-class' set of supported extensions.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class FakeVizService extends AbstractVizService {

		/**
		 * Gets the set of supported extensions. This exposes the set so the
		 * tester can add extensions as a normal sub-class of AbstractVizService
		 * 
		 * @return A reference to the {@code AbstractVizService}'s set of
		 *         supported extensions.
		 */
		public Set<String> getExtensions() {
			return supportedExtensions;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public String getVersion() {
			return null;
		}

		@Override
		public boolean hasConnectionProperties() {
			return false;
		}

		@Override
		public Map<String, String> getConnectionProperties() {
			return null;
		}

		@Override
		public void setConnectionProperties(Map<String, String> props) {
		}

		@Override
		public boolean connect() {
			return false;
		}

		@Override
		public boolean disconnect() {
			return false;
		}
	}
}
