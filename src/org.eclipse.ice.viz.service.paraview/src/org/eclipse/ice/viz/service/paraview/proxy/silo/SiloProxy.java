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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.IProxyProperty;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature.ColorByLocation;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature.ColorByMode;

/**
 * This class provides a concrete {@link IParaViewProxy} that supports loading
 * and rendering Silo files.
 * <p>
 * <b>Note:</b> In practice, instances of this class should not be instantiated.
 * Rather, they should be obtained from the {@link IParaViewProxyFactory}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxy extends AbstractParaViewProxy {

	/**
	 * The default constructor. The associated Silo file's URI <i>must</i> be
	 * specified.
	 * 
	 * @param uri
	 *            The URI for the Silo file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	protected SiloProxy(URI uri) throws NullPointerException {
		super(uri);
		
		// Nothing to do yet.
	}


	/*
	 * Implements an abstract method from AbstractParaViewProxy.
	 */
	@Override
	protected List<ProxyFeature> findFeatures(
			ParaViewConnection connection) {

		// Initialize the list of supported features.
		List<ProxyFeature> features = new ArrayList<ProxyFeature>();

		//features.add(new ProxyFeature(0, "Meshes", "MeshStatus"));
		//features.add(new ProxyFeature(1, "Materials", "MaterialStatus"));
		features.add(new ProxyFeature(2, "Cell Arrays",
				"CellArrayStatus", ColorByMode.ARRAY, ColorByLocation.CELLS));
		features.add(new ProxyFeature(3, "Point Arrays",
				"PointArrayStatus", ColorByMode.ARRAY, ColorByLocation.POINTS));

		return features;
	}

	/*
	 * Implements an abstract method from AbstractParaViewProxy.
	 */
	@Override
	protected List<IProxyProperty> findProperties(
			ParaViewConnection connection) {
		List<IProxyProperty> properties = super.findProperties(connection);
		// TODO Add silo-specific properties.
		return properties;
	}

}
