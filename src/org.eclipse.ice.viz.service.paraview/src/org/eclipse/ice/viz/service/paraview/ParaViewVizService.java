/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview;

import java.net.URI;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;

/**
 * This class is responsible for providing a service to connect to (or launch)
 * either local or remote instances of ParaView. Additionally, it can be used to
 * create {@link ParaViewPlot}s that can render files supported by ParaView
 * within an SWT-based application.
 * 
 * @see ParaViewPlot
 * @see #createPlot(URI)
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewVizService implements IVizService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "ParaView";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Update this value.
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * hasConnectionProperties()
	 */
	@Override
	public boolean hasConnectionProperties() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * getConnectionProperties()
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * setConnectionProperties(java.util.Map)
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#disconnect()
	 */
	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizService#createPlot(java
	 * .net.URI)
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
