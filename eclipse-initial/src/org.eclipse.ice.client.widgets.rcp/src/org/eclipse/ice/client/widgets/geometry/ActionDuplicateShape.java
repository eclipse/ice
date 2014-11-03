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

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.ice.datastructures.form.geometry.AbstractShape;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ActionDuplicateShape extends Action {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ShapeTreeView view;

	/**
	 * The image descriptor associated with the duplicate action's icon
	 */
	private ImageDescriptor imageDescriptor;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param view
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ActionDuplicateShape(ShapeTreeView view) {
		// begin-user-code

		this.view = view;

		this.setText("Duplicate Shape");

		// Load duplicate.gif icon from the bundle's icons/ directory

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL imagePath = BundleUtility.find(bundle, "icons/duplicate.gif");
		imageDescriptor = ImageDescriptor.createFromURL(imagePath);

		// end-user-code
	}

	/**
	 * Returns the image descriptor associated with the duplicate action's icon
	 * 
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void run() {
		// begin-user-code

		GeometryComponent geometry = (GeometryComponent) view.treeViewer
				.getInput();

		// Get selection

		ITreeSelection selection = (ITreeSelection) view.treeViewer
				.getSelection();
		TreePath[] paths = selection.getPaths();

		// Iterate through the paths

		for (TreePath path : paths) {
			Object selectedObject = path.getLastSegment();

			if (selectedObject instanceof AbstractShape) {
				AbstractShape selectedShape = (AbstractShape) selectedObject;

				// Clone the shape

				IShape clonedShape = (IShape) selectedShape.clone();

				// Remove the selected state from the cloned shape

				clonedShape.removeProperty("selected");

				// Try to get the selected shape's parent shape
				// We can assume that if the parent exists, it is a ComplexShape

				ComplexShape parentShape = (ComplexShape) selectedShape
						.getParent();

				if (parentShape != null) {

					// Find the index of the selected shape in the list of its
					// siblings

					ArrayList<IShape> childShapes = parentShape.getShapes();
					int selectedShapeIndex = childShapes.indexOf(selectedShape);

					if (selectedShapeIndex < 0) {
						continue;
					}
					// Add the cloned shape to the original shape's parent

					synchronized (geometry) {
						childShapes.add(selectedShapeIndex + 1, clonedShape);
						parentShape.setShapes(childShapes);
					}

					view.treeViewer.refresh(parentShape);
				} else {

					// Find the index of the selected shape in the list of its
					// siblings

					ArrayList<IShape> childShapes = geometry.getShapes();
					int selectedShapeIndex = childShapes.indexOf(selectedShape);

					// Add the cloned shape to the root GeometryComponent

					synchronized (geometry) {
						childShapes.add(selectedShapeIndex + 1, clonedShape);
						geometry.setShapes(childShapes);
					}

					view.treeViewer.refresh();
				}

				// Collapse the cloned item and select it

				view.treeViewer.collapseToLevel(clonedShape, 0);

				// Change the current selection to the new duplicated shape

				TreePath clonedPath = path.getParentPath().createChildPath(
						clonedShape);
				TreeSelection clonedSelection = new TreeSelection(clonedPath);
				view.treeViewer.setSelection(clonedSelection);
			}
		}

		// end-user-code
	}
}