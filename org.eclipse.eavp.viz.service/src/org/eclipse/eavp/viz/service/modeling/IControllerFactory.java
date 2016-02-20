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
package org.eclipse.eavp.viz.service.modeling;

/**
 * A interface for classes automating the creation of AbstractControllers. An
 * IControllerFactory takes as input an AbstractMeshComponent and creates an
 * appropriate AbstractView for it. The factory then creates an
 * AbstractController to manage both of them. An IControllerFactory
 * implementation should be specific as to what kind of view and/or controller
 * it creates, as the same AbstractMeshComponent may be valid for use with
 * multiple separate implementations of Abstract View and AbstractController.
 * 
 * @author Robert Smith
 *
 */
public interface IControllerFactory {

	/**
	 * Creates a controller and associated view for the given model.
	 * 
	 * @param model
	 *            The model for which a controller will be created
	 * @return The new controller, which contains the input model and the new
	 *         view
	 */
	public AbstractController createController(AbstractMesh model);
}
