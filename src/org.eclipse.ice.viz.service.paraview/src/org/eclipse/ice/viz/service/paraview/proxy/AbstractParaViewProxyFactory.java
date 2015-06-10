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
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * This provides a base class for implementing an {@link IParaViewProxyFactory}.
 * Sub-classes need only do the following:
 * <ol>
 * <li>In their constructor, add supported extensions to {@link #extensions}.</li>
 * <li>Implement {@link #createConcreteProxy(URI)}.</li>
 * </ol>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractParaViewProxyFactory implements
		IParaViewProxyFactory {

	/**
	 * The set of extensions supported by this proxy factory.
	 */
	protected final Set<String> extensions;

	/**
	 * The default constructor. Initializes an empty map of supported
	 * {@link #extensions}.
	 */
	protected AbstractParaViewProxyFactory() {
		// Create an populate the set of supported extensions.
		extensions = new HashSet<String>();
	}

	/*
	 * Implements a method from IParaViewProxyFactory.
	 */
	@Override
	public Set<String> getExtensions() {
		// Return a lexicographically ordered set.
		return new TreeSet<String>(extensions);
	}

	/*
	 * Implements a method from IParaViewProxyFactory.
	 */
	@Override
	public IParaViewProxy createProxy(URI uri) throws NullPointerException,
			IllegalArgumentException {
		// Throw an NPE for null URIs.
		if (uri == null) {
			throw new NullPointerException("ParaViewProxyFactory error: "
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

		// Throw an exception if the extension is not supported or could not be
		// found.
		if (!extensions.contains(extension)) {
			if (extension.isEmpty()) {
				throw new IllegalArgumentException(
						"ParaViewProxyFactory error: "
								+ "An extension for the specified URI could not be found.");
			} else {
				throw new IllegalArgumentException(
						"ParaViewProxyFactory error: "
								+ "The specified extension \"" + extension
								+ "\" is not supported.");
			}
		}

		// If no exception was thrown, call the sub-class' implementation.
		return createConcreteProxy(uri);
	}

	/**
	 * Creates a specialized proxy for the file.
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
}