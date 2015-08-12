/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.geometry;

import java.util.Map;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.datastructures.VizObject;
import org.eclipse.ice.viz.service.geometry.shapes.Geometry;


/**
 * This class is an implementation of IVizService which provides a 3D
 * Constructive Solid Geometry based geometry editing OSGI service to the
 * platform by a creating GeometryCanvas.
 * 
 * @author Robert Smith
 *
 */
public class JME3GeometryVizService extends AbstractVizService {

	/**
	 * The default constructor. There is nothing to initialize.
	 */
	public JME3GeometryVizService() {

	}

	/**
	 * Creates a GeometryCanvas based on the given geometry object.
	 * 
	 * @param geometry
	 *            An object containing a hierarchy of shapes to model in the
	 *            canvas.
	 * @return A new GeometryCanvas containing the geometry.
	 */
	@Override
	public IVizCanvas createCanvas(VizObject geometry) {
		if (geometry instanceof Geometry) {
			JME3GeometryCanvas canvas = new JME3GeometryCanvas((Geometry) geometry);
			return canvas;
		}
		throw new IllegalArgumentException("GeometryService can only render geometry viz objects.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "JME3 Geometry Editor";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public boolean hasConnectionProperties() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#getConnectionProperties()
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IVizService#setConnectionProperties(java.
	 * util.Map)
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizService#disconnect()
	 */
	@Override
	public boolean disconnect() {
		return false;
	}

}
