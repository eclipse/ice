/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.geometry.reactor;

/**
 * An interface for AbstractViews which are based on a PipeComponent model. It
 * exposes functionality for getting the pipe's top and bottom extrema for all
 * such Views.
 * 
 * @author Robert Smith
 *
 */
public interface IPipeView {

	/**
	 * Get the farthest points in all three directions for the pipe's lower end
	 * 
	 * @return The pipe's lower boundary's minimum and maximum x, y, and z
	 *         coordinates
	 */
	public Extrema getLowerExtrema();

	/**
	 * Get the farthest points in all three directions for the pipe's upper end
	 * 
	 * @return The pipe's upper boundary's minimum and maximum x, y, and z
	 *         coordinates
	 */
	public Extrema getUpperExtrema();

}
