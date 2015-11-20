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
package org.eclipse.ice.viz.service.mesh.datastructures;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.ice.viz.service.modeling.VertexComponent;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * A class which provides a JavaFX graphical representation for a Vertex.
 * 
 * @author Robert Smith
 *
 */
public class FXVertexView extends AbstractView {

	/**
	 * A group containing the shape which represents the part and a gizmo which
	 * modifies the shape's appearance
	 */
	private Group node;

	/** */
	private Sphere mesh;

	/** */
	// private TransformGizmo gizmo;

	/** */
	private PhongMaterial defaultMaterial;

	/** */
	private PhongMaterial selectedMaterial;

	/**
	 * The Material to display when the Vertex is first being made
	 */
	private PhongMaterial constructingMaterial;

	/** */
	private boolean selected;

	/**
	 * The nullary constructor.
	 */
	public FXVertexView() {
		super();

		// Instantiate the class variables
		node = new Group();

		// Create the materials
		defaultMaterial = new PhongMaterial(Color.rgb(127, 0, 127));
		defaultMaterial.setSpecularColor(Color.WHITE);
		selectedMaterial = new PhongMaterial(Color.rgb(0, 127, 255));
		selectedMaterial.setSpecularColor(Color.WHITE);
		constructingMaterial = new PhongMaterial(Color.rgb(0, 255, 0));
		constructingMaterial.setSpecularColor(Color.WHITE);

		// Set the sphere to be the constructing material by default
		mesh.setMaterial(constructingMaterial);

	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model which this view will display
	 */
	public FXVertexView(VertexComponent model) {
		this();

		// Set the node's name
		node.setId(model.getProperty("Name"));

		// Center the node on the vertex's location
		transformation.setTranslation(model.getX(), model.getY(), 0);

		// Set the node's transformation
		node.getTransforms().setAll(Util.convertTransformation(transformation));

		// Create a Shape3D for the model
		mesh = new Sphere(50);
		node.getChildren().add(mesh);

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
		node.getProperties().put(Shape.class, mesh);
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

		// Center the node on the vertex's location
		transformation.setTranslation(((VertexComponent) model).getX(),
				((VertexComponent) model).getY(), 0);

		// Set the node's transformation
		node.getTransforms().setAll(Util.convertTransformation(transformation));

		// If the vertex is under construction, leave the material unchanged,
		// otherwise set it based on whether or not the vertex is selected
		if (!"True".equals(model.getProperty("Constructing"))) {

			// Convert the model's selected property to a boolean
			Boolean newSelected = "True".equals(model.getProperty("Selected"));

			// If the selected property has changed, update
			if (selected != newSelected) {

				// Save the selected value
				selected = newSelected;

				if (selected) {

					// Set the material if selected
					mesh.setMaterial(selectedMaterial);
				} else {

					// Set the material if selected
					mesh.setMaterial(defaultMaterial);
				}
			}
		}

		else {
			mesh.setMaterial(constructingMaterial);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractView#clone()
	 */
	@Override
	public Object clone() {
		FXVertexView clone = new FXVertexView();
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
		notifyListeners();
	}
}
