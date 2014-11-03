/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.osgi.framework.Bundle;

/**
 * ConnectCoreAction inherits JFace Action and defines the action that occurs
 * when a user attempts to connect Client to a Core
 * 
 * @author bkj
 * 
 */
public class ConnectCoreAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	// Handle to the workbench window
	private final IWorkbenchWindow workbenchWindow;

	// Action ID - for internal use
	public static final String ID = "org.eclipse.ice.client.common.connectAction";

	// CoreConnectDialog that presents a login screen
	// to the user
	private CoreConnectDialog login;

	// Constructor
	public ConnectCoreAction(IWorkbenchWindow window) {

		// Local Declarations
		Bundle bundle = null;
		Path serverPath = null;
		URL serverImageURL = null;
		ImageDescriptor serverImage = null;

		// Find the client bundle
		bundle = Platform.getBundle("org.eclipse.ice.client.widgets");

		// Create the image descriptor for the create item action
		serverPath = new Path("icons/server.gif");
		serverImageURL = FileLocator.find(bundle, serverPath, null);
		serverImage = ImageDescriptor.createFromURL(serverImageURL);

		// Set the action ID, Menu Text, and a tool tip.
		workbenchWindow = window;
		setId(ID);
		setText("&Connect to Core...");
		setToolTipText("Open connection to Core with " + "hostname and port...");
		setImageDescriptor(serverImage);

		// Add this class as a listener to the window events
		workbenchWindow.getSelectionService().addSelectionListener(this);

		return;
	}

	@Override
	public void dispose() {
		this.workbenchWindow.getSelectionService()
				.removeSelectionListener(this);
	}

	@Override
	public void run() {
		// Create a new CoreConnectDialog to prompt for login credentials
		login = new CoreConnectDialog(new Shell(PlatformUI.getWorkbench()
				.getDisplay()));
		// Open the Dialog
		login.open();

		// Test that the port and hostname was received correctly and run
		// direct ICE to connect to the server
		String hostname = login.getHostname();
		String port = login.getPort();
		if (login.getHostname() != null && login.getPort() != null) {
			ClientHolder.getClient().connectToCore(hostname,
					Integer.parseInt(port));
		}
		return;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
