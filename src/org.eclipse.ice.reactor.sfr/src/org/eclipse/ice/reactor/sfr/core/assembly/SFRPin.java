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
package org.eclipse.ice.reactor.sfr.core.assembly;

import java.util.Iterator;
import java.util.TreeSet;

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;

/**
 * <p>
 * Class representing the SFR pin structure. The pin is the basic unit of the
 * FuelAssembly and ControlAssembly lattice, and contains either fissile,
 * fertile or absorber pellets, in addition to structural features above and
 * below the pellet columns.
 * </p>
 * 
 * @author w5q
 */
public class SFRPin extends SFRComponent {

	/**
	 * <p>
	 * Type of gas filling the the pellet-clad gap (space between pellet stack
	 * and cladding).
	 * </p>
	 * 
	 */
	private Material fillGas;
	/**
	 * <p>
	 * Cylindrical tubing structure that houses a pin.
	 * </p>
	 * 
	 */
	private Ring cladding;
	/**
	 * <p>
	 * Collection of material blocks contained in a single SFRPin; can include
	 * pellet-clad gap rings, and pellet rings.
	 * </p>
	 * 
	 */
	private TreeSet<MaterialBlock> materialBlocks;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public SFRPin() {

		this("SFR Pin 1", null, null, null);

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor with the name specified.
	 * </p>
	 * 
	 * @param name
	 *            Name of the pin.
	 */
	public SFRPin(String name) {

		// Set up all of the defaults.
		this(name, null, null, null);

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor specified pin name, location, fill gas and
	 * material blocks (if any) specified.
	 * </p>
	 * 
	 * @param name
	 *            Name of the pin.
	 * @param cladding
	 *            The pin's cladding.
	 * @param fillGas
	 *            The pin's filler gas.
	 * @param materialBlocks
	 *            A TreeSet of the pin's material blocks.
	 */
	public SFRPin(String name, Ring cladding, Material fillGas,
			TreeSet<MaterialBlock> materialBlocks) {

		// Call the super constructor with the default value.
		super("SFR Pin 1");

		// Set the other defaults.
		setDescription("SFR Pin 1's Description");
		setId(1);

		// Try to set the name.
		setName(name);

		// Numbers below are in mm.

		// Try to set the cladding.
		setCladding(cladding);

		// If the cladding did not set properly, set the default value.
		if (cladding == null || !cladding.equals(this.cladding)) {
			// Create a stainless steel material.
			Material steel = new Material("SS-316");
			steel.setDescription("Stainless Steel");

			// Set the default cladding.
			this.cladding = new Ring("Cladding", steel, -1, 16.25, 17.5);
		}

		// Try to set the fill gas.
		setFillGas(fillGas);

		// If the fill gas did not set properly, set the default value.
		if (fillGas == null || !fillGas.equals(this.fillGas)) {
			// Create a helium material.
			Material helium = new Material("He");
			helium.setDescription("Helium");

			// Set the default fill gas.
			this.fillGas = helium;
		}

		// Try to set the material blocks.
		setMaterialBlocks(materialBlocks);

		// If the material blocks did not set properly, set the default value.
		if (materialBlocks == null
				|| !materialBlocks.equals(this.materialBlocks)) {
			// Create a fuel material.
			Material fuel = new Material("UO2");
			fuel.setDescription("Uranium Oxide");

			// Create the default MaterialBlock TreeSet (one block).
			this.materialBlocks = new TreeSet<MaterialBlock>();

			MaterialBlock block = new MaterialBlock();
			block.setVertPosition(0);

			// Add the cladding, fill gas, and fuel to the block.
			block.addRing(this.cladding);

			// Try to add fill gas and fuel if the cladding does not take up too
			// much space.
			double outerRadius = this.cladding.getInnerRadius();
			double innerRadius = outerRadius - 2.9167;
			if (innerRadius > 0) {
				block.addRing(new Ring("Fill Gas", this.fillGas, -1,
						innerRadius, outerRadius));
				block.addRing(new Ring("Fuel", fuel, -1, 0, innerRadius));
			}

			// Add the block to the TreeSet.
			this.materialBlocks.add(block);
		}

		return;
	}

	/**
	 * <p>
	 * Sets the type of gas present inside the pellet-clad (space between pellet
	 * stack and cladding) gap and gas plenum(s).
	 * </p>
	 * 
	 * @param gas
	 *            The pin's filler gas. Cannot be null.
	 */
	public void setFillGas(Material gas) {

		// Set the fill gas if the parameter is not null.
		if (gas != null) {
			fillGas = gas;
		}
		return;
	}

	/**
	 * <p>
	 * Returns the type of fill gas present inside the pellet-stack gap (space
	 * between pellet stack and cladding) and gas plenum(s).
	 * </p>
	 * 
	 * @return The pin's filler gas.
	 */
	public Material getFillGas() {
		return fillGas;
	}

	/**
	 * <p>
	 * Sets a collection of material blocks within the SFRPin.
	 * </p>
	 * 
	 * @param materialBlocks
	 *            A TreeSet of material blocks contained in the pin. Cannot be
	 *            null or empty.
	 */
	public void setMaterialBlocks(TreeSet<MaterialBlock> materialBlocks) {

		// Set the material blocks if the incoming TreeSet is not null or empty.
		if (materialBlocks != null && !materialBlocks.isEmpty()) {
			this.materialBlocks = materialBlocks;
		}

		return;
	}

	/**
	 * <p>
	 * Returns a collection of material blocks within the SFRPin.
	 * </p>
	 * 
	 * @return Returns the TreeSet of material blocks contained in the pin.
	 */
	public TreeSet<MaterialBlock> getMaterialBlocks() {
		return materialBlocks;
	}

	/**
	 * <p>
	 * Sets the SFRPin cladding as a Ring.
	 * </p>
	 * 
	 * @param cladding
	 *            The pin's cladding. Cannot be null.
	 */
	public void setCladding(Ring cladding) {

		// Set the cladding if the parameter is not null.
		if (cladding != null) {
			this.cladding = cladding;
		}

		return;
	}

	/**
	 * <p>
	 * Returns the SFRPin's cladding as a Ring.
	 * </p>
	 * 
	 * @return The pin's cladding.
	 */
	public Ring getCladding() {
		return cladding;
	}

	/**
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * 
	 * @return The hashcode of the object.
	 */
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * fillGas.hashCode();
		hash += 31 * cladding.hashCode();
		hash += 31 * materialBlocks.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Compares the contents of objects and returns true if that are identical.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object being compared against.
	 * @return Returns true if the both objects are equal, otherwise false.
	 */
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof SFRPin) {

			// We can now cast the other object.
			SFRPin pin = (SFRPin) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && fillGas.equals(pin.fillGas)
					&& cladding.equals(pin.cladding) && materialBlocks.size() == pin.materialBlocks
					.size());

			// Because the TreeSet does not compare the actual MaterialBlock
			// objects in the .equals method (it uses the compare function), we
			// need to manuall check the material blocks.
			Iterator<MaterialBlock> iter1 = materialBlocks.iterator();
			Iterator<MaterialBlock> iter2 = pin.materialBlocks.iterator();

			// Loop until we have checked every block or we have a mismatch.
			while (equals && iter1.hasNext()) {
				equals = iter1.next().equals(iter2.next());
			}

		}

		return equals;
	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 */
	public void copy(SFRPin otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}

		// Copy the super's values.
		super.copy(otherObject);

		// Copy the local values.
		fillGas.copy(otherObject.fillGas);
		cladding.copy(otherObject.cladding);
		materialBlocks.clear();
		materialBlocks.addAll(otherObject.materialBlocks);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated object.
	 */
	public Object clone() {

		// Initialize a new object.
		SFRPin object = new SFRPin();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {

		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}