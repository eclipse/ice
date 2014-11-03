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
 * This class provides a means to show or hide the mesh editor's heads-up
 * display.
 * 
 * @author tnp
 */
public class ShowHideHUDAction extends Action {

	/**
	 * The application for which this action sets the mode.
	 */
	private final MeshApplication application;

	/**
	 * The default constructor.
	 * 
	 * @param parentApp
	 *            The parent MeshApplication that will have its HUD shown or
	 *            hidden.
	 */
	public ShowHideHUDAction(MeshApplication parentApp) {

		// Set the jME3 application
		application = parentApp;

		// Use the super class' methods to set this actions's text and tool tip.
		// If the HUD is currently displayed, the button should offer to hide
		// the HUD. If the HUD is hidden, the button should offer to show the
		// HUD.
		setText("Hide HUD");
		setToolTipText("Hide the heads-up display");

		return;
	}

	/**
	 * The function called whenever the action is selected.
	 */
	@Override
	public void run() {

		// If the HUD is currently displayed...
		if (application.getDisplayHUD()) {
			// Set the HUD to be hidden
			application.setDisplayHUD(false);
			// Update the button text
			// Since we just set the HUD to be hidden, the button should offer
			// to show the HUD.
			setText("Show HUD");
			setToolTipText("Show the heads-up display");
		}
		// If the HUD is currently hidden...
		else {
			// Set the HUD to be shown
			application.setDisplayHUD(true);
			// Update the button text
			// Since we just set the HUD to be shown, the button should offer to
			// hide the HUD.
			setText("Hide HUD");
			setToolTipText("Hide the heads-up display");
		}

		return;
	}

}
