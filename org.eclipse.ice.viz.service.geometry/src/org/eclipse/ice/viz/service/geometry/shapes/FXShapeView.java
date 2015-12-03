/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary, Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.geometry.shapes;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscription;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.javafx.internal.scene.TransformGizmo;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.ice.viz.service.modeling.ShapeComponent;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

/**
 * A class which creates and maintains the JavaFX graphical representation of a
 * Shape.
 * 
 * @author Tony McCrary, Robert Smith
 *
 */
public class FXShapeView extends AbstractView {

	/**
	 * A group containing the shape which represents the part and a gizmo which
	 * modifies the shape's appearance
	 */
	private Group node;

	/** */
	private Shape3D shape;

	/** */
	private TransformGizmo gizmo;

	/** */
	private PhongMaterial defaultMaterial;

	/** */
	private Material selectedMaterial = Util.DEFAULT_HIGHLIGHTED_MATERIAL;

	/** */
	private boolean selected;

	/**
	 * The nullary constructor.
	 */
	public FXShapeView() {
		super();

		// Instantiate the class variables
		node = new Group();
		gizmo = new TransformGizmo(0);
		gizmo.setVisible(false);
		node.getChildren().add(gizmo);

	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model which this view will display
	 */
	public FXShapeView(ShapeComponent model) {
		super();

		// Initialize the JavaFX ndoe
		node = new Group();
		node.setId(model.getProperty("Name"));

		// Set the node's transformation
		node.getTransforms().setAll(Util.convertTransformation(transformation));

		// Create a gizmo with axis for the root node
		if ("True".equals(model.getProperty("Root"))) {
			gizmo = new TransformGizmo(100);
			gizmo.showHandles(false);
		} else {
			gizmo = new TransformGizmo(0);
		}

		gizmo.setVisible(false);
		node.getChildren().add(gizmo);

		// Create a Shape3D for the model
		createShape(ShapeType.valueOf(model.getProperty("Type")));

	}

	/**
	 * Sets the representation to the appropriate type of Shape3D.
	 * 
	 * @param type
	 *            The type of shape to display
	 */
	private void createShape(ShapeType type) {

		// Fail silently for complex shapes
		if (type == null) {
			return;
		}

		// Whether this is the first time creating a shape
		boolean initial = (shape == null ? true : false);

		// The previous shape
		Shape3D prevShape = null;

		// Based on the type, check if the correct shape has already been made.
		// If not, create a new shape, set its materials, and save it as this
		// view's representation
		switch (type) {
		case Cone:
			return;
		case Cube:
			if (!(shape instanceof Box)) {
				// Save the old shape
				prevShape = shape;

				// Remove the old shape from the node
				node.getChildren().remove(shape);

				Box box = new Box(50, 50, 50);
				defaultMaterial = new PhongMaterial(Color.rgb(50, 50, 255));
				defaultMaterial.setSpecularColor(Color.WHITE);
				box.setMaterial(defaultMaterial);
				shape = box;
			}

			break;
		case Cylinder:
			if (!(shape instanceof Cylinder)) {
				// Save the old shape
				prevShape = shape;

				Cylinder cyl = new Cylinder(50, 50);

				defaultMaterial = new PhongMaterial(Color.rgb(0, 181, 255));
				defaultMaterial.setSpecularColor(Color.WHITE);
				cyl.setMaterial(defaultMaterial);
				shape = cyl;
			}

			break;
		case None:
			return;
		case Sphere:
			if (!(shape instanceof Sphere)) {
				// Save the old shape
				prevShape = shape;

				Sphere sphere = new Sphere(50, 50);

				defaultMaterial = new PhongMaterial(Color.rgb(131, 0, 157));
				defaultMaterial.setSpecularColor(Color.WHITE);
				sphere.setMaterial(defaultMaterial);
				shape = sphere;
			}

			break;
		case Tube:
			if (!(shape instanceof Cylinder)) {
				// Save the old shape
				prevShape = shape;

				Cylinder tube = new Cylinder(50, 50);

				defaultMaterial = new PhongMaterial(Color.rgb(0, 131, 157));
				defaultMaterial.setSpecularColor(Color.WHITE);
				tube.setMaterial(defaultMaterial);
				shape = tube;
			}

			break;
		default:
			return;
		}

		// If the function didn't return, a change has occurred. Replace the old
		// shape with the new shape in the group
		if (prevShape != null) {
			node.getChildren().remove(prevShape);
			node.getChildren().add(shape);
		}

		else if (initial) {
			node.getChildren().add(shape);
		}
	}

	/**
	 * Associates the view's controller with the representation's data
	 * structure, so that user interactions with the displayed shape will have a
	 * way be associated back to the controller.
	 * 
	 * @param controller
	 *            This view's controller
	 */
	public void setController(AbstractController controller) {

		// Put the controller in the node's data structure
		node.getProperties().put(Shape.class, shape);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Object getRepresentation() {
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#refresh(org.eclipse.ice
	 * .viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void refresh(AbstractMeshComponent model) {

		// Set the node's transformation
		node.getTransforms().setAll(Util.convertTransformation(transformation));

		// Complex shapes have nothing else to refresh, as their children will
		// handle their own views.
		if (model.getProperty("Operator") == null) {

			// Create the shape if neccesary
			createShape(ShapeType.valueOf(model.getProperty("Type")));

			// Convert the model's selected property to a boolean
			Boolean newSelected = "True".equals(model.getProperty("Selected"));

			// If the selected property has changed, update
			if (selected != newSelected) {

				// Save the selected value
				selected = newSelected;

				if (selected) {

					// Set the material and activate the gizmo if selected
					shape.setMaterial(selectedMaterial);
					gizmo.setVisible(true);
				} else {

					// Set the material and deactivate the gizmo if selected
					shape.setMaterial(defaultMaterial);
					gizmo.setVisible(false);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractView#clone()
	 */
	@Override
	public Object clone() {
		FXShapeView clone = new FXShapeView();
		clone.copy(this);
		clone.update(clone.transformation);
		return clone;
	}

	@Override
	public void update(IVizUpdateable component) {

		// If the transformation updated, update the JavaFX transformation
		if (component == transformation) {
			// Set the node's transformation
			node.getTransforms()
					.setAll(Util.convertTransformation(transformation));
		}

		// Notify own listeners of the change
		UpdateableSubscription[] eventTypes = {UpdateableSubscription.All};
		updateManager.notifyListeners(eventTypes);;
	}

}
