/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.materials.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @author Jay Jay Billings
 *
 */
public class MaterialsPerspective implements IPerspectiveFactory {

	/**
	 * The id of this perspective.
	 */
	public static final String ID = "org.eclipse.ice.materials.perspectives.MaterialsPerspective";

	/*
	 * Implements a method from IPerspectiveFactory.
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {

		// Add the perspective to the layout
		layout.addPerspectiveShortcut(MaterialsPerspective.ID);

		return;
	}

}
