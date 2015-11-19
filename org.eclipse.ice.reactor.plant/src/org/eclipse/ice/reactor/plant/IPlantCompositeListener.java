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

/**
 * This is an interface for listening for updates to {@link PlantComposite}s.
 * The PlantComposite should use these notifications when components are added
 * or removed so that the listener does not need to look for the components that
 * have been removed or added.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IPlantCompositeListener {

	/**
	 * PlantComponents have been added to the PlantComposite.
	 * 
	 * @param composite
	 *            The PlantComposite that has gained PlantComponents.
	 * @param added
	 *            A list of the added PlantComponents.
	 */
	public void addedComponents(PlantComposite composite,
			List<PlantComponent> added);

	/**
	 * PlantComponents have been removed the PlantComposite.
	 * 
	 * @param composite
	 *            The PlantComposite that has removed PlantComponents.
	 * @param removed
	 *            A list of the removed PlantComponents.
	 */
	public void removedComponents(PlantComposite composite,
			List<PlantComponent> removed);
}
