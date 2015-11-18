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
package org.eclipse.ice.viz.service.modeling;

/**
 * A controller for a Face part.
 * 
 * @author Robert Smith
 *
 */
public class Face extends AbstractController {

	/**
	 * The nullary constructor
	 */
	public Face() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public Face(EdgeComponent model, AbstractView view) {
		super(model, view);
	}

}
