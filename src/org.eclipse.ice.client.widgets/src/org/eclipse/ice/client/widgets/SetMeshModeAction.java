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
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.client.widgets.mesh.MeshApplication;
import org.eclipse.ice.client.widgets.mesh.MeshApplicationMode;

import org.eclipse.jface.action.Action;

/**
 * This class provides a means for switching between mesh editor modes.
 * 
 * @author tnp
 */
public class SetMeshModeAction extends Action {

	/**
	 * The application for which this action sets the mode.
	 */
	private final MeshApplication application;

	/**
	 * The MeshApplicationMode that is set for a jME3 application.
	 */
	private final MeshApplicationMode mode;

	/**
	 * The default constructor.
	 * 
	 * @param parentApp
	 *            The parent MeshApplication that will have its mode set.
	 * @param mode
	 *            The MeshApplicationMode to assign to the MeshApplication for
	 *            this action.
	 */
	public SetMeshModeAction(MeshApplication parentApp, MeshApplicationMode mode) {

		// Set the jME3 application
		application = parentApp;
		this.mode = mode;

		// Use the super class' methods to set this action's text and tool tip.
		setText(mode.getName());
		setToolTipText(mode.getDescription());

		return;
	}

	/**
	 * The function called whenever the action is selected.
	 */
	@Override
	public void run() {

		application.setMode(mode);

		return;
	}

}
