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
package org.eclipse.ice.client.rcp.workspaceviewer;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/* View that shows the user's selected workspace location file 
 * structure. It is a navigable hierarchy of directories and files */
public class WorkspaceExplorer extends ViewPart {

	// Tree view for a file system structure
	private TreeViewer viewer;

	// This viewpart's ID
	public static final String ID = "org.eclipse.ice.iceclient.WorkspaceExplorer";

	public WorkspaceExplorer() {
	}

	@Override
	// Creates the SWT widgets needed to show the workspace file system
	public void createPartControl(Composite parent) {
		// Create the TreeViewer with Horizontal and Vertical Scroll bars
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		// Set the viewer's content provider, an instance of
		// WorkspaceExplorerContentProvider
		viewer.setContentProvider(new WorkspaceExplorerContentProvider());

		// Set the viewer's label provider, and instance of
		// WorkspaceExplorerLabelProvider
		// which returns the correct image corresponding to a file or directory
		// folder
		viewer.setLabelProvider(new WorkspaceExplorerLabelProvider());

		// Create a file path corresponding to the user's selected workspace
		// location
		File workspaceChosen = new File(Platform.getInstanceLocation().getURL()
				.getPath());

		// Initialize the TreeViewer with the workspace directory and all
		// children of it
		viewer.setInput(workspaceChosen.listFiles());

		return;
	}

	@Override
	public void setFocus() {
		// Set focus on the viewer
		viewer.getControl().setFocus();
		return;
	}

}
