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
package org.eclipse.ice.client.widgets.reactoreditor.sfr.properties.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.properties.PropertySourceFactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.junit.Test;

/**
 * This class tests the output IPropertySource from the PropertySourceFactory
 * for SFRComponents.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PropertySourceFactoryTester {

	/**
	 * Sets up the List of basic properties for SFRComponents.
	 */
	private List<SimpleProperty> getBaseProperties(SFRComponent component) {
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
	 * Checks getPropertySource for base SFRComponents.
	 */
	@Test
	public void checkSFRComponent() {
		// begin-user-code

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		SFRComponent component = new SFRComponent();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Nothing to add... yet.

		// Checks all of the properties, their descriptors, and their values.
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
		// end-user-code
	}

	/**
	 * Checks getPropertySource for SFReactors.
	 */
	@Test
	public void checkSFReactor() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		SFReactor component = new SFReactor(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		String category = "Dimensions";
		properties.add(new SimpleProperty("SFReactor.size",
				"Size (diameter, # of assemblies)", category, component
						.getSize()));
		properties.add(new SimpleProperty("SFReactor.latticePitch",
				"Lattice Pitch", category, component.getLatticePitch()));
		properties
				.add(new SimpleProperty("SFReactor.outerFlatToFlat",
						"Outer Flat-to-Flat", category, component
								.getOuterFlatToFlat()));

		// Checks all of the properties, their descriptors, and their values.
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
	 * Checks getPropertySource for SFRAssemblies.
	 */
	@Test
	public void checkSFRAssembly() {
		// begin-user-code

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		SFRAssembly component = new SFRAssembly(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Nothing to add... yet.

		// Checks all of the properties, their descriptors, and their values.
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
		// end-user-code
	}

	/**
	 * Checks getPropertySource for PinAssembly.
	 */
	@Test
	public void checkPinAssembly() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		PinAssembly component = new PinAssembly(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add dimension properties.
		String category = "Dimensions";
		properties.add(new SimpleProperty("PinAssembly.size",
				"Size (diameter, # of pins)", category, component.getSize()));
		properties.add(new SimpleProperty("PinAssembly.ductThickness",
				"Duct Thickness", category, component.getDuctThickness()));
		properties.add(new SimpleProperty("PinAssembly.innerDuctThickness",
				"Inner Duct Thickness", category, component
						.getInnerDuctThickness()));
		properties.add(new SimpleProperty("PinAssembly.innerDuctFlatToFlat",
				"Inner Duct Flat-to-Flat", category, component
						.getInnerDuctFlatToFlat()));
		properties.add(new SimpleProperty("PinAssembly.pitch", "Pin Pitch",
				category, component.getPinPitch()));

		// Add other properties.
		category = "Other";
		properties.add(new SimpleProperty("PinAssembly.type", "Assembly Type",
				category, component.getAssemblyType().toString()));
		properties.add(new SimpleProperty("PinAssembly.pinType", "Pin Type",
				category, component.getPinType().toString()));

		// Checks all of the properties, their descriptors, and their values.
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
	 * Checks getPropertySource for ReflectorAssembly.
	 */
	@Test
	public void checkReflectorAssembly() {
		// begin-user-code

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		ReflectorAssembly component = new ReflectorAssembly(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Nothing to add... yet.

		// Checks all of the properties, their descriptors, and their values.
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
		// end-user-code
	}

	/**
	 * Checks getPropertySource for SFRPin.
	 */
	@Test
	public void checkSFRPin() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		SFRPin component = new SFRPin();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add fill gas properties.
		String category = "Fill Gas";
		properties.add(new SimpleProperty("SFRPin.fillGasName", "Name",
				category, component.getFillGas().getName()));
		properties.add(new SimpleProperty("SFRPin.fillGasDesc", "Description",
				category, component.getFillGas().getDescription()));

		// Add cladding properties.
		category = "Cladding";
		properties.add(new SimpleProperty("SFRPin.cladName", "Name", category,
				component.getCladding().getMaterial().getName()));
		properties.add(new SimpleProperty("SFRPin.cladDesc", "Description",
				category, component.getCladding().getMaterial()
						.getDescription()));

		// Add other properties.
		category = "Other";
		properties.add(new SimpleProperty("SFRPin.nBlocks",
				"# of Material Blocks", category, component.getMaterialBlocks()
						.size()));

		// Checks all of the properties, their descriptors, and their values.
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
	 * Checks getPropertySource for SFRRods.
	 */
	@Test
	public void checkSFRRod() {
		// begin-user-code

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		SFRRod component = new SFRRod();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Nothing to add... yet.

		// Checks all of the properties, their descriptors, and their values.
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
		// end-user-code
	}

	/**
	 * Checks getPropertySource for MaterialBlocks.
	 */
	@Test
	public void checkMaterialBlock() {
		// begin-user-code

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		MaterialBlock component = new MaterialBlock();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Nothing to add... yet.

		// Checks all of the properties, their descriptors, and their values.
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
		// end-user-code
	}

	/**
	 * Checks getPropertySource for Materials.
	 */
	@Test
	public void checkMaterials() {
		// begin-user-code

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		Material component = new Material();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Nothing to add... yet.

		// Checks all of the properties, their descriptors, and their values.
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
		// end-user-code
	}

	/**
	 * Checks getPropertySource for Ring.
	 */
	@Test
	public void checkRing() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		Ring component = new Ring();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add dimension properties.
		String category = "Dimensions";
		properties.add(new SimpleProperty("Ring.innerRadius", "Inner Radius",
				category, component.getInnerRadius()));
		properties.add(new SimpleProperty("Ring.outerRadius", "Outer Radius",
				category, component.getOuterRadius()));
		properties.add(new SimpleProperty("Ring.height", "Height", category,
				component.getHeight()));

		// Add Material properties.
		category = "Composition";
		properties.add(new SimpleProperty("Ring.materialName", "Name",
				category, component.getMaterial().getName()));
		properties.add(new SimpleProperty("Ring.materialDesc", "Description",
				category, component.getMaterial().getDescription()));

		// Checks all of the properties, their descriptors, and their values.
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

}
