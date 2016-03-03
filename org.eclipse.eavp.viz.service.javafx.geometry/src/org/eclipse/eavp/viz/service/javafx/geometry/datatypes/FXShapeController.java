/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.geometry.shapes.GeometryMeshProperty;
import org.eclipse.eavp.viz.service.geometry.shapes.OperatorType;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IMeshCategory;
import org.eclipse.eavp.viz.service.modeling.IWireframeController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.Representation;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;

/**
 * A controller for Shapes which have been rendered in JavaFX
 * 
 * @author Robert Smith
 *
 */
public class FXShapeController extends ShapeController
		implements IWireframeController {

	/**
	 * THe nullary constructor
	 */
	public FXShapeController() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public FXShapeController(ShapeMesh model, BasicView view) {
		super(model, view);

		// Associate this controller with the node within the node's internal
		// data structures
		Representation<Group> representation = view.getRepresentation();
		representation.getData().getProperties().put(ShapeController.class,
				this);
	}

	/**
	 * Recursively refresh every descendant
	 */
	public void refreshRecursive() {

		// Queue all messages from recursive refreshing
		updateManager.enqueue();
		refresh();

		// Refresh for child
		for (IController child : model
				.getEntitiesFromCategory(MeshCategory.CHILDREN)) {
			((FXShapeController) child).refreshRecursive();
		}

		// Send updates for all the recursive refreshing
		updateManager.flushQueue();
	}

	/**
	 * Set the shape's default material. The shape is not guaranteed to actually
	 * display in this material after the function returns, as other materials
	 * may be taking precedence over the default one, such as the
	 * selectedMaterial when the shape is selected.
	 * 
	 * @param material
	 */
	public void setMaterial(PhongMaterial material) {
		((FXShapeView) view).setMaterial(material);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.Shape#removeEntity(org.eclipse.ice.
	 * viz.service.modeling.IController)
	 */
	@Override
	public void removeEntity(IController entity) {

		// If the removed entity is a parent FXShape, detach the child's JavaFX
		// node from the parent group
		Representation<Group> representation = entity.getRepresentation();
		if (model.getEntitiesFromCategory(MeshCategory.PARENT).contains(entity)
				&& representation.getData().getChildren()
						.contains(view.getRepresentation().getData())) {
			representation.getData().getChildren()
					.remove(view.getRepresentation().getData());
		}

		// Otherwise, remove its representation from this object's JavaFX node
		else {
			Representation<Group> viewRepresentation = view.getRepresentation();
			representation.getData().getChildren()
					.remove(representation.getData());
		}

		super.removeEntity(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.Shape#setParent(org.eclipse.eavp.
	 * viz. service.modeling.Shape)
	 */
	@Override
	public void setParent(IController parent) {

		// If the shape already has a parent, remove this shape's JavaFX node
		// from the parent's JavaFX node. Ignore this step for the root shape,
		// which has no associated node
		List<IController> parentList = model
				.getEntitiesFromCategory(MeshCategory.PARENT);

		if (!parentList.isEmpty()
				&& !"True".equals(parent.getProperty(MeshProperty.ROOT))) {
			Representation<Group> representation = parentList.get(0)
					.getRepresentation();
			Representation<Group> viewRepresentation = view.getRepresentation();
			representation.getData().getChildren()
					.remove(viewRepresentation.getData());
		}

		// For Union parents, add this part's JavaFX node as a child to the new
		// parent's JavaFX node.
		// TODO Implement other kinds of CSG relations
		String operator = parent.getProperty(GeometryMeshProperty.OPERATOR);
		if (operator != null && OperatorType.valueOf(parent.getProperty(
				GeometryMeshProperty.OPERATOR)) == OperatorType.Union) {

			// Get the parent's representation
			Representation<Group> parentRepresentation = parent
					.getRepresentation();

			// Get this shape's representation
			Representation<Group> viewRepresentation = view.getRepresentation();

			// Add the child node to the parent node
			parentRepresentation.getData().getChildren()
					.add(viewRepresentation.getData());
		}

		super.setParent(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.Shape#clone()
	 */
	@Override
	public Object clone() {
		// Create a new shape from clones of the model and view
		FXShapeController clone = new FXShapeController();

		// Copy any other data into the clone
		clone.copy(this);

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#copy(org. eclipse.
	 * ice.viz.service.modeling.IController)
	 */
	@Override
	public void copy(IController source) {

		// Create the model and view
		model = (BasicMesh) ((BasicMesh) source.getModel()).clone();
		view = new FXShapeView((ShapeMesh) model);
		view.copy(source.getView());
		view.refresh(model);

		// Give the model a reference to this
		model.setController(this);

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#addEntity(org.
	 * eclipse.ice.viz.service.modeling.IController)
	 */
	@Override
	public void addEntity(IController entity) {
		super.addEntity(entity);

		// Add the new child's JavaFX node as a child of this object's node
		Representation<Group> representation = view.getRepresentation();
		Representation<Group> childRepresentation = entity.getRepresentation();
		Group node = representation.getData();
		Group childNode = childRepresentation.getData();

		if (!node.getChildren().contains(childNode)) {
			node.getChildren().add(childNode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling. IController,
	 * java.lang.String)
	 */
	@Override
	public void addEntityToCategory(IController entity,
			IMeshCategory category) {
		super.addEntityToCategory(entity, category);

		// For children, add the new child's JavaFX node as a child of this
		// object's node
		Representation<Group> representation = view.getRepresentation();
		Representation<Group> childRepresentation = entity.getRepresentation();
		Group node = representation.getData();
		Group childNode = representation.getData();

		if (!node.getChildren().contains(childNode)) {
			node.getChildren().add(childNode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.IController#update(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable)
	 */
	@Override
	public void update(IManagedUpdateable component, SubscriptionType[] type) {

		// If the view updated, recursively refresh all children and propagate
		// the update to own listeners
		if (component == view) {
			refreshRecursive();
		}

		// Otherwise just propagate to own listeners
		super.update(component, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#getSubscriptions(org.eclipse.eavp.viz.
	 * service.datastructures.VizObject.IVizUpdateable)
	 */
	@Override
	public ArrayList<SubscriptionType> getSubscriptions(
			IManagedUpdateable source) {

		// Create a list of events to subscribe to
		ArrayList<SubscriptionType> types = new ArrayList<SubscriptionType>();

		// Listen only to new child events from the model
		if (source == model) {
			types.add(SubscriptionType.CHILD);
			types.add(SubscriptionType.SELECTION);
		}

		// Listen only to transformation events from the view
		else if (source == view) {
			types.add(SubscriptionType.TRANSFORMATION);
		}

		// For other objects, register for everything
		else {
			types.add(SubscriptionType.ALL);
		}
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.WireFramePart#setWireFrameMode(
	 * boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {
		((IWireframeController) view).setWireFrameMode(on);
	}
}
