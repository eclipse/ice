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
package org.eclipse.ice.client.widgets.reactoreditor.properties.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.reactoreditor.properties.ReactorComponentPropertySource;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.junit.Test;

/**
 * This class tests ReactorComponentPropertySource and its implementation of
 * IPropertySource.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorComponentPropertySourceTester {

	/**
	 * This class is for testing only. It provides a basic IReactorComponent
	 * implementation for testing the ReactorComponentPropertySource, which
	 * takes IReactorComponents.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	private class TestReactorComponent extends ICEObject implements
			IReactorComponent {

		/**
		 * The default constructor.
		 */
		public TestReactorComponent() {
			super();
		}

		/**
		 * Does nothing. Required to satisfy implementation of
		 * IReactorComponent.
		 */
		public void accept(IComponentVisitor visitor) {
			// Nothing to do here.
		}
	}

	/**
	 * Checks the construction of an ReactorComponentPropertySource with valid
	 * and invalid parameters.
	 */
	@Test
	public void checkConstruction() {

		// Initialize a property source.
		TestReactorComponent component;
		ReactorComponentPropertySource source;

		// Check the default name, description, and id for *null* arguments.
		source = new ReactorComponentPropertySource(null);
		assertEquals("Invalid reactor component!",
				source.getPropertyValue("name"));
		assertEquals("This is an invalid reactor component!",
				source.getPropertyValue("description"));
		assertEquals(1, source.getPropertyValue("id"));

		// Check valid constructor arguments.
		component = new TestReactorComponent();
		component.setName("A valid reactor component.");
		component.setId(14132);
		component.setDescription("Valid valid valid valid valid.");

		// Check the default property values for the valid component.
		source = new ReactorComponentPropertySource(component);
		assertEquals("A valid reactor component.",
				source.getPropertyValue("name"));
		assertEquals("Valid valid valid valid valid.",
				source.getPropertyValue("description"));
		assertEquals(14132, source.getPropertyValue("id"));

		return;
	}

	/**
	 * Checks the ability to add new SimpleProperty instances to an
	 * ReactorComponentPropertySource.
	 */
	@Test
	public void checkAddProperty() {

		// Initialize a property source.
		TestReactorComponent component = new TestReactorComponent();
		ReactorComponentPropertySource source = new ReactorComponentPropertySource(
				component);

		IPropertyDescriptor descriptor;

		// We start off with 3 properties: component name, description, and ID.
		int properties = 3;

		// Add some properties and test.

		// Add a new property. The return value should be true, and we should be
		// able to fetch the property.
		SimpleProperty property1 = new SimpleProperty("same id", "heyo",
				"Potent Potables", 1000);
		assertTrue(source.addProperty(property1));
		// Check the property value.
		assertEquals(1000, source.getPropertyValue("same id"));
		// Check the size of the property descriptors.
		assertEquals(++properties, source.getPropertyDescriptors().length);
		// Check the property descriptor. The ID is checked in the find method.
		descriptor = findPropertyDescriptor(source.getPropertyDescriptors(),
				"same id");
		assertNotNull(descriptor);
		assertEquals("heyo", descriptor.getDisplayName());
		assertEquals("Potent Potables", descriptor.getCategory());

		// Add a new property with the same ID. The return value should be false
		// and the new property shouldn't be in the Map.
		SimpleProperty property2 = new SimpleProperty("same id", "nerf",
				"Potent Potables", "orange, plastic, toy gun");
		assertFalse(source.addProperty(property2));
		// Check the size of the property descriptors. Should not have changed.
		assertEquals(properties, source.getPropertyDescriptors().length);
		// Check the property value. This should not have changed.
		assertEquals(1000, source.getPropertyValue("same id"));
		// Check the property descriptor. This should not have changed.
		descriptor = findPropertyDescriptor(source.getPropertyDescriptors(),
				"same id");
		assertNotNull(descriptor);
		assertEquals("heyo", descriptor.getDisplayName());
		assertEquals("Potent Potables", descriptor.getCategory());

		// Add a new property. The return value should be true and the property
		// should be in the Map.
		property2 = new SimpleProperty("new id", "nerf", "Potent Potables",
				"orange, plastic, toy gun");
		assertTrue(source.addProperty(property2));
		// Check the size of the property descriptors.
		assertEquals(++properties, source.getPropertyDescriptors().length);
		// Check the property value.
		assertEquals("orange, plastic, toy gun",
				source.getPropertyValue("new id"));
		// Check the property descriptor. The ID is checked in the find method.
		descriptor = findPropertyDescriptor(source.getPropertyDescriptors(),
				"new id");
		assertNotNull(descriptor);
		assertEquals("nerf", descriptor.getDisplayName());
		assertEquals("Potent Potables", descriptor.getCategory());

		// Check the default properties.
		assertEquals(component.getName(), source.getPropertyValue("name"));
		assertEquals(component.getDescription(),
				source.getPropertyValue("description"));
		assertEquals(component.getId(), source.getPropertyValue("id"));

		return;
	}

	/**
	 * Tests methods from the IPropertySource interface.
	 */
	@Test
	public void checkIPropertySource() {

		// Initialize a property source.
		TestReactorComponent component = new TestReactorComponent();
		List<SimpleProperty> properties = getBaseProperties(component);
		ReactorComponentPropertySource source = new ReactorComponentPropertySource(
				component);

		// Test getEditableValue
		assertNull(source.getEditableValue());

		// Test getPropertyDescriptors
		IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
		assertEquals(properties.size(), descriptors.length);

		// Make sure all the basic properties are correct.
		for (SimpleProperty property : properties) {

			IPropertyDescriptor descriptor = null;
			for (int i = 0; i < descriptors.length && descriptor == null; i++) {
				if (property.getId().equals(descriptors[i].getId())) {
					descriptor = descriptors[i];
				}
			}
			// Make sure a corresponding descriptor was found.
			if (descriptor == null) {
				fail();
			}

			// Check the descriptor against the expected base property's
			// descriptor.
			PropertyDescriptor baseDescriptor = property
					.getPropertyDescriptor();
			assertEquals(baseDescriptor.getId(), descriptor.getId());
			assertEquals(baseDescriptor.getDisplayName(),
					descriptor.getDisplayName());
			assertEquals(baseDescriptor.getCategory(), descriptor.getCategory());

			Object id = property.getId();
			Object value = property.getValue();

			// Test getPropertyValue
			assertEquals(value, source.getPropertyValue(id));

			// Test setPropertyValue
			source.setPropertyValue(id, "Some other value...");
			assertEquals(value, source.getPropertyValue(id));

			// Test isPropertySet
			assertFalse(source.isPropertySet(id));
			assertEquals(value, source.getPropertyValue(id));

			// Test resetPropertyValue.
			source.resetPropertyValue(id);
			assertEquals(value, source.getPropertyValue(id));
		}

		return;
	}

	/**
	 * Loops over the provided array of descriptors until one is found whose ID
	 * matches the provided ID.
	 * 
	 * @param descriptors
	 *            The array of descriptors to search.
	 * @param id
	 *            The ID of the desired property descriptor.
	 * @return The property descriptor whose ID matches the provided ID, or
	 *         {@code null} if it could not be found.
	 */
	private IPropertyDescriptor findPropertyDescriptor(
			IPropertyDescriptor[] descriptors, Object id) {
		IPropertyDescriptor descriptor = null;
		for (int i = 0; i < descriptors.length && descriptor == null; i++) {
			IPropertyDescriptor tmp = descriptors[i];
			if (id.equals(tmp.getId())) {
				descriptor = tmp;
			}
		}
		return descriptor;
	}

	/**
	 * Sets up the List of basic properties for IReactorComponents.
	 */
	private List<SimpleProperty> getBaseProperties(IReactorComponent component) {
		// Initialize the list and add the basic properties.
		List<SimpleProperty> properties = new ArrayList<SimpleProperty>();

		String category = "Basic Properties";
		properties.add(new SimpleProperty("name", "Name", category, component
				.getName()));
		properties.add(new SimpleProperty("description", "Description",
				category, component.getDescription()));
		properties.add(new SimpleProperty("id", "ID", category, component
				.getId()));

		return properties;
	}

}
