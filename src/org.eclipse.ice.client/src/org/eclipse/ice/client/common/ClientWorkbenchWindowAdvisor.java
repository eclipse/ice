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
package org.eclipse.ice.client.common;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ClientWorkbenchWindowAdvisor extends
		WorkbenchWindowAdvisor {

	public ClientWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ClientActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		// Get Display width and height
		Rectangle bounds = PlatformUI.getWorkbench().getDisplay().getBounds();

		// Grab the Workbench Window Configurer, used to set things such as
		// perspectives bar, coolbar, etc..
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

		// Set the size of the application the the size of the Display
		configurer.setInitialSize(new Point(bounds.width, bounds.height));

		// Turn on the Coolbar, status bar, menu bar, and Perspectives bar
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowMenuBar(true);
		configurer.setShowPerspectiveBar(true);

		// Set the Application Title
		configurer.setTitle("ICE - "
				+ "Eclipse Integrated Computational Environment"); //$NON-NLS-1$

		// Move the Perspectives Bar to the Top Right, and make the editor tabs
		// curved instead of boxy and rectangular (looks better)
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
				false);

		return;
	}

	@Override
	public void postWindowOpen() {

	}
}
