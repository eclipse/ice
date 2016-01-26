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
package org.eclipse.ice.viz.service.geometry.reactor.test;

import static org.junit.Assert.*;

import org.eclipse.ice.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.ice.viz.service.geometry.reactor.PipeController;
import org.eclipse.ice.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.junit.Test;

/**
 * A class to test the functionality of the HeatExchangerMesh.
 * 
 * @author Robert Smith
 *
 */
public class HeatExchangerMeshTester {

	/**
	 * Test the HeatExchanger's ability to correctly manage its contained pipes.
	 */
	@Test
	public void checkPipes(){
		
		//Create a heat exchanger and some pipes
		HeatExchangerMesh exchanger = new HeatExchangerMesh();
		
		PipeController primary = new PipeController(new PipeMesh(), new AbstractView());
		PipeController secondary = new PipeController(new PipeMesh(), new AbstractView());
		
		exchanger.setSecondaryPipe(secondary);
		assertTrue(secondary == exchanger.getSecondaryPipe());
				
		exchanger.setPrimaryPipe(primary);
		assertTrue(primary == exchanger.getPrimaryPipe());
		
	}
}
