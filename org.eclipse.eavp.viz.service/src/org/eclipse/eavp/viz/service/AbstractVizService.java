/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This is an abstract base class for creating new {@link IVizService}s and
 * includes helpful methods that are frequently used.
 * <p>
 * In particular this abstract class provides the following basic functionality:
 * </p>
 * <ul>
 * <li>It provides access to a preference store based on the sub-class and its
 * bundle.</li>
 * <li>It maintains a set of supported extensions that is lazily loaded when
 * necessary.</li>
 * <li>When {@link #createPlot(URI)} is called, this checks that the URI is not
 * null and that its extension is supported.</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractVizService implements IVizService {

	/**
	 * A reference to the associated preference page's {@link IPreferenceStore}.
	 * If this has been determined previously, then it should be returned in
	 * {@link #getPreferenceStore()}.
	 */
	private IPreferenceStore preferenceStore = null;

	/**
	 * The set of supported file extensions for this viz service.
	 * <p>
	 * Extensions added to this are expected to be:
	 * <ul>
	 * <li>simple (tar and gz, but not tar.gz),</li>
	 * <li>should not include the leading period (doc, not .doc), and</li>
	 * <li>should be lower case (txt, not TXT).</li>
	 * </ul>
	 * </p>
	 */
	private Set<String> supportedExtensions;

	/**
	 * This class provides a basic implementation where an
	 * {@link IllegalArgumentException} is thrown if the specified URI or its
	 * associated file's extension is invalid.
	 * <p>
	 * It is recommended that sub-classes override this method but call it in
	 * the first line. Sub-classes should also add supported extensions to the
	 * set of {@link #supportedExtensions} at construction.
	 * </p>
	 */
	@Override
	public IPlot createPlot(URI uri) throws Exception {
		// Throw a NullPointerException if the file's URI is null.
		if (uri == null) {
			throw new NullPointerException(
					"IPlot error: " + "Null URI is not allowed.");
		}
		// Throw an IllegalArgumentException if the file's URI is invalid.
		else if (!extensionSupported(uri)) {
			throw new IllegalArgumentException(
					"IPlot error: " + "Invalid URI or URI not specified . "
							+ ((uri != null) ? uri.toString() : ""));
		}
		return null;
	}

	/**
	 * Nothing to do for the basic implementation for creating a canvas.
	 */
	@Override
	public IVizCanvas createCanvas(AbstractController object) throws Exception {
		return null;
	}

	/**
	 * Determines if the URI's extension is included in the set of
	 * {@link #supportedExtensions}.
	 * 
	 * @param uri
	 *            The URI to test.
	 * @return True if the URI's extension could be determined and is among the
	 *         supported extensions, false otherwise.
	 */
	private boolean extensionSupported(URI uri) {
		boolean supported = false;

		// Lazily load the set of supported extensions from the sub-class.
		if (supportedExtensions == null) {
			getSupportedExtensions();
		}

		String path;
		// Determine the extension, if possible, then see if the set of
		// supported extensions contains it.
		if (uri != null && (path = uri.getPath()) != null) {
			int i = path.lastIndexOf('.') + 1;
			// Note: We do not accept files with blank names.
			if (i > 1 && i < path.length()) {
				// We should convert it to lower case to match the set.
				String extension = path.substring(i).toLowerCase();
				supported = supportedExtensions.contains(extension);
			}
		}

		return supported;
	}

	/**
	 * This method will be called exactly once to get the set of supported
	 * extensions for this viz service. Extensions in the set are expected to
	 * conform to the following format:
	 * <ul>
	 * <li>simple (tar and gz, but not tar.gz),</li>
	 * <li>should not include the leading period (doc, not .doc), and</li>
	 * <li>should be lower case (txt, not TXT).</li>
	 * </ul>
	 * 
	 * @return A set containing all supported extensions.
	 */
	protected abstract Set<String> findSupportedExtensions();

	/**
	 * Gets the {@link IPreferenceStore} for the associated preference page.
	 * 
	 * @return The {@code IPreferenceStore} whose defaults should be set.
	 */
	protected IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			// Get the PreferenceStore for the bundle.
			preferenceStore = new CustomScopedPreferenceStore(getClass());
		}
		return preferenceStore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getSupportedExtensions()
	 */
	@Override
	public Set<String> getSupportedExtensions() {
		// Lazily load the set of supported extensions from the sub-class.
		if (supportedExtensions == null) {
			supportedExtensions = new HashSet<String>();
			supportedExtensions.addAll(findSupportedExtensions());
		}
		// Return an ordered set of the extensions.
		return new TreeSet<String>(supportedExtensions);
	}

}
