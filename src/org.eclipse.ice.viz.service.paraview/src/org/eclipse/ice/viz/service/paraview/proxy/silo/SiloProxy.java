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
	protected Map<String, String[]> findFeatures(VtkWebClient client) {
		Map<String, String[]> featureMap = new HashMap<String, String[]>();
		// TODO
		return featureMap;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected Map<String, String[]> findProperties(VtkWebClient client) {
		Map<String, String[]> propertyMap = new HashMap<String, String[]>();
		// TODO
		return propertyMap;
	}
}