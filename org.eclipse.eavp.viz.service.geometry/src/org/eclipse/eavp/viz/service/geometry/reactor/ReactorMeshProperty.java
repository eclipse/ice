/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.geometry.reactor;

import org.eclipse.eavp.viz.service.modeling.IMeshProperty;

/**
 * Additional IMesh properties used by Plant View parts.
 * 
 * @author Robert Smith
 *
 */
public enum ReactorMeshProperty implements IMeshProperty {

	/**
	 * Defines whether a PipeMesh is the core channel of a ReactorMesh. Expected
	 * values are "True" and "False".
	 */
	CORE_CHANNEL,

	/**
	 * A numerical property for the part's height.
	 */
	HEIGHT,

	/**
	 * A numerical property for the number of rods contained by the part.
	 */
	NUM_RODS,

	/**
	 * A property for the part's pitch.
	 */
	PITCH,

	/**
	 * A numerical property for the diameter of the part's contained rods.
	 */
	ROD_DIAMETER,

	/**
	 * A part's z intake.
	 */
	Z_INTAKE,

	/**
	 * A part's z output.
	 */
	Z_OUTPUT
}
