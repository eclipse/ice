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
package org.eclipse.eavp.viz;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.eavp.viz.visit.VisitPlotViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This Action removes selected resources from the it's parent plot viewer by
 * calling {@link VisitPlotViewer#removeSelection()} when it is run.
 * 
 * @author Taylor Patterson, Jordan H. Deyton
 */
public class DeletePlotAction extends Action {

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final IDeletePlotActionViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public DeletePlotAction(IDeletePlotActionViewPart parent) {

		viewer = parent;

		// Set the action's tool tip text.
		setToolTipText("Delete the selected plot from the list");

		// Set the action's image (the red x button).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "delete_X.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {

		// Call the parent view's method to remove the current tree selection
		// from its TreeViewer
		viewer.removeSelection();

		return;
	}

}
