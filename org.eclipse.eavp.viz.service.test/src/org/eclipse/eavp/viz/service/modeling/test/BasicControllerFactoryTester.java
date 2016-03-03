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

import org.eclipse.eavp.viz.service.modeling.BasicControllerProviderFactory;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IControllerProvider;
import org.eclipse.eavp.viz.service.modeling.IMesh;
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
public class BasicControllerFactoryTester {

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
		IController pointC = factory.createProvider(point)
				.createController(point);
		assertTrue(pointC instanceof PointController);

		// Check that the factory creates the right kind of controller for a
		// VertexMesh
		VertexMesh vertex = new VertexMesh();
		IController vertexC = factory.createProvider(vertex)
				.createController(vertex);
		assertTrue(vertexC instanceof VertexController);

		// Check that the factory returns null for an unrecognized type.
		IControllerProvider nullC = factory.createProvider(new BasicMesh());
		assertTrue(nullC == null);
	}

	/**
	 * A simple extension of the AbstractControllerFactory to include test
	 * classes
	 * 
	 * @author Robert Smith
	 *
	 */
	public class TestControllerFactory extends BasicControllerProviderFactory {

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
			 * IControllerProvider#createController(org.eclipse.eavp.viz.
			 * service. modeling.AbstractMesh)
			 */
			@Override
			public IController createController(IMesh model) {
				return new PointController((PointMesh) model,
						new BasicView());
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
			 * IControllerProvider#createController(org.eclipse.eavp.viz.
			 * service. modeling.AbstractMesh)
			 */
			@Override
			public IController createController(IMesh model) {
				return new VertexController((VertexMesh) model,
						new BasicView());
			}

		}
	}
}
