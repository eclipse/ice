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
package org.eclipse.eavp.viz.visit;

import java.util.HashMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the launch of the VisIt client.
 *
 * @author Jay Jay Billings, Taylor Patterson
 *
 */
public class LaunchVisitHandler extends AbstractHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LaunchVisitHandler.class);

	/**
	 * This operation handles the command and launches the VisIt configuration
	 * dialog.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the window and the shell.
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();

		// Display a dialog to the user to collect the VisIt connection info
		LaunchVisitWizard wizard = new LaunchVisitWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);

		// Open the dialog. If the user closes the dialog or presses "Cancel",
		// do not proceed any further.
		if (dialog.open() == Window.CANCEL) {
			return null;
		}

		// Setup the input map
		HashMap<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("connId", wizard.getPage().getConnectionId());
		inputMap.put("username", "user1");
		inputMap.put("password", wizard.getPage().getPassword());
		inputMap.put("dataType", "image");
		inputMap.put("windowWidth", "1340");
		inputMap.put("windowHeight", "1020");
		inputMap.put("windowId", wizard.getPage().getConnectionWindowId());
		inputMap.put("gateway", wizard.getPage().getGateway());
		inputMap.put("localGatewayPort", wizard.getPage().getGatewayPort());
		inputMap.put("useTunneling", wizard.getPage().getUseTunneling());
		inputMap.put("url", wizard.getPage().getHostname());
		inputMap.put("port", wizard.getPage().getVisItPort());
		inputMap.put("visDir", wizard.getPage().getVisItDir());
		inputMap.put("isLaunch", wizard.getPage().getIsLaunch());

		// Create the input
		VisitEditorInput input = new VisitEditorInput(inputMap);

		// Get the workbench page
		IWorkbenchPage page = window.getActivePage();

		// Open the editor
		try {
			page.openEditor(input, VisitEditor.ID);
		} catch (PartInitException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!",e);
			// Throw up an error dialog
			MessageBox errBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			errBox.setText("VisIt Editor Error!");
			errBox.setMessage("Unable to open the VisIt Editor!");
			errBox.open();
		}

		return null;
	}

}
