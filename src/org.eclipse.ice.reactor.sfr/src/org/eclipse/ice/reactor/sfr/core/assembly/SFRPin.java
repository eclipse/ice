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
 * <!-- begin-UML-doc -->
 * <p>
 * Class representing the SFR pin structure. The pin is the basic unit of the
 * FuelAssembly and ControlAssembly lattice, and contains either fissile,
 * fertile or absorber pellets, in addition to structural features above and
 * below the pellet columns.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRPin extends SFRComponent {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Type of gas filling the the pellet-clad gap (space between pellet stack
	 * and cladding).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Material fillGas;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Cylindrical tubing structure that houses a pin.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Ring cladding;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Collection of material blocks contained in a single SFRPin; can include
	 * pellet-clad gap rings, and pellet rings.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TreeSet<MaterialBlock> materialBlocks;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRPin() {
		// begin-user-code

		this("SFR Pin 1", null, null, null);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with the name specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            Name of the pin.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRPin(String name) {
		// begin-user-code

		// Set up all of the defaults.
		this(name, null, null, null);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor specified pin name, location, fill gas and
	 * material blocks (if any) specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            Name of the pin.
	 * @param cladding
	 *            The pin's cladding.
	 * @param fillGas
	 *            The pin's filler gas.
	 * @param materialBlocks
	 *            A TreeSet of the pin's material blocks.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRPin(String name, Ring cladding, Material fillGas,
			TreeSet<MaterialBlock> materialBlocks) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the type of gas present inside the pellet-clad (space between pellet
	 * stack and cladding) gap and gas plenum(s).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param gas
	 *            The pin's filler gas. Cannot be null.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setFillGas(Material gas) {
		// begin-user-code

		// Set the fill gas if the parameter is not null.
		if (gas != null) {
			fillGas = gas;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the type of fill gas present inside the pellet-stack gap (space
	 * between pellet stack and cladding) and gas plenum(s).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The pin's filler gas.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material getFillGas() {
		// begin-user-code
		return fillGas;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets a collection of material blocks within the SFRPin.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param materialBlocks
	 *            A TreeSet of material blocks contained in the pin. Cannot be
	 *            null or empty.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMaterialBlocks(TreeSet<MaterialBlock> materialBlocks) {
		// begin-user-code

		// Set the material blocks if the incoming TreeSet is not null or empty.
		if (materialBlocks != null && !materialBlocks.isEmpty()) {
			this.materialBlocks = materialBlocks;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a collection of material blocks within the SFRPin.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return Returns the TreeSet of material blocks contained in the pin.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TreeSet<MaterialBlock> getMaterialBlocks() {
		// begin-user-code
		return materialBlocks;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the SFRPin cladding as a Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param cladding
	 *            The pin's cladding. Cannot be null.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCladding(Ring cladding) {
		// begin-user-code

		// Set the cladding if the parameter is not null.
		if (cladding != null) {
			this.cladding = cladding;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the SFRPin's cladding as a Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The pin's cladding.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring getCladding() {
		// begin-user-code
		return cladding;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashcode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The hashcode of the object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * fillGas.hashCode();
		hash += 31 * cladding.hashCode();
		hash += 31 * materialBlocks.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Compares the contents of objects and returns true if that are identical.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other object being compared against.
	 * @return Returns true if the both objects are equal, otherwise false.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            The other object to copy the contents of.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(SFRPin otherObject) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The newly instantiated object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		SFRPin object = new SFRPin();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {
		// begin-user-code

		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}
}