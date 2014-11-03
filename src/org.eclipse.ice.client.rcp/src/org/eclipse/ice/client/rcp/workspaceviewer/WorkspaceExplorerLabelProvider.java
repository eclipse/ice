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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class WorkspaceExplorerLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		// Cast the element as a File
		File file = (File) element;
		// Get that file's string name
		String name = file.getName();
		// If name length is greater than 0, it is a file
		// Return the name
		if (name.length() > 0) {
			return name;
		}
		// Otherwise it is a directory, return the path
		return file.getPath();
	}

	// Return the correct image, either a file image or a folder image
	public Image getImage(Object obj) {
		// If the incoming File is a directory, return a folder image
		if (((File) obj).isDirectory()) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FOLDER);
		} else {
			// If the incoming image is not a directory, return the File image
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE);
		}
	}

}
