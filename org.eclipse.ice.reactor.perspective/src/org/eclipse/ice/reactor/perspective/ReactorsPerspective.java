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
package org.eclipse.ice.reactor.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class implements IPerspectiveFactory to create the Visualization
 * Perspective.
 * 
 * @author Taylor Patterson
 * 
 */
public class ReactorsPerspective implements IPerspectiveFactory {

	/**
	 * The ID of this perspective.
	 */
	public static final String ID = "org.eclipse.ice.reactors.perspective.ReactorsPerspective";

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {

		// Add the perspective to the layout
		layout.addPerspectiveShortcut(ReactorsPerspective.ID);

		return;
	}

}
