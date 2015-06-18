/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    Jay Jay Billings (UT-Battelle, LLC.) - fixed author tags
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class provides the MOOSE perspective. See the <code>plugin.xml</code>
 * for more perspective configuration.
 * 
 * @author Jordan H. Deyton
 *
 */
public class MOOSEPerspective implements IPerspectiveFactory {

	/**
	 * The perspective's ID as used in the <code>plugin.xml</code>.
	 */
	public static final String ID = "org.eclipse.ice.widgets.moose.MOOSEPerspective";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui
	 * .IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Add the perspective to top right corner.
		layout.addPerspectiveShortcut(ID);
	}

}
