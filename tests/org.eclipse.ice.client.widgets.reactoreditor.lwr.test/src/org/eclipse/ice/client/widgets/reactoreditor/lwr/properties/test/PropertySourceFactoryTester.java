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
package org.eclipse.ice.client.widgets.reactoreditor.lwr.properties.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;
import org.eclipse.ice.client.widgets.reactoreditor.lwr.properties.PropertySourceFactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.junit.Test;

/**
 * This class tests the output IPropertySource from the PropertySourceFactory
 * for LWRComponents.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PropertySourceFactoryTester {

	/**
	 * Sets up the List of basic properties for LWRComponents.
	 */
	private List<SimpleProperty> getBaseProperties(LWRComponent component) {
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
	 * Checks getPropertySource for base LWRComponents.
	 */
	@Test
	public void checkLWRComponent() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		LWRComponent component = new LWRComponent();

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
	}

	/**
	 * Checks getPropertySource for PressurizedWaterReactors.
	 */
	@Test
	public void checkPressurizedWaterReactor() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		PressurizedWaterReactor component = new PressurizedWaterReactor(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add dimension properties.
		String category = "Dimensions";
		properties
				.add(new SimpleProperty("PWReactor.size",
						"Size (length, # of assemblies)", category, component
								.getSize()));

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
	 * Checks getPropertySource for BWReactors.
	 */
	@Test
	public void checkBWReactor() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		BWReactor component = new BWReactor(1);

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
	}

	/**
	 * Checks getPropertySource for FuelAssemblies.
	 */
	@Test
	public void checkFuelAssembly() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		FuelAssembly component = new FuelAssembly(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add dimension properties.
		String category = "Dimensions";
		properties.add(new SimpleProperty("FuelAssembly.size",
				"Size (length, # of rods)", category, component.getSize()));
		properties.add(new SimpleProperty("FuelAssembly.rodPitch", "Rod Pitch",
				category, component.getRodPitch()));

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
	 * Checks getPropertySource for RodClusterAssemblies.
	 */
	@Test
	public void checkRodClusterAssembly() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		RodClusterAssembly component = new RodClusterAssembly(1);

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add dimension properties.
		String category = "Dimensions";
		properties.add(new SimpleProperty("RodClusterAssembly.size",
				"Size (length, # of rods)", category, component.getSize()));
		properties.add(new SimpleProperty("RodClusterAssembly.rodPitch",
				"Rod Pitch", category, component.getRodPitch()));

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
	 * Checks getPropertySource for LWRRods.
	 */
	@Test
	public void checkLWRRod() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		LWRRod component = new LWRRod();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add fill gas properties.
		String category = "Fill Gas";
		properties.add(new SimpleProperty("LWRRod.fillGasName", "Name",
				category, component.getFillGas().getName()));
		properties.add(new SimpleProperty("LWRRod.fillGasDesc", "Description",
				category, component.getFillGas().getDescription()));
		properties.add(new SimpleProperty("LWRRod.fillGasPressure", "Pressure",
				category, component.getPressure()));

		// Add cladding properties.
		category = "Cladding";
		properties.add(new SimpleProperty("LWRRod.cladName", "Name", category,
				component.getClad().getMaterial().getName()));
		properties.add(new SimpleProperty("LWRRod.cladDesc", "Description",
				category, component.getClad().getMaterial().getDescription()));

		// Add other properties.
		category = "Other";
		properties.add(new SimpleProperty("LWRRod.nBlocks",
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
	 * Checks getPropertySource for ControlBanks.
	 */
	@Test
	public void checkControlBank() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		ControlBank component = new ControlBank();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add other properties.
		String category = "Other";
		properties.add(new SimpleProperty("ControlBank.numberOfSteps",
				"Max Number of Axial Steps", category, component
						.getMaxNumberOfSteps()));
		properties.add(new SimpleProperty("ControlBank.stepSize",
				"Axial Step Size", category, component.getStepSize()));
		properties.add(new SimpleProperty("ControlBank.strokeLength",
				"Stroke Length", category, component.getStrokeLength()));

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
	 * Checks getPropertySource for IncoreInstruments.
	 */
	@Test
	public void checkIncoreInstrument() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		IncoreInstrument component = new IncoreInstrument();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add thimble properties.
		String category = "Thimble";
		properties.add(new SimpleProperty("IncoreInstrument.thimbleName",
				"Name", category, component.getThimble().getName()));
		properties.add(new SimpleProperty("IncoreInstrument.thimbleDesc",
				"Description", category, component.getThimble()
						.getDescription()));
		properties.add(new SimpleProperty(
				"IncoreInstrument.thimbleInnerRadius", "Inner Radius",
				category, component.getThimble().getInnerRadius()));
		properties.add(new SimpleProperty(
				"IncoreInstrument.thimbleOuterRadius", "Outer Radius",
				category, component.getThimble().getOuterRadius()));
		properties.add(new SimpleProperty("IncoreInstrument.thimbleHeight",
				"Height", category, component.getThimble().getHeight()));

		// Add thimble material properties.
		category = "Thimble Material";
		properties.add(new SimpleProperty(
				"IncoreInstrument.thimbleMaterialName", "Name", category,
				component.getThimble().getMaterial().getName()));
		properties
				.add(new SimpleProperty("IncoreInstrument.thimbleMaterialDesc",
						"Description", category, component.getThimble()
								.getMaterial().getDescription()));
		properties.add(new SimpleProperty(
				"IncoreInstrument.thimbleMaterialType", "Type", category,
				component.getThimble().getMaterial().getMaterialType()));

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
	 * Checks getPropertySource for Tubes.
	 */
	@Test
	public void checkTube() {

		// Initialize a PropertySourceFactory.
		PropertySourceFactory factory = new PropertySourceFactory();

		// Set up the component and its special properties.
		Tube component = new Tube();

		// Get the information we need to test.
		List<SimpleProperty> properties = getBaseProperties(component);
		IPropertySource source = factory.getPropertySource(component);
		assertNotNull(source);

		// Update the List of properties for this component type.
		// Add thimble properties.
		String category = "Dimensions";
		properties.add(new SimpleProperty("Tube.innerRadius", "Inner Radius",
				category, component.getInnerRadius()));
		properties.add(new SimpleProperty("Tube.outerRadius", "Outer Radius",
				category, component.getOuterRadius()));
		properties.add(new SimpleProperty("Tube.height", "Height", category,
				component.getHeight()));

		// Add thimble material properties.
		category = "Thimble Material";
		properties.add(new SimpleProperty("Tube.materialName", "Name",
				category, component.getMaterial().getName()));
		properties.add(new SimpleProperty("Tube.materialDesc", "Description",
				category, component.getMaterial().getDescription()));
		properties.add(new SimpleProperty("Tube.materialType", "Type",
				category, component.getMaterial().getMaterialType()));

		// Add other properties.
		category = "Other";
		properties.add(new SimpleProperty("Tube.type", "Tube Type", category,
				component.getTubeType()));

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
	 * Checks getPropertySource for Rings.
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
		properties.add(new SimpleProperty("Ring.materialType", "Type",
				category, component.getMaterial().getMaterialType()));

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
