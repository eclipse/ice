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
package org.eclipse.ice.client.widgets.reactoreditor.lwr.properties;

import org.eclipse.ice.client.widgets.reactoreditor.properties.ReactorComponentPropertySource;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;

import org.eclipse.ice.reactor.ILWRComponentVisitor;
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
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class is used to construct an IPropertySource for a particular reactor
 * Component. To get an IPropertySource for a reactor Component, call
 * {@linkplain PropertySourceFactory#getPropertySource(LWRComponent)}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PropertySourceFactory implements ILWRComponentVisitor {

	/**
	 * The IPropertySource for the component.
	 */
	private ReactorComponentPropertySource propertySource;

	/**
	 * Gets an IPropertySource for the Component specified in the constructor.
	 * 
	 * @param component
	 *            The component whose properties will be exposed.
	 * @return An IPropertySource instance or null if none is available.
	 */
	public IPropertySource getPropertySource(LWRComponent component) {
		// Reset the property source reference. This effectively adds the basic
		// properties like name, description, and ID.
		propertySource = new ReactorComponentPropertySource(component);

		// Update the IPropertySource for this LWRComponent.
		component.accept(this);

		// Return the property source reference.
		return propertySource;
	}

	/* ---- Implements ILWRComponentVisitor. ---- */
	// These visit operations should set the propertySource variable
	// appropriately in order to expose properties for the visted LWRComponent.
	public void visit(PressurizedWaterReactor reactor) {

		// Add dimension properties.
		String category = "Dimensions";
		propertySource.addProperty(new SimpleProperty("PWReactor.size",
				"Size (length, # of assemblies)", category, reactor.getSize()));

		return;
	}

	public void visit(BWReactor reactor) {
		return;
	}

	public void visit(FuelAssembly assembly) {

		// Add dimension properties.
		String category = "Dimensions";
		propertySource.addProperty(new SimpleProperty("FuelAssembly.size",
				"Size (length, # of rods)", category, assembly.getSize()));
		propertySource.addProperty(new SimpleProperty("FuelAssembly.rodPitch",
				"Rod Pitch", category, assembly.getRodPitch()));

		return;
	}

	public void visit(RodClusterAssembly assembly) {
		// Add dimension properties.
		String category = "Dimensions";
		propertySource.addProperty(new SimpleProperty(
				"RodClusterAssembly.size", "Size (length, # of rods)",
				category, assembly.getSize()));
		propertySource.addProperty(new SimpleProperty(
				"RodClusterAssembly.rodPitch", "Rod Pitch", category, assembly
						.getRodPitch()));

		return;
	}

	public void visit(LWRRod rod) {

		// Add fill gas properties.
		String category = "Fill Gas";
		propertySource.addProperty(new SimpleProperty("LWRRod.fillGasName",
				"Name", category, rod.getFillGas().getName()));
		propertySource.addProperty(new SimpleProperty("LWRRod.fillGasDesc",
				"Description", category, rod.getFillGas().getDescription()));
		propertySource.addProperty(new SimpleProperty("LWRRod.fillGasPressure",
				"Pressure", category, rod.getPressure()));

		// Add cladding properties.
		category = "Cladding";
		propertySource.addProperty(new SimpleProperty("LWRRod.cladName",
				"Name", category, rod.getClad().getMaterial().getName()));
		propertySource.addProperty(new SimpleProperty("LWRRod.cladDesc",
				"Description", category, rod.getClad().getMaterial()
						.getDescription()));

		// Add other properties.
		category = "Other";
		propertySource.addProperty(new SimpleProperty("LWRRod.nBlocks",
				"# of Material Blocks", category, rod.getMaterialBlocks()
						.size()));

		return;
	}

	public void visit(ControlBank controlBank) {

		// Add other properties.
		String category = "Other";
		propertySource.addProperty(new SimpleProperty(
				"ControlBank.numberOfSteps", "Max Number of Axial Steps",
				category, controlBank.getMaxNumberOfSteps()));
		propertySource.addProperty(new SimpleProperty("ControlBank.stepSize",
				"Axial Step Size", category, controlBank.getStepSize()));
		propertySource.addProperty(new SimpleProperty(
				"ControlBank.strokeLength", "Stroke Length", category,
				controlBank.getStrokeLength()));

		return;
	}

	public void visit(IncoreInstrument incoreInstrument) {

		// Add thimble properties.
		String category = "Thimble";
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleName", "Name", category,
				incoreInstrument.getThimble().getName()));
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleDesc", "Description", category,
				incoreInstrument.getThimble().getDescription()));
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleInnerRadius", "Inner Radius",
				category, incoreInstrument.getThimble().getInnerRadius()));
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleOuterRadius", "Outer Radius",
				category, incoreInstrument.getThimble().getOuterRadius()));
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleHeight", "Height", category,
				incoreInstrument.getThimble().getHeight()));

		// Add thimble material properties.
		category = "Thimble Material";
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleMaterialName", "Name", category,
				incoreInstrument.getThimble().getMaterial().getName()));
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleMaterialDesc", "Description",
				category, incoreInstrument.getThimble().getMaterial()
						.getDescription()));
		propertySource.addProperty(new SimpleProperty(
				"IncoreInstrument.thimbleMaterialType", "Type", category,
				incoreInstrument.getThimble().getMaterial().getMaterialType()));

		return;
	}

	public void visit(Tube tube) {

		// Add thimble properties.
		String category = "Dimensions";
		propertySource.addProperty(new SimpleProperty("Tube.innerRadius",
				"Inner Radius", category, tube.getInnerRadius()));
		propertySource.addProperty(new SimpleProperty("Tube.outerRadius",
				"Outer Radius", category, tube.getOuterRadius()));
		propertySource.addProperty(new SimpleProperty("Tube.height", "Height",
				category, tube.getHeight()));

		// Add thimble material properties.
		category = "Thimble Material";
		propertySource.addProperty(new SimpleProperty("Tube.materialName",
				"Name", category, tube.getMaterial().getName()));
		propertySource.addProperty(new SimpleProperty("Tube.materialDesc",
				"Description", category, tube.getMaterial().getDescription()));
		propertySource.addProperty(new SimpleProperty("Tube.materialType",
				"Type", category, tube.getMaterial().getMaterialType()));

		// Add other properties.
		category = "Other";
		propertySource.addProperty(new SimpleProperty("Tube.type", "Tube Type",
				category, tube.getTubeType()));

		return;
	}

	public void visit(Ring ring) {

		// Add dimension properties.
		String category = "Dimensions";
		propertySource.addProperty(new SimpleProperty("Ring.innerRadius",
				"Inner Radius", category, ring.getInnerRadius()));
		propertySource.addProperty(new SimpleProperty("Ring.outerRadius",
				"Outer Radius", category, ring.getOuterRadius()));
		propertySource.addProperty(new SimpleProperty("Ring.height", "Height",
				category, ring.getHeight()));

		// Add Material properties.
		category = "Composition";
		propertySource.addProperty(new SimpleProperty("Ring.materialName",
				"Name", category, ring.getMaterial().getName()));
		propertySource.addProperty(new SimpleProperty("Ring.materialDesc",
				"Description", category, ring.getMaterial().getDescription()));
		propertySource.addProperty(new SimpleProperty("Ring.materialType",
				"Type", category, ring.getMaterial().getMaterialType()));

		return;
	}
	/* ------------------------------------------ */
}
