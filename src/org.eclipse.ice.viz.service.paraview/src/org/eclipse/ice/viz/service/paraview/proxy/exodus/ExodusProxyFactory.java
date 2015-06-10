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
package org.eclipse.ice.viz.service.paraview.proxy.exodus;

import java.net.URI;

import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;

/**
 * This class provides an {@link IParaViewProxyFactory} for the Exodus format.
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
public class ExodusProxyFactory extends AbstractParaViewProxyFactory {

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> This class should be instantiated by OSGi!
	 * </p>
	 */
	public ExodusProxyFactory() {
		// Populate the set of supported extensions.
		extensions.add("e");
		extensions.add("ex");
		extensions.add("exo");
		extensions.add("ex2");
		extensions.add("exii");
		extensions.add("gen");
		extensions.add("exodus");
		extensions.add("nemesis");

		return;
	}

	/*
	 * Implements a method from IParaViewProxyFactory.
	 */
	@Override
	public String getName() {
		return "Default Exodus Proxy Factory";
	}

	/*
	 * Overrides a method from AbstractParaViewProxyFactory.
	 */
	@Override
	protected IParaViewProxy createConcreteProxy(URI uri) {
		return new ExodusProxy(uri);
	}
}
