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
package org.eclipse.ice.client.widgets.mesh;


/**
 * This is an interface for classes that need to listen to particular camera
 * events. The need for this interface arose to allow the Mesh Editor to resize
 * spatials depending on the current zoom level when calls to the camera's
 * getDistanceToTarget() method do not provide the correct distance until after
 * the camera's update() method is called from the application's simpleUpdate().
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface ICameraListener {

	/**
	 * The camera's zoom or distance from its target (typically the player) has
	 * changed.
	 * 
	 * @param distance
	 *            The camera's new distance from the target.
	 */
	public void zoomChanged(float distance);
	
}
