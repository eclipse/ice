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
package org.eclipse.ice.client.widgets.reactoreditor.sfr.properties;

import org.eclipse.ice.client.widgets.reactoreditor.properties.ReactorComponentPropertySource;
import org.eclipse.ice.client.widgets.reactoreditor.properties.SimpleProperty;

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
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
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class is used to construct an IPropertySource for a particular reactor
 * Component. To get an IPropertySource for a reactor Component, call
 * {@linkplain PropertySourceFactory#getPropertySource(SFRComponent)}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PropertySourceFactory implements ISFRComponentVisitor {

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
	public IPropertySource getPropertySource(SFRComponent component) {
		// Reset the property source reference. This effectively adds the basic
		// properties like name, description, and ID.
		propertySource = new ReactorComponentPropertySource(component);

		// Update the IPropertySource for this SFRComponent.
		component.accept(this);

		// Return the property source reference.
		return propertySource;
	}

	/* ---- Implements ISFRComponentVisitor. ---- */
	// These visit operations should set the propertySource variable
	// appropriately in order to expose properties for the visted SFRComponent.
	public void visit(SFReactor reactor) {

		// Add dimension properties.
		String category = "Dimensions";
		propertySource
				.addProperty(new SimpleProperty("SFReactor.size",
						"Size (diameter, # of assemblies)", category, reactor
								.getSize()));
		propertySource.addProperty(new SimpleProperty("SFReactor.latticePitch",
				"Lattice Pitch", category, reactor.getLatticePitch()));
		propertySource.addProperty(new SimpleProperty(
				"SFReactor.outerFlatToFlat", "Outer Flat-to-Flat", category,
				reactor.getOuterFlatToFlat()));

		return;
	}

	public void visit(SFRAssembly sfrComp) {
		return;
	}

	public void visit(PinAssembly assembly) {

		// Add dimension properties.
		String category = "Dimensions";
		propertySource.addProperty(new SimpleProperty("PinAssembly.size",
				"Size (diameter, # of pins)", category, assembly.getSize()));
		propertySource.addProperty(new SimpleProperty(
				"PinAssembly.ductThickness", "Duct Thickness", category,
				assembly.getDuctThickness()));
		propertySource.addProperty(new SimpleProperty(
				"PinAssembly.innerDuctThickness", "Inner Duct Thickness",
				category, assembly.getInnerDuctThickness()));
		propertySource.addProperty(new SimpleProperty(
				"PinAssembly.innerDuctFlatToFlat", "Inner Duct Flat-to-Flat",
				category, assembly.getInnerDuctFlatToFlat()));
		propertySource.addProperty(new SimpleProperty("PinAssembly.pitch",
				"Pin Pitch", category, assembly.getPinPitch()));

		// Add other properties.
		category = "Other";
		propertySource.addProperty(new SimpleProperty("PinAssembly.type",
				"Assembly Type", category, assembly.getAssemblyType()
						.toString()));
		propertySource.addProperty(new SimpleProperty("PinAssembly.pinType",
				"Pin Type", category, assembly.getPinType().toString()));

		return;
	}

	public void visit(ReflectorAssembly sfrComp) {
		return;
	}

	public void visit(SFRPin pin) {

		// Add fill gas properties.
		String category = "Fill Gas";
		propertySource.addProperty(new SimpleProperty("SFRPin.fillGasName",
				"Name", category, pin.getFillGas().getName()));
		propertySource.addProperty(new SimpleProperty("SFRPin.fillGasDesc",
				"Description", category, pin.getFillGas().getDescription()));

		// Add cladding properties.
		category = "Cladding";
		propertySource.addProperty(new SimpleProperty("SFRPin.cladName",
				"Name", category, pin.getCladding().getMaterial().getName()));
		propertySource.addProperty(new SimpleProperty("SFRPin.cladDesc",
				"Description", category, pin.getCladding().getMaterial()
						.getDescription()));

		// Add other properties.
		category = "Other";
		propertySource.addProperty(new SimpleProperty("SFRPin.nBlocks",
				"# of Material Blocks", category, pin.getMaterialBlocks()
						.size()));

		return;
	}

	public void visit(SFRRod sfrComp) {
		return;
	}

	public void visit(MaterialBlock sfrComp) {
		return;
	}

	public void visit(Material sfrComp) {
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

		return;
	}
	/* ------------------------------------------ */
}
