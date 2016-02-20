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
package org.eclipse.eavp.viz;

import java.io.File;
import java.io.IOException;

import org.eclipse.eavp.viz.service.datastructures.resource.IVizResource;
import org.eclipse.eavp.viz.service.datastructures.resource.VisualizationResource;
import org.eclipse.eavp.viz.visit.VisitEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.lbnl.visit.swt.VisItRemoteFileDialog;
import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtWidget;

/**
 * This Action presents a {@link VisItRemoteFileDialog} to select files from a
 * remote file system and add them to the {@link VizFileViewer}.
 *
 * @author Taylor Patterson
 */
public class AddRemoteFileAction extends Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AddRemoteFileAction.class);

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The AddFileAction whose drop-down owns the object of this class.
	 */
	private AddFileAction parentAction;

	/**
	 * The constructor
	 *
	 * @param parentView
	 *            The ViewPart to whom the object of this class belongs.
	 * @param parentAction
	 *            The AddFileAction to whom the object of this class belongs.
	 */
	public AddRemoteFileAction(ViewPart parentView, AddFileAction parentAction) {

		// Keep track of the viewer and parent Action containing this Action
		viewer = parentView;
		this.parentAction = parentAction;

		// Set the display text
		setText("Add a remote file");
	}

	/**
	 * The function called whenever the Action is selected from the drop-down.
	 */
	@Override
	public void run() {

		// Set the parent's default Action
		parentAction.setDefaultAction(this);

		// Get the Shell of the workbench
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		// Get the editor part
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		// If the editor is null, there is no chance VisIt has been initialized.
		if (editorPart == null) {
			// Present an error dialog and return
			MessageDialog.openError(shell, "VisIt Not Initialized",
					"Please connect to a running VisIt client prior to "
							+ "attempting to open a file.");
			return;
		}

		// Get the VisItSWTWidget from the editor
		VisitEditor editor = (VisitEditor) editorPart;
		VisItSwtWidget widget = editor.getVizWidget();

		// Make sure the widget is initialized
		if (widget == null || !widget.hasInitialized()) {
			// Present an error message and return
			MessageDialog.openError(shell, "VisIt Not Initialized",
					"Please connect to a running VisIt client prior to "
							+ "attempting to open a file.");
			return;
		}

		// Get the connection from the widget in the editor. If the connection
		// is back to this machine, use the local file dialog instead.
		VisItSwtConnection conn = widget.getVisItSwtConnection();
		if (conn.getHostname().equals("localhost")) {
			parentAction.getAddLocalFileAction().run();
			return;
		}

		// Otherwise, the connection is to a remote machine, so proceed with the
		// remote file dialog.
		VisItRemoteFileDialog rdialog = new VisItRemoteFileDialog(
				widget.getViewerMethods(), shell.getDisplay());
		String remoteFile = rdialog.open();

		if (remoteFile == null) {
			return;
		}
		try {
			IVizResource resource = new VisualizationResource(new File(remoteFile));
			resource.setHost(conn.getHostname());
			VizFileViewer vizViewer = (VizFileViewer) viewer;
			vizViewer.addFile(resource);
		} catch (IOException e) {
			System.err.println("AddRemoteFileAction error: Failed to create "
					+ "an ICEResource for the file at \"" + remoteFile + "\".");
			logger.error(getClass().getName() + " Exception!",e);
		}

		return;
	}

}
