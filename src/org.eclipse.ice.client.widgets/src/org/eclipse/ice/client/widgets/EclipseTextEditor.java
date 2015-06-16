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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.ice.iclient.uiwidgets.ITextEditor;
import org.eclipse.ice.datastructures.resource.ICEResource;

/**
 * <p>
 * This class implements ITextEditor interface to open a text editor using
 * elements of SWT/JFace and the Eclipse Rich Client Platform.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class EclipseTextEditor implements ITextEditor {
	/**
	 * <p>
	 * The ICEResource managed by the EclipseTextEditor.
	 * </p>
	 * 
	 */
	private ICEResource iceResource;

	/**
	 * (non-Javadoc)
	 * 
	 * @see ITextEditor#setResource(ICEResource resource)
	 */
	public void setResource(ICEResource resource) {

		iceResource = resource;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ITextEditor#getResource()
	 */
	public ICEResource getResource() {

		return iceResource;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ITextEditor#display()
	 */
	public void display() {

		// Local Declarations
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot()
				.findFilesForLocationURI(iceResource.getPath());
		IFile textFile;
		IWorkbenchPage page = null;

		// Only open the editor if the Resource is good to go
		if (iceResource != null && files.length > 0) {
			// We don't need all of the files, just one of them since they are
			// mapped to the same place.
			textFile = files[0];
			// Refresh the file if needed
			if (!textFile.isSynchronized(IResource.DEPTH_ZERO)) {
				try {
					textFile.refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			// Get the workbench page
			page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage();
			// Open the editor
			try {
				page.openEditor(new FileEditorInput(textFile),
						"org.eclipse.ui.DefaultTextEditor");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}