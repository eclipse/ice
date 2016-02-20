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
package org.eclipse.eavp.viz.service.modeling;

import java.util.HashMap;
import java.util.Map;

/**
 * A base implementation of IControllerFactory, which structures the factory as
 * a map between the types of input objects and provider classes.
 * 
 * @author Robert Smith
 *
 */
public class AbstractControllerFactory implements IControllerFactory {

	/**
	 * A map from class types to IControllerProviders, where each entry maps a
	 * type to the IControllerProvider which handles the creation of controllers
	 * for objects of that type.
	 */
	protected Map<Class, IControllerProvider> typeMap;

	/**
	 * The default constructor.
	 */
	public AbstractControllerFactory() {
		typeMap = new HashMap<Class, IControllerProvider>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IControllerFactory#createController(
	 * org.eclipse.eavp.viz.service.modeling.AbstractMesh)
	 */
	@Override
	public AbstractController createController(AbstractMesh model) {

		// Get the provider for the model's type
		IControllerProvider provider = typeMap.get(model.getClass());

		// If a provider was found, create a controller and view for the model
		// and return them
		if (provider != null) {
			return provider.createController(model);
		}

		// If none was found, return null.
		else {
			return null;
		}
	}

	/**
	 * An interface for objects which take a certain kind of AbstractMesh and
	 * generate the appropriate views and controllers for it.
	 * 
	 * @author Robert Smith
	 *
	 */
	protected interface IControllerProvider {

		/**
		 * Create a controller and view for the given model.
		 * 
		 * @param model
		 *            The internal representation of the part to create a
		 *            controller and view for.
		 * @return A new AbstractController of a type appropriate to the
		 *         containing IControllerfactory. The controller will contain
		 *         model and a AbstractView on model.
		 */
		public AbstractController createController(AbstractMesh model);
	}
}
