/******************************************************************************* 
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.mesh.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.ice.viz.service.jme3.mesh.JME3MeshCanvas;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateMode;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateModeFactory;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
import org.junit.Test;

/**
 * This class tests the JME3MeshCanvas.
 * 
 * @author Robert Smith
 *
 */
public class JME3MeshCanvasTester {

	/**
	 * Test basic functionalities for the JME3 mesh canvas
	 */
	@Test
	public void testCanvas() {
		// Create canvas
		VizMeshComponent mesh = new VizMeshComponent();
		JME3MeshCanvas canvas = new JME3MeshCanvas(mesh);

		// The mesh should be 2D
		assertEquals(canvas.getNumberOfAxes(), 2);

		// Get the list of modes
		MeshAppStateModeFactory factory = canvas.getMeshAppStateModeFactory();
		List<org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateModeFactory.Mode> modeList = factory
				.getAvailableModes();

		// Set the mode to edit
		MeshAppStateMode mode = factory.getMode(modeList.get(1));
		canvas.setMode(mode);

	}
}
