/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import org.eclipse.eavp.viz.service.modeling.AbstractController;

/**
 * An interface for objects which contain the data which will be set to an
 * IPlantView.
 * 
 * @author Robert Smith
 *
 */
public interface IPlantData {

	/**
	 * Get the plant's structure converted into a tree of modeling parts.
	 * 
	 * @return The root of the tree structure containing the plant.
	 */
	public AbstractController getPlant();

}
