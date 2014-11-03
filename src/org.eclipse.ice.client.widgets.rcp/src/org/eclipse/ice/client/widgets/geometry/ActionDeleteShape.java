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

import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;

import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Action for deleting the currently selected shapes from the ShapeTreeView
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ActionDeleteShape extends Action {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current ShapeTreeViewer associated with the DeleteShape action
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ShapeTreeView view;

	/**
	 * The image descriptor associated with the delete action's icon
	 */
	private ImageDescriptor imageDescriptor;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Constructor for setting the current ShapeTreeViewer
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param view
	 *            <p>
	 *            The current ShapeTreeView
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ActionDeleteShape(ShapeTreeView view) {
		// begin-user-code

		this.view = view;

		this.setText("Delete Shape");

		// Load the delete.gif ImageDescriptor from the bundle's
		// `icons` directory

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL imagePath = BundleUtility.find(bundle, "icons/delete.gif");
		imageDescriptor = ImageDescriptor.createFromURL(imagePath);

		// end-user-code
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Runs this action
	 * </p>
	 * <p>
	 * Each action implementation must define the steps needed to carry out this
	 * action.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void run() {
		// begin-user-code

		// Get the tree paths of the current selection

		ITreeSelection selection = (ITreeSelection) view.treeViewer
				.getSelection();
		TreePath[] paths = selection.getPaths();

		GeometryComponent geometry = (GeometryComponent) view.treeViewer
				.getInput();

		// Loop through each TreePath

		for (TreePath path : paths) {

			Object selectedObject = path.getLastSegment();

			// Check if the selected object is an IShape

			if (selectedObject instanceof IShape) {

				IShape selectedShape = (IShape) selectedObject;
				IShape parentShape = selectedShape.getParent();

				if (parentShape instanceof ComplexShape) {

					// Remove the selected shape from the parent

					ComplexShape parentComplexShape = (ComplexShape) parentShape;

					synchronized (geometry) {
						parentComplexShape.removeShape(selectedShape);
					}

					view.treeViewer.refresh(parentShape);
				}

				else if (parentShape == null) {

					// The parent shape may be the root GeometryComponent,
					// so try removing it from there.

					synchronized (geometry) {
						geometry.removeShape(selectedShape);
					}
					view.treeViewer.refresh();
				}
			}
		}

		// end-user-code
	}
}