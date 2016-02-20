/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.geometry.reactor.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.geometry.reactor.PipeController;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.junit.Test;

/**
 * A class to test the functionality of the ReactorMesh.
 * 
 * @author Robert Smith
 *
 */
public class ReactorMeshTester {

	/**
	 * Test that the Reactor sets itself as the parent to any core channel which is added to it.
	 */
	@Test
	public void checkPipes(){
		
		//Create a reactor and pipe
		ReactorController reactor = new ReactorController(new ReactorMesh(), new AbstractView());
		PipeController pipe = new PipeController(new PipeMesh(), new AbstractView());
		
		//Add the pipe as a core channel
		reactor.addEntityByCategory(pipe, "Core Channels");
		
		//Check that the pipe has the reactor as a parent
		assertTrue(pipe.getEntitiesByCategory("Parent").get(0) == reactor);
	}
}
