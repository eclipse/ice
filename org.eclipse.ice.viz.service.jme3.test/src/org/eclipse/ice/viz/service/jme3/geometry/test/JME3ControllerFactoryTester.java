/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.geometry.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.jme3.geometry.JME3ControllerFactory;
import org.eclipse.ice.viz.service.jme3.geometry.JME3Shape;
import org.eclipse.ice.viz.service.jme3.geometry.JME3ShapeView;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;
import org.junit.Test;

/**
 * A class for testing the functionality of the JME3ControllerFactory.
 * 
 * @author Robert Smith
 *
 */
public class JME3ControllerFactoryTester {

	/**
	 * Tests the factory's ability to create the proper output
	 */
	@Test
	public void checkFactory() {

		// Create a factory and a component
		JME3ControllerFactory factory = new JME3ControllerFactory();
		AbstractMeshComponent mesh = new AbstractMeshComponent();

		// Since the component is not a shape, it should be rejected by the
		// factory.
		assertNull(factory.createController(mesh));

		// Create a shape and send it to the factory
		ShapeComponent shape = new ShapeComponent();
		AbstractController shapeController = factory.createController(shape);

		// The created controller should be a JME3Shape wrapping the model, and
		// the created view should be a JME3ShapeView.
		assertTrue(shapeController instanceof JME3Shape);
		assertTrue(shapeController.getModel() == shape);
		assertTrue(shapeController.getView() instanceof JME3ShapeView);
	}

}
