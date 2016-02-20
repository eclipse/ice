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

import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.junit.Test;

/**
 * A class to test the functionality of the JunctionController
 * 
 * @author Robert Smith
 *
 */
public class JunctionControllerTester {

	/**
	 * Check that the part is cloned correctly.
	 */
	@Test
	public void checkClone(){
		
		//Create a junction
		JunctionController exchanger = new JunctionController(new JunctionMesh(), new AbstractView());
		exchanger.setProperty("Test", "Property");
		
		//Clone it and check that they are identical
		JunctionController clone = (JunctionController) exchanger.clone();
		assertTrue(exchanger.equals(clone));
	}
}
