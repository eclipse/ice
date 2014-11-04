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

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "org.eclipse.ice.client.widgets.perspective";

	public void createInitialLayout(IPageLayout layout) {

		// Setup the perspective shortcut button in the top right corner
		layout.addPerspectiveShortcut(Perspective.ID);

		// Reserve space for the Console and Properties
		IFolderLayout bottomRight = layout.createFolder("bottomRight",
				IPageLayout.BOTTOM, 0.70f, layout.getEditorArea());
		bottomRight.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottomRight.addView(IPageLayout.ID_PROP_SHEET);

		return;
	}
}
