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

import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComposite;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Class representing the assembly structure of a SFR. The SFR assembly is
 * housed in a hexagonal structure called the wrapper tube (or duct), and
 * contains a lattice of either pins or rods.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRAssembly extends SFRComposite {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Size of a SFRAssembly. Size represents number of pins in a fuel or
	 * control assembly, and rods in a reflector assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int size;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The type of SFR assembly represented, either fuel, control or reflector.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected AssemblyType assemblyType;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Thickness of the assembly duct wall.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double ductThickness;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with assemble size specified. Size represents
	 * number of pins in a fuel or control assembly, and rods in a reflector
	 * assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            Size of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRAssembly(int size) {
		// begin-user-code

		// Set the size if positive, otherwise default to 1.
		this.size = (size > 0 ? size : 1);

		// Set the default name, description, and ID.
		setName("SFR Assembly 1");
		setDescription("SFR Assembly 1's Description");
		setId(1);

		// Default the assembly type to Fuel.
		assemblyType = AssemblyType.Fuel;

		// Initialize ductThickness.
		ductThickness = 0.0;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with assembly name, type and size specified.
	 * Size represents number of pins in a fuel or control assembly, and rods in
	 * a reflector assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the assembly.
	 * @param type
	 *            The assembly type (fuel, control or reflector).
	 * @param size
	 *            The size of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRAssembly(String name, AssemblyType type, int size) {
		// begin-user-code

		// Call the basic constructor first.
		this(size);

		// Set the name.
		setName(name);

		// Set the assembly type if possible. If null, the other constructor has
		// already set the type to the default (Fuel).
		if (type != null) {
			assemblyType = type;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the assembly size. Size represents number of pins in a fuel or
	 * control assembly, and rods in a reflector assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The size of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getSize() {
		// begin-user-code
		return size;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the assembly type (fuel, control or reflector).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The assembly type.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AssemblyType getAssemblyType() {
		// begin-user-code
		return assemblyType;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the thickness of the assembly duct wall.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param thickness
	 *            The duct thickness. Must be non-negative.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDuctThickness(double thickness) {
		// begin-user-code

		// Only set the duct thickness if it is 0 or larger.
		if (thickness >= 0.0) {
			ductThickness = thickness;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the duct wall thickness of an assembly as a double.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The duct thickness.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getDuctThickness() {
		// begin-user-code
		return ductThickness;
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
	 *            The object to be compared.
	 * @return True if otherObject is equal. False otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public boolean equals(Object otherObject) {
		// begin-user-code

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof SFRAssembly) {

			// We can now cast the other object.
			SFRAssembly assembly = (SFRAssembly) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && size == assembly.size
					&& assemblyType == assembly.assemblyType && ductThickness == assembly.ductThickness);
		}

		return equals;
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

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * size;
		hash += 31 * assemblyType.hashCode();
		hash += 31 * ductThickness;

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object from another object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied from.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(SFRAssembly otherObject) {
		// begin-user-code

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		// Copy the super's values.
		super.copy(otherObject);

		// Copy the local values.
		size = otherObject.size;
		assemblyType = otherObject.assemblyType;
		ductThickness = otherObject.ductThickness;

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
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Initialize a new object.
		SFRAssembly object = new SFRAssembly(size);

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