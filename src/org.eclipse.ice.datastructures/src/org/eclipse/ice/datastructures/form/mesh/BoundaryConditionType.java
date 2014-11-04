/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.datastructures.form.mesh;

import java.util.HashMap;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This enumeration provides supported boundary conditions. Currently, these are
 * based on Nek5000 boundary conditions. See
 * http://nek5000.mcs.anl.gov/index.php/Boundary_Definition for more details on
 * the boundary conditions.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum BoundaryConditionType {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * No boundary condition specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	None("None", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Internal (element connectivity). Requires 2 parameters: adjacent element
	 * and face.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Internal("E", 2),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Periodic. Requires 2 parameters: periodic element and face.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Periodic("P", 2),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Dirichlet temperature/scalar. Requires 1 parameter: value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	DirichletTemperatureScalar("T", 1),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Dirichlet velocity. Requires 3 parameters: u, v, and w.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	DirichletVelocity("V", 3),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Outflow. Requires 0 parameters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Outflow("O", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Wall (no slip). Requires 0 parameters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Wall("W", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Flux. Requires 1 parameter: flux.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Flux("F", 1),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Symmetry. Requires 0 parameters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Symmetry("SYM", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Axisymmetric boundary. Requires 0 parameters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	AxisymmetricBoundary("A", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Moving boundary. Requires 0 parameters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	MovingBoundary("MS", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Insulated (zero flux) for temperature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Insulated("I", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Outflow, Normal (need surface to be normal to x, y, or z). Requires 0
	 * parameters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	OutflowNormal("ON", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * User defined Dirichlet temperature. Assigned a value in USERBC.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	UserDirichletVelocity("v", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * User defined Dirichlet temperature. Assigned a value in USERBC.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	UserDirichletTemperature("t", 0),
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * User defined flux. Assigned a value in USERBC.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	UserFlux("f", 0);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This attribute stores a human-readable reason for rejecting an invalid
	 * value passed to setValue(). It may be retrieved by calling
	 * getErrorMessage().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String id;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Specifies the number of parameters required for the simulation. This
	 * number should vary between 0 and 5, inclusive, since there are only 5
	 * values read in for each boundary condition.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int numberOfParameters;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A map for quickly looking up BoundaryConditionTypes based on their ID.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static HashMap<String, BoundaryConditionType> idMap;

	/**
	 * Since our enum is reasonably large, we want to avoid a huge switch. The
	 * code below initializes the static map of the IDs to their corresponding
	 * BoundaryConditionTypes.
	 */
	static {
		// begin-user-code
		idMap = new HashMap<String, BoundaryConditionType>();
		for (BoundaryConditionType type : BoundaryConditionType.values()) {
			idMap.put(type.id, type);
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default, private enum constructor. It must initialize all private
	 * variables.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            The string ID for the boundary condition type. This is used in
	 *            the files storing the boundary conditions as a short, unique
	 *            ID.
	 * @param nParameters
	 *            Specifies the number of parameters required for the
	 *            simulation. This number should vary between 0 and 5,
	 *            inclusive, since there are only 5 values read in for each
	 *            boundary condition.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private BoundaryConditionType(String id, int nParameters) {
		// begin-user-code
		this.id = id;
		this.numberOfParameters = nParameters;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets a BoundaryConditionType from a string ID.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            The ID of the boundary condition. This is usually a one or two
	 *            character string.
	 * @return The BoundaryConditionType corresponding to the string ID, or null
	 *         if the ID is invalid.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static BoundaryConditionType fromId(String id) {
		// begin-user-code
		return idMap.get(id);
		// end-user-code
	}
}