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
package org.eclipse.eavp.viz.service.connections.preferences;

/**
 * An {@code IKeyChangeListener} is designed specifically to listen to a
 * {@link IKeyManager} for key change events, e.g., adding, removing, or
 * changing keys.
 * 
 * @author Jordan Deyton
 *
 */
public interface IKeyChangeListener {

	/**
	 * This method is called with the specified keys have somehow been
	 * manipulated.
	 * 
	 * @param oldKey
	 *            The previous value of the key. This is {@code null} if the key
	 *            did not previously exist. For "discrete" key managers, this
	 *            key was available previously but has been taken.
	 * @param newKey
	 *            The new value of the key. This is {@code null} if the key no
	 *            longer exists. For "discrete" key managers, this key was taken
	 *            previously but is now available.
	 */
	public void keyChanged(String oldKey, String newKey);

}
