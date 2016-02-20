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

import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerController;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeController;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.junit.Test;

/**
 * A class to test the functionality of the HeatExchangerController.
 * 
 * @author Robert Smith
 *
 */
public class HeatExchangerControllerTester {

	/**
	 * Test the HeatExchanger's ability to correctly manage its contained pipes.
	 */
	@Test
	public void checkPipes(){
		
		//Create a heat exchanger and some pipes
		HeatExchangerController exchanger = new HeatExchangerController(new HeatExchangerMesh(), new AbstractView());
		
		//Create pipes for the exchanger
		PipeController primary = new PipeController(new PipeMesh(), new AbstractView());
		PipeController secondary = new PipeController(new PipeMesh(), new AbstractView());
		
		//Check that the secondary pipe can be set
		exchanger.setSecondaryPipe(secondary);
		assertTrue(secondary == exchanger.getSecondaryPipe());
				
		//Check that the primary pipe can be set
		exchanger.setPrimaryPipe(primary);
		assertTrue(primary == exchanger.getPrimaryPipe());
		
	}
	
	/**
	 * Check that the part is cloned correctly.
	 */
	@Test
	public void checkClone(){
		
		//Create a face
		HeatExchangerController exchanger= new HeatExchangerController(new HeatExchangerMesh(), new AbstractView());
		exchanger.setProperty("Test", "Property");
		
		//Clone it and check that they are identical
		HeatExchangerController clone = (HeatExchangerController) exchanger.clone();
		assertTrue(exchanger.equals(clone));
	}
}
