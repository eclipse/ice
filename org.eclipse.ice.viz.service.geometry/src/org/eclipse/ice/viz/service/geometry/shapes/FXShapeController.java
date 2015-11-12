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
		// from the parent's JavaFX node.
		List<AbstractController> parentList = model
				.getEntitiesByCategory("Parent");
		if (!parentList.isEmpty()) {
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
					.remove(view.getRepresentation());
		}

		super.setParent(parent);
	}

}
