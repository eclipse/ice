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
package org.eclipse.ice.viz.service.paraview.proxy.silo;

import java.net.URI;

import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxyBuilder;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyBuilder;

/**
 * This class provides an {@link IParaViewProxyBuilder} for the Exodus format.
 * As such, it is responsible for creating {@link IParaViewProxy} instances that
 * can support Exodus files. Support includes the file extensions:
 * <ul>
 * <li>{@code .silo}</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxyBuilder extends AbstractParaViewProxyBuilder {

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> This class should be instantiated by OSGi!
	 * </p>
	 */
	public SiloProxyBuilder() {
		// Populate the set of supported extensions.
		extensions.add("silo");

		return;
	}

	/*
	 * Implements a method from IParaViewProxyBuilder.
	 */
	@Override
	public String getName() {
		return "Default Silo Proxy Builder";
	}

	/*
	 * Overrides a method from AbstractParaViewProxyBuilder.
	 */
	@Override
	protected IParaViewProxy createConcreteProxy(URI uri) {
		return new SiloProxy(uri);
	}
}
