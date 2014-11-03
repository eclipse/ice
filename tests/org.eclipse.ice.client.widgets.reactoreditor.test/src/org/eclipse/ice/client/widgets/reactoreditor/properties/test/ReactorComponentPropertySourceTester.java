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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.eclipse.ice.client.widgets.reactoreditor.properties.ReactorComponentPropertySource;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.junit.Test;

/**
 * This class tests ReactorComponentPropertySource and its implementation of
 * IPropertySource.
 * 
 * @author djg
 * 
 */
public class ReactorComponentPropertySourceTester {

	/**
	 * This class is for testing only. It provides a basic IReactorComponent
	 * implementation for testing the ReactorComponentPropertySource, which
	 * takes IReactorComponents.
	 * 
	 * @author djg
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

	/**
	 * Checks the construction of an ReactorComponentPropertySource with valid
	 * and invalid parameters.
	 */
	@Test
	public void checkConstruction() {

		// Initialize a property source.
		TestReactorComponent component;
		List<SimpleProperty> properties;
		Map<String, SimpleProperty> propertyMap;
		ReactorComponentProperties source;

		// Check invalid constructor arguments.
		component = new TestReactorComponent();
		component.setName("Invalid reactor component!");
		component.setDescription("This is an invalid reactor component!");
		properties = getBaseProperties(component);

		source = new ReactorComponentProperties(null);
		propertyMap = source.getPropertyMap();

		// Make sure all the basic properties are correct.
		for (SimpleProperty property : properties) {
			SimpleProperty p = propertyMap.get(property.getId());
			assertNotNull(p);

			assertEquals(property.getId(), p.getId());
			assertEquals(property.getValue(), p.getValue());

			PropertyDescriptor descriptor = property.getPropertyDescriptor();
			PropertyDescriptor d = p.getPropertyDescriptor();

			assertEquals(descriptor.getId(), d.getId());
			assertEquals(descriptor.getDisplayName(), d.getDisplayName());
			assertEquals(descriptor.getCategory(), d.getCategory());
		}

		// Check valid constructor arguments.
		component = new TestReactorComponent();
		component.setName("A valid reactor component.");
		component.setId(14132);
		component.setDescription("Valid valid valid valid valid.");
		properties = getBaseProperties(component);

		source = new ReactorComponentProperties(component);
		propertyMap = source.getPropertyMap();

		// Make sure all the basic properties are correct.
		for (SimpleProperty property : properties) {
			SimpleProperty p = propertyMap.get(property.getId());
			assertNotNull(p);

			assertEquals(property.getId(), p.getId());
			assertEquals(property.getValue(), p.getValue());

			PropertyDescriptor descriptor = property.getPropertyDescriptor();
			PropertyDescriptor d = p.getPropertyDescriptor();

			assertEquals(descriptor.getId(), d.getId());
			assertEquals(descriptor.getDisplayName(), d.getDisplayName());
			assertEquals(descriptor.getCategory(), d.getCategory());
		}

		return;
	}

	/**
	 * Checks the ability to add new SimpleProperty instances to an
	 * ReactorComponentPropertySource.
	 */
	@Test
	public void checkAddProperty() {

		// Initialize a property source.
		TestReactorComponent component;
		List<SimpleProperty> properties;
		Map<String, SimpleProperty> propertyMap;
		ReactorComponentProperties source;

		component = new TestReactorComponent();
		properties = getBaseProperties(component);

		source = new ReactorComponentProperties(component);
		propertyMap = source.getPropertyMap();

		// Add some properties and test.

		// Add a new property. The return value should be true and the property
		// should be in the Map.
		SimpleProperty property1 = new SimpleProperty("same id", "heyo",
				"Potent Potables", 1000);
		assertTrue(source.addProperty(property1));
		assertSame(property1, propertyMap.get(property1.getId()));

		// Add a new property with the same ID. The return value should be false
		// and the new property shouldn't be in the Map.
		SimpleProperty property2 = new SimpleProperty("same id", "nerf",
				"Potent Potables", "orange, plastic, toy gun");
		assertFalse(source.addProperty(property2));
		assertSame(property1, propertyMap.get(property2.getId()));

		// Add a new property. The return value should be true and the property
		// should be in the Map.
		property2 = new SimpleProperty("new id", "nerf", "Potent Potables",
				"orange, plastic, toy gun");
		assertTrue(source.addProperty(property2));
		assertSame(property2, propertyMap.get(property2.getId()));

		// Check all the properties from getPropertyDescriptors.
		// Make sure all the basic properties are correct.
		properties.add(property1);
		properties.add(property2);
		IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
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

			// Check the value.
			assertEquals(value, source.getPropertyValue(id));
		}

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
	 * This class is for testing only. It exposes the underlying Map of
	 * SimpleProperties from ReactorComponentPropertySource.
	 * 
	 * @author djg
	 * 
	 */
	private class ReactorComponentProperties extends
			ReactorComponentPropertySource {

		/**
		 * The default constructor.
		 * 
		 * @param component
		 *            The IReactorComponent to pass to the
		 *            ReactorComponentPropertySource.
		 */
		public ReactorComponentProperties(IReactorComponent component) {
			super(component);

			return;
		}

		/**
		 * Gets the Map of SimpleProperty instances keyed on their ID.
		 * 
		 * @return A Map of SimpleProperty instances.
		 */
		public Map<String, SimpleProperty> getPropertyMap() {
			return properties;
		}
	}
}
