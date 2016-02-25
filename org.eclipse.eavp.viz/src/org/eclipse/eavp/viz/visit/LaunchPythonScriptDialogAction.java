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

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import gov.lbnl.visit.swt.VisItSwtWidget;

/**
 * 
 */
public class LaunchPythonScriptDialogAction extends Action {

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public LaunchPythonScriptDialogAction(ViewPart parent) {

		viewer = parent;

		// Set the action's tool tip text.
		setToolTipText("Execute a Python script");

		// Set the action's image (the green plus button for adding).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "launch.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {

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
		final VisItSwtWidget widget = editor.getVizWidget();

		// Make sure the widget is initialized
		if (widget == null || !widget.hasInitialized()) {
			// Present an error message and return
			MessageDialog.openError(shell, "VisIt Not Initialized",
					"Please connect to a running VisIt client prior to "
							+ "attempting to open a file.");
			return;
		}

		// If we've passed the previous checks, we may proceed with opening the
		// python dialog.
		VisitPythonDialog dialog = new VisitPythonDialog(shell, widget);
		dialog.open();

		return;
	}
}
