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
package org.eclipse.eavp.viz.service.paraview.proxy.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty;

/**
 * A fake proxy that extends {@link AbstractParaViewProxy} and exposes certiain
 * methods to ensure the abstract class re-directs method calls when appropriate
 * to its sub-classes.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeParaViewProxy extends AbstractParaViewProxy {

	/**
	 * A collection of supported categories and features for the fake proxy.
	 * This should be configured just after the fake proxy is constructed.
	 */
	public final List<ProxyFeature> features;
	/**
	 * A list of supported properties for the fake proxy. This should be
	 * configured just after the fake proxy is constructed.
	 */
	public final List<ProxyProperty> properties;

	/**
	 * The default constructor. Used to access the parent class' hidden
	 * constructor (after all, it is an abstract class).
	 * 
	 * @param uri
	 *            The URI for the ParaView-supported file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	public FakeParaViewProxy(URI uri) throws NullPointerException {
		super(uri);

		features = new ArrayList<ProxyFeature>();
		properties = new ArrayList<ProxyProperty>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy#findFeatures()
	 */
	@Override
	protected List<ProxyFeature> findFeatures() {
		List<ProxyFeature> features = super.findFeatures();
		features.addAll(this.features);
		return features;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.AbstractParaViewProxy#findProperties()
	 */
	@Override
	protected List<ProxyProperty> findProperties() {
		List<ProxyProperty> properties = super.findProperties();
		properties.addAll(this.properties);
		return properties;
	}

}
