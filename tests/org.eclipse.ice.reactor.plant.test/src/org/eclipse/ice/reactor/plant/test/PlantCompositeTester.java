/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.reactor.plant.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.reactor.plant.IPlantCompositeListener;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.SelectivePlantComponentVisitor;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests PlantComposite, which is a Composite container for multiple
 * PlantComponents.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantCompositeTester {

	/**
	 * <p>
	 * Boolean flag to mark if the PlantComponent was successfully visited.
	 * </p>
	 * 
	 */
	private boolean wasVisited = false;

	/**
	 * Checks the construction of a PlantComposite. Its properties inherited
	 * from PlantComponent should be their defaults, and its child component
	 * lists should be empty and not null.
	 */
	@Test
	public void checkConstruction() {

		// Create a composite to test.
		PlantComposite composite = new PlantComposite();

		// Check the name, ID, and description.
		assertEquals("Plant Composite 1", composite.getName());
		assertEquals(1, composite.getId());
		assertEquals("Container for plant-level reactor components",
				composite.getDescription());

		// The current set of components in the Composite should be empty.
		assertEquals(0, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertTrue(composite.getComponents().isEmpty());
		assertNotNull(composite.getPlantComponents());
		assertTrue(composite.getPlantComponents().isEmpty());

		return;
	}

	/**
	 * Checks the ability to add PlantComponents to the Composite.
	 */
	@Test
	public void checkCompositeImplementation() {

		// Create a PlantComposite for testing.
		PlantComposite composite = new PlantComposite();

		// Create a PlantComponent for adding to the composite.
		PlantComponent component = new PlantComponent("snake eyes");

		// ---- addComponent should do nothing. ---- //
		composite.addComponent(component);

		// None of the lists should contain components.
		assertEquals(0, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertTrue(composite.getComponents().isEmpty());
		assertNotNull(composite.getPlantComponents());
		assertTrue(composite.getPlantComponents().isEmpty());

		// ---- addPlantComponent should add a PlantComponent. ---- //
		composite.addPlantComponent(component);

		// There should be one component.
		assertEquals(1, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertEquals(1, composite.getComponents().size());
		assertNotNull(composite.getPlantComponents());
		assertEquals(1, composite.getPlantComponents().size());

		// We should be able to get the same component out.
		assertSame(component, composite.getComponent(component.getId()));
		assertSame(component, composite.getPlantComponent(component.getId()));

		// Adding a duplicate component should make no difference.
		composite.addPlantComponent(component);

		// There should be one component.
		assertEquals(1, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertEquals(1, composite.getComponents().size());
		assertNotNull(composite.getPlantComponents());
		assertEquals(1, composite.getPlantComponents().size());

		// We should be able to get the same component out.
		assertSame(component, composite.getComponent(component.getId()));
		assertSame(component, composite.getPlantComponent(component.getId()));

		// Adding a different component with the same ID should not work.
		composite.addPlantComponent(new PlantComponent("stormshadow"));

		// There should be one component.
		assertEquals(1, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertEquals(1, composite.getComponents().size());
		assertNotNull(composite.getPlantComponents());
		assertEquals(1, composite.getPlantComponents().size());

		// We should be able to get the same component out.
		assertSame(component, composite.getComponent(component.getId()));
		assertSame(component, composite.getPlantComponent(component.getId()));

		// ---- removeComponent should remove a PlantComponent. ---- //
		composite.removeComponent(component.getId());

		// None of the lists should contain components.
		assertEquals(0, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertTrue(composite.getComponents().isEmpty());
		assertNotNull(composite.getPlantComponents());
		assertTrue(composite.getPlantComponents().isEmpty());

		// We should no longer be able to get the same component out.
		assertNull(composite.getComponent(component.getId()));
		assertNull(composite.getPlantComponent(component.getId()));

		return;
	}

	/**
	 * Checks that registered IPlantCompositeListeners are notified correctly
	 * when PlantComponents are added or removed from the PlantComposite.
	 */
	@Test
	public void checkCompositeListeners() {

		// Create a PlantComposite for testing.
		PlantComposite composite = new PlantComposite();

		// Create a listener for testing the IPlantCompositeListener interface.
		TestPlantCompositeListener listener = new TestPlantCompositeListener();
		composite.registerPlantCompositeListener(listener);

		// Create a PlantComponent for adding to the composite.
		PlantComponent component = new PlantComponent("roadblock");

		// ---- addComponent should do nothing. ---- //
		composite.addComponent(component);

		// None of the lists should contain components.
		assertEquals(0, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertTrue(composite.getComponents().isEmpty());
		assertNotNull(composite.getPlantComponents());
		assertTrue(composite.getPlantComponents().isEmpty());

		// ---- addPlantComponent should add a PlantComponent. ---- //
		composite.addPlantComponent(component);

		// There should be one component.
		assertEquals(1, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertEquals(1, composite.getComponents().size());
		assertNotNull(composite.getPlantComponents());
		assertEquals(1, composite.getPlantComponents().size());

		// We should be able to get the same component out.
		assertSame(component, composite.getComponent(component.getId()));
		assertSame(component, composite.getPlantComponent(component.getId()));

		// The listener should have been notified of the added component.
		assertTrue(listener.wasNotified());
		assertSame(component, listener.getAddedComponents().get(0));
		listener.reset();

		// Adding a duplicate component should make no difference.
		composite.addPlantComponent(component);

		// There should still be one component.
		assertEquals(1, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertEquals(1, composite.getComponents().size());
		assertNotNull(composite.getPlantComponents());
		assertEquals(1, composite.getPlantComponents().size());

		// We should be able to get the same component out.
		assertSame(component, composite.getComponent(component.getId()));
		assertSame(component, composite.getPlantComponent(component.getId()));

		// The listener should not have been notified.
		assertFalse(listener.wasNotified());
		listener.reset();

		// Adding a different component with the same ID should not work.
		composite.addPlantComponent(new PlantComponent("stormshadow"));

		// There should be one component.
		assertEquals(1, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertEquals(1, composite.getComponents().size());
		assertNotNull(composite.getPlantComponents());
		assertEquals(1, composite.getPlantComponents().size());

		// We should be able to get the same component out.
		assertSame(component, composite.getComponent(component.getId()));
		assertSame(component, composite.getPlantComponent(component.getId()));

		// ---- removeComponent should remove a PlantComponent. ---- //
		composite.removeComponent(component.getId());

		// None of the lists should contain components.
		assertEquals(0, composite.getNumberOfComponents());
		assertNotNull(composite.getComponents());
		assertTrue(composite.getComponents().isEmpty());
		assertNotNull(composite.getPlantComponents());
		assertTrue(composite.getPlantComponents().isEmpty());

		// We should no longer be able to get the same component out.
		assertNull(composite.getComponent(component.getId()));
		assertNull(composite.getPlantComponent(component.getId()));

		// The listener should have been notified of the removed component.
		assertTrue(listener.wasNotified());
		assertSame(component, listener.getRemovedComponents().get(0));
		listener.reset();

		return;
	}

	/**
	 * Checks equality and hash codes for PlantComposites. All child components
	 * from two composites should be compared.
	 */
	@Test
	public void checkEquality() {

		// Initialize objects for testing.
		PlantComposite object = new PlantComposite();
		PlantComposite equalObject = new PlantComposite();

		// Set up the two objects.
		object.addPlantComponent(new PlantComponent("optimus"));
		equalObject.addPlantComponent(new PlantComponent("optimus"));

		// Make sure the references are different but the objects are equal.
		assertFalse(object == equalObject);
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check the hash codes.
		assertEquals(object.hashCode(), equalObject.hashCode());

		// ---- Check inequality. ---- //
		// Create an unequal object.
		PlantComposite unequalObject = equalObject;
		unequalObject.getComponents().get(0).setName("megatron");

		// Make sure the references are different and the objects unequal.
		assertFalse(object == equalObject);
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertFalse(object.hashCode() == unequalObject.hashCode());

		// Try other invalid objects.
		assertFalse(object == null);
		assertFalse("starscream".equals(object));

		return;
	}

	/**
	 * Checks the copy and clone methods for PlantComposites.
	 */
	@Test
	public void checkCopying() {

		// Initialize objects for testing.
		PlantComposite object = new PlantComposite();
		PlantComposite copy = new PlantComposite();
		PlantComposite clone = null;

		// Set up the object.
		object.addPlantComponent(new PlantComponent("bumblebee"));

		// Make sure the objects are not equal before copying.
		assertFalse(object == copy);
		assertFalse(object.equals(copy));

		// Copy the object.
		copy.copy(object);

		// Make sure the references are different but contents the same.
		assertFalse(object == copy);
		assertTrue(object.equals(copy));

		// Do the same for the clone operation.

		// Make sure the objects are not equal before copying.
		assertFalse(object == clone);
		assertFalse(object.equals(clone));

		// Clone the object.
		clone = (PlantComposite) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}

	/**
	 * Checks XML persistence for PlantComposites.
	 */
	@Ignore
	@Test
	public void checkPersistence() {
		fail("Not implemented");

		return;
	}

	@Test
	public void checkVisitation() {

		// Create a new component to visit.
		PlantComposite component = new PlantComposite();

		// Create an invalid visitor, and try to visit the component.
		FakeComponentVisitor visitor = null;
		component.accept(visitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		visitor = new FakeComponentVisitor();
		component.accept(visitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		Component visitorComponent = visitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(component == visitorComponent);
		assertTrue(component.equals(visitorComponent));

		// ---- Check PlantComponent visitation. ---- //
		wasVisited = false;
		
		// Create an invalid visitor, and try to visit the component.
		FakePlantComponentVisitor plantVisitor = null;
		component.accept(plantVisitor);

		// Check that the component wasn't visited yet.
		assertFalse(wasVisited);

		// Create a valid visitor, and try to visit the component.
		plantVisitor = new FakePlantComponentVisitor();
		component.accept(plantVisitor);

		// Check that the component was visited.
		assertTrue(wasVisited);

		// Grab the visitor's visited component.
		PlantComponent visitorPlantComponent = plantVisitor.component;

		// Check that the visitor's component is the same component we initially
		// created.
		assertTrue(component == visitorPlantComponent);
		assertTrue(component.equals(visitorPlantComponent));
		
		return;
	}

	/**
	 * <p>
	 * Fake class to test the visitation routine of the component.
	 * </p>
	 * 
	 * @author w5q
	 */
	private class FakeComponentVisitor extends SelectiveComponentVisitor {

		// The fake visitor's visited component.
		private IReactorComponent component = null;
		
		@Override
		public void visit(IReactorComponent component) {

			// Set the IComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (component != null) {
				this.component = component;
				wasVisited = true;
			}
			return;
		}
	};

	/**
	 * Fake class to test the PlantComponent visitation routine.
	 * 
	 * @author Jordan
	 * 
	 */
	private class FakePlantComponentVisitor extends
			SelectivePlantComponentVisitor {

		// The fake visitor's visited component.
		private PlantComponent component = null;
		
		@Override
		public void visit(PlantComposite plantComp) {
			// Set the IComponentVisitor component (if valid), and flag the
			// component as having been visited.
			if (plantComp != null) {
				this.component = plantComp;
				wasVisited = true;
			}
			return;
		}
	}

	/**
	 * This listener is a test IJunctionListener. It should be reset between
	 * tests, and wasNotifed() can be used as a way to determine if it has been
	 * notified within 250ms.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class TestPlantCompositeListener implements IPlantCompositeListener {

		private List<PlantComponent> added = new ArrayList<PlantComponent>();
		private List<PlantComponent> removed = new ArrayList<PlantComponent>();

		private final AtomicBoolean notified = new AtomicBoolean(false);

		/**
		 * Whether or not the listener was notified. Expires after 250ms. It
		 * returns within 10ms of being notified.
		 * 
		 * @return True if the listener was notified within 250ms, false
		 *         otherwise.
		 */
		public boolean wasNotified() {

			int counter = 0;

			while (!notified.get() && ++counter < 250) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					fail("PlantCompositeTester error: "
							+ "Thread interrupted during IPlantCompositeListener test.");
				}
			}

			return notified.get();
		}

		public void reset() {
			added.clear();
			removed.clear();
			notified.set(false);
		}

		public List<PlantComponent> getAddedComponents() {
			return added;
		}

		public List<PlantComponent> getRemovedComponents() {
			return removed;
		}

		public void addedComponents(PlantComposite composite,
				List<PlantComponent> components) {
			added = components;
			notified.set(true);
		}

		public void removedComponents(PlantComposite composite,
				List<PlantComponent> components) {
			removed = components;
			notified.set(true);
		}

	}
}
