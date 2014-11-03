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
package org.eclipse.ice.client.rcp.actions;

import java.io.File;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

// SwitchWorkspaceAction defines the action needed for a user to switch from the currently 
// selected workspace to a new one. 
public class SwitchWorkspaceAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	// Handle to the workbench window
	private final IWorkbenchWindow window;

	// Action ID
	private static final String ID = "org.eclipse.ice.client.rcp.actions.SwitchWorkspaceAction";

	// Dialog to prompt user for new workspace
	private ChooseWorkspaceDialog dialog;

	// Constructor
	public SwitchWorkspaceAction(IWorkbenchWindow window) {
		// Set the action ID, Menu Text, and a tool tip.
		// Add this class as a listener to the window events
		this.window = window;
		this.setId(ID);
		this.setText("&Switch Workspace");
		this.setToolTipText("Switch to a new Workspace location...");
		this.window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void run() {
		// Create the workspace dialog, with true as an argument indicating
		// this is "Switch Workspace" and not the initial start up set workspace
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL fullPath = BundleUtility.find(bundle, "icons/Cool-icon.png");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(fullPath);
		Image icon = descriptor.createImage();

		dialog = new ChooseWorkspaceDialog(true, icon);

		// Open the dialog and get the button clicked
		int click = dialog.open();
		// If the button was cancel...
		if (click == Window.CANCEL) {
			// Do nothing
			return;
		} else {
			// If it was ok, and the workspace was valid, open a prompt
			// informing the user
			// that the client is restarting but will open with the new
			// workspace
			MessageDialog.openInformation(
					Display.getDefault().getActiveShell(), "Switch Workspace",
					"The client will now restart with the new workspace.");
			// Restart the workbench
			PlatformUI.getWorkbench().restart();
		}
		return;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// Not needed now
	}

}
