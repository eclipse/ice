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

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ControlBank class contains properties associated with axially movable
 * RodClusterAssembly objects known as Rod Cluster Control Assemblies. A Bank is
 * a group of RCCA's that are positioned simultaneously by the plant operations.
 * A Bank may have 8 individual RCCAs, for instance, located symmetrically
 * around the core. For a core with 50+ RCCAs, usually only 8 or so banks are
 * controlled independently.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ControlBank extends LWRComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The distance between an axial step.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double stepSize;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The maximum number of axial steps.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int maxNumberOfSteps;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ControlBank() {
		// begin-user-code
		this.setMaxNumberOfSteps(1);
		this.setStepSize(0.0);
		this.setName("ControlBank 1");
		this.setDescription("Default Control Bank");
		this.setId(1);
		this.HDF5LWRTag = HDF5LWRTagType.CONTROL_BANK;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 * @param stepSize
	 *            <p>
	 *            The distance between an axial step.
	 *            </p>
	 * @param maxNumberOfSteps
	 *            <p>
	 *            The maximum number of axial steps.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ControlBank(String name, double stepSize, int maxNumberOfSteps) {
		// begin-user-code
		this();
		this.setStepSize(stepSize);
		this.setMaxNumberOfSteps(maxNumberOfSteps);
		this.setName(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the axial step size.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param stepSize
	 *            <p>
	 *            The axial step size.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setStepSize(double stepSize) {
		// begin-user-code
		if (stepSize > 0.0) {
			this.stepSize = stepSize;
		} else {
			this.stepSize = 0.0;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the maximum number of axial steps.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param maxNumberOfSteps
	 *            <p>
	 *            The maximum number of axial steps.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMaxNumberOfSteps(int maxNumberOfSteps) {
		// begin-user-code
		if (maxNumberOfSteps > 0) {
			this.maxNumberOfSteps = maxNumberOfSteps;
		} else {
			this.maxNumberOfSteps = 1;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the maximum number of axial steps.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The maximum number of axial steps.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getMaxNumberOfSteps() {
		// begin-user-code
		return this.maxNumberOfSteps;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the axial step size.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The axial step size.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getStepSize() {
		// begin-user-code
		return this.stepSize;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Calculates and returns the stroke length, which is the axial step size
	 * multiplied by the maximum number of axial steps.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The axial step size multiplied by the maximum number of axial
	 *         steps.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getStrokeLength() {
		// begin-user-code

		return ((double) this.maxNumberOfSteps) * this.stepSize;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		// begin-user-code
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"stepSize", stepSize);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group,
				"maxNumberOfSteps", maxNumberOfSteps);

		return flag;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = super.hashCode();

		hash += 31 * this.maxNumberOfSteps;
		hash += 31 * this.stepSize;

		return hash;

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
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(ControlBank otherObject) {
		// begin-user-code

		// If otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.maxNumberOfSteps = otherObject.maxNumberOfSteps;
		this.stepSize = otherObject.stepSize;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		ControlBank bank = new ControlBank();

		// Copy contents
		bank.copy(this);

		// Return newly instantiated object
		return bank;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readAttributes(H5Group h5Group) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(ILWRComponentVisitor visitor) {
		// begin-user-code
		visitor.visit(this);
		// end-user-code
	}
}