/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

/**
 * <p>
 * The LWRRod class is a generalized class representing a basic rod as a
 * collection of materialBlocks contained by a ring (aka the clad). A LWRRod
 * should be considered the basis for all "rod-tyoes" implemented within a
 * reactor. Details concerning material compositions should be taken care of on
 * the collections of rings on the materialblocks.
 * </p>
 * <p>
 * Please note that the MaterialBlocks MUST HAVE UNIQUE POSITIONS SET! If there
 * are material blocks with 2 of the same EXACT position type, then the first
 * materialblock will be overridden by the second materialblock of the same
 * position value. This is very important to understand when adding
 * materialBlocks to the grid.
 * </p>
 * <p>
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRRod extends LWRComponent {
	/**
	 * <p>
	 * An annular Ring which surrounds this LWRRod's MaterialBlocks list.
	 * </p>
	 * 
	 */
	private Ring clad;
	/**
	 * <p>
	 * A Material of MaterialType.GAS that fills the voids within this LWRRod.
	 * </p>
	 * 
	 */
	private Material fillGas;
	/**
	 * <p>
	 * The pressure of the fillGas Material. Can not be less than or equal to 0.
	 * </p>
	 * 
	 */
	private double pressure;
	/**
	 * 
	 */
	private TreeSet<MaterialBlock> materialBlocks;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public LWRRod() {

		// Set default LWRComponent Values
		this.name = "LWRRod";
		this.description = "LWRRod's Description";
		this.id = 1;

		// Set default values for privates
		this.pressure = 2200.00;
		Material cladMaterial = new Material("Zirc", MaterialType.SOLID);
		this.clad = new Ring("Clad", cladMaterial, -1.0, -1.0);
		this.fillGas = new Material("Void", MaterialType.GAS);
		this.materialBlocks = new TreeSet<MaterialBlock>();

		// Setup LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRROD;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this LWRRod.
	 *            </p>
	 */
	public LWRRod(String name) {
		// Call nullary operator
		this();
		// Pass name
		this.setName(name);
	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this LWRRod.
	 *            </p>
	 * @param fillGas
	 *            <p>
	 *            A Material of MaterialType.GAS that fills the voids within
	 *            this LWRRod.
	 *            </p>
	 * @param pressure
	 *            <p>
	 *            The pressure of the fillGas Material.
	 *            </p>
	 * @param materialBlocks
	 *            <p>
	 *            The MaterialBlock list within this LWRRod.
	 *            </p>
	 */
	public LWRRod(String name, Material fillGas, double pressure,
			TreeSet<MaterialBlock> materialBlocks) {

		// Call the respective operators for passed parameters
		this(name);
		this.setFillGas(fillGas);
		this.setPressure(pressure);
		this.setMaterialBlocks(materialBlocks);

	}

	/**
	 * <p>
	 * Returns a Material of MaterialType.GAS that fills the voids within this
	 * LWRRod.
	 * </p>
	 * 
	 * @return <p>
	 *         A Material of MaterialType.GAS that fills the voids within this
	 *         LWRRod.
	 *         </p>
	 */
	public Material getFillGas() {
		return this.fillGas;
	}

	/**
	 * <p>
	 * Sets the Material of MaterialType.GAS that fills the voids within this
	 * LWRRod. Can not be set to null.
	 * </p>
	 * 
	 * @param fillGas
	 *            <p>
	 *            A Material of MaterialType.GAS that fills the voids within
	 *            this LWRRod.
	 *            </p>
	 */
	public void setFillGas(Material fillGas) {
		// if fillGas is not null
		if (fillGas != null) {
			this.fillGas = fillGas;
		}

	}

	/**
	 * <p>
	 * Sets the pressure of the fillGas Material. Can not be set less than or
	 * equal to 0.
	 * </p>
	 * 
	 * @param pressure
	 *            <p>
	 *            The pressure of the fillGas Material.
	 *            </p>
	 */
	public void setPressure(double pressure) {
		// if pressure is not 0 or negative
		if (pressure > 0.0) {
			this.pressure = pressure;
		}

	}

	/**
	 * <p>
	 * Returns the pressure of the fillGas Material.
	 * </p>
	 * 
	 * @return <p>
	 *         The pressure of the fillGas Material.
	 *         </p>
	 */
	public double getPressure() {
		return this.pressure;
	}

	/**
	 * <p>
	 * Returns the list of MaterialBlocks within this LWRRod.
	 * </p>
	 * 
	 * @return <p>
	 *         The Stack object within this LWRRod.
	 *         </p>
	 */
	public TreeSet<MaterialBlock> getMaterialBlocks() {
		return this.materialBlocks;
	}

	/**
	 * <p>
	 * Sets the list of MaterialBlocks within this LWRRod. Can not be set null.
	 * </p>
	 * 
	 * @param materialBlocks
	 *            <p>
	 *            The materialBlocks list within this LWRRod.
	 *            </p>
	 */
	public void setMaterialBlocks(TreeSet<MaterialBlock> materialBlocks) {
		if (materialBlocks != null && !(materialBlocks.isEmpty())) {
			this.materialBlocks = materialBlocks;
		}

	}

	/**
	 * <p>
	 * Returns the clad object of this LWRRod object, if set or null.
	 * </p>
	 * 
	 * @return
	 */
	public Ring getClad() {
		return this.clad;
	}

	/**
	 * <p>
	 * Sets the clad object for this LWRRod. It can not be set to null.
	 * </p>
	 * 
	 * @param clad
	 */
	public void setClad(Ring clad) {
		// if clad is not null
		if (clad != null) {
			this.clad = clad;
		}
	}

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

		// Local Declarations
		LWRRod rod;
		boolean retVal = false;
		// If the objects are the same on the heap, return true
		if (this == otherObject) {
			return true;
		}
		// If the object is null or not an instance of this object, return false
		if (otherObject != null && otherObject instanceof LWRRod) {
			// Cast it
			rod = (LWRRod) otherObject;
			// Check values
			retVal = (super.equals(otherObject) && this.clad.equals(rod.clad)
					&& this.fillGas.equals(rod.fillGas)
					&& this.materialBlocks.equals(rod.materialBlocks) && this.pressure == rod.pressure);
		}

		// Return retVal
		return retVal;

	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 */
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		// Hash local contents
		hash += 31 * this.pressure;
		hash += 31 * this.clad.hashCode();
		hash += 31 * this.fillGas.hashCode();
		hash += 31 * this.materialBlocks.hashCode();

		// Return the hash
		return hash;

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 */
	public void copy(LWRRod otherObject) {

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents - deep copy
		this.clad = (Ring) otherObject.clad.clone();
		this.fillGas = (Material) otherObject.fillGas.clone();
		this.pressure = otherObject.pressure;
		this.materialBlocks.clear();
		for (int i = 0; i < otherObject.materialBlocks.size(); i++) {
			this.materialBlocks
					.add((MaterialBlock) ((MaterialBlock) otherObject.materialBlocks
							.toArray()[i]).clone());
		}

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 */
	public Object clone() {

		// Local Declarations
		LWRRod rod = new LWRRod();

		// Copy contents
		rod.copy(this);

		// Return newly instantiated object
		return rod;

	}

	/**
	 * 
	 * @param h5File
	 * @param h5Group
	 * @return
	 */
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"pressure", pressure);

		return flag;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren() {

		// Get the children in super
		ArrayList<IHdfWriteable> children = super.getWriteableChildren();

		// If super had no children
		if (children == null) {

			// Initialize to new array list
			children = new ArrayList<IHdfWriteable>();
		}

		// Add the materialBlocks, clad and fillGas to children
		children.add(this.clad);
		children.add(this.fillGas);
		children.addAll(this.materialBlocks);

		return children;
	}

	/**
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * 
	 * @param iHdfReadable
	 * @return
	 */
	public boolean readChild(IHdfReadable iHdfReadable) {

		// If the child is null or not an instance of LWRComponent, then return
		// false.
		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// If this is a Ring
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.RING) {

			// Assign to correct object
			this.clad = (Ring) childComponent;

			// If this is a material
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.MATERIAL) {

			// Assign to correct object
			this.fillGas = (Material) childComponent;

			// If this is a Material Block
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.MATERIALBLOCK) {

			// Assign to correct object
			this.materialBlocks.add((MaterialBlock) childComponent);

		}

		return true;
	}

	/**
	 * 
	 * @param h5Group
	 * @return
	 */
	public boolean readAttributes(H5Group h5Group) {

		// Local Declarations
		boolean flag = true;

		// Get values
		Double pressure = HdfReaderFactory.readDoubleAttribute(h5Group,
				"pressure");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (pressure == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.pressure = pressure;

		return true;

	}

	/**
	 * <p>
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 */
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}

}