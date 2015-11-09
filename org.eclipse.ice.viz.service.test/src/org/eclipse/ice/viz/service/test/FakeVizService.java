/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.modeling.AbstractController;

/**
 * This is a fake IVizService that is used in testing. The only thing it really
 * does is set the name to whatever is passed in the constructor, which is
 * enough for the tests.
 * 
 * @author Jay Jay Billings
 *
 */
public class FakeVizService implements IVizService {

	/**
	 * The name of the fake service
	 */
	public String name;

	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the fake service
	 */
	public FakeVizService(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#createPlot(java.net.URI)
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#getSupportedExtensions()
	 */
	@Override
	public Set<String> getSupportedExtensions() {
		return new HashSet<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IVizService#createCanvas(org.eclipse.ice.viz.
	 * service.datastructures.VizObject)
	 */
	@Override
	public IVizCanvas createCanvas(AbstractController object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
