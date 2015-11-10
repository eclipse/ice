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
package org.eclipse.ice.viz.service.modeling.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;
import org.junit.Test;

/**
 * A class for testing Shape.
 * 
 * @author Robert Smith
 *
 */
public class ShapeComponentTester {

	/**
	 * Check the special behavior for parent entities
	 */
	@Test
	public void checkParent() {

		// Create a shape to test
		ShapeComponent shapeModel = new ShapeComponent();
		TestShape shape = new TestShape(shapeModel, new AbstractView());

		// Create a parent shape
		ShapeComponent parentModel1 = new ShapeComponent();
		TestShape parent1 = new TestShape(parentModel1, new AbstractView());

		// Set the shape's parent
		shape.setParent(parent1);

		// Clear the parent's notification state
		parent1.wasUpdated();

		// The parent category should now contain the parent
		assertTrue(shape.getEntitiesByCategory("Parent").contains(parent1));

		// Trigger an update from the shape and check that the parent received
		// it
		shape.setProperty("Id", "1");
		assertTrue(parent1.wasUpdated());

		// Create a second parent shape
		TestShape parent2 = new TestShape(parentModel1, new AbstractView());

		// Change the shape's parent
		shape.setParent(parent2);

		// Clear the parents' notification states
		parent1.wasUpdated();
		parent2.wasUpdated();

		// The parent category should contain only parent2
		assertFalse(shape.getEntitiesByCategory("Parent").contains(parent1));
		assertTrue(shape.getEntitiesByCategory("Parent").contains(parent2));

		// Trigger an updated. The old parent should not receive it, while the
		// new parent should
		shape.setProperty("Id", "2");
		assertFalse(parent1.wasUpdated());
		assertTrue(parent2.wasUpdated());

		// Check that trying to change the parent through addEntityByCategory
		// does not allow adding a second parent
		shape.addEntityByCategory(parent1, "Parent");
		assertFalse(shape.getEntitiesByCategory("Parent").contains(parent2));
		assertTrue(shape.getEntitiesByCategory("Parent").contains(parent1));

	}

	/**
	 * An extension of Shape that tracks whether it has received an update
	 * 
	 * @author Robert Smith
	 *
	 */
	public class TestShape extends Shape {

		// Whether the shape has been updated since the last time it was checked
		boolean updated = false;

		/**
		 * The defualt constructor
		 * 
		 * @param model
		 * @param view
		 */
		public TestShape(ShapeComponent model, AbstractView view) {
			super(model, view);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ice.viz.service.modeling.AbstractController#update(org.
		 * eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable)
		 */
		@Override
		public void update(IVizUpdateable component) {
			updated = true;
			super.update(component);
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
