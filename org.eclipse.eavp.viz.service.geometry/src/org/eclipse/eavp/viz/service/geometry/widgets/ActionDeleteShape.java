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
package org.eclipse.eavp.viz.service.geometry.widgets;

import java.net.URL;

import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <p>
 * Action for deleting the currently selected shapes from the ShapeTreeView
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ActionDeleteShape extends Action {
	/**
	 * <p>
	 * The current ShapeTreeViewer associated with the DeleteShape action
	 * </p>
	 * 
	 */
	private ShapeTreeView view;

	/**
	 * The image descriptor associated with the delete action's icon
	 */
	private ImageDescriptor imageDescriptor;

	/**
	 * <p>
	 * Constructor for setting the current ShapeTreeViewer
	 * </p>
	 * 
	 * @param view
	 *            <p>
	 *            The current ShapeTreeView
	 *            </p>
	 */
	public ActionDeleteShape(ShapeTreeView view) {

		this.view = view;

		this.setText("Delete Shape");

		// Load the delete.gif ImageDescriptor from the bundle's
		// `icons` directory

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL imagePath = BundleUtility.find(bundle, "icons/delete.gif");
		imageDescriptor = ImageDescriptor.createFromURL(imagePath);

	}

	/**
	 * Returns the image descriptor associated with the delete action's icon
	 * 
	 * @return The ImageDescriptor with the loaded delete.gif file
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	/**
	 * <p>
	 * Runs this action
	 * </p>
	 * <p>
	 * Each action implementation must define the steps needed to carry out this
	 * action.
	 * </p>
	 * 
	 */
	@Override
	public void run() {

		// Get the tree paths of the current selection

		ITreeSelection selection = (ITreeSelection) view.treeViewer.getSelection();
		TreePath[] paths = selection.getPaths();

		AbstractController geometry = (AbstractController) view.treeViewer.getInput();

		// Loop through each TreePath

		for (TreePath path : paths) {

			Object selectedObject = path.getLastSegment();

			// Check if the selected object is an IShape

			if (selectedObject instanceof ShapeController) {

				ShapeController selectedShape = (ShapeController) selectedObject;
				ShapeController parentShape = (ShapeController) selectedShape.getEntitiesByCategory("Parent").get(0);

				if (parentShape instanceof ShapeController) {

					// Remove the selected shape from the parent

					synchronized (geometry) {
						parentShape.removeEntity(selectedShape);
					}

					view.treeViewer.refresh(parentShape);
				}

				else if (parentShape == null) {

					// The parent shape may be the root GeometryComponent,
					// so try removing it from there.

					synchronized (geometry) {
						geometry.removeEntity(selectedShape);
					}
					view.treeViewer.refresh();
				}
			}
		}

	}
}