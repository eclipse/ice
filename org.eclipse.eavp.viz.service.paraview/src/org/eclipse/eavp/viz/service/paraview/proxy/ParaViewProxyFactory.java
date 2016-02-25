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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the standard implementation of the
 * {@link IParaViewProxyFactory}.
 * <p>
 * This implementation provides an additional feature: If multiple builders
 * support the same extensions, then for each shared extension, the most
 * recently registered builder will be returned in {@link #getProxyBuilder(URI)}
 * . Furthermore, when that builder is unregistered, its supported extensions
 * will fall back to the previously registered builder for that extension.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewProxyFactory implements IParaViewProxyFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ParaViewProxyFactory.class);

	/**
	 * The map of builders, keyed on supported extensions. We use a list for
	 * each extension so that multiple builders can be registered for each
	 * extension.
	 */
	private final Map<String, List<IParaViewProxyBuilder>> builderMap;

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> This class should be instantiated by OSGi!
	 * </p>
	 */
	public ParaViewProxyFactory() {
		// Initialize the map of builders.
		builderMap = new HashMap<String, List<IParaViewProxyBuilder>>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory#createProxy(java.net.URI)
	 */
	@Override
	public IParaViewProxy createProxy(URI uri)
			throws NullPointerException, IllegalArgumentException {
		// Check for a null URI.
		if (uri == null) {
			throw new NullPointerException("ParaViewProxyFactory error: "
					+ "Cannot create a proxy for a null file.");
		}

		// If possible, determine the extension of the URI. Make it lower
		// case, as case should not matter.
		String extension = null;
		try {
			String path = uri.getPath();
			extension = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
		} catch (IndexOutOfBoundsException e) {
			// Nothing to do.
		}

		// Throw an exception if the extension is not supported.
		if (!builderMap.containsKey(extension)) {
			throw new IllegalArgumentException("ParaViewProxyFactory error: "
					+ "The extension \"" + extension + "\" is not supported.");
		}

		// Get the most recently registered builder for the extension.
		List<IParaViewProxyBuilder> builders = builderMap.get(extension);
		IParaViewProxyBuilder builder = builders.get(builders.size() - 1);

		// Return a new proxy created by the builder.
		return builder.createProxy(uri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory#getExtensions()
	 */
	@Override
	public Set<String> getExtensions() {
		return new TreeSet<String>(builderMap.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory#registerProxyBuilder(org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder)
	 */
	@Override
	public boolean registerProxyBuilder(IParaViewProxyBuilder builder) {
		boolean registered = false;
		if (builder != null) {
			// Add the builder to the *end* of the list of builders for each
			// supported extension.
			for (String extension : builder.getExtensions()) {
				if (extension != null) {
					// Convert it to lower case.
					extension = extension.toLowerCase();

					List<IParaViewProxyBuilder> builders = builderMap
							.get(extension);

					// Since we want to re-register the builder as the most
					// recent one, remove it from the list. We also must
					// generate a new list of builders for the extension if
					// this is the first builder for it.
					if (builders != null) {
						builders.remove(builder);
					} else {
						builders = new ArrayList<IParaViewProxyBuilder>();
						builderMap.put(extension, builders);
					}

					// Finally, add the builder to the list.
					registered |= builders.add(builder);
				}
			}
		}

		// Print out debug output.
		if (registered) {
			logger.info("ParaViewProxyFactory message: " + "\""
					+ builder.getName() + "\" registered.");
		}

		return registered;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory#unregisterProxyBuilder(org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder)
	 */
	@Override
	public boolean unregisterProxyBuilder(IParaViewProxyBuilder builder) {
		boolean unregistered = false;
		if (builder != null) {
			// Remove the builder from the list of builders for each supported
			// extension.
			for (String extension : builder.getExtensions()) {
				if (extension != null) {
					// Convert it to lower case.
					extension = extension.toLowerCase();

					List<IParaViewProxyBuilder> builders = builderMap
							.get(extension);
					if (builders != null) {
						unregistered |= builders.remove(builder);

						// Remove the list if the extension is no longer
						// supported.
						if (builders.isEmpty()) {
							builderMap.remove(extension);
						}
					}
				}
			}
		}

		// Print out debug output.
		if (unregistered) {
			logger.info("ParaViewProxyFactory message: " + "\""
					+ builder.getName() + "\" unregistered.");
		}

		return unregistered;
	}
}
