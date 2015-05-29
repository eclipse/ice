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
package org.eclipse.ice.viz.service.paraview.proxy.silo;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;

/**
 * This class provides an {@link IParaViewProxyFactory} for the Silo format. As
 * such, it is responsible for creating {@link IParaViewProxy} instances that
 * can support Silo files. Support includes the file extensions:
 * <ul>
 * <li>{@code .silo}</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxyFactory implements IParaViewProxyFactory {

	/**
	 * The set of extensions supported by this proxy factory.
	 */
	private final Set<String> extensions;

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> This class should be instantiated by OSGi!
	 * </p>
	 */
	public SiloProxyFactory() {
		// Create an populate the set of supported extensions.
		extensions = new HashSet<String>();
		extensions.add("silo");
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
	public IParaViewProxy createProxy(URI file) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
