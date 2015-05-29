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

/**
 * Implementations of this interface provide a registry that maps supported
 * extensions for files to {@link IParaViewProxyFactory} instances. Client code
 * can request a factory by calling {@link #getProxyFactory(URI)}.
 * <p>
 * This interface is designed to be provided and referenced via OSGi.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public interface IParaViewProxyFactoryRegistry {

	/**
	 * Registers a new proxy factory using the factory's supported extensions.
	 * <p>
	 * This method will usually be called by OSGi.
	 * </p>
	 * 
	 * @param factory
	 *            The factory to register. If {@code null}, nothing is
	 *            registered.
	 * @return True if the provided factory was registered, false otherwise
	 *         (including the case where the factory has no supported
	 *         extensions).
	 */
	boolean registerProxyFactory(IParaViewProxyFactory factory);

	/**
	 * Unregisters the specified proxy factory. Its extensions should no longer
	 * be supported if it is the only factory for said extensions.
	 * <p>
	 * This method will usually be called by OSGi.
	 * </p>
	 * 
	 * @param factory
	 *            The factory to unregister. If {@code null}, nothing is
	 *            unregistered.
	 * @return True if the provided factory was unregistered, false otherwise
	 *         (including the case where the factory has no supported
	 *         extensions).
	 */
	boolean unregisterProxyFactory(IParaViewProxyFactory factory);

	/**
	 * Gets a factory for the provided file based on its extension.
	 * 
	 * @param uri
	 *            The file for which a proxy will be created. If {@code null}, a
	 *            factory will not be returned.
	 * @return A factory capable of creating a proxy for the file, or
	 *         {@code null} if a factory could not be created for the file.
	 */
	IParaViewProxyFactory getProxyFactory(URI uri);
}
