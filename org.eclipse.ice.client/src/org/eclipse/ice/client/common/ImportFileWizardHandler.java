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
 *    Jordan Deyton (UT-Battelle, LLC.) - doc cleanup
 *******************************************************************************/
package org.eclipse.ice.client.common;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.common.wizards.ImportFileWizard;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler is used to import files into the ICE project space. It is used
 * in the main Eclipse UI ToolBar (see bundle extensions) and serves as a
 * shortcut to the {@link ImportFileWizard}.
 * <p>
 * <b>Note:</b> This class does not actually call the
 * <code>ImportFileWizard</code>. Instead, it opens a {@link FileDialog} and
 * processes the selected files as the <code>ImportFileWizard</code> normally
 * does.
 * </p>
 * 
 * @author Jordan
 * 
 */
public class ImportFileWizardHandler extends AbstractHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ImportFileWizardHandler.class);

	/**
	 * Opens a new {@link FileDialog} and imports any selected files into the
	 * ICE project space.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the window and the shell.
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();

		// Get the IProject instance if we can
		IProject project = null;
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			}
		}

		// Get the Client
		IClient client = null;
		try {
			client = IClient.getClient();
		} catch (CoreException e) {
			logger.error("Could not get a valid IClient reference.", e);
		}

		// Make sure we got a valid IClient
		if (client != null) {
			// Create the dialog and get the files
			FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
			fileDialog.setText("Select a file to import into ICE");
			fileDialog.open();

			// Import the files
			String filterPath = fileDialog.getFilterPath();
			for (String name : fileDialog.getFileNames()) {
				File importedFile = new File(filterPath, name);
				if (project == null) {
					client.importFile(importedFile.toURI());
				} else {
					client.importFile(importedFile.toURI(), project);
				}
			}
		} else {
			logger.error("Could not find a valid IClient.");
		}
		
		return null;
	}

}
