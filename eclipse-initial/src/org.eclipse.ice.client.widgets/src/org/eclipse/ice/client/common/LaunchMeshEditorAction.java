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
package org.eclipse.ice.client.common;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.osgi.framework.Bundle;

/**
 * This class provides an Action that can be used to launch a Mesh Editor. More
 * specifically, this action creates a Mesh Editor item, which produces the 2D
 * Mesh Editor.
 * 
 * @author djg
 * 
 */
public class LaunchMeshEditorAction extends Action implements IWorkbenchAction {

	/**
	 * The Action's ID.
	 */
	public static final String ID = "org.eclipse.ice.client.common.launchMeshEditorAction";

	/**
	 * The default constructor. This configures the Action's text, tool tip,
	 * image, and more.
	 */
	public LaunchMeshEditorAction() {

		// Set the ID of the action.
		setId(ID);

		// FIXME - We may want to add a hot-key for this action.

		// Set the text and tool tip.
		setText("Launch the Mesh Editor");
		setToolTipText("Launch the MeshEditor for " + "editing 2D meshes.");

		// Set the image used for the button to the custom Mesh gif.
		Bundle bundle = Platform
				.getBundle("org.eclipse.ice.client.widgets");
		URL url = FileLocator.find(bundle, new Path("icons/mesh.gif"), null);
		setImageDescriptor(ImageDescriptor.createFromURL(url));

		return;
	}

	/**
	 * Overrides the JFace Action's run method to create a Mesh Editor item.
	 */
	@Override
	public void run() {

		// Get the client
		IClient client = ClientHolder.getClient();

		// Create a new Mesh Item if the editor Items are available.
		if (client != null
				&& client.getAvailableItemTypes().contains("Mesh Editor")) {
			client.createItem("Mesh Editor");
		} else {
			client.throwSimpleError("The Mesh Editor is unavailable!");
		}

		return;
	}

	/**
	 * Implements IWorkbenchAction's only method. Currently, there is nothing to
	 * do here.
	 */
	public void dispose() {
		// Nothing to do.
	}

}
