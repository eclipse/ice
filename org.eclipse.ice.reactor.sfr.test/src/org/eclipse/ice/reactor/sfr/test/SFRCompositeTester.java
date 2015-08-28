/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRComposite;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the SFRComposite class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFRCompositeTester {

	// FIXME - Removed checkComponent()

	/**
	 * <p>
	 * Tests the constructor and default values of the SFRComposite class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		SFRComposite composite = new SFRComposite();

		// SFRComposite extends SFRComponent, so we need to check the defaults
		// for SFRComponent AND the features that SFRComposite adds.

		// SFRComponent default check.
		// composite.getCurrentTime();
		// composite.getDataAtCurrentTime("feature");
		assertEquals("Composite 1's Description", composite.getDescription());
		// composite.getFeatureList();
		// composite.getFeaturesAtCurrentTime();
		assertEquals(1, composite.getId());
		assertEquals("Composite 1", composite.getName());
		// composite.getNumberOfTimeSteps();
		// composite.getSourceInfo();
		// composite.getTimes();
		// composite.getTimeStep(0.0);
		// composite.getTimeUnit();
		// composite.getTimeUnits();

		// SFRComposite default check. (We initialize the list of components!)
		// composite.getComponent("name");
		// composite.getComponent(0);
		assertNotNull(composite.getComponentNames());
		assertTrue(composite.getComponentNames().isEmpty());
		assertNotNull(composite.getComponents());
		assertTrue(composite.getComponents().isEmpty());
		assertEquals(0, composite.getNumberOfComponents());

		return;
	}

	/**
	 * <p>
	 * Tests the addition and removal of components from the SFRComposite.
	 * </p>
	 * 
	 */
	@Test
	public void checkComponentAddRem() {

		// Initialize a Composite for testing.
		SFRComposite composite = new SFRComposite();

		Component component;
		ArrayList<Component> components;
		ArrayList<String> componentNames;

		/* ---- Make sure the list of components is empty. ---- */
		// This method should return zero.
		assertEquals(0, composite.getNumberOfComponents());

		// The component list should be empty.
		components = composite.getComponents();
		assertNotNull(components);
		assertTrue(components.isEmpty());

		// The component name list should also be empty.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertTrue(componentNames.isEmpty());
		/* ---------------------------------------------------- */

		/* ---- Add a single component and check getting and removing. ---- */
		SFRComponent defaultComponent = new SFRComponent();
		String defaultName = defaultComponent.getName();
		int defaultId = defaultComponent.getId();

		// Make sure defaultComponent is not in the list yet.
		assertNull(composite.getComponent(defaultName));

		// Add defaultComponent to the list.
		composite.addComponent(defaultComponent);

		/* -- Make sure defaultComponent is the only one in the list. -- */
		// Check getComponent(int childId)
		component = composite.getComponent(defaultId);
		assertNotNull(component);
		assertSame(defaultComponent, component);

		// Check getComponent(String name)
		component = composite.getComponent(defaultName);
		assertNotNull(component);
		assertSame(defaultComponent, component);

		// Check the component list.
		components = composite.getComponents();
		assertNotNull(components);
		assertEquals(1, components.size());
		assertSame(defaultComponent, components.get(0));

		// Check the component names list.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertEquals(1, componentNames.size());
		assertEquals(defaultName, componentNames.get(0));
		/* -- ------------------------------------------------------- -- */

		// Remove the component with removeComponent(int childId).
		composite.removeComponent(defaultId);

		// The component list should be empty.
		components = composite.getComponents();
		assertNotNull(components);
		assertTrue(components.isEmpty());

		// The component name list should also be empty.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertTrue(componentNames.isEmpty());
		assertNull(composite.getComponent(defaultId));
		assertNull(composite.getComponent(defaultName));

		// Re-add the component.
		composite.addComponent(defaultComponent);

		/* -- Make sure defaultComponent is the only one in the list. -- */
		// Check getComponent(int childId)
		component = composite.getComponent(defaultId);
		assertNotNull(component);
		assertSame(defaultComponent, component);

		// Check getComponent(String name)
		component = composite.getComponent(defaultName);
		assertNotNull(component);
		assertSame(defaultComponent, component);

		// Check the component list.
		components = composite.getComponents();
		assertNotNull(components);
		assertEquals(1, components.size());
		assertSame(defaultComponent, components.get(0));

		// Check the component names list.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertEquals(1, componentNames.size());
		assertEquals(defaultName, componentNames.get(0));
		/* -- ------------------------------------------------------- -- */

		// Remove the component with removeComponent(String name).
		composite.removeComponent(defaultName);

		// The component list should be empty.
		components = composite.getComponents();
		assertNotNull(components);
		assertTrue(components.isEmpty());

		// The component name list should also be empty.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertTrue(componentNames.isEmpty());
		assertNull(composite.getComponent(defaultId));
		assertNull(composite.getComponent(defaultName));

		/* ---------------------------------------------------------------- */

		/* ---- Make sure the list of components is empty. ---- */
		// This method should return zero.
		assertEquals(0, composite.getNumberOfComponents());

		// The component list should be empty.
		components = composite.getComponents();
		assertNotNull(components);
		assertTrue(components.isEmpty());

		// The component name list should also be empty.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertTrue(componentNames.isEmpty());
		/* ---------------------------------------------------- */

		/* ---- Add multiple components and check getting and removing. ---- */
		SFRComponent component1 = new SFRComponent("Leonardo");
		SFRComponent component2 = new SFRComponent("Raphael");
		SFRComponent component3 = new SFRComponent("Donatello");
		SFRComponent component4 = new SFRComponent("Michelangelo");

		// Set their IDs to non-default IDs, otherwise who knows what
		// composite.getComponent(int childId) will return.
		component1.setId(10);
		component2.setId(20);
		component3.setId(30);
		component4.setId(40);

		// Add all four components.
		composite.addComponent(component1);
		composite.addComponent(component2);
		composite.addComponent(component3);
		composite.addComponent(component4);

		/* -- Make sure all four components are in the list. -- */
		// Check getComponent(int childId)
		component = composite.getComponent(component1.getName());
		assertNotNull(component);
		assertSame(component1, component);

		// Check getComponent(String name)
		component = composite.getComponent("Raphael");
		assertNotNull(component);
		assertSame(component2, component);

		// Check getComponent(int childId)
		component = composite.getComponent(component3.getId());
		assertNotNull(component);
		assertSame(component3, component);

		// Check getComponent(String name)
		component = composite.getComponent("Michelangelo");
		assertNotNull(component);
		assertSame(component4, component);

		// Check the component list.
		components = composite.getComponents();
		assertNotNull(components);
		assertEquals(4, components.size());
		assertTrue(components.contains(component1));
		assertTrue(components.contains(component2));
		assertTrue(components.contains(component3));
		assertTrue(components.contains(component4));

		// Check the component names list.
		componentNames = composite.getComponentNames();
		assertNotNull(componentNames);
		assertEquals(4, componentNames.size());
		assertTrue(componentNames.contains("Leonardo"));
		assertTrue(componentNames.contains("Raphael"));
		assertTrue(componentNames.contains("Donatello"));
		assertTrue(componentNames.contains("Michelangelo"));
		/* -- ---------------------------------------------- -- */
		/* ----------------------------------------------------------------- */

		/* ---- Check adding/getting/removing nulls. ---- */
		composite.addComponent(null);

		// Make sure the components haven't changed.
		assertEquals(4, composite.getNumberOfComponents());
		assertSame(component1, composite.getComponent("Leonardo"));
		assertSame(component2, composite.getComponent("Raphael"));
		assertSame(component3, composite.getComponent("Donatello"));
		assertSame(component4, composite.getComponent("Michelangelo"));

		assertNull(composite.getComponent(null));
		composite.removeComponent(null);

		// Make sure the components haven't changed.
		assertEquals(4, composite.getNumberOfComponents());
		assertSame(component1, composite.getComponent("Leonardo"));
		assertSame(component2, composite.getComponent("Raphael"));
		assertSame(component3, composite.getComponent("Donatello"));
		assertSame(component4, composite.getComponent("Michelangelo"));
		/* ---------------------------------------------- */

		/* ---- Check getting/removing invalid IDs. ---- */
		assertNull(composite.getComponent(1984));
		composite.removeComponent(1984);

		// Make sure the components haven't changed.
		assertEquals(4, composite.getNumberOfComponents());
		assertSame(component1, composite.getComponent("Leonardo"));
		assertSame(component2, composite.getComponent("Raphael"));
		assertSame(component3, composite.getComponent("Donatello"));
		assertSame(component4, composite.getComponent("Michelangelo"));
		/* --------------------------------------------- */

		/* ---- Check getting/removing invalid Strings. ---- */
		assertNull(composite.getComponent("Shredder"));
		composite.removeComponent("Shredder");

		// Make sure the components haven't changed.
		assertEquals(4, composite.getNumberOfComponents());
		assertSame(component1, composite.getComponent("Leonardo"));
		assertSame(component2, composite.getComponent("Raphael"));
		assertSame(component3, composite.getComponent("Donatello"));
		assertSame(component4, composite.getComponent("Michelangelo"));
		/* ------------------------------------------------- */

		/* ---- Check adding a component with a used name. ---- */
		component = new SFRComponent("Donatello");

		// Make absolutely sure there are two "Donatello" components.
		assertNotSame(component, component3);

		// Add the component.
		composite.addComponent(component);

		// We shouldn't get a result because there's already a component with
		// the name "Donatello".
		assertNull(composite.getComponent(component.getId()));
		assertNotSame(component, composite.getComponent("Donatello"));

		// Make sure the components haven't changed.
		assertEquals(4, composite.getNumberOfComponents());
		assertSame(component1, composite.getComponent("Leonardo"));
		assertSame(component2, composite.getComponent("Raphael"));
		assertSame(component3, composite.getComponent("Donatello"));
		assertSame(component4, composite.getComponent("Michelangelo"));
		/* ---------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		// Initialize objects for testing.
		SFRComposite object = new SFRComposite();
		SFRComposite equalObject = new SFRComposite();
		SFRComposite unequalObject = new SFRComposite();

		// Set up the object and equalObject.
		object.setName("Earth");
		object.setDescription("Mostly Harmless");
		object.setSourceInfo("Mice made it");
		object.addComponent(new SFRComponent("Arthur"));
		object.addComponent(new SFRComponent("Trillian"));

		equalObject.setName("Earth");
		equalObject.setDescription("Mostly Harmless");
		equalObject.setSourceInfo("Mice made it");
		equalObject.addComponent(new SFRComponent("Arthur"));
		equalObject.addComponent(new SFRComponent("Trillian"));

		// Set up the unequalObject.
		unequalObject.setName("Earth");
		unequalObject.setDescription("Mostly Harmless");
		unequalObject.setSourceInfo("Mice made it");
		unequalObject.addComponent(new SFRComponent("Arthur"));

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object==null);
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());
		assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
	}

	/**
	 * <p>
	 * Tests the copying and cloning operations.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		// Initialize objects for testing.
		SFRComposite object = new SFRComposite();
		SFRComposite copy = new SFRComposite();
		SFRComposite clone = null;

		// Set up the object.
		object.setName("Earth");
		object.setDescription("Mostly Harmless");
		object.setSourceInfo("Mice made it");
		object.addComponent(new SFRComponent("Arthur"));
		object.addComponent(new SFRComponent("Trillian"));

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

		// Copy the object.
		clone = (SFRComposite) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}