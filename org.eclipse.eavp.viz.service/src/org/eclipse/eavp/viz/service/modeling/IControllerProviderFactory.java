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
 * A interface for classes which serve IControllerProviders. An
 * IControllerFactory takes as input an IMesh and returns an IControllerProvider
 * which is capable of processing that IMesh. An IControllerFactory
 * implementation should be specific as to what kind of view and/or controller
 * its IControllerProviders create, as the same IMesh may be valid for use with
 * multiple separate implementations of IView and IController.
 * 
 * @author Robert Smith
 *
 */
public interface IControllerProviderFactory {

	/**
	 * Creates a controller and associated view for the given model.
	 * 
	 * @param model
	 *            The model for which a controller will be created
	 * @return The new controller, which contains the input model and the new
	 *         view
	 */
	public IControllerProvider createProvider(IMesh model);
}
