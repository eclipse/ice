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
 * This class provides a button for deleting selected elements.
 * 
 * @author tnp
 */
public class DeleteMeshElementAction extends Action {

	/**
	 * The application for which this action sets the mode.
	 */
	private final MeshApplication application;

	/**
	 * The default constructor.
	 * 
	 * @param parentApp
	 *            The parent MeshApplication containing the MeshComponent to
	 *            have its elements deleted.
	 */
	public DeleteMeshElementAction(MeshApplication parentApp) {

		// Set the jME3 application
		application = parentApp;

		// Use the super class' methods to set this action's text and tool tip.
		setText("Delete");
		setToolTipText("Remove the selected element from the mesh");

		return;
	}

	/**
	 * The function called whenever the action is selected.
	 */
	@Override
	public void run() {

		// Call the MeshApplication's method for deleting selections
		application.getSelectionManager().deleteSelection();
		
		return;
	}

}
