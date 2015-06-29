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
package org.eclipse.ice.reactor.pwr;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;

/**
 * <p>
 * The ControlBank class contains properties associated with axially movable
 * RodClusterAssembly objects known as Rod Cluster Control Assemblies. A Bank is
 * a group of RCCA's that are positioned simultaneously by the plant operations.
 * A Bank may have 8 individual RCCAs, for instance, located symmetrically
 * around the core. For a core with 50+ RCCAs, usually only 8 or so banks are
 * controlled independently.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class ControlBank extends LWRComponent {
	/**
	 * <p>
	 * The distance between an axial step.
	 * </p>
	 * 
	 */
	private double stepSize;
	/**
	 * <p>
	 * The maximum number of axial steps.
	 * </p>
	 * 
	 */
	private int maxNumberOfSteps;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public ControlBank() {
		this.setMaxNumberOfSteps(1);
		this.setStepSize(0.0);
		this.setName("ControlBank 1");
		this.setDescription("Default Control Bank");
		this.setId(1);
		this.HDF5LWRTag = HDF5LWRTagType.CONTROL_BANK;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            The name of the component.
	 * @param stepSize
	 *            <p>
	 *            The distance between an axial step.
	 *            </p>
	 * @param maxNumberOfSteps
	 *            <p>
	 *            The maximum number of axial steps.
	 *            </p>
	 */
	public ControlBank(String name, double stepSize, int maxNumberOfSteps) {
		this();
		this.setStepSize(stepSize);
		this.setMaxNumberOfSteps(maxNumberOfSteps);
		this.setName(name);
	}

	/**
	 * <p>
	 * Sets the axial step size.
	 * </p>
	 * 
	 * @param stepSize
	 *            <p>
	 *            The axial step size.
	 *            </p>
	 */
	public void setStepSize(double stepSize) {
		if (stepSize > 0.0) {
			this.stepSize = stepSize;
		} else {
			this.stepSize = 0.0;
		}
	}

	/**
	 * <p>
	 * Sets the maximum number of axial steps.
	 * </p>
	 * 
	 * @param maxNumberOfSteps
	 *            <p>
	 *            The maximum number of axial steps.
	 *            </p>
	 */
	public void setMaxNumberOfSteps(int maxNumberOfSteps) {
		if (maxNumberOfSteps > 0) {
			this.maxNumberOfSteps = maxNumberOfSteps;
		} else {
			this.maxNumberOfSteps = 1;
		}
	}

	/**
	 * <p>
	 * Returns the maximum number of axial steps.
	 * </p>
	 * 
	 * @return <p>
	 *         The maximum number of axial steps.
	 *         </p>
	 */
	public int getMaxNumberOfSteps() {
		return this.maxNumberOfSteps;
	}

	/**
	 * <p>
	 * Returns the axial step size.
	 * </p>
	 * 
	 * @return <p>
	 *         The axial step size.
	 *         </p>
	 */
	public double getStepSize() {
		return this.stepSize;
	}

	/**
	 * <p>
	 * Calculates and returns the stroke length, which is the axial step size
	 * multiplied by the maximum number of axial steps.
	 * </p>
	 * 
	 * @return <p>
	 *         The axial step size multiplied by the maximum number of axial
	 *         steps.
	 *         </p>
	 */
	public double getStrokeLength() {

		return ((double) this.maxNumberOfSteps) * this.stepSize;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"stepSize", stepSize);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group,
				"maxNumberOfSteps", maxNumberOfSteps);

		return flag;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		boolean retVal = false;
		ControlBank bank;

		// If they are the same on the heap, return true
		if (otherObject == this) {
			return true;
		}

		// Make sure the otherObject is not null and an instance of this object
		if (otherObject != null && otherObject instanceof ControlBank) {

			// Cast it
			bank = (ControlBank) otherObject;

			// Compare values
			retVal = (super.equals(otherObject)
					&& this.maxNumberOfSteps == bank.maxNumberOfSteps && this.stepSize == bank.stepSize);
		}

		// Return retVal
		return retVal;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		hash += 31 * this.maxNumberOfSteps;
		hash += 31 * this.stepSize;

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
	public void copy(ControlBank otherObject) {

		// If otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.maxNumberOfSteps = otherObject.maxNumberOfSteps;
		this.stepSize = otherObject.stepSize;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		ControlBank bank = new ControlBank();

		// Copy contents
		bank.copy(this);

		// Return newly instantiated object
		return bank;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		// Local Declarations
		boolean flag = true;

		// Get values
		Double stepSize = HdfReaderFactory.readDoubleAttribute(h5Group,
				"stepSize");
		Integer maxNumberOfSteps = HdfReaderFactory.readIntegerAttribute(
				h5Group, "maxNumberOfSteps");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (stepSize == null || maxNumberOfSteps == null || !flag
				|| h5Group == null) {
			return false;
		}

		// If everything is valid, then set data
		this.stepSize = stepSize.doubleValue();
		this.maxNumberOfSteps = maxNumberOfSteps.intValue();

		return true;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}
}