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

import org.eclipse.jface.action.Action;

/**
 * This class provides a means for resetting the mesh editor camera.
 * 
 * @author tnp
 */
public class SetMeshCameraAction extends Action {

	/**
	 * The application for which this action sets the mode.
	 */
	private final MeshApplication application;

	/**
	 * The default constructor.
	 * 
	 * @param parentApp
	 *            The parent MeshApplication that will have its camera reset.
	 * @param displayText
	 *            The text displayed by this Action.
	 */
	public SetMeshCameraAction(MeshApplication parentApp, String displayText) {

		// Set the jME3 application
		application = parentApp;

		// Use the super class' methods to set this action's text and tool tip.
		setText(displayText);
		setToolTipText(displayText);

		return;
	}

	/**
	 * The function called whenever the action is selected.
	 */
	@Override
	public void run() {

		return;
	}

}
