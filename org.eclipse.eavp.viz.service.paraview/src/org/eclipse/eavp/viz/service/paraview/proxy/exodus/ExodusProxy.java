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
package org.eclipse.eavp.viz.service.paraview.proxy.exodus;

import java.net.URI;
import java.util.List;

import org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature.ColorByLocation;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature.ColorByMode;

/**
 * This class provides a concrete {@link IParaViewProxy} that supports loading
 * and rendering Exodus files.
 * <p>
 * <b>Note:</b> In practice, instances of this class should not be instantiated.
 * Rather, they should be obtained from the {@link IParaViewProxyFactory}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class ExodusProxy extends AbstractParaViewProxy {

	/**
	 * The default constructor. The associated Exodus file's URI <i>must</i> be
	 * specified.
	 * 
	 * @param uri
	 *            The URI for the Exodus file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	protected ExodusProxy(URI uri) throws NullPointerException {
		super(uri);
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy#findFeatures()
	 */
	@Override
	protected List<ProxyFeature> findFeatures() {
		/*
		 * The structure of Exodus files looks like so:
		 * 
		 * "data" > "arrays" appears to have a list of array names, each of
		 * which has a "location" key set to one of { POINTS, CELLS, FIELDS }.
		 * 
		 * "ui" has a lot of elements... important ones listed here.
		 * "Element Variables", a list of variables that can be loaded
		 * "Face Variables", a list of variables that can be loaded
		 * "Edge Variables", a list of variables that can be loaded
		 * "Point Variables", a list of variables that can be loaded
		 * "Global Variables", a list of variables that can be loaded
		 */
		List<ProxyFeature> features = super.findFeatures();

		features.add(new ProxyFeature("Point Variables", 11, ColorByMode.ARRAY,
				ColorByLocation.POINTS) {
			@Override
			protected int getProxyId() {
				return getFileId();
			}
		});
		features.add(new ProxyFeature("Element Variables", 2, ColorByMode.ARRAY,
				ColorByLocation.CELLS) {
			@Override
			protected int getProxyId() {
				return getFileId();
			}
		});
		// features.put("Face Variables", new ProxyFeature(3, "Face Variables",
		// "FaceVariables"));
		// features.put("Edge Variables", new ProxyFeature(4, "Edge Variables",
		// "EdgeVariables"));
		// features.put("Global Variables", new ProxyFeature(12, "Global
		// Variables", "GlobalVariables"));

		return features;
	}
}
