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
package org.eclipse.eavp.viz.service.mesh.datastructures;

import java.util.HashMap;

/**
 * <p>
 * This enumeration provides supported boundary conditions. Currently, these are
 * based on Nek5000 boundary conditions. See
 * http://nek5000.mcs.anl.gov/index.php/Boundary_Definition for more details on
 * the boundary conditions.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public enum BoundaryConditionType {
	/**
	 * <p>
	 * No boundary condition specified.
	 * </p>
	 * 
	 */
	None("None", 0),
	/**
	 * <p>
	 * Internal (element connectivity). Requires 2 parameters: adjacent element
	 * and face.
	 * </p>
	 * 
	 */
	Internal("E", 2),
	/**
	 * <p>
	 * Periodic. Requires 2 parameters: periodic element and face.
	 * </p>
	 * 
	 */
	Periodic("P", 2),
	/**
	 * <p>
	 * Dirichlet temperature/scalar. Requires 1 parameter: value.
	 * </p>
	 * 
	 */
	DirichletTemperatureScalar("T", 1),
	/**
	 * <p>
	 * Dirichlet velocity. Requires 3 parameters: u, v, and w.
	 * </p>
	 * 
	 */
	DirichletVelocity("V", 3),
	/**
	 * <p>
	 * Outflow. Requires 0 parameters.
	 * </p>
	 * 
	 */
	Outflow("O", 0),
	/**
	 * <p>
	 * Wall (no slip). Requires 0 parameters.
	 * </p>
	 * 
	 */
	Wall("W", 0),
	/**
	 * <p>
	 * Flux. Requires 1 parameter: flux.
	 * </p>
	 * 
	 */
	Flux("F", 1),
	/**
	 * <p>
	 * Symmetry. Requires 0 parameters.
	 * </p>
	 * 
	 */
	Symmetry("SYM", 0),
	/**
	 * <p>
	 * Axisymmetric boundary. Requires 0 parameters.
	 * </p>
	 * 
	 */
	AxisymmetricBoundary("A", 0),
	/**
	 * <p>
	 * Moving boundary. Requires 0 parameters.
	 * </p>
	 * 
	 */
	MovingBoundary("MS", 0),
	/**
	 * <p>
	 * Insulated (zero flux) for temperature.
	 * </p>
	 * 
	 */
	Insulated("I", 0),
	/**
	 * <p>
	 * Outflow, Normal (need surface to be normal to x, y, or z). Requires 0
	 * parameters.
	 * </p>
	 * 
	 */
	OutflowNormal("ON", 0),
	/**
	 * <p>
	 * User defined Dirichlet temperature. Assigned a value in USERBC.
	 * </p>
	 * 
	 */
	UserDirichletVelocity("v", 0),
	/**
	 * <p>
	 * User defined Dirichlet temperature. Assigned a value in USERBC.
	 * </p>
	 * 
	 */
	UserDirichletTemperature("t", 0),
	/**
	 * <p>
	 * User defined flux. Assigned a value in USERBC.
	 * </p>
	 * 
	 */
	UserFlux("f", 0);

	/**
	 * <p>
	 * This attribute stores a human-readable reason for rejecting an invalid
	 * value passed to setValue(). It may be retrieved by calling
	 * getErrorMessage().
	 * </p>
	 * 
	 */
	public String id;

	/**
	 * <p>
	 * Specifies the number of parameters required for the simulation. This
	 * number should vary between 0 and 5, inclusive, since there are only 5
	 * values read in for each boundary condition.
	 * </p>
	 * 
	 */
	public int numberOfParameters;

	/**
	 * <p>
	 * A map for quickly looking up BoundaryConditionTypes based on their ID.
	 * </p>
	 * 
	 */
	private static HashMap<String, BoundaryConditionType> idMap;

	/**
	 * Since our enum is reasonably large, we want to avoid a huge switch. The
	 * code below initializes the static map of the IDs to their corresponding
	 * BoundaryConditionTypes.
	 */
	static {
		idMap = new HashMap<String, BoundaryConditionType>();
		for (BoundaryConditionType type : BoundaryConditionType.values()) {
			idMap.put(type.id, type);
		}
	}

	/**
	 * <p>
	 * The default, private enum constructor. It must initialize all private
	 * variables.
	 * </p>
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
	 */
	private BoundaryConditionType(String id, int nParameters) {
		this.id = id;
		this.numberOfParameters = nParameters;
	}

	/**
	 * <p>
	 * Gets a BoundaryConditionType from a string ID.
	 * </p>
	 * 
	 * @param id
	 *            The ID of the boundary condition. This is usually a one or two
	 *            character string.
	 * @return The BoundaryConditionType corresponding to the string ID, or null
	 *         if the ID is invalid.
	 */
	public static BoundaryConditionType fromId(String id) {
		return idMap.get(id);
	}
}