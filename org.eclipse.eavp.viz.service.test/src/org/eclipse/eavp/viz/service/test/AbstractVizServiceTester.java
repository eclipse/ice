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
package org.eclipse.eavp.viz.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.eavp.viz.service.AbstractVizService;
import org.eclipse.eavp.viz.service.modeling.IControllerFactory;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
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
	 * {@link AbstractVizService#createPlot(URI)}.
	 */
	@Test
	public void checkExtensions() {

		FakeVizService service = new FakeVizService();

		/*- We must check two things:
		 * 1 - The supported extension set from the IVizService method returns
		 *     a copy of the sub-class' "found" extensions.
		 * 2 - That attempting to create a plot with valid URIs should return
		 *     null, while attempting to create a plot with an invalid URI
		 *     should throw an exception.
		 */

		// Add some supported extensions.
		Set<String> extensions = service.supportedExtensions;
		extensions.add("txt");
		extensions.add("tar");
		extensions.add("whoosh");

		URI uri;

		// Check the set of supported extensions.
		assertNotNull(service.getSupportedExtensions());
		assertEquals(3, service.getSupportedExtensions().size());
		assertTrue(service.getSupportedExtensions().contains("txt"));
		assertTrue(service.getSupportedExtensions().contains("tar"));
		assertTrue(service.getSupportedExtensions().contains("whoosh"));

		try {
			// ---- Try some valid URIs. ---- //
			// These should throw no exception.
			uri = new URI("/home/bob/file.txt");
			assertNull(service.createPlot(uri));
			uri = new URI("file:/C:Users/user/folder//one.two.three.tar");
			assertNull(service.createPlot(uri));
			uri = new URI("1.txt");
			assertNull(service.createPlot(uri));

			// ---- Try invalid URIs. ---- //
			// These should throw IllegalArgumentExceptions.

			// Try a null URI (invalid).
			uri = null;
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (NullPointerException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try an opaque URI (invalid).
			uri = new URI("mailto:foo@bar.com");
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try an invalid (unsupported type) URI.
			uri = new URI("/home/.bob/file.csv");
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try an invalid (unsupported type) URI.
			uri = new URI("file:/C:Users/user/folder/one.two.three");
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try a URI that's too short (blank name).
			uri = new URI(".txt");
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}

			// Try a URI that's missing an extension.
			uri = new URI("/tmp/txt");
			try {
				service.createPlot(uri);
				fail("AbstractVizService error: "
						+ "No exception was thrown for invalid URI.");
			} catch (IllegalArgumentException e) {
				// An exception should be thrown for an invalid URI.
			}
			// --------------------------- //
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("AbstractVizServiceTester error: "
					+ "A test URI was invalid.");
		} catch (Exception e) {
			e.printStackTrace();
			fail("AbstractVizServiceTester error: " + "Unknown exception!");
		}

		return;
	}

	/**
	 * The abstract viz service throws simple exceptions for the create plot
	 * method, including:
	 * <ol>
	 * <li>null URI specified</li>
	 * <li>URI pointing to a file with a bad extension</li>
	 * </ol>
	 * <p>
	 * However, when a non-null URI is passed in with a valid extension, it
	 * should not throw any exceptions, but should return null.
	 * </p>
	 */
	public void checkCreatePlot() {
		FakeVizService service = new FakeVizService();
		service.supportedExtensions.add("ext");

		// An exception should be thrown when creating a plot with a null URI.
		URI nullURI = null;
		try {
			service.createPlot(nullURI);
			fail(getClass().getName() + " failure: "
					+ "Exception not thrown when attempting to create a plot "
					+ "from a null URI.");
		} catch (Exception e) {
			// Exception thrown as expected.
		}

		// An exception should be thrown when creating a plot with a URI that
		// has an unsupported extension.
		try {
			URI uriWithBadExtension = new URI("file:///home/file.zip");
			service.createPlot(uriWithBadExtension);
			fail(getClass().getName() + " failure: "
					+ "Exception not thrown when attempting to create a plot "
					+ "from a URI with an unsupported extension.");
		} catch (URISyntaxException e) {
			fail("This shouldn't happen.");
		} catch (Exception e) {
			// Exception thrown as expected.
		}

		// Null should be returned for a valid URI with a valid extension.
		try {
			URI validURI = new URI("file:///home/file.ext");
			assertNull(service.createPlot(validURI));
		} catch (URISyntaxException e) {
			fail("This shouldn't happen.");
		} catch (Exception e) {
			fail(getClass().getName() + " failure: "
					+ "Exception thrown when attempting to create a plot "
					+ "from a valid URI with a supported extension.");
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
		 * A set of supported extensions. Starts off empty. This will be
		 * returned when the super class requests the concrete class'
		 * extensions.
		 */
		public final Set<String> supportedExtensions = new HashSet<String>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.IVizService#getName()
		 */
		@Override
		public String getName() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.IVizService#getVersion()
		 */
		@Override
		public String getVersion() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.AbstractVizService#
		 * findSupportedExtensions()
		 */
		@Override
		protected Set<String> findSupportedExtensions() {
			return supportedExtensions;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.IVizService#getFactory()
		 */
		@Override
		public IControllerFactory getFactory() {
			return null;
		}

		@Override
		public int getNumAdditionalPages() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String createAdditionalPage(MultiPageEditorPart parent,
				IFileEditorInput file, int pageNum) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
