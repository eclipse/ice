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
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;

/**
 * This class provides a basic implementation of {@link IParaViewProxy} and
 * should be used whenever possible when dealing with the ParaView Java client.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractParaViewProxy implements IParaViewProxy {

	private final URI uri;

	/**
	 * The default constructor. This should only be called by sub-class
	 * constructors.
	 * 
	 * @param uri
	 *            The URI for the ParaView-supported file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	protected AbstractParaViewProxy(URI uri) throws NullPointerException {
		if (uri == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a null URI.");
		}

		this.uri = uri;

		return;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean open(ParaViewConnectionAdapter connection)
			throws NullPointerException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public URI getURI() {
		return uri;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatureCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatures(String category)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setFeature(String category, String feature)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Map<String, Set<String>> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setProperty(String property, String value)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public int setProperties(Map<String, String> properties)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}
}