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

/** 
 * <!-- begin-UML-doc -->
 * <p>The MaterialType enumeration describes each type of material phase.</p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum MaterialType {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates a gas material phase.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	GAS("Gas"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates a liquid material phase.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	LIQUID("Liquid"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates a solid material phase.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SOLID("Solid");

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Human readable string associated with enumerated value.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor.</p>
	 * <!-- end-UML-doc -->
	 * @param name
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	MaterialType(String name) {
		// begin-user-code

		this.name = name;

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the type of enumeration keyed on name.  Returns null if invalid name.</p>
	 * <!-- end-UML-doc -->
	 * @param name <p>The name associated with the enumerated value.</p>
	 * @return <p>The type of enumeration.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public MaterialType toType(String name) {
		// begin-user-code

		//Cycle over all types
		for (MaterialType p : values()) {

			//If this property's name equals name
			if (p.name.equals(name)) {

				//Return the property
				return p;
			}
		}

		//If not found return null
		return null;

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the string name of the enumerated value.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The name of the enumerated value.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String toString() {
		// begin-user-code

		return name;

		// end-user-code
	}
}