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

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This action is used to save an individual reactor file in the
 * {@link ReactorViewer}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class SaveReactorFileAction extends Action {
	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public SaveReactorFileAction(ViewPart parent) {
		viewer = parent;

		// Set the action's tool tip text.
		setText("Save reactor file");
		setToolTipText("Save an edited reactor file");

		// Set the action's image (the save button).
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {
		// TODO Get the selected resource and try to save it.
	}
}
