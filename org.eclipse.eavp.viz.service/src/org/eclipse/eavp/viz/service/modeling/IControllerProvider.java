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

/**
 * A interface for classes automating the creation of IControllers. An
 * IControllerProvider takes as input an IMesh and creates an appropriate IView
 * for it. The provider then creates an IController to manage both of them.
 * 
 * @author Robert Smith
 *
 */
public interface IControllerProvider<T extends IController> {

	/**
	 * Create a controller and view for the given model.
	 * 
	 * @param model
	 *            The internal representation of the part to create a controller
	 *            and view for.
	 * @return A new IController of a type appropriate to the
	 *         IControllerProvider's graphics engine. The controller will
	 *         contain model and an IView based on model.
	 */
	public T createController(IMesh model);
}
