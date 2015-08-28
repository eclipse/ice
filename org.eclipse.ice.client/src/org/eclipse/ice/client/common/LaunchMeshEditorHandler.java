/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *    Jay Jay Billings (UT-Battelle, LLC.) - relocated from 
 *      org.eclipse.ice.client.widgets bundle
 *******************************************************************************/
package org.eclipse.ice.client.common;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.iclient.IClient;

/**
 * This class provides a handler for launching the MeshEditor. The handler is
 * used to create a command in the main Eclipse UI ToolBar via extensions (see
 * the plugin extensions).
 * 
 * @author Jordan
 * 
 */
public class LaunchMeshEditorHandler extends AbstractHandler {

	/**
	 * Opens a new MeshEditor if possible.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the client
		IClient client = ClientHolder.getClient();

		// Create a new Mesh Item if the editor Items are available.
		if (client != null
				&& client.getAvailableItemTypes().contains("Mesh Editor")) {
			client.createItem("Mesh Editor");
		} else {
			client.throwSimpleError("The Mesh Editor is unavailable!");
		}

		return null;
	}

}
