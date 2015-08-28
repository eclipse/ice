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

import static org.junit.Assert.*;

import org.eclipse.ice.viz.service.datastructures.VizObject.VizObject;
import org.eclipse.ice.viz.service.jme3.mesh.JME3MeshCanvas;
import org.eclipse.ice.viz.service.jme3.mesh.JME3MeshVizService;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
import org.junit.Test;

/**
 * This class tests the JME3MeshService.
 * 
 * @author Robert Smith
 *
 */
public class JME3MeshVizServiceTester {
	
	@Test
	/**
	 * Tests basic functionalities for the JME3 mesh viz service.
	 */
	public void testService() {
		JME3MeshVizService service = new JME3MeshVizService();
		JME3MeshCanvas canvas = null;
		
		//Create a canvas
		try {
			canvas = (JME3MeshCanvas) service.createCanvas(new VizMeshComponent());
		} catch (Exception e1) {
			fail();
		}
		assertNotNull(canvas);
		
		//Try to create a canvas with something other than a VizMeshComponent
		try {
			canvas = (JME3MeshCanvas) service.createCanvas(new VizObject());
		} catch (Exception e) {
			fail();
		}
		
		//The service should have returned null
		assertNull(canvas);
		
	}
}
