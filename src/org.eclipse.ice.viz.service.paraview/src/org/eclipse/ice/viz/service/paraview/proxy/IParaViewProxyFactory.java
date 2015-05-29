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
import java.util.Set;

/**
 * This interface provides a factory from which {@link IParaViewProxy} instances
 * can be created for a set of supported extensions.
 * <p>
 * Instances of this class should register with the
 * {@link IParaViewProxyFactoryRegistry} via OSGi.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public interface IParaViewProxyFactory {

	/**
	 * Gets the set of supported extensions. Note that duplicate extensions are
	 * not to be listed. The extensions should not include the leading period.
	 * 
	 * @return The set of supported extensions for this factory.
	 */
	Set<String> getExtensions();

	/**
	 * Creates an {@link IParaViewProxy} for the specified file based on its
	 * extension.
	 * 
	 * @param file
	 *            The file for which a proxy will be created. If {@code null}, a
	 *            proxy will not be returned.
	 * @return An proxy for the file, or {@code null} if the specified file is
	 *         null or a proxy cannot be created.
	 * @throws IllegalArgumentException
	 *             If the file's extension is not supported by this proxy.
	 */
	IParaViewProxy createProxy(URI file) throws IllegalArgumentException;
}
