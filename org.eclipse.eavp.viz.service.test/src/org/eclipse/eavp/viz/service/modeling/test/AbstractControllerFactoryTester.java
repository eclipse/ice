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
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractControllerFactory;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.PointController;
import org.eclipse.eavp.viz.service.modeling.PointMesh;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class for testing the functionality of the AbstractControllerFactory
 * 
 * @author Robert Smith
 *
 */
public class AbstractControllerFactoryTester {

	/**
	 * Test that the controller invokes the proper provider for the given
	 * object.
	 */
	@Test
	public void checkControllerCreation() {

		TestControllerFactory factory = new TestControllerFactory();

		// Check that the factory creates the right kind of controller for a
		// PointMesh
		PointMesh point = new PointMesh();
		AbstractController pointC = factory.createController(point);
		assertTrue(pointC instanceof PointController);

		// Check that the factory creates the right kind of controller for a
		// VertexMesh
		VertexMesh vertex = new VertexMesh();
		AbstractController vertexC = factory.createController(vertex);
		assertTrue(vertexC instanceof VertexController);

		// Check that the factory returns null for an unrecognized type.
		AbstractController nullC = factory.createController(new AbstractMesh());
		assertTrue(nullC == null);
	}

	/**
	 * A simple extension of the AbstractControllerFactory to include test
	 * classes
	 * 
	 * @author Robert Smith
	 *
	 */
	public class TestControllerFactory extends AbstractControllerFactory {

		/**
		 * The default constructor.
		 */
		public TestControllerFactory() {
			super();

			// Put the test providers into the map
			typeMap.put(PointMesh.class, new TestPointProvider());
			typeMap.put(VertexMesh.class, new TestVertexProvider());
		}

		/**
		 * Creates a PointController for PointMeshes
		 * 
		 * @author Robert Smith
		 *
		 */
		public class TestPointProvider implements IControllerProvider {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.eavp.viz.service.modeling.AbstractControllerFactory.
			 * IControllerProvider#createController(org.eclipse.eavp.viz.service.
			 * modeling.AbstractMesh)
			 */
			@Override
			public AbstractController createController(AbstractMesh model) {
				return new PointController((PointMesh) model,
						new AbstractView());
			}

		}

		/**
		 * Creates a PointController for PointMeshes
		 * 
		 * @author Robert Smith
		 *
		 */
		public class TestVertexProvider implements IControllerProvider {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.eavp.viz.service.modeling.AbstractControllerFactory.
			 * IControllerProvider#createController(org.eclipse.eavp.viz.service.
			 * modeling.AbstractMesh)
			 */
			@Override
			public AbstractController createController(AbstractMesh model) {
				return new VertexController((VertexMesh) model,
						new AbstractView());
			}

		}
	}
}
