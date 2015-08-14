/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.mesh;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.datastructures.VizObject;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;

/**
 * A VizService which makes an IVizCanvas for mesh editing, with graphics
 * powered by jMonkeyEngine3. It takes a VizMeshComponent object as input, using
 * it as the data modeled by the IVizCanvas.
 * 
 * @author Robert Smith
 *
 */
public class JME3MeshVizService extends AbstractVizService {

	/**
	 * Nullary constructor. It has nothing to initialize.
	 */
	public JME3MeshVizService() {
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getName() {
		return "JME3 Mesh Service";
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {
		return null;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public IVizCanvas createCanvas(VizObject object) throws Exception {
		JME3MeshCanvas canvas = null;
		
		//If input is a VizMeshComponent, use it to create a JME3MeshCanvas.
		if (object instanceof VizMeshComponent) {
			canvas = new JME3MeshCanvas((VizMeshComponent) object);
		}
		return canvas;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	protected Set<String> findSupportedExtensions() {
		return new HashSet<String>();
	}

}
