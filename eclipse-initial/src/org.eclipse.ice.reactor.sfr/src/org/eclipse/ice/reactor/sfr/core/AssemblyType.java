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
package org.eclipse.ice.reactor.sfr.core;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Represents the three possible types of assemblies: fuel, control or
 * reflector.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum AssemblyType {
	/**
	 * <!-- begin-UML-doc --> Fuel type includes burnable (inner fuel, outer
	 * fuel) and blanket (optional) assemblies. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Fuel("Fuel Assembly"),
	/**
	 * <!-- begin-UML-doc --> Control type includes primary and secondary
	 * (shutdown) assemblies. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Control("Control Assembly"),
	/**
	 * <!-- begin-UML-doc --> Reflector type contains only reflector assemblies.
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Reflector("Reflector Assembly"),
	/**
	 * <!-- begin-UML-doc --> Shield type only contains shield assemblies. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Shield("Shield Assembly"),
	/**
	 * <!-- begin-UML-doc --> Test type includes materials test and fuel test
	 * assemblies. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Test("Test Assembly");

	/**
	 * <!-- begin-UML-doc --> A user-friendly String for displaying an
	 * AssemblyType value. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private final String name;

	/**
	 * <!-- begin-UML-doc --> The default constructor for an AssemblyType. <!--
	 * end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the assembly type.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AssemblyType(String name) {
		// Set the user-friendly name String for this assembly type.
		this.name = name;

		return;
	}

	/**
	 * <!-- begin-UML-doc --> Override the default toString() behavior to give
	 * the user a better-formatted String representing this AssemblyType. <!--
	 * end-UML-doc -->
	 * 
	 * @return Returns the assembly type as a String.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Converts a String to an AssemblyType enum value. Returns null if the
	 * String is invalid.
	 * 
	 * @param name
	 *            The String to convert.
	 * @return As AssemblyType if the String is valid, null otherwise.
	 */
	public static AssemblyType fromString(String name) {

		AssemblyType type = null;

		if (Fuel.name.equals(name)) {
			type = Fuel;
		} else if (Control.name.equals(name)) {
			type = Control;
		} else if (Reflector.name.equals(name)) {
			type = Reflector;
		} else if (Shield.name.equals(name)) {
			type = Shield;
		} else if (Test.name.equals(name)) {
			type = Test;
		}

		return type;
	}
}