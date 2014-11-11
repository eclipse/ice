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
package org.eclipse.ice.client.widgets.geometry;

import org.eclipse.ice.datastructures.form.geometry.IShape;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IWorkbenchPage;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Handles the selection or deselection of shape items in the shape TreeViewer
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeTreeSelectionListener implements ISelectionChangedListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reference to the current IWorkbenchPage
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IWorkbenchPage workbenchPage;

	/**
	 * A list of shapes of the last selection event
	 */
	private ArrayList<IShape> selectedShapes = new ArrayList<IShape>();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor for setting the internal reference to the current
	 * IWorkbenchPage
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param workbenchPage
	 *            <p>
	 *            The current IWorkbenchPage
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ShapeTreeSelectionListener(IWorkbenchPage workbenchPage) {
		// begin-user-code

		this.workbenchPage = workbenchPage;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Triggered with the shape tree selection is changed
	 * </p>
	 * <p>
	 * The current implementation updates the referenced shape of the
	 * TransformationView.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param event
	 *            <p>
	 *            The selection event
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void selectionChanged(Class event) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * Changes the state of the TransformationView according to the currently
	 * selected shape
	 * 
	 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent
	 *      event)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		// begin-user-code

		// Get the TransformationView if it is open

		TransformationView transformationView = (TransformationView) workbenchPage
				.findView(TransformationView.ID);

		// Return if not

		if (transformationView == null) {
			return;
		}
		// Get the tree paths

		ITreeSelection selection = (ITreeSelection) event.getSelection();
		TreePath[] paths = selection.getPaths();

		// Remove the "selected" value from previously selected shapes

		for (IShape shape : selectedShapes) {
			shape.removeProperty("selected");
		}

		selectedShapes.clear();

		// Set the "selected" value to true for newly selected shapes

		for (TreePath path : paths) {
			Object selectedObject = path.getLastSegment();

			// Only perform the action for selected IShapes
			// (rather than GeometryComponents or null)

			if (selectedObject instanceof IShape) {
				IShape selectedShape = (IShape) selectedObject;

				selectedShape.setProperty("selected", "true");
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

		if (selectedObject instanceof IShape) {
			transformationView.setShape((IShape) selectedObject);
		}

		else {
			transformationView.setShape(null);
		}

		// end-user-code
	}
}