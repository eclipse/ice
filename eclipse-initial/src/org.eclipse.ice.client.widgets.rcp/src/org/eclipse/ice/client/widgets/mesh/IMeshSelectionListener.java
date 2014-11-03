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
 * This interface provides a listener to be notified when the current selection
 * in a jME3-based MeshApplication has changed. The listener should be able to
 * pull information about the current selection from the MeshApplication.
 * 
 * @author djg
 * 
 */
public interface IMeshSelectionListener {

	/**
	 * This method is called by the MeshApplication when its current selection
	 * has somehow changed. This includes selected vertices and edges.
	 */
	public void selectionChanged();
}
