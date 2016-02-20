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

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IWorkbenchPage;

/**
 * <p>
 * Handles the selection or deselection of shape items in the shape TreeViewer
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ShapeTreeSelectionListener implements ISelectionChangedListener {
	/**
	 * <p>
	 * The reference to the current IWorkbenchPage
	 * </p>
	 * 
	 */
	private IWorkbenchPage workbenchPage;

	/**
	 * A list of shapes of the last selection event
	 */
	private ArrayList<ShapeController> selectedShapes = new ArrayList<ShapeController>();

	/**
	 * <p>
	 * The constructor for setting the internal reference to the current
	 * IWorkbenchPage
	 * </p>
	 * 
	 * @param workbenchPage
	 *            <p>
	 *            The current IWorkbenchPage
	 *            </p>
	 */
	public ShapeTreeSelectionListener(IWorkbenchPage workbenchPage) {

		this.workbenchPage = workbenchPage;

	}

	/**
	 * <p>
	 * Triggered with the shape tree selection is changed
	 * </p>
	 * <p>
	 * The current implementation updates the referenced shape of the
	 * TransformationView.
	 * </p>
	 * 
	 * @param event
	 *            <p>
	 *            The selection event
	 *            </p>
	 */
	public void selectionChanged(Class event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Changes the state of the TransformationView according to the currently
	 * selected shape
	 * 
	 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent
	 *      event)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		// Get the TransformationView if it is open

		TransformationView transformationView = (TransformationView) workbenchPage.findView(TransformationView.ID);

		// Return if not

		if (transformationView == null) {
			return;
		}
		// Get the tree paths

		ITreeSelection selection = (ITreeSelection) event.getSelection();
		TreePath[] paths = selection.getPaths();

		// Remove the "selected" value from previously selected shapes

		for (ShapeController shape : selectedShapes) {
			shape.setProperty("Selected", "False");
		}

		selectedShapes.clear();

		// Set the "selected" value to true for newly selected shapes

		for (TreePath path : paths) {
			Object selectedObject = path.getLastSegment();

			// Only perform the action for selected IShapes
			// (rather than GeometryComponents or null)

			if (selectedObject instanceof ShapeController) {
				ShapeController selectedShape = (ShapeController) selectedObject;

				selectedShape.setProperty("Selected", "True");
				selectedShapes.add(selectedShape);
			}
		}

		// Set the TransformationView shape to null if nothing is selected
		// or there are multiple selections

		if (paths.length != 1) {
			transformationView.setShape(null);
			return;
		}

		Object selectedObject = paths[0].getLastSegment();

		// Determine if the shape of the TransformationView should be set

		if (selectedObject instanceof ShapeController) {
			transformationView.setShape((ShapeController) selectedObject);
		}

		else {
			transformationView.setShape(null);
		}

	}
}