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

import org.eclipse.eavp.viz.service.geometry.shapes.OperatorType;
import org.eclipse.eavp.viz.service.geometry.shapes.ShapeType;
import org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeContentProvider.BlankShape;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.IControllerFactory;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * <p>
 * Eclipse UI view containing a toolbar and TreeViewer to manipulate the
 * structure and elements in a Constructive Solid Geometry (CSG) tree
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ShapeTreeView extends ViewPart
		implements ISelectionChangedListener {

	/**
	 * <p>
	 * The currently displayed GeometryComponent
	 * </p>
	 * 
	 */
	private AbstractController geometry;

	/**
	 * <p>
	 * The main TreeViewer occupying the entire space of the view
	 * </p>
	 * 
	 */
	TreeViewer treeViewer;

	/**
	 * The factory responsible for creating graphics program specific
	 * representations of the tree's contents.
	 */
	IControllerFactory factory;

	/**
	 * Eclipse view ID
	 */
	public static final String ID = "org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeView";

	/**
	 * A list of shapes of the last selection event
	 */
	private ArrayList<ShapeController> selectedShapes = new ArrayList<ShapeController>();

	// The actions for manipulating shapes
	private DropdownAction addPrimitiveShapes;
	private DropdownAction addComplexShapes;
	private Action duplicateShapes;
	private Action replicateShapes;
	private Action deleteShape;

	/**
	 * <p>
	 * Creates the SWT controls for this ShapeTreeView
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The parent Composite
	 *            </p>
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create the actions

		createActions();

		// Make a TreeViewer and add a content provider to it

		treeViewer = new TreeViewer(parent);

		ShapeTreeContentProvider contentProvider = new ShapeTreeContentProvider();
		treeViewer.setContentProvider(contentProvider);

		// Add label provider to TreeViewer

		ShapeTreeLabelProvider labelProvider = new ShapeTreeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);

		// Add selection listener to TreeViewer

		treeViewer.addSelectionChangedListener(this);

	}

	/**
	 * <p>
	 * Creates actions required for manipulating the ShapeTreeView and adds them
	 * to the view's toolbar
	 * </p>
	 * 
	 */
	private void createActions() {

		// Get the toolbar

		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbarManager = actionBars.getToolBarManager();

		// Create the add shapes menu managers

		addPrimitiveShapes = new DropdownAction("Add Primitives");
		addComplexShapes = new DropdownAction("Add Complex");

		// Add the PrimitiveShape actions

		Action addSphere = new ActionAddShape(this, ShapeType.Sphere);
		addPrimitiveShapes.addAction(addSphere);

		Action addCube = new ActionAddShape(this, ShapeType.Cube);
		addPrimitiveShapes.addAction(addCube);

		Action addCylinder = new ActionAddShape(this, ShapeType.Cylinder);
		addPrimitiveShapes.addAction(addCylinder);

		Action addTube = new ActionAddShape(this, ShapeType.Tube);
		addPrimitiveShapes.addAction(addTube);

		// Add the ComplexShape actions

		Action addUnion = new ActionAddShape(this, OperatorType.Union);
		addComplexShapes.addAction(addUnion);

		Action addIntersection = new ActionAddShape(this,
				OperatorType.Intersection);
		addIntersection.setEnabled(false);
		addComplexShapes.addAction(addIntersection);

		Action addComplement = new ActionAddShape(this,
				OperatorType.Complement);
		addComplement.setEnabled(false);
		addComplexShapes.addAction(addComplement);

		// Add the Duplicate Shapes action

		duplicateShapes = new ActionDuplicateShape(this);

		// Add the Replicate action

		replicateShapes = new ActionReplicateShape(this);

		// Add the DeleteShape action

		deleteShape = new ActionDeleteShape(this);

		// Add the top level menus to the toolbar
		toolbarManager.add(addPrimitiveShapes);
		toolbarManager.add(addComplexShapes);
		toolbarManager.add(duplicateShapes);
		toolbarManager.add(replicateShapes);
		toolbarManager.add(deleteShape);

	}

	/**
	 * 
	 * @param geometry
	 */
	public void setGeometry(AbstractController geometry) {

		this.geometry = geometry;

		// Set the TreeViewer's input

		this.treeViewer.setInput(geometry);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * Updates the disabled state of the action icons and the state of the
	 * TransformationView
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		// Get the current selection

		ITreeSelection selection = (ITreeSelection) event.getSelection();
		TreePath[] paths = selection.getPaths();

		// Get the TransformationView

		TransformationView transformationView = (TransformationView) getSite()
				.getPage().findView(TransformationView.ID);

		if (paths.length == 1) {

			// Only one item is selected

			Object selectedObject = paths[0].getLastSegment();

			if (selectedObject instanceof ShapeController) {
				ShapeController selectedShape = (ShapeController) selectedObject;

				// Set the TransformationView's shape

				if (transformationView != null) {
					transformationView.setShape(selectedShape);
				}
				// Enable/disable action buttons

				addPrimitiveShapes.setEnabled(true);
				addComplexShapes.setEnabled(true);
				duplicateShapes.setEnabled(true);
				replicateShapes.setEnabled(true);
				deleteShape.setEnabled(true);
			} else if (selectedObject instanceof BlankShape) {

				// Enable/disable action buttons

				addPrimitiveShapes.setEnabled(true);
				addComplexShapes.setEnabled(true);
				duplicateShapes.setEnabled(false);
				replicateShapes.setEnabled(false);
				deleteShape.setEnabled(false);

				// Set the TransformationView to a blank state

				if (transformationView != null) {
					transformationView.setShape(null);
				}
			}
		} else {

			// Multiple or zero items are selected

			if (transformationView != null) {
				transformationView.setShape(null);
			}
			if (paths.length > 1) {

				// Multiple items are selected.

				// Enable/disable action buttons

				addPrimitiveShapes.setEnabled(false);
				addComplexShapes.setEnabled(false);
				duplicateShapes.setEnabled(true);
				replicateShapes.setEnabled(false);
				deleteShape.setEnabled(true);
			} else {

				// Zero items are selected

				// Enable/disable action buttons

				addPrimitiveShapes.setEnabled(true);
				addComplexShapes.setEnabled(true);
				duplicateShapes.setEnabled(false);
				replicateShapes.setEnabled(false);
				deleteShape.setEnabled(false);
			}
		}

		// Edit the shapes' selection property

		for (ShapeController selectedShape : selectedShapes) {
			selectedShape.setProperty("Selected", "False");
		}

		// Update the list of last-selected shapes

		selectedShapes.clear();

		for (TreePath path : paths) {
			Object selectedObject = path.getLastSegment();

			// Only include IShapes, not ShapeTreeLabelProvider::BlankShapes

			if (selectedObject instanceof ShapeController) {

				ShapeController selectedShape = (ShapeController) selectedObject;
				selectedShape.setProperty("Selected", "True");
				selectedShapes.add(selectedShape);
			}
		}
	}

	/**
	 * Getter method for the factory.
	 * 
	 * @return The factory
	 */
	public IControllerFactory getFactory() {
		return factory;
	}

	/**
	 * Setter method for the factory
	 * 
	 * @param factory
	 *            The new factory to store in this view
	 */
	public void setFactory(IControllerFactory factory) {
		this.factory = factory;
	}
}
