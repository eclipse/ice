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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.ui.IRemoteUIConnectionService;
import org.eclipse.remote.ui.IRemoteUIConnectionWizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * This class implements the IExtraInfoWidget interface for Eclipse. It creates
 * an ExtraInfoDialog in sync with the Eclipse workbench and it delegates all
 * listener management to the dialog.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseExtraInfoWidget implements IExtraInfoWidget {
	/**
	 * <p>
	 * The Form containing the extra information request.
	 * </p>
	 * 
	 */
	private Form iceForm;

	/**
	 * <p>
	 * The JFace dialog to use for rendering the DataComponent.
	 * </p>
	 * 
	 */
	private ExtraInfoDialog infoDialog;

	/**
	 * <p>
	 * The IWidgetClosedListeners who have subscribed to this widget for
	 * updates. This list is maintained because the ExtraInfoDialog is created
	 * on a separate thread and the listeners can not be automatically forwarded
	 * to it.
	 * </p>
	 * 
	 */
	private ArrayList<IWidgetClosedListener> listeners;

	/**
	 * Reference to the RemoteServicesManager provided by the platform
	 */
	private static IRemoteServicesManager remoteManager;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 */
	public EclipseExtraInfoWidget() {

		// Setup the listeners array
		listeners = new ArrayList<IWidgetClosedListener>();

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#display()
	 */
	@Override
	public void display() {

		// Sync with the display
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {

				// Local declarations
				Display display;
				Shell shell;

				// Get the display and shell
				display = PlatformUI.getWorkbench().getDisplay();
				shell = display.getActiveShell();

				// Get whether this Extra Info Form corresponds to 
				// a remote host and needs a remote connection
				boolean isLaunchForm = "Log-in Information".equals(iceForm.getComponents().get(0).getName());
				
				// If it is, then let's use the remote interfaces from PTP
				if (isLaunchForm) {
					
					// Get the default SSH connection type
					IRemoteConnectionType type = remoteManager.getRemoteConnectionTypes().get(0);
					
					// Get the UI connection services which can give us a remote connection wizard
					IRemoteUIConnectionService connectionService = type.getService(IRemoteUIConnectionService.class);
					
					// Get the Wizard!
					IRemoteUIConnectionWizard wizard = connectionService.getConnectionWizard(shell);
					if (wizard != null) {
						// Set the connection name
						wizard.setConnectionName("New Connection");
						
						// Open the wizard and get whether the input was valid
						IRemoteConnectionWorkingCopy copy = wizard.open();
						if (copy == null) {
							for (IWidgetClosedListener i : listeners) {
								i.cancelled();
							}
						} else {
							for (IWidgetClosedListener i : listeners) {
								i.closedOK();
							}
						}
					}
				} else {
					// If this is not a remote launch, then just use 
					// the regular ExtraInfoDialog.
					
					// Instantiate the dialog, set the DataComponent and set the
					// listeners
					infoDialog = new ExtraInfoDialog(shell);
					infoDialog.setDataComponent((DataComponent) iceForm.getComponents().get(0));
					// Open the dialog
					infoDialog.open();
					for (IWidgetClosedListener i : listeners) {
						infoDialog.setCloseListener(i);
					}
				}
			}
		});

	}

	/**
	 * This method is used by the platform to give this MOOSEModel a reference
	 * to the available IRemoteServicesManager.
	 * 
	 * @param manager
	 */
	public void setRemoteServicesManager(IRemoteServicesManager manager) {
		if (manager != null) {
			System.out.println(
					"[EclipseExtraInfoWidget Message] Setting the IRemoteServicesManager: " + manager.toString());
			remoteManager = manager;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setForm(Form form)
	 */
	@Override
	public void setForm(Form form) {

		// Set the Form
		iceForm = form;

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#getForm()
	 */
	@Override
	public Form getForm() {
		return iceForm;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setCloseListener(IWidgetClosedListener listener)
	 */
	@Override
	public void setCloseListener(IWidgetClosedListener listener) {

		// Add the listener if it is not null
		if (listener != null) {
			listeners.add(listener);
		}
		return;

	}
}