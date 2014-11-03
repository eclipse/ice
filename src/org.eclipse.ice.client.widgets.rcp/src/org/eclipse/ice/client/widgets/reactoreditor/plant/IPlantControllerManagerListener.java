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
package org.eclipse.ice.client.widgets.reactoreditor.plant;

import org.eclipse.ice.reactor.plant.PlantComponent;

/**
 * This interface is used to listen to a {@link PlantControllerManager} for
 * created or destroyed {@link AbstractPlantController}s for
 * {@link PlantComponent}s. This is particularly useful for
 * {@link JunctionController}s, which must communicate with the
 * {@link PipeController}s for Pipes that are attached to its model Junction.
 * 
 * @author djg
 * 
 */
public interface IPlantControllerManagerListener {

	/**
	 * A controller was created for a PlantComponent.
	 * 
	 * @param component
	 *            The PlantComponent that now has a controller.
	 * @param controller
	 *            The AbstractPlantController for the component.
	 */
	public void addedController(PlantComponent component,
			AbstractPlantController controller);

	/**
	 * A controller was just deleted for a PlantComponent.
	 * 
	 * @param component
	 *            The PlantComponent that no longer has a controller.
	 */
	public void removedController(PlantComponent component);

}
