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

import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the JunctionMesh
 * 
 * @author Robert Smith
 *
 */
public class JunctionMeshTester {

	/**
	 * Check the convenience methods for the JunctionMesh's properties.
	 */
	@Test
	public void checkProperties(){
		
		//The junction for testing
		JunctionMesh junction = new JunctionMesh();
		
		//Check the height functions
		junction.setHeight(1d);
		assertTrue(junction.getHeight() == 1d);
		
		//Check the z in functions
		junction.setZIn(2d);
		assertTrue(junction.getZIn() == 2d);
		
		//Check the z out functions
		junction.setZOut(3d);
		assertTrue(junction.getZOut() == 3d);
	}
	
	/**
	 * Check that the part is cloned correctly.
	 */
	@Test
	public void checkClone(){
		
		//Create a junction
		JunctionMesh junction = new JunctionMesh();
		junction.setProperty("Test", "Property");
		
		//Clone it and check that they are identical
		JunctionMesh clone = (JunctionMesh) junction.clone();
		assertTrue(junction.equals(clone));
	}
}
