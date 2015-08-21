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
package org.eclipse.ice.viz.service.geometry.widgets;

import java.net.URL;

import org.eclipse.ice.viz.service.datastructures.VizObject;
import org.eclipse.ice.viz.service.geometry.shapes.AbstractShape;
import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
import org.eclipse.ice.viz.service.geometry.shapes.Geometry;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;
import org.eclipse.ice.viz.service.geometry.shapes.OperatorType;
import org.eclipse.ice.viz.service.geometry.shapes.Transformation;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <p>
 * Opens a dialog box to replicate the selected shape
 * </p>
 * <p>
 * This action should be enabled only when exactly one shape is selected.
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ActionReplicateShape extends Action {
	/**
	 * <p>
	 * The current ShapeTreeView
	 * </p>
	 * 
	 */
	private ShapeTreeView view;

	/**
	 * The image descriptor associated with the duplicate action's icon
	 */
	private ImageDescriptor imageDescriptor;

	/**
	 * <p>
	 * Initializes the instance with the current ShapeTreeView
	 * </p>
	 * 
	 * @param view
	 *            <p>
	 *            The current ShapeTreeView
	 *            </p>
	 */
	public ActionReplicateShape(ShapeTreeView view) {

		this.view = view;

		this.setText("Replicate Shape");

		// Load replicate.gif icon from the bundle's icons/ directory

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL imagePath = BundleUtility.find(bundle, "icons/replicate.gif");
		imageDescriptor = ImageDescriptor.createFromURL(imagePath);

	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	/**
	 * <p>
	 * Opens the replicate dialog box and clones the selected shape to the
	 * properties
	 * </p>
	 * 
	 */
	@Override
	public void run() {

		Geometry geometry = (Geometry) view.treeViewer
				.getInput();

		// Check that only one shape is selected

		ITreeSelection selection = (ITreeSelection) view.treeViewer
				.getSelection();
		TreePath[] paths = selection.getPaths();

		if (paths.length != 1) {
			return;
		}
		// Get the selected shape from the selection

		Object selectedObject = paths[0].getLastSegment();

		if (!(selectedObject instanceof IShape)) {
			return;
		}
		IShape selectedShape = (IShape) selectedObject;

		// Create a transformation, initialized from the selected shape's
		// transformation

		Transformation accumulatedTransformation = (Transformation) selectedShape
				.getTransformation().clone();

		// Open the dialog

		ReplicateDialog replicateDialog = new ReplicateDialog(view.getSite()
				.getShell());

		if (replicateDialog.open() != IDialogConstants.OK_ID) {
			return;
		}
		// Get the dialog parameters

		int quantity = replicateDialog.getQuantity();
		double[] shift = replicateDialog.getShift();

		// Ignore the request if the desired quantity is 1

		if (quantity == 1) {
			return;
		}
		// Get the parent of the shape
		// If the selected shape is a direct child of a GeometryComponent,
		// its parent shape is null.

		ComplexShape parentShape = (ComplexShape) selectedShape.getParent();

		// Remove the selected shape from its original parent

		synchronized (geometry) {
			if (parentShape != null) {
				parentShape.removeShape(selectedShape);
			} else {
				geometry.removeShape(selectedShape);
			}
		}

		// Create a new parent union shape

		ComplexShape replicateUnion = new ComplexShape(OperatorType.Union);
		replicateUnion.setName("Replication");
		replicateUnion.setId(((VizObject) selectedShape).getId());

		for (int i = 1; i <= quantity; i++) {

			// Clone the selected shape and remove its "selected" property

			IShape clonedShape = (IShape) ((AbstractShape) selectedShape)
					.clone();
			clonedShape.removeProperty("selected");
			((VizObject) clonedShape).setId(i);

			// Add the translation

			clonedShape
					.setTransformation((Transformation) accumulatedTransformation
							.clone());

			// Add it to the replicated union

			replicateUnion.addShape(clonedShape);

			// Shift the transform for the next shape

			accumulatedTransformation.translate(shift);
		}

		// Refresh the TreeViewer

		if (parentShape != null) {

			// The parent is an IShape

			synchronized (geometry) {
				parentShape.addShape(replicateUnion);
			}

			view.treeViewer.refresh(parentShape);
		} else {

			// The parent is the root GeometryComponent

			synchronized (geometry) {
				geometry.addShape(replicateUnion);
			}

			view.treeViewer.refresh();
		}

		view.treeViewer.expandToLevel(parentShape, 1);

	}
}