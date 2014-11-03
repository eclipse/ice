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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/* Provides File content needed by the WorkspaceExplorer TreeViewer */
public class WorkspaceExplorerContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// return the requested element as an array of Files
		return (File[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// Get all children of the parentElement File
		File file = (File) parentElement;
		return file.listFiles();
	}

	@Override
	public Object getParent(Object element) {
		// Return the parent of a given File
		return ((File) element).getParentFile();
	}

	@Override
	public boolean hasChildren(Object element) {
		// True if children exist, false otherwise
		File file = (File) element;
		if (file.isDirectory()) {
			return true;
		}
		return false;
	}

}
