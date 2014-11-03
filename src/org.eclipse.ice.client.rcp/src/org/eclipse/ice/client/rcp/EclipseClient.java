/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.rcp;

import java.net.URL;

import org.eclipse.ice.client.rcp.actions.ChooseWorkspaceDialog;

import org.eclipse.ice.iclient.IClient;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class controls all aspects of the application's execution
 */
public class EclipseClient implements IApplication {

	/**
	 * A reference to the SWT display.
	 */
	private Display display;

	/**
	 * A reference to the EclipseUIWidgetFactory that creates Eclipse widgets
	 * for the Client.
	 */
	private IWidgetFactory widgetFactory;

	/**
	 * A reference to the IClient used by the application
	 */
	private static IClient client;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 *      IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {

		// Local Declarations
		String separator = System.getProperty("file.separator");

		// Create the display
		display = PlatformUI.createDisplay();

		try {
			// Create and run the workbench
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new EclipseClientWorkbenchAdvisor());

			// Figure out which return code needs to be set
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			} else {
				return IApplication.EXIT_OK;
			}

		} finally {
			// Kill the display
			display.dispose();
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}

	/**
	 * Prompt the user for a new workspace location, or set the workspace
	 * location as the last directory the user set.
	 */
	private String showWorkspaceDialog(String defaultWorkspaceLoc)
			throws Exception {

		// Local Declarations
		String workspaceLoc = defaultWorkspaceLoc;

		// Get whether the user selected the remember checkbox last time ICE
		// was run
		boolean remembered = ChooseWorkspaceDialog.isRemembered();

		// Get the last workspace selected
		String lastUsedWS = ChooseWorkspaceDialog.getLastWorkspace();
		workspaceLoc = lastUsedWS;

		// If remember was true but the last workspace is invalid, we should set
		// remembered to false so we can ask the user for a new workspace
		if (remembered && (lastUsedWS == null || lastUsedWS.length() == 0)) {
			remembered = false;
		}

		// If there is no workspace remembered...
		if (!remembered) {
			// Open a choose workspace dialog, false indicates its not a switch
			// workspace dialog
			// but instead the default initial one

			// Get workspace dialog icon in a user file system agnostic manner
			Bundle bundle = FrameworkUtil.getBundle(getClass());
			URL fullPath = BundleUtility.find(bundle,
					"icons/iceLogo_small_20110729.png");
			ImageDescriptor descriptor = ImageDescriptor
					.createFromURL(fullPath);
			Image icon = descriptor.createImage();
			ChooseWorkspaceDialog dialog = new ChooseWorkspaceDialog(false,
					icon);

			// Open it and get the integer indicating whether user selected ok
			// or cancel
			int click = dialog.open();
			// if cancel...
			if (click == Window.CANCEL) {
				// Close the workbench and exit the application
				try {
					PlatformUI.getWorkbench().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.exit(0);
			} else { // If they selected ok...
				// Change the applications workspace location to the
				// selected workspace directory
				workspaceLoc = dialog.getWorkspacePath();
			}
		}

		return workspaceLoc;
	}

}
