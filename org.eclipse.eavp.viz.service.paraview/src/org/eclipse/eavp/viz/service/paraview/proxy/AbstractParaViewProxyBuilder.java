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
package org.eclipse.eavp.viz.service.paraview.proxy;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * This provides a base class for implementing an {@link IParaViewProxyBuilder}.
 * Sub-classes need only do the following:
 * <ol>
 * <li>In their constructor, add supported extensions to {@link #extensions}.
 * </li>
 * <li>Implement {@link #createConcreteProxy(URI)}.</li>
 * </ol>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractParaViewProxyBuilder
		implements IParaViewProxyBuilder {

	/**
	 * The set of extensions supported by this proxy builder.
	 */
	private Set<String> extensions;

	/**
	 * Creates a specialized proxy for the file. When this method is called, the
	 * URI's extension will have already been validated.
	 * 
	 * @param uri
	 *            The uri that needs a new proxy. Implementations may assume
	 *            that this is not {@code null} and that its extension is in
	 *            {@link #extensions}.
	 * @return The new, specialized proxy, or {@code null} if the proxy cannot
	 *         be created for any particular reason.
	 * @see #createProxy(URI)
	 */
	protected abstract IParaViewProxy createConcreteProxy(URI uri);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder#createProxy(java.net.URI)
	 */
	@Override
	public IParaViewProxy createProxy(URI uri)
			throws NullPointerException, IllegalArgumentException {
		// Throw an NPE for null URIs.
		if (uri == null) {
			throw new NullPointerException("ParaViewProxyBuilder error: "
					+ "The specified URI cannot be null.");
		}

		// If possible, determine the extension of the URI. Make it lower
		// case, as case should not matter.
		String extension = "";
		try {
			String path = uri.getPath();
			extension = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
		} catch (IndexOutOfBoundsException e) {
			// Nothing to do.
		}

		// Lazily load the set of supported extensions if necessary.
		if (extensions == null) {
			getExtensions();
		}

		// Throw an exception if the extension is not supported or could not be
		// found.
		if (!extensions.contains(extension)) {
			if (extension.isEmpty()) {
				throw new IllegalArgumentException(
						"ParaViewProxyBuilder error: "
								+ "An extension for the specified URI could not be found.");
			} else {
				throw new IllegalArgumentException(
						"ParaViewProxyBuilder error: "
								+ "The specified extension \"" + extension
								+ "\" is not supported.");
			}
		}

		// If no exception was thrown, call the sub-class' implementation.
		return createConcreteProxy(uri);
	}

	/**
	 * Finds the set of supported file extensions for the concrete class.
	 * <p>
	 * Extensions in this set are expected to be:
	 * <ul>
	 * <li>simple (tar and gz, but not tar.gz),</li>
	 * <li>should not include the leading period (doc, not .doc), and</li>
	 * <li>should be lower case (txt, not TXT).</li>
	 * </ul>
	 * </p>
	 * 
	 * @return A set containing the supported extensions.
	 */
	protected abstract Set<String> findExtensions();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder#getExtensions()
	 */
	@Override
	public Set<String> getExtensions() {
		// Lazily load the set of supported extensions.
		if (extensions == null) {
			extensions = new HashSet<String>();
			extensions.addAll(findExtensions());
		}
		return new TreeSet<String>(extensions);
	}

}
