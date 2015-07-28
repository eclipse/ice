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
package org.eclipse.ice.viz.service;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This is an abstract base class for creating new {@link IVizService}s and
 * includes helpful methods that are frequently used.
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
	protected final Set<String> supportedExtensions;

	/**
	 * The default constructor.
	 */
	public AbstractVizService() {
		// Initialize the HashSet of supported file extensions.
		supportedExtensions = new HashSet<String>();
	}

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
	public IVizCanvas createPlot(URI file) throws Exception {
		// Throw an IllegalArgumentException if the file's URI is invalid.
		if (!extensionSupported(file)) {
			throw new IllegalArgumentException("IPlot error: "
					+ "Invalid URI or URI not specified . " + ((file != null) ? file.toString() : ""));
		}
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
	public boolean extensionSupported(URI uri) {
		boolean supported = false;

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
}
