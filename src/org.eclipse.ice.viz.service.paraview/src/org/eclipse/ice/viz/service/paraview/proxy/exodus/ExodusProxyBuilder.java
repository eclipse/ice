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
package org.eclipse.ice.viz.service.paraview.proxy.exodus;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxyBuilder;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyBuilder;

/**
 * This class provides an {@link IParaViewProxyBuilder} for the Exodus format.
 * As such, it is responsible for creating {@link IParaViewProxy} instances that
 * can support Exodus files. Support includes the file extensions:
 * <ul>
 * <li>{@code .e}</li>
 * <li>{@code .ex}</li>
 * <li>{@code .exo}</li>
 * <li>{@code .ex2}</li>
 * <li>{@code .exii}</li>
 * <li>{@code .gen}</li>
 * <li>{@code .exodus}</li>
 * <li>{@code .nemesis}</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 */
public class ExodusProxyBuilder extends AbstractParaViewProxyBuilder {

	/*
	 * Implements an abstract method from AbstractParaViewProxyBuilder.
	 */
	@Override
	protected IParaViewProxy createConcreteProxy(URI uri) {
		return new ExodusProxy(uri);
	}

	/*
	 * Implements an abstract method from AbstractParaViewProxyBuilder.
	 */
	@Override
	protected Set<String> findExtensions() {
		Set<String> extensions = new HashSet<String>();
		extensions.add("e");
		extensions.add("ex");
		extensions.add("exo");
		extensions.add("ex2");
		extensions.add("exii");
		extensions.add("gen");
		extensions.add("exodus");
		extensions.add("nemesis");
		return extensions;
	}

	/*
	 * Implements a method from IParaViewProxyBuilder.
	 */
	@Override
	public String getName() {
		return "Default Exodus Proxy Builder";
	}
}
