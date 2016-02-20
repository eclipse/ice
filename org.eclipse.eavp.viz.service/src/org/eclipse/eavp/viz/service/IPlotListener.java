/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service;

/**
 * This class provides a simple key/value update listener that can be added to
 * {@link AbstractPlot}s. This is particularly useful when a plot update
 * triggered by another thread requires a UI component to refresh.
 * 
 * @author Jordan
 *
 */
public interface IPlotListener {

	/**
	 * Notifies the listener that the plot has been updated along with state
	 * information that the listener may be able to use.
	 * 
	 * @param plot
	 *            The plot that was updated.
	 * @param key
	 *            The key for the update event that occurred.
	 * @param value
	 *            An associated value for the update event that occurred.
	 */
	public void plotUpdated(IPlot plot, String key, String value);

}
