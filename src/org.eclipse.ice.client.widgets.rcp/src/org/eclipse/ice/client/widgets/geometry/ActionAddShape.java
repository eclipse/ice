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

import org.eclipse.ice.client.widgets.geometry.ShapeTreeContentProvider.BlankShape;
import org.eclipse.ice.datastructures.form.geometry.AbstractShape;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.OperatorType;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
 * Action for adding a specific shape to the ShapeTreeView
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ActionAddShape extends Action {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current ShapeTreeViewer associated with the AddShape action
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ShapeTreeView view;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ShapeType used to create new PrimitiveShapes when the AddShape action
	 * is triggered
	 * </p>
	 * <p>
	 * If the value is null, the Operator is used to create ComplexShapes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ShapeType shapeType;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The OperatorType used to create new ComplexShapes when the AddShape
	 * action is triggered
	 * </p>
	 * <p>
	 * If the value is null, the ShapeType is used to create PrimitiveShapes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private OperatorType operatorType;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current used default shape number appended to the name of every newly
	 * created shape
	 * </p>
	 * <p>
	 * Starting from zero, this number increments every time a new shape is
	 * added to the tree.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int currentShapeId;

	/**
	 * The image to display as the action's icon
	 */
	private ImageDescriptor imageDescriptor;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Constructor for creating new PrimitiveShapes with a given ShapeType
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param view
	 *            <p>
	 *            The current ShapeTreeViewer
	 *            </p>
	 * @param shapeType
	 *            <p>
	 *            The type of PrimitiveShape to create with the action is
	 *            triggered
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ActionAddShape(ShapeTreeView view, ShapeType shapeType) {
		// begin-user-code

		this.view = view;
		this.shapeType = shapeType;
		operatorType = null;
		currentShapeId = 0;

		// Set the Action's name to "Add {ShapeType}"
		setText("Add " + shapeType.toString());

		// Create a map which stores the filenames of the icons, relative
		// to the icons/ directory
		Map<ShapeType, String> shapeIcons = new HashMap<ShapeType, String>();
		shapeIcons.put(ShapeType.Sphere, "sphere.gif");
		shapeIcons.put(ShapeType.Cube, "cube.gif");
		shapeIcons.put(ShapeType.Cylinder, "cylinder.gif");
		shapeIcons.put(ShapeType.Tube, "tube.gif");

		// Create the image descriptor from the file path
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL imagePath = BundleUtility.find(bundle,
				"icons/" + shapeIcons.get(shapeType));
		imageDescriptor = ImageDescriptor.createFromURL(imagePath);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Constructor for creating new ComplexShapes with a given OperatorType
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param view
	 *            <p>
	 *            The current ShapeTreeViewer
	 *            </p>
	 * @param operatorType
	 *            <p>
	 *            The type of ComplexShape to create with the action is
	 *            triggered
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ActionAddShape(ShapeTreeView view, OperatorType operatorType) {
		// begin-user-code

		this.view = view;
		this.shapeType = null;
		this.operatorType = operatorType;
		currentShapeId = 0;

		// Set the Action's name to "Add {ShapeType}"

		this.setText("Add " + operatorType.toString());

		// Create a map which stores the filenames of the icons, relative
		// to the icons/ directory

		Map<OperatorType, String> operatorIcons = new HashMap<OperatorType, String>();
		operatorIcons.put(OperatorType.Union, "union.gif");
		operatorIcons.put(OperatorType.Intersection, "intersection.gif");
		operatorIcons.put(OperatorType.Complement, "complement.gif");

		// Create the image descriptor from the file path

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL imagePath = BundleUtility.find(bundle,
				"icons/" + operatorIcons.get(operatorType));
		imageDescriptor = ImageDescriptor.createFromURL(imagePath);

		// end-user-code
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

		// Get the selection

		ITreeSelection selection = (ITreeSelection) view.treeViewer
				.getSelection();
		TreePath[] paths = selection.getPaths();

		// Fail silently if multiple items are selected

		if (paths.length > 1) {
			return;
		}
		// Get the GeometryComponent from the ShapeTreeView's TreeViewer

		GeometryComponent geometry = (GeometryComponent) view.treeViewer
				.getInput();

		if (geometry == null) {
			return;
		}
		// Get the parent shape, regardless of whether an IShape or BlankShape
		// is selected

		ComplexShape parentComplexShape = null;

		if (paths.length == 1) {

			// Get the selected object from the single selection

			Object selectedObject = paths[0].getLastSegment();

			if (selectedObject instanceof IShape) {

				// Get the selected shape's parent

				IShape selectedShape = (IShape) selectedObject;
				parentComplexShape = (ComplexShape) selectedShape.getParent();
			} else if (selectedObject instanceof BlankShape) {

				// Get the selected blank shape's parent

				BlankShape selectedBlank = (BlankShape) selectedObject;
				parentComplexShape = (ComplexShape) selectedBlank.getParent();
			}

		}

		// Add a child shape to either the GeometryComponent or the parent
		// ComplexShape

		IShape childShape = createShape();

		if (parentComplexShape == null) {

			// Add a new shape to the root GeometryComponent

			synchronized (geometry) {
				geometry.addShape(childShape);
			}

			view.treeViewer.refresh();
		}

		else {

			// Create a new shape and add it to the parentComplexShape

			synchronized (geometry) {
				parentComplexShape.addShape(childShape);
			}

			view.treeViewer.refresh(parentComplexShape);
		}

		// Expand the child in the tree if a ComplexShape was added

		if (childShape != null && operatorType != null) {
			view.treeViewer.expandToLevel(childShape, 1);
		}
		// end-user-code
	}

	/**
	 * Returns the appropriate image descriptor for the action's icon
	 * 
	 * @return The ImageDescriptor generated from the appropriate ShapeType or
	 *         OperatorType
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates a shape corresponding to this Action's ShapeType or OperatorType
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly created shape
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IShape createShape() {
		// begin-user-code

		AbstractShape shape = null;

		// Determine which type of shape should be created

		if (shapeType != null && operatorType == null) {

			// Instantiate a PrimitiveShape and set its name and ID

			shape = new PrimitiveShape(shapeType);

			currentShapeId++;
			shape.setName(shapeType.toString());
			shape.setId(currentShapeId);
		}

		else if (operatorType != null && shapeType == null) {

			// Instantiate a ComplexShape and set its name

			shape = new ComplexShape(operatorType);

			currentShapeId++;
			shape.setName(operatorType.toString());
			shape.setId(currentShapeId);
		}

		// Return the shape
		// If none of the conditions above passed, this will return null.

		return shape;

		// end-user-code
	}
}