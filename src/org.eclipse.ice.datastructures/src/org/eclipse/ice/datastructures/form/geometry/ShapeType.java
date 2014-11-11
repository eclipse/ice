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
package org.eclipse.ice.datastructures.form.geometry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Represents types of primitive solids
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum ShapeType {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Default shape type
	 * </p>
	 * <p>
	 * When rendering, None should be taken to mean "invisible". A
	 * PrimitiveShape with this type should have no effect on its parent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	None,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A "half-unit" sphere with a radius of 0.5 (diameter of 1) with its origin
	 * at its center
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Sphere,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A 1x1x1 cube with its origin at its center (0.5, 0.5, 0.5)
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Cube,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A cylinder with a radius of 0.5 (diameter of 1), a height of 1, and its
	 * origin at its center (0.5, 0.5, 0.5)
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Cylinder,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A circular cone with a diameter of 1, height of 1, and its center at
	 * (0.5, 0.5, 0.5)
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Cone,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A cylinder with an inner and outer radius, an extruded annulus
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Tube
}