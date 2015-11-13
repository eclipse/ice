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
package org.eclipse.ice.viz.service.geometry.shapes;

import java.util.List;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;

import javafx.scene.Group;

/**
 * A controller for Shapes which have been rendered in JavaFX
 * 
 * @author r8s
 *
 */
public class FXShapeController extends Shape {

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
	public FXShapeController(ShapeComponent model, AbstractView view) {
		super(model, view);

		// Associate this controller with the node within the node's internal
		// data structures
		((Group) view.getRepresentation()).getProperties().put(Shape.class,
				this);
	}

	/**
	 * Recursively refresh every descendant
	 */
	public void refreshRecursive() {

		notifyLock.set(true);
		refresh();

		// Refresh for child
		for (AbstractController child : model
				.getEntitiesByCategory("Children")) {
			((FXShapeController) child).refreshRecursive();
		}

		notifyLock.set(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.Shape#removeEntity(org.eclipse.ice.
	 * viz.service.modeling.AbstractController)
	 */
	@Override
	public void removeEntity(AbstractController entity) {

		// If the removed entity is a parent FXShape, detach the child's JavaFX
		// node from the parent group
		if (model.getEntitiesByCategory("Parent").contains(entity)
				&& entity instanceof FXShapeController) {
			((Group) entity.getRepresentation()).getChildren()
					.remove(view.getRepresentation());
		}

		super.removeEntity(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.Shape#setParent(org.eclipse.ice.viz.
	 * service.modeling.Shape)
	 */
	@Override
	public void setParent(Shape parent) {

		// If the shape already has a parent, remove this shape's JavaFX node
		// from the parent's JavaFX node. Ignore this step for the root shape,
		// which has no associated node
		List<AbstractController> parentList = model
				.getEntitiesByCategory("Parent");
		if (!parentList.isEmpty()
				&& !"True".equals(parent.getProperty("Root"))) {
			((Group) parentList.get(0).getRepresentation()).getChildren()
					.remove(view.getRepresentation());
		}

		// For Union parents, add this part's JavaFX node as a child to the new
		// parent's JavaFX node.
		// TODO Implement other kinds of CSG relations
		String operator = parent.getProperty("Operator");
		if (operator != null && OperatorType.valueOf(
				parent.getProperty("Operator")) == OperatorType.Union) {
			((Group) parent.getRepresentation()).getChildren()
					.add((Group) view.getRepresentation());
		}

		super.setParent(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.Shape#clone()
	 */
	@Override
	public Object clone() {
		// Create a new shape from clones of the model and view
		FXShapeController clone = new FXShapeController();

		// Copy any other data into the clone
		clone.copy(this);

		// Add each child shape's JavaFX node as a child to the clone's JavaFX
		// node
		for (AbstractController child : clone
				.getEntitiesByCategory("Children")) {
			((Group) clone.getRepresentation()).getChildren()
					.add((Group) child.getRepresentation());
		}

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractController#copy(org.eclipse.
	 * ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void copy(AbstractController source) {

		// Create the model and give it a reference to this
		model = new ShapeComponent();
		model.setController(this);

		// For simple objects, copy the model and create a new view based on the
		// copy
		if (source.getProperty("Operator") == null) {
			model.copy(source.getModel());
			view = new FXShapeView((ShapeComponent) model);
			view.copy(source.getView());
		}

		// If the object is complex, create the view first so that cloned
		// children can be collected under this object's JavaFX node.
		else {
			view = new FXShapeView();
			model.copy(source.getModel());
			view.copy(source.getView());
		}

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractController#addEntity(org.
	 * eclipse.ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void addEntity(AbstractController entity) {
		super.addEntity(entity);

		// Add the new child's JavaFX node as a child of this object's node
		Group node = (Group) view.getRepresentation();
		Group childNode = (Group) entity.getRepresentation();

		if (!node.getChildren().contains(childNode)) {
			node.getChildren().add(childNode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractController#
	 * addEntityByCategory(org.eclipse.ice.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController entity,
			String category) {
		super.addEntityByCategory(entity, category);

		// For children, add the new child's JavaFX node as a child of this
		// object's node
		Group node = (Group) view.getRepresentation();
		Group childNode = (Group) entity.getRepresentation();

		if (!node.getChildren().contains(childNode)) {
			node.getChildren().add(childNode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractController#update(org.
	 * eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {

		// If the view updated, recursively refresh all children
		if (component == view) {
			refreshRecursive();
		}

		super.update(component);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// org.eclipse.ice.viz.service.modeling.AbstractController#copy(org.eclipse.
	// * ice.viz.service.modeling.AbstractController)
	// */
	// @Override
	// public void copy(AbstractController source) {
	// super.copy(source);
	//
	// List<AbstractController> parentList = model
	// .getEntitiesByCategory("Parent");
	// if (!parentList.isEmpty()) {
	// parentList.get(0).addEntity(this);
	//
	// // AbstractController parent = model.getEntitiesByCategory("Parent")
	// // .get(0);
	// // String operator = parent.getProperty("Operator");
	// // if (operator != null && OperatorType.valueOf(
	// // parent.getProperty("Operator")) == OperatorType.Union) {
	// // ((Group) parent.getRepresentation()).getChildren()
	// // .add((Group) view.getRepresentation());
	// // }
	// }
	//
	// for (AbstractController shape : model
	// .getEntitiesByCategory("Children")) {
	// model.addEntity((AbstractController) shape.clone());
	// }
	//
	// }

}
