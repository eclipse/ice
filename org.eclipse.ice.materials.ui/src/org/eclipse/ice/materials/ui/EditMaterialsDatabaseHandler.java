/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.materials.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a handler for launching the materials database editor.
 * 
 * @author Jay Jay Billings
 *
 */
public class EditMaterialsDatabaseHandler extends AbstractHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EditMaterialsDatabaseHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the active window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		// Get the workbench page
		IWorkbenchPage page = window.getActivePage();

		// Open the editor
		try {
			IMaterialsDatabase database = MaterialsDatabaseServiceHolder.get();
			IEditorInput input = new MaterialsDatabaseEditorInput(database);
			page.openEditor(input, MaterialsDatabaseEditor.ID);
		} catch (PartInitException e) {
			// Complain
			logger.error(getClass().getName() + " Exception!", e);
			// Throw up an error dialog
			MessageBox errBox = new MessageBox(window.getShell(),
					SWT.ICON_ERROR | SWT.OK);
			errBox.setText("VisIt Editor Error!");
			errBox.setMessage("Unable to open the VisIt Editor!");
			errBox.open();
		}

		return null;
	}

}
