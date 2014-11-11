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
 * Represents the set operator applied to a ComplexShape
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum OperatorType {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Default operator type
	 * </p>
	 * <p>
	 * When rendering, None should be taken to mean "invisible". A ComplexShape
	 * with this type should have no effect on its parent.
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
	 * Union of any number of sets
	 * </p>
	 * <p>
	 * The result of a union may be disjoint, meaning that the child shapes may
	 * be separated with no points in common.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Union,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Intersection of any number of sets
	 * </p>
	 * <p>
	 * The intersection of more than 2 sets is defined as
	 * </p>
	 * <p>
	 * ((A_1 intersection A_2) intersection A_3) ...
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Intersection,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Complement of any number of sets
	 * </p>
	 * <p>
	 * Unlike the union and intersection operators, the order of shapes is
	 * important when applying the multi-valued complement operator. For more
	 * than two shapes, the multi-valued complement is defined as
	 * </p>
	 * <p>
	 * A_1 / A_2 / A_3 / ..
	 * </p>
	 * <p>
	 * where "/" is the complement operator. The first shape has significance as
	 * the only additive shape in the final result of the operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Complement
}