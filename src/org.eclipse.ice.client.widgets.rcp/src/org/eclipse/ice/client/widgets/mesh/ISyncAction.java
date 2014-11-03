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
 * This interface is used to perform simple update operations on a jME3
 * SimpleApplication's simpleUpdate() thread. Use of this interface can help
 * avoid unnecessary switches from the application's simpleUpdate() method.
 * 
 * @author djg
 * 
 */
public interface ISyncAction {

	/**
	 * Performs the action that synchronizes the application data with the
	 * scene. This method should only be called from a simpleUpdate() thread.
	 * 
	 * @param tpf
	 *            Time per frame. Standard operating procedure in jME3 uses this
	 *            to adjust the speed of operations depending on the
	 *            application's framerate.
	 */
	public void simpleUpdate(float tpf);
	
}
