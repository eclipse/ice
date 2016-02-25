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
package org.eclipse.eavp.viz.service.mesh.datastructures;

import org.eclipse.eavp.viz.service.IVizCanvas;

/**
 * An extension of IVizCanvas which provides functionality needed for a Mesh to
 * interact with the Mesh Editor.
 * 
 * @author Robert Smith
 *
 */
public interface IMeshVizCanvas extends IVizCanvas {

	/**
	 * Set the canvas's mode of user interaction between creating new objects
	 * and editing those already present.
	 * 
	 * @param edit
	 *            Whether the canvas should be set to edit mode. If false, the
	 *            canvas will register clicks as commands to create new vertices
	 *            and combine them into polygons. If true, the canvas will allow
	 *            the user to select pre-existing vertices/edges/polygons by
	 *            clicking on them.
	 */
	public void setEditMode(boolean edit);

	/**
	 * Set the canvas's display of relevant coordinates on or off.
	 * 
	 * @param on
	 *            Whether or not to render the heads up display. If true, the
	 *            canvas will display the coordinates for the locations of the
	 *            mouse pointer and center of the camera's view. If false, it
	 *            will not display them.
	 */
	public void setVisibleHUD(boolean on);

	/**
	 * Checks whether the HUD is currently visible.
	 * 
	 * @return True if the HUD is being rendered on screen. False if not.
	 */
	public boolean HUDIsVisible();

	/**
	 * Set the canvas's display of the axis.
	 * 
	 * @param on
	 *            Whether or not the canvas will render the x, y, and z axis.
	 */
	public void setVisibleAxis(boolean on);

	/**
	 * Checks whether the axis are visible
	 * 
	 * @return True if the axis are being rendered. False if not.
	 */
	public boolean AxisAreVisible();

	/**
	 * Delete all selected objects.
	 */
	public void deleteSelection();

	/**
	 * Set the canvas's selection to be the set of objects in the input.
	 * 
	 * @param selection
	 *            The set of objects which should comprise the canvas's new
	 *            selection
	 */
	public void setSelection(Object[] selection);
}
