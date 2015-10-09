 /*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.caebat.batml.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Adds a Reflectivity Perspective to ICE. See <code>plugin.xml</code> for the
 * layout code.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class BatMLPerspective implements IPerspectiveFactory {

	/**
	 * The perspective's ID as used in the <code>plugin.xml</code>.
	 */
	public static final String ID = "org.eclipse.ice.reflectivity.ui.ReflectivityPerspective";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.
	 * IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Add the perspective to top right corner.
		layout.addPerspectiveShortcut(ID);

	}

}