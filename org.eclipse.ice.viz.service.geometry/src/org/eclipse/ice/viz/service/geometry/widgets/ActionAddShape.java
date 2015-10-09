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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.viz.service.geometry.shapes.AbstractShape;
import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
import org.eclipse.ice.viz.service.geometry.shapes.Geometry;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;
import org.eclipse.ice.viz.service.geometry.shapes.OperatorType;
import org.eclipse.ice.viz.service.geometry.shapes.PrimitiveShape;
import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.geometry.widgets.ShapeTreeContentProvider.BlankShape;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <p>
 * Action for adding a specific shape to the ShapeTreeView
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ActionAddShape extends Action {
    /**
     * <p>
     * The current ShapeTreeViewer associated with the AddShape action
     * </p>
     * 
     */
    private ShapeTreeView view;
    /**
     * <p>
     * The ShapeType used to create new PrimitiveShapes when the AddShape action
     * is triggered
     * </p>
     * <p>
     * If the value is null, the Operator is used to create ComplexShapes.
     * </p>
     * 
     */
    private ShapeType shapeType;
    /**
     * <p>
     * The OperatorType used to create new ComplexShapes when the AddShape
     * action is triggered
     * </p>
     * <p>
     * If the value is null, the ShapeType is used to create PrimitiveShapes.
     * </p>
     * 
     */
    private OperatorType operatorType;

    /**
     * <p>
     * The current used default shape number appended to the name of every newly
     * created shape
     * </p>
     * <p>
     * Starting from zero, this number increments every time a new shape is
     * added to the tree.
     * </p>
     * 
     */
    private int currentShapeId;

    /**
     * The image to display as the action's icon
     */
    private ImageDescriptor imageDescriptor;

    /**
     * <p>
     * Constructor for creating new PrimitiveShapes with a given ShapeType
     * </p>
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
     */
    public ActionAddShape(ShapeTreeView view, ShapeType shapeType) {

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
        URL imagePath = BundleUtility.find(bundle, "icons/" + shapeIcons.get(shapeType));
        imageDescriptor = ImageDescriptor.createFromURL(imagePath);

        return;
    }

    /**
     * <p>
     * Constructor for creating new ComplexShapes with a given OperatorType
     * </p>
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
     */
    public ActionAddShape(ShapeTreeView view, OperatorType operatorType) {

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
        URL imagePath = BundleUtility.find(bundle, "icons/" + operatorIcons.get(operatorType));
        imageDescriptor = ImageDescriptor.createFromURL(imagePath);

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

        // Get the selection

        ITreeSelection selection = (ITreeSelection) view.treeViewer.getSelection();
        TreePath[] paths = selection.getPaths();

        // Fail silently if multiple items are selected

        if (paths.length > 1) {
            return;
        }
        // Get the GeometryComponent from the ShapeTreeView's TreeViewer

        Geometry geometry = (Geometry) view.treeViewer.getInput();

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
     * <p>
     * Creates a shape corresponding to this Action's ShapeType or OperatorType
     * </p>
     * 
     * @return
     *         <p>
     *         The newly created shape
     *         </p>
     */
    public IShape createShape() {

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

    }
}