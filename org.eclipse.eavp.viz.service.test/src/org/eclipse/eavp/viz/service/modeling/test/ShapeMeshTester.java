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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.junit.Test;

/**
 * A class for testing Shape.
 * 
 * @author Robert Smith
 *
 */
public class ShapeMeshTester {

	/**
	 * Test that the mesh is correctly cloned
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		ShapeMesh mesh = new ShapeMesh();
		mesh.setProperty(MeshProperty.DESCRIPTION, "Property");
		ShapeMesh clone = (ShapeMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}

	/**
	 * Check the special behavior for parent entities
	 */
	@Test
	public void checkParent() {

		// Create a shape to test
		ShapeMesh shapeModel = new ShapeMesh();
		TestShape shape = new TestShape(shapeModel, new BasicView());

		// Create a parent shape
		ShapeMesh parentModel1 = new ShapeMesh();
		TestShape parent1 = new TestShape(parentModel1, new BasicView());

		// Set the shape's parent
		shape.setParent(parent1);

		// Clear the parent's notification state
		parent1.wasUpdated();

		// The parent category should now contain the parent
		assertTrue(shape.getEntitiesFromCategory(MeshCategory.PARENT)
				.contains(parent1));

		// Trigger an update from the shape and check that the parent received
		// it
		shape.setProperty(MeshProperty.ID, "1");
		assertTrue(parent1.wasUpdated());

		// Create a second parent shape
		ShapeMesh parentModel2 = new ShapeMesh();
		TestShape parent2 = new TestShape(parentModel2, new BasicView());
		parent2.setProperty(MeshProperty.ID, "2");

		// Change the shape's parent
		shape.setParent(parent2);

		// Clear the parents' notification states
		parent1.wasUpdated();
		parent2.wasUpdated();

		parent1.equals(parent2);

		// The parent category should contain only parent2
		assertFalse(shape.getEntitiesFromCategory(MeshCategory.PARENT)
				.contains(parent1));
		assertTrue(shape.getEntitiesFromCategory(MeshCategory.PARENT)
				.contains(parent2));

		// Trigger an updated. The old parent should not receive it, while the
		// new parent should
		shape.setProperty(MeshProperty.ID, "2");
		assertFalse(parent1.wasUpdated());
		assertTrue(parent2.wasUpdated());

		// Check that trying to change the parent through addEntityByCategory
		// does not allow adding a second parent
		shape.addEntityToCategory(parent1, MeshCategory.PARENT);
		assertFalse(shape.getEntitiesFromCategory(MeshCategory.PARENT)
				.contains(parent2));
		assertTrue(shape.getEntitiesFromCategory(MeshCategory.PARENT)
				.contains(parent1));

	}

	/**
	 * An extension of Shape that tracks whether it has received an update
	 * 
	 * @author Robert Smith
	 *
	 */
	public class TestShape extends ShapeController {

		// Whether the shape has been updated since the last time it was checked
		boolean updated = false;

		/**
		 * The defualt constructor
		 * 
		 * @param model
		 * @param view
		 */
		public TestShape(ShapeMesh model, BasicView view) {
			super(model, view);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.eavp.viz.service.modeling.AbstractController#update(org.
		 * eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable)
		 */
		@Override
		public void update(IManagedUpdateable component,
				SubscriptionType[] type) {
			updated = true;
			super.update(component, type);
		}

		/**
		 * Gets and resets the shape's updated state
		 * 
		 * @return True if the shape has received an update since the last time
		 *         this method was called. Otherwise returns false
		 */
		public boolean wasUpdated() {

			// Set the updated variable to false
			boolean temp = updated;
			updated = false;

			// Return its previous value
			return temp;
		}
	}

}
