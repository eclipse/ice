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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;

import com.kitware.vtk.web.VtkWebClient;

/**
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxy extends AbstractParaViewProxy {

	protected SiloProxy(URI uri) throws NullPointerException {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected Map<String, Set<String>> findFeatures(VtkWebClient client) {
		Map<String, Set<String>> featureMap = new HashMap<String, Set<String>>();
		// TODO
		return featureMap;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected Map<String, Set<String>> findProperties(VtkWebClient client) {
		Map<String, Set<String>> propertyMap = new HashMap<String, Set<String>>();
		// TODO
		return propertyMap;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected boolean setFeatureOnClient(VtkWebClient client, String category,
			String feature) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected boolean setPropertyOnClient(VtkWebClient client, String property,
			String value) {
		// TODO
		return false;
	}

}