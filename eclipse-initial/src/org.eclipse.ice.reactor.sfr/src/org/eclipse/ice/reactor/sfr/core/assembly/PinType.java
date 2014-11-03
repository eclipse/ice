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

/**
 * <!-- begin-UML-doc --> This enumeration represents allowed types of pins for
 * a PinAssembly. Currently, the PinType corresponds to an attribute of a
 * PinAssembly. <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum PinType {
	/**
	 * <!-- begin-UML-doc --> A primary control pin. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	PrimaryControl("Primary Control", 0),
	/**
	 * <!-- begin-UML-doc --> A secondary (shutdown) control pin. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SecondaryControl("Secondary Control", 1),
	/**
	 * <!-- begin-UML-doc --> An inner core pin containing burnable fuel. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	InnerFuel("Inner Fuel", 2),
	/**
	 * <!-- begin-UML-doc --> An outer core pin containing burnable fuel. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	OuterFuel("Outer Fuel", 3),
	/**
	 * <!-- begin-UML-doc --> A shielding pin. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Shield("Shield", 4),
	/**
	 * <!-- begin-UML-doc --> A pin for conducting materials testing. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	MaterialTest("Material Test", 5),
	/**
	 * <!-- begin-UML-doc --> A pin for conducting fuels testing. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	FuelTest("Fuel Test", 6),
	/**
	 * <!-- begin-UML-doc --> A pin containing fertile blanket fuel. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	BlanketFuel("Blanket Fuel", 7);

	/**
	 * <!-- begin-UML-doc --> A user-friendly String for displaying an PinType
	 * value. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private final String name;
	/**
	 * <!-- begin-UML-doc --> An ID used to distinguish between enum values in
	 * lieu of using their String names. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private final int id;

	/**
	 * <!-- begin-UML-doc --> The default constructor for a PinType. <!--
	 * end-UML-doc -->
	 * 
	 * @param name
	 *            A string representation of the pin type.
	 * @param id
	 *            A unique ID for the pin type.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private PinType(String name, int id) {
		// begin-user-code
		// Set the user-friendly name String for this pin type.
		this.name = name;

		// Set the ID used for this pin type.
		this.id = id;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Override the default toString() behavior to give
	 * the user a better-formatted String representing this PinType. <!--
	 * end-UML-doc -->
	 * 
	 * @return Returns a string representation of the pin type.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public String toString() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Gets the Integer ID associated with this enum
	 * value. The ID is unique for each possible enum value. <!-- end-UML-doc
	 * -->
	 * 
	 * @return The Integer ID for the enum value.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getId() {
		// begin-user-code
		return this.id;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> Gets an PinType enum value for the corresponding
	 * Integer ID. If the ID is invalid, the CoreFuel type is returned. <!--
	 * end-UML-doc -->
	 * 
	 * @param id
	 *            The integer ID to convert to a PinType value.
	 * @return The PinType value for the provided ID, or PinType.CoreFuel if the
	 *         ID is invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static PinType valueOf(int id) {
		// begin-user-code

		// The default return value is InnerFuel.
		PinType value = InnerFuel;

		// Set the value depending on the id.
		switch (id) {
		case 0:
			value = PrimaryControl;
			break;
		case 1:
			value = SecondaryControl;
			break;
		case 3:
			value = OuterFuel;
			break;
		case 4:
			value = Shield;
			break;
		case 5:
			value = MaterialTest;
			break;
		case 6:
			value = FuelTest;
			break;
		case 7:
			value = BlanketFuel;
			break;
		default:
			break;
		}

		// Return the enum value.
		return value;
		// end-user-code
	}
}