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
package org.eclipse.ice.viz.service.jme3.mesh;

import com.jme3.math.ColorRGBA;

/**
 * <p>
 * This enumeration provides a list of possible states for Geometries displayed
 * in a jME3 scene. The states should be reflected somehow in the view, but may
 * or may not have any direct tie to the corresponding model.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public enum StateType {
	/**
	 * <p>
	 * The view has the normal or default features.
	 * </p>
	 * 
	 */
	None(ColorRGBA.Red),
	/**
	 * <p>
	 * The view has been selected or has focus.
	 * </p>
	 * 
	 */
	Selected(ColorRGBA.Green),
	/**
	 * <p>
	 * The view is only temporary.
	 * </p>
	 * 
	 */
	Temporary(ColorRGBA.Yellow),
	/**
	 * <p>
	 * The view is disabled.
	 * </p>
	 * 
	 */
	Disabled(ColorRGBA.Orange);

	/**
	 * The color associated with this StateType.
	 */
	private final ColorRGBA color;

	private StateType(ColorRGBA color) {
		this.color = color;

		return;
	}

	/**
	 * Gets the color associated with this StateType.
	 * 
	 * @return A jME3 ColorRGBA.
	 */
	public ColorRGBA getColor() {
		return color;
	}
}