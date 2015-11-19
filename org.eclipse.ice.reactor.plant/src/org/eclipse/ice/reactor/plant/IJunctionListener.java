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
package org.eclipse.ice.reactor.plant;

import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;

/**
 * <p>
 * This interface is for clients that need to listen to {@link IJunction}s for
 * updates.
 * </p>
 * <p>
 * Normally, components that extend {@link ICEObject} will use its methods to
 * notify listeners of changes. However, its methods are "blanket" notifications
 * that just mean <i>something</i> has changed, but not what.
 * 
 * In the interest of speeding up reactions to changes in Junction's pipes, this
 * interface provides notification methods for when a Junction adds or removes
 * pipes.
 * </p>
 * 
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IJunctionListener {
	// TODO Add this IJunctionListener to the model.
	
	/**
	 * PlantComponents have been added to the Junction. The IJunctionListener
	 * should updated based on which PlantComponents were added.
	 * 
	 * @param junction
	 *            The Junction that was updated.
	 * @param pipes
	 *            A List of PlantComponents that were added to the Junction.
	 */
	public void addedPipes(IJunction junction, List<PlantComponent> pipes);

	/**
	 * PlantComponents have been removed from the Junction. The
	 * IJunctionListener should update based on which PlantComponents were
	 * removed.
	 * 
	 * @param junction
	 *            The Junction that was updated.
	 * @param pipes
	 *            A List of PlantComponents that were removed from the Junction.
	 */
	public void removedPipes(IJunction junction, List<PlantComponent> pipes);

}
