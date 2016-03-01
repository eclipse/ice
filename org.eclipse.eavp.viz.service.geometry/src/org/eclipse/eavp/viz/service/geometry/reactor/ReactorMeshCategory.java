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

import org.eclipse.eavp.viz.service.modeling.IMeshCategory;

/**
 * An enumeration of additional category types used for modeling reactors.
 * 
 * @author Robert Smith
 *
 */
public enum ReactorMeshCategory implements IMeshCategory {

	/**
	 * A category for the pipes which form the core channels of reactors.
	 */
	CORE_CHANNELS,

	/**
	 * A category for related heat exchangers.
	 */
	HEAT_EXCHANGERS,

	/**
	 * A category for parts providing input.
	 */
	INPUT,

	/**
	 * A category for related junctions.
	 */
	JUNCTIONS,

	/**
	 * A category for parts receiving output.
	 */
	OUTPUT,

	/**
	 * A category for related pipes.
	 */
	PIPES,

	/**
	 * A category for a part's primary pipe.
	 */
	PRIMARY_PIPE,

	/**
	 * A category for related reactors.
	 */
	REACTORS,

	/**
	 * A category for parts providing input through the SECONDARY_PIPE.
	 */
	SECONDARY_INPUT,

	/**
	 * A category for parts receiving output from the SECONDARY_PIPE.
	 */
	SECONDARY_OUTPUT,

	/**
	 * A category for a part's secondary pipe.
	 */
	SECONDARY_PIPE,
}
